NAME=UrlShortenerService
VER=1.0

SHELL=/bin/bash
.SHELLFLAGS = -o pipefail -c

PORT=8080
IMAGE=shamil/url-shortener
IMAGE-TEST=shamil/url-shortener-test
CNT=url-shortener
HOST ?= 127.0.0.1
URL = http://$(HOST):$(PORT)/

# Determine this makefile's path.
# Be sure to place this BEFORE `include` directives, if any.
THIS_FILE := $(lastword $(MAKEFILE_LIST))

.PHONY: run help test version start stop status logs build tests-local publish-container restart-container build-test-container tests-basic tests-local start-container stop-container all-tests-container unit-tests-container unit-tests
.DEFAULT_GOAL:=help

build :
	@docker build -t $(IMAGE):latest -f Dockerfile .
	@echo "Container built OK"

build-test :
	@echo "Building test container image"
	@docker build -t $(IMAGE-TEST) -f Dockerfile-test .
	@echo "Test container image built OK"

start-container :
	@docker run -d --rm -p $(PORT):8080/tcp --name $(CNT)-production $(IMAGE):production

run : 
	@echo "Running locally..."
	@./gradlew run

stop-container :
	@docker stop $(CNT)-production || echo "INFO: Container is not running, nothing to stop"

publish-container : build
	@echo "List of all related images:"
	@docker image ls | grep $(IMAGE)
	@echo "Tagging image=$(IMAGE) tag=latest with tag=production..."
	@docker tag $(IMAGE) $(IMAGE):production

restart-container : stop-container start-container

status :
	@docker ps | grep $(CNT)
	@docker ps | grep $(CNT-TEST)

logs :
	@docker logs $(CNT)-production

version :
	@echo "$(NAME) project makefile ver.$(VER)"

help : version
	@echo "Targets list:"
	@grep ' :' Makefile | egrep -v ".PHONY|.DEFAULT_GOAL|^\t|THIS" | cut -d ":" -f1

tests :
	@echo "Running tests..."
	./gradlew --rerun-tasks test integrationTest
	@echo "Tests PASSED OK"

tests-container : build-test
	@echo "Running all tests in container"
	docker run -t --rm --name $(CNT)-tests -e TERM='dumb' $(IMAGE-TEST) ./gradlew --rerun-tasks test integrationTest
	@echo "All tests PASSED OK"

