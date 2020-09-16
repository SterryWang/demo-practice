package com.example.demo.message;

/**
 * 基于JMS的RPC调用接口
 * @author wangxg3
 */
public interface IMsgRpcService {
    /**
     * 向某人问好
     * @param name
     */
    void  sayHello(String  name);

    void sayHelloCopy(String name);
}
