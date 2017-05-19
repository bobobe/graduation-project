
package p2p.chat.ui;

import java.awt.event.MouseEvent;
import java.util.TreeMap;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import p2p.chat.Manager;
import p2p.chat.Peer;
import p2p.chat.message.AttachmentMessage;
import p2p.chat.message.ServiceMessage;

/**
 * @author gl
 *  此类主要用来展示Peer节点共享的文件的界面，继承了JFrame类
 */
public class peerShareFrame extends JFrame {
	
	 // 界面元素用到的变量声明
    private JList ltFileList;
    private JPopupMenu mnuPopup;
    private JMenuItem mnuSaveas;
    private JSplitPane panSplit;
    private JScrollPane scrollContent;
    private JScrollPane scrollFileList;
    private JTextArea tbContent;
    //变量声明结束
    
    
    private Peer peer;
    //TreeMap的结构，用于存储<Peer,peerShareFrame>值对，每一个Peer对应一个显示文件共享的界面
    private static TreeMap peerMap=new TreeMap();
    //定义一个TreeMap结构，用于存储<String,AttachmentMessage>值对，每一个文件名，对应着一个文件内容，也就是根据文件名就可以读取到此文件的内容
    private TreeMap filecache=new TreeMap();
    
    
    public static peerShareFrame ForUser(Peer iPeer){
        if(!peerMap.containsKey(iPeer))
            peerMap.put(iPeer,new peerShareFrame(iPeer));
        peerShareFrame retFrm=(peerShareFrame)peerMap.get(iPeer);
        retFrm.UpdateShare();
        return retFrm;
    }
    
    public void AddToCache(AttachmentMessage iMsg){
        filecache.put(iMsg.GetFileName(),iMsg);
        if(ltFileList.getSelectedValue()!=null  && ltFileList.getSelectedValue().equals(iMsg.GetFileName())){
            tbContent.setText(iMsg.GetContent());
        }
    }
    /**创建一个peerShareFrame**/
    private peerShareFrame(Peer iPeer) {
        peer=iPeer;
        initComponents();
        setTitle(iPeer.GetName() + " 的共享列表");
        setBounds(300, 200, 400, 300);
    }
    //更新用户的共享文件信息
    private boolean UpdateShare(){
        if(Manager.GetInstance().GetUserShare(peer)==null){
        	//当用户没有共享时，弹出相应的提示信息
            JOptionPane.showMessageDialog(this,"当前没有共享文件","错误",JOptionPane.ERROR_MESSAGE);
            setVisible(false);
            return false;
        }
        ltFileList.setModel(new StringListModel(Manager.GetInstance().GetUserShare(peer)));
        return true;
    }
    public void Show(){
        setVisible(true);
        requestFocus();
    }
    
   //共享文件界面的组件初始化
    private void initComponents() {
        mnuPopup = new JPopupMenu();
        mnuSaveas = new JMenuItem();
        panSplit = new JSplitPane();
        scrollFileList = new JScrollPane();
        ltFileList = new JList();
        scrollContent = new JScrollPane();
        tbContent = new JTextArea();

        //设置右键的“另存为”功能，可以将共享的文件存到本地
        mnuSaveas.setLabel("另存为...");
        mnuSaveas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSaveasActionPerformed(evt);
            }
        });

        mnuPopup.add(mnuSaveas);

        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        setTitle("共享文件");
        setName("frmUserShare");
        panSplit.setDividerLocation(140);
        ltFileList.setFont(new java.awt.Font("Tahoma", 0, 10));
        ltFileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ltFileListMouseClicked(evt);
            }
        });

        scrollFileList.setViewportView(ltFileList);

        panSplit.setLeftComponent(scrollFileList);

        tbContent.setBackground(new java.awt.Color(204, 204, 204));
        tbContent.setColumns(20);
        tbContent.setRows(5);
        scrollContent.setViewportView(tbContent);

        panSplit.setRightComponent(scrollContent);

        getContentPane().add(panSplit);

    }//组件初始化结束

    //将Peer共享的文件存储到本地的操作
    private void mnuSaveasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSaveasActionPerformed
        if(ltFileList.getSelectedIndex()>=0){
            if(!filecache.containsKey(ltFileList.getSelectedValue()))
                return;
            
            java.io.File oFile=FileDialog.SaveFileDialog();
            
            if(oFile==null)
                return;
            try {
                AttachmentMessage curSel=(AttachmentMessage)filecache.get(ltFileList.getSelectedValue());
                curSel.SaveToFile(oFile.getPath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,ex.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            
        }
    }

    //从显示文件名的共享列表中，鼠标双击，就可以显示此文件的内容，这里的方法就是用来执行鼠标的动作，是否需要显示文件内容
    private void ltFileListMouseClicked(MouseEvent evt) {
        if(evt.getButton()!=evt.BUTTON1)
            mnuPopup.show(ltFileList,evt.getX(),evt.getY());
        
        if(ltFileList.getSelectedValue()==null)
               return;
        if(!filecache.containsKey(ltFileList.getSelectedValue())){
            ServiceMessage newMsg=new ServiceMessage(Manager.GetInstance().GetMe(),peer,ServiceMessage.CODE_ASK_FILE,(String)ltFileList.getSelectedValue());
            Manager.GetInstance().GetDispatcher().DispatchToAll(newMsg);
        }else{
            tbContent.setText(((AttachmentMessage)filecache.get(ltFileList.getSelectedValue())).GetContent());
        }
    }
}

//内部类，继承自AbstractListModel类，用来控制共享文件列表的显示模式
class StringListModel extends AbstractListModel{
    String[] list;
    public StringListModel(String[] iList){
        list=iList;
    }
    public int getSize() { return list.length; }
    public Object getElementAt(int i) { return list[i]; }
}
