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
package org.egov.collection.integration.services;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.OnlinePayment;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.integration.pgi.AtomAdaptor;
import org.egov.collection.integration.pgi.AxisAdaptor;
import org.egov.collection.integration.pgi.PaymentResponse;
import org.egov.collection.integration.pgi.SbimopsAdaptor;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class SchedularService {

    public static final Integer QUARTZ_SBIMOPS_RECONCILE_BULK_JOBS = 5;
    private static final Logger LOGGER = Logger.getLogger(SchedularService.class);
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private ReconciliationService reconciliationService;

    @Autowired
    private AxisAdaptor axisAdaptor;

    @Autowired
    private AtomAdaptor atomAdaptor;

    @Autowired
    private SbimopsAdaptor sbimopsAdaptor;

    @Transactional
    public void reconcileAXIS() {

        LOGGER.debug("Inside reconcileAXIS");
        final Calendar threeDaysBackCalender = Calendar.getInstance();
        threeDaysBackCalender.add(Calendar.DATE, -3);
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -30);
        final Query qry = persistenceService
                .getSession()
                .createQuery(
                        "select receipt from org.egov.collection.entity.OnlinePayment as receipt where receipt.status.code=:onlinestatuscode"
                                + " and receipt.service.code=:paymentservicecode and receipt.createdDate<:thirtyminslesssysdate")
                .setMaxResults(50);
        qry.setString("onlinestatuscode", CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
        qry.setString("paymentservicecode", CollectionConstants.SERVICECODE_AXIS);
        qry.setParameter("thirtyminslesssysdate", new Date(cal.getTimeInMillis()));
        final List<OnlinePayment> reconcileList = qry.list();

        LOGGER.debug("Thread ID = " + Thread.currentThread().getId() + ": got " + reconcileList.size() + " results.");
        if (!reconcileList.isEmpty()) {
            try {
                for (final OnlinePayment onlinePaymentObj : reconcileList) {
                    final long startTimeInMilis = System.currentTimeMillis();
                    LOGGER.info("AXIS Receiptid::::" + onlinePaymentObj.getReceiptHeader().getId());
                    PaymentResponse paymentResponse = axisAdaptor.createOfflinePaymentRequest(onlinePaymentObj);
                    if (paymentResponse != null && isNotBlank(paymentResponse.getReceiptId())) {
                        LOGGER.info("paymentResponse.getReceiptId():" + paymentResponse.getReceiptId());
                        LOGGER.info("paymentResponse.getAdditionalInfo6():" + paymentResponse.getAdditionalInfo6());
                        LOGGER.info("paymentResponse.getAuthStatus():" + paymentResponse.getAuthStatus());
                        ReceiptHeader onlinePaymentReceiptHeader = (ReceiptHeader) persistenceService.findByNamedQuery(
                                CollectionConstants.QUERY_PENDING_RECEIPT_BY_ID_AND_CITYCODE,
                                Long.valueOf(paymentResponse.getReceiptId()),
                                ApplicationThreadLocals.getCityCode());

                        if (CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS.equals(paymentResponse.getAuthStatus()))
                            reconciliationService.processSuccessMsg(onlinePaymentReceiptHeader, paymentResponse);
                        else if ((DateUtils.compareDates(onlinePaymentReceiptHeader.getCreatedDate(),
                                threeDaysBackCalender.getTime()))
                                &&
                                (onlinePaymentReceiptHeader.getOnlinePayment().getService().getCode()
                                        .equals(CollectionConstants.SERVICECODE_AXIS) &&
                                        CollectionConstants.AXIS_AUTHORISATION_CODES_WAITINGFOR_PAY_GATEWAY_RESPONSE
                                                .contains(paymentResponse.getAuthStatus()))) {
                            onlinePaymentReceiptHeader.getOnlinePayment().setAuthorisationStatusCode(
                                    paymentResponse.getAuthStatus());
                            onlinePaymentReceiptHeader.getOnlinePayment().setRemarks(paymentResponse.getErrorDescription());
                        } else
                            reconciliationService.processFailureMsg(onlinePaymentReceiptHeader, paymentResponse);

                        final long elapsedTimeInMillis = System.currentTimeMillis() - startTimeInMilis;
                        LOGGER.info("$$$$$$ Online Receipt Persisted with Receipt Number: "
                                + onlinePaymentReceiptHeader.getReceiptnumber()
                                + (onlinePaymentReceiptHeader.getConsumerCode() != null ? " and consumer code: "
                                + onlinePaymentReceiptHeader.getConsumerCode() : "")
                                + "; Time taken(ms) = "
                                + elapsedTimeInMillis);
                    }
                }
            } catch (final ApplicationRuntimeException exp) {
                LOGGER.error("AXIS payment reconciliation failed", exp);
            }
        }
    }

    @Transactional
    public void reconcileATOM(Integer modulo) {
        LOGGER.debug("Inside reconcileATOM");
        final Calendar fiveDaysBackCalender = Calendar.getInstance();
        fiveDaysBackCalender.add(Calendar.DATE, -5);
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -30);
        StringBuilder queryString = new StringBuilder(200);
        queryString.append(
                "select receipt from org.egov.collection.entity.OnlinePayment as receipt where receipt.status.code=:onlinestatuscode")
                .append(" and receipt.service.code=:paymentservicecode and receipt.createdDate<:thirtyminslesssysdate  and MOD(receipt.id, ")
                .append(CollectionConstants.QUARTZ_ATOM_RECONCILE_BULK_JOBS)
                .append(") = :modulo  order by receipt.id asc");
        final Query query = persistenceService
                .getSession()
                .createQuery(queryString.toString())
                .setMaxResults(50);
        query.setString("onlinestatuscode", CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
        query.setString("paymentservicecode", CollectionConstants.SERVICECODE_ATOM);
        query.setParameter("thirtyminslesssysdate", new Date(cal.getTimeInMillis()));
        query.setParameter("modulo", modulo);
        final List<OnlinePayment> reconcileList = query.list();
        LOGGER.debug("Thread ID = " + Thread.currentThread().getId() + ": got " + reconcileList.size() + " results.");
        if (!reconcileList.isEmpty()) {
            try {
                for (final OnlinePayment onlinePaymentObj : reconcileList) {
                    final long startTimeInMilis = System.currentTimeMillis();
                    LOGGER.info("ATOM Receiptid::::" + onlinePaymentObj.getReceiptHeader().getId());
                    PaymentResponse paymentResponse = atomAdaptor.createOfflinePaymentRequest(onlinePaymentObj);
                    if (paymentResponse != null && isNotBlank(paymentResponse.getReceiptId())) {
                        LOGGER.info("paymentResponse.getReceiptId():" + paymentResponse.getReceiptId());
                        LOGGER.info("paymentResponse.getAdditionalInfo6():" + paymentResponse.getAdditionalInfo6());
                        LOGGER.info("paymentResponse.getAuthStatus():" + paymentResponse.getAuthStatus());
                        ReceiptHeader onlinePaymentReceiptHeader = null;
                        if (isNotBlank(paymentResponse.getAdditionalInfo2())) {
                            if (paymentResponse.getAdditionalInfo2().equals(ApplicationThreadLocals.getCityCode()))
                                onlinePaymentReceiptHeader = (ReceiptHeader) persistenceService.findByNamedQuery(
                                        CollectionConstants.QUERY_PENDING_RECEIPT_BY_ID_AND_CITYCODE,
                                        Long.valueOf(paymentResponse.getReceiptId()),
                                        paymentResponse.getAdditionalInfo2());
                            else {
                                LOGGER.error("City code is not match");
                                throw new ValidationException(Arrays.asList(new ValidationError("City code is not match",
                                        "City code is not match")));
                            }
                        } else
                            onlinePaymentReceiptHeader = (ReceiptHeader) persistenceService.findByNamedQuery(
                                    CollectionConstants.QUERY_PENDING_RECEIPT_BY_ID_AND_CITYCODE,
                                    Long.valueOf(paymentResponse.getReceiptId()),
                                    ApplicationThreadLocals.getCityCode());
                        if (CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS.equals(paymentResponse.getAuthStatus()))
                            reconciliationService.processSuccessMsg(onlinePaymentReceiptHeader, paymentResponse);
                        else if ((DateUtils.compareDates(onlinePaymentReceiptHeader.getCreatedDate(),
                                fiveDaysBackCalender.getTime()))
                                &&
                                (onlinePaymentReceiptHeader.getOnlinePayment().getService().getCode()
                                        .equals(CollectionConstants.SERVICECODE_ATOM) &&
                                        CollectionConstants.ATOM_AUTHORISATION_CODES_WAITINGFOR_PAY_GATEWAY_RESPONSE
                                                .contains(paymentResponse.getAuthStatus()))) {
                            onlinePaymentReceiptHeader.getOnlinePayment().setAuthorisationStatusCode(
                                    paymentResponse.getAuthStatus());
                            onlinePaymentReceiptHeader.getOnlinePayment().setRemarks(paymentResponse.getErrorDescription());
                        } else
                            reconciliationService.processFailureMsg(onlinePaymentReceiptHeader, paymentResponse);

                        final long elapsedTimeInMillis = System.currentTimeMillis() - startTimeInMilis;
                        LOGGER.info("$$$$$$ Online Receipt Persisted with Receipt Number: "
                                + onlinePaymentReceiptHeader.getReceiptnumber()
                                + (onlinePaymentReceiptHeader.getConsumerCode() != null ? " and consumer code: "
                                + onlinePaymentReceiptHeader.getConsumerCode() : "")
                                + "; Time taken(ms) = "
                                + elapsedTimeInMillis);
                    }
                }
            } catch (final ApplicationRuntimeException exp) {
                LOGGER.error("ATOM payment reconciliation failed", exp);
            }
        }
    }

    @Transactional
    public void reconcileSBIMOPS(Integer modulo) {
        try {
            LOGGER.debug("Inside reconcileSBIMOPS");
            final List<OnlinePayment> reconcileList = getPendingOnlineTransaction(CollectionConstants.SERVICECODE_SBIMOPS,
                    modulo);

            LOGGER.debug("Thread ID = " + Thread.currentThread().getId() + ": got " + reconcileList.size() + " results.");
            if (reconcileList != null && !reconcileList.isEmpty()) {
                for (final OnlinePayment onlinePaymentObj : reconcileList) {
                    LOGGER.info("SBIMOPS Receiptid::::" + onlinePaymentObj.getReceiptHeader().getId());
                    PaymentResponse paymentResponse = sbimopsAdaptor.createOfflinePaymentRequest(onlinePaymentObj);
                    if (paymentResponse == null)
                        LOGGER.debug("Online Receipt Persisted for the Receipt Id: " + onlinePaymentObj.getReceiptHeader().getId()
                                + " Response is null");
                    else if (isNotBlank(paymentResponse.getReceiptId())) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("paymentResponse.getReceiptId():" + paymentResponse.getReceiptId());
                            LOGGER.debug("paymentResponse.getAdditionalInfo6():" +
                                    defaultIfBlank(paymentResponse.getAdditionalInfo6(), " consumer code is blank "));
                            LOGGER.debug("paymentResponse.getAuthStatus():" + paymentResponse.getAuthStatus());
                        }
                        processOnlineTransaction(paymentResponse);
                    }
                }
            }
        } catch (final ApplicationRuntimeException exp) {
            LOGGER.error("SBIMOPS payment reconciliation failed", exp);
        }
    }

    public List<OnlinePayment> getPendingOnlineTransaction(String paymentServiceCode, Integer modulo) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -30);
        StringBuilder queryString = new StringBuilder(200);
        queryString.append(
                "select receipt from org.egov.collection.entity.OnlinePayment as receipt where receipt.status.code=:onlinestatuscode")
                .append(" and receipt.service.code=:paymentservicecode and receipt.createdDate<:thirtyminslesssysdate  and MOD(receipt.id, ")
                .append(QUARTZ_SBIMOPS_RECONCILE_BULK_JOBS)
                .append(") = :modulo  order by receipt.id asc");
        final Query query = persistenceService
                .getSession()
                .createQuery(queryString.toString())
                .setMaxResults(50);
        query.setString("onlinestatuscode", CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
        query.setString("paymentservicecode", paymentServiceCode);
        query.setParameter("thirtyminslesssysdate", new Date(cal.getTimeInMillis()));
        query.setParameter("modulo", modulo);
        return query.list();
    }

    private void processOnlineTransaction(PaymentResponse paymentResponse) {

        // If the transaction status is Pending, Keeping the transaction in pending status for five days from transaction date.
        final Calendar fiveDaysBackCalender = Calendar.getInstance();
        fiveDaysBackCalender.add(Calendar.DATE, -5);

        ReceiptHeader onlinePaymentReceiptHeader = (ReceiptHeader) persistenceService.findByNamedQuery(
                CollectionConstants.QUERY_PENDING_RECEIPT_BY_ID_AND_CITYCODE,
                Long.valueOf(paymentResponse.getReceiptId()),
                ApplicationThreadLocals.getCityCode());

        if (CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS.equals(paymentResponse.getAuthStatus()))
            reconciliationService.processSuccessMsg(onlinePaymentReceiptHeader, paymentResponse);
        else if (CollectionConstants.PGI_AUTHORISATION_CODE_PENDING.equals(paymentResponse.getAuthStatus()) &&
                DateUtils.compareDates(onlinePaymentReceiptHeader.getCreatedDate(), fiveDaysBackCalender.getTime())) {
            onlinePaymentReceiptHeader.getOnlinePayment().setAuthorisationStatusCode(
                    paymentResponse.getAuthStatus());
            onlinePaymentReceiptHeader.getOnlinePayment().setRemarks(paymentResponse.getErrorDescription());
        } else
            reconciliationService.processFailureMsg(onlinePaymentReceiptHeader, paymentResponse);
    }
}
