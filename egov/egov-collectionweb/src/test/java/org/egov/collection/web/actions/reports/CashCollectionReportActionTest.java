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

/**
 *
 */
package org.egov.collection.web.actions.reports;


/**
 * JUnit test cases for cash collection report action
 */
@SuppressWarnings("unchecked")
public class CashCollectionReportActionTest {/* extends
		AbstractPersistenceServiceTest {
	private CashCollectionReportAction action;
	private CollectionsUtil collectionsUtil;
	private ReportService reportService;
	private Map actionSession = new HashMap();

	@Before
	public void setupAction() {
		action = new CashCollectionReportAction();
		action.setPersistenceService(genericService);
		action.setSession(actionSession);

		collectionsUtil = EasyMock.createMock(CollectionsUtil.class);
		action.setCollectionsUtil(collectionsUtil);

		EasyMock.expect(collectionsUtil.getAllCounters()).andReturn(
				new ArrayList<Location>());
		EasyMock.expect(collectionsUtil.getReceiptCreators()).andReturn(
				new ArrayList<User>());
		EasyMock.expect(collectionsUtil.getReceiptZoneList()).andReturn(
				new ArrayList<Boundary>());
		EasyMock.replay(collectionsUtil);
		action.prepare();
	}

	@Test
	public void testGetModel() {
		assertNull(action.getModel());
	}

	@Test
	public void testPrepare() {
		EasyMock.verify(collectionsUtil);
		assertEquals(action.getCounterId(), -1);
		assertEquals(action.getUserId(), -1);
		assertNotNull(action.getFromDate());
		assertNotNull(action.getToDate());
		assertNull(action.getInstrumentStatus());
		assertEquals(action.getBoundaryId(), -1);
	}

	@Test
	public void testSetGet() {
		Long counterId = 10L;
		Long userId = 20L;
		Date fromDate = new Date();
		Date toDate = new Date();
		Long boundaryId = 20L;
		action.setCounterId(counterId);
		action.setUserId(userId);
		action.setFromDate(fromDate);
		action.setToDate(toDate);
		action.setInstrumentStatus(CollectionConstants.INSTRUMENT_NEW_STATUS);
		action.setBoundaryId(boundaryId);
		assertEquals(counterId, action.getCounterId());
		assertEquals(userId, action.getUserId());
		assertEquals(fromDate, action.getFromDate());
		assertEquals(toDate, action.getToDate());
		assertEquals(CollectionConstants.INSTRUMENT_NEW_STATUS, action
				.getInstrumentStatus());
		assertEquals(boundaryId, action.getBoundaryId());
	}

	@Test
	public void testCriteria() {
		assertEquals(BaseFormAction.INDEX, action.criteria());
	}

	@Test
	public void testReport() {
		ReportOutput reportOutput = new ReportOutput();
		reportService = createMock(ReportService.class);
		action.setReportService(reportService);

		expect(reportService.createReport(isA(ReportRequest.class))).andReturn(
				reportOutput);

		replay(reportService);
		assertEquals(CashCollectionReportAction.REPORT, action.report());

		LRUCache<Integer, ReportOutput> reportOutputCache = (LRUCache<Integer, ReportOutput>)actionSession
				.get(ReportConstants.ATTRIB_EGOV_REPORT_OUTPUT_MAP);
		assertTrue(reportOutputCache.containsValue(reportOutput));
		verify(reportService);
	}
*/}
