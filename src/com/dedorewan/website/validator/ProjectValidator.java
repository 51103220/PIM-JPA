package com.dedorewan.website.validator;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dedorewan.website.dom.Project;
import com.dedorewan.website.service.IProjectService;

@Component
public class ProjectValidator implements Validator {
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IProjectService projectService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Project.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Locale locale = LocaleContextHolder.getLocale();
		String message = "";
		Project project = (Project) target;
		StringBuilder visas = new StringBuilder("");
		boolean first = true;
		for (String visa : project.getMembers()) {
			if (!projectService.visaExsisted(visa)) {
				if (first) {
					visas.append(visa);
					first = false;
				} else {
					visas.append("," + visa);
				}
			}
		}
		if (project.getProjectNumber() == null) {
			errors.rejectValue("projectNumber", "NotNull",
					"Project Number must not be left empty");
		} else if (project.getProjectNumber() <= 0) {
			errors.rejectValue("projectNumber", "NotLessThan",
					"Project Number must be greater than 0");
		} else if (project.getProjectNumber() >= 10000) {
			errors.rejectValue("projectNumber", "GreaterThan",
					"Project Number must be 4 digits");
		}
		if (project.getName().length() == 0) {
			errors.rejectValue("name", "NotEmpty",
					"Project Name must not be left empty");
		}
		if (project.getName().length() > 50) {
			errors.rejectValue("name", "InvalidLength",
					"Project Name must be less than 50 characters");
		}
		if (project.getCustomer().length() > 50) {
			errors.rejectValue("customer", "InvalidLength",
					"Customer Name must be less than 50 characters");
		}
		if (project.getCustomer().length() == 0) {
			errors.rejectValue("customer", "NotEmpty",
					"Customer Name must not be left empty");
		}
		if (project.getGroupId() == null) {
			errors.rejectValue("groupId", "NotNull", "Must choose a group");
		}
		if (project.getStartDate() == null) {
			errors.rejectValue("startDate", "NotNull",
					"Must choose a startingDate");
		}
		if (project.getEndDate() != null && project.getStartDate() != null
				&& project.getEndDate().before(project.getStartDate())) {
			errors.rejectValue("endDate", "DateNotValid",
					"End Date must be after Start Date");
		}
		if (projectService.projectNumberExisted(project.getId(),project.getProjectNumber())) {
			message = messageSource.getMessage("errors.projectNumberExisted",
					new Object[] {}, locale);
			errors.rejectValue("projectNumber", "projectNumberExisted", message);
		}
		if (!visas.toString().equals("")) {
			message = messageSource.getMessage("errors.InvalidVisa",
					new Object[] {}, locale);
			errors.rejectValue("members", "InvalidVisa",
					message + visas.toString() + "}");
		}

	}
}
