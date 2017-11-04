package com.hta.ws.test;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class PublisherGw {

    private static final String USERNAME = "d1lhGMM89WkiLxmAfEHN";
    private final Logger logger = Logger.getLogger(PublisherGw.class);

    static PublisherGw _instance;

    public synchronized static PublisherGw getInstace() throws MqttException {
        if (_instance == null) {
            _instance = new PublisherGw();
        }
        return _instance;
    }

    private String url;

    MqttConnectOptions conOpt;
    MqttAsyncClient client;
    private String user;
    private String pass;

    PublisherGw() throws MqttException {
        url = "tcp://localhost:1883";
        conOpt = new MqttConnectOptions();

        conOpt.setUserName(USERNAME);
        conOpt.setCleanSession(true);
        logger.info("----------MQTTUrl: " + url);

//        url = "tcp://35.194.12.75:1883";
//        user = "user";
//        pass = "password";
//
//        conOpt = new MqttConnectOptions();
//        if (pass != null) {
//            conOpt.setPassword(pass.toCharArray());
//        }
//        if (user != null) {
//            conOpt.setUserName(user);
//        }
//        conOpt.setCleanSession(true);
//
//        logger.info("----------MQTTUrl: " + url);
//        logger.info("----------MQTTUser: " + user);
//        logger.info("----------MQTTPass: " + pass);
        init();
    }

    private void init() throws MqttException {
        if (client == null || !client.isConnected()) {
            client = new MqttAsyncClient(url, MqttClient.generateClientId(), new MqttDefaultFilePersistence());
            client.connect(conOpt, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    logger.info("Connected to Thingsboard!");
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable e) {
                    logger.warn("Connected onFailure Thingsboard!");
                }
            }).waitForCompletion();
        }
    }

    public synchronized void disconnect() throws MqttException {
        if (client == null || !client.isConnected()) {
            client.disconnect();
            client = null;
        }
    }

    public void publish2(String topic, String messageString) throws MqttException, Exception {
        logger.debug("== START publish2 ==");
        init();
        MqttMessage message = new MqttMessage();
        message.setPayload(messageString.getBytes());
        if (client == null) {
            throw new Exception("Client is null.");
        }
        if (!client.isConnected()) {
            throw new Exception("Client is not connect.");
        }
        logger.info("--topic: " + topic);
        logger.info("--message: " + message);
        client.publish(topic, message);
    }
}
