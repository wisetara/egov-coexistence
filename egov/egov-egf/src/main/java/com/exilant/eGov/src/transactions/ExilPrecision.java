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
/*
 * Created on Jan 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.transactions;

import org.apache.log4j.Logger;

/**
 * @author siddhu
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class ExilPrecision
{
    private static final Logger LOGGER = Logger.getLogger(ExilPrecision.class);

    public static double convertToDouble(final double dNum, final int precision)
    {
        // precision++;
        if (Double.isNaN(dNum))
            return 0;
        final int afterPoint = (int) Math.pow(10, precision);
        String fraction = "0.";
        for (int i = 0; i < precision; i++)
            fraction += "0";
        fraction += "5";
        final double adjustFraction = Double.parseDouble(fraction);
        final double retNum = Math.floor((dNum + adjustFraction) * afterPoint) / afterPoint;
        return retNum;
    }

    public static String convertToString(final double dNum, final int precision)
    {
        // precision++;
        if (Double.isNaN(dNum))
            return "0";
        final int afterPoint = (int) Math.pow(10, precision);
        String fraction = "0.";
        for (int i = 0; i < precision; i++)
            fraction += "0";
        fraction += "5";
        final double adjustFraction = Double.parseDouble(fraction);
        final double retNum = Math.floor((dNum + adjustFraction) * afterPoint) / afterPoint;
        return String.valueOf(retNum);
    }

    public static double convertToDouble(final String sNum, final int precision)
    {
        // precision++;
        double dNum = 0;
        try {
            dNum = Double.parseDouble(sNum);
        } catch (final Exception e) {
            LOGGER.error("There is error " + e.getMessage());
            return 0;
        }
        final int afterPoint = (int) Math.pow(10, precision);
        String fraction = "0.";
        for (int i = 0; i < precision; i++)
            fraction += "0";
        fraction += "5";
        final double adjustFraction = Double.parseDouble(fraction);
        final double retNum = Math.floor((dNum + adjustFraction) * afterPoint) / afterPoint;
        return retNum;
    }

    public static String convertToString(final String sNum, final int precision)
    {
        // precision++;
        double dNum = 0;
        try {
            dNum = Double.parseDouble(sNum);
        } catch (final Exception e) {
            LOGGER.error("There is error " + e.getMessage());
            return "0";
        }
        final int afterPoint = (int) Math.pow(10, precision);
        String fraction = "0.";
        for (int i = 0; i < precision; i++)
            fraction += "0";
        fraction += "5";
        final double adjustFraction = Double.parseDouble(fraction);
        final double retNum = Math.floor((dNum + adjustFraction) * afterPoint) / afterPoint;
        return String.valueOf(retNum);
    }

    public static void main(final String[] args)
    {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(ExilPrecision.convertToString("18.245287987", 2));

    }
}
