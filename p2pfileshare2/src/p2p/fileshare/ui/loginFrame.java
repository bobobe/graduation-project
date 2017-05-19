package p2p.fileshare.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.json.*;

import p2p.fileshare.Channel;
import p2p.fileshare.Manager;
import p2p.fileshare.net.HttpUtil;

/**
 * @author yb
 * 用户登录的界面，也是系统启动的开始界面，继承自JFrame类
 */
public class loginFrame extends JFrame {
	
	
	 // 变量声明
    
    private JLabel jlname;
    private JLabel jlpass;
    private JTextField jtname;
    private JPasswordField jppass;
    private JButton btOK;
    private JLabel jlline;
    private JLabel jlreg;
    // 变量声明结束
	
    
    //单例模式的结构
	private static loginFrame singObj;
    public static loginFrame GetInstance(){
        if(singObj==null)
            singObj=new loginFrame();
        return singObj;
    }
    //构造方法，建立一个登录界面实例
    private loginFrame() { 
        initComponents();
        this.setBounds(400, 200, 280, 380);
        this.update(this.getGraphics());
    }
    
      /**
     * return_type : void
     * TODO:以下方面主要用于界面元素的设计和布局的，不涉及业务方法，没有特殊需要请不要改动
     */
    private void initComponents() {
    	jtname = new JTextField();
        jlname = new JLabel();
        jlpass = new JLabel();
        btOK = new JButton();
        jppass = new JPasswordField();
        jlline = new JLabel();
        jlreg= new JLabel();
        getContentPane().setLayout(null);
        /*组件初始化*/
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //设置界面的标题
        setTitle("用户登录");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(255, 255, 255));
        //setName("frmStart");
        setResizable(false);
        

        //用户名称
        jlname.setText("用户名称:");
        getContentPane().add(jlname);
        jlname.setBounds(10, 100, 70, 20);
        getContentPane().add(jtname);
        jtname.setBounds(80, 100, 180, 20);
        
        //用户密码
        jlpass.setText("用户密码:");
        getContentPane().add(jlpass);
        jlpass.setBounds(10, 135, 70, 20);
        getContentPane().add(jppass);
        jppass.setBounds(80, 135, 180, 20);
        
        //确定按钮
        btOK.setText("确定");
        getContentPane().add(btOK);
        btOK.setBounds(90, 200, 80, 23);
        btOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btOKActionPerformed(evt);
            }
        });
        
      //分隔线
        jlline.setFont(new java.awt.Font("Tahoma", 1, 12));
        jlline.setText("*******************************");
        getContentPane().add(jlline);
        jlline.setBounds(10, 270, 260, 14);
     
        //注册
        jlreg.setText("还未注册？");
        jlreg.setForeground(Color.BLUE);
        getContentPane().add(jlreg);
        jlreg.setBounds(90, 300, 80, 23);
        jlreg.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				// TODO Auto-generated method stub
				regFrame.GetInstance().setVisible(true);
				dispose();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				jlreg.setForeground(Color.RED);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				jlreg.setForeground(Color.BLUE);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        });
        
        pack();
    }//组件初始化结束

    
    //用户加入一个已经有的频道时的操作，以下是点击“确定”按钮时需要执行的动作
    private void btOKActionPerformed(ActionEvent evt) {
    	
    	//用户必须输入用户名，不充许匿名登录
    	 if(GetName().length()==0){
            JOptionPane.showMessageDialog(this,"用户名称不能为空","错误",JOptionPane.ERROR_MESSAGE);
            return;
         }
    	 if(GetPass().length()==0){
             JOptionPane.showMessageDialog(this,"用户密码不能为空","错误",JOptionPane.ERROR_MESSAGE);
             return;
         }
    	 sendLogin();//请求服务器验证登录信息
        
     
    }
    
    private void sendLogin()
    {	
    	//包装参数
    	JSONObject jb=null;
    	try 
    	{
    		jb = new JSONObject();
		    jb.put("name", GetName());
		    jb.put("pass", GetPass());
    	} catch (JSONException e1) {
		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}

		JSONObject jsresult =HttpUtil.postHttp("http://127.0.0.1/Android_interface/controller/fs_login.php", jb);
		
		if(jsresult == null)
		{
			JOptionPane.showMessageDialog(this,"请求失败，请检查网络","错误",JOptionPane.ERROR_MESSAGE);
            return;
		}
		else 
		{
			String flag = "";
			try {
				flag = jsresult.getJSONObject("data").getString("flag");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(flag.equals("1"))//成功登录
			{
				//JOptionPane.showMessageDialog(this,"成功登录","正确",JOptionPane.ERROR_MESSAGE);
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", GetName());//传递用户名
				chatStart.GetInstance(map).setVisible(true);
				dispose();
				return;
			}
			else if(flag.equals("0"))//用户不存在
			{
				JOptionPane.showMessageDialog(this,"用户名不存在","错误",JOptionPane.ERROR_MESSAGE);
	             return;
			}
			else if(flag.equals("2"))//密码错误
			{
				JOptionPane.showMessageDialog(this,"密码错误","错误",JOptionPane.ERROR_MESSAGE);
	             return;
			}
		}
    }

    
    public String GetName(){
        return jtname.getText().toString();
    }
    
    @SuppressWarnings("deprecation")
	public String GetPass(){
        return jppass.getText().toString();
    }
    
}
