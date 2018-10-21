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

package org.egov.common.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "eg_uom")
@Unique(id = "id", tableName = "eg_uom", fields = { "uom" }, columnName = { "uom" }, enableDfltMsg = true)
@SequenceGenerator(name = UOM.SEQ_UOM, sequenceName = UOM.SEQ_UOM, allocationSize = 1)
public class UOM extends AbstractAuditable {

    private static final long serialVersionUID = 8964393733499647786L;

    public static final String SEQ_UOM = "SEQ_EG_UOM";

    @Id
    @GeneratedValue(generator = SEQ_UOM, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "uomcategoryid")
    private UOMCategory uomCategory;

    @NotNull
    @Length(min = 1, max = 25)
    private String uom;

    @Length(min = 1, max = 25)
    private String narration;

    @NotNull
    @Column(name = "conv_factor")
    private BigDecimal convFactor;

    @NotNull
    private Boolean baseuom;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public UOMCategory getUomCategory() {
        return uomCategory;
    }

    public void setUomCategory(final UOMCategory uomCategory) {
        this.uomCategory = uomCategory;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(final String uom) {
        this.uom = uom;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public BigDecimal getConvFactor() {
        return convFactor;
    }

    public void setConvFactor(final BigDecimal convFactor) {
        this.convFactor = convFactor;
    }

    public Boolean getBaseuom() {
        return baseuom;
    }

    public void setBaseuom(final Boolean baseuom) {
        this.baseuom = baseuom;
    }

}
