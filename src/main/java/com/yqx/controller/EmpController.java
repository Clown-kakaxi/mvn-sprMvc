package com.yqx.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yqx.model.Emp;
import com.yqx.service.EmpService;
import com.yqx.service.LiveService;
import com.yqx.util.ProjConstants;

/**
 * 员工controller  员工的 增删改查功能
 * @author yqx
 *
 */
@Controller
@RequestMapping("/emp")
public class EmpController{

	private Logger log = LoggerFactory.getLogger(CourseController.class);
	
	@Autowired	
	private EmpService empService;

	@RequestMapping(value="/{empId}", method=RequestMethod.GET)
	public @ResponseBody Emp getCourseByJson(@PathVariable Integer empId){
		Emp emp = empService.getEmpbyId(empId);
		return emp;
		
	}
	
	@RequestMapping(value="/jsontype/{empId}",method=RequestMethod.POST)
	public ResponseEntity<Emp> getCourseByJson1(@PathVariable Integer empId){
		Emp emp = empService.getEmpbyId(empId);
		return new ResponseEntity<Emp>(emp, HttpStatus.OK);
	}
	
	@RequestMapping(value="/jsontype2/",method=RequestMethod.POST)
	public @ResponseBody List<Emp> getCourseByJson2(@PathVariable Integer empId){
		List<Emp> list = new ArrayList<Emp>();
		Emp emp = empService.getEmpbyId(empId);
		list.add(emp);
		list.add(emp);
		list.add(emp);
		list.add(emp);
		list.add(emp);
		return list;
	}
	
	@RequestMapping(value="/jsontype3/", method=RequestMethod.POST)
	public @ResponseBody List<Emp> getCourseByJson333(){
		List<Emp> list = new ArrayList<Emp>();
		list= empService.findEmpList();
		return list;
		
	}
	
	@RequestMapping(value="/jsontype4/")
	public @ResponseBody Map<String, Object> getCourseByJson4(String aaa){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Emp> list = new ArrayList<Emp>();
		list= empService.findEmpList();
		map.put("data", list);
		return map;
		
	}
	
	@RequestMapping(value="/addEmp/", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addEmp(String name, String salary,String age){
		
		log.debug("empName:"+name);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", name);
		return map;
	}
	
	@RequestMapping(value="/addEmp2/", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addEmp2(@RequestBody(required=true) Map<String, Object> reqMap){
		String name = String.valueOf(reqMap.get("name"));
		log.debug("empName:"+name);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", name);
		return map;
	}
	
	@RequestMapping(value="/addEmp3/", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addEmp3(Emp emp){
		log.debug("addEmp3 Params[emp]:"+ReflectionToStringBuilder.toString(emp));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", ProjConstants.SUCCESS_FLAG);
		boolean flag = empService.saveOrUpdateEmp(emp);
		map.put("data", emp);
		if(flag){
			map.put("status", ProjConstants.SUCCESS_FLAG);
		}
		return map;
	}
	
}
