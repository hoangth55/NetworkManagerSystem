import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainServer {
	public static void main(String args[]) throws IOException {
		try {
			int portToSend = 26999; 
			int totalPacketUDP = 1000;
			int CLIENT_PORT_ToReceiveUDP = 10001;
			int portToReceiver = 27015;

			if (args.length > 0)
				portToSend = Integer.parseInt(args[0]);

			// The port we'll listen on
			SocketAddress localportToSend = new InetSocketAddress(portToSend);
			SocketAddress localportToReceive = new InetSocketAddress(portToReceiver);
			SocketAddress UDPlocalport = new InetSocketAddress(CLIENT_PORT_ToReceiveUDP);

			// Create and bind a tcp channel to listen for sending connections on.
			ServerSocketChannel tcpserverToSend = ServerSocketChannel.open();
			tcpserverToSend.socket().bind(localportToSend);
			
			// Create and bind a tcp channel to listen for receiving connections  on.
			ServerSocketChannel tcpserverToReceive = ServerSocketChannel.open();
			tcpserverToReceive.socket().bind(localportToReceive);

			// Also create and bind a DatagramChannel to listen on.
			DatagramChannel udpserver = DatagramChannel.open();
			udpserver.socket().bind(UDPlocalport);

			tcpserverToSend.configureBlocking(false);
			tcpserverToReceive.configureBlocking(false);
			udpserver.configureBlocking(false);

			Selector selector = Selector.open();

			tcpserverToSend.register(selector, SelectionKey.OP_ACCEPT);
			tcpserverToReceive.register(selector, SelectionKey.OP_ACCEPT);
			udpserver.register(selector, SelectionKey.OP_READ);

			ByteBuffer receiveBuffer = ByteBuffer.allocate(10);

			for (;;) {
				try { 
					selector.select();
					
					Set keys = selector.selectedKeys();

					// Iterate through the Set of keys.
					for (Iterator i = keys.iterator(); i.hasNext();) {
						// Get a key from the set, and remove it from the set
						SelectionKey key = (SelectionKey) i.next();
						i.remove();

						// Get the channel associated with the key
						Channel c = (Channel) key.channel();
						
						//Send to client to test download
						if (key.isAcceptable() && c == tcpserverToSend) {

							SocketChannel client = tcpserverToSend.accept();

							if (client != null) {
								System.out.println("Waiting connection TCP...");

								// ---------sending a file to a socketSend--
								Socket sockSend = client.socket();
								System.out.println("Accepted connection : "
										+ sockSend);
								long start2 = System.currentTimeMillis();

								//String destPathFileToSend = "C:\\Users\\Adventure\\Documents\\workspace\\NetworkManagerSystem\\src\\com\\example\\networkmanagersystem\\HHH.jpg"; // destination
								//File myFile = new File(destPathFileToSend);
								byte[] newbytearray = new byte[15000000];
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
								float bandwidth2 = contentLength2
										/ ((end2 - start2));

								System.out.println("File size was downloaded: "
										+ contentLength2 + "bytes");
								System.out.println(
								 "[BENCHMARK] Bandwidth downloading is:" +
								 bandwidth2 + "kb/s");

								sockSend.close();
								client.close(); // close connection
							}
						// Receive from client to test upload
						} else if (key.isAcceptable() && c == tcpserverToReceive){
							SocketChannel client = tcpserverToReceive.accept();
							int filesize = 80022386; 
							int bytesRead;
							int current = 0;
							String destPathFile;
							
							if (client != null) {
								Socket sock = client.socket();
								System.out.println("Accepted connection : " + sock);
								// ------------------------receive file--------
								
								long start = System.currentTimeMillis();
								byte[] mybytearray = new byte[filesize];
								InputStream is = sock.getInputStream();
								//destPathFile = "C:\\Users\\Adventure\\Documents\\workspace\\NetworkManagerSystem\\src\\com\\example\\networkmanagersystem\\Test"; // destination
								//FileOutputStream fos = new FileOutputStream(destPathFile);
	
								//BufferedOutputStream bos = new BufferedOutputStream(fos);
								bytesRead = is.read(mybytearray, 0, mybytearray.length);
								current = bytesRead;
	
								do {
									bytesRead = is.read(mybytearray, current,
											(mybytearray.length - current));
									if (bytesRead >= 0)
										current += bytesRead;
								} while (bytesRead > -1);
	
								//bos.write(mybytearray, 0, current);
								//bos.flush();
								long end = System.currentTimeMillis();
	
								System.out.println(end - start + "ms");
	
								// You can re-check the size of your file
								final long contentLength = current;
								// Bandwidth : size(KB)/time(s)
								float bandwidth = contentLength / ((end - start));
	
								System.out.println("File size was uploaded: " + contentLength
										+ "bytes");
								System.out.println( "[BENCHMARK] Bandwidth uploading is:" +
								 bandwidth + "kb/s");
								sock.close();
								//bos.close();
								
								client.close(); // close connection
							}
						// Test UDP packet loss
						} else if (key.isReadable() && c == udpserver) {
							InetSocketAddress clientAddress = (InetSocketAddress) udpserver
									.receive(receiveBuffer);
							// DatagramSocket socket = udpserver.socket();
							try {
								byte[] buf = new byte[100];
				                
								//System.out.println("Receive from android client:" + receiveBuffer.get(buf).toString());
								System.out.println("Listening...");

								if (clientAddress != null) {
									int count = 0;									
									
									// Sending response
									byte[] message = new byte[15000];
									DatagramPacket response = new DatagramPacket(
											message, message.length, clientAddress.getAddress(), CLIENT_PORT_ToReceiveUDP);
										
									DatagramSocket clientSocket = new DatagramSocket();
									
									long start = System.currentTimeMillis();
									long end;
									//Send with the bandwidth is 3 Mbps =>> time = size/3Mb
									for (int x1 = 0; x1 < 1000; x1++) {
										for (int k = 0; k < 10; k++){
											count++;
											clientSocket.send(response);	
											Thread.sleep(1);
										}
										end = System.currentTimeMillis();
										
										long time_band = message.length*count*8/(24*1024*1024); 
										long tmp = time_band*1000 - (end-start);
										
										if (tmp > 0){
											//System.out.println("Sleep: " + tmp);
											//Thread.sleep(tmp);
										}
										
									}
									/*for (int x1 = 0; x1 < 10000; x1++) {
										count++;
										clientSocket.send(response);	
										Thread.sleep(1);
									}*/

									System.out.println("Response sent " + count
											+ "packets!");
									System.out.println("--------------------");

								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} catch (java.io.IOException e) {
					Logger l = Logger.getLogger(MainServer.class.getName());
					l.log(Level.WARNING, "IOException in MainServer", e);
				} catch (Throwable t) {

					Logger l = Logger.getLogger(MainServer.class.getName());
					l.log(Level.SEVERE, "FATAL error in MainServer", t);
					System.exit(1);
				}
			}
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
	}
}
