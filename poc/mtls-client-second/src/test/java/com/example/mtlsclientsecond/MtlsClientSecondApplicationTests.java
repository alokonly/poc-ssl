package com.example.mtlsclientsecond;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MtlsClientSecondApplicationTests {

	@Autowired
	private RestTemplate restTemplateWithTrustStore;

	@Test
	void contextLoads() {
	}

	@Test
	public void okResponse() throws Exception {
		ResponseEntity<String> response = restTemplateWithTrustStore
				.getForEntity("https://localhost:8443/v1/sample", String.class, Collections.emptyMap());

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
