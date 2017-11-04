/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hta.ws.main;

import com.hta.ws.test.PublisherGw;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author thuyetlv
 */
public class TestPbGw {

    public static final String GATEWAY_RPC_TOPIC = "v1/gateway/rpc";

    public static void main(String[] args) throws MqttException, Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("../etc/log4j.conf"));
        PropertyConfigurator.configure(props);
        
        //Connect
        String messageConnect = "{\"id\": \"123\",\"device\":\"hta_01\", \"data\": {\"id\": 1,\"method\": \"myMethod\",\"params\": {\"relay\": \"2\",\"enabled\": true}}}";
        PublisherGw.getInstace().publish2(GATEWAY_RPC_TOPIC, messageConnect);
    }
}
