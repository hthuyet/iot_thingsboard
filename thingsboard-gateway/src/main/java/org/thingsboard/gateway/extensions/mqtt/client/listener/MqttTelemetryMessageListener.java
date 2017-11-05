/**
 * Copyright © 2017 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.thingsboard.gateway.extensions.mqtt.client.listener;

import java.nio.charset.StandardCharsets;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.thingsboard.gateway.extensions.mqtt.client.conf.mapping.MqttDataConverter;
import org.thingsboard.gateway.service.data.DeviceData;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by ashvayka on 24.01.17.
 */
@Data
@Slf4j
public class MqttTelemetryMessageListener implements IMqttMessageListener {

    private final Consumer<List<DeviceData>> consumer;
    private final MqttDataConverter converter;

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        try {
            //@TODO: ThuyetLV: Test for HTA device
            String data = new String(message.getPayload(), StandardCharsets.UTF_8);
            log.info("-------------------messageArrived: " + data);
//            JSONParser parser = new JSONParser();
//            JSONObject json = (JSONObject) parser.parse(data);
//            Object cmd = json.get("cmd");
//            if (cmd != null && ("1").equalsIgnoreCase((String) cmd)) {
//                log.info("-------------------messageArrived cmd: " + cmd);
                consumer.accept(converter.convert(topic, message));
//            } else {
//                log.info("-------------------NO messageArrived cmd: " + cmd);
//            }
        } catch (Exception e) {
            log.info("[{}] Failed to decode message: {}", topic, Arrays.toString(message.getPayload()), e);
        }
    }
}
