/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sms.jfreesms;

import org.sms.jfreesms.webdriver.JWebDriver;

/**
 *
 * @author anantha
 */
public interface SMSLogin {
    
    public JWebDriver getjWebDriver();  
    void login(String userName,String password);
}
