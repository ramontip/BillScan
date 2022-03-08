<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%--@elvariable id="user" type="net.billscan.billscan.entity.User"--%>
<form:form modelAttribute="user" class="bs-form-auth text-start" method="post" action="/login">

    <c:if test="${not empty param.logout}">
        <div class="alert alert-primary" role="alert">
            <spring:message code="loginForm.tag.loggedOut"/>
        </div>
    </c:if>

    <c:if test="${not empty param.deleted}">
        <div class="alert alert-primary" role="alert">
            <spring:message code="loginForm.tag.deleted"/>
        </div>
    </c:if>

    <div class="h4 bs-form-heading mb-4 text-bs-primary-dark">Login</div>


    <c:if test="${param.error != null}">
        <div class="alert alert-danger" role="alert">
            <spring:message code="loginForm.tag.error"/>
        </div>
    </c:if>

    <!---------------- username ------------------>
    <div class="form-floating">
        <input type="text" id="username" name="username" class="form-control bs-form-control" placeholder="_"
               required="required">
        <label for="username" class="form-label"><spring:message code="generic.email"/></label>
    </div>

    <!---------------- password ------------------>
    <div class="form-floating">
        <input type="password" id="password" name="password" class="form-control bs-form-control" placeholder="_"
               required="required">
        <label for="password" class="form-label"><spring:message code="generic.password"/></label>
    </div>

    <!---------------- remember me ------------------>
    <div class="my-3 ms-2">
        <input type="checkbox" id="remember-me" name="remember-me"/>
        <label for="remember-me" class="form-label small"> <spring:message code="generic.rememberMe"/></label>
    </div>

    <!---------------- button login ------------------>
    <button class="btn btn-primary bs-btn w-100" type="submit">Login</button>

</form:form>

<div class="mt-4 text-white text-center h6">
    <a href="/forgot-password" class="text-white"><spring:message
            code="generic.reset"/></a>
</div>