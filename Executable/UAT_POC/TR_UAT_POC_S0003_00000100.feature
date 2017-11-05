#Author: seangriffin@internode.on.net
Feature: Trip Planner

  Background: UAT release 1.1 and STAGING test environment exists.
    
  Scenario: A trip request can be executed and results returned
    Given this is the first scenario
    And I get the "Trip Request" data with comment "Mark" for scenario "TR_UAT_POC_S0003"
		When "Mark" executes a trip plan
		Then a list of trips should be provided
		And close transportnsw
		