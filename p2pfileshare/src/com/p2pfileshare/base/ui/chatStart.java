package com.p2pfileshare.base.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.p2pfileshare.base.bean.Channel;
import com.p2pfileshare.base.bean.Manager;

/**
 * @author gl
 * 用户登录或注册频道的界面，也是系统启动的开始界面，继承自JFrame类
 */
public class chatStart extends JFrame {


	 // 变量声明
    private JButton btOK;
    private JButton jButton1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel7;
    private JLabel jLabelline;
    private JLabel jLabelline2;
    private JScrollPane jScrollPane1;
    private JList ltChans;
    private JTextField tbKey;
    private JTextField tbNewChan;
    private JTextField tbNickname;
    private JTextField tbSelKey;
    // 变量声明结束


    //单例模式的结构
	private static chatStart singObj;
    public static chatStart GetInstance(){
        if(singObj==null)
            singObj=new chatStart();
        return singObj;
    }
    //构造方法，建立一个chatStart的界面实例
    private chatStart() {
        initComponents();
        this.setBounds(400, 200, 280, 380);
        this.update(this.getGraphics());
    }

      /**
     * return_type : void
     * TODO:以下方面主要用于界面元素的设计和布局的，不涉及业务方法，没有特殊需要请不要改动
     */
    private void initComponents() {
        tbNickname = new JTextField();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jScrollPane1 = new JScrollPane();
        ltChans = new JList();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        tbNewChan = new JTextField();
        jLabel5 = new JLabel();
        tbKey = new JTextField();
        btOK = new JButton();
        jButton1 = new JButton();
        tbSelKey = new JTextField();
        jLabel7 = new JLabel();
        jLabelline = new JLabel();
        jLabelline2 = new JLabel();
        getContentPane().setLayout(null);
        /*组件初始化*/
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //设置界面的标题
        setTitle("P2P Chat 用户登录/注册");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(255, 255, 255));
        setName("frmStart");
        setResizable(false);
        getContentPane().add(tbNickname);
        tbNickname.setBounds(80, 10, 180, 20);

        jLabel1.setText("用户名称:");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 10, 70, 20);

        //标记一个分隔线，
        jLabelline2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabelline2.setText("*******************************");
        getContentPane().add(jLabelline2);
        jLabelline2.setBounds(10, 40, 260, 14);

        //用户可以加入一个现有的频道
        jLabel2.setText("加入一个现有的频道:");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 55, 120, 14);
        jLabel2.setForeground(new Color(255,0,0));

        jScrollPane1.setViewportView(ltChans);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 100, 250, 110);

        jLabelline.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabelline.setText("*******************************");
        getContentPane().add(jLabelline);
        jLabelline.setBounds(10, 245, 260, 14);


        //用户可以创建一个新的频道
        jLabel3.setText("创建一个新的频道:");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 260, 148, 14);
        jLabel3.setForeground(new Color(255,0,0));

        jLabel4.setText("频道名称:");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(10, 280, 60, 20);

        tbNewChan.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                tbNewChanKeyTyped(evt);
            }
        });

        getContentPane().add(tbNewChan);
        tbNewChan.setBounds(70, 280, 90, 20);

        jLabel5.setText("密码:");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(165, 280, 40, 20);

        tbKey.setEnabled(false);
        getContentPane().add(tbKey);
        tbKey.setBounds(200, 280, 60, 20);

        btOK.setText("确定");
        btOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btOKActionPerformed(evt);
            }
        });

        getContentPane().add(btOK);
        btOK.setBounds(90, 310, 80, 23);

        jButton1.setText("更新当前频道列表");
        //用于更新频道列表的监听器
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton1);
        jButton1.setBounds(10, 75, 150, 20);

        getContentPane().add(tbSelKey);
        tbSelKey.setBounds(160, 215, 100, 20);

        jLabel7.setText("加入此频道的认证密码:");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(10, 215, 150, 20);

        pack();
    }//组件初始化结束

    //KeyEvent事件，用于键入一个新频道的密码
    private void tbNewChanKeyTyped(KeyEvent evt) {
        tbKey.setEnabled(tbNewChan.getText().length()>0);
    }


    //用户加入一个已经有的频道时的操作，以下是点击“确定”按钮时需要执行的动作
    private void btOKActionPerformed(ActionEvent evt) {

    	//用户必须输入用户名，不充许匿名登录
    	 if(GetNick().length()==0){
            JOptionPane.showMessageDialog(this,"用户名称不能为空","错误",JOptionPane.ERROR_MESSAGE);
            return;
        }
        //必须从频道列表中选择一个已有的频道
        if(ltChans.getSelectedValue()==null && !GetCreateNewChannel()){
            JOptionPane.showMessageDialog(this,"你必须选择加入一个已有的频道或是创建一个新的频道","错误",JOptionPane.ERROR_MESSAGE);
            return;
        }
        //用户在加入频道的时候，需要检查用户名和频道列表，这个过程需一定的连接和缓冲时间，这时会弹出一个对话框，提示用户等待。
        JDialog jWait=new JDialog(this,"请稍候... 检查你的用户名和当前可用的频道列表");
        jWait.setResizable(false);
        jWait.setAlwaysOnTop(true);
        jWait.setBounds(400, 200, 400, 20);
        jWait.setVisible(true);
        jWait.requestFocus();

        boolean NickAvail=true,ChanAvail=true;

        NickAvail=Manager.GetInstance().TrySetNick(GetNick());
        if(GetCreateNewChannel())
            ChanAvail=Manager.GetInstance().IsChannelFree(GetNewChannelName());

        jWait.dispose();
        //检查同一频道内的用户名称是否惟一
        if(!NickAvail){
            JOptionPane.showMessageDialog(this,"用户名称已经被使用了","错误",JOptionPane.ERROR_MESSAGE);
            return;
        }
        //检查网络中的频道名称是否惟一
        if(GetCreateNewChannel()){
            if(ChanAvail){
                Channel.CreateNew(GetNewChannelName(),GetNewChannelKey());
                dispose();
            }
            else
                JOptionPane.showMessageDialog(this,"此频道已经存在了","错误",JOptionPane.ERROR_MESSAGE);
        }
        else{
            String selChan=(String)ltChans.getSelectedValue();
            try {
            	//当用户加入一个新频道时，需要对用户输入的频道密码进行认证
                Channel.JoinExisting(selChan,GetSelChannelKey());
                System.out.println("正在请求加入频道……");
                synchronized(Manager.GetInstance().WaitForJoinAck){ Manager.GetInstance().WaitForJoinAck.wait(Manager.DefaultOperTimeout);}
                if(Manager.GetInstance().GetCurrentChannel()==null){
                	//如果用户密码认证错误，弹出提示信息，并退出系统
                    JOptionPane.showMessageDialog(this,"连接超时（可能由于密码认证错误）","Error",JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    //点击按钮时，更新频道列表
    private void jButton1ActionPerformed(ActionEvent evt) {
    	UpdateChanList();
    }

    public String GetNick(){
        return tbNickname.getText();
    }

    public boolean GetCreateNewChannel(){
        return tbNewChan.getText().length()>0;
    }

    public String GetNewChannelName(){
        return tbNewChan.getText();
    }

    public String GetNewChannelKey(){

        return tbKey.getText();
    }

    public String GetSelChannelKey(){
        return tbSelKey.getText();
    }
    //更新频道列表的具体方法
    private void UpdateChanList(){
        ltChans.setModel(new AbstractListModel() {
            String[] strings = Manager.GetInstance().GetAvailableChannels();
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });

    }



}
