# Sharing Craftsman User

User microservice based on OAuth2 authentication schema. (Simplified schema without scope notion)

## Role
- Authenticate users
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
