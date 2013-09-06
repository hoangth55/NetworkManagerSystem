package com.example.networkmanagersystem;
import java.io.*;
import java.net.*;

public class ServerToSend {

	public static void main(String[] args) throws IOException {
		// create socket
		ServerSocket servsockSend = new ServerSocket(26999);
		
		while (true) {
			System.out.println("Waiting 1...");
			
			//----------------sending a file to a socketSend-------------------------------
			Socket sockSend = servsockSend.accept();
			System.out.println("Accepted connection : " + sockSend);
			String destPathFileToSend = "C:\\Users\\Administrator\\Documents\\workspace\\NetworkManagerSystem\\src\\com\\example\\networkmanagersystem\\HHH.jpg"; // destination
			File myFile = new File(destPathFileToSend);
			byte[] newbytearray = new byte[(int) myFile.length()];
			OutputStream os = sockSend.getOutputStream();
			System.out.println("Sending...");
			os.write(newbytearray, 0, newbytearray.length);
			System.out.println("Sending successful!");
			os.flush();
			
			sockSend.close();

		}
	}

}