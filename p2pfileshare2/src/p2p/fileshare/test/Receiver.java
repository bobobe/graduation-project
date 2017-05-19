/**    
 * @{#} sss.java Create on 2009-9-23 下午07:00:14    
 *    
 * Copyright (c) 2009 by GUANLEI.    
 */
package p2p.fileshare.test;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * @author 管磊
 * @version 1.0
 */

public class Receiver {
	public static void main(String[] arstring) {
		try {
			// Create a multicast datagram socket for receiving IP
			// multicast packets. Join the multicast group at
			// 230.0.0.1, port 7777.
			//加入到IP为230.0.0.1, 端口为  7777 的多播组中，并通过创建一个多播套接字来接收IP多播数据报
			MulticastSocket multicastSocket = new MulticastSocket(7777);
			InetAddress inetAddress = InetAddress.getByName("230.0.0.1");
			multicastSocket.joinGroup(inetAddress);
			// 无限循环的接收来自发送端的消息
			
			while (true) {
				byte[] arb = new byte[100];
				DatagramPacket datagramPacket = new DatagramPacket(arb,arb.length);
				multicastSocket.receive(datagramPacket);
				//接收并将消息打印输出
				System.out.println(new String(arb));
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
