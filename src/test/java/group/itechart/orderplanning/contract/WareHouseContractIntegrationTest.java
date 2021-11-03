package group.itechart.orderplanning.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import group.itechart.orderplanning.rest.document.WareHouseDocument;
import group.itechart.orderplanning.rest.service.WareHouseRestTemplateService;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.itechart.group:warehouses:+:stubs:8090")
class WareHouseContractIntegrationTest {

	@Autowired
	private WareHouseRestTemplateService wareHouseRestTemplateService;

	@Test
	public void shouldReturnNearestWarehousesInRadius() {
		final List<WareHouseDocument> wareHouseDocuments = wareHouseRestTemplateService.find(1.1, 1.2, 2);
		assertEquals(3, wareHouseDocuments.size());
	}

}