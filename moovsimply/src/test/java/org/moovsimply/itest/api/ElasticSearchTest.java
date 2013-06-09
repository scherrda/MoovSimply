package org.moovsimply.itest.api;

import static org.elasticsearch.common.collect.Lists.newArrayList;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.exists.IndicesExistsRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.GeoDistanceFilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Before;
import org.junit.Test;
import org.moovsimply.bean.Station;
import org.springframework.beans.factory.annotation.Autowired;

public class ElasticSearchTest extends AbstractApiTest
{
    private List<Station> stations = new ArrayList<Station>();
    private static final int NUMBER_OF_ITERATIONS = 50;
    @Autowired private Client client;

    @Before
    public void prepareDriver() throws Exception {
        if (!client.admin().indices().exists(new IndicesExistsRequest("stations")).actionGet().exists()) {
            client.admin().indices().prepareCreate("stations").execute().actionGet();
        }
        client.admin().indices() 
                      .preparePutMapping("stations") 
                        .setType("station") 
                        .setSource(mapping()) 
                        .execute().actionGet(); 
    }
    
   
    void prepareSave(Station station) throws Exception {
        stations.add(station);
    }
                                            
   
    protected void commit() throws Exception {
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for (Station station : stations) {
            bulkRequest.add(client.prepareIndex("stations", "station", station.getNom())
                    .setSource(jsonBuilder().startObject()
                            .field("nom", station.getNom())
                            .field("typeStation", station.getTypeStationEnum().name())
                    ));
        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            throw new RuntimeException("Des exceptions ont eu lieu : " + bulkResponse.buildFailureMessage());
        }        
        stations.clear();
    }
    
   
    void save(Station station) throws Exception {
        client.prepareIndex("stations", "station", station.getNom())
                .setSource(jsonBuilder()
                            .startObject()
                            .field("nom", station.getNom())
                            .field("typeStation", station.getTypeStationEnum().name())
                            .endObject())
                .execute()
                .actionGet();        
    }
    
    public static XContentBuilder mapping() throws Exception {
        XContentBuilder xbMapping = 
            jsonBuilder()
                .startObject()
                    .startObject("station")
                        .startObject("properties")
                            .startObject("location")
                                .field("type", "geo_point")
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject();
        return xbMapping;
    }    
    
   
    Iterable<Station> findByLogin(String nom) throws Exception {
        SearchResponse response = client.prepareSearch("stations")
                .setQuery(termQuery("nom", nom))
                .setFrom(0).setSize(100).setExplain(false)
                .execute()
                .actionGet();

        SearchHit[] searchHits = response.hits().getHits();
        List<Station> stations = newArrayList();

        for (SearchHit searchHit : searchHits) {
            Map<String, Object> sourceAsMap = searchHit.sourceAsMap();
            stations.add(new Station((String)sourceAsMap.get("nom")));
        }        
        
        return stations;
    }
    
   
    long count() throws Exception {
        SearchResponse response = client.prepareSearch("stations")
                .setSearchType(SearchType.COUNT)
                .execute()
                .actionGet();
        return response.getHits().getTotalHits();
    }
    
   
    Iterable<Station> findByZipCode(String zipCode) throws Exception {
        SearchResponse response = client.prepareSearch("stations")
                .setQuery(termQuery("zipcode", zipCode))
                .setFrom(0).setSize(100)
                .execute()
                .actionGet();

        SearchHit[] searchHits = response.hits().getHits();
        List<Station> stations = newArrayList();

        for (SearchHit searchHit : searchHits) {
            stations.add(new Station(searchHit.getId()));
        }        
        return stations;
    }
    
   
    void removeStation(Station station) throws Exception {
        client.prepareDelete("stations", "station", station.getNom())
                .execute()
                .actionGet();        
    }
    
   
    List<Station> geoSearch() {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        GeoDistanceFilterBuilder distanceFilter = FilterBuilders.geoDistanceFilter("location")
                .lat(1)
                .lon(1)
                .distance("30km");
        
        queryBuilder =  QueryBuilders.filteredQuery(queryBuilder, distanceFilter);

        SearchResponse response = client.prepareSearch("stations")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(0).setSize(100).setExplain(false)
                .execute()
                .actionGet();        
        
        SearchHit[] searchHits = response.hits().getHits();
        List<Station> stations = newArrayList();

        for (SearchHit searchHit : searchHits) {
            Map<String, Object> sourceAsMap = searchHit.sourceAsMap();
            stations.add(new Station((String)sourceAsMap.get("nom")));
        }        
        return stations;
    }
    
    @Test
    public void testGeoSpatialSearch() throws Exception
    {
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            List<Station> listOfProfileNearby = geoSearch();
            assertTrue(listOfProfileNearby.isEmpty());
        }
    }

	@Override
	protected String getModuleApiUrl() {
		// TODO Auto-generated method stub
		return null;
	}    
}
