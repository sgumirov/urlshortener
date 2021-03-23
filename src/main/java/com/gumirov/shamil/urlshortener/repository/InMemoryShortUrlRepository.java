package com.gumirov.shamil.urlshortener.repository;

import com.gumirov.shamil.urlshortener.config.AppConfiguration;
import com.gumirov.shamil.urlshortener.entity.ShortName;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Profile(AppConfiguration.DEVELOPMENT)
@Repository
public class InMemoryShortUrlRepository
    implements ShortUrlRepository
{
  private Map<String, String> shortToLong = Collections.synchronizedMap(new HashMap<>());
  
  @Override
  public boolean hasShortName(ShortName shortName) {
    return shortToLong.containsKey(shortName.getShortUrl());
  }

  @Override
  public void add(String longUrl, ShortName shortUrl) {
    shortToLong.putIfAbsent(shortUrl.getShortUrl(), longUrl);
  }

  @Override
  public String extractLong(ShortName shortUrl) {
    return shortToLong.get(shortUrl.getShortUrl());
  }

  @Profile(AppConfiguration.DEVELOPMENT)
  @Override
  public void clear() {
    shortToLong.clear();
  }
}
