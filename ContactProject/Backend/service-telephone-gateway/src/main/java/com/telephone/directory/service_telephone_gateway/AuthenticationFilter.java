package com.telephone.directory.service_telephone_gateway;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	private static List<String> whiteList;

	static {
		whiteList = new ArrayList<>();
		whiteList.add("/auth/login");
		whiteList.add("/auth/registration");
		whiteList.add("/auth/forgotPassword");
		whiteList.add("/auth/resetPassword");
	}

	@Autowired
	private RestTemplate restTemplate;

	public AuthenticationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			String path = request.getPath().toString();

			if (path.contains("/auth")) {
				if (whiteList.contains(path)) {
					return chain.filter(exchange);
				}
				exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(404));
				return exchange.getResponse().setComplete();
			}

			if (request.getHeaders().containsKey("Authorization")) {
				// not accessible to public

				path = path.substring(1);
				path = path.substring(0, path.indexOf("/"));

				System.out.println("path........." + path);

				ValidationRequest req = new ValidationRequest(request.getHeaders().get("Authorization").get(0), path);

				System.out.println("req........." + req);

				Boolean isTokenValidate = restTemplate.postForObject("http://localhost:9090/auth/isTokenValidate", req,
						Boolean.class);
				System.out.println(isTokenValidate);
				if (!isTokenValidate) {
					exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(401));
					return exchange.getResponse().setComplete();
				}

			} else {
				exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(401));
				return exchange.getResponse().setComplete();
			}

			return chain.filter(exchange);
		};
	}

	public static class Config {

	}

}
