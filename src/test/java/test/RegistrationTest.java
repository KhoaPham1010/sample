package test;

import abstracts.AbstractUITest;
import entity.RegistrationForm;
import enums.Roles;
import fragment.NonDirectorPage;
import fragment.RegistrationPage;
import fragment.VerifyOTP;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Random;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.testng.Assert.*;

public class RegistrationTest extends AbstractUITest {

    RegistrationForm registrationForm;
    RegistrationPage registration;
    String registrationString = String.valueOf(System.currentTimeMillis());
    String mobileString = String.valueOf(1000000000 + new Random().nextInt(90000000));

    @BeforeClass(alwaysRun = true)
    public void initData() {
        String registrationString = String.valueOf(System.currentTimeMillis());
        registrationForm = new RegistrationForm()
            .withFullName("FullName " + registrationString)
            .withEmail(registrationString + "@gmail.com")
            .withCountry("Viet Nam (+84)")
            .withPhone(mobileString)
            .withRoles(Roles.EMPTY)
            .withHeardAbout("Facebook")
            .withReferralCode("1234");
    }

    @DataProvider(name = "editPermissionProvider")
    public Object[][] getAreaCodeAndExpectedResult() {
        return new Object[][] {
            {"Singapore (+65)", "You have successfully verified your mobile number. You’re on to a great start!"},
            {"Viet Nam (+84)", "You have successfully verified your email. You’re on to a great start!"},
        };
    }


    @Test(dataProvider = "getAreaCodeAndExpectedResult")
    public void checkRegistrationNewUserSuccessfully(String areaCode, String expectedResult){
        registration = initRegisterPage();
        VerifyOTP verifyOTP = new VerifyOTP(driver);

        registration.fillInRegistrationForm(getNewRegistrationForm()).setMobileNumber(areaCode, mobileString)
            .setRole(Roles.APPOINTED_DIRECTOR).agreeWithTermsAndConditions();

        assertFalse(registration.isContinueRegisterButtonDisable(), "Button Continue should be visible");
        registration.continueRegister();
        assertThat(verifyOTP.getVerifyOTPFormContentText(), containsString("Please enter the 4-digit OTP sent to"));
        verifyOTP.verifyOTPByMobile(asList("1", "2", "3", "4"));

        assertEquals(verifyOTP.getMessageSuccessfullyVerifiedText(), expectedResult);
    }

    @Test
    public void checkOTPMobileWithInvalidCode() {
        registration = initRegisterPage();
        VerifyOTP verifyOTP = new VerifyOTP(driver);

        registration.fillInRegistrationForm(getNewRegistrationForm()).agreeWithTermsAndConditions().continueRegister();

        assertFalse(registration.isContinueRegisterButtonDisable(), "Button Continue should be visible");
        registration.continueRegister();
        assertThat(verifyOTP.getVerifyOTPFormContentText(), containsString("Please enter the 4-digit OTP sent to"));
        verifyOTP.verifyOTPByMobile(asList("5", "5", "5", "5"));
        assertEquals(verifyOTP.getMessageInvalidOrExpiredText(), "The OTP you entered is invalid or expired.");

        verifyOTP.verifyOTPByMobile(asList("A", "B", "C", "D"));
        assertEquals(verifyOTP.getOTPCodesInputs(), asList("5", "5", "5", "5"));

        verifyOTP.resendPTO();
        assertThat(verifyOTP.getMessageOTPResendSuccessfulText(), containsString("A new OTP has been sent to"));
    }


    @Test
    public void checkRegistrationSuccessfullyWithAppointedAndNonDirector() {
        // Roles.NON_DIRECTOR
        String newEmail = System.currentTimeMillis() + "@gmail.com";
        String newMobile = String.valueOf(1000000000 + new Random().nextInt(90000000));
        registration = initRegisterPage();
        registration.fillInRegistrationForm(getNewRegistrationForm()).setMobileNumber("Viet Nam (+84)", newMobile)
            .enterEmail(newEmail).setRole(Roles.NON_DIRECTOR).agreeWithTermsAndConditions().continueRegister();

        NonDirectorPage nonDirectorPage = new NonDirectorPage(driver);
        assertEquals(nonDirectorPage.getInviteYourDirectorLabelText(),
            "Please invite your director to complete registration");

        // Roles.APPOINTED_DIRECTOR
        initRegisterPage();
        newEmail = System.currentTimeMillis() + "@gmail.com";
        newMobile = String.valueOf(1000000000 + new Random().nextInt(90000000));
        registration = new RegistrationPage(driver);
        registration.fillInRegistrationForm(getNewRegistrationForm()).setMobileNumber("Viet Nam (+84)", newMobile)
            .enterEmail(newEmail).setRole(Roles.APPOINTED_DIRECTOR).agreeWithTermsAndConditions().continueRegister();

        VerifyOTP verifyOTP = new VerifyOTP(driver);
        assertThat(verifyOTP.getVerifyOTPFormContentText(), containsString("Please enter the 4-digit OTP sent to"));
    }

    @Test
    public void checkButtonContinueIsDisableWithEmptyCase() {
        registration = initRegisterPage();
        registration.fillInRegistrationForm(registrationForm).setRole(Roles.EMPTY);
        registration.agreeWithTermsAndConditions();

        assertTrue(registration.isContinueRegisterButtonDisable(), "Button Continue should be disable");

        registration.setRole(Roles.APPOINTED_DIRECTOR).enterFullName("");
        assertTrue(registration.isContinueRegisterButtonDisable(), "Button Continue should be disable");

        registration.enterFullName("FullName " + registrationString).enterEmail("");
        assertTrue(registration.isContinueRegisterButtonDisable(), "Button Continue should be disable");

        registration.enterEmail(registrationString + "@gmail.com").setMobileNumber("Viet Nam (+84)", "");
        assertTrue(registration.isContinueRegisterButtonDisable(), "Button Continue should be disable");

        registration.setMobileNumber("Viet Nam (+84)", mobileString);
        registration.disAgreeWithTermsAndConditions();
        assertTrue(registration.isContinueRegisterButtonDisable(), "Button Continue should be disable");

        registration.disAgreeWithTermsAndConditions().enterFullName("").enterEmail("");
        assertTrue(registration.isContinueRegisterButtonDisable(), "Button Continue should be disable");
    }

    @Test
    public void checkRegistrationWithInvalidFullName() {
        registration = initRegisterPage();
        registration.enterFullName("1").clickLogo();
        assertEquals(registration.getFullNameLabelText(), "Full Name as per ID must be at least 2 characters.");

        // Bug -> 1 character + 1 space
        registration.enterFullName("1 ").clickLogo();
        assertEquals(registration.getFullNameLabelText(), "Full Name as per ID must be at least 2 characters.");

        // Bug -> input special character
        registration.enterFullName("%$::Ơ} {}").clickLogo();
        assertEquals(registration.getFullNameLabelText(), "Full Name as per ID must be not special characters.");

        // Bug -> input long 265 character
        registration.enterFullName("long characterlong characterlong characterlong characterlong characterlong " +
            "characterlong characterlong characterlong characterlong characterlong characterlong characterlong " +
            "characterlong characterlong characterlong characterlong characterlong characterlong character")
            .clickLogo();

        // Depending on the requirements of the business.
        assertEquals(registration.getFullNameLabelText(),
            "Full Name as per ID must be maximum 100 special characters.");
    }

    @Test
    public void checkRegistrationWithInvalidEmail() throws InterruptedException {
        registration = initRegisterPage();
        registration.fillInRegistrationForm(registrationForm).setRole(Roles.APPOINTED_DIRECTOR)
            .agreeWithTermsAndConditions();
        registration.enterEmail("123@gmail.com").continueRegister();

        RegistrationPage.AccountExistsPopup accountExistsPopup = new RegistrationPage.AccountExistsPopup(driver);
        assertThat(accountExistsPopup.getSubTitle(), containsString("Account exists, try login instead!"));

        registration.closePopup().enterEmail("email.@domain.com").clickLogo();
        assertEquals(registration.getEmailLabelText(), "Email address must be a valid email address.");

        // Bug -> input longer 60 character for email
        registration.enterEmail("longemaillongemaillongemaillongemaillongemaillongemaillongemaillongemail@gmail.com");
        // Depending on the requirements of the business.
        assertEquals(registration.getFullNameLabelText(),
            "Full Name as per ID must be maximum 60 characters.");
    }

    @Test
    public void checkRegistrationWithInvalidPhone() throws InterruptedException {
        registration = initRegisterPage();
        registration.fillInRegistrationForm(registrationForm).setRole(Roles.APPOINTED_DIRECTOR)
            .agreeWithTermsAndConditions();

        registration.setMobileNumber("Viet Nam (+84)", "1000205532").continueRegister();
        RegistrationPage.AccountExistsPopup accountExistsPopup = new RegistrationPage.AccountExistsPopup(driver);
        assertThat(accountExistsPopup.getSubTitle(), containsString("Account exists, try login instead!"));

        registration.closePopup().setMobileNumber("Viet Nam (+84)", "abcxyzgh").continueRegister();
        registration.waitMessageErrorDataInvalidVisible();
        assertEquals(registration.getPhoneLabelText(),
            "Incorrect phone format for phone., The phone format is invalid.");
    }

    @Test
    public void checkValueOfDropdownList_WhereDidYouHearAboutUs() {
        registration = initRegisterPage();
        registration.clickHeardAboutInput();
        assertEquals(registration.getListItemsHearAboutUsText(), asList("Referral", "Brochure", "Facebook", "Instagram",
            "Google", "LinkedIn", "Friends and relatives", "Online Articles", "Payment Invitation", "Brokers", "Others"));

        registration.chooseItems("Facebook");
        assertEquals(registration.getValueHeardAboutInput(), "Facebook");

        registration.clickHeardAboutInput().chooseItems("Google");
        assertEquals(registration.getValueHeardAboutInput(), "Google");
    }

    private RegistrationForm getNewRegistrationForm() {
        return new RegistrationForm()
            .withFullName("FullName " + System.currentTimeMillis())
            .withEmail(System.currentTimeMillis() + "@gmail.com")
            .withCountry("Viet Nam (+84)")
            .withPhone(String.valueOf(1000000000 + new Random().nextInt(90000000)))
            .withRoles(Roles.APPOINTED_DIRECTOR)
            .withHeardAbout("Facebook")
            .withReferralCode("1234");
    }
}
