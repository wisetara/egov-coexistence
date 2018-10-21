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
package org.egov.infra.microservice.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class ResponseInfo {

    private String apiId;

    private String ver;

    private String ts;

    private String resMsgId;

    private String msgId;

    private String status;

    public ResponseInfo(String apiId, String ver, String ts, String resMsgId, String msgId, String status) {
		this.apiId = apiId;
		this.ver = ver;
		this.ts = ts;
		this.resMsgId = resMsgId;
		this.msgId = msgId;
		this.status = status;
	}

    public ResponseInfo(){}

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getResMsgId() {
		return resMsgId;
	}

	public void setResMsgId(String resMsgId) {
		this.resMsgId = resMsgId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ResponseInfo [apiId=" + apiId + ", ver=" + ver + ", ts=" + ts + ", resMsgId=" + resMsgId + ", msgId="
				+ msgId + ", status=" + status + "]";
	}
	 public enum StatusEnum {
	        SUCCESSFUL("SUCCESSFUL"),

	        FAILED("FAILED");

	        private String value;

	        StatusEnum(String value) {
	          this.value = value;
	        }

	        @Override
	        @JsonValue
	        public String toString() {
	          return String.valueOf(value);
	        }

	        @JsonCreator
	        public static StatusEnum fromValue(String text) {
	          for (StatusEnum b : StatusEnum.values()) {
	            if (String.valueOf(b.value).equals(text)) {
	              return b;
	            }
	          }
	          return null;
	        }
	      }


}
