package fragment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import static utils.ElementUtils.*;

public class VerifyOTP {

    private static By ICON_LOADING = By.cssSelector(".q-inner-loading");

    WebDriver driver;

    @FindBy(className = "verify-otp-form__content")
    private WebElement verifyOTPFormContent;

    @FindBy(css = ".digit-input__input.cursor-pointer")
    private List<WebElement> OTPInput;

    @FindBy(className = "aspire-cta-screen__content")
    private WebElement messageSuccessfullyVerified;

    @FindBy(className = "aspire-label__text")
    private WebElement messageInvalidOrExpired;

    @FindBy(css = ".q-btn__content span")
    private WebElement resendOTP;

    @FindBy(className = "q-notification__message")
    private WebElement messageOTPResendSuccessful;

    @FindBy(className = "q-spinner q-mb-lg text-primary")
    private WebElement iconLoading;

    public VerifyOTP(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getVerifyOTPFormContentText() {
        return waitForElementVisible(verifyOTPFormContent, driver).getText();
    }

    public void verifyOTPByMobile(List<String> otpNumbers) {
        try{
            waitForElementVisible(ICON_LOADING, driver);
            waitForElementNotVisible(ICON_LOADING, driver);
        } catch (Exception e){
            // Do nothing
        }

        waitForElementVisible(OTPInput.get(0), driver);
        for (int i = 0; i < otpNumbers.size(); i++) {
            waitForElementVisible(OTPInput.get(i), driver).click();
            new Actions(driver).sendKeys(otpNumbers.get(i)).perform();
        }
    }

    public List<String> getOTPCodesInputs() {
        return getElementTexts(OTPInput);
    }

    public String getMessageSuccessfullyVerifiedText() {
        return waitForElementVisible(messageSuccessfullyVerified, driver).getText();
    }

    public String getMessageInvalidOrExpiredText() {
        return waitForElementVisible(messageInvalidOrExpired, driver).getText();
    }

    public String getMessageOTPResendSuccessfulText() {
        return waitForElementVisible(messageOTPResendSuccessful, driver).getText();
    }

    public VerifyOTP resendPTO() {
        waitForElementVisible(resendOTP, driver).click();
        return this;
    }
}
