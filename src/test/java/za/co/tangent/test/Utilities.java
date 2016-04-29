package za.co.tangent.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.mock.web.MockHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import za.co.tangent.domain.File;
import za.co.tangent.domain.Project;

public class Utilities {
	public static File getFile(){
		Map<String, String> content = new HashMap<String, String>();
		content.put("body", "##Cyclomatic Complexity\nCyclomatic Complexity corresponds to the number of decisions a block of code contains plus 1. This number (also called McCabe number) is equal to the number of linearly independent paths through the code. This number can be used as a guide when testing conditional logic in blocks.\n\nRadon analyzes the AST tree of a Python program to compute Cyclomatic Complexity. Statements have the following effects on Cyclomatic Complexity:\n\n\n| Construct | Effect on CC | Reasoning |\n| --------- | ------------ | --------- |\n| if | +1 | An *if* statement is a single decision. |\n| elif| +1| The *elif* statement adds another decision. |\n| else| +0| The *else* statement does not cause a new decision. The decision is at the *if*. |\n| for| +1| There is a decision at the start of the loop. |\n| while| +1| There is a decision at the *while* statement. |\n| except| +1| Each *except* branch adds a new conditional path of execution. |\n| finally| +0| The finally block is unconditionally executed. |\n| with| +1| The *with* statement roughly corresponds to a try/except block (see PEP 343 for details). |\n| assert| +1| The *assert* statement internally roughly equals a conditional statement. |\n| Comprehension| +1| A list/set/dict comprehension of generator expression is equivalent to a for loop. |\n| Boolean Operator| +1| Every boolean operator (and, or) adds a decision point. |\n\nSource: http://radon.readthedocs.org/en/latest/intro.html");
		
		ArrayList<String> categories = new ArrayList<String>();
		categories.add("Complexity");
		
		Map<String, String> path = new HashMap<String, String>();
		path.put("path", "siebel/siebel_py");
		
		Map<String, Integer> begin = new HashMap<String, Integer>();
		begin.put("begin", 74);
		
		Map<String, Integer> end = new HashMap<String, Integer>();
		end.put("end", 132);
		
		Map<Map<String, Integer>, Map<String, Integer>> errorLines = new HashMap<Map<String, Integer>, Map<String, Integer>>();
		errorLines.put(begin, end);
		
		Map<String, Map<Map<String, Integer>, Map<String, Integer>>> lines = new HashMap<String, Map<Map<String, Integer>, Map<String, Integer>>>();
		lines.put("lines", errorLines);
		
		Map<Map<String, String>, Map<String, Map<Map<String, Integer>, Map<String, Integer>>>> location = new HashMap<Map<String, String>, Map<String, Map<Map<String, Integer>, Map<String, Integer>>>>();
		location.put(path, lines);

		File file = new File();
		file.setContent(content);
		file.setCategories(categories);
		file.setLocation(location);
		file.setType("issue");
		file.setRemediation_points("1100000");
		file.setCheck_name("Complexity");
		file.setDescription("Cyclomatic complexity is too high in method __runSoapRequest. (6)");
		file.setFingerprint("e9ef52570a598f72a57035f600c2a2ed");
		file.setEngine_name("radon");
		
		Project project = new Project();
		project.setBranch("master");
		project.setBuildNumber(1);
		project.setName("Some test project");
		
		file.setProject(project);
		
		return file;
	}
	
	public static String getFileJSON() throws JsonProcessingException{
		File file = getFile();
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(file);
	}
	
	public static MockHttpServletRequest getMockHttpServletRequest(){
		MockHttpServletRequest httpRequest = new MockHttpServletRequest();
		httpRequest.setRequestURI("/");
		httpRequest.setMethod("POST");
		httpRequest.addHeader("HEADER_buildid", "1");
		httpRequest.addHeader("HEADER_project", "TestProject");
		httpRequest.addHeader("HEADER_git_branch", "master");
		return httpRequest;
	}
}
