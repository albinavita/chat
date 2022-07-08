package server;

import connection.Connection;
import connection.IConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server implements IConnectionListener {

    List<Connection> connections = new ArrayList<>();

    public static void main(String[] args) {
        new Server();
    }
    private Server(){
        System.out.println("Сервер запущен...");
        try(ServerSocket serverSocket = new ServerSocket(1234)){
            while (true){
                try {
                    new Connection(this, serverSocket.accept());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnection(Connection connection) {
        connections.add(connection);
        sendMessage("Новый участник вошел в чат: " + connection);
    }

    @Override
    public synchronized void onReceiveString(Connection connection, String value) {
        sendMessage(value);
    }

    @Override
    public synchronized void onDiconnect(Connection connection) {
        connections.remove(connection);
        sendMessage("Участник вышел из чата: " + connection);
    }

    private void sendMessage(String value){
        System.out.println(value);
        for( int i = 0; i < connections.size(); i++ ){
            connections.get(i).send(value);
        }
    }

}
