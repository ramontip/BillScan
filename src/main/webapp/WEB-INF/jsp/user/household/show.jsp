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
    <title>BillScan • <spring:message code="household.title"/></title>
</head>


<body class="d-flex h-100 w-100 overflow-hidden">
<body:sidebar/>

<div class="w-100">

    <body:navbar/>
    <body:header activePage="household"/>

    <form id="filterForm">
        <div class="overflow-auto bs-content w-100">
            <div class="container p-5">
                <div class="d-grid gap-4">
                    <div class="row">
                        <div class="col">
                            <div class="card bs-box-shadow w-100">
                                <div class="card-body">
                                    <div class="row align-items-center">
                                        <div class="col ps-3 h4">
                                            <c:out value="${purchasePeriodSelected.second}"/>
                                        </div>
                                        <div class="col-3">

                                            <div class="form-floating">
                                                <select id="selectPeriod" name="period"
                                                        class="form-select form-select-sm">
                                                    <option value=""><spring:message code="sort.time"/></option>
                                                    <option value=""><spring:message code="generic.thisMonth"/></option>
                                                    <c:forEach var="purchasePeriod" items="${purchasePeriods}">
                                                        <option value="${purchasePeriod.first}" ${purchasePeriod.first == purchasePeriodSelected.first ? 'selected' : ''}>
                                                                ${purchasePeriod.second}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">
                            <div class="card bs-box-shadow h-100 w-100">
                                <div class="card-title pt-3 px-3 mb-0">
                                    <div class="h5"><spring:message code="household.show.expenses"/></div>
                                </div>
                                <div class="card-body d-flex align-items-center justify-content-center">
                                    <div class="card-text h1 text-primary">
                                        € ${totalExpenses}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col">
                            <div class="card bs-box-shadow w-100">
                                <div class="card-title pt-3 px-3 mb-0">
                                    <div class="h5"><spring:message code="generic.visualisation"/></div>
                                </div>
                                <div class="card-body">
                                    <div class="chart-container">
                                        <canvas id="householdChart"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">
                            <div class="card bs-box-shadow w-100">
                                <div class="card-body p-0">
                                    <div class="row align-items-center p-3">
                                        <div class="col">
                                            <div class="h5">
                                                <spring:message code="generic.allBills"/>
                                            </div>
                                        </div>
                                        <div class="col-3">
                                            <div class="form-floating">
                                                <select id="selectSort" name="sort"
                                                        class="form-select form-select-sm">
                                                    <option value=""><spring:message code="sort.select"/></option>
                                                    <option value="0"><spring:message code="generic.date"/></option>
                                                    <option value="1" ${param.sort == 1 ? 'selected' : ''}>
                                                        <spring:message code="generic.price"/>
                                                    </option>
                                                    <option value="2" ${param.sort == 2 ? 'selected' : ''}>
                                                        <spring:message code="generic.shop"/>
                                                    </option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="table-responsive">
                                        <table class="table table-hover table-striped align-middle">
                                            <thead>
                                            <tr>
                                                <th scope="col"><spring:message code="generic.date"/></th>
                                                <th scope="col"><spring:message code="generic.shop"/></th>
                                                <th scope="col"><spring:message code="generic.price"/></th>
                                                <th scope="col"></th>
                                            </tr>
                                            </thead>
                                            <tbody class="align-content-center">

                                            <c:if test="${not empty bills}">

                                                <c:forEach var="bill" items="${bills}">

                                                    <tr>
                                                        <td><c:out value="${bill.purchaseDateFormatted}"/></td>
                                                        <td><c:out value="${bill.store.title}"/></td>
                                                        <td>€ <c:out value="${bill.purchasePrice}"/></td>
                                                        <td class="text-end">
                                                            <c:if test="${not empty bill.files}">
                                                                <a href="/household/${household.id}/bill/${bill.id}/show/file">
                                                                    <i class="bi bi-arrow-down-square-fill pe-2"></i>
                                                                </a>
                                                            </c:if>
                                                            <a href="/household/${bill.household.id}/bill/${bill.id}/edit">
                                                                <i class="bi bi-gear-fill pe-2"></i>
                                                            </a>
                                                            <a href="/household/${bill.household.id}/bill/${bill.id}/show">
                                                                <i class="bi bi-eye-fill pe-2"></i>
                                                            </a>
                                                        </td>
                                                    </tr>

                                                </c:forEach>

                                            </c:if>

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
</div>

<body:toast-messages/>
<script type="text/javascript" src="/js/custom.js"></script>
<scripts:household-chart/>

</body>