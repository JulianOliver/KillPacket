/*
//------------------------------------------------------------------>

_|    _|  _|  _|  _|
_|  _|        _|  _|
_|_|      _|  _|  _|
_|  _|    _|  _|  _|
_|    _|  _|  _|  _|



_|_|_|                        _|                    _|
_|    _|    _|_|_|    _|_|_|  _|  _|      _|_|    _|_|_|_|
_|_|_|    _|    _|  _|        _|_|      _|_|_|_|    _|
_|        _|    _|  _|        _|  _|    _|          _|
_|          _|_|_|    _|_|_|  _|    _|    _|_|_|      _|_|

Proposal and reference implementation v.01

//<------------------------------------------------------------------

Copyright (C) 2013  Julian Oliver

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package com.killpacket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TcpClient extends Activity
{
  int serverPort;
  String KILLSIG;
  String filename = "killpacket.cfg";
  String killSigText;  
  String serverIPText;
  Button buttonDeleteConfig;
  Button buttonExit;
  Button buttonSaveConfig;
  Button buttonSend;
  EditText serverPortField;
  EditText killSigField;
  EditText serverIPField;
  TextView sent;
  TextView textConfigure;
  TextView title;
  TextView connectionResult;  

  private void runTcpClient()
  {
	readConfig();
	try
    {
      Socket localSocket = new Socket(this.serverIPText, this.serverPort);
      new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
      BufferedWriter localBufferedWriter = new BufferedWriter(new OutputStreamWriter(localSocket.getOutputStream()));
      String str = "TCP connecting to " + this.serverPort + System.getProperty("line.separator");
      localBufferedWriter.write(str + this.killSigText);
      localBufferedWriter.flush();
      Log.i("TcpClient", "sent: \n" + str + "\n");
      localSocket.close();
      this.buttonSend.setVisibility(8);
      this.buttonExit.setVisibility(0);
      this.connectionResult.setVisibility(0); 
      TcpClient.this.connectionResult.setText("Done.\nDeleting local config.");
      deleteConfig();
      return;
    }
   /*
    catch (UnknownHostException localUnknownHostException)
    {
      //while (true)
        localUnknownHostException.printStackTrace();
    }
    */
    catch (ConnectException ConnectException)
    {
    	 Log.i("Can't reach server!", serverIPText);
    	 TcpClient.this.buttonSaveConfig.setVisibility(8);
         TcpClient.this.buttonDeleteConfig.setVisibility(0); 
         TcpClient.this.killSigField.setVisibility(8);
         TcpClient.this.serverIPField.setVisibility(8);
         TcpClient.this.serverPortField.setVisibility(8);
         TcpClient.this.buttonSend.setVisibility(8);
         TcpClient.this.buttonExit.setVisibility(0);
         TcpClient.this.connectionResult.setVisibility(0);
         TcpClient.this.connectionResult.setText("Connection Error!\nChecklist:\n* Is the server script running?\n* Are you firewalled?\n* Are you using the correct port?");
         TcpClient.this.textConfigure.setVisibility(0); 
    }
    
    catch (IOException localIOException)
    {
    	localIOException.printStackTrace();
    }
  }

  public void addListenerOnDeleteButton(Button paramButton)
  {
    paramButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        TcpClient.this.buttonSaveConfig.setVisibility(0);
        TcpClient.this.buttonDeleteConfig.setVisibility(8);
        TcpClient.this.textConfigure.setVisibility(8);
        TcpClient.this.killSigField.setVisibility(0);
        TcpClient.this.serverIPField.setVisibility(0);
        TcpClient.this.serverPortField.setVisibility(0);
        TcpClient.this.buttonSend.setVisibility(8);
        TcpClient.this.buttonExit.setVisibility(8);
    	TcpClient.this.connectionResult.setVisibility(8);

      }
    });
  }

  public void addListenerOnExitButton(Button paramButton)
  {
    paramButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.HOME");
        localIntent.setFlags(268435456);
        TcpClient.this.startActivity(localIntent);
      }
    });
  }

  public void addListenerOnSaveButton(Button paramButton)
  {
    paramButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        TcpClient.this.saveConfig();
      }
    });
  }

  public void addListenerOnSendButton(Button paramButton)
  {
    paramButton.setOnLongClickListener(new View.OnLongClickListener()
    {
      public boolean onLongClick(View paramAnonymousView)
      {
        Log.i("this is IP from config", TcpClient.this.serverIPText);
        TcpClient.this.runTcpClient();
		return true;
      }
    });
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903040);
    File localFile1 = getBaseContext().getFileStreamPath(this.filename);
    this.buttonSaveConfig = ((Button)findViewById(2131034121));
    this.buttonDeleteConfig = ((Button)findViewById(2131034115));
    this.killSigField = ((EditText)findViewById(2131034118));
    this.serverIPField = ((EditText)findViewById(2131034119));
    this.serverPortField = ((EditText)findViewById(2131034120));
    this.buttonSend = ((Button)findViewById(2131034114));
    this.buttonExit = ((Button)findViewById(2131034116));
    this.title = ((TextView)findViewById(2131034112));
    this.textConfigure = ((TextView)findViewById(2131034113));
    this.connectionResult = ((TextView)findViewById(R.id.ConnectionResult));
    this.connectionResult.setTextColor(Color.parseColor("#FF0000"));
    this.sent = ((TextView)findViewById(2131034117));
    Typeface localTypeface = Typeface.createFromAsset(getAssets(), "fonts/plain_germanica.ttf");
    this.title.setTypeface(localTypeface);
    this.buttonSend.setVisibility(8);
    this.buttonExit.setVisibility(8); 
    this.sent.setVisibility(8);
    //BufferedWriter localBufferedWriter;
    
    readConfig();
    addListenerOnSaveButton(this.buttonSaveConfig);
    addListenerOnDeleteButton(this.buttonDeleteConfig);
    addListenerOnSendButton(this.buttonSend);
    addListenerOnExitButton(this.buttonExit);
    
    if (!localFile1.exists()) {
    
    	this.buttonSend.setVisibility(8);
    	this.buttonExit.setVisibility(8);
    	this.title.setVisibility(0);
    	this.textConfigure.setVisibility(8);
    	this.buttonSaveConfig.setVisibility(0);
    	this.buttonDeleteConfig.setVisibility(8);
    	this.killSigField.setVisibility(0);
    	this.serverIPField.setVisibility(0);
    	this.serverPortField.setVisibility(0); 
    	this.connectionResult.setVisibility(8);

    }
   
    else {
    	this.buttonSend.setVisibility(0);
    	this.buttonExit.setVisibility(8);
    	this.title.setVisibility(0);
    	this.textConfigure.setVisibility(0);
    	this.buttonSaveConfig.setVisibility(8);
    	this.buttonDeleteConfig.setVisibility(0);
    	this.killSigField.setVisibility(8);
    	this.serverIPField.setVisibility(8);
    	this.serverPortField.setVisibility(8);
    	this.connectionResult.setVisibility(8);

    }
  }
  
  public void readConfig()
  {
	  BufferedReader localBufferedReader;
      
      try
      {
      	localBufferedReader = new BufferedReader(new FileReader(new File(getFilesDir() + File.separator + this.filename)));
      	new StringBuilder("");
      	for (int i = 0; i < 3; i++) {
      	if (i == 0) {
      		this.killSigText = localBufferedReader.readLine();
      	}
      	else if (i == 1)
      	{
      		this.serverIPText = localBufferedReader.readLine();
      	}
      	else
      	{
      		this.serverPort = Integer.parseInt(localBufferedReader.readLine());
      		Log.i("this is server port", String.valueOf(this.serverPort));
      	}
      }
      this.textConfigure.setText("Target server: " + this.serverIPText + "\nTarget port:    " + this.serverPort);
  	
    }

    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
  }
  
  public void deleteConfig()
  {
	  File localFile = new File(getFilesDir() + File.separator + this.filename);
	  localFile.delete();
  }
 
  public void saveConfig()
  {
    this.killSigText = this.killSigField.getText().toString();
    this.serverIPText = this.serverIPField.getText().toString();
    this.serverPort = Integer.parseInt(this.serverPortField.getText().toString());
    String str = this.killSigText + "\n" + this.serverIPText + "\n" + this.serverPort;
    File localFile = new File(getFilesDir() + File.separator + this.filename);
    try
    {
      BufferedWriter localBufferedWriter = new BufferedWriter(new FileWriter(localFile));
      localBufferedWriter.write(str);
      localBufferedWriter.close();
      Log.i("logged config to ", getFilesDir().getAbsolutePath() + "/" + this.filename);
      this.title.setVisibility(0); 
      this.buttonSend.setVisibility(0);
      this.textConfigure.setVisibility(8);
      this.buttonSaveConfig.setVisibility(8);
      this.killSigField.setVisibility(8);
      this.serverIPField.setVisibility(8);
      this.serverPortField.setVisibility(8);
      this.buttonDeleteConfig.setVisibility(8);
      this.buttonExit.setVisibility(0);
  	  this.connectionResult.setVisibility(8);

      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
}
