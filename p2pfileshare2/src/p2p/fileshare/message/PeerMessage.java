
package p2p.fileshare.message;

import p2p.fileshare.Peer;

public class PeerMessage extends Message{
    private Peer fromuser;
    /*创建一个PeerMessage实例*/
    public PeerMessage(String iMsg,Peer iFrom) {
        super(iMsg,iFrom);
        fromuser=iFrom;
    }
    
}
