package com.hta.ws.test;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Publisher {

    private final Logger logger = Logger.getLogger(Publisher.class);

    static Publisher _instance;

    public synchronized static Publisher getInstace() throws MqttException {
        if (_instance == null) {
            _instance = new Publisher();
        }
        return _instance;
    }

    private String user;
    private String pass;
    private String url;

    MqttConnectOptions conOpt;
    MqttClient client;

    Publisher() throws MqttException {
        url = "tcp://35.194.12.75:1883";
        user = "user";
        pass = "password";

        conOpt = new MqttConnectOptions();
        if (pass != null) {
            conOpt.setPassword(pass.toCharArray());
        }
        if (user != null) {
            conOpt.setUserName(user);
        }
        conOpt.setCleanSession(true);

        logger.info("----------MQTTUrl: " + url);
        logger.info("----------MQTTUser: " + user);
        logger.info("----------MQTTPass: " + pass);

        init();
    }

    private void init() throws MqttException {
        if (client == null || !client.isConnected()) {
            client = new MqttClient(url, MqttClient.generateClientId());
            client.connect(conOpt);
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
        logger.info("--topic: " + topic);
        logger.info("--message: " + message);
        client.publish(topic, message);
    }
}
