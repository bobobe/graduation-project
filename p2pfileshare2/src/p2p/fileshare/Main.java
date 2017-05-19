/**    
 * @{#} Main.java Create on 2009.9
 *    
 * Copyright (c) 2009 by author.    
 */  
package p2p.fileshare;

import java.awt.EventQueue;

import p2p.fileshare.ui.chatStart;
import p2p.fileshare.ui.loginFrame;

public class Main {
       //主类，系统的入口函数
	    public static void main(String[] args) {
	    	
	        EventQueue.invokeLater(new Runnable() {
	            public void run() {
	            	//取得一个Manager的实例
	                Manager.GetInstance();
	                //启动用户登录注册界面
	                //chatStart.GetInstance().setVisible(true);
	                loginFrame.GetInstance().setVisible(true);
	            }
	        });          
	    }
}
