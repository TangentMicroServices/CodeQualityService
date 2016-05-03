package za.co.tangent;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CodeQualityApplication.class)
public class CodeQualityApplicationTests implements ApplicationContextAware {

	private ApplicationContext appContext;

	@Test
	public void contextLoads() {
		Assert.assertNotNull(appContext);
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.appContext = applicationContext;
	}

}
