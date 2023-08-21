Feature: user Credentials
  Scenario: validating credentials of user.
    Given : Execute userLogin api
    Then : validate method invoked to verify given credentials and user verified and returned status OK