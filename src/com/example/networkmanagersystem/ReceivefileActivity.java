package com.example.networkmanagersystem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.networkmanagersystem.R;
import com.example.networkmanagersystem.R.id;
import com.example.networkmanagersystem.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ReceivefileActivity extends Activity {
	/** Called when the activity is first created. */

	static String host = "192.168.9.101";
	static int port = 26999;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receivefile);
		
		;
		//System.out.println("51");
		Button send = (Button) findViewById(R.id.receive);
		//final TextView status = (TextView) findViewById(R.id.tvStatus);

		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				recieveFile();

			}
		});
	}
	
	public void recieveFile() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				PrintWriter out;
				int filesize = 6022386; // filesize temporary hardcoded

				int bytesRead;
				int current = 0;
				try {
					Socket sock = new Socket(host, port);
					System.out.println("Connecting...");
					
					long start = System.currentTimeMillis();
					
					// 
					byte[] mybytearray = new byte[filesize];
					
					InputStream is = sock.getInputStream();
			
					
					//File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Test.jpg");
	                //file.createNewFile();
	                //FileOutputStream fos = new FileOutputStream(file);
	                
					FileOutputStream fos = openFileOutput("HHH.jpg", Context.MODE_PRIVATE);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					System.out.println( "Create successful!");
					bytesRead = is.read(mybytearray, 0, mybytearray.length);
					current = bytesRead;

					// thanks to A. Cádiz for the bug fix
					do {
						bytesRead = is.read(mybytearray, current,
								(mybytearray.length - current));
						if (bytesRead >= 0)
							current += bytesRead;
					} while (bytesRead > -1);

					bos.write(mybytearray, 0, current);
					bos.flush();
					
					long end = System.currentTimeMillis();
					System.out.println("Time to transfer is:" + (end - start) + "ms");
					
					//You can re-check the size of your file
					final long contentLength = current;
					// Bandwidth : size(KB)/time(s)
					float bandwidth = contentLength / ((end-start));
								
					System.out.println( "File size: " + contentLength + "kb");
					System.out.println( "[BENCHMARK] Bandwidth:" + bandwidth + "kb/s");
					
					
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
