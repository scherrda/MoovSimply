/*
 * Licensed to scrutmydocs.org (the "Author") under one
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

package org.moovsimply.service.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.geo.GeoDistance;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class LocalSearchService {

	@Autowired
	protected Client esClient;

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	public SearchResponse geoSearch(double lat, double lon) {
		if (logger.isDebugEnabled())
			logger.debug("google('{}', {}, {})", lat, lon);

		Assert.notNull(lat);
		Assert.notNull(lon);

		long totalHits = -1;
		long took = -1;

		SearchResponse searchResponse = null;
		QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

		
		FilterBuilder distanceFilter = FilterBuilders.geoDistanceFilter("station.location")
				.point(lat, lon).distance(10, DistanceUnit.KILOMETERS)
				.optimizeBbox("memory").geoDistance(GeoDistance.ARC);

		queryBuilder = QueryBuilders
				.filteredQuery(queryBuilder, distanceFilter);
		searchResponse = esClient.prepareSearch().setQuery(queryBuilder).setFilter(distanceFilter).execute().actionGet();
		logger.info("Full response is : {}", searchResponse);
		Assert.notNull(searchResponse);
		Assert.notNull(searchResponse.getHits());
		SearchHits searchHits = searchResponse.getHits();
		totalHits = searchHits.getTotalHits();
		took = searchResponse.tookInMillis();

		if (totalHits > 0) {
			SearchHit[] docs = searchResponse.getHits().getHits();
			float maxScore = searchResponse.getHits().getMaxScore();
			for (SearchHit sd : docs) {
				// ServiceInfo aServiceInfo =
				// ServiceInfoHelper.toServiceInfo(sd.getSource(), sd.score());
				if (maxScore == sd.score()) {
					// resultat.setServiceInfo(aServiceInfo);
				} else {
					// resultat.getServiceInfosRelies().add(aServiceInfo);
				}
			}
		}
		return searchResponse;
	}


}
