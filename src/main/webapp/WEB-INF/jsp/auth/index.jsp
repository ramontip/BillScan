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
    <title>BillScan • <spring:message code="index.pageTitle"/></title>
    <head:meta-main/>
</head>

<body>

<div class="container-fluid bs-background-pattern h-75 d-flex flex-column align-items-center justify-content-center">
    <!---------------- logo ---------------- -->
    <div class="container fixed-top m-0 p-4">
        <img src="/img/billscan-logo-light.svg" alt="BillScan Logo" height="50px" draggable="false">
    </div>
    <div class="container text-white text-center">
        <div class="row justify-content-around align-items-center">
            <div class="col-12 col-lg-5 my-4">
                <!---------------- Welcome text ---------------- -->
                <h1 class="h2"><spring:message code="index.pageTitle"/></h1>
                <p class="my-4 fs-5">
                    <spring:message code="index.description"/>
                </p>
                <a class="btn btn-info text-white" href="registration"><spring:message code="index.signUp"/></a>
            </div>
            <div class="col-12 col-lg-5 my-4">
                <!---------------- Log In ---------------- -->
                <body:login-form/>
            </div>
        </div>
    </div>
</div>

<footer class="footer w-100 bg-white h-25 d-flex flex-column align-items-center justify-content-center">
    <div class="row no-gutters w-75">
        <div class="row no-gutters">
            <div class="col-6 col-md-4 p-2">
                <img src="/img/fhj-logo.png" alt="FH Joanneum Logo" draggable="false"
                     style="max-height: 80px" class="w-auto p-1 img-fluid">
            </div>
            <div class="col-12 col-sm-6 col-md-8 p-2">
                <p class="ms-2">
                    <span class="text-bs-primary-dark fw-bold">BillScan</span> ist ein Projekt von <span
                        class="text-bs-primary-dark fw-bold">
                    Christopher Rolke, David Sebernegg und Ramon Tippl</span>.
                    Es wurde im Rahmen der
                    <span class="text-bs-primary-dark fw-bold">Bereichsübergreifenden Projektarbeit</span> (5. Semester,
                    Informationsmanagement IMA19, FH Joanneum) unter der
                    Betreuung von <span class="text-bs-primary-dark fw-bold">DI DI (FH) Michael Nestler</span>
                    entwickelt.
                </p>
            </div>
        </div>
    </div>
    <p class="small mt-2 text-light">BillScan • Version 1.0</p>
</footer>

</body>