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

package org.egov.infra.web.spring.handler;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.exception.ApplicationValidationException;
import org.egov.infra.exception.MicroServiceInvalidTokenException;
import org.egov.infra.exception.MicroServiceNotAuthroizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = Controller.class)
public final class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String DEFAULT_ERROR_VIEW = "/error/500";
    private static final String MS_INVALID_TOKEN="/error/msinvalidtoken";
    private static final String MS_NOTAUTHROIZED_TOKEN="/error/msnotauthroized";
    private static final String ERROR_MESSAGE = "An error occurred while processing the request";
    private static final String VALIDATION_ERROR_MESSAGE = "Validation failed";

    @ExceptionHandler({Exception.class, ApplicationRuntimeException.class})
    public RedirectView handleGenericException(HttpServletRequest request, Exception e) {
        LOG.error(ERROR_MESSAGE, e);
        return errorView(request, e.getMessage(),DEFAULT_ERROR_VIEW);
    }

    @ExceptionHandler(ApplicationValidationException.class)
    public RedirectView handleValidationException(HttpServletRequest request, ApplicationValidationException e) {
        if (LOG.isWarnEnabled())
            LOG.warn(VALIDATION_ERROR_MESSAGE, e);
        return errorView(request, e.getMessage(),DEFAULT_ERROR_VIEW);
    }

    @ExceptionHandler(MicroServiceInvalidTokenException.class)
    public RedirectView handleMicroServiceInvalidTokenException(HttpServletRequest request,MicroServiceInvalidTokenException e){
        LOG.error(ERROR_MESSAGE, e);
        return errorView(request, e.getMessage(),MS_INVALID_TOKEN);
    }

    @ExceptionHandler(MicroServiceNotAuthroizedException.class)
    public RedirectView handleMicroServiceNotAuthroizedException(HttpServletRequest request,MicroServiceNotAuthroizedException e){
        LOG.error(ERROR_MESSAGE, e);
        return errorView(request, e.getMessage(),MS_NOTAUTHROIZED_TOKEN);
    }

    public RedirectView errorView(HttpServletRequest request, String message,String view) {
        RedirectView rw = new RedirectView(view, true);
        FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
        if (outputFlashMap != null) {
            outputFlashMap.put("error", message);
            outputFlashMap.put("url", request.getRequestURL());
        }
        return rw;
    }
}
