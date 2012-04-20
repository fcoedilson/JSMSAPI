/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sms.jfreesms.fullonsms;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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

/**
 *
 * @author anantha
 */
public class FullonSMS implements SMS{

    private HttpClient smsClient = null;
    private String jid = "";

    private boolean authenticated = false;
    
    private boolean debug = false;
    
    public FullonSMS() {
        ClientConnectionManager manager = new ThreadSafeClientConnManager();
        smsClient = new DefaultHttpClient(manager);
    }
    
    @Override
    public boolean login(String userName, String password) {
        authenticated = true;
        
        smsClient.getParams().setParameter("Host", "www.fullonsms.com");
        smsClient.getParams().setParameter("Connection", "keep-alive");
        smsClient.getParams().setParameter("Cache-Control", "max-age=0");
        smsClient.getParams().setParameter("Origin", "http://www.fullonsms.com");
        smsClient.getParams().setParameter("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.162 Safari/535.19");
        smsClient.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded");
        smsClient.getParams().setParameter("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        smsClient.getParams().setParameter("Accept-Encoding", "gzip,deflate,sdch");
        smsClient.getParams().setParameter("Accept-Language", "en-US,en;q=0.8");
        smsClient.getParams().setParameter("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
        smsClient.getParams().setParameter("Cookie", "FOSSID=jkis8fu6gph32tfraoerv8sto7; __gads=ID=1636dce592916ece:T=1334919889:S=ALNI_MaCiIWSZGKaX6ibd_HrJpykdq2b5Q; __utma=172487732.1333900712.1334909451.1334909451.1334919566.2; __utmb=172487732.19.10.1334919566; __utmc=172487732; __utmz=172487732.1334909451.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");

        HttpPost post = new HttpPost("http://www.fullonsms.com/login.php");

        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("MobileNoLogin", userName));
        nameValuePairs.add(new BasicNameValuePair("LoginPassword", password));
        nameValuePairs.add(new BasicNameValuePair("x", "0"));
        nameValuePairs.add(new BasicNameValuePair("y", "0"));
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        // Execute HTTP Post Request
        HttpResponse response = null;
        try {
            response = smsClient.execute(post);
            BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
            StringBuffer html = new StringBuffer();
            while(true)
            {
                int i = bis.read();
                if(i==-1)
                {
                    break;
                }
                char c = (char)i;
                html.append(c);
            }
            
            if(html.indexOf("landing_page.php")<0 || html.indexOf("login.php")>=0)
            {
                authenticated = false;
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        for (Header header : response.getAllHeaders()) {
            if (header.getName().equalsIgnoreCase("Set-Cookie")) {
                String value = header.getValue();
                if(value.startsWith("FOSSID"))
                {
                    jid = value.substring(value.indexOf("=") + 1, value.indexOf(";"));
                    //System.out.println(jid);
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
        
        smsClient.getParams().setParameter("Host", "www.fullonsms.com");
        smsClient.getParams().setParameter("Connection", "keep-alive");
        smsClient.getParams().setParameter("Cache-Control", "max-age=0");
        smsClient.getParams().setParameter("Origin", "http://www.fullonsms.com");
        smsClient.getParams().setParameter("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.162 Safari/535.19");
        smsClient.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded");
        smsClient.getParams().setParameter("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        smsClient.getParams().setParameter("Accept-Encoding", "gzip,deflate,sdch");
        smsClient.getParams().setParameter("Accept-Language", "en-US,en;q=0.8");
        smsClient.getParams().setParameter("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
        smsClient.getParams().setParameter("Cookie", "__gads=ID=1636dce592916ece:T=1334919889:S=ALNI_MaCiIWSZGKaX6ibd_HrJpykdq2b5Q; FOSSID="+jid+"; __utma=172487732.1333900712.1334909451.1334909451.1334919566.2; __utmb=172487732.24.10.1334919566; __utmc=172487732; __utmz=172487732.1334909451.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); ");

        HttpPost post = new HttpPost("http://www.fullonsms.com/home.php");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("ActionScript", "%2Fhome.php"));
        nameValuePairs.add(new BasicNameValuePair("CancelScript", "%2Fhome.php"));
        nameValuePairs.add(new BasicNameValuePair("HtmlTemplate", "%2Fvar%2Fwww%2Fhtml%2Ffullonsms%2FStaticSpamWarning.html"));
        nameValuePairs.add(new BasicNameValuePair("MessageLength", "140"));
        nameValuePairs.add(new BasicNameValuePair("MobileNos", mobileNo));
        nameValuePairs.add(new BasicNameValuePair("Message", msg));
        nameValuePairs.add(new BasicNameValuePair("Gender", "0"));
        nameValuePairs.add(new BasicNameValuePair("FriendName", "Your+Friend+Name"));
        nameValuePairs.add(new BasicNameValuePair("ETemplatesId", ""));
        nameValuePairs.add(new BasicNameValuePair("TabValue", "contacts"));
        
        
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
