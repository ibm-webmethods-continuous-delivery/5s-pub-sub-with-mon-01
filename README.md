# Publish-Subscribe Service Development Template

- [Publish-Subscribe Service Development Template](#publish-subscribe-service-development-template)
  - [Prerequisites](#prerequisites)
  - [Fundamental Principles and Decisions](#fundamental-principles-and-decisions)
  - [Structure of the repository](#structure-of-the-repository)


This template aims to help a webMethods implementation team quickly start with local development and continuous delivery for publish-subscribe services.

## Prerequisites

Check the images configured in the `.env` files. The default values are done consistently to the [generic ingestion framework project](../4h-ext-images/framework/generic-image-ingester/)

## Fundamental Principles and Decisions

1. Monorepo, multiple standpoints

    Due to how webMethods Integration Server and Microservices Runtime work and to a desire to simplify source control management, we assume that all the integration system code reside in a single repository, this one.
    There is a single code folder, and there are multiple run configurations for development and test.

2. Code is text

    This repository is not allowed to hold any binary form of code.
    Flow code is XML. Eventual Archi files are also XML.
    XML is text.

    Do not upload any form of zip, jar, binary, class files and so on. Also, .frag integration server files must be left out, they are temporary generated files.

    Any form of documentation is also text. Use markdown files, diagrams as code, for example using mermaid in the markdown files.
    DrawIO files are also allowed.

    Documentation may make use of diagrams snapshots, for example in form of png or jpeg, but the original file must be present for increments.

3. To the possible extent, CI/CD processes MUST be tested locally with agnostic container based agents

    The idea is to have the local agent adapted to any orchestrator of choice

4. CI/CD agents are ephemeral

    Having ephemeral CI/CD agents forces the principle of having stateless agents. This allows for multiple positive outcomes, like:

    - leverage cloud strong points: elastic scalability, quick provisioning, parallelism, pay for what you use (minutes, not 24x7)
    - protect against snowflake servers phenomenon
    - guarantee a high level of determinism for the pursued outcomes

5. Deliverables are built IS packages or container images
6. It is not the purpose of this repo to build technology images. Use provided images and whenever those are insufficient to the problem at hand, refer to the dedicated image building repos. 

## Structure of the repository

Folder|Description
-|-
01.code|Contains code exclusively
02.build|Contains build run configurations
03.dev|Contains development "run configurations" or "development environments"
04.container-builds|Contains local test "run configurations" or "test environments"
05.test-envs|Contain acceptance level test environments
06.cicd|Contains continuous integration and delivery scripts, run configurations, pipeline artifacts and so on
09.artifacts|Artifacts produced while working with this repository. Files here are mainly intended to live on the development or CI box.

