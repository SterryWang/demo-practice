package com.example.demo.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangxg3
 */
public class MsgRpcServiceImpl implements  IMsgRpcService {
    private static Logger  log = LoggerFactory.getLogger(MsgRpcServiceImpl.class);
    @Override
    public void sayHello(String name) {
        log.info("向{}问好！",name);

    }
}
