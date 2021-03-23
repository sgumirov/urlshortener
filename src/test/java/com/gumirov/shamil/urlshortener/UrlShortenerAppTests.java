package com.gumirov.shamil.urlshortener;

import com.gumirov.shamil.urlshortener.controller.HttpFrontendController;
import com.gumirov.shamil.urlshortener.dto.ShortUrlResponse;
import com.gumirov.shamil.urlshortener.dto.ShortenUrlRequest;
import com.gumirov.shamil.urlshortener.entity.ShortName;
import com.gumirov.shamil.urlshortener.model.ExtractResult;
import com.gumirov.shamil.urlshortener.model.ShortenRequest;
import com.gumirov.shamil.urlshortener.model.ShortenResult;
import com.gumirov.shamil.urlshortener.repository.ShortUrlRepository;
import com.gumirov.shamil.urlshortener.service.ShortenerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UrlShortenerAppTests
{
	@Autowired
	private HttpFrontendController controller;
	@Autowired
	private ShortenerService service;
	@Autowired
	private ShortUrlRepository repository;

	private static final String LONG_URL = "http://longurl", DESIRED_NAME = "abcdef", EXPECTED = "/"+DESIRED_NAME;

	@Test
	void contextLoads() {
		assertNotNull(controller);
	}

	@Test
	void storesShortUrl() {
		ShortUrlResponse result = controller.add(new ShortenUrlRequest(LONG_URL, DESIRED_NAME));
		assertEquals(EXPECTED, result.getShortUrl());
		assertTrue(result.isHasAcceptedDesiredName());
	}

	@AfterEach
	void cleanRepository() {
		repository.clear();
	}

	@Test
	void redirectWorks() {
		//todo test in integration test setup
		serviceExtractLongWorks();
	}

	void serviceExtractLongWorks() {
		//this is a replacement for redirectWorks()
		controller.add(new ShortenUrlRequest(LONG_URL, DESIRED_NAME));
		ExtractResult res = service.extractLong(new ShortName(DESIRED_NAME));
		assertEquals(LONG_URL, res.getLongUrl());
	}

	@Test
	void serviceTestDesiredName() {
		ShortenRequest req0 = new ShortenRequest(new ShortName(DESIRED_NAME), LONG_URL);
		// Used the desired name if added for the first time
		assertTrue(service.shortenUrl(req0).isAcceptedDesiredName());

		// Same if added multiple times
		assertTrue(service.shortenUrl(req0).isAcceptedDesiredName());

		ShortenRequest req1 = new ShortenRequest(new ShortName(DESIRED_NAME), LONG_URL+"1");
		// Cannot use the desired name if different long url
		ShortenResult res1 = service.shortenUrl(req1);
		assertFalse(res1.isAcceptedDesiredName());
		assertNotEquals(EXPECTED, res1.getShortUrl());

		// Same for repeated request
		ShortenResult res2 = service.shortenUrl(req1);
		assertFalse(res2.isAcceptedDesiredName());
		assertNotEquals(EXPECTED, res2.getShortUrl());

		// And generates different urls
		assertNotEquals(res1.getShortUrl(), res2.getShortUrl());
	}
}
