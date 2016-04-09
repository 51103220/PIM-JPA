<%@ page language="java" contentType="text/html; charset=charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="newProject">
	<p class="formName">
		<spring:message code="menu.${formName}Project" />
	</p>
	<div class="errorPanel">
		<p class="panelMessage">
			<spring:message code="errors.mandatory" />
		</p>
		<a href="#" class="closePanel"> <span
			class="glyphicon glyphicon-remove"></span>
		</a>
	</div>
	<div class="formContent general-content">
		<form class="form-horizontal general-form" role="form"
			action="${formName}Project" method="POST">
			<div class="form-group">
				<label class="control-label col-sm-3" for="pNumber"><spring:message
						code="form.number" /><span class="isRequired">*</span> </label>
				<div class="col-sm-9">
					<input type="hidden" name="id" class="form-control shortWidth"
						value="${project.getId()}">
						<input type="hidden" name="version" class="form-control shortWidth"
						value="${project.getVersion()}"> 
						<input type="number"
						class="form-control shortWidth firstInput" name="projectNumber"
						id="pNumber" value="${project.getProjectNumber()}" autofocus>
					<p class="hiddenError"></p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-3" for="pName"><spring:message
						code="form.name" /><span class="isRequired">*</span> </label>
				<div class="col-sm-9">
					<input type="text" class="form-control longWidth " name="name"
						id="pName" value="${project.getName()}">
					<p class="hiddenError"></p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-3" for="customer"><spring:message
						code="form.customer" /> <span class="isRequired">*</span> </label>
				<div class="col-sm-9">
					<input type="text" class="form-control longWidth" name="customer"
						id="customer" value="${project.getCustomer()}">
					<p class="hiddenError"></p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-3" for="group"><spring:message
						code="form.group" /><span class="isRequired">*</span></label>
				<div class="col-sm-9">
					<select class="form-control shortWidth empty" id="group"
						name="groupId">
						<c:choose>
							<c:when test="${formName=='New'}">
								<c:forEach items="${groups}" var="group">
									<option value="${group.key}">${group.value}</option>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<option value="${project.getGroupId()}" selected>${leaderVisa}</option>
								<c:forEach items="${groups}" var="group">
									<c:choose>
										<c:when test="${group.key != project.getGroupId()}">
											<option value="${group.key}">${group.value}</option>
										</c:when>
									</c:choose>
								</c:forEach>
							</c:otherwise>
						</c:choose>

					</select>
					<p class="hiddenError"></p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-3" for="members"><spring:message
						code="form.members" /></label>
				<div class="col-sm-9">
					<div class=tagsDiv>
						<ul class="tags list-inline">
							<li class="tagInput"><input type="text" name="members"
								class="" id="members" value="${project.membersToString()}"
								placeholder="">
								<p class="hiddenError"></p></li>
						</ul>
						<ul class="visaList">
						</ul>
					</div>
				</div>

			</div>
			<div class="form-group">
				<label class="control-label col-sm-3" for="status"><spring:message
						code="form.status" /><span class="isRequired">*</span></label>
				<div class="col-sm-9">
					<select class="form-control shortWidth empty" id="status"
						name="status">
						<c:choose>
							<c:when test="${formName=='New'}">
								<c:forEach items="${statusValues}" var="status">
									<option value="${status}"><spring:message
											code="status.${status}" /></option>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<option value="${project.getStatus()}" selected><spring:message
										code="status.${project.getStatus()}" /></option>
								<c:forEach items="${statusValues}" var="status">
									<c:choose>
										<c:when test="${status != project.getStatus()}">
											<option value="${status}"><spring:message
													code="status.${status}" /></option>
										</c:when>
									</c:choose>
								</c:forEach>
							</c:otherwise>
						</c:choose>

					</select>
					<p class="hiddenError"></p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-3" for="startDate"><spring:message
						code="form.startdate" /><span class="isRequired">*</span> </label>
				<fmt:formatDate value="${project.getStartDate()}"
					var="startDateString" pattern="dd-MM-yyyy" />
				<div class="col-sm-4">
					<div class='input-group date shortWidth'>
						<input type='text' class="form-control datePicker" id="startDate"
							name="startDate" value="${startDateString}" /> <span
							class="input-group-addon datePickerIcon"> <span
							class="glyphicon glyphicon-calendar"></span>
						</span>
						<p class="hiddenError"></p>
					</div>
				</div>
				<label class="control-label col-sm-2" for="endDate"><spring:message
						code="form.enddate" /> </label>
				<fmt:formatDate value="${project.getEndDate()}" var="endDateString"
					pattern="dd-MM-yyyy" />
				<div class="col-sm-3 ">
					<div class='input-group date expand'>
						<input type='text' class="form-control datePicker" id="endDate"
							name="endDate" value="${endDateString}" /> <span
							class="input-group-addon datePickerIcon"> <span
							class="glyphicon glyphicon-calendar"></span>
						</span>
						<p class="hiddenError"></p>
					</div>

				</div>
			</div>
		</form>
		<div class="form-group btnGroup">
			<a tabIndex='-1' href="<c:url value='/'/>"><button type="submit"
					class="btn btn-default cancelBtn">
					<spring:message code="button.cancel" />
				</button></a>
			<button type="submit" class="btn btn-primary processBtn">
				<spring:message code="form.${formName}Project" />
			</button>

		</div>
	</div>
</div>
<script>
$("#newProject .firstInput").focus();
</script>