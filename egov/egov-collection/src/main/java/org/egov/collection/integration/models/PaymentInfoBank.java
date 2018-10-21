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
package org.egov.collection.integration.models;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentInfoBank implements PaymentInfo {

    private BigDecimal instrumentAmount;
    private Long bankId;
    private Long bankAccountId;
    private Integer transactionNumber;
    private Date transactionDate;

    public PaymentInfoBank(final BigDecimal instrumentAmount, final Long bankId, final Long bankAccountId,
            final Integer transactionNumber, final Date transactionDate) {
        this.instrumentAmount = instrumentAmount;
        this.bankId = bankId;
        this.bankAccountId = bankAccountId;
        this.transactionNumber = transactionNumber;
        this.transactionDate = transactionDate;
    }

    /**
     * Default Constructor
     */
    public PaymentInfoBank() {
    }

    @Override
    public BigDecimal getInstrumentAmount() {
        return instrumentAmount;
    }

    @Override
    public TYPE getInstrumentType() {
        return TYPE.bank;
    }

    /**
     * Gets the bank id.
     *
     * @return the bankId
     */
    public Long getBankId() {
        return bankId;
    }

    /**
     * Gets the bank account id.
     *
     * @return the bankAccountId
     */
    public Long getBankAccountId() {
        return bankAccountId;
    }

    /**
     * Gets the transaction number.
     *
     * @return the transactionNumber
     */
    public Integer getTransactionNumber() {
        return transactionNumber;
    }

    /**
     * Gets the transaction date.
     *
     * @return the transactionDate
     */
    public Date getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param instrumentAmount
     *            the instrumentAmount to set
     */
    @Override
    public void setInstrumentAmount(final BigDecimal instrumentAmount) {
        this.instrumentAmount = instrumentAmount;
    }

    /**
     * @param bankId
     *            the bankId to set
     */
    public void setBankId(final Long bankId) {
        this.bankId = bankId;
    }

    /**
     * @param bankAccountId
     *            the bankAccountId to set
     */
    public void setBankAccountId(final Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    /**
     * @param transactionNumber
     *            the transactionNumber to set
     */
    public void setTransactionNumber(final Integer transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    /**
     * @param transactionDate
     *            the transactionDate to set
     */
    public void setTransactionDate(final Date transactionDate) {
        this.transactionDate = transactionDate;
    }

}
