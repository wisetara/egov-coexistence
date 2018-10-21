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
package org.egov.collection.web.actions.reports;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.service.CollectionReportHeadWiseService;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.reporting.engine.ReportDataSourceType;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Action class for the cash collection summary report
 */
@ParentPackage("egov")
@Results({ @Result(name = CollectionSummaryHeadWiseAction.INDEX, location = "collectionSummaryHeadWise-index.jsp"),
        @Result(name = CollectionSummaryHeadWiseAction.REPORT, location = "collectionSummaryHeadWise-report.jsp") })
public class CollectionSummaryHeadWiseAction extends ReportFormAction {

    private static final long serialVersionUID = 1L;

    private static final String EGOV_FROM_DATE = "EGOV_FROM_DATE";
    private static final String EGOV_TO_DATE = "EGOV_TO_DATE";
    private static final String EGOV_PAYMENT_MODE = "EGOV_PAYMENT_MODE";
    private static final String COLLECTION_SUMMARY_TEMPLATE = "collection_summary_headwise";
    private static final String EGOV_SOURCE = "EGOV_SOURCE";
    private static final String EGOV_STATUS = "EGOV_STATUS";
    private static final String EGOV_GLCODE_NAME = "EGOV_GLCODE_NAME";
    private static final String EGOV_GLCODE_ID = "EGOV_GLCODE_ID";
    private static final String EGOV_BRANCH_NAME = "EGOV_BRANCH_NAME";

    private Integer statusId;
    private Long revenueId;
    private String glCode;
    private final Map<String, String> paymentModes = createPaymentModeList();
    private final Map<String, String> sources = createSourceList();
    private CollectionsUtil collectionsUtil;
    @Autowired
    private CollectionReportHeadWiseService reportService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    private Integer branchId = -1;

    @Autowired
    private CityService cityService;

    /**
     * @return the payment mode list to be shown to user in criteria screen
     */
    private Map<String, String> createPaymentModeList() {
        final Map<String, String> paymentModesMap = new HashMap<String, String>(0);
        paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CASH, CollectionConstants.INSTRUMENTTYPE_CASH);
        paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD,
                CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD);
        paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_ONLINE, CollectionConstants.INSTRUMENTTYPE_ONLINE);
        paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CARD, CollectionConstants.INSTRUMENTTYPE_CARD);
        return paymentModesMap;
    }

    private Map<String, String> createSourceList() {
        final Map<String, String> sourcesMap = new HashMap<String, String>(0);
        sourcesMap.put(Source.APONLINE.toString(), Source.APONLINE.toString());
        sourcesMap.put(Source.ESEVA.toString(), Source.ESEVA.toString());
        sourcesMap.put(Source.MEESEVA.toString(), Source.MEESEVA.toString());
        sourcesMap.put(Source.SYSTEM.toString(), Source.SYSTEM.toString());
        return sourcesMap;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infra.web.struts.actions.BaseFormAction#prepare()
     */
    @Override
    public void prepare() {
        setReportFormat(ReportFormat.PDF);
        setDataSourceType(ReportDataSourceType.JAVABEAN);
        addDropdownData("bankBranchList", collectionsUtil.getBankCollectionBankBranchList());
    }

    /**
     * Action method for criteria screen
     *
     * @return index
     */
    @Override
    @Action(value = "/reports/collectionSummaryHeadWise-criteria")
    public String criteria() {
        // Setup drop down data for department list
        addRelatedEntity("department", Department.class, "name");
        addDropdownData("servicetypeList", collectionsUtil.getBillingServiceList());
        setupDropdownDataExcluding();

        // Set default values of criteria fields
        setReportParam(EGOV_FROM_DATE, new Date());
        setReportParam(EGOV_TO_DATE, new Date());
        addDropdownData("receiptStatuses",
                getPersistenceService().findAllByNamedQuery(CollectionConstants.STATUS_OF_RECEIPTS));
        addDropdownData("revenueHeads",
                chartOfAccountsHibernateDAO.getActiveAccountsForTypes(CollectionConstants.REVENUEHEADS));
        return INDEX;
    }

    @Override
    @Action(value = "/reports/collectionSummaryHeadWise-report")
    public String report() {
        /*
         * if (getServiceId() != null && getServiceId() != -1) { ServiceDetails serviceDets = (ServiceDetails)
         * entityManager.find(ServiceDetails.class, getServiceId()); setServiceName(serviceDets.getName()); }
         */
        if (getStatusId() != -1) {
            final EgwStatus statusObj = entityManager.find(EgwStatus.class, getStatusId());
            setStatusName(statusObj.getDescription());
        }

        if (getRevenueId() != -1) {
            final CChartOfAccounts statusObj = entityManager.find(CChartOfAccounts.class, getRevenueId());
            setGlCodeName(statusObj.getName());
            setGlCode(statusObj.getGlcode());
        }
        if (getBranchId() != null && getBranchId() != -1) {
            final Bankbranch bankbranch = entityManager.find(Bankbranch.class, getBranchId());
            String brankname = bankbranch.getBank().getName() + "-" + bankbranch.getBranchname();
            setBranchName(brankname);
        } else {
            setBranchName("All Bank Branch");
        }
        setReportParam(CollectionConstants.LOGO_PATH, cityService.getCityLogoAsStream());
        setReportData(reportService.getCollectionSummaryReport(getFromDate(), getToDate(), getPaymentMode(),
                getSource(), getGlCode(), getStatusId(), getBranchId()));
        return super.report();
    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    /**
     * @return the from date
     */
    public Date getFromDate() {
        return (Date) getReportParam(EGOV_FROM_DATE);
    }

    /**
     * @param fromDate the from date to set
     */
    public void setFromDate(final Date fromDate) {
        setReportParam(EGOV_FROM_DATE, fromDate);
    }

    /**
     * @return the do date
     */
    public Date getToDate() {
        return (Date) getReportParam(EGOV_TO_DATE);
    }

    /**
     * @param toDate the to date to set
     */
    public void setToDate(final Date toDate) {
        setReportParam(EGOV_TO_DATE, toDate);
    }

    /**
     * @return the payment mode (cash/cheque)
     */
    public String getPaymentMode() {
        return (String) getReportParam(EGOV_PAYMENT_MODE);
    }

    /**
     * @param paymentMode the payment mode to set (cash/cheque)
     */
    public void setPaymentMode(final String paymentMode) {
        setReportParam(EGOV_PAYMENT_MODE, paymentMode);
    }

    /**
     * @return the payment modes
     */
    public Map<String, String> getPaymentModes() {
        return paymentModes;
    }

    /**
     * @return the do date
     */
    public String getSource() {
        return (String) getReportParam(EGOV_SOURCE);
    }

    public void setSource(final String source) {
        setReportParam(EGOV_SOURCE, source);
    }

    public Map<String, String> getSources() {
        return sources;
    }

    @Override
    protected String getReportTemplateName() {
        return COLLECTION_SUMMARY_TEMPLATE;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(final int statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return (String) getReportParam(EGOV_STATUS);
    }

    public void setStatusName(final String statusName) {
        setReportParam(EGOV_STATUS, statusName);
    }

    public Long getGlCodeId() {
        return (Long) getReportParam(EGOV_GLCODE_ID);
    }

    public void setGlCodeId(final String glCodeId) {
        setReportParam(EGOV_GLCODE_ID, glCodeId);
    }

    public String getGlCodeName() {
        return (String) getReportParam(EGOV_GLCODE_ID);
    }

    public void setGlCodeName(final String glCodeName) {
        setReportParam(EGOV_GLCODE_NAME, glCodeName);
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(final String glCode) {
        this.glCode = glCode;
    }

    public Long getRevenueId() {
        return revenueId;
    }

    public void setRevenueId(final Long revenueId) {
        this.revenueId = revenueId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public void setBranchName(final String branchName) {
        setReportParam(EGOV_BRANCH_NAME, branchName);
    }

}
