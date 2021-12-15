package com.example.certclient.config;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import javax.net.ssl.KeyManagerFactory;
import java.security.KeyStore;


@Component
@Slf4j
public class SSLWebClient {
    private String KEYSTORE_FILE = "/Users/alok.alok/Workspace/Jio/poc/keystore.jks";
    private String KEYSTORE_PASSWORD = "changeit";
    WebClient webClient;

    public WebClient getWebClient() {
        return webClient;
    }

    @Autowired
    public SSLWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = buildCustomWebClient(webClientBuilder, "http://localhost:8443");
    }

    private WebClient buildCustomWebClient(WebClient.Builder builder, String baseUrl){

        HttpClient client = HttpClient.create().wiretap("reactor.netty.http.client.HttpClient",
                LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL).secure(spec -> spec.sslContext(createSSLContext()));
        ClientHttpConnector connector = new ReactorClientHttpConnector(client);

        return builder
                .baseUrl(baseUrl)
                .clientConnector(connector)
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(logRequest());
                    exchangeFilterFunctions.add(logResponse());
                })
                .build();
    }

    private SslContext createSSLContext() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(getClass().getClassLoader().getResourceAsStream(KEYSTORE_FILE), KEYSTORE_PASSWORD.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

            return SslContextBuilder.forClient()
                    .keyManager(keyManagerFactory)
                    .build();

        }
        catch (Exception e) {
            throw new RuntimeException("Error creating SSL context.");
        }
    }

    ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                StringBuilder sb = new StringBuilder("Request: \n");
                //append clientRequest method and url
                clientRequest
                        .headers()
                        .forEach((name, values) -> values.forEach(value -> sb.append(value)));
                log.debug(sb.toString());
            return Mono.just(clientRequest);
        });
    }

    ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
                StringBuilder sb = new StringBuilder("Response: \n");
                clientResponse
                        .headers().asHttpHeaders()
                        .forEach((name, values) -> values.forEach(value -> sb.append(value)));
                log.debug(sb.toString());
            return Mono.just(clientResponse);
        });
    }
}
