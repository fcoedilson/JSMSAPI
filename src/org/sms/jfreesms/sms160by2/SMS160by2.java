/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sms.jfreesms.sms160by2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.sms.jfreesms.SMS;
import org.sms.jfreesms.exception.NotAuthenticatedException;
import org.sms.jfreesms.way2sms.Way2SMS;

/**
 *
 * @author anantha
 */
public class SMS160by2 implements SMS{

    private HttpClient smsClient = null;
    private String jid = "";

    private boolean authenticated = false;
    
    private boolean debug = false;
    
    public SMS160by2() {
        ClientConnectionManager manager = new ThreadSafeClientConnManager();
        smsClient = new DefaultHttpClient(manager);
    }
    
    @Override
    public boolean login(String userName, String password) {
        authenticated = true;
        
        smsClient.getParams().setParameter("Host", "www.160by2.com");
        smsClient.getParams().setParameter("Connection", "keep-alive");
        smsClient.getParams().setParameter("Cache-Control", "max-age=0");
        smsClient.getParams().setParameter("Origin", "http://www.160by2.com");
        smsClient.getParams().setParameter("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.162 Safari/535.19");
        smsClient.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded");
        smsClient.getParams().setParameter("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        smsClient.getParams().setParameter("Accept-Encoding", "gzip,deflate,sdch");
        smsClient.getParams().setParameter("Accept-Language", "en-US,en;q=0.8");
        smsClient.getParams().setParameter("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
        smsClient.getParams().setParameter("Cookie", "__gads=ID=0523aeb8dd4d2602:T=1334902996:S=ALNI_MY3yF_P4okrBNqEF9911u0YPwa6yw");

        HttpPost post = new HttpPost("http://www.160by2.com/re-login");

        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", userName));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        nameValuePairs.add(new BasicNameValuePair("button", "Login"));
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        // Execute HTTP Post Request
        HttpResponse response = null;
        try {
            response = smsClient.execute(post);
            //System.out.println(response);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        for (Header header : response.getAllHeaders()) {
            if (header.getName().equalsIgnoreCase("Set-Cookie")) {
                String value = header.getValue();
                if(value.startsWith("JSESSIONID"))
                {
                    jid = value.substring(value.indexOf("=") + 1, value.indexOf(";"));
                    //System.out.println(jid);
                }
                
            }
            if (header.getName().equalsIgnoreCase("Location")) {
                String value = header.getValue();
                if(value.contains("?id=") == false || value.endsWith("id=") == true)
                {
                    authenticated = false;
                }
                
            }
        }

        if (response.getEntity() != null) {
            try {
                response.getEntity().consumeContent();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        if(debug == true)
        {
            if(authenticated == true)
            {
                System.out.println("Logged in successfully.");
            }
            else
            {
                System.out.println("Failed to login. Please try again");
            }
        }
        
        return authenticated;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void send(String mobileNo, String msg) throws NotAuthenticatedException {
        if(isAuthenticated() == false)
        {
            throw new NotAuthenticatedException("You are not Authenticated.");
        }
        
        smsClient.getParams().setParameter("Host", "www.160by2.com");
        smsClient.getParams().setParameter("Connection", "keep-alive");
        smsClient.getParams().setParameter("Cache-Control", "max-age=0");
        smsClient.getParams().setParameter("Origin", "http://www.160by2.com");
        smsClient.getParams().setParameter("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.162 Safari/535.19");
        smsClient.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded");
        smsClient.getParams().setParameter("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        smsClient.getParams().setParameter("Accept-Encoding", "gzip,deflate,sdch");
        smsClient.getParams().setParameter("Accept-Language", "en-US,en;q=0.8");
        smsClient.getParams().setParameter("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
        smsClient.getParams().setParameter("Cookie", "__gads=ID=0523aeb8dd4d2602:T=1334902996:S=ALNI_MY3yF_P4okrBNqEF9911u0YPwa6yw; JSESSIONID=" + jid+"; noad=0");

        HttpPost post = new HttpPost("http://www.160by2.com/SendSMSAction");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("hid_exists", "no"));
        nameValuePairs.add(new BasicNameValuePair("action1", "hgfgh5656fgd"));
        nameValuePairs.add(new BasicNameValuePair("mobile1", mobileNo));
        nameValuePairs.add(new BasicNameValuePair("msg1", msg));
        nameValuePairs.add(new BasicNameValuePair("sel_month", "0"));
        nameValuePairs.add(new BasicNameValuePair("sel_year", "0"));
        nameValuePairs.add(new BasicNameValuePair("sel_hour", "hh"));
        nameValuePairs.add(new BasicNameValuePair("sel_minute", "mm"));
        nameValuePairs.add(new BasicNameValuePair("sel_cat", "0"));
        
        
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        try {
            // Execute HTTP Post Request
            HttpResponse response = smsClient.execute(post);
            //System.out.println(response);
            if (response.getEntity() != null) {
                try {
                    response.getEntity().consumeContent();
                    //EntityUtils.consume(response.getEntity());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        if(debug == true)
        {
            System.out.println("Message sent Successfully.");
        }
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
}
