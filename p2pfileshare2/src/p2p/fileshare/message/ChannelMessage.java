
package p2p.fileshare.message;

import java.io.*;

import p2p.fileshare.Channel;
import p2p.fileshare.Peer;


/**
 * @author gl
 * 继承Message类，并实现了Serializable接口
 */
public class ChannelMessage extends Message implements Serializable{
    private Channel channel;
    
    public Channel GetChannel(){
        return channel;
    }
    //构造方法，创建一个ChannelMessage实例
    public ChannelMessage(String iMsg,Peer iFrom,Channel iToChan) {
        super(iMsg,iFrom);
        channel=iToChan;
    }
    
    /*
     * 以下两个方法分别是读、写对象的操作
     */
   private void writeObject(java.io.ObjectOutputStream out) throws IOException{
     out.writeUTF(channel.GetName());   
   }
 
   private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
       channel=Channel.GetByName(in.readUTF());
   }
    
    
}
