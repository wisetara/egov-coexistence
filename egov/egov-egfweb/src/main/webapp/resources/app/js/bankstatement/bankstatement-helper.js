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

var $bankBranchId = 0;

$(document).ready(function () {
    $bankBranchId = $('#bankbranch').val();
});

jQuery('#btnsearch').click(function (e) {

    callAjaxSearch();
});

$('#bank').change(function () {
    $bankBranchId = "";
    $('#bankbranch').empty();
    $('#bankbranch').append($('<option>').text('Select from below').attr('value', ''));
    if ($('#bank').val() != "")
        loadBankBranches($('#bank').val());
    else
        loadBankBranches(0);
});

$('#bankbranch').change(function () {
    $bankBranchId = "";
    $('#bankaccount').empty();
    $('#bankaccount').append($('<option>').text('Select from below').attr('value', ''));
    if ($('#bankbranch').val() != "")
        loadBankAccounts($('#bankbranch').val());
});

function getFormData($form) {
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function (n, i) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function loadBankBranches(bankId) {
    $.ajax({
        method: "GET",
        url: "/services/EGF/common/getbankbranchesbybankid",
        data: {
            bankId: bankId
        },
        async: true
    }).done(
        function (response) {
            $('#bankbranch').empty();
            $('#bankbranch').append($("<option value=''>Select from below</option>"));
            $.each(response, function (index, value) {
                var selected = "";
                if ($bankBranchId && $bankBranchId == value.id) {
                    selected = "selected";
                }
                $('#bankbranch').append($('<option ' + selected + '>').text(value.branchname).attr('value', value.id));
            });
        });
}
function loadBankAccounts(branchId) {
    $.ajax({
        method: "GET",
        url: "/services/EGF/common/getbankaccountbybranchid",
        data: {
            branchId: branchId
        },
        async: true
    }).done(
        function (response) {
            $('#bankaccount').empty();
            $('#bankaccount').append($("<option value=''>Select from below</option>"));
            $.each(response, function (index, value) {
                var selected = "";
                if ($bankBranchId && $bankBranchId == value.id) {
                    selected = "selected";
                }
                $('#bankaccount').append($('<option ' + selected + '>').text(value.accountnumber).attr('value', value.id));
            });
        });
}


function callAjaxSearch() {
    drillDowntableContainer = jQuery("#resultTable");
    jQuery('.report-section').removeClass('display-hide');
    reportdatatable = drillDowntableContainer.dataTable({
        ajax: {
            url: "/services/EGF/bankstatement/ajaxsearch",
            type: "POST",
            "data": getFormData(jQuery('form'))
        },

        "bDestroy": true,
        'bAutoWidth': false,
        "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
        "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
        "oTableTools": {
            "sSwfPath": "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
            "aButtons": []
        },
        "fnRowCallback": function (row, data, index) {
            if (data.fileStoreId != null)
                $('td:eq(2)', row).html('<a href="javascript:void(0);" onclick="downloadFile(\''+ data.fileStoreId + '\')">'+ data.filename + '</a>');
        },
        buttons: [
            {
                extend: 'print',
                title: 'Bank Statement',
                filename: 'Bank Statements'
            }, {
                extend: 'pdf',
                title: 'Bank Statement',
                filename: 'Bank Statements'
            }, {
                extend: 'excel',
                filename: 'Bank Statements'
            }
        ],
        aaSorting: [],
        columns: [{
            "data": "filename",
            "sClass": "text-left"
        }, {
            "data": "fieldate",
            "sClass": "text-left"
        }, {
            "data": "file",
            "sClass": "text-left"
        }]
    });
}
function downloadFile(fileStoreId) {
    window.open("/services/EGF/bankstatement/downloadDoc?fileStoreId=" + fileStoreId, '',
        'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
