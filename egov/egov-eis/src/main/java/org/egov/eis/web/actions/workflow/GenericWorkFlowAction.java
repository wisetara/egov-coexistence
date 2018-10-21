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
package org.egov.eis.web.actions.workflow;

import org.egov.infra.microservice.models.Department;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Generic WorkFlow Action. Can be extended by any action class that intends to provide Work flow functionality.
 *
 * @author subhash
 */
public abstract class GenericWorkFlowAction extends BaseFormAction {

    private static final long serialVersionUID = 1L;
    public final static String FORWARD = "Forward";
    protected CustomizedWorkFlowService customizedWorkFlowService;
    protected String workFlowAction;
    protected String approverComments;
    protected String currentState;
    protected String currentDesignation;
    protected String additionalRule;
    protected BigDecimal amountRule;
    protected String workFlowDepartment;
    protected String pendingActions;
    protected String approverName;
    protected String approverDepartment;
    protected String approverDesignation;
    protected Long approverPositionId;

    @Override
    public abstract StateAware getModel();

    @Autowired
    protected EgovMasterDataCaching masterDataCache;

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("approverDepartmentList", masterDataCache.get("egi-department"));
        addDropdownData("approverList", Collections.EMPTY_LIST);
        addDropdownData("designationList", Collections.EMPTY_LIST);
    }

    /**
     * Implementations must override this method based on their object's value that needs to be used in workflow
     *
     * @return the value that needs to be compared in the Amount rule table against FromAmount and ToAmount
     */

    protected BigDecimal getAmountRule() {
        return null;
    }

    /**
     * Implementations must override this method to get additional rule for workflow.
     *
     * @return the value that needs to be compared in the matrix table against Additional rule
     */

    protected String getAdditionalRule() {
        return null;
    }

    /**
     * Implementations must override this method to achieve department wise workflow.
     *
     * @return the value that needs to be compared in the matrix table against Department.
     */

    protected String getWorkFlowDepartment() {
        return null;
    }

    /**
     * Used to get valid actions that needs to be performed Based on these value workflow buttons will be rendered
     */

    public List<String> getValidActions() {
        List<String> validActions = Collections.emptyList();
        if (null == getModel() || null == getModel().getId() || getModel().getCurrentState() == null
                || getModel().getCurrentState().getValue().endsWith("NEW")
                || (getModel() != null && getModel().getCurrentState() != null ? getModel().getCurrentState().getValue()
                        .equals("Closed")
                        || getModel().getCurrentState().getValue().equals("END") : false)) {

            validActions = Arrays.asList(FORWARD);
        } else {
            if (getModel().getCurrentState() != null) {
                validActions = this.customizedWorkFlowService.getNextValidActions(getModel()
                        .getStateType(), getWorkFlowDepartment(), getAmountRule(),
                        getAdditionalRule(), getModel().getCurrentState().getValue(),
                        getPendingActions(), getModel().getCreatedDate());
            }
        }
        return validActions;
    }

    /**
     * Used to get next action If the nextAction value is END then approval Information won't be shown on the UI.
     */
    public String getNextAction() {
        WorkFlowMatrix wfMatrix = null;
        if (getModel().getId() != null) {
            if (getModel().getCurrentState() != null) {
                wfMatrix = this.customizedWorkFlowService.getWfMatrix(getModel().getStateType(),
                        getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(), getModel()
                                .getCurrentState().getValue(),
                        getPendingActions(), getModel()
                                .getCreatedDate());
            } else {
                wfMatrix = this.customizedWorkFlowService.getWfMatrix(getModel().getStateType(),
                        getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
                        State.DEFAULT_STATE_VALUE_CREATED, getPendingActions(), getModel()
                                .getCreatedDate());
            }
        }
        return wfMatrix == null ? "" : wfMatrix.getNextAction();
    }

    public void setCustomizedWorkFlowService(
            final CustomizedWorkFlowService customizedWorkFlowService) {
        this.customizedWorkFlowService = customizedWorkFlowService;
    }

    /**
     * Used to Set actionValue that will be used to call workflow script.
     *
     * @param workFlowAction
     */

    public void setWorkFlowAction(final String workFlowAction) {
        this.workFlowAction = workFlowAction;
    }

    /**
     * This parameter is used to get matrix object Implementations must override this method to get pendingActions
     *
     * @return the value needs to be compared against matrix table pendingActions
     */

    protected String getPendingActions() {
        return null;
    }

    public String getApproverComments() {
        return this.approverComments;
    }

    public void setApproverComments(final String approverComments) {
        this.approverComments = approverComments;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getCurrentDesignation() {
        return currentDesignation;
    }

    public void setCurrentDesignation(String currentDesignation) {
        this.currentDesignation = currentDesignation;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getApproverDepartment() {
        return approverDepartment;
    }

    public void setApproverDepartment(String approverDepartment) {
        this.approverDepartment = approverDepartment;
    }

    public String getApproverDesignation() {
        return approverDesignation;
    }

    public void setApproverDesignation(String approverDesignation) {
        this.approverDesignation = approverDesignation;
    }

    public Long getApproverPositionId() {
        return approverPositionId;
    }

    public void setApproverPositionId(Long approverPositionId) {
        this.approverPositionId = approverPositionId;
    }

    public String getWorkFlowAction() {
        return workFlowAction;
    }

    public void setAdditionalRule(String additionalRule) {
        this.additionalRule = additionalRule;
    }

    public void setAmountRule(BigDecimal amountRule) {
        this.amountRule = amountRule;
    }

    public void setWorkFlowDepartment(String workFlowDepartment) {
        this.workFlowDepartment = workFlowDepartment;
    }

    public void setPendingActions(String pendingActions) {
        this.pendingActions = pendingActions;
    }

}
