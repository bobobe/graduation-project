 
package com.p2pfileshare.base.message;

import java.io.*;

import com.p2pfileshare.base.bean.Peer;

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
