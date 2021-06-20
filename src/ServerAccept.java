import java.net.ServerSocket;
public class ServerAccept implements Runnable {
    private static ServerSocket listener;
    private static ServerHost[] clients=new ServerHost[5];
    private static Thread[] clientsThread= new Thread[5];
    public static int NPlayer=0;

    @Override
    public void run() {
        if(createServer()){
            for(int i=0;i<5;i++){
                clients[i]=new ServerHost(listener);
                NPlayer++;
                clientsThread[i]=new Thread(clients[i]);
                clientsThread[i].start();

            }
        }else{
            System.out.println("Errore creazione server");
        }
        System.out.println("Sono qui");
    }

    private static boolean createServer(){
        try {
            listener = new ServerSocket(4000);
            return true;
        }catch(Exception e){e.printStackTrace();return false;}
    }

    public static void updatePlayers(){
        for(int i=0;i<NPlayer;i++){
            try {
                clients[i].sendPlayers();
            }catch (Exception e){e.printStackTrace();}
        }
    }

    public static void sendData(String line){
        for(int i=0;i<NPlayer;i++){
            clients[i].sendData(line);
        }
    }
}
