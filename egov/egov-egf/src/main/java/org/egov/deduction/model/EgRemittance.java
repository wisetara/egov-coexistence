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
package org.egov.deduction.model;

// Generated Oct 3, 2007 7:28:41 PM by Hibernate Tools 3.2.0.b9

import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.model.recoveries.Recovery;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * EgRemittance generated by hbm2java
 */
public class EgRemittance implements java.io.Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -2310246578830169762L;

    private Integer id;

    private CFinancialYear financialyear;

    private CVoucherHeader voucherheader;

    private Recovery recovery;

    private Fund fund;

    private BigDecimal month;

    Set<EgRemittanceDetail> egRemittanceDetail = new HashSet<EgRemittanceDetail>();

    Date asOnDate;

    private BigDecimal createdby;

    private Date createddate;

    private BigDecimal lastmodifiedby;

    private Date lastmodifieddate;

    public EgRemittance()
    {
    }

    public EgRemittance(final CFinancialYear financialyear,
            final CVoucherHeader voucherheader, final Recovery recovery, final Fund fund, final BigDecimal month,
            final BigDecimal createdby, final Date createddate)
    {
        this.financialyear = financialyear;
        this.voucherheader = voucherheader;
        this.recovery = recovery;
        this.fund = fund;
        this.month = month;
        this.createdby = createdby;
        this.createddate = createddate;
    }

    public EgRemittance(final CFinancialYear financialyear,
            final CVoucherHeader voucherheader, final Recovery recovery, final Fund fund, final BigDecimal month,
            final BigDecimal createdby, final Date createddate, final BigDecimal lastmodifiedby,
            final Date lastmodifieddate)
    {
        this.financialyear = financialyear;
        this.voucherheader = voucherheader;
        this.recovery = recovery;
        this.fund = fund;
        this.month = month;
        this.createdby = createdby;
        this.createddate = createddate;
        this.lastmodifiedby = lastmodifiedby;
        this.lastmodifieddate = lastmodifieddate;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(final Integer id)
    {
        this.id = id;
    }

    public CFinancialYear getFinancialyear()
    {
        return financialyear;
    }

    public void setFinancialyear(final CFinancialYear financialyear)
    {
        this.financialyear = financialyear;
    }

    /**
     * @return the egRemittanceDetail
     */
    public Set<EgRemittanceDetail> getEgRemittanceDetail() {
        return egRemittanceDetail;
    }

    /**
     * @param egRemittanceDetail the egRemittanceDetail to set
     */
    public void setEgRemittanceDetail(final Set<EgRemittanceDetail> egRemittanceDetail) {
        this.egRemittanceDetail = egRemittanceDetail;
    }

    /**
     * @return the asOnDate
     */
    public Date getAsOnDate() {
        return asOnDate;
    }

    /**
     * @param asOnDate the asOnDate to set
     */
    public void setAsOnDate(final Date asOnDate) {
        this.asOnDate = asOnDate;
    }

    public CVoucherHeader getVoucherheader()
    {
        return voucherheader;
    }

    public void setVoucherheader(final CVoucherHeader voucherheader)
    {
        this.voucherheader = voucherheader;
    }

    public Recovery getRecovery() {
        return recovery;
    }

    public void setRecovery(final Recovery recovery) {
        this.recovery = recovery;
    }

    public Fund getFund()
    {
        return fund;
    }

    public void setFund(final Fund fund)
    {
        this.fund = fund;
    }

    public BigDecimal getMonth()
    {
        return month;
    }

    public void setMonth(final BigDecimal month)
    {
        this.month = month;
    }

    public BigDecimal getCreatedby()
    {
        return createdby;
    }

    public void setCreatedby(final BigDecimal createdby)
    {
        this.createdby = createdby;
    }

    public Date getCreateddate()
    {
        return createddate;
    }

    public void setCreateddate(final Date createddate)
    {
        this.createddate = createddate;
    }

    public BigDecimal getLastmodifiedby()
    {
        return lastmodifiedby;
    }

    public void setLastmodifiedby(final BigDecimal lastmodifiedby)
    {
        this.lastmodifiedby = lastmodifiedby;
    }

    public Date getLastmodifieddate()
    {
        return lastmodifieddate;
    }

    public void setLastmodifieddate(final Date lastmodifieddate)
    {
        this.lastmodifieddate = lastmodifieddate;
    }

}
