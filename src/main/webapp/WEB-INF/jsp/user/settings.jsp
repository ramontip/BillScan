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
    <title>BillScan • <spring:message code="settings.title"/></title>
</head>

<body class="d-flex h-100 w-100">

<body:sidebar/>

<div class="w-100">

    <body:navbar/>
    <body:header activePage="settings"/>

    <div class="container">

        <div class="d-grid justify-content-center">
            <div class="col py-4">
                <a href="dashboard" class="btn btn-link text-light text-decoration-none">
                    <i class="bi bi-arrow-left me-1"></i><spring:message code="generic.goBack"/>
                </a>
            </div>
        </div>

        <div class="justify-content-center row">

            <div class="col mx-2">

                <div class="col mb-5">
                    <form:form class="bs-form bg-white rounded-3 text-start" method="post"
                               action="/settings/update/thumbnail"
                               enctype="multipart/form-data">

                        <div class="h5 bs-form-heading mb-4"><spring:message code="settings.updImg.title"/></div>

                        <c:if test="${not empty messageError}">
                            <div class="alert alert-danger" role="alert">
                                <c:out value="${messageError}"/>
                            </div>
                        </c:if>

                        <!---------------- thumbnail ------------------>
                        <div class="form-floating">
                            <input id="thumbnail" name="thumbnail" type="file"
                                   class="form-control bs-form-control form-control-sm lpt-4-5" data-show-upload="true"
                                   data-show-caption="true" accept="image/png, image/jpeg" required="required"/>
                            <label for="thumbnail" class="form-label"><spring:message
                                    code="generic.addPicture"/></label>
                        </div>

                        <!---------------- button ------------------>
                        <button class="btn btn-primary bs-btn w-100 mt-2" type="submit">
                            <spring:message code="generic.update"/>
                        </button>

                    </form:form>
                </div>

                <div class="col mb-5">
                    <%--@elvariable id="passwordChangeDto" type="net.billscan.app.billscan.dto.PasswordChangeDto"--%>
                    <form:form modelAttribute="passwordChangeDto" class="bs-form bg-white rounded-3 text-start"
                               method="post" action="/settings/update/password">

                        <div class="h5 bs-form-heading mb-4"><spring:message code="settings.updPass.title"/></div>

                        <!---------------- current password ------------------>
                        <div class="form-floating">
                            <c:set var="currentPasswordInvalid"><form:errors var="currentPasswordInvalid"
                                                                             path="currentPassword"
                                                                             cssClass="invalid-feedback text-center mb-2"/></c:set>
                            <form:input path="currentPassword"
                                        class="form-control bs-form-control ${not empty currentPasswordInvalid ? 'is-invalid' : ''}"
                                        id="inputCurrentPassword" type="password" placeholder="_"
                                        required="required"/>
                                ${currentPasswordInvalid}
                            <label for="inputCurrentPassword" class="form-label"><spring:message
                                    code="settings.updPass.currPass"/></label>
                        </div>

                        <!---------------- new password ------------------>
                        <div class="form-floating">
                            <c:set var="newPasswordInvalid"><form:errors var="newPasswordInvalid" path="newPassword"
                                                                         cssClass="invalid-feedback text-center mb-2"/></c:set>
                            <form:input path="newPassword"
                                        class="form-control bs-form-control ${not empty newPasswordInvalid ? 'is-invalid' : ''}"
                                        id="inputNewPassword" type="password" placeholder="_" required="required"/>
                                ${newPasswordInvalid}
                            <label for="inputNewPassword" class="form-label"><spring:message
                                    code="settings.updPass.newPass"/></label>
                        </div>

                        <!---------------- confirm password ------------------>
                        <div class="form-floating">
                            <c:set var="confirmPasswordInvalid"><form:errors var="confirmPasswordInvalid"
                                                                             path="confirmPassword"
                                                                             cssClass="invalid-feedback text-center mb-2"/></c:set>
                            <form:input path="confirmPassword"
                                        class="form-control bs-form-control ${not empty newPasswordInvalid ? 'is-invalid' : ''}"
                                        id="inputConfirmPassword" type="password" placeholder="_"
                                        required="required"/>
                                ${confirmPasswordInvalid}
                            <label for="inputConfirmPassword" class="form-label"><spring:message
                                    code="settings.updPass.repPass"/></label>
                        </div>

                        <!---------------- button ------------------>
                        <button class="btn btn-primary bs-btn w-100 mt-2" type="submit">
                            <spring:message code="generic.update"/>
                        </button>
                    </form:form>
                </div>

                <div class="row mt-5">
                    <a href="/settings/deleteProfile"
                       class="btn btn-sm text-danger text-center ">
                        <spring:message code="generic.deleteProfile"/>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<body:toast-messages/>

</body>