package com.networkmanagersystem;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

import com.example.networkmanagersystem.R;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

public class ClientRSTCP extends Activity {
	private Handler handler = new Handler();
	static String host;
	static int portSending = 27015;
	static int portReceiving = 26999;

	public ListView msgView;
	public ArrayAdapter<String> msgList;

	public static Runnable runnable;
	public static Thread thread;
	public long totalTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_rstcp);
		msgView = (ListView) findViewById(R.id.listViewTCP);

		msgList = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		msgView.setAdapter(msgList);

		ImageButton back = (ImageButton) findViewById(R.id.TCPBack);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ClientRSTCP.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// get address server from users
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			host = extras.getString("AddressServer");

			runnable = new recieveFile();
			thread = new Thread(runnable);
			thread.start();
			
			try {
				thread.join();
				runnable = new sendFile();
				thread = new Thread(runnable);
				thread.start();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			
		}
	}

	public class sendFile implements Runnable {

		public void run() {
			try {
				Socket sockSend = new Socket(host, portSending);
				// displayMsg("Connecting to sending...");
				long startSend = System.currentTimeMillis();

				// sendfile
				// File myFile = new File(selectedFilePath);
				byte[] mybytearray = new byte[20000000];
				// FileInputStream fis = new FileInputStream(myFile);
				// BufferedInputStream bis = new BufferedInputStream(fis);
				// bis.read(mybytearray, 0, mybytearray.length);
				OutputStream os = sockSend.getOutputStream();

				os.write(mybytearray, 0, mybytearray.length);
				os.flush();
				sockSend.close();
				
				long end = System.currentTimeMillis();

				// displayMsg("Sending...");
				//displayMsg("Time at starting upload: " + startSend);
				//displayMsg("Time at finishing upload: " + end);
				displayMsg("Time to uploading is:" + (end - startSend) + "ms");

				// You can re-check the size of your file
				final long contentLength = mybytearray.length;
				totalTime = end - startSend;
				// Bandwidth : size(KB)/time(s)
				double bandwidth = contentLength / (totalTime) * 8;
				DecimalFormat dcf = new DecimalFormat("#.00");

				displayMsg("File size was uploaded: " + contentLength / 1024
						+ "kb");
				displayMsg("[BENCHMARK] Bandwidth uploading is: "
						+ dcf.format(bandwidth / 1024) + "Mbps");

			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	};

	public class recieveFile implements Runnable {

		public void run() {
			int filesize = 16022386;

			int bytesRead;
			int current = 0;
			try {
				Socket sock = new Socket(host, portReceiving);
				// displayMsg("Connecting to receiving...");
				//
				byte[] mybytearray = new byte[filesize];

				InputStream is = sock.getInputStream();
				
				// File file = new
				// File(Environment.getExternalStorageDirectory() +
				// File.separator + "Test.jpg");
				// file.createNewFile();
				// FileOutputStream fos = new FileOutputStream(file);

				//FileOutputStream fos = openFileOutput("HHH.jpg", Context.MODE_PRIVATE);
				//BufferedOutputStream bos = new BufferedOutputStream(fos);
				bytesRead = is.read(mybytearray, 0, mybytearray.length);
				current = bytesRead;
				long startReceive = System.currentTimeMillis();
				do {
					bytesRead = is.read(mybytearray, current,
							(mybytearray.length - current));
					if (bytesRead >= 0)
						current += bytesRead;
				} while (bytesRead > -1);

				//bos.write(mybytearray, 0, current);
				//bos.flush();
				sock.close();

				long end1 = System.currentTimeMillis();

				// displayMsg("Receiving....");
				//displayMsg("Time at starting download: " + startReceive);
				//displayMsg("Time at finishing download: " + end1);
				displayMsg("Time to downloading is:" + (end1 - startReceive)
						+ "ms");

				// You can re-check the size of your file
				final long contentLength = current;
				totalTime = end1 - startReceive;
				// Bandwidth : size(KB)/time(s)
				double bandwidth = contentLength / (totalTime) * 8;
				DecimalFormat dcf = new DecimalFormat("#.00");

				displayMsg("File size was downloaded: " + contentLength / 1024
						+ "kb");
				displayMsg("[BENCHMARK] Bandwidth downloading is:"
						+ dcf.format(bandwidth / 1024) + " Mbps");
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void displayMsg(String msg) {
		final String mssg = msg;
		handler.post(new Runnable() {
			public void run() {
				msgList.add(mssg);
				msgView.setAdapter(msgList);
				msgView.smoothScrollToPosition(msgList.getCount() - 1);
			}
		});

	}

}
