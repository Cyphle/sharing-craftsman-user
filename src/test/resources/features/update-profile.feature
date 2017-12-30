Feature: Registration

  Background: Setup application
    Given The application is initialized with client
    And I have a connected account with credentials <john@doe.fr> and <password>

  Scenario: Update profile
    When I update my account with firstname <John>, lastname <Doe>, email <john@doe.fr>, website <www.johndoe.fr>, github <github.com/johndoe>, linkedin <linkedin.com/johndoe>
    Then I have my profile updated