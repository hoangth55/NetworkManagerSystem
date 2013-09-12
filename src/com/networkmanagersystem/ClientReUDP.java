package com.networkmanagersystem;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import android.util.Log;

public class ClientReUDP implements Runnable {
	private final static int LISTENING_PORT = 10001;
	
	@Override
	public void run() {
		int count = 0;
		try {
			// Opening listening socket
			Log.d("UDP Receiver", "Opening listening socket on port "
					+ LISTENING_PORT + "...");
			DatagramSocket socket = new DatagramSocket(LISTENING_PORT);
			socket.setBroadcast(true);
			socket.setReuseAddress(true);
			socket.setSoTimeout(1000);
			try{
				for (int i = 0; i <=10000; i++) {
					// Listening on socket
					//Log.d("UDP Receiver", "Listening...");
					byte[] buf = new byte[1024];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);									
					socket.receive(packet);
					//Log.d("UDP", "Received: " + count + "packets");
					count++;	
				}
				socket.close();
				Log.d("UDP", "Total Received: " + count + "packets");
			} catch (Exception e){
				e.printStackTrace();
				//Log.d("UDP", "Total Received: " + count + "packets");
			}
			
		} catch (Exception e) {
			
			Log.e("UDP", "Receiver error", e);
		}
	}
}

