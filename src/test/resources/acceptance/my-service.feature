Feature: Features of my service

  Background: Initialize application
    Given I go on the application

  Scenario: Calling my service
    Given my service exists
    When I call my service
    Then it should have been a success