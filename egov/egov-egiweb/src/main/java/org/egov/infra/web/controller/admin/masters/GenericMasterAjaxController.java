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

package org.egov.infra.web.controller.admin.masters;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class GenericMasterAjaxController {

    private static final String BLOCK = "Block";
    private static final String REVENUE_HIERARCHY_TYPE = "REVENUE";
    private static final String DISPLAY_KEY = "Text";
    private static final String VALUE_KEY = "Value";

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CrossHierarchyService crossHierarchyService;

    @GetMapping("/boundarytype/ajax/boundarytypelist-for-hierarchy")
    @ResponseBody
    public void getBoundaryTypeByHierarchyType(@RequestParam Long hierarchyTypeId, HttpServletResponse response)
            throws IOException {
        final List<BoundaryType> boundaryTypes = boundaryTypeService.getAllBoundarTypesByHierarchyTypeId(hierarchyTypeId);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(buildJSONString(boundaryTypes), response.getWriter());
    }

    @GetMapping("/boundaries-by-boundaryType")
    @ResponseBody
    public void getBoundariesByBoundaryType(@RequestParam Long boundaryTypeId, HttpServletResponse response)
            throws IOException {
        BoundaryType boundaryType = boundaryTypeService.getBoundaryTypeById(boundaryTypeId);
        final List<Boundary> boundaries = boundaryService.getAllBoundariesOrderByBoundaryNumAsc(boundaryType);
        final JsonArray jsonArray = new JsonArray();
        for (final Boundary boundary : boundaries) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(DISPLAY_KEY, boundary.getLocalName());
            jsonObject.addProperty(VALUE_KEY, boundary.getId());
            jsonArray.add(jsonObject);
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(jsonArray.toString(), response.getWriter());
    }

    @GetMapping("/check-is-root")
    @ResponseBody
    public boolean isRootBoundary(@RequestParam Long boundaryTypeId, @RequestParam Long hierarchyTypeId) {
        final BoundaryType boundaryType = boundaryTypeService.getBoundaryTypeByIdAndHierarchyType(boundaryTypeId,
                hierarchyTypeId);
        return boundaryType.getParent() != null && boundaryType.getParent().getId() == 0;
    }

    private String buildJSONString(List<BoundaryType> boundaryTypes) {
        final JsonArray jsonArray = new JsonArray();
        for (final BoundaryType boundaryType : boundaryTypes) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(DISPLAY_KEY, boundaryType.getName());
            jsonObject.addProperty(VALUE_KEY, boundaryType.getId());
            jsonArray.add(jsonObject);
        }

        return jsonArray.toString();
    }

    /*
     * Used in ajax validation to check if child exists for a boundary type -
     * Add child screen
     */
    @GetMapping("/boundarytype/ajax/checkchild")
    @ResponseBody
    public boolean isChildBoundaryTypePresent(@RequestParam Long parentId) {
        return boundaryTypeService.getBoundaryTypeByParent(parentId) != null;
    }

    @GetMapping("/userRole/ajax/rolelist-for-user")
    @ResponseBody
    public void getRolesByUserName(@RequestParam String username, HttpServletResponse response) throws IOException {
        if (username != null) {
            final Set<Role> roles = userService.getRolesByUsername(username);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            IOUtils.write(buildRoles(roles), response.getWriter());
        }
    }

    private String buildRoles(Set<Role> roles) {
        final JsonArray jsonArray = new JsonArray();
        for (final Role role : roles) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(VALUE_KEY, role.getId());
            jsonObject.addProperty(DISPLAY_KEY, role.getName());
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }

    @GetMapping(value = "/userRole/ajax/userlist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void getAllActiveUserByNameLike(@RequestParam String userName, HttpServletResponse response) throws IOException {
        final List<User> userList = userService.findAllByMatchingUserNameForType(userName, UserType.EMPLOYEE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(buildUser(userList), response.getWriter());
    }

    private String buildUser(List<User> users) {
        final JsonArray jsonArray = new JsonArray();
        for (final User user : users) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(VALUE_KEY, user.getId());
            jsonObject.addProperty(DISPLAY_KEY, user.getUsername());
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }

    @GetMapping({"/boundary/ajaxBoundary-blockByLocality", "/public/boundary/ajaxBoundary-blockByLocality"})
    public void blockByLocality(@RequestParam Long locality, HttpServletResponse response) throws IOException {
        BoundaryType blockType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyTypeName(BLOCK, REVENUE_HIERARCHY_TYPE);
        List<Boundary> blocks = crossHierarchyService.getParentBoundaryByChildBoundaryAndParentBoundaryType(locality, blockType.getId());
        List<Boundary> streets = boundaryService.getActiveChildBoundariesByBoundaryId(locality);
        final List<JsonObject> wardJsonObjs = new ArrayList<>();
        final List<Long> boundaries = new ArrayList<>();
        for (final Boundary block : blocks) {
            final Boundary ward = block.getParent();
            final JsonObject jsonObject = new JsonObject();
            if (!boundaries.contains(ward.getId())) {
                jsonObject.addProperty("wardId", ward.getId());
                jsonObject.addProperty("wardName", ward.getName());
            }
            jsonObject.addProperty("blockId", block.getId());
            jsonObject.addProperty("blockName", block.getName());
            wardJsonObjs.add(jsonObject);
            boundaries.add(ward.getId());
        }
        final List<JsonObject> streetJsonObjs = new ArrayList<>();
        for (final Boundary street : streets) {
            final JsonObject streetObj = new JsonObject();
            streetObj.addProperty("streetId", street.getId());
            streetObj.addProperty("streetName", street.getName());
            streetJsonObjs.add(streetObj);
        }
        final Map<String, List<JsonObject>> map = new HashMap<>();
        map.put("boundaries", wardJsonObjs);
        map.put("streets", streetJsonObjs);
        final JsonObject bj = new JsonObject();
        bj.add("results", new Gson().toJsonTree(map));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(bj.toString(), response.getWriter());
    }

    @GetMapping({"/boundary/ajaxBoundary-blockByWard", "/public/boundary/ajaxBoundary-blockByWard"})
    public void blockByWard(@RequestParam Long wardId, HttpServletResponse response) throws IOException {
        List<Boundary> blocks = boundaryService.getActiveChildBoundariesByBoundaryId(wardId);
        final List<JsonObject> jsonObjects = new ArrayList<>();
        for (final Boundary block : blocks) {
            final JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("blockId", block.getId());
            jsonObj.addProperty("blockName", block.getName());
            jsonObjects.add(jsonObj);
        }
        IOUtils.write(jsonObjects.toString(), response.getWriter());
    }

    @GetMapping("/boundary/ward-bylocality")
    public void wardsByLocality(@RequestParam Long locality, HttpServletResponse response) throws IOException {
        BoundaryType wardBoundaryType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyTypeName("Ward", "ADMINISTRATION");
        List<Boundary> wards = crossHierarchyService.getParentBoundaryByChildBoundaryAndParentBoundaryType(locality, wardBoundaryType.getId());
        final List<JsonObject> jsonObjects = new ArrayList<>();
        for (final Boundary block : wards) {
            final JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("wardId", block.getId());
            jsonObj.addProperty("wardName", block.getName());
            jsonObjects.add(jsonObj);
        }
        IOUtils.write(jsonObjects.toString(), response.getWriter());
    }

}
