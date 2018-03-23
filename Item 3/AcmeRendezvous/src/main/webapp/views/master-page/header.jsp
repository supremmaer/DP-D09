<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="/AcmeRendezvous"><img class="banner" src="${pageBanner}"
		alt="Sample Co., Inc." /></a>
</div>

<h2>
	<jstl:out value="${bussinessName}" />
</h2>


<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<li><a class="fNiv"><spring:message
					code="master.page.rendezvous" /></a>
			<ul>
				<li class="arrow"></li>
				<li><a href="rendezvous/list.do"><spring:message
							code="master.page.rendezvous.list" /></a></li>
			</ul></li>
		<li><a href="actor/list.do"><spring:message
					code="master.page.anonymous.action.2" /></a></li>




		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>
			<li><a href="actor/create.do?actorType=USER"><spring:message
						code="master.page.anonymous.action.1" /></a></li>
			<li><a href="actor/create.do?actorType=MANAGER"><spring:message
						code="master.page.registrerManager" /></a></li>

		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv" href="service/user/list.do"><spring:message
						code="master.page.user.service.list" /></a> <security:authorize
					access="hasRole('MANAGER')">
					<ul>
						<li class="arrow"></li>
						<li><a href="service/user/list.do"><spring:message
									code="master.page.user.service.list" /></a></li>
						<li><a href="service/manager/list.do"><spring:message
									code="master.page.manager.service.list" /></a></li>
						<li><a href="service/manager/create.do"><spring:message
									code="master.page.manager.service.create" /></a></li>
					</ul>
				</security:authorize></li>


			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('USER')">
						<li><a href="rendezvous/user/list.do"><spring:message
									code="master.page.user.rendezvous.list" /></a></li>
						<li><a href="announcement/user/list.do"><spring:message
									code="master.page.user.announcement.list" /></a></li>
						<li><a href="rendezvous/user/listrsvp.do"><spring:message
									code="master.page.user.rendezvous.listrsvp" /></a></li>
					</security:authorize>


					<security:authorize access="hasRole('ADMINISTRATOR')">
						<li><a href="administrator/dashboard.do"><spring:message
									code="master.page.administrator.dashboard" /></a></li>
						<li><a href="category/administrator/list.do"><spring:message
									code="master.page.administrator.categories" /></a></li>
						<li><a href="config/administrator/edit.do"><spring:message
									code="master.page.administrator.config.edit" /></a></li>
					</security:authorize>

					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>
				</ul></li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

