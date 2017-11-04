package com.hta.ws.test;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Subscriber {
    public static final String TOPIC_SUB_RPC = "hta/+/request/+/+";

    private final Logger logger = Logger.getLogger(Subscriber.class);

    static Subscriber _instance;

    public synchronized static Subscriber getInstace() throws MqttException {
        if (_instance == null) {
            _instance = new Subscriber();
        }
        return _instance;
    }

    private String user;
    private String pass;
    private String url;
    private String topic;

    MqttConnectOptions conOpt;
    MqttClient client;

    Subscriber() throws MqttException {
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
        logger.info("----------MQTTSub: " + topic);
        init();
    }

    private MqttClient init() throws MqttException {
        if (client == null || !client.isConnected()) {
            logger.info("-------MqttClient init----");
            client = new MqttClient(url, MqttClient.generateClientId());
            client.setCallback(new CmdMqttCallBack());
            client.connect(conOpt);
        }
        return client;
    }

    public void subscriber(String topic) throws MqttException {
        logger.debug("== START SUBSCRIBER == " + topic);
        init().subscribe(topic);
    }

    public void stop(String topic) throws MqttException {
        init().unsubscribe(topic);
    }
}
