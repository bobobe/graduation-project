  
package p2p.fileshare.function;



import p2p.fileshare.Channel;
import p2p.fileshare.Manager;
import p2p.fileshare.message.ServiceMessage;


/**
 * @author gl
 * 多线程的方式实现，用来向网络中广告当前网络中的频道信息
 */
public class ChanAdvertiserThread extends Thread {
    private Channel channel;
    
    public ChanAdvertiserThread(Channel iChan){
        channel=iChan;
    }
    public void run(){
        Manager manager=Manager.GetInstance();
        
        for(;;){
            try {
            	//要广告的消息
                ServiceMessage advMsg=new ServiceMessage(manager.GetMe(),ServiceMessage.CODE_CHAN_ADV,channel.GetName());
                //向每个Peer广播
                manager.GetDispatcher().DispatchToAll(advMsg);
                //每隔2秒广播一次
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
         }
    }
}

