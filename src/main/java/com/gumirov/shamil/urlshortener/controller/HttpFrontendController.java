package com.gumirov.shamil.urlshortener.controller;

import com.gumirov.shamil.urlshortener.dto.ShortUrlResponse;
import com.gumirov.shamil.urlshortener.dto.ShortenUrlRequest;
import com.gumirov.shamil.urlshortener.entity.ShortName;
import com.gumirov.shamil.urlshortener.model.ExtractResult;
import com.gumirov.shamil.urlshortener.model.ShortenRequest;
import com.gumirov.shamil.urlshortener.model.ShortenResult;
import com.gumirov.shamil.urlshortener.service.ShortenerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP endpoint frontend.
 */
@Slf4j
@RestController
public class HttpFrontendController
{
  @Autowired
  private ShortenerService service;
  @Autowired
  private ModelMapper mapper;
  private byte[] indexPage;

  @RequestMapping(
      value = "/add",
      method = RequestMethod.POST,
      consumes = {
          MediaType.APPLICATION_FORM_URLENCODED_VALUE,
          MediaType.APPLICATION_JSON_VALUE
      },
      produces = {MediaType.APPLICATION_JSON_VALUE}
  )
  public ShortUrlResponse add(@Validated ShortenUrlRequest req) {
    log.info("ADD: desired={} long={}", req.getDesiredName(), req.getLongUrl());
    ShortenResult result = service.shortenUrl(mapper.map(req, ShortenRequest.class));
    log.info("ADD result={}", result);
    return mapper.map(result, ShortUrlResponse.class);
  }

  @RequestMapping(value = "/{shortName}", method = RequestMethod.GET)
  public void redirect(@PathVariable String shortName, HttpServletRequest req, HttpServletResponse res)
      throws IOException
  {
    log.info("GET: short={}", shortName);
    ExtractResult result = service.extractLong(new ShortName(shortName));
    log.info("GET result={}", result);
    if (result.getLongUrl() == null)
      res.sendRedirect(req.getContextPath() + "/");
    else
      res.sendRedirect(result.getLongUrl());
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public void get(HttpServletResponse res) throws IOException {
    log.info("get(): index.html");
    res.getOutputStream().write(getIndexPage());
    res.setContentType(MediaType.TEXT_HTML_VALUE);
    res.setStatus(HttpServletResponse.SC_OK);
  }

  private byte[] getIndexPage() throws IOException {
    if (indexPage == null) {
      synchronized (this) {
        if (indexPage == null) {
          InputStream in = getClass().getClassLoader().getResourceAsStream("index.html");
          if (in == null) throw new RuntimeException("Cannot find index.html");
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          IOUtils.copy(in, bos);
          bos.flush();
          indexPage = bos.toByteArray();
          bos.close();
        }
      }
    }
    return indexPage;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(Exception.class)
  public Map<String, String> handleValidationExceptions(Exception ex) {
    Map<String, String> errors = new HashMap<>();
    if (ex instanceof BindException) {
      ((BindException)ex).getBindingResult().getAllErrors().forEach((error) -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
      });
    }else{
      errors.put("error", ex.getClass().getName() + " " + ex.getMessage());
    }
    return errors;
  }
}

