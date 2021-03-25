package entity;

import enums.Roles;

public class RegistrationForm {

    private String fullName;
    private String email;
    private String country;
    private String phone;
    private Roles role;
    private String heardAbout;
    private String referralCode;
    private Boolean agreeTerms;

    public RegistrationForm() {
        this.fullName = "";
        this.email = "";
        this.country = "";
        this.phone = "";
        this.role = Roles.APPOINTED_DIRECTOR;
        this.heardAbout = "";
        this.referralCode = "";
        this.agreeTerms = false;
    }

    public RegistrationForm withFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public RegistrationForm withEmail(String email){
        this.email = email;
        return this;
    }

    public RegistrationForm withCountry(String country) {
        this.country = country;
        return this;
    }


    public RegistrationForm withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public RegistrationForm withRoles(Roles role) {
        this.role = role;
        return this;
    }

    public RegistrationForm withHeardAbout(String heardAbout) {
        this.heardAbout = heardAbout;
        return this;
    }

    public RegistrationForm withReferralCode(String referralCode) {
        this.referralCode = referralCode;
        return this;
    }

    public RegistrationForm withAgreeTerms(Boolean isAgree) {
        this.agreeTerms = isAgree;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    public Roles getRoles() {
        return role;
    }

    public String getHeardAbout() {
        return heardAbout;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public Boolean getAgreeTerms() {
        return agreeTerms;
    }
}
