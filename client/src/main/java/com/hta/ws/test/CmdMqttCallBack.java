package com.hta.ws.test;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class CmdMqttCallBack implements MqttCallback {

    protected static final Logger logger = Logger.getLogger(CmdMqttCallBack.class);
//    public static final String TOPIC_REQUEST = "v1/devices/me/rpc/request/+";
    public static final String TOPIC_REQUEST = "hta/+/request/+/+";
    private String topicResp = "";

    public void connectionLost(Throwable throwable) {
        logger.warn("Connection to MQTT broker lost!");
    }

    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        logger.info("Message received:\t" + topic + " -- " + new String(mqttMessage.getPayload()));
        topicResp = topic.replace("request", "response");
        String requestId = topic.substring(topic.lastIndexOf("/") + 1);

        logger.info("-----------requestId: " + requestId);
        String message = new String(mqttMessage.getPayload());
        try {
            logger.info("-------message: " + message);
            if (message.length() <= 0 || message.equalsIgnoreCase("{}") || message.equalsIgnoreCase("\"{}\"")) {
                getRelayStatus();
            } else {
                JSONObject jsonObj = new JSONObject(message);

                Integer relay = jsonObj.getInt("relay");
                boolean enabled = jsonObj.getBoolean("enabled");
                if (relay != null) {
                    updateRelayStatus(relay, enabled, requestId);
                }
            }
        } catch (Exception ex) {
            logger.error("ERROR messageArrived: ", ex);
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        logger.warn("----deliveryComplete----");
    }

    private void getRelayStatus() {
        try {
            JSONObject response = new JSONObject();
            response.put("stateMsg", "0,1,0,1");
            Publisher.getInstace().publish2(topicResp, response.toString());
        } catch (MqttException ex) {
            logger.error("ERROR getRelayStatus: ", ex);
        } catch (Exception ex) {
            logger.error("ERROR getRelayStatus: ", ex);
        }
    }

    private void updateRelayStatus(int pin, boolean enabled, String requestId) throws Exception {
        JSONObject response = new JSONObject();
        String responseMsg = "";
        responseMsg = (enabled) ? "1" : "0";
        response.put("responseMsg", responseMsg);
        Publisher.getInstace().publish2(topicResp, response.toString());
    }
}
