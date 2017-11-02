/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hta.ws.main;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

/**
 *
 * @author thuyetlv
 */
public class RelayControl {

    protected static final Logger logger = Logger.getLogger(RelayControl.class);

    private static final String THINGSBOARD_HOST = "localhost";
    private static final String ACCESS_TOKEN = "NJrK0OlYPuaEMFf2vCvs";
    private MqttAsyncClient mThingsboardMqttClient;

    private static final Map<Integer, Integer> listRelay = new HashMap<>();

    static {
        listRelay.put(0, 1);
        listRelay.put(1, 0);
        listRelay.put(2, 0);
        listRelay.put(3, 0);
    }

    public void init() {
        try {
            mThingsboardMqttClient = new MqttAsyncClient("tcp://" + THINGSBOARD_HOST + ":1883", "Raspberry Pi 3", new MemoryPersistence());
            mqttConnect();
        } catch (MqttException e) {
            logger.error("Unable to create MQTT client", e);
        }
    }

    private void mqttConnect() {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(ACCESS_TOKEN);
        mThingsboardMqttClient.setCallback(mMqttCallback);
        try {
            mThingsboardMqttClient.connect(connOpts, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    logger.info("MQTT client connected!");
                    try {
                        mThingsboardMqttClient.subscribe("v1/devices/me/rpc/request/+", 0);
                    } catch (MqttException e) {
                        logger.error("Unable to subscribe to rpc requests topic", e);
                    }
                    try {
                        mThingsboardMqttClient.publish("v1/devices/me/attributes", getRelayStatusMessage());
                    } catch (Exception e) {
                        logger.error("Unable to publish GPIO status to Thingsboard server", e);
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable e) {
                    if (e instanceof MqttException) {
                        MqttException mqttException = (MqttException) e;
                        logger.error(String.format("Unable to connect to Thingsboard server: %s, code: %d", mqttException.getMessage(),
                                mqttException.getReasonCode()), e);
                    } else {
                        logger.error(String.format("Unable to connect to Thingsboard server: %s", e.getMessage()), e);
                    }
                }
            });
        } catch (MqttException e) {
            logger.error(String.format("Unable to connect to Thingsboard server: %s, code: %d", e.getMessage(), e.getReasonCode()), e);
        }
    }

    private void mqttDisconnect() {
        try {
            mThingsboardMqttClient.disconnect();
            logger.info("MQTT client disconnected!");
        } catch (MqttException e) {
            logger.error("ERROR mqttDisconnect Unable to disconnect from the Thingsboard server: ", e);
        }
    }

    private MqttCallback mMqttCallback = new MqttCallback() {

        @Override
        public void connectionLost(Throwable e) {
            logger.error("Disconnected from Thingsboard server", e);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            logger.info(String.format("Received message from topic [%s]", topic));
            String requestId = topic.substring("v1/devices/me/rpc/request/".length());
            JSONObject messageData = new JSONObject(new String(message.getPayload()));
            String method = messageData.getString("method");
            if (method != null) {
                if (method.equals("getRelayStatus")) {
                    sendRelayStatus(requestId);
                } else if (method.equals("setRelayStatus")) {
                    JSONObject params = messageData.getJSONObject("params");
                    Integer relay = params.getInt("relay");
                    boolean enabled = params.getBoolean("enabled");
                    if (relay != null) {
                        updateRelayStatus(relay, enabled, requestId);
                    }
                } else {
                    //Client acts as an echo service
                    mThingsboardMqttClient.publish("v1/devices/me/rpc/response/" + requestId, message);
                }
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            logger.info("-------deliveryComplete----");
        }
    };

    private MqttMessage getRelayStatusMessage() throws Exception {
        JSONObject gpioStatus = new JSONObject();
        String stateMsg = "";
        for (Map.Entry<Integer, Integer> entrySet : listRelay.entrySet()) {
            stateMsg += entrySet.getValue() + ",";
        }
        if (StringUtils.endsWith(stateMsg, ",")) {
            stateMsg = stateMsg.substring(0, stateMsg.length() - 1);
        }
        gpioStatus.put("stateMsg", stateMsg);
        MqttMessage message = new MqttMessage(gpioStatus.toString().getBytes());
        return message;
    }

    private void sendRelayStatus(String requestId) throws Exception {
        mThingsboardMqttClient.publish("v1/devices/me/rpc/response/" + requestId, getRelayStatusMessage());
    }

    private void updateRelayStatus(int pin, boolean enabled, String requestId) throws Exception {
        JSONObject response = new JSONObject();
        String responseMsg = "";
        Integer state = listRelay.get(pin % listRelay.size());
        if (state != null) {
            responseMsg = (enabled) ? "1" : "0";
        }
        response.put("responseMsg", responseMsg);
        MqttMessage message = new MqttMessage(response.toString().getBytes());
        mThingsboardMqttClient.publish("v1/devices/me/rpc/response/" + requestId, message);
        mThingsboardMqttClient.publish("v1/devices/me/attributes", message);
    }
}
