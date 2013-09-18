package com.example.networkmanagersystem;

import java.io.*;
import java.net.*;

public class Server {
	private final static int SERVER_PORT = 10000;
    private final static int CLIENT_PORT = 10001;
	
	public static void main(String[] args) throws IOException {
		int filesize = 80022386; // filesize temporary hardcoded

		int bytesRead;
		int current = 0;

		String destPathFile;

		int totalPacketUDP = 10000;

		InetAddress clientAddr = null;
		DatagramSocket socket = null;
		/*try {
			// Initializing the UDP server
			System.out.println(String
					.format("Connecting on %s...", SERVER_PORT));
			socket = new DatagramSocket(SERVER_PORT);
			System.out.println("Connected.");
			System.out.println("====================");
		} catch (SocketException e) {
			e.printStackTrace();
		}*/

		// create socket
		ServerSocket servsock = new ServerSocket(27015);
		ServerSocket servsockSend = new ServerSocket(26999);

		while (true) {
			/*--------------Server old
			 * try {
				
				DatagramSocket servSockUDPTesting = new DatagramSocket();
				InetAddress servAddr = InetAddress.getByName("192.168.1.105");
				
				byte[] buf;
				buf = ("Default message").getBytes();
			
				DatagramPacket packet = new DatagramPacket(buf, buf.length, servAddr, 2700);
				servSockUDPTesting.setSoTimeout(2000);
				for (int i = 0; i < totalPacketUDP; i++)
				{	
					count++;
					servSockUDPTesting.send(packet);
					Thread.sleep(1000);
				}
				servSockUDPTesting.close();
			} 
			 catch ( SocketException e) {
				System.out.println("***** UDP server has: " + "Socket Exception");
			} catch ( UnknownHostException e ) {
				System.out.println("***** UDP server has: " + "UnknownHostException");
			} catch (IOException e){
				System.out.println("***** UDP server has IOException"+ "e: " + e);
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Server: Error!\n");
			}
			System.out.println("Sending " + count + " packets to client");
			 */

			System.out.println("Waiting connection TCP...");
			// ----------------sending a file to a
			// socketSend-------------------------------
			Socket sockSend = servsockSend.accept();
			System.out.println("Accepted connection : " + sockSend);
			long start2 = System.currentTimeMillis();

			String destPathFileToSend = "C:\\Users\\Adventure\\Documents\\workspace\\NetworkManagerSystem\\src\\com\\example\\networkmanagersystem\\HHH.jpg"; // destination
			File myFile = new File(destPathFileToSend);
			byte[] newbytearray = new byte[(int) myFile.length()];
			OutputStream os = sockSend.getOutputStream();
			// System.out.println("Sending to client...");
			os.write(newbytearray, 0, newbytearray.length);
			// System.out.println("Sending successful!");
			os.flush();

			long end2 = System.currentTimeMillis();

			System.out.println(end2 - start2 + "ms");

			// You can re-check the size of your file
			final long contentLength2 = newbytearray.length;
			// Bandwidth : size(KB)/time(s)
			float bandwidth2 = contentLength2 / ((end2 - start2));

			System.out.println("File size was downloaded: " + contentLength2
					+ "bytes");
			// System.out.println( "[BENCHMARK] Bandwidth downloading is:" +
			// bandwidth2 + "kb/s");

			sockSend.close();

			Socket sock = servsock.accept();
			System.out.println("Accepted connection : " + sock);
			// ------------------------receive file------------------------------------------
			long start = System.currentTimeMillis();
			byte[] mybytearray = new byte[filesize];
			InputStream is = sock.getInputStream();
			destPathFile = "C:\\Users\\Adventure\\Documents\\workspace\\NetworkManagerSystem\\src\\com\\example\\networkmanagersystem\\Test.jpg"; // destination
			FileOutputStream fos = new FileOutputStream(destPathFile);

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

			// You can re-check the size of your file
			final long contentLength = current;
			// Bandwidth : size(KB)/time(s)
			float bandwidth = contentLength / ((end - start));

			System.out.println("File size was uploaded: " + contentLength
					+ "bytes");
			// System.out.println( "[BENCHMARK] Bandwidth uploading is:" +
			// bandwidth + "kb/s");
			sock.close();
			bos.close();
		}
	}
}