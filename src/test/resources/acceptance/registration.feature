Feature: Registration

  Background: Setup application
    Given The application is setup

  Scenario: Register to the application
    Given I register to the application with my credentials <john@doe.fr> and password <helloworld>
    When I connect to the application with my credentials <john@doe.fr> and password <helloworld>
    Then I am connected