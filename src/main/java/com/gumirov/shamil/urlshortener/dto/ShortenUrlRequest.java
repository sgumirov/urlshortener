package com.gumirov.shamil.urlshortener.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShortenUrlRequest
{
  @NotNull
  @NotBlank
  private String longUrl;
  @NotNull
  @NotBlank
  private String desiredName;
}
