<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="bootstrap" tagdir="/WEB-INF/tags/bootstrap" %>
<%@taglib prefix="head" tagdir="/WEB-INF/tags/common/head" %>
<%@taglib prefix="body" tagdir="/WEB-INF/tags/common/body" %>
<%@taglib prefix="scripts" tagdir="/WEB-INF/tags/common/scripts" %>

<!doctype html>
<html lang="de">
<head>
    <title>BillScan • <spring:message code="index.signUp"/></title>
    <head:meta-main/>
    <bootstrap:bootstrap-css/>
</head>
<body class="bs-background-pattern">
<div class="container vh-100 d-flex flex-column align-items-center justify-content-center">

    <a class="navbar-brand mb-3" href="/">
        <img src="/img/billscan-logo-light.svg" alt="BillScan Logo" height="50px" draggable="false">
    </a>

    <%--@elvariable id="userDto" type="net.billscan.billscan.dto.UserDto"--%>
    <form:form modelAttribute="userDto" class="bs-form-auth text-start" method="post" action="registration"
               autocomplete="off">

        <div class="h4 bs-form-heading mb-4 text-bs-primary-dark"><spring:message code="index.signUp"/></div>

        <c:if test="${not empty messageError}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${messageError}"/>
            </div>
        </c:if>

        <!---------------- first name ------------------>
        <div class="form-floating">
            <c:set var="firstNameInvalid"><form:errors var="firstNameInvalid" path="firstname"
                                                       cssClass="invalid-feedback text-center mb-2"/></c:set>
            <form:input path="firstname"
                        class="form-control bs-form-control ${not empty firstNameInvalid ? 'is-invalid' : ''}"
                        id="inputFirstName" type="text" placeholder="_" required="required"/>
                ${firstNameInvalid}
            <label for="inputFirstName" class="form-label"><spring:message code="generic.firstname"/></label>
        </div>

        <!---------------- last name ------------------>
        <div class="form-floating">
            <c:set var="lastNameInvalid"><form:errors var="lastNameInvalid" path="lastname"
                                                      cssClass="invalid-feedback text-center mb-2"/></c:set>
            <form:input path="lastname"
                        class="form-control bs-form-control ${not empty lastNameInvalid ? 'is-invalid' : ''}"
                        id="inputLastName" type="text" placeholder="_" required="required"/>
                ${lastNameInvalid}
            <label for="inputLastName" class="form-label"><spring:message code="generic.lastname"/></label>
        </div>

        <!---------------- username ------------------>
        <div class="form-floating">
            <c:set var="usernameInvalid"><form:errors var="usernameInvalid" path="username"
                                                      cssClass="invalid-feedback text-center mb-2"/></c:set>
            <form:input path="username"
                        class="form-control bs-form-control ${not empty usernameInvalid ? 'is-invalid' : ''}"
                        id="inputUsername" type="email" placeholder="_" required="required"/>
                ${usernameInvalid}
            <label for="inputUsername" class="form-label"><spring:message code="generic.email"/></label>
        </div>

        <!---------------- password ------------------>
        <div class="form-floating">
            <c:set var="passwordInvalid"><form:errors var="passwordInvalid" path="password"
                                                      cssClass="invalid-feedback text-center mb-2"/></c:set>
            <form:input path="password"
                        class="form-control bs-form-control ${not empty passwordInvalid ? 'is-invalid' : ''}"
                        id="inputPassword" type="password" placeholder="_" required="required"/>
                ${passwordInvalid}
            <label for="inputPassword" class="form-label"><spring:message code="generic.password"/></label>
        </div>

        <!---------------- confirm password ------------------>
        <div class="form-floating">
            <c:set var="confirmPasswordInvalid"><form:errors var="passwordInvalid" path="password"
                                                             cssClass="invalid-feedback text-center mb-2"/></c:set>
            <form:input path="confirmPassword"
                        class="form-control bs-form-control ${not empty passwordInvalid ? 'is-invalid' : ''}"
                        id="inputConfirmPassword" type="password" placeholder="_" required="required"/>
                ${confirmPasswordInvalid}
            <label for="inputConfirmPassword" class="form-label"><spring:message code="generic.confirmPass"/></label>
        </div>

        <!---------------- button ------------------>
        <button class="btn btn-primary w-100 bs-btn" type="submit"><spring:message code="index.signUp"/></button>

    </form:form>

    <div class="mt-4 text-white text-center h6">
        <spring:message code="generic.alreadyAccount"/> <a href="login" class="text-white-50">Login</a>
    </div>
</div>
</body>