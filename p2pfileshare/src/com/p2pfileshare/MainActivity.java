package com.p2pfileshare;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import sun.security.util.Length;

import com.p2pfileshare.base.bean.Channel;
import com.p2pfileshare.base.bean.Manager;
import com.p2pfileshare.base.net.NetworkDispatcher;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	private EditText name;
    private Button update_channel;
    private EditText channel_name;
    private EditText channel_pass;
    private Button create_channel;
    private Button join_channel;
    private EditText join_channel_pass;
    

    private LayoutInflater inflater;
    private RadioGroup channel_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     
        Manager.GetInstance();//取的一个manager实例
        init_view();
        init_action();

    }

    protected void init_view()
    {
        name=(EditText) findViewById(R.id.name);
        update_channel = (Button)findViewById(R.id.btn_update_channel);
        channel_name = (EditText)findViewById(R.id.channel_name);
        channel_pass = (EditText)findViewById(R.id.channel_pass);
        create_channel = (Button) findViewById(R.id.btn_create_channel);
        join_channel = (Button) findViewById(R.id.btn_join_channel);
        join_channel_pass = (EditText)findViewById(R.id.join_channel_pass);
        inflater=LayoutInflater.from(this);
        channel_list =(RadioGroup) findViewById(R.id.channel_list);
        
    }

    protected void init_action()
    {
        update_channel.setOnClickListener(new View.OnClickListener() {//更新频道列表按钮事件
            @Override
            public void onClick(View v) {
            	UpdateChanList();
            }
        });
        create_channel.setOnClickListener(new View.OnClickListener() {//创建频道按钮事件
            @Override
            public void onClick(View v) {
                CreateChan();
            }
        });
        join_channel.setOnClickListener(new View.OnClickListener() {//更新频道列表按钮事件
            @Override
            public void onClick(View v) {
            	JoinChan();
            	
            }
        });
    }
    
  //更新频道列表
    private void UpdateChanList(){
    	    channel_list.removeAllViews();
            String[] chanlist = Manager.GetInstance().GetAvailableChannels();
            for (int i = 0; i < chanlist.length; i++) {
            	RadioButton radioButton = new RadioButton(this);      
                radioButton.setText(chanlist[i]);  
                radioButton.setTextSize(15);  
                //radioButton.setId(i);
                //radioButton.setButtonDrawable(android.R.color.transparent);//隐藏单选圆形按钮  
                radioButton.setGravity(Gravity.CENTER);         
                radioButton.setTextColor(getResources().getColorStateList(R.color.abc_search_url_text_normal));//设置选中/未选中的文字颜色 
                channel_list.addView(radioButton);//往左边的scrollview动态添加item（1，2，3，4。。。。）
            }
    }
    
    //创建频道
    private void CreateChan(){
    	
	    	//用户必须输入用户名，不充许匿名登录
	   	   if(name.getText().toString().equals("")){
	           Toast.makeText(this, "用户名称不能为空", Toast.LENGTH_SHORT).show();
	           return;
	       }
	   	   if(channel_name.getText().toString().equals(""))
	   	   {
	   		   Toast.makeText(this, "频道名称不能为空", Toast.LENGTH_SHORT).show();
	           return;
	   	   }
	   	   if(channel_pass.getText().toString().equals(""))
	   	   {
	   		   Toast.makeText(this, "频道密码不能为空", Toast.LENGTH_SHORT).show();
	           return;
	   	   }
	   	        
		   boolean NickAvail=true,ChanAvail=true;
	        
           NickAvail=Manager.GetInstance().TrySetNick(name.getText().toString());//检查用户名称是否重复
           if(!NickAvail){
               Toast.makeText(this, "用户名称已被使用", Toast.LENGTH_SHORT).show();
               return;
           }

           ChanAvail=Manager.GetInstance().IsChannelFree(channel_name.getText().toString());//检查频道名称是否重复
           if(ChanAvail){ 
        	   
        	   Channel.CreateNew(channel_name.getText().toString(),channel_pass.getText().toString());//创建频道
        	 //跳转到新建的频道界面
               Intent intent =new Intent(MainActivity.this,MainChannelActivity.class);
               //用Bundle携带数据
               Bundle bundle=new Bundle();
               bundle.putString("channel_name",channel_name.getText().toString());
               intent.putExtras(bundle);
               
               startActivity(intent);
           } 
           else{
        	   Toast.makeText(this, "此频道已经存在", Toast.LENGTH_SHORT).show();
               return; 
           }
 
	       
    }
    
  //加入已有频道
    private void JoinChan(){
    	if(name.getText().toString().equals("")){
           Toast.makeText(this, "用户名称不能为空", Toast.LENGTH_SHORT).show();
           return;
	    }
    	if(join_channel_pass.getText().toString().equals(""))
   	    {
   		   Toast.makeText(this, "请输入频道密码", Toast.LENGTH_SHORT).show();
           return;
   	    }
    	if((RadioButton)findViewById(channel_list.getCheckedRadioButtonId())==null)
    	{
    		Toast.makeText(this, "请选择一个频道", Toast.LENGTH_SHORT).show();
            return;
    	}
    	//检查同一频道内的用户名称是否惟一
    	boolean NickAvail=true;
    	NickAvail=Manager.GetInstance().TrySetNick(name.getText().toString());
    	if(!NickAvail){
    		Toast.makeText(this, "此用户名称已存在", Toast.LENGTH_SHORT).show();
            return;
        }
    	//加入频道
    	String channelname =((RadioButton)findViewById(channel_list.getCheckedRadioButtonId())).getText().toString();
    	System.out.println("频道名称是"+channelname);
        String channelpass =join_channel_pass.getText().toString();
    	try {
        	//当用户加入一个新频道时，需要对用户输入的频道密码进行认证
            Channel.JoinExisting(channelname,channelpass);
            System.out.println("正在请求加入频道……");
            synchronized(Manager.GetInstance().WaitForJoinAck){ Manager.GetInstance().WaitForJoinAck.wait(Manager.DefaultOperTimeout);}
            if(Manager.GetInstance().GetCurrentChannel()==null){
            	//如果用户密码认证错误，弹出提示信息
            	Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	
       
            
    }
    

}
