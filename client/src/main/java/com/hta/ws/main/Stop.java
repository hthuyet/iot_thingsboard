/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hta.ws.main;

import static com.hta.ws.main.Start.logger;
import com.hta.ws.application.Publisher;
import com.hta.ws.application.Subscriber;
import com.hta.ws.process.ProcessHisCmd;
import com.hta.ws.process.ProcessIrriStart;
import com.hta.ws.process.ProcessIrriStop;
import com.hta.ws.process.ProcessSchedule;
import com.hta.ws.process.ProcessSendCommandMgr;

/**
 *
 * @author ThuyetLV
 */
public class Stop {

    public static void main(String[] args) throws Exception {
        try {
            //Utils
            Publisher.getInstace().disconnect();
            Subscriber.getInstace().stop();

            //Process
            ProcessIrriStart.getInstance().stop();
            ProcessIrriStop.getInstance().stop();
            ProcessSchedule.getInstance().stop();
            ProcessSendCommandMgr.getInstance().stop();
            ProcessHisCmd.getInstance().stop();
        } catch (Exception ex) {
            logger.error("ERROR stop: ", ex);
            System.exit(1);
        }
    }
}
