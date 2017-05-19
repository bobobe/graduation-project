
package p2p.fileshare.message;

import java.io.*;
import java.security.*;
import java.util.*;

import p2p.fileshare.Peer;

public class AttachmentMessage extends Message implements Serializable {
    private String filename;                //文件名
    private int filelength;                 //文件大小
    private byte[] filecontent;             //文件内容
    private byte[] checksum;                //文件的完整性较验
    private boolean requested;              //用来表示此文件是被Peer发送过来的，还请查看其它Peer共享得到的
    //构造方法，用于创建一个AttachmentMessage实例
    public AttachmentMessage(Peer iFrom,File iFile,boolean iReq) throws Exception {
        super(iFile.getName(),iFrom);
        filename=iFile.getName();
        filelength=(int)iFile.length();
        filecontent=new byte[filelength];
        requested=iReq;
        FileInputStream fIn=new FileInputStream(iFile);
        fIn.read(filecontent);
        fIn.close();
        checksum=CalcDigest();
    }
    
    public void SetFileName(String iName){
        filename=iName;
    }
    public String GetFileName(){
        return filename;
    }
    
    public boolean equals (Object obj){
        if(!(obj instanceof AttachmentMessage))
            return false;
        AttachmentMessage tMsg=(AttachmentMessage)obj;
        return GetSender().equals(tMsg.GetSender()) && filename.equals(tMsg.filename);
    }
    public boolean IsRequested(){
        return requested;
    }

    //通过较验值检查文件的完整性
    public boolean CheckDigest() throws Exception{
        return Arrays.equals(checksum,CalcDigest());
    }
    //通过MD5算法对文件进行较验加密
    private byte[] CalcDigest() throws Exception{
        MessageDigest md=MessageDigest.getInstance("MD5");
        return md.digest(filecontent);
    }
    
    public byte[] GetBytes(){
        return filecontent;
    }
    
    public int GetLength(){
        return filelength;
    }
    
    //将文件存储到本地
    public void SaveToFile(String iPath) throws Exception{
        FileOutputStream fOut=new FileOutputStream(iPath);
        fOut.write(filecontent);
        fOut.close();
    }
    
    public String toString(){
        return "[" + GetSender() + "] " + filename + " ( " + filelength + " bytes)";
    }
    //显示文件内容
    public String GetContent(){
       char []chArr=new char[filecontent.length];
       for(int i=0;i<filecontent.length;i++)
           chArr[i]=(char)filecontent[i];
       return new String(chArr);
    }
}
