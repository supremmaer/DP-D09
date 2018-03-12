<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<!-- Listing grid -->
<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="services" requestURI="${requestURI}" id="row">

	<jstl:choose>
		<jstl:when test="${row.cancelled}">
		<tr class='red'>
		</jstl:when>
	</jstl:choose>

	<spring:message code="service.name" var="name" />
	<display:column title="${name}" sortable="false">
		<jstl:out value="${row.name}" />
	</display:column>

	<spring:message code="service.description" var="description" />
	<display:column title="${description}" sortable="false">
		<jstl:out value="${row.description}" />
	</display:column>

	<jstl:url value="${row.picture}" var="pictureURL"></jstl:url>
	<spring:message code="service.picture" var="picture" />
	<display:column title="${picture}" sortable="false">
		<img src="<jstl:out value="${row.picture}" />" />
	</display:column>

	<spring:message code="service.category" var="category" />
	<display:column title="${category}" sortable="false">
		<a href="category/list.do?categoryId=${row.id}"><jstl:out
				value="${row.category.name}" /></a>
	</display:column>

	<spring:message code="service.status" var="status" />
	<display:column title="${status}" sortable="false">
		<jstl:choose> <jstl:when test="${row.cancelled}">
		<spring:message code="service.status.cancelled"/>
		</jstl:when>
		<jstl:otherwise>
		<spring:message code="service.status.active"/>
		</jstl:otherwise>
		</jstl:choose>
		
	</display:column>

	<security:authorize access="hasRole('MANAGER')">
		<spring:message code="service.manager" var="manager" />
		<display:column title="${manager}" sortable="false">
			<a href="service/manager/edit.do?serviceId=${row.id}"><spring:message
					code="service.edit" /></a>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('USER')">
		<display:column sortable="false">
			<a href="service/user/request.do?serviceId=${row.id}"><spring:message
					code="service.request" /></a>
		</display:column>
	</security:authorize>

	<security:authorize access="hasRole('ADMINISTRATOR')">
	<display:column>
		<jstl:if test="${row.cancelled == false}">
			<a href="service/administrator/cancel.do?serviceId=${row.id}">
				<spring:message	code="service.cancel" />
			</a>
		</jstl:if>
	</display:column>
	
</security:authorize>


</display:table>




