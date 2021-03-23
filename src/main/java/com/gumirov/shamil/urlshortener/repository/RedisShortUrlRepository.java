package com.gumirov.shamil.urlshortener.repository;

import com.gumirov.shamil.urlshortener.config.AppConfiguration;
import com.gumirov.shamil.urlshortener.entity.ShortName;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile(AppConfiguration.PRODUNCTION)
@Repository
public class RedisShortUrlRepository implements ShortUrlRepository
{
  @Override
  public boolean hasShortName(ShortName shortName){return false;}

  @Override
  public void add(String longUrl, ShortName shortUrl) {

  }

  @Override
  public String extractLong(ShortName shortUrl) {
    return null;
  }

  @Override
  public void clear() {

  }
}
