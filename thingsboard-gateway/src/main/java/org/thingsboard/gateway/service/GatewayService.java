/**
 * Copyright © 2017 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.gateway.service;

import org.thingsboard.gateway.service.data.*;
import org.thingsboard.server.common.data.kv.KvEntry;
import org.thingsboard.server.common.data.kv.TsKvEntry;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by ashvayka on 16.01.17.
 */
public interface GatewayService {

    /**
     * Inform gateway service that device is connected
     * @param deviceName
     */
    MqttDeliveryFuture onDeviceConnect(String deviceName);

    /**
     * Inform gateway service that device is disconnected
     * @param deviceName
     */
    Optional<MqttDeliveryFuture> onDeviceDisconnect(String deviceName);

    /**
     * Report device attributes change to Thingsboard
     * @param deviceName - the device name
     * @param attributes - the attribute values list
     */
    MqttDeliveryFuture onDeviceAttributesUpdate(String deviceName, List<KvEntry> attributes);

    /**
     * Report device telemetry to Thingsboard
     * @param deviceName - the device name
     * @param telemetry - the telemetry values list
     */
    MqttDeliveryFuture onDeviceTelemetry(String deviceName, List<TsKvEntry> telemetry);

    /**
     * Report attributes request to Thingsboard
     * @param attributeRequest - attributes request
     * @param listener - attributes response
     */
    void onDeviceAttributeRequest(AttributeRequest attributeRequest, Consumer<AttributeResponse> listener);

    /**
     * Report response from device to the server-side RPC call from Thingsboard
     * @param response - the device response to RPC call
     */
    void onDeviceRpcResponse(RpcCommandResponse response);

    /**
     * Subscribe to attribute updates from Thingsboard
     * @param subscription - the subscription
     * @return true if successful, false if already subscribed
     *
     */
    boolean subscribe(AttributesUpdateSubscription subscription);

    /**
     * Subscribe to server-side rpc commands from Thingsboard
     * @param subscription - the subscription
     * @return true if successful, false if already subscribed
     */
    boolean subscribe(RpcCommandSubscription subscription);

    /**
     * Unsubscribe to attribute updates from Thingsboard
     * @param subscription - the subscription
     * @return true if successful, false if already unsubscribed
     */
    boolean unsubscribe(AttributesUpdateSubscription subscription);

    /**
     * Unsubscribe to server-side rpc commands from Thingsboard
     * @param subscription - the subscription
     * @return true if successful, false if already unsubscribed
     */
    boolean unsubscribe(RpcCommandSubscription subscription);

    /**
     * Report generic error from one of gateway components
     * @param e - the error
     */
    void onError(Exception e);

    /**
     * Report error related to device
     * @param deviceName - the device name
     * @param e - the error
     */
    void onError(String deviceName, Exception e);

}
