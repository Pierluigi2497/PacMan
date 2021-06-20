import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main{
    static int stateOfGame=0;//0-Menu 1-SinglePlayer 2-Multiplayer 3-Options 4-Lobby
    static Frame f=new Frame();
    static Audio audio=new Audio();
    static Thread audioThread=new Thread(audio);
    static Ne[] ne=new Ne[4];
    static BufferedImage img;
    static Pg pg;
    static Thread disegno=new Thread(f);
    static Thread[] n=new Thread[4];
    static Thread p;
    static Boolean gOver = false;
    static BufferedImage map;
    static BufferedImage Scritte;
    static int dots=0;
    static int Eat=0;
    static int dX;
    static int dY;
    static int trueHeight;
    static int trueWidth;
    static int startx,starty;
    static int Life=3;
    static int Level=1;
    static BufferedImage LifeImage;
    static BufferedImage readyImg;
    static Pulse pulse;
    static Thread pul;
    //Immagini di numeri
    static BufferedImage[] s=new BufferedImage[11];
    //Immagini di numeri per lo score
    static BufferedImage[] sf=new BufferedImage[4];
    //Immagini di numeri per i livelli
    static BufferedImage[] le=new BufferedImage[4];
    static int score;
    static Boolean stop=false;
    static int Ngiocatori=1;
    static int range=12;
    static boolean startGame=false;
    static ServerClient server;
    static Thread serverThread;
    static MouseInput ms;
    static ServerAccept serverAccept;
    static Thread SA;
    static Boolean pause=false; //Utilizzata per bloccare tutto il gioco quando qualcuno viene mangiato

    //Options
    static String volume = "true";
    static String difficulty = "medium";

    //Variabile statica per sincronizzare i nemici ed il Pacman


    public static void main(String[] args) {
        new Map();
        setImages();
        setGraphics();
        setListener();
        waitingMenu();
        setAllCharacter();
        startAllCharacter();
        if(Boolean.parseBoolean(volume))
            audioThread.start();


    }

    public static void setAllCharacter(){
        //Imposto il raggio che i fantasmini hanno per vedermi, in base alla difficoltà impostata dal giocatore host
        switch(difficulty){
            case "easy": {range=12;}break;
            case "medium": {range=16;}break;
            case "hard": {range=18;}break;
        }
        switch(server.getID()){
            case 2:{
                pg = new Pg(6, 10, false);
                ne[0]=new Ne(12,14,"pink",true,false);
                ne[1]=new Ne(13,14,"red",false,false);
                ne[2]=new Ne(14,14,"blue",false,false);
                ne[3]=new Ne(15,14,"yellow",false,false);
            }break;
            case 3:{
                pg = new Pg(6, 10, false);
                ne[0]=new Ne(12,14,"pink",false,false);
                ne[1]=new Ne(13,14,"red",true,false);
                ne[2]=new Ne(14,14,"blue",false,false);
                ne[3]=new Ne(15,14,"yellow",false,false);
            }break;
            case 4:{
                pg = new Pg(6, 10, false);
                ne[0]=new Ne(12,14,"pink",false,false);
                ne[1]=new Ne(13,14,"red",false,false);
                ne[2]=new Ne(14,14,"blue",true,false);
                ne[3]=new Ne(15,14,"yellow",false,false);
            }break;
            case 5:{
                pg = new Pg(6, 10, false);
                ne[0]=new Ne(12,14,"pink",false,false);
                ne[1]=new Ne(13,14,"red",false,false);
                ne[2]=new Ne(14,14,"blue",false,false);
                ne[3]=new Ne(15,14,"yellow",true,false);
            }break;
            case 1:{
                System.out.println("Sono nel caso 1");
                pg = new Pg(6, 10, true);
                ne[0]=new Ne(12,14,"pink",false,false);
                ne[1]=new Ne(13,14,"red",false,false);
                ne[2]=new Ne(14,14,"blue",false,false);
                ne[3]=new Ne(15,14,"yellow",false,false);
            }break;
            case 99:{
                pg = new Pg(6, 10, true);
                ne[0]=new Ne(12,14,"pink",false,true);
                ne[1]=new Ne(13,14,"red",false,true);
                ne[2]=new Ne(14,14,"blue",false,true);
                ne[3]=new Ne(15,14,"yellow",false,true);
            }break;
        }

        p=new Thread(pg);
        //Se il fantasmino è controllato, i suoi movimenti verranno gestiti da cdir altrimenti
        //se sono il client in cui il pacman viene controllato allora il fantasmino deve avere un'intelligenza
        //altrimenti il fantasmino è passivo e gestito da dir(COLORE)
        n[0]=new Thread(ne[0]);
        n[1]=new Thread(ne[1]);
        n[2]=new Thread(ne[2]);
        n[3]=new Thread(ne[3]);

        pulse=new Pulse();
        pul=new Thread(pulse);
        pul.start();
    }

    public static void startAllCharacter(){
        p.start();
        for(int i=0;i<Ngiocatori;i++){
            n[i].start();
        }
    }

    public static void setGraphics(){
        //f.f.setSize(644,742);

        f.f.setResizable(false);
        f.f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.f.setUndecorated(true);
        f.f.setVisible(true);

        //Dimensioni in pixel di un quadrato
        //Il disegno inizia da ((f.f.getWidth()-(f.f.getHeight()*0.90))/2)
        //Quindi moltiplicando x2 ottengo lo schermo senza il disegno
        //Sottraendo quel valore al max schermo, ottengo la dimensione del gioco in larghezza
        double dimGame= (f.f.getWidth()-(f.f.getHeight()*0.90));



        dY=(f.f.getHeight()/Map.y);
        dX=dY;
        trueHeight=dY*Map.y;
        trueWidth=dX*Map.x;
        startx=(f.f.getWidth()-trueWidth)/2;
        starty=(f.f.getHeight()-trueHeight)/2;

        f.f.add(f);
        f.f.setBackground(Color.BLACK);
        f.f.getContentPane().setBackground(Color.BLACK);

        disegno.start();
        try{Thread.sleep(300);}catch(Exception e){}//Inserire 3000
        f.f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                stop=true;
            }
        });
    }

    public static void setImages(){
        try{img=ImageIO.read(new File("res/sheet.png"));
            map=ImageIO.read(new File("res/map.png"));
            Scritte=ImageIO.read(new File("res/scritte.png"));
        }
        catch(Exception e){e.printStackTrace();System.out.println(System.getProperty("user.dir"));System.exit(-1);}

        s[0]=Scritte.getSubimage(0,0,11,10);
        s[1]=Scritte.getSubimage(13,0,9,10);
        s[2]=Scritte.getSubimage(24,0,11,10);
        s[3]=Scritte.getSubimage(36,0,11,10);

        s[4]=Scritte.getSubimage(48,0,11,10);
        s[5]=Scritte.getSubimage(60,0,11,10);
        s[6]=Scritte.getSubimage(72,0,11,10);
        s[7]=Scritte.getSubimage(84,0,11,10);
        s[8]=Scritte.getSubimage(96,0,11,10);
        s[9]=Scritte.getSubimage(108,0,11,10);
        s[10]=Scritte.getSubimage(127,0,168,17);

        readyImg=Main.Scritte.getSubimage(0,11,100,18);

        LifeImage=img.getSubimage(20,1,13,13);
    }

    public static boolean checkHost(){
        return server.getRunning();
    }

    public static void setListener(){
        f.f.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                //Scelgo in base all'id a chi modificare la direzione, le altre direzioni verranno modificate dal server
                switch (server.getID()){
                    case 2: {ne[0].controller.changeDir=e.getKeyChar();
                    }break;
                    case 3: {ne[1].controller.changeDir=e.getKeyChar();
                    }break;
                    case 4: {ne[2].controller.changeDir=e.getKeyChar();
                    }break;
                    case 5: {ne[3].controller.changeDir=e.getKeyChar();
                    }break;
                    default: {pg.controller.changeDir=e.getKeyChar();
                    }
                }
            }
        });
        ms = new MouseInput();
        f.f.addMouseListener(ms);
        f.f.addMouseMotionListener(ms);
        f.f.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                Audio.stopAllSounds();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                Audio.stopAllSounds();
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    public static void waitingMenu(){
        //Aspetto finchè statusOfGame non cambia
        while(true){
            if(stateOfGame!=0){
                if(stateOfGame==1){
                    //Inizializzo la classe ServerClient in maniera da far capire a tutte le altre classi
                    //l'id del giocatore(in questo caso 99)
                    try{
                        server = new ServerClient(false);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                if(stateOfGame==2){
                    stateOfGame=4;
                    try{
                        server = new ServerClient(true);
                        //Se sono riuscito a collegarmi
                        System.out.println("Connessione eseguita ad un server remoto");
                        //Ora avvio il Thread
                        serverThread = new Thread(server);
                        serverThread.start();
                        //Aspetto finchè tutti e 5 i giocatori non sono connessi
                        Frame.timer1.start();
                        Frame.timer1.setActionCommand("Waiting");
                        while(server.getNumberOfPlayer()!=Ngiocatori+1){
                            try{
                                Thread.sleep(500);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        Frame.timer1.stop();
                        stateOfGame=2;
                        break;
                    }
                    catch (Exception j) {
                        //Se non sono riuscito a collegarmi, creo il server e mi connetto
                        serverAccept = new ServerAccept();
                        SA = new Thread(serverAccept);
                        SA.start();
                        //Aspetto un paio di secondi così da avere il server in ascolto
                        try{
                            //Ora mi collego al mio stesso server
                            server = new ServerClient(true);
                            //Se sono riuscito a collegarmi, faccio partire il Thread
                            serverThread = new Thread(server);
                            serverThread.start();
                            Frame.timer1.start();
                            Frame.timer1.setActionCommand("Waiting");
                            while(server.getNumberOfPlayer()!=Ngiocatori+1){
                                try{
                                    Thread.sleep(500);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            Frame.timer1.stop();
                            stateOfGame=2;
                            break;
                        }catch (Exception p){
                            System.out.println("Errore connessione server mio");
                            break;
                        }
                    }
                }
                if(stateOfGame==3){
                    //Non so cosa cazzo fare
                }
            }
            try{
                Thread.sleep(50);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}