package za.co.tangent.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import za.co.tangent.service.CodeQualityService;

@RestController
public class CodeQualityController {
	
	@Autowired
	CodeQualityService service;
	
	@RequestMapping(value = "/", method = RequestMethod.POST, headers = "Accept=application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Map<String,String>> createNewCodeSmell(@RequestBody String payload, HttpServletRequest request){
		return service.parseRequest(payload, request);
	}
	
	/*@RequestMapping("/files")
	@ResponseBody
	public Iterable<File> files(){
		return fileDao.findAll();
	}
	
	@RequestMapping("/projects")
	@ResponseBody
	public Iterable<Project> projects(){
		return projectDao.findAll();
	}*/
}
