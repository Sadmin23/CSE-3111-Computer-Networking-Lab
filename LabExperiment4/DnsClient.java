import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class DnsClient {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("localhost");

        DnsMessage packet = new DnsMessage((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);

        // Construct the header data for the DNS message
        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putShort((short) packet.identification);
        buffer.putShort((short) packet.flags);
        buffer.putShort((short) packet.numQuestions);
        buffer.putShort((short) packet.numAnswerRRs);
        buffer.putShort((short) packet.numAuthorityRRs);
        buffer.putShort((short) packet.numAdditionalRRs);

        byte[] headerData = buffer.array();

        // Construct the message body with additional data if needed
        byte[] message = headerData;

        DatagramPacket sendPacket = new DatagramPacket(message, message.length, serverAddress, 53);
        socket.send(sendPacket);

        socket.close();
    }
}
