

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

<form:form action="service/user/request.do" modelAttribute="requestForm">
	
	<form:hidden path="service" />
	
	<form:label path="rendezvous"><spring:message code="request.rendezvous" /></form:label>
	<form:select path="rendezvous">
		<form:option
			label="-----"
			value="0"
			/>
		<form:options
			items="${rendezvouses}"
			itemLabel="name"
			itemValue="id"
		/>
	</form:select>
	
	<acme:textarea code="request.comment" path="comment"/>
	
	<acme:textbox code="creditCard.holderName" path="holderName"/>
	<br />
	<acme:textbox code="creditCard.brandName" path="brandName"/>
	<br />
	<acme:textbox code="creditCard.number" path="number"/>
	<br />
	<acme:textbox code="creditCard.expirationMonth" path="expirationMonth"/>
	<br />
	<acme:textbox code="creditCard.expirationYear" path="expirationYear"/>
	<br />
	<acme:textbox code="creditCard.CVV" path="CVV"/>
	<br />
	<acme:submit name="save" code="service.save"/>
	
	<jstl:if test="${request.id!=0}">
		<acme:submit name="delete" code="service.delete"/>
	</jstl:if>
	
	<acme:cancel url="service/manager/list.do" code="service.cancel"/>

</form:form>