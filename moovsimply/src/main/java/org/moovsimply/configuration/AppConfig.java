/*
 * Licensed to moovsimply.org (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.moovsimply.configuration;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.node.Node;
import org.moovsimply.util.PropertyScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import fr.pilato.spring.elasticsearch.ElasticsearchAbstractClientFactoryBean;
import fr.pilato.spring.elasticsearch.ElasticsearchClientFactoryBean;
import fr.pilato.spring.elasticsearch.ElasticsearchNodeFactoryBean;
import fr.pilato.spring.elasticsearch.ElasticsearchTransportClientFactoryBean;
@Configuration
public class AppConfig {

	private ESLogger logger = Loggers.getLogger(getClass().getName());
 
	@Bean
	public MoovSimplyProperties smdProperties() throws Exception {
		MoovSimplyProperties smdProperties = PropertyScanner.scanPropertyFile(); 
		return smdProperties;
	}

	@Bean
	public Node esNode() throws Exception {
		MoovSimplyProperties smdProperties = smdProperties();
		
		if (smdProperties.isNodeEmbedded()) {
			logger.info("Starting embedded Node...");
			ElasticsearchNodeFactoryBean factory = new ElasticsearchNodeFactoryBean();
			factory.afterPropertiesSet();
			return factory.getObject();
		}
		
		return null;
	}

	@Bean
	public Client esClient() throws Exception {
		MoovSimplyProperties smdProperties = smdProperties();
		ElasticsearchAbstractClientFactoryBean factory = null;
		if (smdProperties.isNodeEmbedded()) {
			logger.info("Starting client Node...");
			factory = new ElasticsearchClientFactoryBean();
			((ElasticsearchClientFactoryBean) factory).setNode(esNode());
		} else {
			logger.info("Starting client for cluster {} at {} ...", smdProperties.getClusterName(), smdProperties.getNodeAdresses());
			factory = new ElasticsearchTransportClientFactoryBean();
			((ElasticsearchTransportClientFactoryBean) factory).setEsNodes(smdProperties.getNodeAdresses());
		}
		// TODO Manage ES Settings
		// factory.setSettings(settings)
		factory.afterPropertiesSet();
		
		return factory.getObject();
	}
	
	@Bean
	public RestTemplate restTemplate (){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		return restTemplate;
	}
	
}
