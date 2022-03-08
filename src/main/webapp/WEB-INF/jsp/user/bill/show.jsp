<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="head" tagdir="/WEB-INF/tags/common/head" %>
<%@taglib prefix="body" tagdir="/WEB-INF/tags/common/body" %>
<%@taglib prefix="scripts" tagdir="/WEB-INF/tags/common/scripts" %>


<head>
    <head:meta-main/>
    <title>BillScan • <spring:message code="bill.header"/></title>
</head>


<body class="d-flex h-100 w-100 overflow-hidden">
<body:sidebar/>

<div class="w-100">

    <body:navbar/>
    <body:header activePage="household"/>

    <form id="filterForm">
        <div class="overflow-auto bs-content w-100">

            <div class="d-grid justify-content-center">
                <div class="col py-4">
                    <a href="/household/${household.id}/show" class="btn btn-link text-light text-decoration-none">
                        <i class="bi bi-arrow-left me-1"></i><spring:message code="generic.goBack"/>
                    </a>
                </div>
            </div>

            <div class="container px-5">

                <div class="d-grid gap-4">

                    <div class="row">
                        <div class="col">
                            <div class="card bs-box-shadow w-100 mb-4">
                                <div class="card-body">
                                    <div class="row align-items-center">
                                        <div class="col ps-3">
                                            <div class="h4">
                                                <c:out value="${bill.store.title}"/> -
                                                <c:out value="${bill.purchaseDateFormatted}"/>
                                            </div>
                                            <p class="text-light p-0 m-0">
                                                <spring:message code="generic.addedBy"/> <c:out
                                                    value="${bill.user.fullName}"/>
                                            </p>
                                        </div>
                                        <div class="col-2 text-end">
                                            <a href="/household/${bill.household.id}/bill/${bill.id}/edit">
                                                <i class="bi bi-gear-fill pe-2"></i>
                                            </a>
                                            <a href="/household/${bill.household.id}/bill/${bill.id}/delete">
                                                <i class="bi bi-trash-fill"></i>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="card bs-box-shadow w-100 mb-3 bs-price-container">
                                <div class="card-title pt-3 px-3 mb-0">
                                    <span class="h5"><spring:message code="generic.totalPrice"/></span>
                                </div>
                                <div class="card-body d-flex align-items-center justify-content-center">
                                    <div class="card-text h1 text-primary">
                                        € <c:out value="${bill.purchasePrice}"/>
                                    </div>
                                </div>

                            </div>
                        </div>

                        <c:if test="${not empty bill.files}">
                            <div class="col">
                                <div class="card bs-box-shadow w-100">
                                    <div class="card-title pt-3 px-3 mb-0">
                                        <div class="row align-items-center">
                                            <div class="col">
                                                <div class="h5"><spring:message code="bill.title"/></div>
                                            </div>
                                            <div class="col-2 text-end">
                                                <a href="/household/${household.id}/bill/${bill.id}/show/file">
                                                    <i class="bi bi-arrow-down-square-fill"></i>
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="card-body text-center bs-image-scroll-container">
                                        <img src="/household/${household.id}/bill/${bill.id}/show/file"
                                             class="img-fluid" alt=""/>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </div>

                    <div class="row">
                        <div class="col">
                            <div class="card bs-box-shadow w-100">
                                <div class="card-body p-0">
                                    <div class="row align-items-center p-3">
                                        <div class="col">
                                            <div class="h5"><spring:message
                                                    code="generic.productList"/></div>
                                        </div>
                                        <div class="col-3">
                                            <div class="form-floating">
                                                <select id="selectSort" name="sort"
                                                        class="form-select form-select-sm">
                                                    <option value=""><spring:message
                                                            code="sort.select"/></option>
                                                    <option value="0"><spring:message
                                                            code="generic.price"/></option>
                                                    <option value="1" ${param.sort == 1 ? 'selected' : ''}>
                                                        <spring:message code="household.create.title"/>
                                                    </option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="table">
                                        <table class="table table-hover table-striped align-middle">
                                            <thead>
                                            <tr>
                                                <th scope="col"><spring:message
                                                        code="generic.product.description"/></th>
                                                <th class="text-end" scope="col"><spring:message
                                                        code="generic.price"/></th>
                                            </tr>
                                            </thead>
                                            <tbody class="align-content-center">
                                            <c:forEach var="product" items="${bill.products}">
                                                <tr>
                                                    <th scope="row"><c:out value="${product.title}"/></th>
                                                    <td class="text-end">€ <c:out value="${product.price}"/></td>
                                                </tr>
                                            </c:forEach>

                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </form>

    <body:toast-messages/>
    <script type="text/javascript" src="/js/custom.js"></script>

    <script>
        $(document).on("click", '[data-toggle="lightbox"]', function (event) {
            event.preventDefault();
            $(this).ekkoLightbox();
        });
    </script>

</body>