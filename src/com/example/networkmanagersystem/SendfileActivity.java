package com.example.networkmanagersystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SendfileActivity extends Activity {
	/** Called when the activity is first created. */

	private static final int SELECT_PICTURE = 1;
	static String host = "192.168.9.101";
	static int port = 27015;
	
	private String selectedImagePath;
	private ImageView img;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendfile);
		System.out.println("34");
		img = (ImageView) findViewById(R.id.ivPic);
		System.out.println("36");
		((Button) findViewById(R.id.bBrowse))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						System.out.println("40");
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(
								Intent.createChooser(intent, "Select Picture"),
								SELECT_PICTURE);
						System.out.println("47");
					}
				});
		;
		System.out.println("51");
		Button send = (Button) findViewById(R.id.bSend);
		final TextView status = (TextView) findViewById(R.id.tvStatus);

		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sendFile();

			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);
				TextView path = (TextView) findViewById(R.id.tvPath);
				path.setText("Image Path : " + selectedImagePath);
				img.setImageURI(selectedImageUri);
			}
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	public void sendFile() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				PrintWriter out;
				try {
					Socket sock = new Socket(host, port);
					System.out.println("Connecting...");

					// sendfile
					File myFile = new File(selectedImagePath);
					byte[] mybytearray = new byte[(int) myFile.length()];
					FileInputStream fis = new FileInputStream(myFile);
					BufferedInputStream bis = new BufferedInputStream(fis);
					bis.read(mybytearray, 0, mybytearray.length);
					OutputStream os = sock.getOutputStream();
					System.out.println("Sending...");
					os.write(mybytearray, 0, mybytearray.length);
					os.flush();

					sock.close();
					
					
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