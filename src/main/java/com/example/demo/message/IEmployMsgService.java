package com.example.demo.message;

import com.example.demo.entity.Employee;

import javax.jms.Destination;

/**
 * @author wangxg3
 */
public interface IEmployMsgService {

    /**
     * 发送employee 信息到消息服务器
     * @param e
     * @param dst
     *
     */
    void  sendEmployeeInfo(final Employee e, Destination dst);

    /**
     * 接收信息
     * @param dst
     * @return
     */
    Object  recvMsg(Destination dst);
}
