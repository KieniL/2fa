# 2fa


A repo for storing 2fa for multiple different programming languages.

See Makefile for run different commands like
* start nodejs server

## NodeJs

Uses express, speakeasy and uuid to implement the 2fa with speakeasy to use google authenticator

### /api/register:
Used for registration. Returns the base64 encoded string for the qrCode.

### /api/verify:
Use this to verify the mfa so that the tempsecret is stored permanently (only on registration)


### /api/validate:
Use this to validate the mfa on login