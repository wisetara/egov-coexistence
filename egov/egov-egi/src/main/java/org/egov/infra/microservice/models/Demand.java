/*
 * eGov suite of products aim to improve the internal efficiency, transparency,
 *    accountability and the service delivery of the government organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.infra.microservice.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Demand {

    private String id;

    private String tenantId;

    private String consumerCode;

    private String consumerType;

    private String businessService;

    private User owner;

    private Long taxPeriodFrom;

    private Long taxPeriodTo;

    private List<DemandDetail> demandDetails = new ArrayList<>();

    private BigDecimal minimumAmountPayable = BigDecimal.ZERO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }

    public String getBusinessService() {
        return businessService;
    }

    public void setBusinessService(String businessService) {
        this.businessService = businessService;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Long getTaxPeriodFrom() {
        return taxPeriodFrom;
    }

    public void setTaxPeriodFrom(Long taxPeriodFrom) {
        this.taxPeriodFrom = taxPeriodFrom;
    }

    public Long getTaxPeriodTo() {
        return taxPeriodTo;
    }

    public void setTaxPeriodTo(Long taxPeriodTo) {
        this.taxPeriodTo = taxPeriodTo;
    }

    public List<DemandDetail> getDemandDetails() {
        return demandDetails;
    }

    public void setDemandDetails(List<DemandDetail> demandDetails) {
        this.demandDetails = demandDetails;
    }

    public BigDecimal getMinimumAmountPayable() {
        return minimumAmountPayable;
    }

    public void setMinimumAmountPayable(BigDecimal minimumAmountPayable) {
        this.minimumAmountPayable = minimumAmountPayable;
    }

}
