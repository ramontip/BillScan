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
    <title>BillScan • <spring:message code="household.create.header"/></title>
</head>


<body class="d-flex h-100 w-100">
<body:sidebar/>

<div class="w-100">

    <body:navbar/>
    <body:header activePage="dashboard"/>

    <div class="container">

        <div class="d-grid justify-content-center">
            <div class="col py-4">
                <a href="/dashboard" class="btn btn-link text-light text-decoration-none">
                    <i class="bi bi-arrow-left me-1"></i><spring:message code="generic.goBack"/>
                </a>
            </div>
        </div>

        <%--@elvariable id="invitationCodeDto" type="net.billscan.app.billscan.dto.InvitationCodeDto"--%>
        <form:form modelAttribute="invitationCodeDto" class="bs-form bg-white bs-box-shadow rounded-3 text-start"
                   method="post" action="/household/join">
            <div class="h5 bs-form-heading mb-4">
                <spring:message code="household.join.header"/>
            </div>

            <c:if test="${not empty messageError}">
                <div class="alert alert-danger" role="alert">
                    <c:out value="${messageError}"/>
                </div>
            </c:if>

            <!---------------- invitationCode ------------------>
            <div class="form-floating">
                <c:set var="invitationCodeInvalid"><form:errors var="invitationCodeInvalid" path="invitationCode"
                                                                cssClass="invalid-feedback text-center mb-2"/></c:set>
                <form:input path="invitationCode"
                            class="form-control bs-form-control ${not empty invitationCodeInvalid ? 'is-invalid' : ''}"
                            id="inputInvitationCode" type="text" placeholder="_" required="required"/>
                    ${invitationCodeInvalid}
                <label for="inputInvitationCode" class="form-label"><spring:message
                        code="household.join.invitationCode"/></label>
            </div>

            <!---------------- button ------------------>
            <button class="btn btn-primary bs-btn w-100 mt-2" type="submit">
                <spring:message code="household.join.button"/>
            </button>
        </form:form>

    </div>
</div>

<body:toast-messages/>

</body>