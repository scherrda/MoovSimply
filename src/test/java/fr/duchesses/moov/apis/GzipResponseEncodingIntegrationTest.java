package fr.duchesses.moov.apis;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class GzipResponseEncodingIntegrationTest {
    @Test
    public void responseIsGzipEncoded(){
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8080/rest/moovin/around");

        ClientResponse response = webResource.header("Accept-Encoding", "gzip").head();

        Object contentEncoding = response.getHeaders().get("Content-Encoding");
        assertThat(contentEncoding).isNotNull();
        assertThat(contentEncoding.toString()).isEqualTo("[gzip]");
    }
}
