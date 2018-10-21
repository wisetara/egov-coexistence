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

package org.egov.infra.utils;

import org.egov.infra.persistence.utils.DatabaseSequenceCreator;
import org.egov.infra.persistence.utils.DatabaseSequenceProvider;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

import static java.lang.String.format;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.upperCase;

/**
 * Generic application number generator class, this implementation will return ERP wide
 * application number, which consist of 5 digit sequence number (padded with trialing
 * zero if sequence length is less than 5 digit) followed by current year and 2 random alphabets.
 * Sequence number, year and alphabets are separated by hyphen.<br/>
 * eg: 00010-2016-QX<br/>
 * Sequence number will be reset to 1 on every year
 */
@Service
public class ApplicationNumberGenerator {
    private static final String APP_NUMBER_SEQ_PREFIX = "SEQ_APPLICATION_NUMBER%s";
    private static final String APP_NUMBER_FORMAT = "%05d-%s-%s";

    @Autowired
    private DatabaseSequenceCreator databaseSequenceCreator;

    @Autowired
    private DatabaseSequenceProvider databaseSequenceProvider;

    @Transactional
    public String generate() {
        String currentYear = DateUtils.currentYear();
        String sequenceName = format(APP_NUMBER_SEQ_PREFIX, currentYear);
        Serializable sequenceNumber;
        try {
            sequenceNumber = databaseSequenceProvider.getNextSequence(sequenceName);
        } catch (SQLGrammarException e) {
            databaseSequenceCreator.createSequence(sequenceName);
            sequenceNumber = databaseSequenceProvider.getNextSequence(sequenceName);
        }
        return format(APP_NUMBER_FORMAT, sequenceNumber, currentYear, upperCase(randomAlphabetic(2)));
    }

}
