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

package org.egov.commons;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Unique(id = "id", tableName = "EG_REGIONALHIERARCHY", fields = { "name","code" }, columnName = {"name","code" }, enableDfltMsg = true)
@Table(name = "EG_REGIONALHIERARCHY")
@SequenceGenerator(name = RegionalHierarchy.SEQ_REGIONALHIERARCHY, sequenceName = RegionalHierarchy.SEQ_REGIONALHIERARCHY, allocationSize = 1)
public class RegionalHierarchy extends AbstractAuditable {

    private static final long serialVersionUID = -6147843882272796650L;

    public static final String SEQ_REGIONALHIERARCHY = "seq_eg_regionlaHierarchy";

    @Id
    @GeneratedValue(generator = SEQ_REGIONALHIERARCHY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Length(max = 512)
    @SafeHtml
    @NotNull
    private String name;

    @NotNull
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    @Fetch(value = FetchMode.SELECT)
    private RegionalHierarchy parent;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private RegionalHierarchyType type;

    private boolean isHistory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public RegionalHierarchy getParent() {
        return parent;
    }

    public void setParent(RegionalHierarchy parent) {
        this.parent = parent;
    }

    public RegionalHierarchyType getType() {
        return type;
    }

    public void setType(RegionalHierarchyType type) {
        this.type = type;
    }

    public boolean isHistory() {
        return isHistory;
    }

    public void setHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }


}
