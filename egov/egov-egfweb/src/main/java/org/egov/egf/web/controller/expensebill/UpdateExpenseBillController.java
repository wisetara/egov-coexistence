/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency, transparency,
 *    accountability and the service delivery of the government organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.egf.web.controller.expensebill;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.commons.service.CheckListService;
import org.egov.egf.expensebill.repository.DocumentUploadRepository;
import org.egov.egf.expensebill.service.ExpenseBillService;
import org.egov.egf.utils.FinancialUtils;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.microservice.models.Department;
import org.egov.infra.microservice.utils.MicroserviceUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.models.EgChecklists;
import org.egov.model.bills.DocumentUpload;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/expensebill")
public class UpdateExpenseBillController extends BaseBillController {

    private static final String NET_PAYABLE_AMOUNT = "netPayableAmount";

    private static final String APPROVAL_DESIGNATION = "approvalDesignation";

    private static final String EG_BILLREGISTER = "egBillregister";

    private static final String APPROVAL_POSITION = "approvalPosition";

    private static final String EXPENSEBILL_VIEW = "expensebill-view";

    private static final String NET_PAYABLE_ID = "netPayableId";
    @Autowired
    private DocumentUploadRepository documentUploadRepository;
    @Autowired
    private ExpenseBillService expenseBillService;
    @Autowired
    @Qualifier("chartOfAccountsService")
    private ChartOfAccountsService chartOfAccountsService;
    @Autowired
    private FinancialUtils financialUtils;
    @Autowired
    private CheckListService checkListService;
    @Autowired
    private MicroserviceUtils microServiceUtil;

    public UpdateExpenseBillController(final AppConfigValueService appConfigValuesService) {
        super(appConfigValuesService);
    }

    @ModelAttribute(EG_BILLREGISTER)
    public EgBillregister getEgBillregister(@PathVariable String billId) {
    	if(billId.contains("showMode")){
    		String[] billIds = billId.split("\\&");
    		billId = billIds[0];
    	}
        return expenseBillService.getById(Long.parseLong(billId));
    }

    @RequestMapping(value = "/update/{billId}", method = RequestMethod.GET)
    public String updateForm(final Model model, @PathVariable final String billId,
                             final HttpServletRequest request) throws ApplicationException {
        final EgBillregister egBillregister = expenseBillService.getById(Long.parseLong(billId));
        final List<DocumentUpload> documents = documentUploadRepository.findByObjectId(Long.valueOf(billId));
        egBillregister.setDocumentDetail(documents);
        List<Map<String, Object>> budgetDetails = null;
        setDropDownValues(model);
        model.addAttribute("stateType", egBillregister.getClass().getSimpleName());
        if (egBillregister.getState() != null)
            model.addAttribute("currentState", egBillregister.getState().getValue());
        model.addAttribute("workflowHistory",
                financialUtils.getHistory(egBillregister.getState(), egBillregister.getStateHistory()));

        prepareWorkflow(model, egBillregister, new WorkflowContainer());
        egBillregister.getBillDetails().addAll(egBillregister.getEgBilldetailes());
        prepareBillDetailsForView(egBillregister);
        expenseBillService.validateSubledgeDetails(egBillregister);
        final List<CChartOfAccounts> expensePayableAccountList = chartOfAccountsService
                .getNetPayableCodes();
        for (final EgBilldetails details : egBillregister.getBillDetails())
            if (expensePayableAccountList != null && !expensePayableAccountList.isEmpty()
                    && expensePayableAccountList.contains(details.getChartOfAccounts())) {
                model.addAttribute(NET_PAYABLE_ID, details.getChartOfAccounts().getId());
                model.addAttribute(NET_PAYABLE_AMOUNT, details.getCreditamount());
            }
        prepareCheckListForEdit(egBillregister, model);

        String department= this.getDepartmentName(egBillregister.getEgBillregistermis().getDepartmentcode());

       if(department!=null)
           egBillregister.getEgBillregistermis().setDepartmentName(department);
        model.addAttribute(EG_BILLREGISTER, egBillregister);
        if (egBillregister.getState() != null
                && (FinancialConstants.WORKFLOW_STATE_REJECTED.equals(egBillregister.getState().getValue())
                || financialUtils.isBillEditable(egBillregister.getState()))) {
            model.addAttribute("mode", "edit");
            return "expensebill-update";
        } else {
            model.addAttribute("mode", "view");
            if (egBillregister.getEgBillregistermis().getBudgetaryAppnumber() != null && !egBillregister.getEgBillregistermis().getBudgetaryAppnumber().isEmpty()) {
                budgetDetails = expenseBillService.getBudgetDetailsForBill(egBillregister);
            }

            model.addAttribute("budgetDetails", budgetDetails);
            return EXPENSEBILL_VIEW;
        }
    }

    @RequestMapping(value = "/update/{billId}", method = RequestMethod.POST)
    public String update(@ModelAttribute(EG_BILLREGISTER) final EgBillregister egBillregister,
                         final BindingResult resultBinder, final RedirectAttributes redirectAttributes, final Model model,
                         final HttpServletRequest request, @RequestParam final String workFlowAction)
            throws ApplicationException, IOException {

        String mode = "";
        EgBillregister updatedEgBillregister = null;

        if (request.getParameter("mode") != null)
            mode = request.getParameter("mode");

        Long approvalPosition = 0l;
        String approvalComment = "";
        String apporverDesignation = "";

        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");

        if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));

        if ((approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
                && request.getParameter(APPROVAL_POSITION) != null
                && !request.getParameter(APPROVAL_POSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));
        if(request.getParameter(APPROVAL_DESIGNATION) != null && !request.getParameter(APPROVAL_DESIGNATION).isEmpty())
            apporverDesignation = String.valueOf(request.getParameter(APPROVAL_DESIGNATION));

        if (egBillregister.getState() != null
                && (FinancialConstants.WORKFLOW_STATE_REJECTED.equals(egBillregister.getState().getValue())
                || financialUtils.isBillEditable(egBillregister.getState()))) {
            populateBillDetails(egBillregister);
            validateBillNumber(egBillregister, resultBinder);
            validateLedgerAndSubledger(egBillregister, resultBinder);
        }
        if (resultBinder.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute("stateType", egBillregister.getClass().getSimpleName());
            prepareWorkflow(model, egBillregister, new WorkflowContainer());
            model.addAttribute(APPROVAL_DESIGNATION, request.getParameter(APPROVAL_DESIGNATION));
            model.addAttribute(APPROVAL_POSITION, request.getParameter(APPROVAL_POSITION));
            model.addAttribute(NET_PAYABLE_ID, request.getParameter(NET_PAYABLE_ID));
            model.addAttribute(NET_PAYABLE_AMOUNT, request.getParameter(NET_PAYABLE_AMOUNT));
            model.addAttribute("designation", request.getParameter("designation"));
            if (egBillregister.getState() != null
                    && (FinancialConstants.WORKFLOW_STATE_REJECTED.equals(egBillregister.getState().getValue())
                    || financialUtils.isBillEditable(egBillregister.getState()))) {
                prepareValidActionListByCutOffDate(model);
                model.addAttribute("mode", "edit");
                return "expensebill-update";
            } else {
                model.addAttribute("mode", "view");
                return EXPENSEBILL_VIEW;
            }
        } else {
            try {
                if (null != workFlowAction)
                    updatedEgBillregister = expenseBillService.update(egBillregister, approvalPosition, approvalComment, null,
                            workFlowAction, mode,apporverDesignation);
            } catch (final ValidationException e) {
                setDropDownValues(model);
                model.addAttribute("stateType", egBillregister.getClass().getSimpleName());
                prepareWorkflow(model, egBillregister, new WorkflowContainer());
                model.addAttribute(APPROVAL_DESIGNATION, request.getParameter(APPROVAL_DESIGNATION));
                model.addAttribute(APPROVAL_POSITION, request.getParameter(APPROVAL_POSITION));
                model.addAttribute(NET_PAYABLE_ID, request.getParameter(NET_PAYABLE_ID));
                model.addAttribute(NET_PAYABLE_AMOUNT, request.getParameter(NET_PAYABLE_AMOUNT));
                model.addAttribute("designation", request.getParameter("designation"));
                if (egBillregister.getState() != null
                        && (FinancialConstants.WORKFLOW_STATE_REJECTED.equals(egBillregister.getState().getValue())
                        || financialUtils.isBillEditable(egBillregister.getState()))) {
                    prepareValidActionListByCutOffDate(model);
                    model.addAttribute("mode", "edit");
                    return "expensebill-update";
                } else {
                    model.addAttribute("mode", "view");
                    return EXPENSEBILL_VIEW;
                }
            }

            redirectAttributes.addFlashAttribute(EG_BILLREGISTER, updatedEgBillregister);

            // For Get Configured ApprovalPosition from workflow history
            if (approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
                approvalPosition = expenseBillService.getApprovalPositionByMatrixDesignation(
                        egBillregister, null, mode, workFlowAction);

            final String approverName = String.valueOf(request.getParameter("approverName"));
            final String approverDetails = financialUtils.getApproverDetails(workFlowAction,
                    updatedEgBillregister.getState(), updatedEgBillregister.getId(), approvalPosition,approverName);

            return "redirect:/expensebill/success?approverDetails=" + approverDetails + "&billNumber="
                    + updatedEgBillregister.getBillnumber();
        }
    }

    @RequestMapping(value = "/view/{billId}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable String billId,
                       final HttpServletRequest request) throws ApplicationException {
    	if(billId.contains("showMode")){
    		String[] billIds = billId.split("\\&");
    		billId = billIds[0];
    	}
        final EgBillregister egBillregister = expenseBillService.getById(Long.parseLong(billId));
        setDropDownValues(model);
        egBillregister.getBillDetails().addAll(egBillregister.getEgBilldetailes());
        model.addAttribute("mode", "readOnly");
        prepareBillDetailsForView(egBillregister);
        prepareCheckList(egBillregister);
        final List<CChartOfAccounts> expensePayableAccountList = chartOfAccountsService
                .getNetPayableCodesByAccountDetailType(0);
        for (final EgBilldetails details : egBillregister.getBillDetails())
            if (expensePayableAccountList != null && !expensePayableAccountList.isEmpty()
                    && expensePayableAccountList.contains(details.getChartOfAccounts()))
                model.addAttribute(NET_PAYABLE_AMOUNT, details.getCreditamount());
        model.addAttribute(EG_BILLREGISTER, egBillregister);
        return EXPENSEBILL_VIEW;
    }

    private void prepareCheckList(final EgBillregister egBillregister) {
        final List<EgChecklists> checkLists = checkListService.getByObjectId(egBillregister.getId());
        egBillregister.getCheckLists().addAll(checkLists);
    }

    private void prepareCheckListForEdit(final EgBillregister egBillregister, final Model model) {
        final List<EgChecklists> checkLists = checkListService.getByObjectId(egBillregister.getId());
        egBillregister.getCheckLists().addAll(checkLists);
        final StringBuilder selectedCheckList = new StringBuilder();
        for (final EgChecklists checkList : egBillregister.getCheckLists()) {
            selectedCheckList.append(checkList.getAppconfigvalue().getId());
            selectedCheckList.append("-");
            selectedCheckList.append(checkList.getChecklistvalue());
            selectedCheckList.append(",");
        }
        if (!checkLists.isEmpty())
            model.addAttribute("selectedCheckList", selectedCheckList.toString().substring(0, selectedCheckList.length() - 1));
    }

    @Override
    protected void setDropDownValues(final Model model) {
        super.setDropDownValues(model);
    }

    private String getDepartmentName(String departmentCode){

        List<Department> deptlist = this.masterDataCache.get("egi-department");
        String departmentName = null;

        if(null!=deptlist && !deptlist.isEmpty()){

        List<Department> dept =    deptlist.stream()
                                .filter(department->departmentCode.equalsIgnoreCase(department.getCode()))
                                .collect(Collectors.toList());
        if(null!=dept && dept.size()>0)
            departmentName = dept.get(0).getName();
        }

        if(null==departmentName){
            Department dept = this.microServiceUtil.getDepartmentByCode(departmentCode);
            if(null!= dept)
                departmentName= dept.getName();
        }

        return departmentName;
    }

}
