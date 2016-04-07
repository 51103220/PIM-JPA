<%@ page language="java" contentType="text/html; charset=charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<nav class="navbar navbar-default header">
	<div class="container">
		<a href="<c:url value='/'/>" class="pull-left"><img id="logo"
			src="resources/images/logo_elca.png"> </a>
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#headingNavbar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" id="projectName" href="<c:url value='/'/>"><spring:message
					code="application.prjName" /></a>
		</div>
		<div class="collapse navbar-collapse" id="headingNavbar">
			<c:set var="lang" value="${cookie['localeCookie'].value}" />
			<ul class="nav navbar-nav navbar-right" id="langOption">
				<c:choose>
					<c:when test="${lang == 'fr'}">
						<li><a href="<c:url value='?lang=en'/>">EN</a></li>
						<li><a href="<c:url value='?lang=fr'/>" class="selected">&nbsp;|&nbsp;FR</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="<c:url value='?lang=en'/>" class="selected">EN</a></li>
						<li><a href="<c:url value='?lang=fr'/>">&nbsp;|&nbsp;FR</a></li>
					</c:otherwise>
				</c:choose>

			</ul>
			<ul class="nav navbar-nav navbar-right" id="funcOption">
				<li><a href="#" id="helpButton" class="selected"><spring:message
							code="link.help" /></a></li>
				<li><a href="#" id="logButton"><spring:message
							code="link.logout" /></a></li>
			</ul>
		</div>
	</div>
</nav>
