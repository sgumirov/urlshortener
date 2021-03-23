package com.gumirov.shamil.urlshortener.dto;

import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ShortUrlResponse
{
  private String shortUrl;
  private boolean hasAcceptedDesiredName;
}
