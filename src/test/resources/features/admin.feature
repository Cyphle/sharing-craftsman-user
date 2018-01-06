Feature: Admin functionalities

  Background: Setup application
    Given The client <sharingcraftsman> is registered
    And An admin group is created with admin role
    And I have registered an admin account with username <admin@scuser.fr> and password <password>
    And I am connected with my account <admin@scuser.fr> and password <password>

  Scenario: Create, delete and consult list of available roles and groups
    Given I create the following authorizations
      | Group  | Role       |
      | ADMINS | ROLE_USER  |
      | USERS  | ROLE_USER  |
      | GOD    | ROLE_ROOT  |
      | GOD    | ROLE_ADMIN |
      | GOD    | ROLE_USER  |
    When I delete the role <ROLE_USER> from the group <GOD>
    And I consult all the groups and roles
    Then I get the roles
      | Group  | Role       |
      | ADMINS | ROLE_USER  |
      | ADMINS | ROLE_ADMIN |
      | USERS  | ROLE_USER  |
      | GOD    | ROLE_ROOT  |
      | GOD    | ROLE_ADMIN |

#
#  Initialize application by adding roles and group (use autowired rolerepo
#  Add admin (use userrepo and grouprepo)
#
#  - Get all collaborators
#  - Delete one
#- Deactivate user + Modify user (send all user except authorizations)
#- Modify user ?
#- Add user
#- Get list of roles, groups
#- Add/remove roles groups (impact on user groups)
#- Get list of authorizations -> OK
#- Add/remove authorizations (roles and groups) -> OK

#  Background: Setup application
#    Given The application is setup
#    And A client <sharingcraftsman> is registered
#
#  Scenario: Register to the application
#    Given I register to the application with my credentials <john@doe.fr> and password <helloworld>
#    When I connect to the application with my credentials <john@doe.fr> and password <helloworld>
#    And I refresh my token
#    Then I have a new token