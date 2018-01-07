# Sharing Craftsman User

User microservice based on OAuth2 authentication schema. (Simplified schema without scope notion)

## Role
- Authenticate userEntities
- Send user details
- Deliver token
- Verify token validity
- Renew token

## Tech
Spring OAuth2

## Requests flows
TO DO

Partie authentication: OAuth2 avec comptes Github ou autre (envoie de mail de proposition d'inscription avec token JWT)
Partie autorisations: Roles gérés en interne
Autres services feront références aux user avec leur rôle et l'id du user

See Spring data rest


Try:
- curl acme:acmesecret@localhost:8080/oauth/token -d grant_type=client_credentials
  -> To get client access token from self hosted authorization server
- curl acme:acmesecret@localhost:8080/oauth/token -d grant_type=password -d username=user -d password=... (see launch logs to get the password)


- Get github token
- Save user infos
- Save user token
- Save validity date (???)

- Each client will send to this app the token to get user infos
- Each app will send with each request, its own id and secret to verify that is a sharing craftsman app

- This app needs a page to register manually clients

- Add permitAll route /getUserDetails to allow other applications to get user infos