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

package org.egov.collection.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.collection.config.properties.CollectionApplicationProperties;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.BranchUserMap;
import org.egov.collection.entity.Challan;
import org.egov.collection.entity.CollectionIndex;
import org.egov.collection.entity.OnlinePayment;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.BillReceiptInfoReq;
import org.egov.collection.integration.models.BillReceiptReq;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.collection.integration.services.BillingIntegrationService;
import org.egov.commons.Bankbranch;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.commons.exception.NoSuchObjectException;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.EmployeeView;
import org.egov.eis.entity.Jurisdiction;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Location;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.core.EnvironmentSettings;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.microservice.models.BusinessDetails;
import org.egov.infra.microservice.utils.MicroserviceUtils;
import org.egov.infra.notification.service.NotificationService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.SearchPositionService;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.Query;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CollectionsUtil {
    private static final Logger LOGGER = Logger.getLogger(CollectionsUtil.class);
    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    private SearchPositionService searchPositionService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EisUtilService eisService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PositionMasterService posService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private InstallmentHibDao installmentHibDao;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private ReportService reportService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CollectionApplicationProperties collectionApplicationProperties;

    @Autowired
    MicroserviceUtils microserviceUtils;

    @Autowired
    private EnvironmentSettings environmentSettings;

    @Autowired
    @Qualifier("branchUserMapService")
    private PersistenceService<BranchUserMap, Long> branchUserMapService;

    @Autowired
    private transient EmployeeService employeeService;

    /**
     * Returns the Status object for given status code for a receipt
     *
     * @param statusCode Status code for which status object is to be returned
     * @return the Status object for given status code for a receipt
     */
    public EgwStatus getReceiptStatusForCode(final String statusCode) {
        return getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_RECEIPTHEADER, statusCode);
    }

    /**
     * This method returns the <code>EgwStatus</code> for the given module type and status code
     *
     * @param moduleName Module name of the required status
     * @param statusCode Status code of the required status
     * @return the <code>EgwStatus</code> instance
     */
    public EgwStatus getStatusForModuleAndCode(final String moduleName, final String statusCode) {
        return egwStatusDAO.getStatusByModuleAndCode(moduleName, statusCode);
    }

    /**
     * This method returns the List of <code>EgwStatus</code> for ReceiptHeader
     *
     * @return the List of <code>EgwStatus</code> instance
     */
    public List<EgwStatus> getAllReceiptHeaderStatus() {
        return egwStatusDAO.getStatusByModule(CollectionConstants.MODULE_NAME_RECEIPTHEADER);
    }

    /**
     * @param sessionMap Map of session variables
     * @return user name of currently logged in user
     */
    public String getLoggedInUserName() {
        return securityUtils.getCurrentUser().getName();
    }

    /**
     * This method returns the User instance associated with the logged in user
     *
     * @param sessionMap Map of session variables
     * @return the logged in user
     */
    public User getLoggedInUser() {
        return securityUtils.getCurrentUser();
    }

    /**
     * @param user the user whose department is to be returned
     * @return department of the given user
     */
    public Department getDepartmentOfUser(final User user) {
        if (assignmentService.getPrimaryAssignmentForUser(user.getId()) == null)
            return null;
        else
            return eisCommonService.getDepartmentForUser(user.getId());
    }

    /**
     * @param sessionMap map of session variables
     * @return department of currently logged in user
     */
    public Department getDepartmentOfLoggedInUser() {
        final User user = securityUtils.getCurrentUser();
        return getDepartmentOfUser(user);
    }

    /**
     * This method returns the User instance for the userName passed as parameter
     *
     * @param userName
     * @return User
     */
    public User getUserByUserName(final String userName) {
        return userService.getUserByUsername(userName);
    }

    /**
     * @param sessionMap Map of session variables
     * @return Location object for given user
     */
    public Location getLocationOfUser(final Map<String, Object> sessionMap) {
        Location location = null;
        final String locationId = (String) sessionMap.get(CollectionConstants.SESSION_VAR_LOGIN_USER_LOCATIONID);
        if (locationId != null && !locationId.isEmpty())
            location = getLocationById(Long.valueOf(locationId));
        if (location == null)
            throw new ValidationException(Arrays.asList(new ValidationError("Location Not Found",
                    "submitcollections.validation.error.location.notfound")));
        return location;
    }

    public Location getLocationById(final Long locationId) {
        return (Location) persistenceService.findByNamedQuery(CollectionConstants.QUERY_GET_LOCATIONBYID, locationId);
    }

    /**
     * @return list of all active counters
     */
    public List getAllCounters() {
        return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALLCOUNTERS);
    }

    /**
     * @return List of all collection services (service type = C)
     */
    public List getChallanServiceList() {
        return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_SERVICES_BY_TYPE,
                CollectionConstants.SERVICE_TYPE_CHALLAN_COLLECTION);
    }

    /**
     * @return List of all billing services
     */
    public List getBillingServiceList() {
        return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_SERVICES_BY_TYPE,
                CollectionConstants.SERVICE_TYPE_BILLING);
    }

    /**
     * @return list of all users who have created at least one receipt
     */
    public List getReceiptCreators() {
        return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_CREATEDBYUSERS_OF_RECEIPTS);
    }

    /**
     * @return list of all zones that have receipts created
     */
    public List getReceiptZoneList() {
        return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ZONE_OF_RECEIPTS);
    }

    /**
     * This method returns the collection modes that are not allowed based on rules configured in the script
     *
     * @param loggedInUser a <code>User</code> entity representing the logged in user.
     * @return a <code>List</code> of <code>String</code> values representing the mode of payments supported.
     */
    public List<String> getCollectionModesNotAllowed(final User loggedInUser) {
        final List<String> collectionsModeNotAllowed = new ArrayList<>(0);
        final List<AppConfigValues> deptCodesApp = appConfigValuesService
                .getConfigValuesByModuleAndKey(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                        CollectionConstants.COLLECTION_DEPARTMENT_COLLMODES);
        final List<String> deptCodes = new ArrayList<>();
        for (final AppConfigValues deptCode : deptCodesApp)
            deptCodes.add(deptCode.getValue());
        List<Assignment> assignList;
        Boolean isDeptAllowed = false;
        final Boolean isEmp = isEmployee(loggedInUser);
        if (isEmp) {
            assignList = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(loggedInUser.getId());
            for (final Assignment assign : assignList)
                if (!deptCodes.isEmpty() && deptCodes.contains(assign.getDepartment().getCode()))
                    isDeptAllowed = true;
        }
        if (isEmp && !isDeptAllowed)
            throw new ValidationException(Arrays.asList(new ValidationError("Department",
                    "billreceipt.counter.deptcode.null")));
        if (isBankCollectionOperator(loggedInUser)) {
            // Bank Collection Operator cash, cheque, dd and card collection modes are
            // allowed.
            collectionsModeNotAllowed.add(CollectionConstants.INSTRUMENTTYPE_BANK);
            collectionsModeNotAllowed.add(CollectionConstants.INSTRUMENTTYPE_ONLINE);
        } else
            collectionsModeNotAllowed.add(CollectionConstants.INSTRUMENTTYPE_ONLINE);
        return collectionsModeNotAllowed;
    }

    public Boolean isEmployee(final User user) {
        for (final Role role : user.getRoles())
            for (final AppConfigValues appconfig : getThirdPartyUserRoles())
                if (role != null && role.getName().equals(appconfig.getValue()))
                    return false;
        return true;
    }

    public List<AppConfigValues> getThirdPartyUserRoles() {

        final List<AppConfigValues> appConfigValueList = appConfigValuesService.getConfigValuesByModuleAndKey(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG, CollectionConstants.COLLECTION_ROLEFORNONEMPLOYEE);
        return !appConfigValueList.isEmpty() ? appConfigValueList : null;

    }

    public String getDepartmentForWorkFlow() {
        String department = "";
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG, CollectionConstants.COLLECTION_WORKFLOWDEPARTMENT);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            department = appConfigValue.get(0).getValue();
        return department;
    }

    public Position getPositionByDeptDesgAndBoundary(final Boundary boundary) {
        Position position = null;
        final String designationStr = getDesignationForThirdPartyUser();
        final String departmentStr = getDepartmentForWorkFlow();
        final String[] department = departmentStr.split(",");
        final String[] designation = designationStr.split(",");
        List<Assignment> assignment = new ArrayList<>();
        for (final String dept : department) {
            for (final String desg : designation) {
                assignment = assignmentService.findByDepartmentDesignationAndBoundary(departmentService
                        .getDepartmentByName(dept).getId(), designationService.getDesignationByName(desg).getId(),
                        boundary.getId());
                if (!assignment.isEmpty())
                    break;
            }
            if (!assignment.isEmpty())
                break;
        }
        if (!assignment.isEmpty())
            position = assignment.get(0).getPosition();
        return position;
    }

    public String getDesignationForThirdPartyUser() {
        String designation = "";
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.COLLECTION_DESIGNATIONFORCSCOPERATOR);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            designation = appConfigValue.get(0).getValue();
        return designation;
    }

    /**
     * @param sessionMap Map of session variables
     * @return Position of logged in user
     */
    public Position getPositionOfUser(final User user) {
        Position position = posService.getCurrentPositionForUser(user.getId());
        if (position == null) {
            List<Assignment> assignList = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(user.getId());
            if (!assignList.isEmpty())
                position = assignList.get(0).getPosition();
            else
                throw new ValidationException(user.getUsername() + " User doesn't have active assignments.",
                        user.getUsername() + " User doesn't have active assignments.");
        }

        return position;
    }

    public List<Position> getPositionsForEmployee(final User user) {
        return posService.getPositionsForEmployee(user.getId(), null);
    }

    /**
     * Gets position by given position name
     *
     * @param positionName Position name
     * @return Position object for given position name
     */
    public Position getPositionByName(final String positionName) {
        return posService.getPositionByName(positionName);
    }

    /**
     * This method retrieves the <code>CFinancialYear</code> for the given date.
     *
     * @param date an instance of <code>Date</code> for which the financial year is to be retrieved.
     * @return an instance of <code></code> representing the financial year for the given date
     */
    public CFinancialYear getFinancialYearforDate(final Date date) {
        return (CFinancialYear) persistenceService
                .getSession()
                .createQuery(
                        "from CFinancialYear cfinancialyear where ? between "
                                + "cfinancialyear.startingDate and cfinancialyear.endingDate")
                .setDate(0, date).list()
                .get(0);
    }

    /**
     * This method checks if the given challan is valid.
     *
     * @param challan the <code>Challan</code> instance whose validity has to be checked
     * @return a boolean value - true indicating that the challan is valid and false indicating that teh challan is not valid
     */
    public boolean checkChallanValidity(final Challan challan) {
        final Calendar current = Calendar.getInstance();
        current.clear(Calendar.HOUR_OF_DAY);
        current.clear(Calendar.MINUTE);
        current.clear(Calendar.SECOND);
        current.clear(Calendar.MILLISECOND);

        final Calendar validityStart = Calendar.getInstance();
        validityStart.setTime(challan.getChallanDate());
        validityStart.clear(Calendar.HOUR_OF_DAY);
        validityStart.clear(Calendar.MINUTE);
        validityStart.clear(Calendar.SECOND);
        validityStart.clear(Calendar.MILLISECOND);

        final Calendar validityEnd = Calendar.getInstance();
        validityEnd.setTime(challan.getValidUpto());
        validityEnd.clear(Calendar.HOUR_OF_DAY);
        validityEnd.clear(Calendar.MINUTE);
        validityEnd.clear(Calendar.SECOND);
        validityEnd.clear(Calendar.MILLISECOND);

        return validityStart.compareTo(current) <= 0 && validityEnd.compareTo(current) >= 0;
    }

    /**
     * Fetches given bean from application context
     *
     * @param beanName name of bean to be fetched
     * @return given bean from application context
     */
    public Object getBean(final String beanName) {

        Object bean = null;
        try {
            bean = context.getBean(beanName);
            LOGGER.debug(" Got bean : " + beanName);
        } catch (final BeansException e) {
            final String errorMsg = "Could not locate bean [" + beanName + "]";
            LOGGER.error(errorMsg, e);
            throw new ApplicationRuntimeException(errorMsg, e);
        }
        return bean;
    }

    /**
     * This method returns the currently active config value for the given module name and key
     *
     * @param moduleName a <code>String<code> representing the module name
     *                     &#64;param key
     *                     a <code>String</code> representing the key
     * @param defaultValue Default value to be returned in case the key is not defined
     * @return <code>String</code> representing the configuration value
     */
    public String getAppConfigValue(final String moduleName, final String key, final String defaultValue) {
        final AppConfigValues configVal = appConfigValuesService.getAppConfigValueByDate(moduleName, key, new Date());
        return configVal == null ? defaultValue : configVal.getValue();
    }

    /**
     * This method returns the config value for the given module name and key
     *
     * @param moduleName a <code>String<code> representing the module name
     *                   &#64;param key
     *                   a <code>String</code> representing the key
     * @return <code>String</code> representing the configuration value
     */
    public String getAppConfigValue(final String moduleName, final String key) {
        final List<AppConfigValues> appConfValues = appConfigValuesService.getConfigValuesByModuleAndKey(moduleName,
                key);
        if (appConfValues != null && !appConfValues.isEmpty())
            return appConfValues.get(0).getValue();
        else
            return "";
    }

    /**
     * This method returns the list of config values for the given module name and key
     *
     * @param moduleName a <code>String<code> representing the module name
     *                   &#64;param key
     *                   a <code>String</code> representing the key
     * @return <code>List<AppConfigValues></code> representing the list of configuration values
     */
    public List<AppConfigValues> getAppConfigValues(final String moduleName, final String key) {
        return appConfigValuesService.getConfigValuesByModuleAndKey(moduleName, key);
    }

    /**
     * Gets position by given position id
     *
     * @param positionId Position Id
     * @return Position object for given position id
     */
    public Position getPositionById(final Long positionId) {
        return posService.getPositionById(positionId);
    }

    /**
     * This method is invoked from the ReceiptHeader.workFlow script and returns the position for the employee id passed as
     * parameter
     *
     * @param employeeId PersonalInformation Id
     * @return Position object for Employee Id passed as parameter
     */

    public Position getPositionforEmp(final Long employeeId) {
        return posService.getPositionByUserId(employeeId);
    }

    /**
     * This method is invoked from the ReceiptHeader.workFlow script and returns Employee object for the given Department Id,
     * Designation Id ,Boundary Id and FunctionaryId
     *
     * @param deptId Department Id
     * @param designationId Designation Id
     * @param boundaryId Boundary Id
     * @param functionaryId Functionary Id
     * @return PersonalInformation
     */

    public PersonalInformation getEmployeeByDepartmentDesignationBoundaryandFunctionary(final Long deptId,
            final Long designationId, final Integer boundaryId, final Integer functionaryId) {
        PersonalInformation personalInformation = null;
        try {
            personalInformation = EisManagersUtill.getEmployeeService().getEmployeeByFunctionary(deptId, designationId,
                    Long.valueOf(boundaryId), functionaryId);
        } catch (final Exception e) {
            final String errorMsg = "Could not get PersonalInformation";
            LOGGER.error(errorMsg, e);
            throw new ApplicationRuntimeException(errorMsg, e);
        }
        return personalInformation;
    }

    /**
     * @param sessionMap
     * @return
     */

    public List<Department> getAllNonPrimaryAssignmentsOfLoggedInUser() {
        return getAllNonPrimaryAssignmentsOfUser(getLoggedInUser());
    }

    /**
     * @param user the user whose non-primary department list is to be returned
     * @return list of non-primary department of the given user
     */
    public List<Department> getAllNonPrimaryAssignmentsOfUser(final User user) {
        final List<Department> departmentlist = new ArrayList<>();
        try {
            final HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("code", EisManagersUtill.getEmployeeService().getEmpForUserId(user.getId()).getCode());
            final List<EmployeeView> employeeViewList = eisService.getEmployeeInfoList(paramMap);
            if (!employeeViewList.isEmpty())
                for (final EmployeeView employeeView : employeeViewList)
                    if (!employeeView.getAssignment().getPrimary())
                        departmentlist.add(employeeView.getAssignment().getDepartment());
        } catch (final Exception e) {
            final String errorMsg = "Could not get list of assignments";
            LOGGER.error(errorMsg, e);
            throw new ApplicationRuntimeException(errorMsg, e);
        }

        return departmentlist;
    }

    /**
     * @param user the user whose non-primary department is to be returned
     * @return non-primary department of the given user. In case user has multiple non-primary departments, the first one will be
     * returned.
     */
    public Department getNonPrimaryDeptOfUser(final User user) {
        final List<Department> nonPrimaryAssignments = getAllNonPrimaryAssignmentsOfUser(user);
        return nonPrimaryAssignments.isEmpty() ? null : nonPrimaryAssignments.get(0);
    }

    public List<Designation> getDesignationsAllowedForChallanApproval(final Integer departmentId) {
        final List<Designation> designations = designationService.getAllDesignationByDepartment(
                Long.valueOf(departmentId), new Date());
        final List<Designation> designation = new ArrayList<>(0);
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.COLLECTION_DESIG_CHALLAN_WORKFLOW);
        for (final Designation desig : designations)
            for (final AppConfigValues app : appConfigValue)
                if (desig.getName().equals(app.getValue()))
                    designation.add(desig);
        return designation;
    }

    public List<Department> getDepartmentsAllowedForChallanApproval() {
        final List<Department> departments = new ArrayList<>(0);
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.COLLECTION_DESIG_CHALLAN_WORKFLOW);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            for (final AppConfigValues app : appConfigValue) {
                final List<Assignment> assignments = assignmentService.findPrimaryAssignmentForDesignationName(app
                        .getValue());
                for (final Assignment assign : assignments)
                    if (!departments.contains(assign.getDepartment()))
                        departments.add(assign.getDepartment());
            }
        return departments;
    }

    /**
     * This method checks if the given glcode belongs to an account head representing an arrear account head (for Property Tax).
     * The glcodes for such accounts are retrieved from App Config.
     *
     * @param glcode The Chart of Accounts Code
     * @param description Description of the glcode
     * @returna a <code>Boolean</code> indicating if the glcode is arrear account head
     */
    public boolean isPropertyTaxArrearAccountHead(final String glcode, final String description) {
        final List<AppConfigValues> list = appConfigValuesService.getConfigValuesByModuleAndKey(
                CollectionConstants.MODULE_NAME_PROPERTYTAX, "ISARREARACCOUNT");
        final AppConfigValues penaltyGlCode = appConfigValuesService.getAppConfigValueByDate(
                CollectionConstants.MODULE_NAME_PROPERTYTAX, "PTPENALTYGLCODE", new Date());
        boolean retValue;
        LOGGER.debug("isPropertyTaxArrearAccountHead glcode " + glcode + " description " + description);
        if (penaltyGlCode != null && penaltyGlCode.getValue().equals(glcode)) {
            final Module module = moduleService.getModuleByName(CollectionConstants.MODULE_NAME_PROPERTYTAX);
            final String currInst = installmentHibDao.getInsatllmentByModuleForGivenDate(module, new Date())
                    .getDescription();
            if (currInst.equals(description.substring(16, description.length())))
                retValue = false;
            else
                retValue = true;
        } else {
            final ArrayList<String> accValues = new ArrayList<>(0);
            for (final AppConfigValues value : list)
                accValues.add(value.getValue());
            if (accValues.contains(glcode))
                retValue = true;
            else
                retValue = false;
        }

        return retValue;
    }

    public List<EmployeeView> getPositionBySearchParameters(final String beginsWith, final Integer desId,
            final Integer deptId, final Integer jurdId, final Integer roleId, final Date userDate,
            final Integer maxResults) throws NoSuchObjectException {

        return searchPositionService.getPositionBySearchParameters(beginsWith, desId, deptId,
                jurdId != null ? Long.valueOf(jurdId) : null, roleId, userDate, maxResults);

    }

    /**
     * @param consumerCode
     * @return last three online transaction for the consumerCode
     */
    public List<OnlinePayment> getOnlineTransactionHistory(final String consumerCode) {
        final String hql = "select online from ReceiptHeader rh, org.egov.collection.entity.OnlinePayment online where rh.id = online.receiptHeader.id and rh.consumerCode =:consumercode  order by online.id desc";
        final Query query = persistenceService.getSession().createQuery(hql);
        query.setString("consumercode", consumerCode);
        query.setMaxResults(3);
        return query.list();
    }

    /**
     * @return list of all active locations
     */
    public List getAllLocations() {
        return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_LOCATIONS);
    }

    /**
     * @return list of all fund
     */
    public List<Fund> getAllFunds() {
        return persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_FUND);
    }

    public User getUserById(final Long userId) {
        return userService.getUserById(userId);
    }

    public boolean isValidTemplate(final String templateName) {
        return reportService.isValidTemplate(templateName);
    }

    public ReportOutput createReport(final ReportRequest reportRequest) {
        return reportService.createReport(reportRequest);
    }

    public ReceiptAmountInfo updateReceiptDetailsAndGetReceiptAmountInfo(final BillReceiptReq billReceipt,
            final String serviceCode) {
        final RestTemplate restTemplate = new RestTemplate();
        billReceipt.setTenantId(microserviceUtils.getTenentId());
        final BillReceiptInfoReq billReceiptInfoReq = new BillReceiptInfoReq();
        billReceiptInfoReq.setBillReceiptInfo(billReceipt);
        billReceiptInfoReq.setRequestInfo(microserviceUtils.createRequestInfo());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateReceiptDetailsAndGetReceiptAmountInfo - before calling LAMS update");
        final String url = collectionApplicationProperties.getLamsServiceUrl().concat(
                collectionApplicationProperties.getUpdateDemandUrl(serviceCode.toLowerCase()));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateReceiptDetailsAndGetReceiptAmountInfo - url" + url);
        ReceiptAmountInfo receiptAmountInfo = null;
        try {
            receiptAmountInfo = restTemplate.postForObject(url, billReceiptInfoReq, ReceiptAmountInfo.class);
        } catch (final Exception e) {
            final String errMsg = "Exception while updateReceiptDetailsAndGetReceiptAmountInfo for bill number  ["
                    + billReceipt.getBillReferenceNum() + "]!";
            LOGGER.error(errMsg, e);
            throw new ApplicationRuntimeException(errMsg, e);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateReceiptDetailsAndGetReceiptAmountInfo - response" + receiptAmountInfo);
        return receiptAmountInfo;
    }

    public CollectionIndex constructCollectionIndex(final ReceiptHeader receiptHeader) {
        ReceiptAmountInfo receiptAmountInfo = new ReceiptAmountInfo();

        String instrumentType = "";
        if (!receiptHeader.getReceiptInstrument().isEmpty())
            instrumentType = receiptHeader.getReceiptInstrument().iterator().next().getInstrumentType().getType();

        if (receiptHeader.getReceipttype() == CollectionConstants.RECEIPT_TYPE_BILL)
            try {
                final Set<BillReceiptInfo> billReceipts = new HashSet<>(0);
                final BillReceiptInfo billReceipt = new BillReceiptInfoImpl(receiptHeader, chartOfAccountsHibernateDAO,
                        persistenceService, null);
                billReceipts.add(billReceipt);
                if (receiptHeader.getService().equals(CollectionConstants.SERVICECODE_LAMS))
                    receiptAmountInfo = updateReceiptDetailsAndGetReceiptAmountInfo(new BillReceiptReq(billReceipt),
                            receiptHeader.getService());
                else {
                    final BillingIntegrationService billingServiceBean = (BillingIntegrationService) getBean(
                            receiptHeader.getService() + CollectionConstants.COLLECTIONS_INTERFACE_SUFFIX);
                    receiptAmountInfo = billingServiceBean.receiptAmountBifurcation(billReceipt);
                }
            } catch (final Exception e) {
                final String errMsg = "Exception while constructing collection index for receipt number ["
                        + receiptHeader.getReceiptnumber() + "]!";
                LOGGER.error(errMsg, e);
                throw new ApplicationRuntimeException(errMsg, e);
            }
        return CollectionIndex
                .builder()
                .withReceiptDate(receiptHeader.getReceiptdate())
                .withReceiptnumber(receiptHeader.getReceiptnumber())
                .withBillingservice(receiptHeader.getService())
                .withPaymentMode(instrumentType)
                .withTotalamount(receiptHeader.getTotalAmount())
                .withChannel(receiptHeader.getSource())
                .withStatus(receiptHeader.getStatus().getDescription())
                .withConsumerCode(receiptHeader.getConsumerCode() != null ? receiptHeader.getConsumerCode() : "")
                .withBillNumber(receiptHeader.getReferencenumber() != null ? receiptHeader.getReferencenumber() : null)
                .withPaymentGateway(
                        receiptHeader.getOnlinePayment() != null ? receiptHeader.getOnlinePayment().getService()
                                .getName() : "")
                .withConsumerName(receiptHeader.getPayeeName() != null ? receiptHeader.getPayeeName() : "")
                /* .withReceiptCreator(receiptHeader.getCreatedBy() != null ? receiptHeader.getCreatedBy().getName() : "") */
                .withArrearAmount(receiptAmountInfo.getArrearsAmount())
                .withAdvanceAmount(receiptAmountInfo.getAdvanceAmount())
                .withCurrentAmount(receiptAmountInfo.getCurrentInstallmentAmount())
                .withPenaltyAmount(receiptAmountInfo.getPenaltyAmount())
                .withArrearCess(receiptAmountInfo.getArrearCess())
                .withLatePaymentChargesAmount(receiptAmountInfo.getLatePaymentCharges())
                .withCurrentCess(receiptAmountInfo.getCurrentCess())
                .withReductionAmount(receiptAmountInfo.getReductionAmount())
                .withInstallmentFrom(
                        receiptAmountInfo.getInstallmentFrom() != null ? receiptAmountInfo.getInstallmentFrom() : "")
                .withInstallmentTo(
                        receiptAmountInfo.getInstallmentTo() != null ? receiptAmountInfo.getInstallmentTo() : "")
                .withRevenueWard(receiptAmountInfo.getRevenueWard())
                .withConsumerType(receiptHeader.getConsumerType() != null ? receiptHeader.getConsumerType() : "")
                .withConflict(receiptAmountInfo.getConflict() != null ? receiptAmountInfo.getConflict() : 0).build();
    }

    public Boolean checkVoucherCreation(final ReceiptHeader receiptHeader) {
        Boolean createVoucherForBillingService = Boolean.FALSE;
        BusinessDetails servcie = microserviceUtils.getBusinessDetailsByCode(receiptHeader.getService());
        if (servcie != null) {
            if (servcie.getVoucherCutoffDate() != null
                    && receiptHeader.getReceiptdate().compareTo(new Date(servcie.getVoucherCutoffDate())) > 0) {
                if (servcie.getVoucherCreation() != null)
                    createVoucherForBillingService = servcie.getVoucherCreation();
            } else if (servcie.getVoucherCutoffDate() == null
                    && servcie.getVoucherCreation() != null)
                createVoucherForBillingService = servcie.getVoucherCreation();
        }
        return createVoucherForBillingService;
    }

    public Designation getDesignationForApprover() {
        return designationService.getDesignationByName(getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.COLLECTION_DESIGNATIONFORAPPROVER));
    }

    public String getApproverName(final Long position) {
        String approver = null;
        final List<Assignment> assignments = assignmentService.getAssignmentsForPosition(position);
        for (final Assignment assignment : assignments)
            if (assignment.getPrimary())
                approver = assignment.getEmployee().getName().concat("~").concat(assignment.getEmployee().getCode())
                        .concat("~").concat(assignment.getPosition().getName());
        return approver;
    }

    public List<ReceiptDetail> reconstructReceiptDetail(final ReceiptHeader receiptHeader,
            final List<ReceiptDetail> receiptDetailList) {
        final BillingIntegrationService billingService = (BillingIntegrationService) getBean(receiptHeader.getService()
                + CollectionConstants.COLLECTIONS_INTERFACE_SUFFIX);
        return billingService.reconstructReceiptDetail(receiptHeader.getReferencenumber(),
                receiptHeader.getTotalAmount(), receiptDetailList);
    }

    public Date getRemittanceVoucherDate(final Date receiptDate) {
        Boolean useReceiptDateAsContraVoucherDate = false;
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date voucherDate;
        Date rcptDate = null;
        if (getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_USERECEIPTDATEFORCONTRA).equals(CollectionConstants.YES))
            useReceiptDateAsContraVoucherDate = true;

        try {
            Date finDate = null;
            if (receiptDate != null) {
                rcptDate = receiptDate;
                finDate = financialYearDAO.getFinancialYearByDate(rcptDate).getStartingDate();
            }
            if (finDate != null && finDate.toString().equals(financialYearDAO.getCurrYearStartDate())) {
                if (useReceiptDateAsContraVoucherDate)
                    voucherDate = rcptDate;
                else
                    voucherDate = sdf.parse(sdf.format(new Date()));
            } else
                voucherDate = financialYearDAO.getPreviousFinancialYearByDate(new Date()).getEndingDate();
        } catch (final ParseException e) {

            LOGGER.debug("Exception in parsing date  " + rcptDate + " - " + e.getMessage());
            throw new ApplicationRuntimeException("Exception while parsing date", e);
        }
        return voucherDate;
    }

    public Boolean getVoucherType() {
        Boolean voucherTypeForChequeDDCard = false;
        if (getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_REMITTANCEVOUCHERTYPEFORCHEQUEDDCARD).equals(
                        CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE))
            voucherTypeForChequeDDCard = true;
        return voucherTypeForChequeDDCard;
    }

    /**
     * @param serviceCode Billing service code for which the receipt template is to be returned
     * @return Receipt template to be used for given billing service code.
     */
    public String getReceiptTemplateName(final char receiptType, final String serviceCode) {
        String templateName = null;

        switch (receiptType) {
        case CollectionConstants.RECEIPT_TYPE_BILL:
            templateName = serviceCode + CollectionConstants.SEPARATOR_UNDERSCORE
                    + CollectionConstants.RECEIPT_TEMPLATE_NAME;// <servicecode>_collection_receipt
            if (!isValidTemplate(templateName)) {
                LOGGER.info("Billing system specific report template [" + templateName
                        + "] not available. Using the default template [" + CollectionConstants.RECEIPT_TEMPLATE_NAME
                        + "]");
                templateName = "PT_collection_receipt";

                if (!isValidTemplate(templateName)) {
                    // No template available for creating the receipt report.
                    // Throw
                    // exception.
                    final String errMsg = "Report template [" + templateName
                            + "] not available! Receipt report cannot be generated.";
                    LOGGER.error(errMsg);
                    throw new ApplicationRuntimeException(errMsg);
                }
            }
            break;
        case CollectionConstants.RECEIPT_TYPE_CHALLAN:
            templateName = CollectionConstants.CHALLAN_RECEIPT_TEMPLATE_NAME;
            break;
        case CollectionConstants.RECEIPT_TYPE_ADHOC:
            templateName = CollectionConstants.RECEIPT_TEMPLATE_NAME;
            break;
        }
        return templateName;
    }

    public void emailReceiptAsAttachment(final ReceiptHeader receiptHeader, final byte[] attachment) {
        String emailBody = collectionApplicationProperties.getEmailBody();
        BusinessDetails bd = microserviceUtils.getBusinessDetailsByCode(receiptHeader.getService());
        emailBody = String.format(emailBody, ApplicationThreadLocals.getCityName(), receiptHeader.getTotalAmount()
                .toString(), bd.getName(), receiptHeader.getConsumerCode(), receiptHeader
                        .getReceiptdate().toString(),
                ApplicationThreadLocals.getCityName());
        String emailSubject = collectionApplicationProperties.getEmailSubject();
        emailSubject = String.format(emailSubject, bd.getName());
        notificationService.sendEmailWithAttachment(receiptHeader.getPayeeEmail(), emailSubject, emailBody,
                "application/pdf", "Receipt" + receiptHeader.getReceiptdate().toString(), attachment);
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public String getBeanNameForDebitAccountHead() {
        Class<?> service = null;
        try {
            service = Class.forName(environmentSettings.getProperty("collection.debitaccounthead.client.impl.class"));
        } catch (final ClassNotFoundException e) {
            LOGGER.error("Error getting Class name for Debit Account Head" + e);
        }
        // getting the entity type service.
        String serviceClassName = null;
        if (service != null)
            serviceClassName = service.getSimpleName();
        if (serviceClassName != null)
            serviceClassName = Character.toLowerCase(serviceClassName.charAt(0))
                    + serviceClassName.substring(1).substring(0, serviceClassName.length() - 5);
        return serviceClassName;
    }

    public Boolean isBankCollectionOperator(final User user) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equals(CollectionConstants.BANK_COLLECTION_OPERATOR))
                return true;
        return false;
    }

    public Boolean isBankCollectionRemitter(final User user) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equals(CollectionConstants.BANK_COLLECTION_REMITTER))
                return true;
        return false;
    }

    public Boolean isUserRole(final User user, String roleName) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equals(roleName))
                return true;
        return false;
    }

    public Boolean isRoleToCreateReceiptApprovedStatus(final User user) {
        final List<AppConfigValues> appConfigValuesList = getAppConfigValues(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_ROLES_CREATERECEIPT_APPROVEDSTATUS);
        String value;
        for (final AppConfigValues appConfigVal : appConfigValuesList) {
            value = appConfigVal.getValue();
            for (final Role role : user.getRoles())
                if (role != null && role.getName().equals(value))
                    return true;
        }
        return false;
    }

    public List<Bankbranch> getBankCollectionBankBranchList() {
        List<Bankbranch> bankBranchArrayList = new ArrayList<>();
        StringBuilder queryString = new StringBuilder(
                "select distinct(bb.id) as branchid,b.NAME||'-'||bb.BRANCHNAME as branchname from BANK b,BANKBRANCH bb,"
                        + " EGCL_COLLECTIONMIS cmis where bb.BANKID=b.ID  and bb.id=cmis.depositedBranch ");
        final Query query = persistenceService.getSession().createSQLQuery(queryString.toString());
        List<Object[]> queryResult = query.list();
        for (int i = 0; i < queryResult.size(); i++) {
            final Object[] arrayObjectInitialIndex = queryResult.get(i);
            final Bankbranch newBankbranch = new Bankbranch();
            newBankbranch.setId(Integer.valueOf(arrayObjectInitialIndex[0].toString()));
            newBankbranch.setBranchname(arrayObjectInitialIndex[1].toString());
            bankBranchArrayList.add(newBankbranch);
        }
        return bankBranchArrayList;
    }

    public String getJurisdictionBoundary() {
        final User user = getLoggedInUser();
        if (!isBankCollectionRemitter(user)) {
            final Employee employee = employeeService.getEmployeeById(user.getId());
            final StringBuilder jurValuesId = new StringBuilder();
            for (final Jurisdiction element : employee.getJurisdictions()) {
                if (jurValuesId.length() > 0)
                    jurValuesId.append(',');
                jurValuesId.append(element.getBoundary().getId());

                for (final Boundary boundary : element.getBoundary().getChildren()) {
                    jurValuesId.append(',');
                    jurValuesId.append(boundary.getId());
                }
            }
            return jurValuesId.toString();
        } else
            return "";
    }

}
