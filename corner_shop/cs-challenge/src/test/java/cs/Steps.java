package cs;

import cs.pages.SearchQueryPage;
import cs.pages.SearchResultsPage;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


import java.sql.*;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class Steps {

    private WebDriver driver;
    private Object currentPage;
    private String term;
    private static final Logger LOGGER = Logger.getLogger(Steps.class.getName());

    private static final String URL_CONN = "jdbc:mysql://localhost:3305/explorecalifornia";

    @Before({"@requires_browser"})
    public void buildDriver(){
        driver = new FirefoxDriver();
    }

    @After({"@requires_browser"})
    public void destroyDriver(){
        driver.quit();
    }

    @Given("^A Google Search page$")
    public void a_Google_Search_page() throws Throwable {
        currentPage = SearchQueryPage.loadUsing(driver);
        verifyCurrentPage(SearchQueryPage.class);
    }

    @When("^I enter the search term \"(.*?)\"$")
    public void i_enter_the_search_term(String term) throws Throwable {
        this.term =  term;
        currentPage = ((SearchQueryPage) currentPage).setQuery(term);
        verifyCurrentPage(SearchQueryPage.class);
    }

    @When("^I submit the search by pressing \"(.*?)\"$")
    public void i_submit_the_search_by_pressing(String method) throws Throwable {
        verifyCurrentPage(SearchQueryPage.class);
        switch (method.toLowerCase()){
            case "enter":
                currentPage = ((SearchQueryPage) currentPage).pressEnterInQuery();
                LOGGER.info(String.format("Selected method %s : ", method.toLowerCase()));
                break;
                default:
                    throw new RuntimeException("method for query submission not available. ");
        }
    }

    @Then("^the search result page title should contain the search term$")
    public void the_search_result_page_title_should_contain_the_search_term() throws Throwable {
        verifyCurrentPage(SearchResultsPage.class);
        String title = ((SearchResultsPage) currentPage).getTermFromTitle();
        assertTrue(title.contains(term));
    }

    @Given("^I have a SQL connection$")
    public void i_have_a_SQL_connection() {
        Connection conn = null;
        Statement stmt  = null;
        ResultSet rs =  null;

        try {
            conn = DriverManager.getConnection(URL_CONN, "dbuser", "dbpassword");
            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT * FROM Explorers;");
            while (rs.next()){
                System.out.println(rs.getString("firstName"));
            }
            LOGGER.info("Connected!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private void verifyCurrentPage(Class pageClass){
        if(!currentPage.getClass().getSimpleName().equals(pageClass.getSimpleName())){
            fail(
                    String.format("Expected current page to have type %s - actual type %s",
                            pageClass.getSimpleName(),
                            currentPage.getClass().getSimpleName())
            );
        }
    }
}
