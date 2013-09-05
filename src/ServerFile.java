import java.io.*;
import java.net.*;

public class ServerFile {

	public static void main(String[] args) throws IOException {
		int filesize = 6022386; // filesize temporary hardcoded

		int bytesRead;
		int current = 0;

		// create socket
		ServerSocket servsock = new ServerSocket(27015);
		while (true) {
			System.out.println("Waiting...");

			Socket sock = servsock.accept();
			System.out.println("Accepted connection : " + sock);

			// receive file
			long start = System.currentTimeMillis();
			byte[] mybytearray = new byte[filesize];
			InputStream is = sock.getInputStream();
			FileOutputStream fos = new FileOutputStream(
					"C:\\Users\\Administrator\\Documents\\workspace\\NetworkManagerSystem\\src\\com\\example\\networkmanagersystem\\Test.jpg"); // destination
			// path and
			// name of
			// file
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
						
			System.out.println( "File size: " + contentLength + "kb");
			System.out.println( "[BENCHMARK] Bandwidth:" + bandwidth + "kb/s");
			
			bos.close();
			
			sock.close();
		}
	}

}