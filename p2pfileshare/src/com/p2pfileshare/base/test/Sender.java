/**    
 * @{#} Client.java Create on 2009-9-23 下午07:00:50    
 *    
 * Copyright (c) 2009 by GUANLEI.    
 */
package com.p2pfileshare.base.test;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * @author 管磊
 * @version 1.0
 */
public class Sender {

	public static void main(String[] args) {
		try {
			//发送的消息
			byte[] arb = new byte[] { 'h', 'e', 'l', 'l', 'o' };
			//创建一个IP地址为： 230.0.0.1, 端口为： 7777的多播套接字。
			InetAddress inetAddress = InetAddress.getByName("230.0.0.1");
			DatagramPacket datagramPacket = new DatagramPacket(arb, arb.length,	inetAddress, 7777);
			//新建一个数据报，封装多播信息并将其发送出去。
			MulticastSocket multicastSocket = new MulticastSocket();
			multicastSocket.send(datagramPacket);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
