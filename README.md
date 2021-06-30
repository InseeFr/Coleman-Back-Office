# Coleman-Back-Office
Back-office services for Coleman  
REST API for communication between Coleman DB and Coleman UI.

## Requirements
For building and running the application you need:
- [JDK 11](https://jdk.java.net/archive/)
- Maven 3  

## Install and excute unit tests
Use the maven clean and maven install 
```shell
mvn clean install
```  

## Running the application locally
Use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:  
```shell
mvn spring-boot:run
```  

## Application Accesses locally
To access to swagger-ui, use this url : [http://localhost:8080/api/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
To access to h2 console, use this url : [http://localhost:8080/api/h2-console](http://localhost:8080/api/h2-console)  


## Keycloak Configuration 
1. To start the server on port 8180 execute in the bin folder of your keycloak :
```shell
standalone.bat -Djboss.socket.binding.port-offset=100 (on Windows)

standalone.sh -Djboss.socket.binding.port-offset=100 (on Unix-based systems)
```  
2. Go to the console administration and create role investigator and a user with this role.

 
## Deploy application on Tomcat server
### 1. Package the application
Use the [Spring Boot Maven plugin]  (https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:  
```shell
mvn clean package
```  
The war will be generate in `/target` repository  

### 2. Install tomcat and deploy war
To deploy the war file in Tomcat, you need to : 
Download Apache Tomcat and unpackage it into a tomcat folder  
Copy your WAR file from target/ to the tomcat/webapps/ folder  

### 3. Tomcat config
Before to startup the tomcat server, some configurations are needed : 
 

#### External Properties file
Create colemanbo.properties or colemcobo.properties near war file and complete the following properties:  
```shell  
#Logs configuration
fr.insee.coleman.logging.path=classpath:log4j2.xml
fr.insee.coleman.logging.level=INFO
#Application configuration
fr.insee.coleman.application.mode=noauth
fr.insee.coleman.application.crosOrigin=*
#Database configuration
fr.insee.coleman.persistence.database.host = localhost
fr.insee.coleman.persistence.database.port = 5432
fr.insee.coleman.persistence.database.schema = coleman_api
fr.insee.coleman.persistence.database.user = coleman
fr.insee.coleman.persistence.database.password = coleman
fr.insee.coleman.persistence.database.driver = org.postgresql.Driver
fr.insee.coleman.defaultSchema=public
#Keycloak configuration
keycloak.realm=insee-realm
keycloak.resource=coleman
keycloak.auth-server-url=http://localhost:8180/auth
keycloak.public-client=true
keycloak.bearer-only=true
keycloak.principal-attribute:preferred_username

coleman.environnement="environnement local"
fr.insee.coleman.ldap.services.uri.hote=https://contacts.insee.test
fr.insee.coleman.ldap.services.uri.port=443
fr.insee.coleman.ldap.externe.server.login=coleman
fr.insee.coleman.ldap.externe.server.pw=coleman
fr.insee.coleman.ldap.externe.domaineGestion=coleman
​
fr.insee.coleman.api.dataloader.load=false​
```

#### External log file
Create log4j2.xml near war file and define your  external config for logs.  

### 4. Tomcat start
From a terminal navigate to tomcat/bin folder and execute  
```shell
catalina.bat run (on Windows)
```  
```shell
catalina.sh run (on Unix-based systems)
```  

### 5. Application Access
To access to swagger-ui, use this url : [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
To access to keycloak, use this url : [http://localhost:8180](http://localhost:8180)  

## Before you commit
Before committing code please ensure,  
1 - README.md is updated  
2 - A successful build is run and all tests are sucessful  
3 - All newly implemented APIs are documented  
4 - All newly added properties are documented  

## End-Points
- Rest Basic
	- GET /env` : env
	- GET /helloworld` : helloWorld


- Rest Campaign
	- `GET /campaigns` : displayCampaignInProgress
	- `POST /campaigns` : addCampaigns
	- `GET /campaigns/{idCampaign}` : displayACampaign
	- `PUT /campaigns/{idCampaign}` : updateCampaign
	- `DELETE /campaigns/{idCampaign}` : deleteACampaign


- Rest Contact
	- `GET /contact/{idContact}/mail` : Get mail of contact


- Rest Management Monitoring Info
	- `POST /campaigns/{idCampaign}/management-monitoring-info` : addManagementMonitoringInfoInfosViaBatch
	- `POST /campaigns/{idCampaign}/management-monitoring-infos` : addManagementMonitoringInfoViaBatch
	- `GET /campaigns/{idCampaign}/survey-units/{idSu}/management-monitoring-infos` : displayManagementMonitoringInfoOfOneSurveyUnit
	- `GET /management-monitoring-info/{id}` : displayManagementMonitoringInfo
	- `DELETE /management-monitoring-infos/{id}` : deleteAManagementMonitoringInfo


- Rest Monitoring
	- `GET /campaigns/{idCampaign}/extraction` : provideDataForExtraction
	- `GET /campaigns/{idCampaign}/monitoring/follow-up` : getDataToFollowUp
	- `GET /campaigns/{idCampaign}/monitoring/progress` : getDataForProgress


- Rest Survey Unit
	- `POST /campaigns/{idCampaign}/survey-units` : initializeSurveyUnitsViaBatch
	- `GET /campaigns/{idCampaign}/survey-units/{idSu}` : getSurveyUnitByIdCampaign
	- `GET /campaigns/{idCampaign}/survey-units/follow-up` : displaySurveyUnitsToFollowUp
	- `GET /campaigns/survey-units` : displaySurveyUnitByFilter


- Rest Upload
	- `GET /campaigns/{idCampaign}/uploads` : displayAllUploads
	- `POST /campaigns/{idCampaign}/uploads` : addManagementMonitoringInfoViaUpload
	- `POST /campaigns/{idCampaign}/uploads/validation` : checkManagementMonitoringInfo
	- `DELETE /uploads/{id}` : deleteOneUpload

## Libraries used
- spring-boot-jpa
- spring-boot-security
- spring-boot-web
- spring-boot-tomcat
- spring-boot-test
- postgresql
- junit
- springfox-swagger2
- hibernate
- keycloak 

## Developers
- Benjamin Claudel (benjamin.claudel@keyconsulting.fr)
- Samuel Corcaud (samuel.corcaud@keyconsulting.fr)
- Paul Guillemet (paul.guillemet@keyconsulting.fr)

## License
Please check [LICENSE](https://github.com/InseeFr/Coleman-Back-Office/blob/master/LICENSE) file
