package com.networkmanagersystem;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.util.Log;

public class ClientSeUDP implements Runnable {
	private final static String SERVER_ADDRESS = "192.168.9.198";// public ip of my server
																	
	private final static int SERVER_PORT = 10000;

	@Override
	public void run() {
		try {
			// Preparing the socket
			InetAddress serverAddr = InetAddress.getByName(SERVER_ADDRESS);
			DatagramSocket socket = new DatagramSocket();

			// Preparing the packet
			byte[] buf = ("Hello computer").getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length,
					serverAddr, SERVER_PORT);

			// Sending the packet
			Log.d("UDP", String.format("Sending: '%s' to %s:%s",
					new String(buf), SERVER_ADDRESS, SERVER_PORT));
			socket.send(packet);
			Log.d("UDP", "Packet sent.");
		} catch (Exception e) {
			Log.e("UDP", "Client error", e);
		}
	}
}
