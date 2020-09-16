package com.example.demo.message;

/**
 * IMsgRpcService接口在客户端侧的副本
 * @author wangxg3
 */
public interface IMsgRpcServiceClientCopy {
    /**
     * 定义一个不同于服务端接口方法名的方法
     * @param name
     */
    void sayHelloCopy(String name);

    /**
     * 客户端的调用方法保持和服务端IMsgRpcService方法名一致
     * @param name
     */
    void sayHello(String name);
}
