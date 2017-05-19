
package p2p.chat.ui;

import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import p2p.chat.message.AttachmentMessage;
/**
 * @author gl
 *  接收文件的界面，Peer通过此界面来查看收到的文件列表、查看文件内容、将文件存储到本地等。
 */
public class recvFilesFrame extends JFrame {
	
	
    // 界面变量声明
    private JScrollPane jScrollPane1;
    private JList ltFiles;
    private JPopupMenu mnuPopup;
    private JMenuItem mnuSaveas;
    private JSplitPane panSplit;
    private JScrollPane scrollArea;
    private JTextArea tbFileContent;
    //声明结束
    
    private AttachmentMessage []files;
    //创建一个接收文件界面的实例
    public recvFilesFrame(AttachmentMessage []iArr) {
        initComponents();
        files=iArr;
        ltFiles.setModel(new FileListModel(files));
        this.setBounds(300, 200, 300, 300);
        setVisible(true);
    }
    
   //进行界面组件元素的初始化
    private void initComponents() {
        mnuPopup = new JPopupMenu();
        mnuSaveas = new JMenuItem();
        panSplit = new JSplitPane();
        jScrollPane1 = new JScrollPane();
        ltFiles = new JList();
        scrollArea = new JScrollPane();
        tbFileContent = new JTextArea();
        //将接收到的文件存储到本地，另存为操作
        mnuSaveas.setLabel("另存为...");
        mnuSaveas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSaveasActionPerformed(evt);
            }
        });

        mnuPopup.add(mnuSaveas);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        setTitle("接收文件");
        setName("frmRecvFiles");
        panSplit.setDividerLocation(100);
        panSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
        ltFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ltFilesMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(ltFiles);

        panSplit.setTopComponent(jScrollPane1);

        tbFileContent.setBackground(new java.awt.Color(204, 204, 204));
        tbFileContent.setColumns(20);
        tbFileContent.setRows(5);
        scrollArea.setViewportView(tbFileContent);

        panSplit.setRightComponent(scrollArea);

        getContentPane().add(panSplit);

        pack();
    }
    //执行存储文件的功能
    private void mnuSaveasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSaveasActionPerformed
       if(ltFiles.getSelectedIndex()>=0){
           AttachmentMessage curSel=(AttachmentMessage)ltFiles.getSelectedValue();
            java.io.File oFile=FileDialog.SaveFileDialog();
            
            if(oFile==null)
                return;
            try {
                curSel.SaveToFile(oFile.getPath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,ex.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            
        }
    }

  //从显示文件名的共享列表中，鼠标双击，就可以显示此文件的内容，这里的方法就是用来执行鼠标的动作，是否需要显示文件内容
    private void ltFilesMouseClicked(java.awt.event.MouseEvent evt) {
        if(evt.getButton()==java.awt.event.MouseEvent.BUTTON1){
            if(ltFiles.getSelectedIndex()>=0){
                AttachmentMessage curSel=(AttachmentMessage)ltFiles.getSelectedValue();
                tbFileContent.setText(curSel.GetContent());
            }
        }else{
            mnuPopup.show(this,evt.getX(),evt.getY());
        }
    }
    


    
}
//内部类，继承自AbstractListModel类，用来控制接收文件列表的显示模式
class FileListModel extends AbstractListModel{
    AttachmentMessage[] list;
    public FileListModel(AttachmentMessage[] iList){
        list=iList;
    }
    public int getSize() { return list.length; }
    public Object getElementAt(int i) { return list[i]; }
}