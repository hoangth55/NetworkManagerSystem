package com.example.networkmanagersystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Client extends Activity {

	private Handler handler = new Handler();
	public ListView msgView;
	public ArrayAdapter<String> msgList;
	
	static String host = "192.168.9.101";
	static int port = 8009;
	
	byte[] buf = new byte[1024];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);

		Button btnTest = (Button) findViewById(R.id.btn_Starting);

		btnTest.setOnClickListener(new View.OnClickListener() {

			@Override 
			public void onClick(View v) {
				// TODO Auto-generated method stub

				sendPacket();
  
				Intent intent = new Intent(getBaseContext(), SendfileActivity.class);
				startActivity(intent);
				
			}
		});
	}

	public void sendPacket() {
		new Thread(new Runnable() {
			final String str = "Test TCP Connection to Server";
			@Override
			public void run() {
				PrintWriter out;
				try {
					InetAddress group = InetAddress.getByName(host);
					
					Socket socket = new Socket(host, port);
					out = new PrintWriter(socket.getOutputStream());

					out.println(str);
			
					out.flush();
					

			        
					
					
					
					
					byte[] inbuf = new byte[1024];			    			        
					DatagramPacket packet = new DatagramPacket(inbuf, 1024, group, port);
					
					DatagramSocket socket2 = new DatagramSocket(port, group);
					
					socket2.send(packet);
					
					
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

}

/*
long startTime = System.currentTimeMillis();
long endTime = System.currentTimeMillis();

// Log
Log.d("", "[BENCHMARK] Dowload time :"+(endTime-startTime)+" ms");

//You can re-check the size of your file
final long contentLength = inbuf.length;
// Bandwidth : size(KB)/time(s)
float bandwidth = contentLength / ((endTime-startTime) *1000);
			
Log.d("", "[BENCHMARK] Bandwidth:" + bandwidth + "kb/s");
*/