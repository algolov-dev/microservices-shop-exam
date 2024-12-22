package com.techie.microservices.cat.stubs;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class LoginClientStub {

    public static void stubLoginCall(String skuCode, Integer quantity) {
        if (quantity <= 100) {
            stubFor(get(urlEqualTo("/api/login?skuCode=" + skuCode + "&quantity=" + quantity))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody("true")));
        } else {
            stubFor(get(urlEqualTo("/api/login?skuCode=" + skuCode + "&quantity=" + quantity))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody("false")));
        }
    }
}
