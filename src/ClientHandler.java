import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class ClientHandler extends Thread {
    private DataInputStream input;
    private Socket clientSocket;

    public ClientHandler(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            input = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        Thread inputThread = new Thread(() -> {
            try {
                while (true) {
                    String data = input.readUTF();
                    if (data.equalsIgnoreCase("Thau server :)")) {
                        break;
                    }
                    System.out.println("Mensagem recebida: " + data);
                    ThreadPoolTCPServer.broadcast("Broadcast: " + data);
                }
            } catch (IOException e) {
                System.out.println("Erro durante a troca de mensagens: " + e.getMessage());
            }
        });

        inputThread.start();

        scanner.close();
    }
}
