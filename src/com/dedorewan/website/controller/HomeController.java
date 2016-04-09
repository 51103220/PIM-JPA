package com.dedorewan.website.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dedorewan.website.dom.Project;
import com.dedorewan.website.dom.Project.STATUS;
import com.dedorewan.website.domain.JsonResponse;
import com.dedorewan.website.service.IProjectService;

@Controller
public class HomeController {
	@Value("${application.errors.default}")
	private String default_errors;
	@Autowired
	JsonResponse jsonResponse;
	@Autowired
	private IProjectService projectService;

	@Value("${projects.maxProjectPerPage}")
	Integer projectsPerPage;

	private static final int FIRST_PAGE = 1;
	private static final int DEFAULT_SELECTED = 1;

	private ModelAndView makeProjectModel(String view,
			List<Project> projectList, Integer page, Integer selectedPage,
			Boolean isSearchResult) {
		ModelAndView model = new ModelAndView(view);
		model.addObject("projects",
				projectService.projectsInPage(projectList, page));
		model.addObject("pages",
				projectService.numberPages(projectList, projectsPerPage));
		model.addObject("isSearchResult", isSearchResult);
		model.addObject("selected", selectedPage);
		return model;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ModelAndView index(Locale locale, HttpServletRequest request) {
		List<Project> projects;
		if (request.getSession().getAttribute("projectList") != null) {
			projects = (List<Project>) request.getSession().getAttribute(
					"projectList");
		} else {
			projects = projectService.findAll();
			request.getSession().setAttribute("projectList", projects);
		}
		ModelAndView model = makeProjectModel("index", projects, FIRST_PAGE,
				DEFAULT_SELECTED, false);
		return model;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/errorsunexpected={message:.+}")
	public ModelAndView errorPage(@PathVariable String message) {
		ModelAndView model = new ModelAndView("errors");
		model.addObject("message", message);
		return model;
	}
	@RequestMapping(method = RequestMethod.POST, value = "/deleteMultiple")
	@ResponseBody
	public JsonResponse deleteProjects(@RequestBody List<Project> projects,HttpServletRequest request) throws Exception {
		projectService.deleteProjects(projects);
		request.getSession().setAttribute("projectList", projectService.findAll());
		jsonResponse.setStatus("SUCCESS");
		return jsonResponse;
	}

	@ModelAttribute("statusValues")
	private STATUS[] statusList() {
		STATUS[] status = STATUS.values();
		return status;
	}

}
