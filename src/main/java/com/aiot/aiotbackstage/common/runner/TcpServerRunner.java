package com.aiot.aiotbackstage.common.runner;

import com.aiot.aiotbackstage.server.TcpServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Avernus
 */
@Component
public class TcpServerRunner implements ApplicationRunner {

    @Resource
    private TcpServer tcpServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        tcpServer.start();
    }
}
