package za.co.tangent.service;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
import de.flapdoodle.embed.mongo.config.ExtractedArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.extract.UserTempNaming;
import de.flapdoodle.embed.process.runtime.Network;
import za.co.tangent.CodeQualityApplication;
import za.co.tangent.dao.FileDao;
import za.co.tangent.dao.ProjectDao;
import za.co.tangent.domain.File;
import za.co.tangent.domain.Project;
import za.co.tangent.test.Utilities;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CodeQualityApplication.class)
public class CodeQualityServiceTest {
	
	@Autowired
	FileDao fileDao;
	@Autowired
	ProjectDao projectDao;
	@Autowired
	MongoDBCounterService mongoDbService;
	@Autowired
	CodeQualityService service;
	
	private MockHttpServletRequest httpRequest;
	private String jsonString;
    private static MongodExecutable mongodExecutable;
    private static MongodProcess mongod;
    private static MongoClient mongo;
    
    @BeforeClass
    public static void createDB() throws Exception {
   	
    	int port = 9876;

        Command command = Command.MongoD;

        IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
            .defaults(command)
            .artifactStore(new ExtractedArtifactStoreBuilder()
                .defaults(command)
                .download(new DownloadConfigBuilder()
                        .defaultsForCommand(command).build())
                .executableNaming(new UserTempNaming()))
            .build();

        IMongodConfig mongodConfig = new MongodConfigBuilder()
            .version(Version.Main.PRODUCTION)
            .net(new Net(port, Network.localhostIsIPv6()))
            .build();

        MongodStarter runtime = MongodStarter.getInstance(runtimeConfig);

        mongodExecutable = runtime.prepare(mongodConfig);
        mongod = mongodExecutable.start();

        mongo = new MongoClient("localhost", port);
        DB db = mongo.getDB("testdb");
    }
    
    @AfterClass
    public static void tearDown() throws Exception {
    	mongo.close();
    	mongod.stop();
    	mongodExecutable.stop();
    }
    
	@Before
    public void setUp() throws Exception {
		
		jsonString = Utilities.getFileJSON();
		httpRequest = Utilities.getMockHttpServletRequest();

		this.service = new CodeQualityService();
		this.mongoDbService = new MongoDBCounterService();
		
		//This is lame...
		this.service.fileDao = fileDao;
		this.service.projectDao = projectDao;
		this.service.mongoDbService = mongoDbService;
    }
	
	@Test
	public void checkSavingOfProjectAndFile(){
		//Check if the project is saved (as it doesn't exist in db yet) and file is saved
		List<File> initialFileData = fileDao.findAll();
		Assert.assertTrue(initialFileData.isEmpty());
		
		List<Project> initialProjectData = projectDao.findAll();
		Assert.assertTrue(initialProjectData.isEmpty());
		
		this.service.parseRequest(jsonString, httpRequest);
		
		//Did the file save
		List<File> afterFileSave = fileDao.findAll();
		Assert.assertFalse(afterFileSave.isEmpty());
		
		//Did the project save
		List<Project> afteProjectSave = projectDao.findAll();
		Assert.assertFalse(afteProjectSave.isEmpty());
	}
	
	@Test
	public void checkSavingOfFileOnly(){
		//Ensure project is not saved (as it already exists in db) and file is saved
		List<File> initialFileData = fileDao.findAll();
		Assert.assertTrue(initialFileData.size() == 1);
		
		List<Project> initialProjectData = projectDao.findAll();
		Assert.assertTrue(initialProjectData.size() == 1);
		
		this.service.parseRequest(jsonString, httpRequest);
		
		//Did the file save
		List<File> afterFileSave = fileDao.findAll();
		Assert.assertTrue(afterFileSave.size() == 2);
		
		//The project should not have saved
		List<Project> afteProjectSave = projectDao.findAll();
		Assert.assertTrue(afteProjectSave.size() == 1);
	}
}
