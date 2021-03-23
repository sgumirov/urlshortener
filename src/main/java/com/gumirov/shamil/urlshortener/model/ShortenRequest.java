package com.gumirov.shamil.urlshortener.model;

import com.gumirov.shamil.urlshortener.entity.ShortName;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShortenRequest
{
  private ShortName desiredName;
  private String longUrl;
}
