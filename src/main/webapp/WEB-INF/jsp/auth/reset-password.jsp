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
    <title>BillScan • <spring:message code="generic.resetPassword"/></title>
    <head:meta-main/>
    <bootstrap:bootstrap-css/>
</head>
<body class="bs-background-pattern">
<div class="container vh-100 d-flex flex-column align-items-center justify-content-center">

    <a class="navbar-brand mb-3" href="/">
        <img src="/img/billscan-logo-light.svg" alt="BillScan Logo" height="50px" draggable="false">
    </a>

    <%--@elvariable id="user" type="net.billscan.billscan.entity.User"--%>
    <form:form modelAttribute="user" class="bs-form-auth text-start" method="post" action="/reset-password">

        <div class="h4 bs-form-heading mb-4 text-bs-primary-dark"><spring:message code="generic.resetPassword"/></div>

        <c:if test="${not empty messageError}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${messageError}"/>
            </div>
        </c:if>

        <c:choose>

            <c:when test="${validPasswordReset eq false}">
                <div class="alert alert-danger" role="alert">
                    <spring:message code="generic.passResetText"/> <a href="/forgot-password"
                                                                      class="text-danger"><spring:message
                        code="generic.passGoToText"/></a>
                </div>
            </c:when>

            <c:otherwise>
                <input type="hidden" id="token" name="token" value="${token}" hidden>

                <!---------------- password ------------------>
                <div class="form-floating">
                    <input type="password" id="password" name="password" class="form-control bs-form-control"
                           placeholder="_" required="required">
                    <label for="password" class="form-label"><spring:message code="settings.updPass.newPass"/></label>
                </div>

                <!---------------- confirm password ------------------>
                <div class="form-floating">
                    <input type="password" id="confirmPassword" name="confirmPassword"
                           class="form-control bs-form-control" placeholder="_" required="required">
                    <label for="confirmPassword" class="form-label"><spring:message
                            code="generic.confirmNewPass"/></label>
                </div>

                <!---------------- button login ------------------>
                <button class="btn btn-primary w-100 bs-btn mt-2" type="submit"><spring:message
                        code="generic.updatePass"/></button>
            </c:otherwise>

        </c:choose>

    </form:form>

    <div class="mt-4 text-white text-center h6">
        <spring:message code="forgotPass.wrongPage"/> <a href="/" class="text-white-50"><spring:message
            code="generic.back"/></a>
    </div>

</div>
</body>