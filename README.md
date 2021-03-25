# URL Shortener microservice

Spring microservice which maps short url to long one and retrieves long one using short one.
For a list of operations see HTTP Endpoints section below.

## Architecture

```
  ------------------     --------------------     ------------------------------------------
  | HttpController | ==> | ShortenerService | ==> | ShortUrlRepository (Redis or InMemory) |
  ------------------     --------------------     ------------------------------------------
                                  || uses
				  \/
 	                ----------------------
 		        | ShortNameGenerator |
                        ----------------------
```

## Quick run

In docker (port 28080):  
`make publish start`

Locally (port 8080):  
`make run-local`

## Frontends

### HTTP Endpoints

Endpoints:

- [POST] `/add` with json object `{longUrl: string, desiredName: string}` adds new url.  
For example:
```
{
  'longUrl': 'http://google.com',
  'desiredName': 'gogl'
}
```

- [GET] `/` displays basic html input form allowing to add new url

- [GET] `/*` extracts long url and redirects via `HTTP 302 Moved` or to '/' if not found.


### Message Queue

RabbitMQ [todo].

## Persistency layers

- Redis (production, to be implemented)
- In-memory (development)

## Tests

### Unit tests

Coverage: Service methods 100%.

### Integration tests

To be implemented.
