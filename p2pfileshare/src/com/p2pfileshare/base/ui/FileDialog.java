
package com.p2pfileshare.base.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

/**
 * @author gl
 * 此类主要用于弹出一个文件对话框，提供用户流浏览文件、选择目录、择路径等功能。
 */
public class FileDialog {
    private static File selfile;
    
    private static File GenericFileDialog(int iType){
        return GenericFileDialog(iType,JFileChooser.FILES_ONLY);
    }
   
    //主要是调用javax.swing包下的JDialog类和JFileChooser类来实现相关操作，代码有相对固定的模型。
    private static File GenericFileDialog(int iType,int iSelMode){
        selfile=null;
        final JDialog fDiag=new JDialog();
        fDiag.setModal(true);
        
        final JFileChooser fc=new JFileChooser();
        fc.setDialogType(iType);
        
        fc.setFileSelectionMode(iSelMode);
        fc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                if(evt.getActionCommand().equals("ApproveSelection"))
                    FileDialog.selfile=fc.getSelectedFile();
                    fDiag.setVisible(false);
                
                }
            
        });
        fDiag.getContentPane().add(fc);
        fDiag.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        fDiag.setBounds(300, 300, 400, 300);
        fDiag.setVisible(true);
        return fc.getSelectedFile();
        
        
    }
    
    public static File DirFileDialog(){
        return GenericFileDialog(JFileChooser.OPEN_DIALOG ,JFileChooser.DIRECTORIES_ONLY);
    }

    public static File OpenFileDialog(){
        return GenericFileDialog(JFileChooser.OPEN_DIALOG);
    }
    
    public static File SaveFileDialog(){
        return GenericFileDialog(JFileChooser.SAVE_DIALOG);
    }
    
    
}
