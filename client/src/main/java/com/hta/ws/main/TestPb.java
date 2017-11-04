/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hta.ws.main;

import com.hta.ws.test.Publisher;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author thuyetlv
 */
public class TestPb {
    private static final String TOPIC_CONNECT = "sensors/connect";
    private static final String TOPIC_SENSOR = "sensors";
    private static final String TOPIC_DISCONNECT = "sensors/disconnect";
    private static final String TOPIC_ATTRIBUTES = "sensors/attributes";
    private static final String TOPIC_SINGLE_DATA = "sensor/%serialNumber%/temperature";

    public static void main(String[] args) throws MqttException, Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("../etc/log4j.conf"));
        PropertyConfigurator.configure(props);

        //Connect
        String messageConnect = "{\"serialNumber\":\"SN-001\"}";
        Publisher.getInstace().publish2(TOPIC_CONNECT, messageConnect);
        
        String messageConnectMulti = "[{\"serialNumber\":\"SN-002\", \"model\":\"M2\", \"temperature\":42.0}, {\"serialNumber\":\"SN-003\", \"model\":\"M3\", \"temperature\":73.0}]";
        Publisher.getInstace().publish2(TOPIC_CONNECT, messageConnectMulti);
        
        Thread.sleep(5000);
        
        //Send data
        String messageSensor = "{\"serialNumber\":\"SN-001\", \"model\":\"T1001\", \"temperature\":40}";
        Publisher.getInstace().publish2(TOPIC_SENSOR, messageSensor);
        
        //Update single data
        Thread.sleep(5000);
        String messageData = "{\"value\":36.6}";
        String serialNumber = "SN-001";
        Publisher.getInstace().publish2(TOPIC_SINGLE_DATA.replace("%serialNumber%", serialNumber), messageData);
        
        Thread.sleep(5000);
        
        //ATTRIBUTES
        String messageArr = "{\"serialNumber\":\"SN-001\", \"key\":\"dataUploadFrequency\", \"requestId\": 123}";
        Publisher.getInstace().publish2(TOPIC_ATTRIBUTES, messageArr);
        
        Thread.sleep(15000);
        
        //Disconnect
        String messageDis = "{\"serialNumber\":\"SN-001\"}";
        Publisher.getInstace().publish2(TOPIC_DISCONNECT, messageDis);
    }
}
