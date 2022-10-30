import java.lang.*;
import java.util.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.net.Socket;
import java.io.* ;
import java.net.* ;

// response to the following command: “java Client 139.165.99.199 uliege.be”
//sending msg through TCP socket
//app may W/R byte chuks of different sizes 

//Your job is to send and recover application messages to and from the stream.
// write(may not sent after calling write) flush(force writing to socket)
//socket.setTcpNoDelay(true) to disable Nagle's algorithm(reducing the number of small packets sent over TCP)

public class Client
{
	private Socket socket;
	private OutputStream output;
	private InputStream input;

	// initiate connection parameters
	public Client(String DNS_IP){

		try{
			this.socket = new Socket(DNS_IP, 53);
			socket.setTcpNoDelay(true);
			this.output = socket.getOutputStream();
			this.input  = socket.getInputStream();
		}
		catch(UnknownHostException u) {
             System.out.println(u);
         }
        catch (IOException i) {
            System.out.println(i);
        }
	}


	public byte[] convertToDNSMessage(String QName, String QType){

		byte[] Header = new byte[6];


		byte[] Message = new byte[9];

		return Message;
	}

	public byte[] query(String QName, String QType) throws IOException{

		byte[] DNS_QueryMessage = convertToDNSMessage(QName, QType);
		// send query
		this.output.write(DNS_QueryMessage);
		this.output.flush();

		// get the total message length
		byte[] LengthBuffer = new byte[2];
		this.input.read(LengthBuffer);
		int length = ((LengthBuffer[0] & 0xff) << 8) | (LengthBuffer[1] & 0xff);


		byte[] DNS_AnswerMessage = new byte[length];
		this.input.read(DNS_AnswerMessage);

		// convert the response to Litte indian
		ByteBuffer  ResponseBuffer = ByteBuffer.allocate(length);
		ResponseBuffer.put(DNS_AnswerMessage);
		ResponseBuffer.order(ByteOrder.LITTLE_ENDIAN);

		return ResponseBuffer.array();
	}

	private String parseAnswer(byte[] ResponseBuffer){
		int length = ResponseBuffer.length; 

	}

	public void printAnswer(String QName, String QType) throws IOException{
		byte[] ResponseBuffer = query(QName, QType);

		String Answer = parseAnswer(ResponseBuffer);
		System.out.println(Answer);
	}

	public static void main(String[] args) throws IOException{
        String DNS_IP = args[0];
        String QName = args[1];
        String QType = args[2];

		Client client = new Client(DNS_IP);


		client.printAnswer(QName, QType);
	}
}


/*public class Message
{
	byte[] Header;

	this(int id, ){
		this.Header = new byte[6];
	}
}

public class QMessage extends Message
{
	byte[] Question;

	this(){
		super();

	}
	
}
public class AMessage extends Message
{
	
}*/