
## Code.Hub | Pfizer - Software & Cloud Engineering Bootcamp
 
  
# Sacchon 
 
## Project scope  
 
The project aim is to develop the Sacchon app and deliver it ready to be 
released. The requirements are given by the Sacchon Consulting Enterprise 
(a fictional company).  

Register
-----------
Request: POST examplehost.com:9000/v1/

JSON body:

{
    "username" : "username",
    "password":"password"
}


Login
-----------
Request: POST examplehost.com:9000/v1/login

JSON body:

{
    "username":"username",
    "password":"password"
}

The server will return a JWT token.

Authenticate
-----------
To Authenticate simply add to your Headers the bellow Authorization:
"Authorization: BEARER {jwt}"
 
