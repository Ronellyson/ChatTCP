import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
            input = new DataInputStream(socket.getInputStream());

            System.setOut(new PrintStream(System.out, true, "UTF-8"));

            System.out.println("[C2] Conexão estabelecida, eu sou o cliente: " + socket.getLocalSocketAddress());
            final boolean[] receivedBroadcast = {false};
            boolean isFirstMessage = true;

            Thread receiveThread = new Thread(() -> {
                try {
                    String received;
                    while ((received = input.readUTF()) != null) {
                        System.out.println("Broadcast recebido: " + received);
                        synchronized (receivedBroadcast) {
                            receivedBroadcast[0] = true;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Conexão com o servidor fechada.");
                }
            });

            receiveThread.start();
            Scanner scanner = new Scanner(System.in);
            while (socket != null) {
                synchronized (receivedBroadcast) {
                    if (receivedBroadcast[0] || isFirstMessage) {
                        System.out.print("Digite uma mensagem (ou 'bye' para sair): ");
                        
                        String msg = scanner.nextLine();
                        if (msg.equalsIgnoreCase("bye")) {
                            String message = "Tchau, servidor :)";
                            output.writeUTF(message);
                            break;
                        }
                        output.writeUTF(msg);
                        receivedBroadcast[0] = false;
                        isFirstMessage = false;
                    }
                }
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
