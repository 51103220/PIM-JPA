package com.dedorewan.website.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dedorewan.website.dom.Employee;
import com.dedorewan.website.dom.Project;
import com.dedorewan.website.dom.Project.STATUS;
import com.dedorewan.website.domain.JsonResponse;
import com.dedorewan.website.service.IEmployeeService;
import com.dedorewan.website.service.IGroupService;
import com.dedorewan.website.service.IProjectService;
import com.dedorewan.website.validator.ProjectValidator;

@Controller
public class ProjectController {
	@Value("${projects.maxProjectPerPage}")
	Integer projectsPerPage;

	@Autowired
	private ProjectValidator projectValidator;

	@Autowired
	private IProjectService projectService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	JsonResponse jsonResponse;
	private static final int FIRST_PAGE = 1;
	private static final int DEFAULT_SELECTED = 1;
	
	/**
	 * Bind Project Validation
	 */
	@InitBinder
	public void dataBinding(WebDataBinder binder) {
		binder.addValidators(projectValidator);
	}
	/**
	 * Return modeAndView of Project List page with pagination
	 */
	private ModelAndView makeProjectModel(String view,
			List<Project> projectList, Integer page, Integer selectedPage) {
		ModelAndView model = new ModelAndView(view);
		model.addObject("projects",
				projectService.projectsInPage(projectList, page));
		model.addObject("pages",
				projectService.numberPages(projectList, projectsPerPage));
		model.addObject("selected", selectedPage);
		return model;
	}
	/**
	 * Return modeAndView of Project List page
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/listProject")
	@ResponseBody
	public ModelAndView listProjectPage(HttpServletRequest request) {
		List<Project> projects = (List<Project>) request.getSession()
				.getAttribute("projectList");
		ModelAndView model = makeProjectModel("forms/projectList", projects,
				FIRST_PAGE, DEFAULT_SELECTED);
		return model;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "projects/page/{page}")
	@ResponseBody
	public ModelAndView projectsPage(@PathVariable Integer page,
			HttpServletRequest request) {
		List<Project> projects = (List<Project>) request.getSession()
				.getAttribute("projectList");
		ModelAndView model = makeProjectModel("forms/projectList", projects,
				page, page);
		return model;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/newProject")
	@ResponseBody
	public ModelAndView newProjectPage() {
		ModelAndView model = new ModelAndView("forms/newProject");
		model.addObject("formName", "New");
		model.addObject("groups", groupService.groupLeaders());
		return model;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/NewProject")
	@ResponseBody
	public JsonResponse newProject(@Validated @RequestBody Project project,
			BindingResult result, HttpServletRequest request) throws Exception {
		if (result.hasErrors()) {
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(result.getFieldErrors());

		} else {
			projectService.addProject(project);
			request.getSession().setAttribute("searchValue", "");
			request.getSession().setAttribute("statusKey", null);
			request.getSession().setAttribute("projectList", projectService.findAll());
			jsonResponse.setStatus("SUCCESS");
		}
		return jsonResponse;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/project/{id}/detail")
	@ResponseBody
	public ModelAndView getProject(@PathVariable Long id) throws Exception {
		Project project = projectService.getProject(id);
		ModelAndView model = new ModelAndView("forms/newProject");
		String leaderVisa = projectService.groupLeaderVisa(project);
		model.addObject("formName", "Edit");
		model.addObject("project", project);
		model.addObject("leaderVisa", leaderVisa);
		model.addObject("groups", groupService.groupLeaders());
		return model;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/resetCriteria")
	@ResponseBody
	public ModelAndView resetCriteria(HttpServletRequest request) {
		List<Project> projects = projectService.findAll();
		ModelAndView model = makeProjectModel("forms/projectList", projects,
				FIRST_PAGE, DEFAULT_SELECTED);
		request.getSession().setAttribute("searchValue", "");
		request.getSession().setAttribute("statusKey", null);
		request.getSession().setAttribute("projectList", projects);
		return model;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/filterProject")
	@ResponseBody
	public ModelAndView filterProjects(
			@RequestParam(value = "keywords") String keywords,
			@RequestParam(value = "statusKey") STATUS statusKey,
			HttpServletRequest request) {

		if (statusKey == null && keywords == "") {
			return resetCriteria(request);
		} else {
			List<Project> projects = projectService.filterProjects(keywords,
					statusKey);
			request.getSession().setAttribute("searchValue", keywords);
			request.getSession().setAttribute("statusKey", statusKey);
			request.getSession().setAttribute("projectList", projects);
			ModelAndView model = makeProjectModel("forms/projectList",
					projects, FIRST_PAGE, DEFAULT_SELECTED);
			if (projects.size() == 0) {
				model.addObject("searchResult", "No Results Found");
			}
			return model;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/project/{id}/delete")
	@ResponseBody
	public String deleteProject(@PathVariable Long id) throws Exception {
		projectService.deleteProject(id);
		return "success";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteMultiple")
	@ResponseBody
	public String deleteProjects(@RequestParam(value = "ids[]") Long[] ids)
			throws Exception {
		projectService.deleteProjects(ids);
		return "success";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getVisas")
	@ResponseBody
	public List<Employee> getVisas() {
		return employeeService.availableEmployee();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/EditProject")
	@ResponseBody
	public JsonResponse editProject(@Validated @RequestBody Project project,
			BindingResult result,HttpServletRequest request) throws Exception {

		if (result.hasErrors()) {
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(result.getFieldErrors());
		} else {
			projectService.updateProject(project);
			request.getSession().setAttribute("searchValue", "");
			request.getSession().setAttribute("statusKey", null);
			request.getSession().setAttribute("projectList", projectService.findAll());
			jsonResponse.setStatus("SUCCESS");
		}
		return jsonResponse;
	}

	@ModelAttribute("statusValues")
	private STATUS[] statusList() {
		STATUS[] status = STATUS.values();
		return status;
	}
}
