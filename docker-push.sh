#!/bin/bash
DOCKER_USER=trifonnt

docker push ${DOCKER_USER}/config-service:latest
docker push ${DOCKER_USER}/discovery-service:latest
docker push ${DOCKER_USER}/gateway-service:latest
docker push ${DOCKER_USER}/product-query-side:latest
docker push ${DOCKER_USER}/product-command-side:latest
