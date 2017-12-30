#Feature: Change password
#
#  - Should have for user
#    -> GROUP_USERS
#        -> Containing ROLE_USER
#  - Should have for admin
#    -> GROUP_ADMINS
#        -> Containing ROLE_ADMIN, ROLE_USER


#  Background: Setup application
#    Given The application is setup
#    And A client <sharingcraftsman> is registered
#
#  Scenario: Change my password
#    Given I register to the application with my credentials <john@doe.fr> and password <helloworld>
#    And I connect to the application with my credentials <john@doe.fr> and password <helloworld>
#    When I ask to change my password
#    And I change my password with new password <newpassword>
#    Then I connect to the application with my credentials <john@doe.fr> and password <newpassword>
#    And I am connected