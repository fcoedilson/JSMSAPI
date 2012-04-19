/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sms.jfreesms.way2sms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.sms.jfreesms.SMSLogin;
import org.sms.jfreesms.webdriver.JWebDriver;

/**
 *
 * @author anantha
 */
public class Way2SMSLogin implements SMSLogin {

    private JWebDriver jWebDriver = null;
    private String WAY2SMSURL = "http://site5.way2sms.com/content/index.html";

    public Way2SMSLogin() {
        jWebDriver = new JWebDriver();
    }

    @Override
    public JWebDriver getjWebDriver() {
        return jWebDriver;
    }

    @Override
    public void login(String userName, String password) {
        try {
            WebDriver webDriver = jWebDriver.getWebDriver();
            webDriver.get(WAY2SMSURL);
            
            WebElement usernameInput = webDriver.findElement(By.id("username"));
            usernameInput.sendKeys(userName);

            WebElement passwordInput = webDriver.findElement(By.id("password"));
            passwordInput.sendKeys(password);

            WebElement buttonInput = webDriver.findElement(By.id("button"));
            buttonInput.click();

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
