package com.example.networkmanagersystem;

import java.io.*;
import java.net.*;

public class ServerFile {

	public static void main(String[] args) throws IOException {
		int filesize = 80022386; // filesize temporary hardcoded

		int bytesRead;
		int current = 0;
		String destPathFile;

		// create socket
		ServerSocket servsock = new ServerSocket(27015);
		ServerSocket servsockSend = new ServerSocket(26999);
		
		while (true) {
			System.out.println("Waiting 1...");
			
			
			//----------------sending a file to a socketSend-------------------------------
			Socket sockSend = servsockSend.accept();
			System.out.println("Accepted connection : " + sockSend);
			long start2 = System.currentTimeMillis();
			String destPathFileToSend = "C:\\Users\\Administrator\\Documents\\workspace\\NetworkManagerSystem\\src\\com\\example\\networkmanagersystem\\HHH.jpg"; // destination
			File myFile = new File(destPathFileToSend);
			byte[] newbytearray = new byte[(int) myFile.length()];
			OutputStream os = sockSend.getOutputStream();
			//System.out.println("Sending to client...");
			os.write(newbytearray, 0, newbytearray.length);
			//System.out.println("Sending successful!");
			os.flush();
			
			long end2 = System.currentTimeMillis();

			System.out.println(end2 - start2 + "ms");
			
			//You can re-check the size of your file
			final long contentLength2 = newbytearray.length;
			// Bandwidth : size(KB)/time(s)
			float bandwidth2 = contentLength2 / ((end2-start2));
						
			System.out.println( "File size was downloaded: " + contentLength2 + "bytes");
			System.out.println( "[BENCHMARK] Bandwidth downloading is:" + bandwidth2 + "kb/s");
			
			
			sockSend.close();
			
			
			Socket sock = servsock.accept();
			System.out.println("Accepted connection : " + sock);

			// ------------------------receive file------------------------------------------
			long start = System.currentTimeMillis();
			byte[] mybytearray = new byte[filesize];
			InputStream is = sock.getInputStream();
			destPathFile = "C:\\Users\\Administrator\\Documents\\workspace\\NetworkManagerSystem\\src\\com\\example\\networkmanagersystem\\Test.jpg"; // destination
			FileOutputStream fos = new FileOutputStream(destPathFile);
					
			// path and name of file
			// 
			// 
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
			long end = System.currentTimeMillis();

			System.out.println(end - start + "ms");
			
			//You can re-check the size of your file
			final long contentLength = current;
			// Bandwidth : size(KB)/time(s)
			float bandwidth = contentLength / ((end-start));
						
			System.out.println( "File size was uploaded: " + contentLength + "bytes");
			System.out.println( "[BENCHMARK] Bandwidth uploading is:" + bandwidth + "kb/s");
			sock.close();
			bos.close();
			
			

		}
	}

}