import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatTCPClient {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public ChatTCPClient() {
    }

    public void start(String serverIp, int serverPort) throws IOException {
        try {
            socket = new Socket(serverIp, serverPort);
            output = new DataOutputStream(socket.getOutputStream());

            System.out.println("[C2] Conexão estabelecida, eu sou o cliente: " + socket.getLocalSocketAddress());

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Digite uma mensagem (ou 'bye' para sair): ");
                String msg = scanner.nextLine();

                if (msg.equalsIgnoreCase("bye")) {
                    String message = "Thau server :)";
                    output.writeUTF(message);
                    output.flush();
                    String response = input.readUTF();
                    System.out.println(response);
                }

                output.writeUTF(msg);
                output.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    public void stop() {
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverIp = "127.0.0.1";
        int serverPort = 6666;
        try {
            ChatTCPClient client = new ChatTCPClient();
            client.start(serverIp, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
