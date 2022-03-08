<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="scripts" tagdir="/WEB-INF/tags/common/scripts" %>

<scripts:main/>

<nav class="navbar navbar-expand-sm navbar bg-white bs-bar-shadow" aria-label="Navbar">
    <div class="container-fluid">

        <!-- Search -->
        <div class="container-fluid">
            <div class="row row-cols-2 justify-content-start">
                <div class="col-12 col-sm-8 col-md-6 col-lg-8">
                    <form method="get" action="/search" class="input-group m-0">
                        <input type="text" name="term" class="form-control border-0 bs-form-search"
                               placeholder="<spring:message code="search.placeholder"/>">
                        <button class="btn btn-primary rounded-end input-group-append align-items-center align-content-center"
                                type="submit"><i
                                class="bi bi-search mx-1"></i></button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Profile -->
        <div class="collapse navbar-collapse" id="navbarsExample03">
            <ul class="navbar-nav me-auto mb-2 mb-sm-0">
                <li class="nav-item pt-2 pb-2 pe-2 ps-sm-1 ps-lg-4 d-none d-md-block">
                    <c:choose>
                        <c:when test="${currentUser.thumbnail != null}">
                            <div class="bs-thumbnail rounded-circle border-0"
                                 style="background-image: url('/user/thumbnail/${currentUser.id}')"></div>
                        </c:when>
                        <c:otherwise>
                            <div class="bs-thumbnail rounded-circle border-0"
                                 style="background-image: url('/img/thumbnail.png')"></div>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li class="nav-item pt-2">
                    <div class="dropdown">
                        <a class="nav-link btn dropdown-toggle"
                           href="#" role="button"
                           id="dropdownMenuLink" data-bs-toggle="dropdown"
                           aria-expanded="false">${currentUser.firstname} ${currentUser.lastname} </a>
                        <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                            <li>
                                <form:form method="post" action="/logout">
                                    <button class="dropdown-item" type="submit"><spring:message
                                            code="generic.logout"/></button>
                                </form:form>
                            </li>
                        </ul>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</nav>