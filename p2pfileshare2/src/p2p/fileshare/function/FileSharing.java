package p2p.fileshare.function;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import p2p.fileshare.Manager;


/**
 * @author gl
 * 主要用于实现文件共享功能
 */
public class FileSharing {
	
	//用于存储共享的文件列表
    private LinkedList<File> SharedFiles=new LinkedList<File>();
    //基本的共享目录
    private File BaseShareFolder;
    
    //共享目录
    public void ShareDir(String iPath){
        BaseShareFolder=new File(iPath);
        SharedFiles.clear();
        if(!BaseShareFolder.exists())
            return;
        AddShareDir(BaseShareFolder);
    }
    //取消共享
    public void Unshare(){
        BaseShareFolder=null;
        SharedFiles.clear();
    }
    //取得共享文件的列表
    public String[] GetSharedFiles(){
        String[] outArr=new String[SharedFiles.size()];
        for(int i=0;i<SharedFiles.size();i++)
            outArr[i]=((File)SharedFiles.get(i)).getAbsolutePath().substring(BaseShareFolder.getAbsolutePath().length());
            
        Arrays.sort(outArr);
        return outArr;
    }
    //读取每一个共享文件的全路径
    public String GetFullFilePath(String iFile){
        if(BaseShareFolder==null) return "";
        return BaseShareFolder.getAbsolutePath()+iFile;
    }
    //增加共享目录
    private void AddShareDir(File iDir){
        File[] fList=iDir.listFiles();
        for(int i=0;i<fList.length;i++){
            
            if(fList[i].isFile()){ 
                if(fList[i].length()>Manager.GetInstance().GetDispatcher().GetMaxFileSize())
                    continue;
                SharedFiles.add(fList[i]);
            }else if(fList[i].isDirectory())
                AddShareDir(fList[i]);
        }
    }
    
    
    public FileSharing() {
    }
    
    public FileSharing(String iShareFld) {
        ShareDir(iShareFld);
    }
    
}
