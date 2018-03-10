

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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="service/manager/edit.do" modelAttribute="service">

	<form:hidden path="id" />

	<acme:textbox code="service.name" path="name" />

	<acme:textarea code="service.description" path="description" />

	<acme:textbox code="service.picture" path="picture" />
	
	<form:label path="category"><spring:message code="service.category" /></form:label>
	<form:select path="category">
		<form:option
			label="----"
			value="0" />
		<form:options
			items="${categories}"
			itemLabel="name"
			itemValue="id" />
	</form:select>
	<br />
		
	<acme:submit name="save" code="service.save"/>
	
	<jstl:if test="${service.id!=0}">
		<acme:submit name="delete" code="service.delete"/>
	</jstl:if>
	
	<acme:cancel url="service/manager/list.do" code="service.cancel"/>

</form:form>