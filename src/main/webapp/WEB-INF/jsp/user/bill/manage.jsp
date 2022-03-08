<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="head" tagdir="/WEB-INF/tags/common/head" %>
<%@taglib prefix="body" tagdir="/WEB-INF/tags/common/body" %>
<%@taglib prefix="scripts" tagdir="/WEB-INF/tags/common/scripts" %>


<head>
    <head:meta-main/>
    <head:jquery/>
    <title>BillScan • <spring:message code="bill.create.header"/></title>
</head>


<body class="d-flex h-100 w-100">
<body:sidebar/>

<div class="w-100">

    <body:navbar/>
    <body:header activePage="household"/>
    <div class="overflow-auto bs-content w-100">
        <div class="container">

            <div class="d-grid justify-content-center">
                <div class="col py-4">
                    <a href="/household/${household.id}/show" class="btn btn-link text-light text-decoration-none">
                        <i class="bi bi-arrow-left me-1"></i><spring:message code="generic.goBack"/>
                    </a>
                </div>
            </div>

            <%----------------- Step 1 Upload Bill --------------------%>
            <c:if test="${type eq 1}">

            <p class="col">
                <form:form class="bs-form bg-white rounded-3 text-start" method="post"
                           action="/household/${household.id}/bill/upload"
                           enctype="multipart/form-data" onsubmit="formUploadBill()">

            <div id="formUploadInner">
                <div class="text-light h6"><spring:message code="bill.generic.step"/> 1</div>
                <div class="h5 bs-form-heading py-3"><spring:message code="bill.create.header"/></div>

                <c:if test="${not empty messageError}">
                    <div class="alert alert-danger" role="alert">
                        <c:out value="${messageError}"/>
                    </div>
                </c:if>

                <!---------------- bill ------------------>
                <div class="form-floating">
                    <input id="file" name="file" type="file"
                           class="form-control bs-form-control form-control-sm lpt-4-5"
                           data-show-upload="true"
                           data-show-caption="true" accept="image/png, image/jpeg" required="required"/>
                    <label for="file" class="form-label"><spring:message code="generic.addFile"/></label>
                </div>

                <!---------------- button ------------------>
                <button id="upload-form-submit" class="btn btn-primary bs-btn w-100 mt-2" type="submit">
                    <spring:message code="generic.continue"/>
                </button>

            </div>

            <div id="formUploadSpinner" class="visually-hidden">
                <div class="d-flex justify-content-center m-5">
                    <div class="spinner-border" role="status">
                        <span class="sr-only"></span>
                    </div>
                </div>
                <p class="text-center fw-bold m-2">
                    <spring:message code="generic.billIsProcessed"/>
                </p>
            </div>
            </form:form>
        </div>
        <div class="d-grid justify-content-center">
            <div class="col py-4">
                <a href="/household/${household.id}/bill/manage"
                   class="btn btn-link text-primary text-decoration-none fw-bold">
                    <spring:message code="generic.manualInput"/></a>
            </div>
        </div>

        </c:if>

        <%----------------- Type 2 Verify or Insert Manually --------------------%>
        <c:if test="${type eq 2}">

            <div class="col">

                <c:if test="${variant lt 3}">
                    <c:set var="action" value="/household/${household.id}/bill/manage"/>
                </c:if>

                <c:if test="${variant eq 3}">
                    <c:set var="action" value="/household/${householdId}/bill/${bill.id}/edit"/>
                </c:if>

                    <%--@elvariable id="billDto" type="net.billscan.app.billscan.dto.BillDto"--%>
                <form:form modelAttribute="billDto" class="bs-form bg-white bs-box-shadow rounded-3 text-start"
                           method="post" action="${action}">

                    <c:choose>

                        <c:when test="${variant eq 1}">
                            <%---------------- Variant 1 - Manually Insert Data ------------------%>
                            <div class="text-light h6"><spring:message code="bill.generic.step"/> 1</div>
                            <div class="h5 bs-form-heading py-3"><spring:message code="bill.create.insert"/></div>
                        </c:when>

                        <c:when test="${variant eq 2}">
                            <%---------------- Variant 2 - API Call, Verify Data ------------------%>
                            <div class="text-light h6"><spring:message code="bill.generic.step"/> 2</div>
                            <div class="h5 bs-form-heading py-3"><spring:message code="bill.create.verify"/></div>
                        </c:when>

                        <c:when test="${variant eq 3}">
                            <%---------------- Variant 3 - Edit Data ------------------%>
                            <div class="h5 bs-form-heading py-3"><spring:message code="bill.edit"/></div>
                        </c:when>

                    </c:choose>

                    <c:if test="${not empty messageError}">
                        <div class="alert alert-danger" role="alert">
                            <c:out value="${messageError}"/>
                        </div>
                    </c:if>

                    <c:if test="${not empty messageWarning}">
                        <div class="alert alert-warning" role="alert">
                            <c:out value="${messageWarning}"/>
                        </div>
                    </c:if>

                    <!---------------- hidden file id ------------------>
                    <c:if test="${not empty fileId}">
                        <input type="hidden" id="fileId" name="fileId" value="${fileId}"/>
                    </c:if>

                    <!---------------- store ------------------>
                    <div class="form-floating">
                        <c:set var="storeInvalid"><form:errors var="storeInvalid" path="store"
                                                               cssClass="invalid-feedback text-center mb-2"/></c:set>
                        <form:input path="store"
                                    class="form-control bs-form-control ${not empty storeInvalid ? 'is-invalid' : ''} ${storeConfidence eq false ? 'bs-form-warning' : ''}"
                                    id="inputStore" type="text" placeholder="_" required="required"/>
                            ${storeInvalid}
                        <label for="inputStore" class="form-label"><spring:message
                                code="bill.generic.store"/></label>
                    </div>

                    <div class="row row-cols-2 py-2">
                        <!---------------- date ------------------>
                        <div class="col-6 pb-2 pe-1">
                            <div class="form-floating">
                                <c:set var="dateInvalid"><form:errors var="dateInvalid" path="date"
                                                                      cssClass="invalid-feedback text-center mb-2"/></c:set>
                                <form:input path="date"
                                            class="form-control bs-form-control ${not empty dateInvalid ? 'is-invalid' : ''} ${dateConfidence eq false ? 'bs-form-warning' : ''}"
                                            id="inputDate" type="date" placeholder="_" required="required"/>
                                    ${dateInvalid}
                                <label for="inputDate" class="form-label"><spring:message
                                        code="bill.generic.BillDate"/></label>
                            </div>
                        </div>
                        <!---------------- purchase price ------------------>
                        <div class="col-6 pb-2">
                            <div class="form-floating">
                                <c:set var="purchasePriceInvalid"><form:errors var="purchasePriceInvalid"
                                                                               path="purchasePrice"
                                                                               cssClass="invalid-feedback text-center mb-2"/></c:set>
                                <form:input path="purchasePrice"
                                            class="form-control bs-form-control ${not empty purchasePriceInvalid ? 'is-invalid' : ''} ${purchasePriceConfidence eq false ? 'bs-form-warning' : ''}"
                                            id="inputPurchasePrice" type="number" placeholder="_" min="0"
                                            step="0.01"
                                            required="required"/>
                                    ${purchasePriceInvalid}
                                <label for="inputPurchasePrice" class="form-label"><spring:message
                                        code="generic.totalPrice"/></label>
                            </div>
                        </div>
                    </div>

                    <div id="productsContainer" class="row row-cols-2 py-2">

                        <div class="w-100 m-0 p-0">

                            <c:forEach begin="0" end="${productsLimit - 1}" var="idx">

                                <div id="productsLine[${idx}]" class="row w-100 m-0 p-0">

                                    <!---------------- product title ------------------>
                                    <div class="col-6 pb-2 pe-1">
                                        <div class="form-floating">
                                            <c:set var="productTitlesInvalid"><form:errors
                                                    var="productTitlesInvalid"
                                                    path="productTitles[${idx}]"
                                                    cssClass="invalid-feedback text-center mb-2"/></c:set>
                                            <form:input path="productTitles[${idx}]"
                                                        class="form-control bs-form-control ${not empty productTitlesInvalid ? 'is-invalid' : ''} ${productConfidences[idx] eq false ? 'bs-form-warning' : ''}"
                                                        id="inputProductTitles[${idx}]" type="text"
                                                        placeholder="_"/>
                                                ${productTitlesInvalid}
                                            <label for="inputProductTitles[${idx}]"
                                                   class="form-label"><spring:message
                                                    code="generic.product"/></label>
                                        </div>
                                    </div>

                                    <!---------------- product price ------------------>
                                    <div class="col-4 pb-2">
                                        <div class="form-floating">
                                            <c:set var="productPricesInvalid"><form:errors
                                                    var="productPricesInvalid"
                                                    path="productPrices[${idx}]"
                                                    cssClass="invalid-feedback text-center mb-2"/></c:set>
                                            <form:input path="productPrices[${idx}]"
                                                        class="form-control bs-form-control ${not empty productPricesInvalid ? 'is-invalid' : ''} ${productConfidences[idx] eq false ? 'bs-form-warning' : ''}"
                                                        id="inputProductPrices[${idx}]" type="number"
                                                        placeholder="_"
                                                        min="0" step="0.01"/>
                                                ${productPricesInvalid}
                                            <label for="inputProductPrices[${idx}]"
                                                   class="form-label"><spring:message
                                                    code="generic.price"/></label>
                                        </div>
                                    </div>

                                    <!---------------- delete ------------------>
                                    <div class="col-2 pb-2 ps-1">
                                        <div class="form-floating">
                                            <input class="bs-button-delete form-control bs-form-control text-primary"
                                                   value="&#10005;"
                                                   type="button"
                                                   onclick="removeElement('productsLine[${idx}]')"/>
                                        </div>
                                    </div>

                                </div>

                            </c:forEach>

                        </div>

                        <div id="additionalProducts" class="visually-hidden row w-100 m-0 p-0">

                            <c:forEach begin="${productsLimit}" end="${productsLimit + 10}" var="idx">

                                <div id="productsLine[${idx}]" class="row w-100 m-0 p-0">

                                    <!---------------- product title ------------------>
                                    <div class="col-6 pb-2 pe-1">
                                        <div class="form-floating">
                                            <c:set var="productTitlesInvalid"><form:errors
                                                    var="productTitlesInvalid"
                                                    path="productTitles[${idx}]"
                                                    cssClass="invalid-feedback text-center mb-2"/></c:set>
                                            <form:input path="productTitles[${idx}]"
                                                        class="form-control bs-form-control ${not empty productTitlesInvalid ? 'is-invalid' : ''} ${productConfidences[idx] eq false ? 'bs-form-warning' : ''}"
                                                        id="inputProductTitles[${idx}]" type="text"
                                                        placeholder="_"/>
                                                ${productTitlesInvalid}
                                            <label for="inputProductTitles[${idx}]"
                                                   class="form-label"><spring:message
                                                    code="generic.product"/></label>
                                        </div>
                                    </div>

                                    <!---------------- product price ------------------>
                                    <div class="col-4 pb-2">
                                        <div class="form-floating">
                                            <c:set var="productPricesInvalid"><form:errors
                                                    var="productPricesInvalid"
                                                    path="productPrices[${idx}]"
                                                    cssClass="invalid-feedback text-center mb-2"/></c:set>
                                            <form:input path="productPrices[${idx}]"
                                                        class="form-control bs-form-control ${not empty productPricesInvalid ? 'is-invalid' : ''} ${productConfidences[idx] eq false ? 'bs-form-warning' : ''}"
                                                        id="inputProductPrices[${idx}]" type="number"
                                                        placeholder="_"
                                                        min="0" step="0.01"/>
                                                ${productPricesInvalid}
                                            <label for="inputProductPrices[${idx}]"
                                                   class="form-label"><spring:message
                                                    code="generic.price"/></label>
                                        </div>
                                    </div>

                                    <!---------------- delete ------------------>
                                    <div class="col-2 pb-2 ps-1">
                                        <div class="form-floating">
                                            <input class="bs-button-delete form-control bs-form-control text-primary"
                                                   value="&#10005;"
                                                   type="button"
                                                   onclick="removeElement('productsLine[${idx}]');"/>
                                        </div>
                                    </div>

                                </div>

                            </c:forEach>

                        </div>

                        <div class="w-100 justify-content-center">
                            <div class="col py-3 w-100">

                                <!---------------- moveComma ------------------>
                                <div class="row mb-3">
                                    <div class="col-6 pe-1">
                                        <button type="button"
                                                class="btn btn-sm bg-body text-primary w-100"
                                                onclick="moveComma(-1)">
                                            <i class="bi bi-arrow-left me-2"></i><spring:message
                                                code="generic.moveComma"/>
                                        </button>
                                    </div>
                                    <div class="col-6">
                                        <button type="button"
                                                class="btn btn-sm bg-body text-primary w-100"
                                                onclick="moveComma(1)">
                                            <spring:message
                                                    code="generic.moveComma"/><i
                                                class="bi bi-arrow-right ms-2"></i>
                                        </button>
                                    </div>
                                </div>

                                <!---------------- add product ------------------>
                                <button id="addProducts" type="button"
                                        class="btn btn-sm bg-body text-primary w-100 mb-3">
                                    <i class="bi bi-plus-square me-2"></i><spring:message
                                        code="generic.moreProducts"/>
                                </button>

                                <!---------------- remove products ------------------>
                                <button id="removeAllProducts"
                                        class="btn btn-sm bg-body text-primary w-100 mb-3"
                                        type="button">
                                    <i class="bi bi-x-square me-2"></i><spring:message
                                        code="generic.button.removeAllProducts"/>
                                </button>

                                <!---------------- update purchase price ------------------>
                                <button class="btn btn-sm bg-body text-primary w-100"
                                        type="button" onclick="updateTotalPrice()">
                                    <i class="bi bi-arrow-clockwise me-2"></i><spring:message
                                        code="generic.button.updateTotalPrice"/>
                                </button>
                            </div>
                        </div>

                    </div>

                    <!---------------- button ------------------>
                    <button class="btn btn-primary bs-btn w-100 mt-2" type="submit">
                        <spring:message code="generic.save"/>
                    </button>

                </form:form>
            </div>
        </c:if>
    </div>

</div>

</div>

<body:toast-messages/>

<!-- Javascript -->
<script type="text/javascript">

    if (document.getElementById("addProducts")) {
        document.getElementById("addProducts").onclick = function () {
            document.getElementById("additionalProducts").classList.remove("visually-hidden");
            document.getElementById("addProducts").classList.add("visually-hidden");
        };

        document.getElementById("removeAllProducts").onclick = function () {
            document.getElementById("productsContainer").remove();
        };
    }

    function updateTotalPrice() {
        var totalPrice = 0;
        for (let i = 0; i < 100; i++) {
            let productPrice = document.getElementById('inputProductPrices[' + i + ']');
            if (productPrice) {
                if (productPrice.value !== '') {
                    totalPrice += parseFloat(productPrice.value);
                }
            }
        }
        document.getElementById("inputPurchasePrice").value = Math.round(parseFloat(totalPrice) * 100) / 100;
    }

    function removeElement(element) {
        let e = document.getElementById(element);
        if (e) {
            e.remove();
            updateTotalPrice();
        }
    }

    function formUploadBill() {
        document.getElementById("formUploadInner").classList.add("visually-hidden");
        document.getElementById("formUploadSpinner").classList.remove("visually-hidden");
    }

    function moveComma(direction) {
        let factor;
        if (direction === -1) {
            factor = 10;
        } else {
            factor = 0.1;
        }
        let totalPrice = document.getElementById('inputPurchasePrice');
        if (totalPrice.value !== '') {
            totalPrice.value = Math.round((totalPrice.value / factor) * 100) / 100;
        }
        for (let i = 0; i < 100; i++) {
            let purchasePrice = document.getElementById('inputProductPrices[' + i + ']');
            if (purchasePrice) {
                if (purchasePrice.value !== '') {
                    purchasePrice.value = Math.round((purchasePrice.value / factor) * 100) / 100;
                }
            }
        }
    }

</script>
<script>
    $("#upload-form-submit").click(function (e) {
        let file = document.getElementById("file");

        if (file) {
            let file_size = 0;
            if (file && file.value !== "") {
                file_size = file.files[0].size;
            }
            
            if (file_size > 10000000) {
                alert("<spring:message code="message.alert.maxFileSize10MB"/>");
                e.preventDefault();
            }
        }
    });
</script>

</body>