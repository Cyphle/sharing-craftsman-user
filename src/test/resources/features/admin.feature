#Feature: Registration
#
#  Initialize application by adding roles and group (use autowired rolerepo
#  Add admin (use userrepo and grouprepo)
#
#  - Get all collaborators
#  - Delete one
#  Background: Setup application
#    Given The application is setup
#    And A client <sharingcraftsman> is registered
#
#  Scenario: Register to the application
#    Given I register to the application with my credentials <john@doe.fr> and password <helloworld>
#    When I connect to the application with my credentials <john@doe.fr> and password <helloworld>
#    And I refresh my token
#    Then I have a new token