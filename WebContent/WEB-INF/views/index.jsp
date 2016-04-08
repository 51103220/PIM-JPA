<%@ page language="java" contentType="text/html; charset=charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=yes">

<spring:url value="/resources/css/boostrap.css" var="bootCSS" />
<spring:url value="/resources/css/main.css" var="mainCSS" />
<spring:url value="/resources/css/form.css" var="formCSS" />
<spring:url value="/resources/css/footer.css" var="footerCSS" />
<spring:url value="/resources/css/header.css" var="headerCSS" />
<spring:url value="/resources/css/body.css" var="bodyCSS" />
<spring:url value="/resources/css/jquery-ui.css" var="jqueryUICSS" />
<spring:url value="/resources/js/jquery-1.12.0.min.js" var="jquery" />
<spring:url value="/resources/js/main.js" var="mainJS" />
<spring:url value="/resources/js/boostrap.js" var="bootJS" />
<spring:url value="/resources/js/datepicker.js" var="datePickerJS" />
<spring:url value="/resources/js/moment.js" var="momentJS" />
<spring:url value="/resources/js/tablesorter.js" var="tablesorterJS" />
<link href="${bootCSS}" rel="stylesheet" />
<link href="${mainCSS}" rel="stylesheet" />
<link href="${headerCSS}" rel="stylesheet" />
<link href="${footerCSS}" rel="stylesheet" />
<link href="${bodyCSS}" rel="stylesheet" />
<link href="${formCSS}" rel="stylesheet" />
<link href="${jqueryUICSS}" rel="stylesheet" />
<script src="${jquery}"></script>
<script src="${tablesorterJS}"></script>

<title><spring:message code="application.tittle" /></title>
</head>
<body>
	<div class="wrapper">
		<jsp:include page="header.jsp" />
		<jsp:include page="body.jsp" />
	</div>
	<div id="dialog" title="Confirmation Required"></div>

</body>
<script src="${bootJS}"></script>
<script src="${momentJS}"></script>
<script src="${datePickerJS}"></script>
<script src="${mainJS}"></script>
</html>