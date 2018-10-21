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
package org.egov.commons.service;

import org.egov.commons.CFinancialYear;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.python.bouncycastle.asn1.isismtt.x509.Restriction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FinancialYearService extends PersistenceService<CFinancialYear, Long>
{

    public FinancialYearService() {
        super(CFinancialYear.class);
    }

    public FinancialYearService(final Class<CFinancialYear> type) {
        super(type);
    }

    public List<CFinancialYear> getAll() {

        Query query = getSession()
                .createQuery(
                        " from CFinancialYear cfinancialyear order by  cfinancialyear.finYearRange desc");
        return query.list();
    }

    public CFinancialYear getCurrentFinancialYear() {

        Date date = new Date();
        CFinancialYear cFinancialYear = null;
        Query query = getSession()
                .createQuery(
                        " from CFinancialYear cfinancialyear where cfinancialyear.startingDate <=:sDate and cfinancialyear.endingDate >=:eDate");
        query.setDate("sDate", date);
        query.setDate("eDate", date);
        ArrayList list = (ArrayList) query.list();
        if (list.size() > 0)
            cFinancialYear = (CFinancialYear) list.get(0);
        if (null == cFinancialYear)
            throw new ApplicationRuntimeException("Financial Year is not active For Posting.");
        return cFinancialYear;
    }

   public List<CFinancialYear> Search(CFinancialYear finYear,List<Integer> ids,String sortBy,int pageSize,int offset){

	   Criteria criteria = getSession().createCriteria(CFinancialYear.class);
	   criteria.add(Restrictions.eq("financialyear", finYear.getFinYearRange()));
	   criteria.add(Restrictions.eq("startingDate", finYear.getStartingDate()));
	   criteria.add(Restrictions.eq("endingDate",finYear.getEndingDate()));
	   criteria.add(Restrictions.eq("isActive",finYear.getIsActive()));
	   criteria.add(Restrictions.eq("isACtiveForPosting",finYear.getIsActiveForPosting()));
	   if(ids.size()>0)
	   criteria.add(Restrictions.in("id", ids));

	   criteria.setFirstResult(offset);
	   criteria.setMaxResults(pageSize);
	   criteria.addOrder(Order.asc(sortBy));
	   return criteria.list();
   }
}
