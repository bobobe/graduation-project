/**    
 * @{#} Main.java Create on 2009.9
 *    
 * Copyright (c) 2009 by author.    
 */  
package p2p.fileshare;

import java.util.LinkedList;
import java.util.TreeMap;

import p2p.fileshare.message.ChannelMessage;
import p2p.fileshare.message.ServiceMessage;
import p2p.fileshare.ui.mainFrame;

/**
 * @author gl
 *
 */
public class Channel{
    private String name;           //定义频道的名称
    private Peer owner;            //频道的所有者，是哪个Peer创建的
    private mainFrame ui;          //主频道界面
    
    //主频道界面中的Peer列表
    private LinkedList<Peer> peers=new LinkedList<Peer>();
    //当前网络中已经存在的频道，用Map存储，一个频道名称对应一个频道实体
    private static TreeMap<String, Channel> knownchannels=new TreeMap<String, Channel>();
    
    //通过频道的名称返回一个频道实体
    public static Channel GetByName(String iName){
        if(knownchannels.containsKey(iName))
            return (Channel)knownchannels.get(iName);
        else
            return null;
    }
    
    public String GetName(){
        return name;
    }
    
    public Peer GetOwner(){
        return owner;
    }
    
    public void SetOwner(Peer iOwner){
        owner=iOwner;
    }
    //用Peer[]型表示当前频道中所有的Peer，也就是取得所有的用户
    public Peer[] GetPeers(){
        Peer[] outArr=new Peer[peers.size()];
        peers.toArray(outArr);
        return outArr;
    }
    
    //添加一个新用户，也就是新加入一个Peer的意思 
    public void AddPeer(Peer iPeer){
        if(peers.contains(iPeer))
           return;
        peers.add(iPeer);
        ui.UpdateNickList(GetPeers());
    }
    
    //加入频道
    public void Join(Peer iPeer){
        Notice("Peer::" + iPeer.GetName() + " 加入了频道 " + GetName());
        AddPeer(iPeer);
    }
   //频道的通知消息
    public void Notice(String iStr){
        ui.AddRecvLine("::: "+iStr);
    }
    //Peer离开频道
    public void Part(Peer iPeer){
        if(peers.contains(iPeer))
            peers.remove(iPeer);
        Notice("Peer::" + iPeer.GetName() + "离开了频道" + GetName());
        ui.UpdateNickList(GetPeers());
    }
    //由Peer向频道发送消息
    public void MessageReceived(ChannelMessage iMsg){
         ui.AddRecvLine("< "+iMsg.GetSender().GetName() + ">说： " + iMsg.GetText());
    }
    //设置频道密码
    public void SetKey(String iKey){
    	//通过Manager实例调用NetworkDispatcher的SetKey()方法，对密码进行加密
        Manager.GetInstance().GetDispatcher().SetKey(iKey);
    }
    
    /** 构造方法，创建一个新的频实例 */
    public Channel(String iName) {
        name=iName;
       
        ui=new mainFrame();
        ui.setTitle("当前频道： " + name);
        ui.setVisible(true);
        
        knownchannels.put(name,this);
    }
    
    //频道退出以后，从已知频道列表中移除
    public void finalize() throws Throwable{
        knownchannels.remove(this);
        super.finalize();
    }
    
    public boolean equals(Channel iTo){
        if(iTo==null) return false;
        return name.equals(iTo.name);
    }
    
    //执行创建频道的操作，通过名称和密码来惟一的确定一个频道
    public static void CreateNew(String iName,String iKey){
        Channel newChan=new Channel(iName);
      if(iKey!=null && iKey.length()>0)
        newChan.SetKey(iKey);
      //设置频道的创建者
      newChan.SetOwner(Manager.GetInstance().GetMe());
      //频道创建成功之后，将频道消息交给“频道广告线程”，将新建的频道信息不停的广告出去。
      Manager.GetInstance().SetAndAdvertiseChannel(newChan);
    }
  //执行加入一个已有的频道的操作，通过选择频道名称和输入频道的认证密码来加入此频道。
    public static void JoinExisting(String iName,String iKey) throws Exception{
        //对用户输入的密码进行加密，然后进行密文较验
    	Manager.GetInstance().GetDispatcher().SetKey(iKey);
        //系统发出通知消息，消息内容是当前是哪一个Peer加入了哪一个Channel
        ServiceMessage newMsg=new ServiceMessage(Manager.GetInstance().GetMe(),ServiceMessage.CODE_JOIN,iName);
        //将以上的消息内容通过IP多点广播的形式通知给每一个Peer
        Manager.GetInstance().GetDispatcher().DispatchToAll(newMsg);
    }
}
