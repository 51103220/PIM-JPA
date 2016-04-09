
<%@ page language="java" contentType="text/html; charset=charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="links">
	<spring:message code="pagination.maxLinksPerPage" />
</c:set>
<div id="projectList">
	<p class="formName">
		<spring:message code="menu.projectsList" />
	</p>
	<spring:message code="placeholder.search" var="searchPlaceholder" />
	<spring:message code="placeholder.status" var="searchStatus" />

	<div class="formContent">
		<div class="errorPanel">
			<p class="panelMessage">
				<spring:message code="errors.mandatory" />
			</p>
			<a href="#" class="closePanel"> <span
				class="glyphicon glyphicon-remove"></span>
			</a>
		</div>
		<form class="form-inline" action="filterProject" role="form"
			id="searchInputs">
			<div class="form-group">
				<input type="text" id="keywords" class="form-control firstInput"
					placeholder="${searchPlaceholder}" value="${searchValue}">
			</div>
			<div class="form-group">
				<select class="form-control empty" id="statusKey">
					<c:choose>
						<c:when test="${statusKey !=null}">
							<option value="${statusKey}"><spring:message
									code="status.${statusKey}" /></option>
						</c:when>
						<c:otherwise>
							<option value="" selected disabled>${searchStatus}</option>
						</c:otherwise>
					</c:choose>

					<c:forEach items="${statusValues}" var="status">
						<c:choose>
							<c:when test="${status!=statusKey}">
								<option value="${status}"><spring:message
										code="status.${status}" /></option>
							</c:when>
						</c:choose>
					</c:forEach>
					<option></option>
				</select>

			</div>
			<div class="form-group">
				<button type="button" id="search_btn"
					class="btn btn-primary processBtn">
					<spring:message code="button.search" />
				</button>
			</div>
			<div class="form-group">
				<a href="#" id="reset_btn"> <spring:message
						code="link.resetSearch" /></a>
			</div>
		</form>
		<div class="table-responsive">
			<table class="table" id="searchDatas">
				<thead>
					<tr>
						<th></th>
						<th class="sorter-true"><spring:message code="table.number" /></th>
						<th class="sorter-true"><spring:message code="table.name" /></th>
						<th class="sorter-true"><spring:message code="table.status" /></th>
						<th class="sorter-true"><spring:message code="table.customer" /></th>
						<th class="sorter-true"><spring:message code="table.date" /></th>
						<th><spring:message code="table.delete" /></th>
					</tr>
					<tr id="filterInputs">
						<th></th>
						<th class="col30px"><input type="text" name="pNumber"
							class="form-control"></th>
						<th><input type="text" name="pName" class="form-control"></th>
						<th class="col30px"><input type="text" name="pStatus"
							class="form-control"></th>
						<th class="col30px"><input type="text" name="pCustomer"
							class="form-control"></th>
						<th class="col30px"><input type="text" name="pDate"
							class="form-control"></th>
						<th></th>
					</tr>

				</thead>
				<tbody>
					<c:forEach items="${projects}" var="project">
						<tr>
							<td align="center"><input id="${project.getId()}"
								type="checkbox" class="checkIcon" value="${project.isNew()}"></td>
							<td class="col1" align="right"><a
								href="project/${project.getId()}/detail" class="projectDetail">${project.getProjectNumber()}</a></td>
							<td class="col2">${project.getName()}</td>
							<td class="col3">${project.getStatus().getValue()}</td>
							<td class="col4">${project.getCustomer()}</td>
							<fmt:formatDate value="${project.getStartDate()}"
								var="dateString" pattern="dd.MM.yyyy" />
							<td class="col5 dateAlign">${dateString}</td>
							<td align="center"><a id="${project.getId()}-${project.getVersion()}-${project.getStatus()}" 
								href="project/${project.getId()}/delete" class="deleteIcon">
									<c:choose>
										<c:when test="${project.isNew()}">
											<span class="glyphicon glyphicon-trash"></span>
										</c:when>
										<c:otherwise>
										</c:otherwise>
									</c:choose>
							</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="resultRow">
			<p class="totalItems"></p>
			<a href="<c:url value='/deleteMultiple'/>" class="deleteMultiple"><spring:message
					code="delete.all" />&nbsp;&nbsp;&nbsp;&nbsp;<span
				class="glyphicon glyphicon-trash"></span> </a>
		</div>
		<div>
			<p class="searchResult">${searchResult}</p>
		</div>
	</div>
	<input type="hidden" id="paginationMax" value="${pages}">
	<c:choose>
		<c:when test="${selected%links ==0}">
			<input type="hidden" id="paginationStart"
				value="${selected -(links-1)}">
			<input type="hidden" id="paginationEnd" value="${selected}">
		</c:when>
		<c:otherwise>
			<input type="hidden" id="paginationStart" value="${selected}">
			<c:choose>
				<c:when test="${pages<links}">
					<input type="hidden" id="paginationEnd" value="${pages}">
				</c:when>
				<c:otherwise>
					<input type="hidden" id="paginationEnd"
						value="${selected + (links-1)}">
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
	<c:set var="directiveClass" value="projects/page/" />
	<ul class="pagination">
		<li><a href="${directiveClass}" class="directives" id="previous"><img
				id="logo" src="resources/images/previous_page.png"></a></li>
		<c:forEach begin="1" end="${pages}" varStatus="loop">
			<c:choose>
				<c:when test="${selected==loop.index}">
					<c:set var="className" value="paging selected" />
				</c:when>
				<c:otherwise>
					<c:set var="className" value="paging" />
				</c:otherwise>
			</c:choose>
			<li><a class="${className}" id="${loop.index}"
				href="projects/page/${loop.index}">${loop.index}</a></li>
		</c:forEach>
		<li><a href="${directiveClass}" class="directives" id="next"><img
				id="logo" src="resources/images/nextpage_icon.png"></a></li>
	</ul>
</div>
<script>
	$("#projectList .firstInput").focus();
	$("#projectList #searchDatas").tablesorter({
		selectorHeaders : '.sorter-true'
	});

	var maxPaginationLinks = 2;
	function handlePagination(id) {
		var links = $("#projectList .pagination .paging");
		id = parseInt(id);
		var i = 1;
		var start, end = 0;
		if (id % maxPaginationLinks == 0) {
			start = id - maxPaginationLinks + 1;
			end = id;
		} else {
			start = id;
			end = id + maxPaginationLinks - 1;
		}
		links.each(function() {
			var link = $(this);
			if (i >= start && i <= end) {
				link.show();
			} else {
				link.hide();
			}

			i = i + 1;
		});
	};
	handlePagination("2");
</script>