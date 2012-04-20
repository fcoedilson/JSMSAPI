/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sms.jfreesms;

import org.sms.jfreesms.exception.NotAuthenticatedException;

/**
 *
 * @author anantha
 */
public interface SMS {
    
    
    boolean login(String userName,String password);
    boolean isAuthenticated();
    public void send(String mobileNo,String msg)throws NotAuthenticatedException;
    void setDebug(boolean debug);
}
