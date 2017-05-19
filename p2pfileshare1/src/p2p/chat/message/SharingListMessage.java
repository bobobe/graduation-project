 
package p2p.chat.message;

import java.io.*;

import p2p.chat.Peer;

/**
 * @author gl
 * 继承Message类，实现Serializable接口
 */
public class SharingListMessage extends Message implements Serializable{
    
    String[] share;
    //取得共享列表
    public String[] GetShareList(){
        return share;
    }
    //创建一个SharingListMessage消息实例
    public SharingListMessage(Peer iFrom, String[] iSharingList) {
        super("",iFrom);
        share=iSharingList;
        
    }
    
}
