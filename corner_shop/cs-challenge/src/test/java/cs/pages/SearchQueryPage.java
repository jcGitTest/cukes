package cs.pages;

import cs.util.TestConf;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class SearchQueryPage {

    private WebDriver driver;
    private static final TestConf TEST_CONF = TestConf.getConfInstance();

    @FindBy(css = "input[name=q]")
    WebElement query;

    public SearchQueryPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(
                driver, TEST_CONF.getAjaxWaitSeconds()), this);
    }

    public static SearchQueryPage loadUsing(WebDriver driver){
        driver.get(TEST_CONF.getSearchUrl());
        return new SearchQueryPage(driver);
    }

    public SearchQueryPage setQuery(String term){
        query.clear();
        query.sendKeys(term);
        return this;
    }

    public SearchResultsPage pressEnterInQuery(){
        query.sendKeys(Keys.ENTER);
        return new SearchResultsPage(driver);
    }
}
