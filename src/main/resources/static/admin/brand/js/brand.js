$(document).ready(function() {

	// Show brands list when opening page
	findAllBrands(1);

	// Show brands list when clicking pagination button
	$('.pagination').on('click', '.page-link', function() {
		const pagerNumber = $(this).attr("data-index");
		findAllBrands(pagerNumber);
	})

	const $brandInfoForm = $('#brandInfoForm');
	const $brandInfoModal = $('#brandInfoModal');

	// Search when press Enter
	$('.form-search').keypress((e) => {
        if (e.which === 13) {
            $('.info').submit();
            searchBrandName(1, true);
    	}
    })

	// Search brand when clicking Search button 
	$('#searchBrand').on('click', function() {
		searchBrandName(1, true);
	})
	

	// Rest form
	$('#restPage').on('click', function() {
		$('#keyword').val("");
		findAllBrands(1);
	})

	// Show add brand modal
	$('#addBrandInfoModal').on('click', function() {
		resetFormModal($brandInfoForm);
		showModalWithCustomizedTitle($brandInfoModal, "Add New Brand");
		$('#logoImg img').attr('src', '/images/image-demo.png');
		$('#brandId').closest(".form-group").addClass("d-none");
		$("#brandLogo .required-mask").removeClass("d-none");
	});

	// Show update brand modal
	$("#brandInfoTable").on('click', '.edit-btn', function() {

		$("#brandLogo .required-mask").addClass("d-none");

		// Get brand info by brand ID
		$.ajax({
			url : "http://localhost:8889/admin/api/find?id=" + $(this).data("id"),
			type : 'GET',
			dataType : 'json',
			contentType : 'application/json',
			success : function(responseData) {
				if (responseData.responseCode === 100) {
					const brandInfo = responseData.data;
					resetFormModal($brandInfoForm);
					showModalWithCustomizedTitle($brandInfoModal, "Edit Brand");

					$('#brandId').val(brandInfo.brandId);
					$('#brandName').val(brandInfo.brandName);
					$('#description').val(brandInfo.description);

					let brandLogo = brandInfo.logo;
					if (brandLogo === null || brandLogo === "") {
						brandLogo = "/images/image-demo.png";
					}
					$("#logoImg img").attr("src", brandLogo);
					$("#logo").val(brandLogo);
					$('#brandId').closest(".form-group").removeClass("d-none");
				}
			}
		});
	});

	// Show delete brand confirmation modal
	$("#brandInfoTable").on('click', '.delete-btn', function() {
		$("#deletedBrandName").text($(this).data("name"));
		$("#deleteSubmitBtn").attr("data-id", $(this).data("id"));
		$('#confirmDeleteModal').modal('show');
	});

	// Submit delete brand
	$("#deleteSubmitBtn").on('click' , function() {
		$.ajax({
			url : "http://localhost:8889/admin/api/delete/" + $(this).attr("data-id"),
			type : 'DELETE',
			dataType : 'json',
			contentType : 'application/json',
			success : function(responseData) {
				$('#confirmDeleteModal').modal('hide');
				showNotification(responseData.responseCode === 100, responseData.responseMsg);
				findAllBrands(1);
			}
		});
	});

	// Submit add and update brand
	$('#saveBrandBtn').on('click', function (event) {

		event.preventDefault();
		const formData = new FormData($brandInfoForm[0]);
		const brandId = formData.get("brandId");
		const isAddAction = brandId === undefined || brandId === "";

		$brandInfoForm.validate({
			ignore: [],
			rules: {
				brandName: {
					required: true,
					maxlength: 100
				},
				logoFiles: {
					required: isAddAction,
				}
			},
			messages: {
				brandName: {
					required: "Please input Brand Name",
					maxlength: "The Brand Name must be less than 100 characters",
				},
				logoFiles: {
					required: "Please upload Brand Logo",
				}
			},
			errorElement: "div",
			errorClass: "error-message-invalid"
		});

		if ($brandInfoForm.valid()) {

			// POST data to server-side by AJAX
			$.ajax({
				url: "http://localhost:8889/admin/api/" + (isAddAction ? "add" : "update"),
				type: 'POST',
				enctype: 'multipart/form-data',
				processData: false,
				contentType: false,
				cache: false,
				timeout: 10000,
				data: formData,
				success: function(responseData) {

					// Hide modal and show success message when save successfully
					// Else show error message in modal
					if (responseData.responseCode === 100) {
						$brandInfoModal.modal('hide');
						findAllBrands(1);
						showNotification(true, responseData.responseMsg);
					} else {
						showMsgOnField($brandInfoForm.find("#brandName"), responseData.responseMsg);
					}
				}
			});
		}
	});
});

/**
 * Find brands list with pager
 * 
 * @param pagerNumber
 */
function findAllBrands(pagerNumber) {
	$.ajax({
		url : "http://localhost:8889/admin/api/findAll/" + pagerNumber,
		type : 'GET',
		dataType : 'json',
		contentType : 'application/json',
		success : function(responseData) {
			if (responseData.responseCode === 100) {
				renderBrandsTable(responseData.data.brandsList);
				renderPagination(responseData.data.paginationInfo);
				if ($('.pagination').removeClass("d-none")) {
					$('#resultSearch p').empty();
				}
			}
		}
	});
}

/**
 * search Brand name
 *
 * @param pageNumber
 * @param isClickedSearchBtn
 */
function searchBrandName(pageNumber, isClickedSearchBtn) {
	var brandName = $('#keyword').val();
	$.ajax({
		url: "http://localhost:8889/admin/api/search/" + brandName + "/" + pageNumber,
		type: 'GET',
		dataType: 'json',
		contentType: 'application/json',
		success: function(responseData) {
			if (responseData.responseCode === 100) {
				renderBrandsTable(responseData.data.brandsList);
				renderPagination(responseData.data.paginationList);
				if (responseData.data.paginationList.pageNumberList.length < 2) {
					$('.pagination').addClass("d-none");
				} else {
					$('.pagination').removeClass("d-none");
				}
				renderMessageSearch(responseData.responseMsg);
			}
		}
	});
}

function renderMessageSearch(responseMsg) {
	$('#resultSearch p').empty();
	$('#resultSearch p').append(responseMsg);
}


/**
 * Render HTML for brand table
 * 
 * @param brandsList
 */
function renderBrandsTable(brandsList) {

	let rowHtml = "";
	$("#brandInfoTable tbody").empty();
	$.each(brandsList, function(key, value) {
		rowHtml = "<tr>"
				+		"<td>" + value.brandId + "</td>"
				+		"<td>" + value.brandName + "</td>"
				+		"<td class='text-center'><a href='" + value.logo + "' data-toggle='lightbox' data-max-width='1000'><img class='img-fluid' alt='Laptop' src='" + value.logo + "'></td>"
				+		"<td>" + value.description + "</td>"
				+		"<td class='action-btns'>"
				+			"<a class='edit-btn' data-id='" + value.brandId + "'><i class='fas fa-edit'></i></a> | <a class='delete-btn' data-name='" + value.brandName + "' data-id='" + value.brandId + "'><i class='fas fa-trash-alt'></i></a>"
				+		"</td>"
				+	"</tr>";
		$("#brandInfoTable tbody").append(rowHtml);
	});
}

/**
 * Render HTML for pagination bar
 * 
 * @param paginationInfo
 */
function renderPagination(paginationInfo) {

	let paginationInnerHtml = "";
	if (paginationInfo.pageNumberList.length > 0) {
		$("ul.pagination").empty();
		paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (paginationInfo.firstPage === 0 ? 'disabled' : '') + '" href="javascript:void(0)" data-index="'+ paginationInfo.firstPage + '">First</a></li>'
		paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (paginationInfo.previousPage === 0 ? 'disabled' : '') + '" href="javascript:void(0)" data-index="'+ paginationInfo.previousPage + '"> < </a></li>'
		$.each(paginationInfo.pageNumberList, function(key, value) {
			paginationInnerHtml += '<li class="page-item"><a class="page-link '+ (value === paginationInfo.currentPage ? 'active' : '') +'" href="javascript:void(0)" data-index="' + value +'">' + value + '</a></li>';
		});
		paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (paginationInfo.nextPage === 0 ? 'disabled' : '') + '" href="javascript:void(0)" data-index="'+ paginationInfo.nextPage + '"> > </a></li>'
		paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (paginationInfo.lastPage === 0 ? 'disabled' : '') + '" href="javascript:void(0)" data-index="'+ paginationInfo.lastPage + '">Last</a></li>'
		$("ul.pagination").append(paginationInnerHtml);
	}
}

