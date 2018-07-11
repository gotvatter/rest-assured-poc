Feature: Perform rest request

@Run
  Scenario: RST-001 Verify GET response with customized validation
    Given schema validation is customized
    When user sends 'GET' request for id number '2'
    Then  response is correctly validated against schema
    And response is retrieved and validated


  Scenario: RST-002 Verify DELETE response with customized validation
    Given schema validation is customized
    When user sends 'DELETE' request for id number '2'
    Then returned response code is '200'
