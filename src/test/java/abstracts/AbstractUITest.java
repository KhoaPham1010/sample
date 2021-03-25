package abstracts;

import fragment.RegistrationPage;
import utils.WebdriverInstance;

public class AbstractUITest extends WebdriverInstance {

    public RegistrationPage initRegisterPage() {
        openUrl(registerUrl);
        return new RegistrationPage(driver);
    }
}
