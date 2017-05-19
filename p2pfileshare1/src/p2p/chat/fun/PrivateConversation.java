package p2p.chat.fun;
import p2p.chat.Manager;
import p2p.chat.Peer;
import p2p.chat.ui.privateConvFrame;

/**
 * @author gl
 * 此类主要用于实现Peer间的私人会话功能
 */
public class PrivateConversation {
    private privateConvFrame ui;
    private Peer to;
    
    public PrivateConversation(Peer iTo) {
        to=iTo;
        ui=new privateConvFrame(this);
    }

    public Peer GetTo(){
        return to;
    }
    
    public void Show(){
        ui.setVisible(true);
    }
    //处理消息到达时的操作
    public void MessageArrival(String iMsg){
        ui.AddRecvLine("<" + to.GetName()+">说： " + iMsg);
        if(!ui.isVisible()){
            ui.setVisible(true);
            ui.requestFocus();
        }
            
    }
    //处理发送消息的操作
    public void SendMessage(String iMsg){
        Manager.GetInstance().SendPrivateMsg(to,iMsg);
        ui.AddRecvLine("<" + Manager.GetInstance().GetMe().GetName() +">说：" + iMsg);
    }
}
