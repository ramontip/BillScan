<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${not empty toastError}">
    <div id="toastMessage" class="toast bs-toast" data-autohide="false" role="alert" aria-live="assertive"
         aria-atomic="true">
        <div class="toast-header">
            <strong class="me-auto text-danger"><spring:message key="generic.error"/></strong>
        </div>
        <div class="toast-body">
            <c:out value="${toastError}"/>
        </div>
    </div>
</c:if>

<c:if test="${not empty toastSuccess}">
    <div id="toastMessage" class="toast bs-toast bs-box-shadow border-0" data-autohide="false" role="alert" aria-live="assertive"
         aria-atomic="true">
        <div class="toast-header">
            <strong class="me-auto text-success"><spring:message key="generic.success"/></strong>
        </div>
        <div class="toast-body">
            <c:out value="${toastSuccess}"/>
        </div>
    </div>
</c:if>