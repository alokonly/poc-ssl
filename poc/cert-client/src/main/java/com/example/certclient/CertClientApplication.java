package com.example.certclient;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;

@SpringBootApplication
public class CertClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(CertClientApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() throws Exception {
		SSLContext sslContext = new SSLContextBuilder()
				.loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
				.build();
		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
		HttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(socketFactory)
				.build();
		HttpComponentsClientHttpRequestFactory factory =
				new HttpComponentsClientHttpRequestFactory(httpClient);
		return new RestTemplate(factory);
	}


}
