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
     
        Manager.GetInstance();//ȡ��һ��managerʵ��
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
        update_channel.setOnClickListener(new View.OnClickListener() {//����Ƶ���б�ť�¼�
            @Override
            public void onClick(View v) {
            	UpdateChanList();
            }
        });
        create_channel.setOnClickListener(new View.OnClickListener() {//����Ƶ����ť�¼�
            @Override
            public void onClick(View v) {
                CreateChan();
            }
        });
        join_channel.setOnClickListener(new View.OnClickListener() {//����Ƶ���б�ť�¼�
            @Override
            public void onClick(View v) {
            	JoinChan();
            	
            }
        });
    }
    
  //����Ƶ���б�
    private void UpdateChanList(){
    	    channel_list.removeAllViews();
            String[] chanlist = Manager.GetInstance().GetAvailableChannels();
            for (int i = 0; i < chanlist.length; i++) {
            	RadioButton radioButton = new RadioButton(this);      
                radioButton.setText(chanlist[i]);  
                radioButton.setTextSize(15);  
                //radioButton.setId(i);
                //radioButton.setButtonDrawable(android.R.color.transparent);//���ص�ѡԲ�ΰ�ť  
                radioButton.setGravity(Gravity.CENTER);         
                radioButton.setTextColor(getResources().getColorStateList(R.color.abc_search_url_text_normal));//����ѡ��/δѡ�е�������ɫ 
                channel_list.addView(radioButton);//����ߵ�scrollview��̬���item��1��2��3��4����������
            }
    }
    
    //����Ƶ��
    private void CreateChan(){
    	
	    	//�û����������û�����������������¼
	   	   if(name.getText().toString().equals("")){
	           Toast.makeText(this, "�û����Ʋ���Ϊ��", Toast.LENGTH_SHORT).show();
	           return;
	       }
	   	   if(channel_name.getText().toString().equals(""))
	   	   {
	   		   Toast.makeText(this, "Ƶ�����Ʋ���Ϊ��", Toast.LENGTH_SHORT).show();
	           return;
	   	   }
	   	   if(channel_pass.getText().toString().equals(""))
	   	   {
	   		   Toast.makeText(this, "Ƶ�����벻��Ϊ��", Toast.LENGTH_SHORT).show();
	           return;
	   	   }
	   	        
		   boolean NickAvail=true,ChanAvail=true;
	        
           NickAvail=Manager.GetInstance().TrySetNick(name.getText().toString());//����û������Ƿ��ظ�
           if(!NickAvail){
               Toast.makeText(this, "�û������ѱ�ʹ��", Toast.LENGTH_SHORT).show();
               return;
           }

           ChanAvail=Manager.GetInstance().IsChannelFree(channel_name.getText().toString());//���Ƶ�������Ƿ��ظ�
           if(ChanAvail){ 
        	   
        	   Channel.CreateNew(channel_name.getText().toString(),channel_pass.getText().toString());//����Ƶ��
        	 //��ת���½���Ƶ������
               Intent intent =new Intent(MainActivity.this,MainChannelActivity.class);
               //��BundleЯ������
               Bundle bundle=new Bundle();
               bundle.putString("channel_name",channel_name.getText().toString());
               intent.putExtras(bundle);
               
               startActivity(intent);
           } 
           else{
        	   Toast.makeText(this, "��Ƶ���Ѿ�����", Toast.LENGTH_SHORT).show();
               return; 
           }
 
	       
    }
    
  //��������Ƶ��
    private void JoinChan(){
    	if(name.getText().toString().equals("")){
           Toast.makeText(this, "�û����Ʋ���Ϊ��", Toast.LENGTH_SHORT).show();
           return;
	    }
    	if(join_channel_pass.getText().toString().equals(""))
   	    {
   		   Toast.makeText(this, "������Ƶ������", Toast.LENGTH_SHORT).show();
           return;
   	    }
    	if((RadioButton)findViewById(channel_list.getCheckedRadioButtonId())==null)
    	{
    		Toast.makeText(this, "��ѡ��һ��Ƶ��", Toast.LENGTH_SHORT).show();
            return;
    	}
    	//���ͬһƵ���ڵ��û������Ƿ�Ωһ
    	boolean NickAvail=true;
    	NickAvail=Manager.GetInstance().TrySetNick(name.getText().toString());
    	if(!NickAvail){
    		Toast.makeText(this, "���û������Ѵ���", Toast.LENGTH_SHORT).show();
            return;
        }
    	//����Ƶ��
    	String channelname =((RadioButton)findViewById(channel_list.getCheckedRadioButtonId())).getText().toString();
    	System.out.println("Ƶ��������"+channelname);
        String channelpass =join_channel_pass.getText().toString();
    	try {
        	//���û�����һ����Ƶ��ʱ����Ҫ���û������Ƶ�����������֤
            Channel.JoinExisting(channelname,channelpass);
            System.out.println("�����������Ƶ������");
            synchronized(Manager.GetInstance().WaitForJoinAck){ Manager.GetInstance().WaitForJoinAck.wait(Manager.DefaultOperTimeout);}
            if(Manager.GetInstance().GetCurrentChannel()==null){
            	//����û�������֤���󣬵�����ʾ��Ϣ
            	Toast.makeText(this, "�������", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	
       
            
    }
    

}
