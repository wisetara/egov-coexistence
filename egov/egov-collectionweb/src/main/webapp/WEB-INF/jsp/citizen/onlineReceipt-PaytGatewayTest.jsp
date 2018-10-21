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


<%@ include file="/includes/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="coc.title"/></title>
<script type="text/javascript">

</script>
</head>
<body>
<s:form name="onlinePaytDetails" action="/citizen/onlineReceipt.action">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" border="1">
	<tr>
		<td>Receipt Id : </td>
		<td><s:textfield name="testReceiptId" id="testReceiptId" value="" /></td>
	</tr>
	<tr>
		<td>Authorisation Status Code : </td>
		<td><s:textfield name="testAuthStatusCode" id="testAuthStatusCode" value="" /></td>
	</tr>
	</table>

<br/>
<div class="buttonbottom">
	<s:submit type="button" value="Test Online Payment Message" method="acceptMessageFromPaymentGateway" align="left"/>
	<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close"/>
</div>
</s:form>
</body>
</html>
