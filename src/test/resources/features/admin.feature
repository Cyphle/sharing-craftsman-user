#Feature: Registration
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
#- Get list of authorizations
#- Add/remove authorizations (roles and groups)

#  Background: Setup application
#    Given The application is setup
#    And A client <sharingcraftsman> is registered
#
#  Scenario: Register to the application
#    Given I register to the application with my credentials <john@doe.fr> and password <helloworld>
#    When I connect to the application with my credentials <john@doe.fr> and password <helloworld>
#    And I refresh my token
#    Then I have a new token