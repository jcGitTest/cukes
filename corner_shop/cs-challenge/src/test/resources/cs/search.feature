@requires_browser
Feature: Enter a search in Google
  Scenario: Search results should display the search term
    Given A Google Search page
    When I enter the search term "cats"
    And I submit the search by pressing "enter"
    Then the search result page title should contain the search term

  Scenario: Search results should display the search term in the page title when pressing "search"
    Given A Google Search page
    When I enter the search term "dogs"
    And I submit the search by pressing "enter"
    Then the search result page title should contain the search term

  Scenario Outline: Search results should display the search term in the page title when pressing "search"
    Given A Google Search page
    When I enter the search term "<search_term>"
    And I submit the search by pressing "<submission_method>"
    Then the search result page title should contain the search term

    Examples:
      | search_term  | submission_method |
      | cats         | enter             |
      | dogs         | enter             |

