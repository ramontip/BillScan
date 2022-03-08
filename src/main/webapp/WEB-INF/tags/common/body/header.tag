<%@attribute name="activePage" required="true" %>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="bootstrap" tagdir="/WEB-INF/tags/bootstrap" %>
<%@taglib prefix="scripts" tagdir="/WEB-INF/tags/common/scripts" %>

<div class="container-fluid bg-white bs-bar-shadow py-3">
    <div class="row">
        <div class="col d-flex align-items-center">
            <div class="row justify-content-start py-2">
                <div class="col-auto">
                    <c:choose>
                        <c:when test="${activePage == 'dashboard'}">
                            <span class="h4 text-bs-primary-dark mt-1 ms-3"><i
                                    class="me-3 bi bi-collection-fill text-bs-primary-dark"></i><spring:message
                                    code="sidebar.dashboard"/>
                            </span>
                        </c:when>
                        <c:when test="${activePage == 'expenses'}">
                            <span class="h4 text-bs-primary-dark mt-1 ms-3"><i
                                    class="me-3 bi bi-bar-chart-fill text-bs-primary-dark"></i><spring:message
                                    code="sidebar.expenses"/>
                            </span>
                        </c:when>
                        <c:when test="${activePage == 'files'}">
                            <span class="h4 text-bs-primary-dark mt-1 ms-3"><i
                                    class="me-3 bi bi-folder-fill text-bs-primary-dark"></i><spring:message
                                    code="sidebar.files"/>
                            </span>
                        </c:when>
                        <c:when test="${activePage == 'settings'}">
                            <span class="h4 text-bs-primary-dark mt-1 ms-3"><i
                                    class="me-3 bi bi-gear-fill text-bs-primary-dark"></i><spring:message
                                    code="generic.settings"/>
                            </span>
                        </c:when>
                        <c:when test="${activePage == 'household'}">
                            <div class="d-flex">
                                <div class="h4 bs-svg-color mt-1 ms-3 text-light d-flex">
                                    <i class="me-3 bi bi-house-door-fill"></i>
                                    <span class="align-text-bottom"><spring:message
                                            code="household.title"/></span>
                                    <span class="px-2 text-light">|</span>
                                    <span class="text-bs-primary-dark align-text-bottom">
                                            ${household.title}
                                    </span>
                                </div>
                                <div class="d-flex ms-2">
                                    <c:forEach var="user" items="${household.users}" varStatus="status">

                                        <c:if test="${status.index < 5}">

                                            <c:choose>
                                                <c:when test="${user.thumbnail != null}">
                                                    <div class="bs-thumbnail-small rounded-circle m-1 border-0"
                                                         style="background-image: url('/user/thumbnail/${user.id}')"></div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="bs-thumbnail-small rounded-circle m-1 border-0"
                                                         style="background-image: url('/img/thumbnail.png')"></div>
                                                </c:otherwise>
                                            </c:choose>

                                        </c:if>

                                        <c:if test="${status.last}">
                                            <div class="bs-dashboard-member-small rounded-circle m-1 text-center text-white text align-self-center">
                                                    ${status.count}
                                            </div>
                                        </c:if>

                                    </c:forEach>
                                </div>
                            </div>
                        </c:when>
                        <c:when test="${activePage == 'search'}">
                            <span class="h4 bs-svg-color mt-1 ms-3"><i
                                    class="me-3 bi bi-search text-bs-primary-dark"></i>
                                <span class="text-bs-primary-dark align-text-bottom"><spring:message
                                        code="generic.resultsFor"/> <i><c:out value="${activeSearch}"/></i>
                                </span>
                            </span>
                        </c:when>
                    </c:choose>
                </div>
            </div>
        </div>
        <div class="col-auto d-flex align-items-center">
            <c:choose>
                <c:when test="${activePage == 'dashboard'}">
                    <a class="btn btn-bs-blue-button text-white me-3" href="/household/create" role="button">
                        <i class="bi bi-plus-square me-1"></i>
                        <spring:message
                                code="household.create.header"/></a>
                    <a class="btn btn-info text-white me-3" href="/household/join" role="button"><i
                            class="bi bi-box-arrow-right me-1"></i><spring:message
                            code="household.create.accept"/></a>
                </c:when>
                <c:when test="${activePage == 'household'}">
                    <a class="btn btn-bs-blue-button text-white me-3" href="/household/${household.id}/bill/upload"
                       role="button">
                        <i class="bi bi-camera-fill"></i>
                        <spring:message
                                code="bill.title"/></a>
                    <a class="btn btn-info text-white me-3" href="/household/${household.id}/edit" role="button"><i
                            class="bi bi-gear-fill"></i><spring:message
                            code="bill.manage"/></a>
                </c:when>
            </c:choose>
        </div>
    </div>
</div>



