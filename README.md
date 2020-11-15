# 2fa


A repo for storing 2fa for multiple different programming languages.

See Makefile for run different commands like
* start nodejs server

## NodeJs

Uses express, speakeasy and uuid to implement the 2fa with speakeasy to use google authenticator.<br/>
To test it run the command: make run-nodejs and call localhost:3000
### POST /api/register:
Used for registration. Returns the base64 encoded string for the qrCode.

### POST /api/verify:
Use this to verify the mfa so that the tempsecret is stored permanently (only on registration)
use this body:
{
    "userId": "USERID from Creation",
    "token": "Code from app"
}

### POST /api/validate:
Use this to validate the mfa on login
use this body:
{
    "userId": "USERID from Creation",
    "token": "Code from app"
}



## Spring
Uses totp (https://mvnrepository.com/artifact/dev.samstevens.totp/totp-spring-boot-starter)
To test it run the command: make run-spring and call localhost:8080

### GET /mfa/setup
Returns the QRCode

### POST /mfa/verify:
use this body:
{
    "token": "Code from app",
    "secret": "FILL IN SECRET (In reallife parameter should be a userId or something)"
}