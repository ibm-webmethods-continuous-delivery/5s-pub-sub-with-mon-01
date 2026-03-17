ARG __from_img=ibmwebmethods.azurecr.io/webmethods-microservicesruntime:11.1.0.9
FROM ${__from_img}

## Context MUST be the repository root folder

ARG __wm_home=/opt/softwareag
ARG __wm_user_id=1724

COPY  --chown=${__wm_user_id}:0 01.code/is-packages/Canonicals ${__wm_home}/IntegrationServer/packages/Canonicals
COPY  --chown=${__wm_user_id}:0 01.code/is-packages/CommonUtils ${__wm_home}/IntegrationServer/packages/CommonUtils
COPY  --chown=${__wm_user_id}:0 01.code/is-packages/SubscriberExample ${__wm_home}/IntegrationServer/packages/SubscriberExample

RUN mkdir -p /opt/softwareag/IntegrationServer/logs

VOLUME [ "/opt/softwareag/IntegrationServer/logs" ]
