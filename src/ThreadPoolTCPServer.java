import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

public class ThreadPoolTCPServer {
    private static CopyOnWriteArrayList<DataOutputStream> clientOutputStreams = new CopyOnWriteArrayList<>();
    private static Object broadcastLock = new Object();

    public static void main(String args[]) {
        try {
            ExecutorService threadPool = Executors.newFixedThreadPool(10);
            int serverPort = 6666;
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (serverSocket.isBound()) {
                System.out.println("Aguardando conexao no endereco: " + InetAddress.getLocalHost() + ":" + serverPort);
                Socket clientSocket = serverSocket.accept();
                DataOutputStream clientOutput = new DataOutputStream(clientSocket.getOutputStream());
                clientOutputStreams.add(clientOutput);

                ClientHandler handler = new ClientHandler(clientSocket);
                threadPool.submit(() -> {
                    handler.run();
                    try {
                        clientSocket.close();
                        clientOutputStreams.remove(clientOutput);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println("Conexao feita com: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            }
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        }
    }

    public static void broadcast(String message) {
        synchronized (broadcastLock) {
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
}
