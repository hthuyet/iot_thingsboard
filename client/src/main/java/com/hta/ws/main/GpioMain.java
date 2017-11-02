/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hta.ws.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author thuyetlv
 */
public class GpioMain {

    protected static final Logger logger = Logger.getLogger(GpioMain.class);

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("../etc/log4j.conf"));
        PropertyConfigurator.configure(props);

//        GpioControl gpioControl = new GpioControl();
//        gpioControl.init();
        RelayControl relayControl = new RelayControl();
        relayControl.init();
    }
}
