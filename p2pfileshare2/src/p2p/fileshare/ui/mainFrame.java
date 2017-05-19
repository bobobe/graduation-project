 
package p2p.fileshare.ui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import p2p.fileshare.Manager;
import p2p.fileshare.Peer;

/**
 * @author gl
 * 系统的主界面类，用于显示一个频道窗口，在此界面中可以显示Peer列表、显示系统消息及提供功能菜单项等
 */
public class mainFrame extends JFrame {
	
	  // 界面无素、组件的变量初始化
    private JScrollPane TextScroller;
    private JButton btClear;
    private JButton btRecvFiles;
    private JToggleButton btShare;
    private JScrollPane jScrollPane1;
    private JTextArea msgArea;
    private JList nickList;
    private JPopupMenu nickPopup;
    private JPanel panRight;
    private JSplitPane panSplit;
    private JMenuItem ppShowShare;
    private JMenuItem ppStartConv;
    private JButton tbSendFile;
    private JToolBar toolbar;
    private JTextField typeBox;
    // 变量初始化结束
	
	
	
    
    /** 创建一个mainFrame类的实例 */
    public mainFrame() {
        initComponents();
        msgArea.setEditable(false);
        setBounds(300, 200, 500, 400);       //设定界面的大小
        this.requestFocus();
    }
    
    public void dispose(){
        Manager.GetInstance().Quit();
    }
    
    //主界面内元素、组件的初始化
    private void initComponents() {
        nickPopup = new JPopupMenu();
        ppStartConv = new JMenuItem();
        ppShowShare = new JMenuItem();
        panSplit = new JSplitPane();
        jScrollPane1 = new JScrollPane();
        nickList = new JList();
        panRight = new JPanel();
        TextScroller = new JScrollPane();
        msgArea = new JTextArea();
        typeBox = new JTextField();
        toolbar = new JToolBar();
        btShare = new JToggleButton();
        tbSendFile = new JButton();
        btRecvFiles = new JButton();
        btClear = new JButton();
        //Peer列表中的右键选择是的“私人聊天”按钮
        nickPopup.setInvoker(nickList);
        ppStartConv.setText("私人聊天");
        //选择此项后，会弹出一个私人会话的界面，两个Peer就可以能过这个界面进行私人会话
        ppStartConv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ppStartConvActionPerformed(evt);
            }
        });

        nickPopup.add(ppStartConv);
        //Peer列表中的右键选择是的“显示共享”按钮
        ppShowShare.setText("显示共享");
        //选择此项后，将弹出文件共享界面，显示此Peer共享的文件内容
        ppShowShare.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ppShowShareActionPerformed(evt);
            }
        });

        nickPopup.add(ppShowShare);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("频道视图");
        setName("frmMain");
        panSplit.setDividerLocation(100);
        panSplit.setPreferredSize(new java.awt.Dimension(200, 25));
        nickList.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                nickListMouseReleased(evt);
            }
        });

        jScrollPane1.setViewportView(nickList);

        panSplit.setLeftComponent(jScrollPane1);

        panRight.setLayout(new java.awt.BorderLayout());

        TextScroller.setAutoscrolls(true);
        msgArea.setBackground(new java.awt.Color(229, 229, 249));
        msgArea.setColumns(20);
        msgArea.setRows(5);
        msgArea.setEditable(false);
        TextScroller.setViewportView(msgArea);

        panRight.add(TextScroller, java.awt.BorderLayout.CENTER);

        typeBox.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                typeBoxKeyPressed(evt);
            }
        });

        panRight.add(typeBox, java.awt.BorderLayout.SOUTH);

        panSplit.setRightComponent(panRight);

        getContentPane().add(panSplit, java.awt.BorderLayout.CENTER);
        //菜单项中的“共享目录”按钮
        btShare.setText("共享目录");
        //点击此按钮时，会弹出文件选择对话框，用户可以从中选择要共享的目录
        btShare.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btShareActionPerformed(evt);
            }
        });

        toolbar.add(btShare);
        //菜单项中的“发送文件”按钮
        tbSendFile.setText("发送文件");
        tbSendFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                tbSendFileActionPerformed(evt);
            }
        });

        toolbar.add(tbSendFile);
        //菜单项中的“接收文件”按钮
        btRecvFiles.setText("接收文件");
        //点击此按钮，弹出接收文件的对话框
        btRecvFiles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btRecvFilesActionPerformed(evt);
            }
        });

        toolbar.add(btRecvFiles);
        //菜单项中的“清屏”按钮
        btClear.setText("清屏");
        //点击此按钮时，执行清屏操作，将频道的消息框中的内容置空
        btClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btClearActionPerformed(evt);
            }
        });

        toolbar.add(btClear);

        getContentPane().add(toolbar, java.awt.BorderLayout.NORTH);

        pack();
    }// 组件初始化结束

    ///////////////////以下几个方法是各个按钮动作的具体执行/////////////////////
    
    //执行清屏按钮的动作，将消息框的内容置为空。
    private void btClearActionPerformed(ActionEvent evt) {
        msgArea.setText("");
    }
    
    //执行显示共享的动作，显示Peer的共享文件
    private void ppShowShareActionPerformed(ActionEvent evt) {
        if(nickList.getSelectedValue()!=null && nickList.getSelectedValue() instanceof Peer)
                   peerShareFrame.ForUser((Peer)(nickList.getSelectedValue())).Show();
        
    }
    //执行主动共享的动作，用户将自己本地的目录共享出去
    private void btShareActionPerformed(ActionEvent evt) {
        Manager.GetInstance().SetMyShare(btShare.isSelected());
        
    }
    //执行发送文件的动作，点击此按钮后，会让用户选择一个文件发送出去
    private void tbSendFileActionPerformed(ActionEvent evt) {
        Manager.GetInstance().SendFileToAll();
    }
     //执行接收文件的动作，点击此按钮后，会弹出接收文件的界面，显示Peer节点接收到的文件的情况 
    private void btRecvFilesActionPerformed(ActionEvent evt) {
        Manager.GetInstance().ShowRecvFiles();
    }

    //在选择Peer节点，进行右键单击操作时，要保证有Peer被选中
    private void nickListMouseReleased(MouseEvent evt) {
        if(evt.getButton()!=evt.BUTTON1)
            nickPopup.show(nickList,evt.getX(),evt.getY());
        
    }
    //开启一个私人会话，右键选择Peer，点击此按钮后，会弹出私人聊天的界面
    private void ppStartConvActionPerformed(ActionEvent evt) {
       if(nickList.getSelectedValue()!=null && nickList.getSelectedValue() instanceof Peer)
           Manager.GetInstance().StartPrivateConversation((Peer)(nickList.getSelectedValue()));
    }
    //用来设定，敲击键盘上的“回车”键时，发送一条消息
    private void typeBoxKeyPressed(KeyEvent evt) {
        if(evt.getKeyChar()=='\n')
            SendMsg();
    }
    
   
   //////////////////////以下几个方法，是具体的业务方法，用于实现一些基本的功能///////////////////////////
    
    //更新Peer列表
    public void UpdateNickList(Peer[] iUsers){
        if(nickList.getModel()==null || !(nickList.getModel() instanceof NickListModel))
            nickList.setModel(new NickListModel(iUsers));
        ((NickListModel)nickList.getModel()).Update(iUsers);
        
    }
    //发送消息
    public void SendMsg(){
        Manager.GetInstance().SendChanMessage(typeBox.getText()); 
        typeBox.setText("");
    }
    //在每条消息后加上"\n"，用于换行操作
    public void AddRecvLine(String iMsg){
        msgArea.setText(msgArea.getText()+iMsg+"\n");
        msgArea.scrollRectToVisible(new Rectangle(0,msgArea.getHeight()-20,1,1));
    }
    //执行清屏操作
    public void ClearRecvArea(){
        msgArea.setText("");
    }
    
}

//内部类，继承了AbstractListModel类，主要用于Peer列表的显示模式
class NickListModel extends AbstractListModel{
    Peer[] nicklist;
    public NickListModel(Peer[] iList){
        nicklist=iList;
    }
    public int getSize() { return nicklist.length; }
    public Object getElementAt(int i) { return nicklist[i]; }
    public void Update(Peer[] iList){
        fireIntervalRemoved(this,0,getSize());
        nicklist=iList;
        fireIntervalAdded(this, 0,getSize()); 
    }
}