package za.co.tangent.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import za.co.tangent.dao.FileDao;
import za.co.tangent.dao.ProjectDao;
import za.co.tangent.domain.File;
import za.co.tangent.domain.Project;

@Service
@Configuration
public class CodeQualityService {
	
	@Autowired
	ProjectDao projectDao;
	@Autowired
	FileDao fileDao;
	@Autowired
	MongoDBCounterService mongoDbService;

	public ResponseEntity<Map<String,String>> parseRequest(String payload, HttpServletRequest request) {
		int buildNumber = Integer.parseInt(request.getHeader("HEADER_buildid"));
		String projectName = request.getHeader("HEADER_project");
		String branchName = request.getHeader("HEADER_git_branch");
		return handleRequest(buildNumber, projectName, branchName, payload);
	}
	
	private ResponseEntity<Map<String,String>> handleRequest (int buildNumber, String projectName, String branchName, String payload) {
		Project project = projectDao.findByName(projectName);
		File file = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String,String> result = new HashMap<String,String>();
		if (project == null)
		{
			project = insertProject(buildNumber, projectName, branchName);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			file = mapper.readValue(payload, File.class);
			file.setProject(project);
			int nextSequence = mongoDbService.getNextSequence("file");
			if (nextSequence > 0){
				file.setId(nextSequence);
				fileDao.save(file);
				result.put("Response", "Ok");
				return new ResponseEntity<Map<String,String>>(result, headers, HttpStatus.CREATED);
			}
			result.put("Response", "NOk");
			return new ResponseEntity<Map<String,String>>(result, headers, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		result.put("Response", "NOk");
		return new ResponseEntity<Map<String,String>>(result, headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private Project insertProject(int buildNumber, String projectName, String branchName) {
		Project project = new Project();
		int nextSequence = mongoDbService.getNextSequence("project");
		if (nextSequence > 0){
			project.setId(nextSequence);
			project.setName(projectName);
			project.setBranch(branchName);
			project.setBuildNumber(buildNumber);
			return projectDao.save(project);
		}
		return project;
	}
}







