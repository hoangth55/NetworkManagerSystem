package com.networkmanagersystem;

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
import java.net.SocketException;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Tramsfer extends Activity {
	/** Called when the activity is first created. */

	private Handler handler = new Handler();
	private static final int SELECT_FILE = 1;
	static String host = "192.168.9.105";
	static int portSending = 27015;
	static int portReceiving = 26999;

	static int portUDPTesting = 2700;
	static int totalPacketUDP = 1;

	public ListView msgView;
	public ArrayAdapter<String> msgList;



	private String selectedFilePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendfile);

		msgView = (ListView) findViewById(R.id.listView);

		msgList = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		msgView.setAdapter(msgList);

		/*((Button) findViewById(R.id.bBrowse))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {

						Intent intent = new Intent();
						intent.setType("data/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(
								Intent.createChooser(intent, "Select File"),
								SELECT_FILE);

						msgView.smoothScrollToPosition(msgList.getCount() - 1);
					}
				});
		;*/

		Button send = (Button) findViewById(R.id.bSend);
		// final TextView status = (TextView) findViewById(R.id.tvStatus);

		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				recieveFile();	
				sendFile();
			}
		});
		
		Button sendUDP = (Button) findViewById(R.id.bTestUDP);
		sendUDP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testUDPPacket();
			}
		});
		
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_FILE) {
				Uri selectedFileUri = data.getData();
				// selectedImagePath = getPath(selectedImageUri);
				selectedFilePath = selectedFileUri.getPath();
				if (selectedFilePath.startsWith("/mimetype/")) {
					String trimmedFilePath = selectedFilePath
							.substring("/mimetype/".length());
					selectedFilePath = trimmedFilePath
							.substring(trimmedFilePath.indexOf("/"));
				}
				// TextView path = (TextView) findViewById(R.id.tvPath);
				// path.setText("File Path : " + selectedFilePath);
				// img.setImageURI(selectedImageUri);
			}
		}
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

					// thanks to A. Cádiz for the bug fix
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

	public void testUDPPacket() {
		// new Thread(new ClientReUDP()).start();
		new Thread(new Runnable() {
			private final static int CLIENT_PORT_ToReceiveUDP = 10001;
			int totalPacket = 0;
			public void run() {

				try {
					// Opening listening socket
					Log.d("UDP Receiver", "Opening listening socket on port "
							+ CLIENT_PORT_ToReceiveUDP + "...");
					DatagramSocket socket = new DatagramSocket(CLIENT_PORT_ToReceiveUDP);
					//socket.setBroadcast(true);
					socket.setReuseAddress(true);
					socket.setSoTimeout(1000);
					
					byte[] buf = new byte[1024];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					for (int i = 0; i <= 10000; i++) {
						// Listening on socket
						// Log.d("UDP Receiver", "Listening...");
						socket.receive(packet);
						totalPacket++;	
					}
					socket.close();
					
				} catch (Exception e) {
					e.printStackTrace();
					displayMsg("Total packet was lost is:" + (10000 - totalPacket) + "/10000 packets was sended! ");
					//displayMsg("-------Finish testing..........");
					Log.e("UDP", "Receiver error", e);
				}
			}
			

		}).start();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		new Thread(new ClientSeUDP()).start();
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