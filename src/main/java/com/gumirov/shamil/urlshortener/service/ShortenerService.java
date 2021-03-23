package com.gumirov.shamil.urlshortener.service;

import com.gumirov.shamil.urlshortener.entity.ShortName;
import com.gumirov.shamil.urlshortener.model.ExtractResult;
import com.gumirov.shamil.urlshortener.model.ShortenRequest;
import com.gumirov.shamil.urlshortener.model.ShortenResult;
import com.gumirov.shamil.urlshortener.repository.ShortUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShortenerService
{
  private static final int MAX_ITERATIONS = 10;
  
  @Autowired
  private ShortUrlRepository repository;

  @Autowired
  private ShortNameGenerator generator;

  public ShortenResult shortenUrl(ShortenRequest req) {
    ShortName shortName = req.getDesiredName();

    // already has the short name?
    boolean desiredNameUsed = repository.hasShortName(shortName);
    // ..and the target is different?
    boolean targetIsDifferent = desiredNameUsed && !repository.extractLong(shortName).equals(req.getLongUrl());
    log.info("shortenUrl(): shortName={} longUrl={} desiredNameUsed={} targetIsDifferent={}", shortName, req.getLongUrl(),
              desiredNameUsed, targetIsDifferent);
    if (!desiredNameUsed || targetIsDifferent) {
      if (targetIsDifferent) {
        int count = MAX_ITERATIONS;
        boolean hasShort;
        do {
          shortName = generator.generateShortName();
        } while ((hasShort = repository.hasShortName(shortName)) && --count > 0);
        if (hasShort && count == 0) {
          throw new RuntimeException("Error: cannot generate short name after " + MAX_ITERATIONS + " tries.");
        }
      }
      repository.add(req.getLongUrl(), shortName);
    }
    // There's only one case left: desired name is already used for the same long url. No need to add anything to repo then.
    return ShortenResult.builder().
        desiredName(shortName).
        longUrl(req.getLongUrl()).
        shortUrl(buildShortUrl(shortName)).
        acceptedDesiredName(req.getDesiredName().getShortUrl().equals(shortName.getShortUrl())).
        build();
  }

  private String buildShortUrl(ShortName desiredName) {
    return "/" + desiredName.getShortUrl();
  }

  public ExtractResult extractLong(ShortName req) {
    String longUrl = repository.extractLong(req);
    return new ExtractResult(longUrl);
  }
}