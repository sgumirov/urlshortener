package com.gumirov.shamil.urlshortener.service;

import com.gumirov.shamil.urlshortener.entity.ShortName;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ShortNameGenerator
{
  private static final char[] CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ123456789".toCharArray();
  private static final int CHARS_LEN = CHARS.length;
  private static final int SIZE = 6;

  private Random random = new Random(System.currentTimeMillis());

  public ShortName generateShortName() {
    char[] res = new char[SIZE];
    for (int i = 0; i < SIZE; ++i) {
      res[i] = CHARS[random.nextInt(CHARS_LEN)];
    }
    return new ShortName(new String(res));
  }
}
