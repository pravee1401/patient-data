
This is a Restful service managing the Patient Entities

Operations/API Specifications

Fetch all Patients data

    GET /api/v1/patients HTTP/1.1
    Host: localhost:8080
    Content-Type: application/json

Fetch all Female Patients data

    GET /api/v1/patients/female HTTP/1.1
    Host: localhost:8080
    Content-Type: application/json

Fetch Patients data by ID

    GET /api/v1/patients/1 HTTP/1.1
    Host: localhost:8080
    Content-Type: application/json

Fetch Patients data by First and Last Name

    GET /api/v1/patients/byName?firstName=Christina&lastName=Schneider HTTP/1.1
    Host: localhost:8080
    Content-Type: application/json

Create Patient

    POST /api/v1/patients HTTP/1.1
    Host: localhost:8080
    Content-Type: application/json
    
    {
        "id":null,
        "firstName":"Christina",
        "lastName":"Schneider",
        "gender":"FEMALE",
        "birthDay":"2002-06-18"
    }

Delete Patient

    DELETE /api/v1/patients/1 HTTP/1.1
    Host: localhost:8080
    Content-Type: application/json

Other Features

    - Supports purging/deleting of patient records that are created older than 1 year before (configurable)
    - Unit and Integration tests
    - Uses Inmemory database for database operations
    - Uses Custom validator for verifying the validity of patient's age

Improvements

    - Could use actual database instead of in memory database
    - If actual database is used then testcontainers for could be used for integration tests, which run test in docker containers
    - Audit attributes for ex: createdOn etc., could be seperated out from the actual entity class to a super class, so that it could be used in multiple entities
    - Tests could be written for services
