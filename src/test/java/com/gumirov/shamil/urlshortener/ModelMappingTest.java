package com.gumirov.shamil.urlshortener;

import com.gumirov.shamil.urlshortener.dto.ShortUrlResponse;
import com.gumirov.shamil.urlshortener.entity.ShortName;
import com.gumirov.shamil.urlshortener.model.ShortenResult;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ModelMappingTest
{
  @Autowired
  private ModelMapper mapper;

  @Test
  void testModelMapping() {
    String l = "l", s = "s";
    ShortenResult result = new ShortenResult(l, s, new ShortName(s), true);
    ShortUrlResponse response = mapper.map(result, ShortUrlResponse.class);
    assertEquals(result.isAcceptedDesiredName(), response.isHasAcceptedDesiredName());
    assertEquals(result.getShortUrl(), response.getShortUrl());
  }
}
