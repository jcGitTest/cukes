package cs.pages;

import cs.util.TestConf;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.logging.Logger;

public class SearchResultsPage {

    private static final TestConf BASE_CONF = TestConf.getConfInstance();
    private static final Logger LOGGER = Logger.getLogger(SearchResultsPage.class.getName());
    private WebDriver driver;

    public SearchResultsPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, BASE_CONF.getAjaxWaitSeconds()), this);
        new WebDriverWait(driver, BASE_CONF.getAjaxWaitSeconds())
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#search li")));
    }

    public String getTermFromTitle(){
        String title = driver.getTitle();
        LOGGER.info(title);
        return title.substring(0, title.indexOf(" - "));
    }
}
