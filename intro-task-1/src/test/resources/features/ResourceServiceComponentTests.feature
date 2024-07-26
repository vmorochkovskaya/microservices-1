Feature: Resource Service

  Scenario: Successfully uploading a resource
    Given the file is uploaded via the postResource endpoint
    Then the file should be stored in the database
    And a Kafka message should be sent

  Scenario: Retrieving an existing resource
    Given an existing resource in the database
    When the resource is requested via the getResource endpoint
    Then the correct resource data should be returned

  Scenario: Deleting multiple resources
    Given multiple existing resources in the database
    When the resources are deleted via the deleteResources endpoint
    Then the resources should be removed from the database