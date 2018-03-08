

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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="category/administrator/edit.do" modelAttribute="category">
	
	<form:hidden path="id" />
	  
	<acme:textbox code="category.name" path="name"/>
	
	<acme:textarea code="category.description" path="description"/>
	
	<%-- <form:select path="parent">
		<form:option label="${category.parent.name}" value="${category.parent.id}" />
		<form:options items="${categories}" itemLabel="name" itemValue="id" />
	</form:select> --%>
	
	<form:label path="parent"><spring:message code="category.parent" /></form:label>
	<form:select path="parent">
		<form:option
			label="----"
			value="0" />
		<form:options
			items="${categories}"
			itemLabel="name"
			itemValue="id" />
	</form:select>
	<br />
		
	<acme:submit name="save" code="category.save"/>
	
	<jstl:if test="${category.categories[0]==null}">
		<acme:submit name="delete" code="category.delete"/>
	</jstl:if>
	
	<acme:cancel url="category/administrator/list.do" code="category.cancel"/>
	
</form:form>
	