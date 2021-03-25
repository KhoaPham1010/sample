package enums;

public enum Roles {
    APPOINTED_DIRECTOR("div[aria-label=\"Appointed director\"]"),
    NON_DIRECTOR("div[aria-label=\"Non-director\"]"),
    EMPTY("");

    private String roles;

    Roles(String roles) {
        this.roles = roles;
    }

    public String toString() {
        return roles;
    }
}