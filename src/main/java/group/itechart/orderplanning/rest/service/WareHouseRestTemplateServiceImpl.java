package group.itechart.orderplanning.rest.service;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import group.itechart.orderplanning.rest.document.WareHouseDocument;


@Service
public class WareHouseRestTemplateServiceImpl implements WareHouseRestTemplateService {

	private final RestTemplate restTemplate;

	@Value("${warehouses.url}")
	private String warehousesUrl;

	public WareHouseRestTemplateServiceImpl(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public List<WareHouseDocument> find(final double lat, final double lon, final double radius) {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		final UriComponents uriComponents = builder.uri(URI.create(warehousesUrl)).queryParam("lat", lat).queryParam("lon", lon)
				.queryParam("radius", radius).build();

		final URI uri = uriComponents.toUri();

		final WareHouseDocument[] forObject = restTemplate.getForObject(uri, WareHouseDocument[].class);
		//todo create Wrapper Class ListWareHouses
		assert forObject != null;
		return Arrays.asList(forObject);
	}

}
