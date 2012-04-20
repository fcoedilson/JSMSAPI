/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sms.jfreesms.way2sms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.sms.jfreesms.SMS;
import org.sms.jfreesms.exception.NotAuthenticatedException;

/**
 *
 * @author anantha
 */
public class Way2SMS implements SMS {

    private HttpClient smsClient = null;
    private String jid = "";

    private boolean authenticated = false;
    
    public Way2SMS() {
        ClientConnectionManager manager = new ThreadSafeClientConnManager();
        smsClient = new DefaultHttpClient(manager);
    }

    @Override
    public boolean login(String userName, String password) {
        
        authenticated = true;
        
        smsClient.getParams().setParameter("Host", "site4.way2sms.com");
        smsClient.getParams().setParameter("Connection", "keep-alive");
        smsClient.getParams().setParameter("Cache-Control", "max-age=0");
        smsClient.getParams().setParameter("Origin", "http://site4.way2sms.com");
        smsClient.getParams().setParameter("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.162 Safari/535.19");
        smsClient.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded");
        smsClient.getParams().setParameter("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        smsClient.getParams().setParameter("Accept-Encoding", "gzip,deflate,sdch");
        smsClient.getParams().setParameter("Accept-Language", "en-US,en;q=0.8");
        smsClient.getParams().setParameter("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
        smsClient.getParams().setParameter("Cookie", "__gads=ID=c9b227532a00044e:T=1334849437:S=ALNI_MaWzQCpmJ7x1Bv36O4X6aln2uyyOw; JSESSIONID=A03~EAC1C01D2271FF74DBF7FC0BAF236370.w803");

        HttpPost post = new HttpPost("http://site4.way2sms.com/Login1.action");

        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("username", userName));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        // Execute HTTP Post Request
        HttpResponse response = null;
        try {
            response = smsClient.execute(post);
            System.out.println(response);
        } catch (IOException ex) {
            Logger.getLogger(Way2SMS.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Header header : response.getAllHeaders()) {
            if (header.getName().equalsIgnoreCase("Set-Cookie")) {
                String value = header.getValue();
                jid = value.substring(value.indexOf("=") + 1, value.indexOf(";"));
                //System.out.println(jid);
            }
            if (header.getName().equalsIgnoreCase("Location")) {
                String value = header.getValue();
                if(value.endsWith("id="))
                {
                    authenticated = false;
                }
                //System.out.println(jid);
            }
        }

        if (response.getEntity() != null) {
            try {
                response.getEntity().consumeContent();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return authenticated;
    }

    @Override
    public void send(String mobileNo, String msg)throws NotAuthenticatedException {
        
        if(isAuthenticated() == false)
        {
            throw new NotAuthenticatedException("You are not Authenticated.");
        }
        
        smsClient.getParams().setParameter("Host", "site4.way2sms.com");
        smsClient.getParams().setParameter("Connection", "keep-alive");
        smsClient.getParams().setParameter("Cache-Control", "max-age=0");
        smsClient.getParams().setParameter("Origin", "http://site4.way2sms.com");
        smsClient.getParams().setParameter("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.162 Safari/535.19");
        smsClient.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded");
        smsClient.getParams().setParameter("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        smsClient.getParams().setParameter("Accept-Encoding", "gzip,deflate,sdch");
        smsClient.getParams().setParameter("Accept-Language", "en-US,en;q=0.8");
        smsClient.getParams().setParameter("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
        smsClient.getParams().setParameter("Cookie", "__gads=ID=c9b227532a00044e:T=1334849437:S=ALNI_MaWzQCpmJ7x1Bv36O4X6aln2uyyOw; JSESSIONID=" + jid);

        HttpPost post = new HttpPost("http://site4.way2sms.com/quicksms.action");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("HiddenAction", "instantsms"));
        nameValuePairs.add(new BasicNameValuePair("catnamedis", "Birthday"));
        nameValuePairs.add(new BasicNameValuePair("Action", "sf55sa5655sdf5"));
        nameValuePairs.add(new BasicNameValuePair("chkall", "on"));
        nameValuePairs.add(new BasicNameValuePair("MobNo", mobileNo));
        nameValuePairs.add(new BasicNameValuePair("textArea", msg));
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        try {
            // Execute HTTP Post Request
            HttpResponse response = smsClient.execute(post);
            System.out.println(response);
            if (response.getEntity() != null) {
                try {
                    response.getEntity().consumeContent();
                    //EntityUtils.consume(response.getEntity());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    
}
