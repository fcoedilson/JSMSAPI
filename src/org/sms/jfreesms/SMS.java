/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sms.jfreesms;

/**
 *
 * @author anantha
 */
public interface SMS {
    
    
    void login(String userName,String password);
    public void send(String mobileNo,String msg);
}
