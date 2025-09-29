@echo off

docker buildx build -t host.docker.internal:44450/demo/5s-subscriber -f .\Dockerfile ..\..
