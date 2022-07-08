package connection;

public interface IConnectionListener {
    /** запустили соединение и можем с ним работать*/
    void onConnection(Connection connection);
    /** соединение приняло строчку входящую*/
    void onReceiveString(Connection connection, String value);
    /**соединение разорвалось*/
    void onDiconnect(Connection connection);



}
