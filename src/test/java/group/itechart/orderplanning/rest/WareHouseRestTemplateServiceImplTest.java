package group.itechart.orderplanning.rest;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import group.itechart.orderplanning.rest.config.RestTemplateConfig;
import group.itechart.orderplanning.rest.document.Geometry;
import group.itechart.orderplanning.rest.document.WareHouseDocument;
import group.itechart.orderplanning.rest.service.WareHouseRestTemplateService;


@RestClientTest(WareHouseRestTemplateService.class)
@Import(value = RestTemplateConfig.class)
class WareHouseRestTemplateServiceImplTest {

	@Autowired
	private WareHouseRestTemplateService wareHouseRestTemplateService;

	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	private ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	public void init() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void givenMockingIsDoneByMockRestServiceServer_whenGetIsCalled_thenReturnsMockedObject()
			throws JsonProcessingException {
		List<WareHouseDocument> wareHouseDocuments = new ArrayList<>();
		WareHouseDocument wareHouses1 = new WareHouseDocument("1", "test1", new Geometry("Point", new double[] { 1.0, 2.1 }));
		WareHouseDocument wareHouses2 = new WareHouseDocument("2", "test2", new Geometry("Point", new double[] { 1.0, 2.1 }));
		WareHouseDocument wareHouses3 = new WareHouseDocument("3", "test3", new Geometry("Point", new double[] { 1.0, 2.1 }));
		wareHouseDocuments.add(wareHouses1);
		wareHouseDocuments.add(wareHouses2);
		wareHouseDocuments.add(wareHouses3);

		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

		final UriComponents uriComponents = builder.uri(URI.create("http://localhost:8090/api/v1/warehouses")).build();

		final URI uri = uriComponents.toUri();

		mockServer.expect(ExpectedCount.once(), requestTo(startsWith(uri.toString()))).andExpect(queryParam("lat", "1.1"))
				.andExpect(queryParam("lon", "1.2")).andExpect(queryParam("radius", "10.0")).andExpect(method(HttpMethod.GET))

				.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
								.body(mapper.writeValueAsString(wareHouseDocuments))
				);

		final List<WareHouseDocument> wareHouseDocuments1 = wareHouseRestTemplateService.find(1.1, 1.2, 10.0);
		mockServer.verify();

		assertEquals(wareHouseDocuments, wareHouseDocuments1);
	}
}