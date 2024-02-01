$(document).ready(function() {
	
	// Show products list when opening page
	findAllProducts(1);


		// Jquery Dependency		
	$("input[data-type='currency']").on({
	    keyup: function() {
	      formatCurrency($(this));
	    }
	    
	});
	
	
	function formatCurrency(input) {	  
	  // get input value
	  var input_val = input.val();
	    
		input_val=input_val.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	     
	  // send updated string to input
	  	input.val(input_val);
	}
	
	
		
	

	
	// Show products list when clicking pagination button
	$('.pagination').on('click', '.page-link', function() {
		var pageNumber = $(this).attr("data-index");
		var keyword = $('#keyword').val();
		var priceFrom = $('#priceFrom').val();
		var priceTo = $('#priceTo').val();
		if (keyword != "" || priceFrom != "" || priceTo != "") {
			searchProduct(pageNumber, true);
		} else {
			findAllProducts(pageNumber);
		}
	});

	// Search when press Enter
	$('.search-product').keypress((e) => {
        if (e.which === 13) {
            $('.info').submit();
            searchProduct(1, true);        
        }
    })
	// Search product when clicking Search button 
	$('#searchByPrice').on('click', function() {
		searchProduct(1, true);
	});


	// Reset page 
	$('#restPage').on('click', function() {
		$('#priceFrom').val("");
		$('#priceTo').val("");
		$('#keyword').val("");	
		$('#resultSearch p').empty();
		findAllProducts(1);
	})

	
	var $productInfoForm = $('#productInfoForm');
	var $productInfoModal = $('#productInfoModal');


	// Set current day default for sale date
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth() + 1; 
	var yyyy = today.getFullYear();
	if (dd < 10) { dd = '0' + dd }
	if (mm < 10) { mm = '0' + mm }
	today = yyyy + '-' + mm + '-' + dd;
	
	
	// Show add product modal
	$("#addProductInfoModal").on('click', function() {
		resetFormModal($productInfoForm);
		$('input[name=saleDate]').val(today);
		showModalWithCustomizedTitle($productInfoModal, "Add New Product");
		$('#logoImg img').attr('src', '/images/image-demo.png');
		$('#productId').closest(".form-group").addClass("d-none");
		$("#productImage .required-mask").removeClass("d-none");
	});

	// Show update product modal
	$("#productInfoTable").on('click', '.edit-btn', function() {
		$("#productImage .required-mask").addClass("d-none");
		
		// Get project info by brand ID
		$.ajax({
			url: "/product/api/find?id=" + $(this).data("id"),
			type: 'GET',
			dateType: 'json',
			contentType: 'application/json',
			success: function(responseData) {
				if (responseData.responseCode == 100) {
					var productInfo = responseData.data;
					resetFormModal($productInfoForm);
					showModalWithCustomizedTitle($productInfoModal, "Edit Product");

					$('#productId').val(productInfo.productId);
					$('#productName').val(productInfo.productName);
					$('#quantity').val(productInfo.quantity);
					$('#price').val(productInfo.price);
					$('#brandId').val(productInfo.brandEntity.brandId);
					$('#saleDate').val(productInfo.saleDate);
					$('#description').val(productInfo.description);

					var productImage = productInfo.image;
					if (productImage == null || productImage == "") {
						productImage = "/images/image-demo.png";
					}
					$("#logoImg img").attr("src", productImage);
					$("#image").val(productImage);
					$('#productId').closest(".form-group").removeClass("d-none");
				}
			}
		})
	});

	//Show delete product confirmation modal
	$("#productInfoTable").on('click', '.delete-btn', function() {
		$("#deletedProductName").text($(this).data("name"));
		$("#deleteSubmitBtn").attr("data-id", $(this).data("id"));
		$('#confirmDeleteModal').modal('show');
	});

	//Submit delete product
	$("#deleteSubmitBtn").on('click', function() {
		$.ajax({
			url: "/product/api/delete/" + $(this).attr("data-id"),
			type: 'DELETE',
			dataType: 'json',
			contentType: 'application/json',
			success: function(responseData) {
				$('#confirmDeleteModal').modal('hide');
				showNotification(responseData.responseCode == 100, responseData.responseMsg);
				findAllProducts(1);
			}
		});
	});
	
	//Submit add new product or update product
	$("#saveProductBtn").on('click', function(event) {
		event.preventDefault();
		var formData = new FormData($productInfoForm[0]);
		var productId = formData.get("productId");
		var isAddAction = productId == undefined || productId == "";
		$productInfoForm.validate({
			ignore: [],
			rules: {
				productName: {
					required: true,
					minlength: 2,
					maxlength: 100
				},
				quantity: {
					required: true,
					number: true,
				},
				price: {
					required: true,
					number: true,
				},
				brandName: {
					required: true,
				},
				saleDate: {
					required: true,
				},
				imageFiles: {
					required: isAddAction,
				}
			},
			messages: {
				productName: {
					required: "Please input Product Name",
					minlength: "Your Product name must consist of at least 2 characters",
					maxlength: "The Product Name must be less than 100 characters"
				},
				quantity: {
					required: "Please input Quantity",
					number: "Quantity is number"
				},
				price: {
					required: "Please input Price",
					number: "Price is number"
				},
				brandName: {
					required: "Please input Brand Name",
				},
				saleDate: {
					required: "Please input Sale date"
				},
				imageFiles: {
					required: "Please upload Product Image"
				}
			},
			errorElement: "div",
			errorClass: "error-message-invalid"
		});
		if ($productInfoForm.valid()) {

			$.ajax({
				url: "/product/api/" + (isAddAction ? "add" : "update"),
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
					if (responseData.responseCode == 100) {
						$productInfoModal.modal('hide');
						findAllProducts(1);
						showNotification(true, responseData.responseMsg);
					} else {
						showMsgOnField($productInfoForm.find("#productName"), responseData.responseMsg);
					}
				}
			});
		}
	})
});




/**
* Find products list with pager
* @param pageNumber
*/
function findAllProducts(pageNumber) {
	$.ajax({
		url: "/product/api/findAll/" + pageNumber,
		type: 'GET',
		dataType: 'json',
		contentType: 'application/json',
		success: function(responseData) {
			if (responseData.responseCode == 100) {
				renderProductsTable(responseData.data.productsList);
				renderPagination(responseData.data.paginationList);
				if ($('.pagination').removeClass("d-none")) {
					$('#resultSearch p').empty();
				}
			}
		}
	});
}

/**
 * Search product by conditions 
 * @param pagerNumber
 */
function searchProduct(pageNumber, isClickedSearchBtn) {
	
	 var priceFrom =$("#priceFrom").val();
	 var priceTo = $("#priceTo").val();
	if ( priceFrom != "") {
		 $("#priceFrom").val(function(i, oldVal){
            priceFrom= oldVal.replace(/,/g, "");
			return priceFrom;
       });
	}
	if ( priceTo != "") {
		 var priceTo =$("#priceTo").val(function(i, oldVal){
            priceTo = oldVal.replace(/,/g, "");
			return priceTo;
       });
	}
	
	var searchConditions = {
		keyword: $("#keyword").val(),
		priceFrom: $('#priceFrom').val(),
		priceTo: $("#priceTo").val()
	}
	$.ajax({
		url: '/product/api/searchProduct/' + pageNumber,
		type: 'POST',
		dataType: 'json',
		contentType: 'application/json',
		success: function(responseData) {
			if (responseData.responseCode == 100) {
				renderProductsTable(responseData.data.productsList);
				renderPagination(responseData.data.paginationList);
				if (responseData.data.paginationList.pageNumberList.length < 2) {
					$('.pagination').addClass("d-none");
				} else {
					$('.pagination').removeClass("d-none");
				}
				renderMessageSearch(responseData.responseMsg);
			}
		},
		data: JSON.stringify(searchConditions)
	});
}



/** 
* Currency format 
* @param price
* @return price format
*/
function currencyFormat(price) {
	return price.toLocaleString('vi', {style : 'currency', currency : 'VND'});
}


/**
 * Render HTML for product table
 * 
 * @param productsList
 */
function renderProductsTable(productsList) {

	var rowHtml = "";
	$("#productInfoTable tbody").empty();
	$.each(productsList, function(key, value) {
		rowHtml = "<tr>"
			+ "<td class='no'>" + value.productId + "</td>"
			+ "<td>" + value.productName + "</td>"
			+ "<td class='quantity'>" + value.quantity + "</td>"
			+ "<td class='price-td'>" + currencyFormat(value.price) + "</td>"
			+ "<td class='brand-logo-td'>" + value.brandEntity.brandName + "</td>"
			+ "<td class='sale-date'>" + getFormattedDate(value.saleDate) + "</td>"
			+ "<td class='text-center'><a href='" + value.image + "' data-toggle='lightbox' data-max-width='1000'><img class='img-fluid' src='" + value.image + "'></td>"
			+ "<td>" + value.description + "</td>"
			+ "<td class='action-btns'>"
			+ "<a class='edit-btn' data-id='" + value.productId + "'><i class='fas fa-edit'></i></a> | <a class='delete-btn' data-name='" + value.productName + "' data-id='" + value.productId + "'><i class='fas fa-trash-alt'></i></a>"
			+ "</td>"
		"</tr>";
		$("#productInfoTable tbody").append(rowHtml);
	})
}

// Render result message search 
function renderMessageSearch(responseMsg) {
	$('#resultSearch p').empty();
	$('#resultSearch p').append(responseMsg);
}


/**
 * Format Date
 * 
 * @param saleDate
 */
function getFormattedDate(saleDate) {
	var date = new Date(saleDate);
	var day = date.getDate();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();
	if (date < 10) { date = '0' + date }
	if (month < 10) { month = '0' + month }
	return day + '/' + month + '/' + year;
}


/**
 * Render HTML for pagination bar
 * 
 * @param paginationInfo
 */
function renderPagination(paginationList) {

	var paginationInnerHtml = "";
	if (paginationList.pageNumberList.length > 0) {
		$("ul.pagination").empty();
		paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (paginationList.firstPage == 0 ? 'disabled' : '') + '" href="javascript:void(0)" data-index="' + paginationList.firstPage + '" > Fisrt  </a></li>'
		paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (paginationList.prevPage == 0 ? 'disabled' : '') + '" href="javascript:void(0)" data-index="' + paginationList.prevPage + '" > < </a></li>'
		$.each(paginationList.pageNumberList, function(key, value) {
			paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (value == paginationList.currentPage ? 'active' : '') + '" href="javascript:void(0)" data-index="' + value + '">' + value + '</a></li>';
		});
		paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (paginationList.nextPage == 0 ? 'disabled' : '') + '" href="javascript:void(0)" data-index="' + paginationList.nextPage + '" > > </a></li>'
		paginationInnerHtml += '<li class="page-item"><a class="page-link ' + (paginationList.lastPage == 0 ? 'disabled' : '') + '" href="javascript:void(0)" data-index="' + paginationList.lastPage + '" > Last </a></li>'
		$("ul.pagination").append(paginationInnerHtml);

	}
}

