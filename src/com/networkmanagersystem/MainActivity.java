package com.networkmanagersystem;

import com.example.networkmanagersystem.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.content.DialogInterface;

public class MainActivity extends Activity {
	public ListView msgView;
	public ArrayAdapter<String> msgList;
	final Context context = this;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		ImageButton send = (ImageButton) findViewById(R.id.bTestTCP);
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//diaglog show to get server address from user
				LayoutInflater layoutInflater = LayoutInflater.from(context);

				View promptView = layoutInflater.inflate(R.layout.prompt_get_tcp_address, null);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

				alertDialogBuilder.setView(promptView);

				final EditText input = (EditText) promptView.findViewById(R.id.userInputAddressTCP);

				// setup a dialog window
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// get user input and set it to result
										String address = input.getText().toString();
										
										Intent intent = new Intent(MainActivity.this, ClientRSTCP.class);
										intent.putExtra("AddressServer", address);
							            startActivity(intent);      
							            finish();
										
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,	int id) {
										dialog.cancel();
									}
								});

				// create an alert dialog
				AlertDialog alertD = alertDialogBuilder.create();

				alertD.show();
				/// finish dialog
				
				//recieveFile();	
				//sendFile();
			}
		});
		
		ImageButton sendUDP = (ImageButton) findViewById(R.id.bTestUDP);
		//Button sendUDP = (Button) findViewById(R.id.bTestUDP);
		sendUDP.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//diaglog show to get server address from user
				LayoutInflater layoutInflater = LayoutInflater.from(context);

				View promptView = layoutInflater.inflate(R.layout.promt_get_udp_configure, null);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

				alertDialogBuilder.setView(promptView);

				final EditText input = (EditText) promptView.findViewById(R.id.userInputAddressUDP);

				// setup a dialog window
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// get user input and set it to result
										String address = input.getText().toString();
										
										Intent intent = new Intent(MainActivity.this, ClientReUDP.class);
										intent.putExtra("AddressServer", address);
										startActivity(intent);      
							            finish();
										//testUDPPacket();
										
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,	int id) {
										dialog.cancel();
									}
								});

				// create an alert dialog
				AlertDialog alertD = alertDialogBuilder.create();

				alertD.show();
			}
		});
		
	}
}