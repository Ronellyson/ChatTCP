import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SimpleTCPClient {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public void start(String serverIp, int serverPort) throws IOException {
        try {
            // Cria socket de comunicação com o servidor e obtém canais de entrada e saída
            System.out.println("[C1] Conectando com servidor " + serverIp + ":" + serverPort);
            socket = new Socket(serverIp, serverPort);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            // Espera mensagem ser digitada da entrada padrão (teclado)
            System.out.println("[C2] Conexão estabelecida, eu sou o cliente: " + socket.getLocalSocketAddress());

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Digite uma mensagem (ou 'bye' para sair): ");
                String msg = scanner.nextLine();

                if (msg.equalsIgnoreCase("bye")) {
                    String message = "Thau server :)";
                    output.writeUTF(message);
                    // Recebendo resposta do servidor
                    System.out.println(input.readUTF());
                    break;
                }

                // Envia mensagem para o servidor no canal de saída
                System.out.println("[C3] Enviando mensagem para servidor");
                output.writeUTF(msg);
                System.out.println("[C4] Mensagem enviada, recebendo resposta");

                // Recebendo resposta do servidor
                String response = input.readUTF();
                System.out.println(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop(); // Garante que a conexão seja fechada
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
        String serverIp = "127.0.0.1"; // Altere para o endereço IP correto do servidor
        int serverPort = 6666;
        try {
            // Cria e roda cliente
            SimpleTCPClient client = new SimpleTCPClient();
            client.start(serverIp, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
