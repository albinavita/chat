package client;

import connection.Connection;
import connection.IConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, IConnectionListener {

    // адрес сервера
    private static final String SERVER_HOST = "localhost";
    // порт
    private static final int SERVER_PORT = 1234;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private final JTextArea area = new JTextArea();
    private final JTextField fieldName = new JTextField("alba");
    private final JTextField fieldInput = new JTextField();

    private Connection connection;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        area.setEditable(false);
        area.setLineWrap(false);
        add(area, BorderLayout.CENTER);
        //enter
        fieldInput.addActionListener(this);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldName, BorderLayout.NORTH);

        setVisible(true);
        try {
            connection = new Connection(this, SERVER_HOST, SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if (msg.equals("")) return;
        fieldInput.setText(null);
        connection.send(fieldName.getText() + " : " + msg);
    }

    @Override
    public void onConnection(Connection connection) {
        printMsg("Подключение готово... " );
    }

    @Override
    public void onReceiveString(Connection connection, String value) {
        printMsg(value);
    }

    @Override
    public void onDiconnect(Connection connection) {
        printMsg("Соединение закрыто... " );
    }

    private synchronized void printMsg (String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                area.append((msg + "\n"));
                area.setCaretPosition(area.getDocument().getLength());
            }
        });
    }


}
