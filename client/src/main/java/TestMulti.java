/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.hta.ws.main.*;
import com.hta.ws.test.Subscriber;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author thuyetlv
 */
public class TestMulti {

    public static void main(String[] args) throws MqttException, Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("../etc/log4j.conf"));
        PropertyConfigurator.configure(props);

        Subscriber.getInstace().subscriber(Subscriber.TOPIC_SUB_RPC);
        
        int stt = 10;
        TestHta.run(stt);
        TestEwi.run(stt);
    }
}
