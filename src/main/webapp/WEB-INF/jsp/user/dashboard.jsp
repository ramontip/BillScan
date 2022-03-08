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
    <title>BillScan • <spring:message code="sidebar.dashboard"/></title>
</head>

<body class="d-flex h-100 w-100">

<body:sidebar/>

<div class="w-100">

    <body:navbar/>
    <body:header activePage="dashboard"/>

    <div class="container-fluid p-5">
        <div class="row row-cols-1 row-cols-md-2 g-4">

            <c:forEach var="household" items="${households}">

                <a href="/household/${household.id}/show">
                    <div class="col">
                        <div class="card col bs-box-shadow h-100">
                            <div class="card-body">
                                <!---------------- header ---------------- -->
                                <div class="row justify-content-center">
                                    <div class="col text-center text-muted px-1">
                                        <div class="d-flex justify-content-center text-light">
                                            <i class="bi bi-house-door-fill align-self-center pe-1"></i>
                                            <div class="h5 card-text"><spring:message code="household.title"/></div>
                                        </div>
                                    </div>
                                </div>
                                <!---------------- household title ---------------- -->
                                <div class="d-flex justify-content-center py-3">
                                    <h3 class="card-text"><c:out
                                            value="${household.title}"/></h3>
                                </div>

                                <!---------------- member icons ---------------- -->
                                <div class="d-flex justify-content-center py-1">

                                    <c:forEach var="user" items="${household.users}" varStatus="status">

                                        <c:if test="${status.index < 5}">

                                            <c:choose>
                                                <c:when test="${user.thumbnail != null}">
                                                    <div class="bs-thumbnail rounded-circle m-1 border-0"
                                                         style="background-image: url('/user/thumbnail/${user.id}')"></div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="bs-thumbnail rounded-circle m-1 border-0"
                                                         style="background-image: url('/img/thumbnail.png')"></div>
                                                </c:otherwise>
                                            </c:choose>

                                        </c:if>

                                        <c:if test="${status.last}">
                                            <div class="bs-dashboard-member rounded-circle m-1 text-center text-white text align-self-center">
                                                    ${status.count}
                                            </div>
                                        </c:if>

                                    </c:forEach>

                                </div>

                            </div>
                        </div>
                    </div>
                </a>

            </c:forEach>

        </div>
    </div>

</div>

<body:toast-messages/>

</body>