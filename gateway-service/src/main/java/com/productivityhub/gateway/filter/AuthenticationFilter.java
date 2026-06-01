package com.productivityhub.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final WebClient webClient;
    private final String authServiceUrl;
    private final String moduleRegistryUrl;

    public AuthenticationFilter(
            WebClient.Builder webClientBuilder,
            @Value("${app.auth.service-url}") String authServiceUrl,
            @Value("${app.module-registry.service-url}") String moduleRegistryUrl
    ) {
        super(Config.class);
        this.webClient = webClientBuilder.build();
        this.authServiceUrl = authServiceUrl;
        this.moduleRegistryUrl = moduleRegistryUrl;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // Skip auth for public endpoints
            if (isPublicEndpoint(path)) {
                return chain.filter(exchange);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return unauthorized(exchange, "Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);

            return webClient.post()
                    .uri(authServiceUrl + "/api/auth/validate")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .flatMap(response -> {
                        boolean valid = Boolean.TRUE.equals(response.get("valid"));
                        if (!valid) {
                            return unauthorized(exchange, "Invalid or expired token");
                        }

                        String userId = (String) response.get("userId");
                        String module = extractModule(path);

                        if (module != null && !module.equals("auth")) {
                            return webClient.get()
                                    .uri(moduleRegistryUrl + "/api/modules/user/check?module=" + module + "&userId=" + userId)
                                    .retrieve()
                                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                                    .flatMap(moduleResponse -> {
                                        boolean enabled = Boolean.TRUE.equals(moduleResponse.get("enabled"));
                                        if (!enabled) {
                                            return unauthorized(exchange, "Module '" + module + "' is disabled for this user");
                                        }
                                        ServerHttpRequest modifiedRequest = request.mutate()
                                                .header("X-User-Id", userId)
                                                .build();
                                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                                    })
                                    .onErrorResume(e -> {
                                        log.warn("Failed to check module status, proceeding: {}", e.getMessage());
                                        ServerHttpRequest modifiedRequest = request.mutate()
                                                .header("X-User-Id", userId)
                                                .build();
                                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                                    });
                        }

                        ServerHttpRequest modifiedRequest = request.mutate()
                                .header("X-User-Id", userId)
                                .build();
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    })
                    .onErrorResume(e -> {
                        log.error("Auth service call failed: {}", e.getMessage());
                        return unauthorized(exchange, "Authentication service unavailable");
                    });
        };
    }

    private boolean isPublicEndpoint(String path) {
        return path.equals("/api/auth/register") || path.equals("/api/auth/login");
    }

    private String extractModule(String path) {
        if (path.startsWith("/api/todos")) return "todo";
        if (path.startsWith("/api/finance")) return "finance";
        if (path.startsWith("/api/notes")) return "notes";
        if (path.startsWith("/api/planner")) return "planner";
        if (path.startsWith("/api/dashboard")) return "dashboard";
        if (path.startsWith("/api/modules")) return "module-registry";
        return null;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        log.warn("Unauthorized request: {}", message);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // configuration properties if needed
    }
}
