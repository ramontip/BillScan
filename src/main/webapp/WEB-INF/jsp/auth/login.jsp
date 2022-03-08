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

<!doctype html>
<html lang="de">
<head>
    <title>BillScan • <spring:message code="generic.login"/></title>
    <head:meta-main/>
    <bootstrap:bootstrap-css/>
</head>
<body class="bs-background-pattern">
<div class="container vh-100 d-flex flex-column align-items-center justify-content-center my-4">

    <a class="navbar-brand mb-3" href="/">
        <img src="/img/billscan-logo-light.svg" alt="BillScan Logo" height="50px" draggable="false">
    </a>

    <body:login-form/>

</div>
</body>