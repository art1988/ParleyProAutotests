package com.parley.testing.model.roles;

import org.openqa.selenium.WebElement;

import java.util.List;

public class User {
    private String name;
    private String email;
    private List<String> roles;
    private WebElement enableWebElement;
    private WebElement userMenu;
    private Integer number;

    public User(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public WebElement getEnableWebElement() {
        return enableWebElement;
    }

    public void setEnableWebElement(WebElement enableWebElement) {
        this.enableWebElement = enableWebElement;
    }

    public WebElement getUserMenu() {
        return userMenu;
    }

    public void setUserMenu(WebElement userMenu) {
        this.userMenu = userMenu;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
