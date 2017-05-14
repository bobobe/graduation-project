
package com.p2pfileshare.base.message;

import com.p2pfileshare.base.bean.Peer;

public class PeerMessage extends Message{
    private Peer fromuser;
    /*创建一个PeerMessage实例*/
    public PeerMessage(String iMsg,Peer iFrom) {
        super(iMsg,iFrom);
        fromuser=iFrom;
    }
    
}
