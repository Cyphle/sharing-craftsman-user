Feature: Features of my service

  Background: Setup application
    Given I setup application

  Scenario: Calling my service
    Given my service exists
    When I call my service
    Then it should have been a success