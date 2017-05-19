
/**    
 * @{#} Main.java Create on 2009.9
 *    
 * Copyright (c) 2009 by author.    
 */  
package p2p.fileshare;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import p2p.fileshare.function.ChanAdvertiserThread;
import p2p.fileshare.function.FileSharing;
import p2p.fileshare.function.PrivateConversation;
import p2p.fileshare.message.AttachmentMessage;
import p2p.fileshare.message.ChannelMessage;
import p2p.fileshare.message.Message;
import p2p.fileshare.message.PrivateMessage;
import p2p.fileshare.message.ServiceMessage;
import p2p.fileshare.message.SharingListMessage;
import p2p.fileshare.net.MulticastDispatcher;
import p2p.fileshare.net.NetworkDispatcher;
import p2p.fileshare.ui.FileDialog;
import p2p.fileshare.ui.peerShareFrame;
import p2p.fileshare.ui.recvFilesFrame;

/**
 * @TODO：系统管理类，协调和管理系统中多个类之间的关系
 *
 */
public class Manager {
	
    /*单例模式的结构*/
    private static Manager singObj;
      public static Manager GetInstance() {
        try {
            if(singObj==null)
                singObj=new Manager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return singObj;
    }//单例模式结构结束
  
    //定义两个静态常量，一个是默认的超时时间，另一个是可接收的最大文件数量
    public static final int DefaultOperTimeout=3000;
    private static final int MAX_RECVFILES_NUM=50;
    //常量定义结束
    
    //定义本类中用到的结构、类、变量等
    private NetworkDispatcher Dispatcher;
    private ArrayList<String> ChannelList=new ArrayList<String>();
    private TreeMap<Peer, Object> SharingMap=new TreeMap<Peer, Object>();
    private TreeMap<Peer, PrivateConversation> privconvs=new TreeMap<Peer, PrivateConversation>();
    private Channel curChan;
    private Peer me;
    private ChanAdvertiserThread advThread;
    private boolean ChannelFree=true;
    private LinkedList<AttachmentMessage> recvfiles=new LinkedList<AttachmentMessage>();
    private recvFilesFrame recvui;
    private String ReqChan="";
    private FileSharing myshare=new FileSharing();
    //定义结束
    
    public Object WaitForJoinAck=new Object();
    
    public NetworkDispatcher GetDispatcher(){
        return Dispatcher;
    }
    
    /**
     * 构造方法
     * @throws Exception
     */
    private Manager() throws Exception{
        Dispatcher=new MulticastDispatcher();
        me=Peer.Anonymous; //Anonymous user
    }

    
    /**
     * return_type : void
     * TODO:显示接收的文件列表
     */
    public void ShowRecvFiles(){
       AttachmentMessage []recvArr=new AttachmentMessage[recvfiles.size()];
        try {
            if(recvui!=null)
                recvui.dispose();
            recvfiles.toArray(recvArr);
            //通过一个接收文件的窗口来展示
            recvui=new recvFilesFrame(recvArr);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * return_type : void
     * TODO:发送文件给每一个Peer
     */
    public void SendFileToAll(){
        try {
        	//用于弹出文件选择对话框 
            File chFile=FileDialog.OpenFileDialog();
            if(chFile==null)
                return;
            if(!chFile.exists())
                return;
            if(chFile.length()>GetDispatcher().GetMaxFileSize()){
            	//控制发送文件的最大容量65000，如果超出这个容量则弹出错误的提示信息
                JOptionPane.showMessageDialog(null,"文件太大 (最大容量为: "+ GetDispatcher().GetMaxFileSize() +" bytes)","错误",JOptionPane.ERROR_MESSAGE);
                return;
            }
            AttachmentMessage newMsg=new AttachmentMessage(GetMe(),chFile,false);
            GetDispatcher().DispatchToAll(newMsg);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * return_type : boolean
     * TODO:设置Peer的名字
     * @param iNick
     * @return
     */
    public boolean TrySetNick(String iNick){
        me=new Peer(iNick);
        me.SetStatus(Peer.STATUS_ASKINGNICK);
        ServiceMessage newMsg=new ServiceMessage(Peer.Anonymous,ServiceMessage.CODE_QUERY_NICK_FREE,iNick);        
        GetDispatcher().DispatchToAll(newMsg);
        try {
            Thread.sleep(DefaultOperTimeout);
        } catch (InterruptedException ex) {}
        if(me.GetStatus()==Peer.STATUS_NICKFAILED)
            return false;
        
        me.SetStatus(Peer.STATUS_AUTH);
        return true;
    }
    
    /**
     * return_type : void
     * TODO:Peer退出，系统发出通知消息
     */
    public void Quit(){
        if(curChan!=null){
            ServiceMessage newMsg=new ServiceMessage(GetMe(),ServiceMessage.CODE_PART,curChan.GetName());
            GetDispatcher().DispatchToAll(newMsg);
        }
        System.exit(0);
    }
    
    /**
     * return_type : boolean
     * TODO:根据频道名称来判断当前频道是否空闲
     * @param iChanName
     * @return
     */
    public boolean IsChannelFree(String iChanName){
        ChannelFree=true;
        ServiceMessage newMsg=new ServiceMessage(GetMe(),ServiceMessage.CODE_QUERY_CHAN_FREE,iChanName);  
        GetDispatcher().DispatchToAll(newMsg);
        try {
            Thread.sleep(DefaultOperTimeout);
        } catch (InterruptedException ex) {}
        return ChannelFree;
    }
    
    
    /**
     * return_type : String[]
     * TODO:取得可得到的频道信息
     * @return
     */
    public String[] GetAvailableChannels(){
        String []outArr=new String[ChannelList.size()];
        ChannelList.toArray(outArr);
        return outArr;
    }
    
    /**
     * return_type : void
     * TODO:设置广告频道信息的操作
     * @param iChan
     */
    public void SetAndAdvertiseChannel(Channel iChan){
        curChan=iChan;
        curChan.AddPeer(GetMe());
        ReqChan="";
        if(advThread!=null)
            advThread.stop();
        advThread=new ChanAdvertiserThread(curChan);
        //启动多线程 ，开始进行广播
        advThread.start();
    }
    
    /**
     * return_type : void
     * TODO:发送频道消息
     * @param iMsg
     */
    public void SendChanMessage(String iMsg){
        if(curChan==null)
            return;

        ChannelMessage newMsg=new ChannelMessage(iMsg,GetMe(),curChan);
        GetDispatcher().DispatchToAll(newMsg);

    }
    
    public Channel GetCurrentChannel(){
        return curChan;
    }
    
    public boolean HasJoined(){
        return curChan!=null;
    }
    
    public void SendPrivateMsg(Peer iTo,String iMsg){
        PrivateMessage newMsg=new PrivateMessage(iMsg,GetMe(),iTo);
        GetDispatcher().DispatchToAll(newMsg);
    }
    
    /**
     * return_type : void
     * TODO:开启一个私人会话
     * @param iTo
     */
    public void StartPrivateConversation(Peer iTo){
        if(iTo==null)
            return;
        if(!privconvs.containsKey(iTo)){
            privconvs.put(iTo,new PrivateConversation(iTo));
        }else
        	((PrivateConversation)privconvs.get(iTo)).Show();
    }
    
    public void SetMyShare(boolean iSet){
        if(!iSet){
            myshare.Unshare();
        }else{
            File shareFld=FileDialog.DirFileDialog();
            if(shareFld==null || !shareFld.exists() || !shareFld.isDirectory())
                return;
            myshare.ShareDir(shareFld.getAbsolutePath());
            System.out.println(myshare.GetSharedFiles().length + " 个文件已被共享");
            //广播我的共享情况
            SharingListMessage newMsg=new SharingListMessage(GetMe(),myshare.GetSharedFiles());
            GetDispatcher().DispatchToAll(newMsg);
        }
    }
 
    /**
     * return_type : String[]
     * TODO:取得用户的共享信息
     * @param iUser
     * @return
     */
    public String[] GetUserShare(Peer iUser){
        if(!SharingMap.containsKey(iUser))
            return null;
        return (String[])SharingMap.get(iUser);
    }
    
    /*
     * 以下的几个方法都是用来处理各种消息的。
     * 
     */
    
    public void ParseMessage(Message iMsg){
        if(iMsg instanceof ServiceMessage)
            ParseServiceMessage((ServiceMessage)iMsg);
        else if(iMsg instanceof ChannelMessage)
            ParseChannelMessage((ChannelMessage)iMsg);
        else if(iMsg instanceof PrivateMessage)
            ParsePrivateMessage((PrivateMessage)iMsg);
        else if(iMsg instanceof AttachmentMessage)
            ParseAttachmentMessage((AttachmentMessage)iMsg);
        else if(iMsg instanceof SharingListMessage)
            ParseSharingListMessage((SharingListMessage)iMsg);
    }

    private void ParseSharingListMessage(SharingListMessage iMsg){
        SharingMap.put(iMsg.GetSender(),iMsg.GetShareList());
        if(curChan!=null)
            curChan.Notice(iMsg.GetSender() + " 正在共享 "+ iMsg.GetShareList().length +" 个文件... 在Peer列中点击右键可查看其共享信息");
    }
    
    private void ParseAttachmentMessage(AttachmentMessage iMsg){
        if(iMsg.IsRequested()){
            peerShareFrame.ForUser(iMsg.GetSender()).AddToCache(iMsg);
        }else{
            recvfiles.add(iMsg);
            if(recvfiles.size()>MAX_RECVFILES_NUM)
                recvfiles.removeFirst();
            if(curChan!=null)
                curChan.Notice("接收到新的文件 " + iMsg);
        }
    }
    
    private void ParsePrivateMessage(PrivateMessage iMsg){
        if(!iMsg.GetTo().equals(GetMe()))
            return;
        
        StartPrivateConversation(iMsg.GetSender());
        ((PrivateConversation)privconvs.get(iMsg.GetSender())).MessageArrival(iMsg.GetText());
    }
    
    private void ParseChannelMessage(ChannelMessage iMsg){
        if(iMsg.GetChannel()==null)
            return;
        if(!iMsg.GetChannel().equals(curChan))
            return;

        iMsg.GetChannel().MessageReceived(iMsg);
    }
    
    /**
     * return_type : void
     * TODO:处理各种各样的系统服务消息，如通知消息、状态消息、系统消息等
     * @param iMsg
     */
    private void ParseServiceMessage(ServiceMessage iMsg){
        if(!iMsg.IsBroadcast() && !iMsg.GetToUser().equals(GetMe()))
            return;
        
        switch(iMsg.GetCode()){
            case ServiceMessage.CODE_CHAN_ADV:{
                if(!ChannelList.contains(iMsg.GetArg())){
                    ChannelList.add(iMsg.GetArg());
                    System.out.println("发现网络中有新的频道，名称为： " +iMsg.GetArg());
                }
            }
                break;
                //有Peer加入频道的时候
            case ServiceMessage.CODE_JOIN:{
                if(curChan==null){//我加入了频道
                    ReqChan=iMsg.GetArg();
                }else{//其它的Peer加入
                    if(!iMsg.GetArg().equals(curChan.GetName()))
                        return;
                    curChan.Join(iMsg.GetSender());
                    //向每一个Peer客户端发送一个Hello消息，来通知：“我加入这个频道了”。
                    ServiceMessage newMsg=new ServiceMessage(GetMe(),ServiceMessage.CODE_HELOJOIN,curChan.GetName());
                    GetDispatcher().DispatchToAll(newMsg);
                    //如果我是这个频道的创建者，那么就通知这个新加入的Peer，告诉他：我是这个频道的主人！
                    if(GetMe().equals(curChan.GetOwner())){
                        newMsg=new ServiceMessage(GetMe(),ServiceMessage.CODE_CHAN_OWNER,curChan.GetName());
                        GetDispatcher().DispatchToAll(newMsg);
                    }
                    
                }
            } break;
            
            case ServiceMessage.CODE_HELOJOIN:{
                if(curChan==null && ReqChan.length()>0 && ReqChan.equals(iMsg.GetArg())){
                    System.out.println("已经成功加入到频道");
                    SetAndAdvertiseChannel(new Channel(iMsg.GetArg()));
                    curChan.AddPeer(GetMe());
                    synchronized(WaitForJoinAck){ WaitForJoinAck.notify();}
                }else if(curChan==null && ReqChan.length()<=0)        
                    return;
                
                if(! iMsg.GetArg().equals(curChan.GetName()))
                    return;
                
                
                curChan.AddPeer(iMsg.GetSender());                
            }break;
             //Peer离开频道的时候
            case ServiceMessage.CODE_PART:{
                if(curChan==null) return;
                if(!iMsg.GetArg().equals(curChan.GetName()))
                    return;
                curChan.Part(iMsg.GetSender());

            }break;
            
            case ServiceMessage.CODE_CHAN_OWNER:{
                if(curChan==null) return;
                if(!iMsg.GetArg().equals(curChan.GetName()))
                    return;
                curChan.SetOwner(iMsg.GetSender());
            }break;
            
            case ServiceMessage.CODE_QUERY_NICK_FREE:{
                if(GetMe().GetName().equals(iMsg.GetArg()) && GetMe().GetStatus()==Peer.STATUS_AUTH){
                    ServiceMessage newMsg=new ServiceMessage(GetMe(),ServiceMessage.CODE_NICK_TAKEN,GetMe().GetName());
                    GetDispatcher().DispatchToAll(newMsg);
                }
            }break;
            
            case ServiceMessage.CODE_QUERY_CHAN_FREE:{
                if(Channel.GetByName(iMsg.GetArg())!=null){
                    ServiceMessage newMsg=new ServiceMessage(GetMe(),ServiceMessage.CODE_CHAN_TAKEN,iMsg.GetArg());
                    GetDispatcher().DispatchToAll(newMsg);
                }
            }break;
            
            case ServiceMessage.CODE_CHAN_TAKEN:{
                if(curChan.GetName().equals(iMsg.GetArg()))
                    ChannelFree=false;
            }break;
            
            case ServiceMessage.CODE_NICK_TAKEN:{
                if(GetMe().GetStatus()!=Peer.STATUS_ASKINGNICK) return;
                GetMe().SetStatus(Peer.STATUS_NICKFAILED);
            }break;
            
            case ServiceMessage.CODE_ASK_SHARE:{
                SharingListMessage newMsg=new SharingListMessage(GetMe(),myshare.GetSharedFiles());
                GetDispatcher().DispatchToAll(newMsg);
            }break;        
            //查看Peer的共享文件的时候
            case ServiceMessage.CODE_ASK_FILE:{
                String []MyShareList=myshare.GetSharedFiles();
                System.out.println("共享的文件 ：" +iMsg.GetArg());
                if(MyShareList.length<=0)
                    return;

                if(Arrays.binarySearch(MyShareList,iMsg.GetArg())<0)
                    return;
                
                File sFile=new File(myshare.GetFullFilePath(iMsg.GetArg()));
                if(!sFile.exists()){
                    System.out.println("找不到文件 " + sFile.getPath());
                    return;
                }
                AttachmentMessage newMsg;
                try {
                    newMsg = new AttachmentMessage(GetMe(), sFile, true);
                    newMsg.SetFileName(iMsg.GetArg());
                    GetDispatcher().DispatchToAll(newMsg);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
            }break;        

            
        }
    }
    
    public Peer GetMe(){
        return me;
    }
    
}
