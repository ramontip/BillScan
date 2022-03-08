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
    <title>BillScan • <spring:message code="files.header"/></title>
</head>


<body class="d-flex h-100 w-100 overflow-hidden">
<body:sidebar/>

<div class="w-100">

    <body:navbar/>
    <body:header activePage="files"/>

    <div class="overflow-auto bs-content w-100">
        <div class="container p-5">
            <div class="d-grid gap-4">
                <div class="row">
                    <div class="col">
                        <div class="card bs-box-shadow w-100">
                            <div class="card-body">
                                <div class="row align-items-center">
                                    <div class="col ps-3 h4">
                                        <spring:message code="files.header"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <div class="card bs-box-shadow w-100">
                            <div class="card-body p-0">
                                <div class="table-responsive">
                                    <table class="table table-hover table-striped align-middle">
                                        <thead>
                                        <tr>
                                            <th scope="col"><spring:message code="generic.file"/></th>
                                            <th scope="col"><spring:message code="generic.date"/></th>
                                            <th scope="col"><spring:message code="generic.connectedBill"/></th>
                                            <th scope="col"></th>
                                        </tr>
                                        </thead>
                                        <tbody class="align-content-center">
                                        <c:if test="${not empty files}">

                                            <c:forEach var="file" items="${files}">

                                                <tr>
                                                    <td><c:out value="${file.name}"/></td>
                                                    <td><c:out value="${file.createdAtFormatted}"/></td>
                                                    <td>
                                                        <c:if test="${not empty file.bill}">
                                                            <a href="/household/${file.bill.household.id}/bill/${file.bill.id}/show">${file.bill.store.title}
                                                                (${file.bill.purchasePrice})</a>
                                                        </c:if>
                                                    </td>
                                                    <td class="text-end">
                                                        <a href="/files/${file.id}/download">
                                                            <i class="bi bi-arrow-down-square-fill pe-2"></i>
                                                        </a>
                                                        <a href="/files/${file.id}/delete">
                                                            <i class="bi-trash-fill pe-2"></i>
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
</div>

<body:toast-messages/>

</body>