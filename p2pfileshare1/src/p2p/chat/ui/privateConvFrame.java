/**    
 * @{#} ChanAdvertiserThread.java Create on 2009-9-23 上午08:45:14    
 *    
 * Copyright (c) 2009 by GUANLEI.    
 */  
package p2p.chat.ui;
/**    
 * @author 管磊  
 * @version 1.0    
 */
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import p2p.chat.fun.PrivateConversation;
/**
 * @author gl
 *  Peer间进行私人会话的聊天界面，继承自JFrame类
 */
public class privateConvFrame extends JFrame {
	
	 private PrivateConversation refConv;
	
	 // 私人会话界面中用到的组件元素声明
    private JScrollPane jScroller;
    private JTextArea msgArea;
    private JTextField typeBox;
    //变量声明结束
	
   
    /** 从界面mainFrame中，创建一个新的界面，就是私人会话的界面 */
    public privateConvFrame(PrivateConversation iRef) {
        refConv=iRef;
        initComponents();
        this.setBounds(300, 300, 300, 300);
        setVisible(true);
        requestFocus();
        setTitle(refConv.GetTo().GetName());
        typeBox.requestFocus();
        
    }
    
   //私人会话的聊天界面组件初始化
    private void initComponents() {
        typeBox = new javax.swing.JTextField();
        jScroller = new javax.swing.JScrollPane();
        msgArea = new javax.swing.JTextArea();
        //设置标题
        setTitle("私人聊天");
        setName("frmPrivateConv");
        //监听键盘按键动作，什么时候发送消息
        typeBox.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                typeBoxKeyTyped(evt);
            }
        });
        //整个界面分两个区域，下方的是消息输入框
        getContentPane().add(typeBox, BorderLayout.SOUTH);

        msgArea.setBackground(new java.awt.Color(244, 241, 241));
        msgArea.setColumns(20);
        msgArea.setEditable(false);
        msgArea.setRows(5);
        jScroller.setViewportView(msgArea);
        //上方的部分，是消息显示区
        getContentPane().add(jScroller, java.awt.BorderLayout.CENTER);

        pack();
    }

    //处理键盘按键的动作，当按下“回车”键后，发送消息 
    private void typeBoxKeyTyped(KeyEvent evt) {
        if(evt.getKeyChar()=='\n')    
            SendMsg();
    }
    
    //消息的发送方法，当发送的消息是/clear或者/cls，执行清屏操作。
    public void SendMsg(){
        if(typeBox.getText().equals("/clear") || typeBox.getText().equals("/cls") ){
            ClearRecvArea();
        }else
            refConv.SendMessage(typeBox.getText());
        typeBox.setText("");
    }
    
    //接收消息，在每条接收到的消息后，加上\n，用于自动换行
    public void AddRecvLine(String iMsg){
        msgArea.setText(msgArea.getText()+iMsg+"\n");
        msgArea.scrollRectToVisible(new Rectangle(0,msgArea.getHeight()-20,1,1));
    }
    public void ClearRecvArea(){
        msgArea.setText("");
    }
}

