@E2E
Feature: Resource Processing

  Scenario Outline: Process resource and create song entry
    Given a new resource "<fileName>" is posted to the Resource Service
    Then the Resource Service should return <statusCode>

    Examples:
      | fileName                       | statusCode |
      | sample.mp3                     | 200        |
