package com.p2pfileshare;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import sun.security.util.Length;

import com.p2pfileshare.base.bean.Channel;
import com.p2pfileshare.base.bean.Manager;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainChannelActivity extends ActionBarActivity {

	private EditText name;
    private Button update_channel;
    private EditText channel_name;
    private EditText channel_pass;
    private Button create_channel;
    private Button join_channel;

    private LayoutInflater inflater;
    private LinearLayout channel_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_channel);
        Manager.GetInstance();//ȡ��һ��managerʵ��
       //�����ϸ�ҳ�������
        Bundle bundle = this.getIntent().getExtras();
        //����nameֵ
        String channel_name = bundle.getString("channel_name");
        this.setTitle(channel_name);//���ñ�ҳ���Ƶ������ 
        init_view();
        init_action();

    }

    protected void init_view()
    {
        
        
    }

    protected void init_action()
    {
        
	       
    }
    
  
}

