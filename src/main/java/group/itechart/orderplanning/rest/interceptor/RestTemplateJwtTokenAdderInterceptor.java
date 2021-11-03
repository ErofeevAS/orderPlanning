package group.itechart.orderplanning.rest.interceptor;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


public class RestTemplateJwtTokenAdderInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution)
			throws IOException {

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof JwtAuthenticationToken) {
			final String tokenValue = ((JwtAuthenticationToken) authentication).getToken().getTokenValue();
			request.getHeaders().add("Authorization", "Bearer " + tokenValue);
		}

		ClientHttpResponse response = execution.execute(request, body);

		return response;
	}
}
