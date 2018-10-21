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
package org.egov.collection.xml.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptInstrumentInfo;

public class BillReceiptInfoConverter implements Converter {

    @Override
    public void marshal(final Object value, final HierarchicalStreamWriter writer, final MarshallingContext context) {

        final BillReceiptInfo receiptInfo = (BillReceiptInfo) value;
        ConverterUtil.createNode(writer, "event", receiptInfo.getEvent());
        ConverterUtil.createNode(writer, "referenceNumber", receiptInfo.getBillReferenceNum());
        ConverterUtil.createNode(writer, "receiptNumber", receiptInfo.getReceiptNum());
        ConverterUtil.createNode(writer, "receiptDate", String.valueOf(receiptInfo.getReceiptDate()));
        ConverterUtil.createNode(writer, "location", receiptInfo.getReceiptLocation() == null ? "" : receiptInfo
                .getReceiptLocation().getName());
        ConverterUtil.createNode(writer, "status", receiptInfo.getReceiptStatus().getCode());
        ConverterUtil.createNode(writer, "PayeeName", receiptInfo.getPayeeName());
        ConverterUtil.createNode(writer, "PayeeAddress", receiptInfo.getPayeeAddress());
        writer.startNode("Receipt-Accounts");
        for (final ReceiptAccountInfo receiptAccInfo : receiptInfo.getAccountDetails())
            context.convertAnother(receiptAccInfo);
        writer.endNode();

        writer.startNode("Receipt-Instruments");
        for (final ReceiptInstrumentInfo receiptAccInfo : receiptInfo.getInstrumentDetails())
            context.convertAnother(receiptAccInfo);
        writer.endNode();
    }

    @Override
    public Object unmarshal(final HierarchicalStreamReader arg0, final UnmarshallingContext arg1) {
        return null;
    }

    @Override
    public boolean canConvert(final Class clazz) {
        return clazz.equals(BillReceiptInfoImpl.class);
    }
}
