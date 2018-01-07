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

  Scenario: Create, delete, update users
    Given I create the following users
      | Username    | Firstname | Lastname | Email           | Website             | Github                 | Linkedin                 |
      | john@doe.fr | John      | Doe      | john@doe.fr     | www.johndoe.fr      | github.com/johndoe     | linkedin.com/johndoe     |
      | misterhello | Mister    | Hello    | mister@hello.fr | www.misterhello.com | github.com/misterhello | linkedin.com/misterhello |
      | foo@bar.com | Foo       | Bar      | foo@bar.com     | www.foobar.de       | github.com/foobar      | linkedin.com/foobar      |
    And I create the following authorizations
      | Group | Role      |
      | USERS | ROLE_USER |
    When I update users
      | Username    | Firstname | Lastname | Email              | Website        | Github             | Linkedin             |
      | john@doe.fr | John      | Doe      | johndoe@thebest.eu | www.johndoe.eu | github.com/johndoe | linkedin.com/johndoe |
    And I delete user <misterhello>
    And I consult all users profiles
    Then I get the profiles
      | Username    | Firstname | Lastname | Email              | Website        | Github             | Linkedin             | isActive | Authorizations | Roles     |
      | john@doe.fr | John      | Doe      | johndoe@thebest.eu | www.johndoe.eu | github.com/johndoe | linkedin.com/johndoe | true     | USERS          | ROLE_USER |
      | foo@bar.com | Foo       | Bar      | foo@bar.com        | www.foobar.de  | github.com/foobar  | linkedin.com/foobar  | true     | USERS          | ROLE_USER |

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