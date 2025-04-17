package Server.Controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientConnection implements Runnable {
    private Socket socket;
    private ConnectionController connectionController;
    private ObjectInputStream inputStream;
    private OutputStream outputStream;

    public ClientConnection(Socket socket, ConnectionController connectionController){
        this.socket = socket;
        this.connectionController = connectionController;
        try {
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try{
            System.out.println("Hallåja");
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            closeConnection();
        }
    }

    private void closeConnection(){
        try{
            if(socket != null){
                socket.close();
            }
            if(inputStream != null){
                inputStream.close();
            }
            if(outputStream != null){
                outputStream.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
