<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency, transparency,
  ~    accountability and the service delivery of the government organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="row add-border">
	<c:choose>
		<c:when test="${headerFields.contains('fund')}">
			<div class="col-md-3 col-xs-6 add-margin">
				<spring:message code="lbl.fund" />
			</div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
				<c:out default="N/A" value="${egBillregister.egBillregistermis.fund.name }" />
			</div>
		</c:when>
		<c:otherwise>
			<div class="col-md-3 col-xs-6 add-margin">
			</div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
			</div>
		</c:otherwise>
	</c:choose>

	<c:choose>
		<c:when test="${headerFields.contains('fundsource')}">
			<div class="col-md-3 col-xs-6 add-margin">
				<spring:message code="lbl.sourcefund" />
			</div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
				<c:out default="N/A" value="${egBillregister.egBillregistermis.fundsource.name }" />
			</div>
		</c:when>
		<c:otherwise>
			<div class="col-md-3 col-xs-6 add-margin">
			</div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
			</div>
		</c:otherwise>
	</c:choose>
</div>

<div class="row add-border">
	<c:choose>
		<c:when test="${headerFields.contains('scheme')}">
			<div class="col-md-3 col-xs-6 add-margin">
				<spring:message code="lbl.scheme" />
			</div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
				<c:out default="N/A" value="${egBillregister.egBillregistermis.scheme.name }" />
			</div>
		</c:when>
		<c:otherwise>
			<div class="col-md-3 col-xs-6 add-margin"></div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
			</div>
		</c:otherwise>
	</c:choose>

	<c:choose>
		<c:when test="${headerFields.contains('subscheme')}">
			<div class="col-md-3 col-xs-6 add-margin">
				<spring:message code="lbl.subscheme" />
			</div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
				<c:out default="N/A" value="${egBillregister.egBillregistermis.subScheme.name }" />
			</div>
		</c:when>
		<c:otherwise>
			<div class="col-md-3 col-xs-6 add-margin"></div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
			</div>
		</c:otherwise>
	</c:choose>
</div>

<div class="row add-border">
	<c:choose>
		<c:when test="${headerFields.contains('department')}">
			<div class="col-md-3 col-xs-6 add-margin">
				<spring:message code="lbl.department" />
			</div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
				<c:out default="N/A" value="${egBillregister.egBillregistermis.departmentName }" />
			</div>
		</c:when>
		<c:otherwise>
			<div class="col-md-3 col-xs-6 add-margin"></div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
			</div>
		</c:otherwise>
	</c:choose>

	<c:choose>
		<c:when test="${headerFields.contains('field')}">
			<div class="col-md-3 col-xs-6 add-margin">
				<spring:message code="lbl.field" />
				<span class="mandatory"></span>
			</div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
				<c:out default="N/A" value="${egBillregister.egBillregistermis.fieldid.name }" />
			</div>
		</c:when>
		<c:otherwise>
			<div class="col-md-3 col-xs-6 add-margin"></div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
			</div>
		</c:otherwise>
	</c:choose>
</div>
<c:choose>
	<c:when test="${headerFields.contains('functionary')}">
		<div class="row add-border">
			<div class="col-md-3 col-xs-6 add-margin">
				<spring:message code="lbl.functionaryid" />
			</div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
				<c:out default="N/A" value="${egBillregister.egBillregistermis.functionaryid.name }" />
			</div>
			<div class="col-md-3 col-xs-6 add-margin"></div>
			<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
			</div>
		</div>
	</c:when>
	<c:otherwise>
	</c:otherwise>
</c:choose>
