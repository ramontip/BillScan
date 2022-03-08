<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layout" %>
<%@taglib prefix="bootstrap" tagdir="/WEB-INF/tags/bootstrap" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="head" tagdir="/WEB-INF/tags/common/head" %>

<!doctype html>
<html lang="de">
<head>
    <title>BillScan • <spring:message code="error.title"/></title>
    <head:meta-main/>
    <bootstrap:bootstrap-css/>
</head>

<body class="bs-background-pattern">
<div class="container vh-100 d-flex flex-column align-items-center justify-content-center text-white">

    <c:if test="${statusCode != null}">
        <h1><c:out value="${statusCode}"/></h1>
    </c:if>
    <span class="h2 py-3"><spring:message code="error.title"/></span>
    <a href="/">
        <button class="btn btn-light w-100 mt-2" type="submit"><spring:message code="generic.goBack"/></button>
    </a>

</div>
</body>