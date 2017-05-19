/**    
 * @{#} MulticastDispatcher.java Create on 2009-9
 */  
package p2p.chat.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 此类主要用IP多播技术来发送数据，关于此技术的详细讲解，请参考本章第三节所讲的Java网络编程、P2P实现机制等方面的知识
 *
 */
public class MulticastDispatcher extends NetworkDispatcher{
	
	//选用专门为多播指定的D类IP地址（224.0.0.1到239.255.255.255）任选一个即可，用来创建一个多播组
    public static final String DefaultMulticastGroupIP="230.1.1.1";
    //使用指定的端口（一般选1024以上的端口号）只要是空闲端口即可，用于建立多播套接字。
    public static final int DefaultMulticastGroupPort=1314;
    //定义一个接收文件的缓冲区
    private static final int RecvBufSize=65536;
    
    private MulticastSocket cSock;       //定义多点Sockets，用于IP多播
    private InetAddress curAddr;         //InetAddress来获取本机名称和IP地址信息
    private int curPort;                 // 当前端口
    
     ///内部的私有类，用多线程的方法来执行接收过程
    private class RecvThread extends Thread{
    	
        private MulticastSocket cSock;
        private NetworkDispatcher Dispatcher;
        
        public RecvThread(NetworkDispatcher iDispatcher,MulticastSocket iSock){
            cSock=iSock;
            Dispatcher=iDispatcher;
        }
        
        public void run(){
            byte inBuf[]=new byte[RecvBufSize];
            DatagramPacket rPack=new DatagramPacket(inBuf,RecvBufSize);
            
            try {
                for(;;){
                    cSock.receive(rPack);
                    Dispatcher.DataReceived(rPack.getData(),rPack.getLength());
                
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private RecvThread rThread;
    
    protected void DispatchToAll(byte iBuf[],int iSize) throws Exception{
        DatagramPacket dPack=new DatagramPacket(iBuf,iSize,curAddr,curPort);
        cSock.send(dPack);
    }
    
    /**创建一个新的MulticastDispatcher实例 */
    public MulticastDispatcher() throws Exception{
        curAddr=InetAddress.getByName(DefaultMulticastGroupIP);
        curPort=DefaultMulticastGroupPort;
        cSock=new MulticastSocket(curPort);
        cSock.joinGroup(curAddr);
     
        rThread=new RecvThread(this,cSock);
        rThread.start();
    }
    
}
