package group.itechart.orderplanning.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SecurityController {

	@GetMapping("/profile")
	public String home(@AuthenticationPrincipal OidcUser user) {
		return "Welcome, " + user.getFullName() + "!";
	}

	@GetMapping("/token")
	public String showToken(@RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client) {
		return client.getAccessToken().getTokenValue();
	}

}
