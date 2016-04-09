package com.dedorewan.website.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
		List<String> visas = new ArrayList<String>();
		for (String visa : project.getMembers()) {
			if (!projectService.visaExsisted(visa)) {
				visas.add(visa);
			}
		}
		String visaString = StringUtils.collectionToCommaDelimitedString(visas);
		if (project.getProjectNumber() == null) {
			errors.rejectValue("projectNumber", "NotNull", "Project Number must not be left empty");
		} else if (project.getProjectNumber() <= 0) {
			message = messageSource.getMessage("errors.projectNumberLessThanZero", new Object[] {}, locale);
			errors.rejectValue("projectNumber", "NotLessThan", message);
		} else if (project.getProjectNumber() >= 10000) {
			message = messageSource.getMessage("errors.projectNumberGreateThan", new Object[] {}, locale);
			errors.rejectValue("projectNumber", "GreaterThan", message);
		}
		if (project.getName().length() == 0) {
			errors.rejectValue("name", "NotEmpty", "Project Name must not be left empty");
		}
		if (project.getName().length() > 50) {
			message = messageSource.getMessage("errors.projectNumber50", new Object[] {}, locale);
			errors.rejectValue("name", "InvalidLength", message);
		}
		if (project.getCustomer().length() > 50) {
			message = messageSource.getMessage("errors.customer50", new Object[] {}, locale);
			errors.rejectValue("customer", "InvalidLength", message);
		}
		if (project.getCustomer().length() == 0) {
			errors.rejectValue("customer", "NotEmpty", "Customer Name must not be left empty");
		}
		if (project.getGroupId() == null) {
			errors.rejectValue("groupId", "NotNull", "Must choose a group");
		}
		if (project.getStartDate() == null) {
			errors.rejectValue("startDate", "NotNull", "Must choose a startingDate");
		}
		if (project.getEndDate() != null && project.getStartDate() != null
				&& project.getEndDate().before(project.getStartDate())) {
			message = messageSource.getMessage("errors.startDateEndDate", new Object[] {}, locale);
			errors.rejectValue("endDate", "DateNotValid", message);
		}
		if (projectService.projectNumberExisted(project.getId(), project.getProjectNumber())) {
			message = messageSource.getMessage("errors.projectNumberExisted", new Object[] {}, locale);
			errors.rejectValue("projectNumber", "projectNumberExisted", message);
		}
		if (visaString != null && !visaString.equals("")) {
			message = messageSource.getMessage("errors.InvalidVisa", new Object[] {}, locale);
			errors.rejectValue("members", "InvalidVisa", message + visaString + "}");
		}

	}
}
