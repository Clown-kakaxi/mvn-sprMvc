package com.yqx.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yqx.model.Course;
import com.yqx.service.CourseService;

@Controller
@RequestMapping("/courses")
public class CourseController{

	private Logger log = LoggerFactory.getLogger(CourseController.class);
			
	private CourseService courseService;
	
	@Autowired
	public void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}


	//传统方式url  调用Model 来传递属性值
	//本方法将处理 /courses/view?courseId=123   的请求
	@RequestMapping(value="/view",method=RequestMethod.GET)
	public String viewCourse(@RequestParam("courseId") Integer courseId, Model model){
		log.debug("In viewCourse,courseId={}", courseId);
		Course course = courseService.getCoursebyId(courseId);
		model.addAttribute(course);
		return "course_overview";
	}
	
	//restful方式url  调用Map<String, Object> 来传递属性值
	//本方法将处理 /courses/view/666   的请求
	@RequestMapping(value="/view2/{courseId}", method=RequestMethod.GET)
	public String viewCourse2(@PathVariable("courseId")Integer courseId, Map<String, Object> map){
		log.debug("In viewCourse2{courseId}", courseId);
		Course course = courseService.getCoursebyId(courseId);
		map.put("course", course);
		return "course_overview";
	}
	
	//HttpServletRequest 方式url       调用request 来传递属性值
	//本方法将处理 /courses/view?courseId=777   的请求
	@RequestMapping(value="/view3")
	public String viewCourse3(HttpServletRequest request){
		Integer courseId = Integer.parseInt(String.valueOf(request.getParameter("courseId")));
		log.debug("In viewCourse3,courseId={}", courseId);
		Course course = courseService.getCoursebyId(courseId);
		request.setAttribute("course", course);
		return "course_overview";
	}
	
	@RequestMapping(value="/admin", method=RequestMethod.GET, params="add")
	public String createCourse(){
		log.debug("createCourse method");
		return "course_admin/edit";
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String doSave(@ModelAttribute Course course){
		log.debug("Info of Course from   form!");
		log.debug(ReflectionToStringBuilder.toString(course));
		//数据库持久化
		course.setCourseId(777);
		//使用重定向方式
		return "redirect:view2/"+course.getCourseId();
	}
	
	@RequestMapping(value="/showUpload", method=RequestMethod.GET)
	public String showUploadPage(){
		log.debug("Info of showUploadPage ");
		return "course_admin/file";
	}
	
	@RequestMapping(value="/doUpload", method=RequestMethod.POST)
	public String doUpload(@RequestParam("file") MultipartFile file) throws IOException{
		
		if(!file.isEmpty()){
			//通过流的方式 将源文件拷贝到 目标目录下
			log.debug("Info of doUpload method,file:" + file.getOriginalFilename());
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File("F:\\fileDir\\upload", System.currentTimeMillis()+file.getOriginalFilename()));
		}
		
		return "success";
	}
	
	@RequestMapping(value="/{courseId}", method=RequestMethod.GET)
	public @ResponseBody Course getCourseByJson(@PathVariable Integer courseId){
		Course course = courseService.getCoursebyId(courseId);
		return course;
		
	}
	
	@RequestMapping(value="/jsontype/{courseId}",method=RequestMethod.GET)
	public ResponseEntity<Course> getCourseByJson2(@PathVariable Integer courseId){
		Course course = courseService.getCoursebyId(courseId);
		return new ResponseEntity<Course>(course, HttpStatus.OK);
	}
	
}
