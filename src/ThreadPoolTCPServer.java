import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTCPServer {
    private static final int PORT = 6666;
    private static List<DataOutputStream> clientOutputStreams = new ArrayList<>();
    private static Scanner serverScanner = new Scanner(System.in);

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor aguardando conexões na porta " + PORT);

            Thread serverInputThread = new Thread(() -> {
                while (true) {
                    String serverMessage = readServerInput();
                    sendToAllClients("Servidor: " + serverMessage);
                }
            });
            serverInputThread.start();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                DataOutputStream clientOutput = new DataOutputStream(clientSocket.getOutputStream());
                clientOutputStreams.add(clientOutput);

                System.out.println("Conexão estabelecida com: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                ClientHandler handler = new ClientHandler(clientSocket);
                pool.execute(handler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message) {
        synchronized (clientOutputStreams) {
            for (DataOutputStream clientOutput : clientOutputStreams) {
                try {
                    clientOutput.writeUTF(message);
                    clientOutput.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendToAllClients(String message) {
        synchronized (clientOutputStreams) {
            for (DataOutputStream clientOutput : clientOutputStreams) {
                try {
                    clientOutput.writeUTF(message);
                    clientOutput.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void removeClientOutputStream(Socket clientSocket) {
        synchronized (clientOutputStreams) {
            clientOutputStreams.removeIf(output -> output == null || output.equals(clientSocket));
        }
    }

    public static String readServerInput() {
        return serverScanner.nextLine();
    }
}
