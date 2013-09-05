import java.io.*;
import java.net.*;
import java.util.*;

public class ServerNMS {

	private static final String USAGE = "Usage: java ServerNMS";

	/** Default port number on which this server to be run. */
	private static final int PORT_NUMBER = 8009;

	/**
	 * List of print writers associated with current clients, one for each.
	 */
	private List<PrintWriter> clients;

	/** Creates a new server. */
	public ServerNMS() {
		clients = new LinkedList<PrintWriter>();
	}

	/** Starts the server. */
	public void start() {
		System.out.println("AndyChat server started on port " + PORT_NUMBER
				+ "!");

		try {
			ServerSocket s = new ServerSocket(PORT_NUMBER);
			DatagramSocket incomingUDP = new DatagramSocket(PORT_NUMBER);
			for (;;) {
				Socket incoming = s.accept();
				new ClientHandler(incoming, incomingUDP).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception");
		}
		System.out.println("AndyChat server stopped.");
	}

	/** Adds a new client identified by the given print writer. */
	private void addClient(PrintWriter out) {
		synchronized (clients) {
			clients.add(out);
		}
	}

	/** Adds the client with given print writer. */
	private void removeClient(PrintWriter out) {
		synchronized (clients) {
			clients.remove(out);
		}
	}

	/** Broadcasts the given text to all clients. */
	private void broadcast(String msg) {
		for (PrintWriter out : clients) {
			out.println(msg);
			out.flush();
		}
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			System.out.println(USAGE);
			System.exit(-1);
		}
		new ServerNMS().start();
	}

	/**
	 * A thread to serve a client. This class receive messages from a client and
	 * broadcasts them to all clients including the message sender.
	 */
	private class ClientHandler extends Thread {

		/** Socket to read client messages. */
		private Socket incoming;
		private DatagramSocket incomingUDP;

		/** Creates a hander to serve the client on the given socket. */
		public ClientHandler(Socket _incoming, DatagramSocket _incomingUDP) {
			this.incoming = _incoming;
			this.incomingUDP = _incomingUDP;

		}

		/** Starts receiving and broadcasting messages. */
		public void run() {
			PrintWriter out = null;
			try {

				/*
				 * byte[] buf = new byte[2048]; DatagramPacket packet = new
				 * DatagramPacket(buf, buf.length);
				 * System.out.print("Server: Receiving\n");
				 * incomingUDP.receive(packet);
				 * 
				 * System.out.print("Receiving successful packet with :" +
				 * packet.getLength() + "kb");
				 */
				
				
				

				out = new PrintWriter(new OutputStreamWriter(
						incoming.getOutputStream()));

				// inform the server of this new client
				ServerNMS.this.addClient(out);

				out.print("Welcome to AndyChat! ");
				out.println("Enter BYE to exit.");
				out.flush();

				BufferedReader in = new BufferedReader(new InputStreamReader(
						incoming.getInputStream()));
				for (;;) {
					String msg = in.readLine();
					if (msg == null) {
						break;
					} else {
						if (msg.trim().equals("BYE"))
							break;
						System.out.println("Received: " + msg
								+ "With size is: " + msg.getBytes().length
								+ "bytes");
						// broadcast the receive message
						ServerNMS.this.broadcast(msg);
					}
				}
				incoming.close();

				ServerNMS.this.removeClient(out);
			} catch (Exception e) {
				if (out != null) {
					ServerNMS.this.removeClient(out);
				}
				e.printStackTrace();
			}
		}
	}
}
