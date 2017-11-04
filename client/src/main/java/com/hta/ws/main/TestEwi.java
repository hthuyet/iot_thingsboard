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
public class TestEwi {

    private static final String TOPIC_CONNECT = "ewi/connect";
    private static final String TOPIC_SENSOR = "ewi";
    private static final String TOPIC_DISCONNECT = "ewi/disconnect";
    private static final String TOPIC_ATTRIBUTES = "ewi/attributes";
    private static final String TOPIC_SINGLE_DATA = "ewi/%uid%/temperature";

    public static void main(String[] args) throws MqttException, Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("../etc/log4j.conf"));
        PropertyConfigurator.configure(props);

        //Connect
        String messageConnect = "{\"cmd\":{\"id\": \"ewi_1\", \"type\": \"1\"},\"data\": \"0,0,0,0,0,0\"}";
        Publisher.getInstace().publish2(TOPIC_CONNECT, messageConnect);

//        String messageConnectMulti = "[{\"serialNumber\":\"SN-002\", \"model\":\"M2\", \"temperature\":42.0}, {\"serialNumber\":\"SN-003\", \"model\":\"M3\", \"temperature\":73.0}]";
//        Publisher.getInstace().publish2(TOPIC_CONNECT, messageConnectMulti);
        Thread.sleep(5000);
//        
//        //Send data
        String messageSensor = "{\"cmd\":{\"id\": \"ewi_1\", \"type\": \"1\"},\"data\": \"0,0,0,0,0,0\"}";
        Publisher.getInstace().publish2(TOPIC_SENSOR, messageSensor);
//        
//        //Update single data
//        Thread.sleep(5000);
//        String messageData = "{\"value\":36.6}";
//        String serialNumber = "SN-001";
//        Publisher.getInstace().publish2(TOPIC_SINGLE_DATA.replace("%uid%", serialNumber), messageData);
//        
//        Thread.sleep(5000);
//        
//        //ATTRIBUTES
//        String messageArr = "{\"serialNumber\":\"SN-001\", \"key\":\"dataUploadFrequency\", \"requestId\": 123}";
//        Publisher.getInstace().publish2(TOPIC_ATTRIBUTES, messageArr);
//        
//        Thread.sleep(15000);
//        
//        //Disconnect
//        String messageDis = "{\"serialNumber\":\"SN-001\"}";
//        Publisher.getInstace().publish2(TOPIC_DISCONNECT, messageDis);
    }

    public static void run(int stt) throws MqttException, Exception {
        //Connect
        String messageConnect = "{\"cmd\":{\"id\": \"ewi_%stt%\", \"type\": \"1\"},\"data\": \"0,0,0,0,0,0\"}";
        Publisher.getInstace().publish2(TOPIC_CONNECT, messageConnect.replace("%stt%", String.valueOf(stt)));

//        Thread.sleep(5000);
//
//        //Send data
//        String messageSensor = "{\"cmd\":{\"id\": \"ewi_%stt%\", \"type\": \"1\"},\"data\": \"0,0,0,0,0,0\"}";
//        Publisher.getInstace().publish2(TOPIC_SENSOR, messageSensor.replace("%stt%", String.valueOf(stt)));
    }
}
