<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layout" %>
<%@taglib prefix="bootstrap" tagdir="/WEB-INF/tags/bootstrap" %>
<%@taglib prefix="head" tagdir="/WEB-INF/tags/common/head" %>
<%@taglib prefix="body" tagdir="/WEB-INF/tags/common/body" %>
<%@taglib prefix="scripts" tagdir="/WEB-INF/tags/common/scripts" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!doctype html>
<html lang="de">
<head>
    <title>BillScan • <spring:message code="generic.forgotPass"/></title>
    <head:meta-main/>
    <bootstrap:bootstrap-css/>
</head>
<body class="bs-background-pattern">
<div class="container vh-100 d-flex flex-column align-items-center justify-content-center">

    <a class="navbar-brand mb-3" href="/">
        <img src="/img/billscan-logo-light.svg" alt="BillScan Logo" height="50px" draggable="false">
    </a>

    <%--@elvariable id="user" type="net.billscan.billscan.entity.User"--%>
    <form:form modelAttribute="user" class="bs-form-auth text-start" method="post"
               action="/forgot-password">

        <div class="h4 bs-form-heading mb-4 text-bs-primary-dark"><spring:message code="generic.forgotPass"/></div>

        <c:choose>

            <c:when test="${not forgotPasswordFormSubmitted}">
                <!---------------- username ------------------>
                <div class="form-floating">
                    <input type="text" id="username" name="username" class="form-control bs-form-control"
                           placeholder="_"
                           required="required"/>
                    <label for="username" class="form-label"><spring:message code="generic.email"/></label>
                </div>

                <!---------------- button login ------------------>
                <button class="btn btn-primary bs-btn w-100 mt-2" type="submit"><spring:message
                        code="forgotPass.sendResMail"/></button>
            </c:when>

            <c:otherwise>
                <div class="alert alert-primary" role="alert">
                    <spring:message code="forgotPass.text"/>
                </div>
            </c:otherwise>

        </c:choose>

    </form:form>

    <div class="mt-4 text-white text-center h6">
        <spring:message code="forgotPass.wrongPage"/> <a href="/" class="text-white-50"><spring:message
            code="generic.back"/></a>
    </div>

</div>
</body>