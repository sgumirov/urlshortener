NAME=urlshortener
VER=0.0.1-SNAPSHOT

SHELL=/bin/bash
.SHELLFLAGS = -o pipefail -c

PORT=28080
IMAGE=shamil/url-shortener
IMAGE-TEST=shamil/url-shortener-test
CNT=url-shortener

# Determine this makefile's path.
# Be sure to place this BEFORE `include` directives, if any.
THIS_FILE := $(lastword $(MAKEFILE_LIST))

.PHONY: run-local help test version start stop status logs build publish restart build-test test start stop test-container
.DEFAULT_GOAL:=help

build :
	@docker build --build-arg VERSION=${VER} --build-arg NAME=${NAME} -t $(IMAGE):latest -f Dockerfile .
	@echo "Container built OK"

build-test :
	@echo "Building test container image"
	@docker build -t $(IMAGE-TEST) -f Dockerfile-test .
	@echo "Test container image built OK"

start :
	@docker run -d --rm -p $(PORT):8080/tcp --name $(CNT)-production $(IMAGE):production

run-local : 
	@echo "Running locally..."
	@./gradlew bootRun

stop :
	@docker stop $(CNT)-production || echo "INFO: Container is not running, nothing to stop"

publish : build
	@echo "List of all related images:"
	@docker image ls | grep $(IMAGE)
	@echo "Tagging image=$(IMAGE) tag=latest with tag=production..."
	@docker tag $(IMAGE) $(IMAGE):production

restart : stop start

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

test :
	@echo "Running tests locally..."
	./gradlew --rerun-tasks test integrationTest
	@echo "Tests PASSED OK"

test-container : build-test
	@echo "Running all tests in container"
	docker run -t --rm --name $(CNT)-tests -e TERM='dumb' $(IMAGE-TEST) ./gradlew :test -i
	@echo "All tests PASSED OK"

