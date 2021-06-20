import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.Date;
public class ServerClient implements Runnable{
    public BufferedWriter writerChannel;
    private boolean running=false;
    public int numberOfPlayer=0;
    private int id=99; //1-Pacman 2-Pink 3-Red 4-Blue 5-Yellow 99-PacmanSinglePlayer
    Socket s;

    public ServerClient(Boolean multi) throws Exception{
        if(multi){
            System.out.println("Perimna");
            s = new Socket("127.0.0.1", 4000);
            System.out.println("Dopo");
        }else{
            //Non faccio niente perchè mi servivano solo le variabili inizializzate
        }
    }
    public void run(){
        try {
            writerChannel = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            BufferedReader readerChannel = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String line;
            running=true;

            writerChannel.write(new Date().toString() + "\n\r");
            writerChannel.flush();
            while ((line = readerChannel.readLine()) != null) {
                //System.out.println(line);

                    try {
                        //Se non è un messaggio inviato da me
                           if(line.charAt(0)!=String.valueOf(getID()).charAt(0)) {
                               //Se è un messaggio valido
                               //P-Numero di giocatori
                               //1-Pacman
                               //2-Pink
                               //3-Red
                               //4-Blue
                               //5-Yellow
                               //6-Informazioni generali di gioco
                               //7-Informazioni per i fantasmini(da smistare)
                               if (line.charAt(0) != String.valueOf(id).charAt(0)) {
                                   switch (line.charAt(0)) {
                                       case 'P': {
                                           String[] splitted = line.split(":");
                                           numberOfPlayer = Integer.parseInt(splitted[1]);
                                           if (id == 99) {
                                               //Setto id solo una volta
                                               id = numberOfPlayer;
                                           }
                                           System.out.println("Numebrs: " + numberOfPlayer + "\n");
                                       }
                                       break;
                                       case '2': {
                                           //Pink
                                           String[] splitted = line.split(":");
                                           String[] path = splitted[1].split(",");
                                           Main.ne[0].controller.changeDir = path[0].charAt(0);
                                           Main.ne[0].controller.pathx = Integer.parseInt(path[1]);
                                           Main.ne[0].controller.pathy = Integer.parseInt(path[2]);
                                           Main.ne[0].controller.tX = Integer.parseInt(path[3]);
                                           Main.ne[0].controller.tY = Integer.parseInt(path[4]);
                                           Main.ne[0].controller.stop = Boolean.parseBoolean(path[5]);
                                           //Main.ne[0].controller.vel = Integer.parseInt(path[6]);
                                           Main.ne[0].start = Boolean.parseBoolean(path[7]);
                                           Main.ne[0].ready = Boolean.parseBoolean(path[8]);

                                       }
                                       break;
                                       case '3': {
                                           //Red
                                           String[] splitted = line.split(":");
                                           String[] path = splitted[1].split(",");
                                           Main.ne[1].controller.changeDir = path[0].charAt(0);
                                           Main.ne[1].controller.pathx = Integer.parseInt(path[1]);
                                           Main.ne[1].controller.pathy = Integer.parseInt(path[2]);
                                           Main.ne[1].controller.tX = Integer.parseInt(path[3]);
                                           Main.ne[1].controller.tY = Integer.parseInt(path[4]);
                                           Main.ne[1].controller.stop = Boolean.parseBoolean(path[5]);
                                           //Main.ne[1].controller.vel = Integer.parseInt(path[6]);
                                           Main.ne[1].start = Boolean.parseBoolean(path[7]);
                                           Main.ne[1].ready = Boolean.parseBoolean(path[8]);
                                       }
                                       break;
                                       case '4': {
                                           //Blue
                                           String[] splitted = line.split(":");
                                           String[] path = splitted[1].split(",");
                                           Main.ne[2].controller.changeDir = path[0].charAt(0);
                                           Main.ne[2].controller.pathx = Integer.parseInt(path[1]);
                                           Main.ne[2].controller.pathy = Integer.parseInt(path[2]);
                                           Main.ne[2].controller.tX = Integer.parseInt(path[3]);
                                           Main.ne[2].controller.tY = Integer.parseInt(path[4]);
                                           Main.ne[2].controller.stop = Boolean.parseBoolean(path[5]);
                                           //Main.ne[2].controller.vel = Integer.parseInt(path[6]);
                                           Main.ne[2].start = Boolean.parseBoolean(path[7]);
                                           Main.ne[2].ready = Boolean.parseBoolean(path[8]);
                                       }
                                       break;
                                       case '5': {
                                           //Yellow
                                           String[] splitted = line.split(":");
                                           String[] path = splitted[1].split(",");
                                           Main.ne[3].controller.changeDir = path[0].charAt(0);
                                           Main.ne[3].controller.pathx = Integer.parseInt(path[1]);
                                           Main.ne[3].controller.pathy = Integer.parseInt(path[2]);
                                           Main.ne[3].controller.tX = Integer.parseInt(path[3]);
                                           Main.ne[3].controller.tY = Integer.parseInt(path[4]);
                                           Main.ne[3].controller.stop = Boolean.parseBoolean(path[5]);
                                           //Main.ne[3].controller.vel = Integer.parseInt(path[6]);
                                           Main.ne[3].start = Boolean.parseBoolean(path[7]);
                                           Main.ne[3].ready = Boolean.parseBoolean(path[8]);

                                       }
                                       break;
                                       case '1': {
                                           String[] splitted = line.split(":");
                                           String[] path = splitted[1].split(",");
                                           Main.pg.controller.changeDir = path[0].charAt(0);
                                           Main.pg.controller.pathx = Integer.parseInt(path[1]);
                                           Main.pg.controller.pathy = Integer.parseInt(path[2]);
                                           Main.pg.controller.tX = Integer.parseInt(path[3]);
                                           Main.pg.controller.tY = Integer.parseInt(path[4]);
                                           Main.pg.controller.stop = Boolean.parseBoolean(path[5]);
                                           Main.pg.controller.vel = Integer.parseInt(path[6]);

                                       }
                                       break;
                                       case '6': {
                                           String[] splitted = line.split(":");
                                           String[] path = splitted[1].split(",");
                                        //   Main.gOver = Boolean.parseBoolean(path[0]);
                                           Pg.Restart = Boolean.parseBoolean(path[1]);
                                           Main.startGame = Boolean.parseBoolean(path[2]);
                                       //    Main.Life = Integer.parseInt(path[3]);
                                       //    Main.pulse.win = Boolean.parseBoolean(path[4]);
                                       //    Main.Level = Integer.parseInt(path[5]);
                                       }break;
                                       case '7': {
                                           String[] splitted = line.split(":");
                                           String[] path = splitted[1].split(",");
                                           int l=Integer.parseInt(String.valueOf(line.charAt(2)))-2;
                                           System.out.println("Valore di l: "+l);
                                           Main.ne[l].controller.changeDir = path[0].charAt(0);
                                           Main.ne[l].controller.pathx = Integer.parseInt(path[1]);
                                           Main.ne[l].controller.pathy = Integer.parseInt(path[2]);
                                           Main.ne[l].controller.tX = Integer.parseInt(path[3]);
                                           Main.ne[l].controller.tY = Integer.parseInt(path[4]);
                                           Main.ne[l].controller.stop = Boolean.parseBoolean(path[5]);
                                           Main.ne[l].controller.vel = Integer.parseInt(path[6]);
                                           Main.ne[l].start = Boolean.parseBoolean(path[7]);
                                           Main.ne[l].ready = Boolean.parseBoolean(path[8]);
                                       }
                                   }
                               }
                           }

                    } catch (IndexOutOfBoundsException g) {
                        //Sono qui perchè line era vuoto
                        //non faccio nulla
                    }

            }
        }catch(Exception e){
            e.printStackTrace();
            running=false;
            System.out.println("Errore in qualcosa in server");}
    }

    public void sendDir(String s) throws Exception{
        writerChannel.write(s+"\n\r");
        writerChannel.flush();
        System.out.flush();
    }

    public boolean getStatus(){
        return running;
    }

    public int getNumberOfPlayer(){
        return numberOfPlayer;
    }

    public int getID(){
        return id;
    }

    public boolean getRunning(){
        return running;
    }
}