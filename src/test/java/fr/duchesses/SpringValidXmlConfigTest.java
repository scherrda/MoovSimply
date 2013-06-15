package fr.duchesses;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author amrouche
 * 
 */
public class SpringValidXmlConfigTest {

	@Test
	public void should_load_spring_xml_config_with_no_errors_or_exceptions() throws Exception {
		String[] configLocations = {"classpath*:spring/applicationContext.xml"};
		ClassPathXmlApplicationContext xmlContext = new ClassPathXmlApplicationContext(configLocations, true);

		try {

			xmlContext.refresh();
			Assert.assertNotNull(xmlContext.getBean("autolibApiService"));
			Assert.assertNotNull(xmlContext.getBean("velibApiService"));
			Assert.assertNotNull(xmlContext.getBean("ratpApiService"));
			

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Context Spring en Erreur !! ");
		}

	}

}
