<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!--  Listing grid -->

<display:table pagesize="5" class="displaytag"  name="categories" requestURI="${requestURI}" id="row">

<!-- Attributes -->

<spring:message code="category.name" var="name"/>
<display:column title="${name}" sortable="false">
	<a href="${requestURI}?categoryId=${row.id}"><jstl:out value="${row.name}"/></a>
</display:column>

<spring:message code="category.description" var="description"/>
<display:column title="${description}" sortable="false">
	<jstl:out value="${row.description}"/>
</display:column>

<security:authorize access="hasRole('ADMINISTRATOR')">
	<display:column>
			<a href="category/administrator/edit.do?categoryId=${row.id}">
				<spring:message	code="category.edit" />
			</a>
	</display:column>
</security:authorize>

</display:table>

<security:authorize access="hasRole('ADMINISTRATOR')">
	<a href="category/administrator/create.do"> 
		<spring:message code="category.create" />
	</a>
	<br/>
</security:authorize>
