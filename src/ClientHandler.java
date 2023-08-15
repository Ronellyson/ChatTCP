import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

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
    try {
        while (true) {
            String data = input.readUTF();
            if (data == null) {
                System.out.println("Received null data. Client disconnected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                break;
            }
            if (data.equalsIgnoreCase("bye")) {
                System.out.println("Client disconnected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                break;
            }
            System.out.println("Mensagem recebida: " + data);

            ThreadPoolTCPServer.broadcast("Broadcast: " + data);
        }
        } catch (IOException e) {
            System.out.println("Erro durante a troca de mensagens: " + e.getMessage());
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                    ThreadPoolTCPServer.removeClientOutputStream(clientSocket);
                }
            } catch (IOException e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }
}
