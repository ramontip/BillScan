<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="bs-sidebar d-flex flex-column flex-shrink-0 text-white bg-secondary">

    <div class="bs-sidebar-header justify-content-center align-items-center">
        <a class="navbar-brand d-flex align-items-center justify-content-center h-100" href="/dashboard">
            <img src="/img/billscan-logo-light.svg" alt="Billscan Logo"
                 height="36px" draggable="false">
        </a>
    </div>

    <div class="py-4 ps-3 pe-2">
        <ul class="nav nav-pills flex-column mb-auto">
            <li>
                <a href="/dashboard" class="nav-link text-light">
                    <i class="bi bi-collection-fill"></i>
                    <spring:message code="sidebar.dashboard"/>
                </a>
            </li>
            <li>
                <a href="/expenses" class="nav-link text-light">
                    <i class="bi bi-bar-chart-fill"></i>
                    <spring:message code="sidebar.expenses"/>
                </a>
            </li>
            <li>
                <a href="/files" class="nav-link text-light">
                    <i class="bi bi-folder-fill"></i>
                    <spring:message code="sidebar.files"/>
                </a>
                <hr class="w-75 text-white-50 mx-auto">
            </li>
            <c:forEach var="household" items="${currentUser.households}" varStatus="status">
                <li>
                    <a href="/household/${household.id}/show" class="nav-link text-light">
                        <i class="bi bi-house-door-fill"></i>
                        <c:out value="${household.title}"/>
                    </a>
                </li>
                <c:if test="${status.last}">
                    <hr class="w-75 text-white-50 mx-auto">
                </c:if>
            </c:forEach>
            <li>
                <a href="/settings" class="nav-link text-light">
                    <i class="bi bi-gear-fill"></i>
                    <spring:message code="settings.title"/>
                </a>
            </li>
        </ul>
    </div>
    <footer class="footer bs-footer mt-auto">
        <div class="container-fluid py-2 text-center">
            <p class="text-white-50 mb-0">
                <spring:message code="sidebar.footer1"/>
            </p>
            <p class="text-white-50 mb-0">
                <spring:message code="sidebar.footer2"/>
            </p>
            <p class="text-white-50 mb-0">
                <spring:message code="sidebar.footer3"/>
            </p>
        </div>
    </footer>
</div>


