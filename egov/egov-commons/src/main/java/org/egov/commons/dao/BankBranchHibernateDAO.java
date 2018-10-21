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
package org.egov.commons.dao;

import org.egov.commons.Bankbranch;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
public class BankBranchHibernateDAO {
    @Transactional
    public Bankbranch update(final Bankbranch entity) {
        getCurrentSession().update(entity);
        return entity;
    }

    @Transactional
    public Bankbranch create(final Bankbranch entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Transactional
    public void delete(Bankbranch entity) {
        getCurrentSession().delete(entity);
    }

    public Bankbranch findById(Number id, boolean lock) {
        return (Bankbranch) getCurrentSession().load(Bankbranch.class, id);
    }

    public List<Bankbranch> findAll() {
        return (List<Bankbranch>) getCurrentSession().createCriteria(Bankbranch.class).list();
    }

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<Bankbranch> getAllBankBranchs() {
        return getCurrentSession().createQuery("from Bankbranch BB order by BB.bank.name").list();
    }

    public List<Bankbranch> getAllBankBranchsByBank(Integer bankId) {
        Set<Bankbranch> ss = new LinkedHashSet<Bankbranch>();
        List<Bankbranch> bankBranchList = new ArrayList<Bankbranch>();

        Query createQuery = getCurrentSession()
                .createQuery(
                        "select distinct bb from Bankbranch bb , Bankaccount ba  where ba.bankbranch =bb and ba.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and bb.bank.id=:bankId and bb.isactive=true")
                .setInteger("bankId", bankId);
        if (bankId != null) {
            List<Bankbranch> list = (List<Bankbranch>) createQuery.list();
            if (list != null && !list.isEmpty())
            {
                ss.addAll(list);
                bankBranchList.addAll(ss);
            }
        }
        return bankBranchList;
    }

    /**
     * @param bankId
     * @return BankBranch for type "RECEIPTS_PAYMENTS"
     */
    public List<Bankbranch> getAllBankBranchsByBankForReceiptPayments(Integer bankId) {
        Set<Bankbranch> ss = new LinkedHashSet<Bankbranch>();
        List<Bankbranch> bankBranchList = new ArrayList<Bankbranch>();

        Query createQuery = getCurrentSession()
                .createQuery(
                        "select distinct bb from Bankbranch bb , Bankaccount ba  where ba.bankbranch =bb and ba.type in ('RECEIPTS_PAYMENTS','RECEIPTS') and bb.bank.id=:bankId and bb.isactive=true")
                .setInteger("bankId", bankId);
        if (bankId != null) {
            List<Bankbranch> list = (List<Bankbranch>) createQuery.list();
            if (list != null && !list.isEmpty())
            {
                ss.addAll(list);
                bankBranchList.addAll(ss);
            }
        }
        return bankBranchList;
    }
}
