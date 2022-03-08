<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="head" tagdir="/WEB-INF/tags/common/head" %>
<%@taglib prefix="body" tagdir="/WEB-INF/tags/common/body" %>
<%@taglib prefix="scripts" tagdir="/WEB-INF/tags/common/scripts" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<head>
    <head:meta-main/>
    <!-- JQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
</head>


<body class="d-flex h-100 w-100">
<body:sidebar/>

<div class="w-100">

    <body:navbar/>
    <body:header activePage="household"/>
    <div class="overflow-auto bs-content w-100">
        <div class="container w-100">

            <div class="d-grid justify-content-center">
                <div class="col py-4">
                    <a href="/household/${household.id}/show" class="btn btn-link text-light text-decoration-none">
                        <i class="bi bi-arrow-left me-1"></i><spring:message code="generic.goBack"/>
                    </a>
                </div>
            </div>

            <div class="justify-content-center row">

                <div class="col mx-2">

                    <div class="col mb-5">

                        <div class="card bs-box-shadow bs-form">
                            <div class="card-body p-0">
                                <div class="row align-items-center">
                                    <div class="h5 bs-form-heading mb-4">
                                        <spring:message code="household.join.invitationCode"/>
                                    </div>
                                </div>
                                <div class="row align-items-center">
                                    <div class="col-auto">
                                        <div class="btn btn-primary btn-block user-select-all text-white">
                                            <span class="h5">
                                                ${household.invitationCode}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col mb-5">
                        <%--@elvariable id="householdDto" type="net.billscan.app.billscan.dto.HouseholdDto"--%>
                        <form:form modelAttribute="householdDto"
                                   class="bs-form bg-white bs-box-shadow rounded-3 text-start"
                                   method="post" action="/household/${household.id}/updateTitle">
                            <div class="h5 bs-form-heading mb-4">
                                <spring:message code="household.manage.change"/>
                            </div>

                            <c:if test="${not empty messageError}">
                                <div class="alert alert-danger" role="alert">
                                    <c:out value="${messageError}"/>
                                </div>
                            </c:if>

                            <!---------------- title ------------------>
                            <div class="form-floating">
                                <c:set var="titleInvalid"><form:errors var="titleInvalid" path="title"
                                                                       cssClass="invalid-feedback text-center mb-2"/></c:set>
                                <form:input path="title"
                                            class="form-control bs-form-control ${not empty titleInvalid ? 'is-invalid' : ''}"
                                            id="inputTitle" type="text" placeholder="_" required="required"/>
                                    ${titleInvalid}
                                <label for="inputTitle" class="form-label"><spring:message
                                        code="household.create.title"/></label>
                            </div>

                            <!---------------- button ------------------>
                            <button class="btn btn-primary bs-btn w-100 mt-2" type="submit">
                                <spring:message code="generic.change"/>
                            </button>
                        </form:form>
                    </div>

                    <div class="col mb-5">

                        <c:if test="${household.createdByUser.id eq currentUser.id}">
                            <div class="card bs-box-shadow bs-form">
                                <div class="card-body p-0">
                                    <div class="row">
                                        <div class="h5 bs-form-heading mb-4">
                                            <spring:message code="generic.members"/>
                                        </div>
                                    </div>
                                    <div class="table-responsive">
                                        <table class="table table-hover table-striped align-middle">
                                            <tbody class="align-content-center">
                                            <c:forEach var="user" items="${household.users}" varStatus="i">
                                                <tr>
                                                    <th scope="row">${user.firstname} ${user.lastname}</th>
                                                    <td class="text-end">
                                                        <c:if test="${user.id == household.createdByUser.id}">
                                                            <spring:message code="generic.creator"/>
                                                        </c:if>
                                                        <c:if test="${user.id != household.createdByUser.id}">
                                                            <a href="/household/${household.id}/delete-user/${user.id}/">
                                                                <i class="bi bi-trash-fill"></i>
                                                            </a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>

                                </div>
                            </div>

                            <div class="row mt-5">
                                <a href="/household/${household.id}/delete"
                                   class="btn btn-sm text-danger text-center ">
                                    <spring:message code="generic.deleteHousehold"/>
                                </a>
                            </div>
                        </c:if>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<body:toast-messages/>

</body>