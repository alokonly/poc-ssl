package com.example.mtlsclientsecond;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;

@SpringBootApplication
public class MtlsClientSecondApplication {

	public static void main(String[] args) {
		SpringApplication.run(MtlsClientSecondApplication.class, args);
	}
	@Value("${key.store}")
	private String keyStore;

	@Value("${key.store.password}")
	private String keyStorePassword;

	@Value("${trust.store}")
	private Resource trustStore;

	@Value("${trust.store.password}")
	private String trustStorePassword;

	@Bean
	RestTemplate restTemplateWithTrustStore() throws Exception {
		KeyStore keyStore = KeyStore.getInstance("JKS");

		ClassPathResource classPathResource = new ClassPathResource(this.keyStore);

		keyStore.load(classPathResource.getInputStream(), keyStorePassword.toCharArray());

		SSLContext sslContext = new SSLContextBuilder()
				.loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
				.loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
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
