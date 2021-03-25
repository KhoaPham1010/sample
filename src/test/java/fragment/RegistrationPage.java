package fragment;

import entity.RegistrationForm;
import enums.Roles;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static utils.ElementUtils.*;

public class RegistrationPage {

    private WebDriver driver;

    @FindBy(css = "input[name='full_name']")
    private WebElement fullNameInput;

    @FindBy(css = "div[name='full_name'] .aspire-label")
    private WebElement fullNameLabel;

    @FindBy(css = "input[name='email']")
    private WebElement emailInput;

    @FindBy(css = "div[name='email'] .aspire-label")
    private WebElement emailLabel;

    @FindBy(css = "input[name='phone']")
    private WebElement phoneInput;

    @FindBy(css = "div[name='phone'] .aspire-label")
    private WebElement phoneLabel;

    @FindBy(className = "flag-select__icon")
    private WebElement flagMobileCountry;

    @FindBy(css = "input[data-cy=\"register-person-heard-about\"]")
    private WebElement heardAboutInput;

    @FindBy(css = "input[data-cy=\"register-person-referral-code\"]")
    private WebElement referralCodeInput;

    @FindBy(css = ".register-step-person__privacy .q-checkbox")
    private WebElement agreeWithTermsAndConditions;

    @FindBy(className = "q-btn-item")
    private WebElement continueButton;

    @FindBy(className = "verify-otp-form__content")
    private WebElement verifyOTPHeader;

    @FindBy(className = "auth-desktop-sidebar__logo")
    private WebElement logo;

    @FindBy(css = ".q-card img")
    private WebElement closePopup;

    @FindBy(className = "q-notification--standard")
    private WebElement messageErrorDataInvalid;

    @FindBy(className = "q-item__label")
    private List<WebElement> itemsHearAboutUs;

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public RegistrationPage setMobileNumber(String country, String number){
        waitForElementVisible(flagMobileCountry, driver).click();
        new Actions(driver).sendKeys(country).perform();
        By itemXpath = By.xpath(format("//div[@class='q-item__label'][.='%s']", country));
        waitForElementVisible(itemXpath, driver).click();
        waitForElementVisible(phoneInput, driver).click();
        clearInput(phoneInput);
        waitForElementVisible(phoneInput, driver).sendKeys(number);
        return this;
    }

    public List<String> getListItemsHearAboutUsText() {
        return getElementTexts(itemsHearAboutUs);
    }

    public RegistrationPage setRole(Roles role) {
        if (role.equals(Roles.EMPTY)) {
            return this;
        }
        waitForElementClickable(By.cssSelector(role.toString()), driver).click();
        return this;
    }

    public RegistrationPage enterEmail(String email) {
        enterData(emailInput, email);
        return this;
    }

    public RegistrationPage enterFullName(String fullName) {
        enterData(fullNameInput, fullName);
        return this;
    }

    public String getFullNameLabelText() {
        return waitForElementVisible(fullNameLabel, driver).getText();
    }

    public String getEmailLabelText() {
        return waitForElementVisible(emailLabel, driver).getText();
    }

    public String getPhoneLabelText() {
        return waitForElementVisible(phoneLabel, driver).getText();
    }

    public RegistrationPage agreeWithTermsAndConditions() {
        if (!isAgreeWithTermsAndConditions()) {
            waitForElementVisible(agreeWithTermsAndConditions, driver).click();
        }
        return this;
    }

    public RegistrationPage disAgreeWithTermsAndConditions() {
        if (isAgreeWithTermsAndConditions()) {
            waitForElementVisible(agreeWithTermsAndConditions, driver).click();
        }
        return this;
    }

    private Boolean isAgreeWithTermsAndConditions() {
        return waitForElementVisible(agreeWithTermsAndConditions, driver).getAttribute("aria-checked").contains("true");
    }

    public RegistrationPage continueRegister() {
        waitForElementVisible(continueButton, driver).click();
        return this;
    }

    public boolean isContinueRegisterButtonDisable() {
        return waitForElementVisible(continueButton, driver).getAttribute("class").contains("disable");
    }

    public RegistrationPage closePopup() throws InterruptedException {
        waitForElementVisible(closePopup, driver).click();
        Thread.sleep(1000);
        return this;
    }

    public void waitMessageErrorDataInvalidVisible() {
        try {
            waitForElementVisible(messageErrorDataInvalid, driver);
            waitForElementNotVisible(messageErrorDataInvalid, driver);
        } catch (TimeoutException e) { }
    }

    public RegistrationPage fillInRegistrationForm(RegistrationForm registrationForm) {
        return enterData(fullNameInput, registrationForm.getFullName())
            .enterData(emailInput, registrationForm.getEmail())
            .setMobileNumber(registrationForm.getCountry(), registrationForm.getPhone())
            .setRole(registrationForm.getRoles())
            .enterData(heardAboutInput, registrationForm.getHeardAbout())
            .enterData(referralCodeInput, registrationForm.getReferralCode())
            .disAgreeWithTermsAndConditions();
    }

    public RegistrationPage clickHeardAboutInput() {
        waitForElementVisible(heardAboutInput, driver).click();
        return this;
    }

    public String getValueHeardAboutInput() {
        return waitForElementVisible(heardAboutInput, driver)
            .findElement(By.xpath("//ancestor::div[@label=\"Where did you hear about us?\"]")).getAttribute("value");
    }


    public RegistrationPage clickLogo() {
        waitForElementVisible(logo, driver).click();
        return this;
    }

    public RegistrationPage chooseItems(final String item) {
        itemsHearAboutUs.stream()
            .filter(input -> item.equals(input.getText()))
            .findFirst()
            .get().click();
        return this;
    }

    private RegistrationPage enterData(WebElement input, String data) {
        if(Objects.isNull(data)){
            return this;
        }
        waitForElementClickable(input, driver).click();
        clearInput(input);
        input.sendKeys(data, Keys.ENTER);
        return this;
    }

    private void clearInput(WebElement input) {
        int length = input.getAttribute("value").length();
        for (int i = 0; i <= length; i++) {
            input.sendKeys(Keys.BACK_SPACE);
        }
    }

    public static class AccountExistsPopup {

        WebDriver driver;

        @FindBy(css = "div[class^='text-subtitle']")
        private WebElement subTitle;

        public final By LOCATOR = By.className("q-card");

        public AccountExistsPopup(WebDriver driver) {
            this.driver = driver;
            PageFactory.initElements(driver, this);
        }

        public String getSubTitle() {
            return waitForElementVisible(subTitle, driver).getText();
        }
    }
}
