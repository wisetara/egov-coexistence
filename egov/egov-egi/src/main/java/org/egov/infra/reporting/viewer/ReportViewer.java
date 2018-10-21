/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency, transparency,
 *    accountability and the service delivery of the government organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.infra.reporting.viewer;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;

import static org.egov.infra.utils.ApplicationConstant.CONTENT_DISPOSITION;

@Component("reportViewer")
public class ReportViewer implements HttpRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportViewer.class);
    private static final String REPORT_ERROR_RESPONSE = "<html><body><b>Report not generated, Reason : %s!</b></body></html>";

    @Autowired
    private ReportViewerUtil reportViewerUtil;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reportId = request.getParameter(ReportConstants.REQ_PARAM_REPORT_ID);
        try {
            ReportOutput reportOutput = reportViewerUtil.getReportOutputFormCache(reportId);
            if (reportOutput == null) {
                renderError(response, "Report output not available");
                return;
            }

            ReportFormat reportFormat = reportOutput.getReportFormat();
            if (reportFormat == null) {
                renderError(response, "Report format not available");
                return;
            }

            byte[] reportData = reportOutput.getReportOutputData();
            if (reportData == null) {
                renderError(response, "Report data not available");
                return;
            }

            renderReport(response, reportOutput);
        } catch (Exception e) {
            LOGGER.error("Invalid report id [{}]", reportId, e);
            renderError(response, "Report can not be rendered");
        } finally {
            reportViewerUtil.removeReportOutputFromCache(reportId);
        }
    }

    private void renderReport(HttpServletResponse resp, ReportOutput reportOutput) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(resp.getOutputStream())) {
            resp.setHeader(CONTENT_DISPOSITION, reportOutput.reportDisposition());
            resp.setContentType(ReportViewerUtil.getContentType(reportOutput.getReportFormat()));
            resp.setContentLength(reportOutput.getReportOutputData().length);
            outputStream.write(reportOutput.getReportOutputData());
        } catch (Exception e) {
            LOGGER.error("Exception in rendering report response with format [{}]!", reportOutput.getReportFormat(), e);
            throw new ApplicationRuntimeException("Error occurred in report viewer", e);
        }
    }

    private void renderError(HttpServletResponse resp, String error) {
        byte[] errorResponse = String.format(REPORT_ERROR_RESPONSE, error).getBytes();
        try (BufferedOutputStream outputStream = new BufferedOutputStream(resp.getOutputStream())) {
            resp.setContentType(ReportViewerUtil.getContentType(ReportFormat.HTM));
            resp.setContentLength(errorResponse.length);
            outputStream.write(errorResponse);
        } catch (Exception e) {
            LOGGER.error("Error occurred while preparing report error response!", e);
            throw new ApplicationRuntimeException("Error occurred in report viewer", e);
        }
    }
}
