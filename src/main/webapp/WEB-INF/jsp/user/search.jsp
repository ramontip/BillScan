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
    <title>BillScan • <spring:message code="search.title"/></title>
</head>

<body class="d-flex h-100 w-100">

<body:sidebar/>

<div class="w-100">

    <body:navbar/>
    <body:header activePage="search"/>
    <div class="overflow-auto bs-content w-100">
        <div class="container p-5">
            <div class="d-grid gap-4">
                <div class="row-cols-1">

                    <c:if test="${not empty products}">

                        <div class="col mb-5">
                            <div class="card">
                                <div class="card-title p-3">
                                    <span class="h5"><spring:message code="generic.products"/></span>
                                </div>
                                <div class="card-body p-0">
                                    <div class="table-responsive">
                                        <table class="table table-hover table-striped align-middle">
                                            <thead>
                                            <tr>
                                                <th scope="col"><spring:message code="generic.date"/></th>
                                                <th scope="col"><spring:message code="generic.productShop"/></th>
                                                <th scope="col"><spring:message code="generic.price"/></th>
                                                <th scope="col"></th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="product" items="${products}">
                                                <tr>
                                                    <td><c:out value="${product.createdAtFormatted}"/></td>
                                                    <td><c:out value="${product.title}"/> (<c:out
                                                            value="${product.bill.store.title}"/>)
                                                    </td>
                                                    <td>€ <c:out value="${product.price}"/></td>
                                                    <td class="text-end">
                                                        <a href="/household/${product.bill.household.id}/bill/${product.bill.id}/show">
                                                            <i class="bi bi-eye-fill bs-svg-color"></i>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </c:if>

                    <c:if test="${not empty bills}">

                        <div class="col">
                            <div class="card">
                                <div class="card-title p-3">
                                    <span class="h5"><spring:message code="generic.stores"/></span>
                                </div>
                                <div class="card-body p-0">
                                    <div class="table-responsive">
                                        <table class="table table-hover table-striped align-middle">
                                            <thead>
                                            <tr>
                                                <th scope="col"><spring:message code="generic.date"/></th>
                                                <th scope="col"><spring:message code="generic.shop"/></th>
                                                <th scope="col"><spring:message code="bill.cost"/></th>
                                                <th scope="col"></th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="bill" items="${bills}">
                                                <tr>
                                                    <td><c:out value="${bill.purchaseDateFormatted}"/></td>
                                                    <td><c:out value="${bill.store.title}"/></td>
                                                    <td>€ <c:out value="${bill.purchasePrice}"/></td>
                                                    <td class="text-end">
                                                        <a href="/household/${bill.household.id}/bill/${bill.id}/show">
                                                            <i class="bi bi-eye-fill bs-svg-color"></i>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </c:if>

                </div>
            </div>
        </div>
    </div>
</div>

<body:toast-messages/>

</body>