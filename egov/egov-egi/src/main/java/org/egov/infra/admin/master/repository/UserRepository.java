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

package org.egov.infra.admin.master.repository;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infra.persistence.entity.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Set;

import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, RevisionRepository<User, Long, Integer> {

    @QueryHints({@QueryHint(name = HINT_CACHEABLE, value = "true")})
    User findByUsername(String userName);

    List<User> findByNameContainingIgnoreCase(String userName);

    User findByEmailId(String emailId);

    User findByAadhaarNumber(String aadhaarNumber);

    List<User> findByAadhaarNumberAndType(String aadhaarNumber, UserType type);

    @Query("select distinct usr.roles from User usr where usr.username = :usrName ")
    Set<Role> findUserRolesByUserName(@Param("usrName") String userName);

    List<User> findByUsernameContainingIgnoreCaseAndTypeAndActiveTrue(String username, UserType type);

    List<User> findByNameContainingIgnoreCaseAndTypeAndActiveTrue(String name, UserType type);

    @Query("select distinct usr from User usr, IN (usr.roles) role where role.name = :roleName ")
    Set<User> findUsersByRoleName(@Param("roleName") String roleName);

    @Query("select distinct usr from User usr, IN (usr.roles) role where role.name = :roleName and usr.username = :usrName ")
    List<User> findUsersByUserAndRoleName(@Param("usrName") String userName, @Param("roleName") String roleName);

    @Query(" select count(*) from User usr where usr.username like :name||'%' ")
    Integer getUserSerialNumberByName(@Param("name") final String name);

    User findByNameAndMobileNumberAndGender(String name, String mobileNumber, Gender gender);
}
