/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

/**
 *
 * @author char0n
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class OpensubtitlesXmlRpcTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void opensubtitlesSearchTest() throws MalformedURLException, XmlRpcException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://www.opensubtitles.org/xml-rpc"));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);
        Object[] params = new Object[]{"", "", "en", "RIFAS"};

        Map<String, String> result = (Map<String, String>) client.execute("Login", params);

        System.out.println(result.get("token"));
    }
}
