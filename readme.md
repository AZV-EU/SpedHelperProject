SpedHelper Project

Requirements:
* PostgreSql 15.3 server with database:
* Database Name: spedhelperdb
* and user:
* Username: spedhelper
* Password: spedhelper
  * Recommended: postgresql:alpine on docker with exposed port 5432
* java 17 runtime

Installation:
* PostgreSql server:
  * Run Project application once to setup database
  * Stop Project application after the database structure has been setup by the application
  * (Optional) Use pgadmin to restore sample data on database from file: spedhelper_sample_data.sql
* Postman:
  * Import collection from file: SpedHelper REST Api.postman_collection.json

Setup:
* Run PostgreSql server on port 5432
* Run Project application using maven
* Use provided Postman collection to interact with the API
  * Authentication: change x-api-key header in collection to desired key
  * "debugkey" is default debugging/test key with ADMIN authority
