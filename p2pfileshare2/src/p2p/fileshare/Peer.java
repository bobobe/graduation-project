/**    
 * @{#} Peer.java Create on 2009-9-22 ����03:34:58    
 *    
 * Copyright (c) 2009 by GUANLEI.    
 */  
package p2p.fileshare;

import java.io.Serializable;

public class Peer implements Serializable,Comparable<Object>{
	
	//定义一个静态的匿名Peer
    public static Peer Anonymous=new Peer();
    
    //定义Peer的状态代码，分别用Char型的'0','1','2','3'表示
    public static final char STATUS_NOTAUTH='0';       //当前Peer状态没有经过认证
    public static final char STATUS_ASKINGNICK='1';    //请求Peer的名称
    public static final char STATUS_NICKFAILED='2';    //Peer的名称出现问题
    public static final char STATUS_AUTH='3';          //经过认证的Peer状态
    
    
    private String name;                               //Peer的名字
    private boolean anonymous=false;                   //是否匿名
    private char status=STATUS_NOTAUTH;                //默认的Peer状态是没有经过认证的
    
   //判断此Peer是否是匿名的
    public boolean IsAnonymous(){
        return anonymous;
    }
    
    //取得Peer的状态信息
    public char GetStatus(){
        return status;
    }
    
   
    //设置Peer的状态
    public void SetStatus(char iStatus){
        status=iStatus;
    }
    
    
   //取Peer的名字
    public String GetName(){
        return name;
    }
    /** 创建一个新的Peer实例 */
    public Peer(String iName) {
        name=iName;
    }
    
    //创建一个匿名的Peer用户
    private Peer(){
        anonymous=true;
        name="???";
    }
    
    public boolean equals(Object obj){
        if(!(obj instanceof Peer))
            return false;
        if(obj==null) return false;
        if(IsAnonymous() || ((Peer)obj).IsAnonymous()) return false;
        
        return name.equals(((Peer)obj).name);
    }
    
    public String toString(){
        return name;
    }
    
    public int compareTo(Object iTo){
        if(! (iTo instanceof Peer))
            return 0;
        return name.compareTo(((Peer)iTo).name);
    }
}

