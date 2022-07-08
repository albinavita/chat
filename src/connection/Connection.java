package connection;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class Connection {

    private final Socket socket;
    private final Thread thread;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final IConnectionListener listener;

    /**Socket создается изнутри*/
    public Connection(IConnectionListener listener, String ipAddress, int port ) throws IOException{
        this(listener, new Socket(ipAddress, port));
    }

    /**конструктор принимает готовый объект сокета
     *  и с этим сокетом создаст соединение */
    public Connection(IConnectionListener listener, Socket socket) throws IOException {
       this.listener = listener;
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onConnection(Connection.this);
                    while(!thread.isInterrupted()) {
                        String msg = reader.readLine();
                        listener.onReceiveString(Connection.this, msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    listener.onDiconnect(Connection.this);
                }
            }
        });
        thread.start();
    }

    /**отправить сообщение*/
    public synchronized void send(String value){
        try {
            writer.write(value + "\r\n");
            writer.flush();
        } catch (IOException e) {
           e.printStackTrace();
            disconnect();
        }
    }

    /**разорвать соединение*/
    public synchronized  void disconnect(){
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "\t" + socket.getInetAddress() + " : " + socket.getPort();
    }
}
