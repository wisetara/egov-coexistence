<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency, transparency,
  ~    accountability and the service delivery of the government organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%@ tag body-content="empty" dynamic-attributes="true" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:eval expression="@environment.getProperty('captcha.strength')" var="strength"/>
<c:choose>
    <c:when test="${strength == 'high'}" >
        <script src='https://www.google.com/recaptcha/api.js' async defer></script>
        <div id="g-recaptcha" class="g-recaptcha" data-sitekey="${sessionScope.siteKey}"></div>
    </c:when>
    <c:when test="${strength == 'low'}">
        <div class="row">
            <div class="col-sm-12 col-md-12 col-lg-12">
                <br/>
                <img id="captchaimg" style="border:1px dashed DarkSlateBlue;">
                <i class="fa fa-refresh" id="refresh" style="cursor: pointer" title="Choose a different word"></i>
                <br/><br/>
                <input type='text' class="form-control" name='j_captcha_response' value='' placeholder="Enter the above displayed text"
                       required="required" style="width:95%;margin-left:19px">
                <input type='hidden' name='j_captcha_key' value='' id="captchakey" required="required">
            </div>
        </div>
        <script>
            $("#refresh").click(function () {
                var key = Date.parse(new Date()).toString();
                $("#captchakey").val(key);
                $("#captchaimg").attr('src', '/egi/jcaptcha?key=' + key);
            });
            $("#refresh").trigger('click');
        </script>
    </c:when>
</c:choose>
