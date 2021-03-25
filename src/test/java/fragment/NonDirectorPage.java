package fragment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static utils.ElementUtils.waitForElementVisible;

public class NonDirectorPage {

    WebDriver driver;

    @FindBy(className = "aspire-cta-screen__title")
    private WebElement inviteYourDirectorLabel;

    public NonDirectorPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getInviteYourDirectorLabelText() {
        return waitForElementVisible(inviteYourDirectorLabel, driver).getText();
    }
}
