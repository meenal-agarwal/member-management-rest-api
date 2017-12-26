# Membe-gement-rest-api REST API 


The purpose of this project is to create a RESTfull API for Member Services. 

The API exposes following functionalities (CRUD operation) :
  1. `GET /membserService/member/{dob}/{lastName}`
  2. `PUT /memberService/member`
  3. `POST /memberService/update/{dob}/{lastName}`
  4. `DELETE /memberService/delete/{dob}/{lastName}`
  5. `GET /memberService/memberList`

Description
1. Read an existing member
2. Create a new member
3. Update an existing member
4. Delete member(s) which are no longer used
5. List existing members

This project is built using [SpringBoot](), [Java 1.8]() and [Maven](). It accepts JSON and replies with JSON along with StatusCodes and ErrorMessages wherever required. 

Unit and Integration tests using [Mockito]() framework are also included.

#### How to run

```bash
$ git clone https://github.com/meenal-agarwal/member-management-rest-api.git 
$ cd ./member-management-rest-api
$ mvn spring-boot:run
```

#### How to use

##### Creating a new Member

###### Valid Request
```bash
$ curl -X PUT -H 'Content-Type: application/json' -d '{"firstName": "John", "lastName": "Snow", "dob" : "1990.02.01", "postalCode" : 12345}' 'localhost:8080/memberService/member'
  {"code":201,"status":"Created"}
```
###### Member Already Added
```bash
$ curl -X PUT -H 'Content-Type: application/json' -d '{"firstName": "John", "lastName": "Snow", "dob" : "1990.02.01", "postalCode" : 12345}' 'localhost:8080/memberService/member'
  {"status":"ALREADY_REPORTED","message":"Member already available"}
```
###### Date Format Invalid
```bash
$ curl -X PUT -H 'Content-Type: application/json' -d '{"firstName": "Micheal", "lastName": "Mathew", "dob" : "2014.13.01", "postalCode" : 10045}' 'localhost:8080/memberService/member'
{"status":"NOT_ACCEPTABLE","message":"DOB Format should be yyyy.MM.dd"}
```
###### Valid Request
```bash
$ curl -X PUT -H 'Content-Type: application/json' -d '{"firstName": "Micheal", "lastName": "Mathew", "dob" : "2014.02.01", "postalCode" : 10045}' 'localhost:8080/memberService/member'
  {"code":201,"status":"Created"}
```


##### Read an existing member

###### Valid Request
```bash
$ curl -X GET localhost:8080/memberService/member/2014.02.01/Mathew
{"firstName":"Micheal","lastName":"Mathew","dob":"2014.02.01","postalCode":10045}
```

###### Valid Request
```bash
curl -X GET localhost:8080/memberService/member/1990.02.01/Snow
{"firstName":"John","lastName":"Snow","dob":"1990.02.01","postalCode":12345}
```

###### Member Not Found
```bash
curl -X GET localhost:8080/memberService/member/1990.02.02/Micheal
{"status":"NOT_FOUND","message":"Member is not available"}
```

###### Invalid Date Format
```bash
 curl -X GET localhost:8080/memberService/member/1990.13.02/Micheal
{"status":"NOT_ACCEPTABLE","message":"DOB Format should be yyyy.MM.dd"}
```


##### Update an existing member

###### Valid Request
```bash
$ curl -X POST -H 'Content-Type: application/json' -d '{"firstName": "Mark", "lastName": "Mathew", "dob" : "2015.07.01", "postalCode" : 10046}' 'localhost:8080/memberService/update/2014.02.01/Mathew'
{"code":200,"status":"OK"}
```

###### Invalid Date format
```bash
$ curl -X POST -H 'Content-Type: application/json' -d '{"firstName": "Mark", "lastName": "Mathew", "dob" : "2015.07.01", "postalCode" : 10046}' 'localhost:8080/memberService/update/2014.13.01/Mathew'
{"status":"NOT_ACCEPTABLE","message":"DOB Format should be yyyy.MM.dd"}
```

###### Requested Member not found
```bash
$ curl -X POST -H 'Content-Type: application/json' -d '{"firstName": "Mark", "lastName": "Mathew", "dob" : "2015.07.01", "postalCode" : 10046}' 'localhost:8080/memberService/update/1990.02.01/Bill'
{"status":"NOT_FOUND","message":"Member is not available"}
```

###### Invalid date format in new data
```bash
$ curl -X POST -H 'Content-Type: application/json' -d '{"firstName": "Mark", "lastName": "Mathew", "dob" : "2015.13.01", "postalCode" : 10046}' 'localhost:8080/memberService/update/2014.02.01/Mathew'
{"status":"NOT_ACCEPTABLE","message":"DOB Format should be yyyy.MM.dd"}
```

##### Delete member(s) which are no longer used

###### Valid Request
```bash
$ curl -X DELETE localhost:8080/memberService/delete/1990.02.01/Snow
{"code":200,"status":"OK"}
```

###### Valid Request
```bash
$ curl -X DELETE localhost:8080/memberService/delete/2015.07.01/Mathew
{"code":200,"status":"OK"}
```

###### Invalid Date format
```bash
$ curl -X DELETE localhost:8080/memberService/delete/03.1988.02/Mark
{"status":"NOT_ACCEPTABLE","message":"DOB Format should be yyyy.MM.dd"}
```

###### Member not found
```bash
$ curl -X DELETE localhost:8080/memberService/delete/2016.01.02/Mark
{"status":"NOT_FOUND","message":"Member is not available"}
```


##### List existing members

###### Valid Request
```bash
$ curl -X GET localhost:8080/memberService/memberList
[{"firstName":"John","lastName":"Snow","dob":"1990.02.01","postalCode":12345},{"firstName":"Mark","lastName":"Mathew","dob":"2015.07.01","postalCode":10046}]
```

###### Valid Request : No Member Present
```bash
$ curl -X GET localhost:8080/memberService/memberList
{"status":"NOT_FOUND","message":"No Member Present"}
```
