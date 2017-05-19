 
package p2p.fileshare.message;

import java.io.*;

import p2p.fileshare.Peer;

/**
 * @author gl
 * 继承Message类，实现Serializable接口
 */
public class PrivateMessage extends Message implements Serializable{
    private Peer to;
    
    public Peer GetTo(){
        return to;
    }
    //创建一个PrivateMessage实例
    public PrivateMessage(String iMsg,Peer iFrom,Peer iTo) {
        super(iMsg,iFrom);
        to=iTo;
    }
    
}
