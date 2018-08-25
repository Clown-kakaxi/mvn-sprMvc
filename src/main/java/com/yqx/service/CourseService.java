package com.yqx.service;

import org.springframework.stereotype.Service;

import com.yqx.model.Course;

@Service
public interface CourseService {

	Course getCoursebyId(Integer courseId);
	
}
