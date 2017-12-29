#Feature: Change password
#
#  Background: Setup application
#    Given The application is setup
#    And A client <sharingcraftsman> is registered
#
#  Scenario: Change my password
#    Given I register to the application with my credentials <john@doe.fr> and password <helloworld>
#    When I ask to change my password
#    And I change my password
#    Then I can reconnect with my new password