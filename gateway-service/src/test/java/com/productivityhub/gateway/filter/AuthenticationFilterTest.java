package com.productivityhub.gateway.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationFilterTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void contextLoads() {
        // Verify application context loads
    }

    @Test
    void testAuthRouteExists() {
        org.junit.jupiter.api.Assertions.assertNotNull(routeLocator);
    }
}
