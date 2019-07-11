package com.parley.testing.model.contracts;

import org.openqa.selenium.By;

public class ContractField {
    private ContractFieldType type;
    private String value;
    private By selector;


    public ContractField(ContractFieldType type, By selector, String value) {
        this.type = type;
        this.value = value;
        this.selector = selector;
    }

    public ContractFieldType getType() {
        return type;
    }

    public void setType(ContractFieldType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public By getSelector() {
        return selector;
    }

    public void setSelector(By selector) {
        this.selector = selector;
    }

    public enum ContractFieldType{
        INPUT,
        CHECKBOX,
        RADIO_BUTTON,
        SELECT,
        MULTI_SELECT
    }
}
