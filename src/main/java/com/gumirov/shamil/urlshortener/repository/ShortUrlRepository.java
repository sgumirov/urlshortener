package com.gumirov.shamil.urlshortener.repository;

import com.gumirov.shamil.urlshortener.config.AppConfiguration;
import com.gumirov.shamil.urlshortener.entity.ShortName;
import org.springframework.context.annotation.Profile;

public interface ShortUrlRepository
{
  boolean hasShortName(ShortName shortName);

  void add(String longUrl, ShortName shortUrl);

  String extractLong(ShortName shortUrl);

  @Profile(AppConfiguration.DEVELOPMENT)
  public void clear();
}
