package com.gumirov.shamil.urlshortener.model;

import com.gumirov.shamil.urlshortener.entity.ShortName;
import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ShortenResult
{
  private String longUrl;
  private String shortUrl;

  private ShortName desiredName;
  private boolean acceptedDesiredName;
}
