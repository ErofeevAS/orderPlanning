package group.itechart.orderplanning.rest.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import group.itechart.orderplanning.rest.interceptor.RestTemplateJwtTokenAdderInterceptor;


@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		final RestTemplate restTemplate = new RestTemplate();

		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		if (CollectionUtils.isEmpty(interceptors)) {
			interceptors = new ArrayList<>();
		}
		interceptors.add(new RestTemplateJwtTokenAdderInterceptor());
		restTemplate.setInterceptors(interceptors);
		return restTemplate;
	}

}
