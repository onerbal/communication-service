echo "Staring Vault..."
docker-entrypoint.sh server -dev &
echo "Sleeping 10..."
sleep 10
echo "Vault Started."

echo "Exporting address"
export VAULT_ADDR="http://localhost:8200"

echo "Authenticate into Vault"
# Authenticate to Vault
vault login $VAULT_DEV_ROOT_TOKEN_ID

echo "Adding secrets to Vault..."
vault kv put secret/user-service/dev spring.datasource.username=$DB_USERNAME spring.datasource.password=$DB_PASSWORD spring.datasource.url=$USERDB_URL
vault kv put secret/user-service/prod spring.datasource.username=$DB_USERNAME spring.datasource.password=$DB_PASSWORD spring.datasource.url=$USERDB_URL

vault kv put secret/campaign-service/dev spring.datasource.username=$DB_USERNAME spring.datasource.password=$DB_PASSWORD spring.datasource.url=$USERDB_URL
vault kv put secret/campaign-service/prod spring.datasource.username=$DB_USERNAME spring.datasource.password=$DB_PASSWORD spring.datasource.url=$USERDB_URL

vault kv put secret/notification-service/dev spring.datasource.username=$DB_USERNAME spring.datasource.password=$DB_PASSWORD spring.datasource.url=$USERDB_URL
vault kv put secret/notification-service/prod spring.datasource.username=$DB_USERNAME spring.datasource.password=$DB_PASSWORD spring.datasource.url=$USERDB_URL

vault kv put secret/email-service/dev spring.datasource.username=$DB_USERNAME spring.datasource.password=$DB_PASSWORD spring.datasource.url=$USERDB_URL
vault kv put secret/email-service/prod spring.datasource.username=$DB_USERNAME spring.datasource.password=$DB_PASSWORD spring.datasource.url=$USERDB_URL

while [ "$(curl -XGET --insecure --silent -H "X-Vault-Token: $VAULT_DEV_ROOT_TOKEN_ID" http://localhost:8200/v1/sys/health | jq '.initialized')" == "true" ]
do
    sleep 2
done