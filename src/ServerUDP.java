import java.io.*;
import java.net.*;

public class ServerUDP {
	private final static int SERVER_PORT = 10000;
    private final static int CLIENT_PORT = 10001;
    
	public static void main(String[] args) throws IOException {
		int totalPacketUDP = 10000;
		
		InetAddress clientAddr = null;
        DatagramSocket socket = null;
       try {
            //Initializing the UDP server
            System.out.println(String.format("Connecting on %s...", SERVER_PORT));
            socket = new DatagramSocket(SERVER_PORT);
            System.out.println("Connected.");
            System.out.println("====================");
        } catch (SocketException e) {
            e.printStackTrace();
        }
		
		while (true) {	
			//--------------------Testing UDP Packets------------------------
			try {
                //Listening
                byte[] buf = new byte[100];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                System.out.println("Listening...");
                socket.receive(packet);
                int count = 0;
                
                //Getting client address from the packet we received
                clientAddr = packet.getAddress();
                System.out.println("Received: '" + new String(packet.getData()).trim() + "' from "+clientAddr.toString());

                //Sending response
                byte[] message = ("Hello Android").getBytes();
                DatagramPacket response = new DatagramPacket(message, message.length, clientAddr, CLIENT_PORT);
                DatagramSocket clientSocket = new DatagramSocket();
                System.out.println("Sending: '" + new String(message) + "'");
                
                for (int i = 0; i < totalPacketUDP; i++)
				{	
					count++;
					clientSocket.send(response);
					Thread.sleep(9/9);
				}
                
                System.out.println("Response sent " + count + "packets!");
                System.out.println("--------------------");
            } catch (Exception e) {
                e.printStackTrace();
            }
           
		}
		
	}

}