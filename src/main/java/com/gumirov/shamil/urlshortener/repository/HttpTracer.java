package com.gumirov.shamil.urlshortener.repository;

import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HttpTracer extends InMemoryHttpTraceRepository
{
}
