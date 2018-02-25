# Sharing Craftsman User

User microservice

## Role
- Register
- Update profile
- Get authorizations
- Get access token (login & refresh token)
- Logout
- Upload profile picture
- Admin functionalities (manage users & authorizations)

## Techs
- Spring
- Liquibase
- Jenkins
- Docker

## Notes
In UserController:requestLostPassword, note that this is an unsecured version that sends a change password token as response but should send it in a mail. This is only for a demo to not bother with mail management