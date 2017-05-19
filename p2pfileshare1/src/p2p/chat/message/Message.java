 
package p2p.chat.message;

import java.io.*;

import p2p.chat.Peer;


/**
 * @author gl
 * 抽象的虚拟类，实现了Serializable接口
 */
public abstract class Message implements Serializable{
    protected String text;
    protected Peer from;
    
    /*
     * 以下方法，主要用于实现消息的三个基本要点
     */
    public boolean DontEncrypt(){
        return false;
    }
    
    public Peer GetSender(){
        return from;
    }
    
    public String GetText(){
        return text;
    }
   
    protected Message(String iMsg,Peer iFrom) {
        text=iMsg;
        from=iFrom;
    }
    
}
