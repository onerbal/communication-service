FROM vault

# Install Curl
RUN apk add --no-cache curl

# Install jq
ADD https://github.com/stedolan/jq/releases/download/jq-1.6/jq-linux64 /usr/bin/jq
RUN chmod 755 /usr/bin/jq

ENV USERDB_URL jdbc:postgresql://db-users:5432/userdb
ENV DB_USERNAME admin
ENV DB_PASSWORD password
ENV VAULT_DEV_ROOT_TOKEN_ID 0000

EXPOSE 8200
ADD vault-init.sh /

RUN chmod 755 vault-init.sh

ENTRYPOINT exec "./vault-init.sh"