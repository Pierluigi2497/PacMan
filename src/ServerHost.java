import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerHost implements Runnable{
    public String line;
    public Socket socket;
    public ServerSocket listener;
    public BufferedReader readerChannel;
    public BufferedWriter writerChannel;
    public void run(){
        try{
            while(true){
                readerChannel = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writerChannel = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                try {
                    writerChannel.write(new Date().toString() + "\n\r");
                    writerChannel.flush();
                    //Invio al nuovo arrivato, le opzioni di partita (al momento esiste solo la difficoltà)
                    writerChannel.write("8:"+Main.difficulty + "\n\r");
                    writerChannel.flush();
                    ServerAccept.updatePlayers();
                    while ((line = readerChannel.readLine()) != null) {
                        //Quando ricevo qualcosa, lo invio a tutti i client
                        if(line.compareTo("")!=0) {
                            ServerAccept.sendData(line);
                            System.out.println(line);
                        }
                    }
                } finally{
                    //Quando il client si disconnette
                    socket.close();
                }
            }
        }catch (Exception e){}
        finally {
            //Quando il client è disconnesso
            try {
                listener.close();
            }catch (Exception e){}
        }
    }

    public ServerHost(ServerSocket listener){
        this.listener=listener;
        serverAccept();
    }

    public void serverAccept(){
        //Mi blocco finchè non arriva una nuova connessione
        try{
            System.out.println("Accettando");
            socket = listener.accept();
            System.out.println("Accettato");
        }
        catch (IOException e){e.printStackTrace();}
    }

    public void sendPlayers(){
        try{
            writerChannel.write("P:"+ServerAccept.NPlayer + "\n\r");
            writerChannel.flush();
        }catch (Exception e){e.printStackTrace();}
    }

    public void sendData(String line){
        try{
            writerChannel.write(line+"\r");
            writerChannel.flush();
        }catch (Exception e){e.printStackTrace();}
    }
}
