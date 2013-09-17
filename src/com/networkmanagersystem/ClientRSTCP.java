package com.networkmanagersystem;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.networkmanagersystem.R;
import com.example.networkmanagersystem.R.layout;
import com.example.networkmanagersystem.R.menu;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ClientRSTCP extends Activity {
	private Handler handler = new Handler();
	static String host;
	static int portSending = 27015;
	static int portReceiving = 26999;

	static int portUDPTesting = 2700;
	static int totalPacketUDP = 1;

	public ListView msgView;
	public ArrayAdapter<String> msgList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_rstcp);
		msgView = (ListView) findViewById(R.id.listViewTCP);

		msgList = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		msgView.setAdapter(msgList);
		
		Button back = (Button) findViewById(R.id.TCPBack);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ClientRSTCP.this, MainActivity.class);
	            startActivity(intent);      
	            finish();
			}
		});
		//get address server from users
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			host = extras.getString("AddressServer");
		}
		
		sendFile();
		recieveFile();
		
	}

	
	
	public void sendFile() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Socket sock = new Socket(host, portSending);
					//displayMsg("Connecting to sending...");

					long start = System.currentTimeMillis();
					// sendfile
					//File myFile = new File(selectedFilePath);
					byte[] mybytearray = new byte[5000000];
					//FileInputStream fis = new FileInputStream(myFile);
					//BufferedInputStream bis = new BufferedInputStream(fis);
					//bis.read(mybytearray, 0, mybytearray.length);
					OutputStream os = sock.getOutputStream();

					os.write(mybytearray, 0, mybytearray.length);
					os.flush();

					sock.close();

					long end = System.currentTimeMillis();
					//displayMsg("Sending...");

					displayMsg("Time to uploading is:" + (end - start) + "ms");

					// You can re-check the size of your file
					final long contentLength = mybytearray.length;
					// Bandwidth : size(KB)/time(s)
					float bandwidth = contentLength / ((end - start));

					displayMsg("File size was uploaded: " + contentLength/1024
							+ "kb");
					displayMsg("[BENCHMARK] Bandwidth uploading is:"
							+ bandwidth + "kb/s");

				} catch (UnknownHostException e) {
					e.printStackTrace();
					Log.d("", "hello222");
				} catch (IOException e) {
					e.printStackTrace();
					Log.d("", "hello4333");
				}

			}
		}).start();
	}

	public void recieveFile() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				int filesize = 6022386; // filesize temporary hardcoded

				int bytesRead;
				int current = 0;
				try {
					Socket sock = new Socket(host, portReceiving);
					//displayMsg("Connecting to receiving...");

					long start = System.currentTimeMillis();

					//
					byte[] mybytearray = new byte[filesize];

					InputStream is = sock.getInputStream();

					// File file = new
					// File(Environment.getExternalStorageDirectory() +
					// File.separator + "Test.jpg");
					// file.createNewFile();
					// FileOutputStream fos = new FileOutputStream(file);

					FileOutputStream fos = openFileOutput("HHH.jpg",
							Context.MODE_PRIVATE);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					bytesRead = is.read(mybytearray, 0, mybytearray.length);
					current = bytesRead;

					// thanks to A. C�diz for the bug fix
					do {
						bytesRead = is.read(mybytearray, current,
								(mybytearray.length - current));
						if (bytesRead >= 0)
							current += bytesRead;
					} while (bytesRead > -1);

					bos.write(mybytearray, 0, current);
					bos.flush();
					sock.close();

					long end = System.currentTimeMillis();

					//displayMsg("Receiving....");
					displayMsg("Time to downloading is:" + (end - start) + "ms");

					// You can re-check the size of your file
					final long contentLength = current;
					// Bandwidth : size(KB)/time(s)
					float bandwidth = contentLength / ((end - start));

					displayMsg("File size was downloaded: " + contentLength/1024
							+ "kb");
					displayMsg("[BENCHMARK] Bandwidth downloading is:"
							+ bandwidth + "kb/s");

				} catch (UnknownHostException e) {
					e.printStackTrace();
					Log.d("", "hello222");
				} catch (IOException e) {
					e.printStackTrace();
					Log.d("", "hello4333");
				}

			}
		}).start();
	}
	
	public void displayMsg(String msg) {
		final String mssg = msg;

		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				msgList.add(mssg);
				msgView.setAdapter(msgList);
				msgView.smoothScrollToPosition(msgList.getCount() - 1);
				//Log.d("", "hi");
			}
		});

	}

}