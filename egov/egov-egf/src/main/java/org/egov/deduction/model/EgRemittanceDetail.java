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

// Generated Oct 11, 2007 12:03:45 PM by Hibernate Tools 3.2.0.b9

import org.egov.commons.CGeneralLedger;

import java.math.BigDecimal;
import java.util.Date;

/**
 * EgRemittanceDetail generated by hbm2java
 */
public class EgRemittanceDetail implements java.io.Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -2162847648034757161L;

    private Integer id;

    private EgRemittance egRemittance;

    private EgRemittanceGldtl egRemittanceGldtl;

    private Date lastmodifieddate;

    private BigDecimal remittedamt;

    private CGeneralLedger generalLedger;

    public EgRemittanceDetail()
    {
    }

    public EgRemittanceDetail(final EgRemittance egRemittance,
            final EgRemittanceGldtl egRemittanceGldtl)
    {
        this.egRemittance = egRemittance;
        this.egRemittanceGldtl = egRemittanceGldtl;
    }

    public EgRemittanceDetail(final EgRemittance egRemittance,
            final EgRemittanceGldtl egRemittanceGldtl, final Date lastmodifieddate,
            final BigDecimal remittedamt)
    {
        this.egRemittance = egRemittance;
        this.egRemittanceGldtl = egRemittanceGldtl;
        this.lastmodifieddate = lastmodifieddate;
        this.remittedamt = remittedamt;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(final Integer id)
    {
        this.id = id;
    }

    public EgRemittance getEgRemittance()
    {
        return egRemittance;
    }

    public void setEgRemittance(final EgRemittance egRemittance)
    {
        this.egRemittance = egRemittance;
    }

    public EgRemittanceGldtl getEgRemittanceGldtl()
    {
        return egRemittanceGldtl;
    }

    public void setEgRemittanceGldtl(final EgRemittanceGldtl egRemittanceGldtl)
    {
        this.egRemittanceGldtl = egRemittanceGldtl;
    }

    public Date getLastmodifieddate()
    {
        return lastmodifieddate;
    }

    public void setLastmodifieddate(final Date lastmodifieddate)
    {
        this.lastmodifieddate = lastmodifieddate;
    }

    public BigDecimal getRemittedamt()
    {
        return remittedamt;
    }

    public void setRemittedamt(final BigDecimal remittedamt)
    {
        this.remittedamt = remittedamt;
    }

    public CGeneralLedger getGeneralLedger() {
        return generalLedger;
    }

    public void setGeneralLedger(final CGeneralLedger generalLedger) {
        this.generalLedger = generalLedger;
    }

}
