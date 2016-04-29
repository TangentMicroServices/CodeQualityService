package za.co.tangent.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import za.co.tangent.CodeQualityApplication;
import za.co.tangent.domain.File;
import za.co.tangent.service.CodeQualityService;
import za.co.tangent.test.Utilities;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CodeQualityApplication.class)
@WebAppConfiguration
@ContextConfiguration(classes = MockServletContext.class)
public class CodeQualityControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private CodeQualityService codeQualityService;
	
	@InjectMocks
	CodeQualityController controller;

    @Before
    public void setup() {
    	MockitoAnnotations.initMocks(this);
    	this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
    
	@Test
	public void badRequest() throws Exception {
		this.mockMvc.perform(post("/")).andExpect(status().isBadRequest());
    }
	
	@Test
	public void successfullRequest() throws Exception {

		String jsonString = Utilities.getFileJSON();
	
		this.mockMvc.perform(post("/")
				.header("HEADER_buildid", "1")
				.header("HEADER_project", "Some test project")
				.header("HEADER_git_branch", "master")
				.header("Content-Type", MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
    }
}
