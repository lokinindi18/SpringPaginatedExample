package com.in28minutes.springboot.controller;

import java.net.URI;
import java.util.List;

import javax.jws.soap.SOAPBinding;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.springboot.model.Course;
import com.in28minutes.springboot.service.StudentService;

@RestController
public class StudentController {
	
	@Value("${spring.datasource.username}")
	private String databaseUserName;
	
	@Value("${spring.datasource.password}")
	private String databasePassword;
	
	@Value("${spring.datasource.url}")
	private String databaseUrl;
	
	
	@Autowired
	private StudentService studentService;

	@GetMapping("/students/{studentId}/courses")
	public List<Course> retrieveCoursesForStudent(@PathVariable String studentId) {
		System.out.println("----> databaseUserName: " + databaseUserName + " , databasePassword: " + databasePassword + " , databaseUrl: " + databaseUrl);
		return studentService.retrieveCourses(studentId);
	}
	
	@GetMapping("/students/{studentId}/courses/{courseId}")
	public Course retrieveDetailsForCourse(@PathVariable String studentId,
			@PathVariable String courseId) {
		return studentService.retrieveCourse(studentId, courseId);
	}
	
	@PostMapping("/students/{studentId}/courses")
	public ResponseEntity<Void> registerStudentForCourse(
			@PathVariable String studentId, @RequestBody Course newCourse) {

		Course course = studentService.addCourse(studentId, newCourse);

		if (course == null)
			return ResponseEntity.noContent().build();

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
				"/{id}").buildAndExpand(course.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

}