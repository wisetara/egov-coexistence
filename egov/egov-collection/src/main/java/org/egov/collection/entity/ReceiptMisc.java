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
package org.egov.collection.entity;

import org.egov.commons.Bankbranch;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.validator.annotation.Required;

/**
 * ReceiptMisc generated by hbm2java
 */
public class ReceiptMisc implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private ReceiptHeader receiptHeader;
    private Fund fund;
    private Fundsource fundsource;

    private Boundary boundary;
    private String department;
    private Scheme scheme;
    private SubScheme subscheme;
    private Functionary idFunctionary;
    private Bankbranch depositedBranch;

    public ReceiptMisc() {
    }

    public ReceiptMisc(final Boundary boundary, final Fund fund, final Functionary functionary,
            final Fundsource fundSource, final String department, final ReceiptHeader receiptHeader,
            final Scheme scheme, final SubScheme subscheme, final Bankbranch depositedBranch) {
        this.boundary = boundary;
        this.fund = fund;
        idFunctionary = functionary;
        fundsource = fundSource;
        this.department = department;
        this.receiptHeader = receiptHeader;
        this.scheme = scheme;
        this.subscheme = subscheme;
        this.depositedBranch = depositedBranch;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public ReceiptHeader getReceiptHeader() {
        return receiptHeader;
    }

    public void setReceiptHeader(final ReceiptHeader receiptHeader) {
        this.receiptHeader = receiptHeader;
    }

    @Required(message = "billingsystem.fund.null")
    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public Fundsource getFundsource() {
        return fundsource;
    }

    public void setFundsource(final Fundsource fundsource) {
        this.fundsource = fundsource;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(final Boundary boundary) {
        this.boundary = boundary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(final String department) {
        this.department = department;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(final Scheme scheme) {
        this.scheme = scheme;
    }

    public SubScheme getSubscheme() {
        return subscheme;
    }

    public void setSubscheme(final SubScheme subscheme) {
        this.subscheme = subscheme;
    }

    /**
     * @return the idFunctionary
     */
    public Functionary getIdFunctionary() {
        return idFunctionary;
    }

    /**
     * @param idFunctionary the idFunctionary to set
     */
    public void setIdFunctionary(final Functionary idFunctionary) {
        this.idFunctionary = idFunctionary;
    }

    public Bankbranch getDepositedBranch() {
        return depositedBranch;
    }

    public void setDepositedBranch(Bankbranch depositedBranch) {
        this.depositedBranch = depositedBranch;
    }

}
