package com.gumirov.shamil.urlshortener.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShortName
{
  private String shortUrl;

  @Override
  public String toString() {
    return shortUrl;
  }
}
