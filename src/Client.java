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


	public byte[] craftDNSQuery(String QName, String QType){

		byte[] Header = new byte[6];


		byte[] Message = new byte[9];

		return Message;
	}

	public byte[] query(String QName, String QType) throws IOException{

		byte[] DNS_QueryMessage = craftDNSQuery(QName, QType);
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
		ByteBuffer  responseBuffer = ByteBuffer.allocate(length);
		responseBuffer.put(DNS_AnswerMessage);
		responseBuffer.order(ByteOrder.LITTLE_ENDIAN); // it may be usefull to get  

		return responseBuffer.array();
	}

	private String parseAnswer(byte[] responseBuffer) {

		String Answer = new String();
		int len = responseBuffer.length;

		boolean QR = responseBuffer[len - 16][7];

		int QDCOUNT = ((responseBuffer[len-4] & 0xff) << 8) | (responseBuffer[len-3] & 0xff);
		int ANCOUNT = ((responseBuffer[len-6] & 0xff) << 8) | (responseBuffer[len-5] & 0xff);


		// number of possible Question bytes to skip
		int questions_length = 0;
		for (int i = 0, newRR = 6*2+1, len_domaine = responseBuffer[newRR];
			 i < QNCOUNT;
			 i++, newRR = responseBuffer[len_domaine + 2*2])
		{
			questions_length + newRR + 4*2 + RDLENGTH;

			("Answer (TYPE="++", TTL="++", DATA=\"MS"++"\")")
		}

//		int end_Question_Entries = QDCOUNT + questions_length;




		// looping over Record Resoures in the answer cas
		for (int i = 0, newRR = questions_length+1, len_name = responseBuffer[newRR],
			 RDLENGTH = responseBuffer[len_name+4*2+1];
			 i < ANCOUNT;
			 i++, newRR = responseBuffer[len_name + 4*2 + RDLENGTH + 1],
			  len_name = responseBuffer[newRR], RDLENGTH = responseBuffer[len_name+4*2+1])
		{
			System.stdout
		}

		// return .toString();
	}

	public void printAnswer(String QName, String QType) throws IOException{

		byte[] responseBuffer = query(QName, QType);
		String Answer = parseAnswer(responseBuffer);
		System.out.println(Answer);
	}

	public static void main(String[] args) throws IOException{
        String DNS_IP = args[0];
        String QName = args[1];
        String QType = args[2];

		Client client = new Client(DNS_IP);

		StdOut.println("Question (NS="+DNS_IP+", NAME="+QName+", TYPE="+QType+")");


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