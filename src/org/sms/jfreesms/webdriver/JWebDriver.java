/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sms.jfreesms.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 *
 * @author anantha
 */
public class JWebDriver{

    private WebDriver webDriver = null; 
   
    public JWebDriver() {
          webDriver = new HtmlUnitDriver();
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }
    
   
    
}
