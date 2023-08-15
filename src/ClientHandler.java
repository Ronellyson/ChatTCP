import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class ClientHandler extends Thread {
    DataInputStream input;
    DataOutputStream output;
    Socket clientSocket;

    public ClientHandler(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            input = new DataInputStream(clientSocket.getInputStream());
            output = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String data = input.readUTF();
                if(data.equals("Thau server :)")){
                    String message = "Até mais cliente :)" + clientSocket.getInetAddress();
                    output.writeUTF(message);
                    break;
                }
                System.out.println("Mensagem recebida: " + data);

                System.out.print("Digite uma mensagem (ou 'bye' para sair): ");
                String msg = scanner.nextLine();

                if (msg.equalsIgnoreCase("bye")) {
                    msg = "Até mais cliente :)" + clientSocket.getInetAddress();
                    output.writeUTF(msg);
                    break;
                }

                output.writeUTF(msg);
                System.out.println("Resposta enviada: " + msg);
            }

            scanner.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Erro durante a troca de mensagens: " + e.getMessage());
        }
    }
}