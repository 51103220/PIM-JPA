package com.dedorewan.website.exception;

public class ProjectNumberAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public ProjectNumberAlreadyExistsException(Integer project_number) {
		super(
				"The project number already existed: " + project_number + ".Please select a different project number");
	}

}
