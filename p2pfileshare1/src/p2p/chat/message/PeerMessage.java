
package p2p.chat.message;

import p2p.chat.Peer;

public class PeerMessage extends Message{
    private Peer fromuser;
    /*创建一个PeerMessage实例*/
    public PeerMessage(String iMsg,Peer iFrom) {
        super(iMsg,iFrom);
        fromuser=iFrom;
    }
    
}
