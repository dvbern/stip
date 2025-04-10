# Troubleshooting

## Startup failure
A startup failure could be caused by any number of issues, including but not limited to

### Liquibase fails to acquire changelog lock
Liquibase requires locking a database entry before it does anything with the schema, but sometimes (in exceptional circumstances) it fails to release the lock. In that case a manual lock reset is often required to get it working again.

1. Connect to the database instance (Postgres CLI, IntelliJ Query Console etc.)
2. Execute the following query `UPDATE databasechangeloglock SET locked=false, lockgranted=null, lockedby=null where id=1;`
