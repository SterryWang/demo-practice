package com.example.demo.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IMsgRpcService实现类
 * 实现后端RPC业务逻辑
 * @author wangxg3
 */
public class MsgRpcServiceImpl implements  IMsgRpcService {
    private static Logger  log = LoggerFactory.getLogger(MsgRpcServiceImpl.class);
    @Override
    public void sayHello(String name) {
        log.info("向{}问好！",name);

    }

    @Override
    public void sayHelloCopy(String name) {
        log.info("sayHelloCopy()方法被调用，特向{}问好",name
        );
    }
}
