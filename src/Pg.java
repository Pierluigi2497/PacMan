import java.awt.image.BufferedImage;
import java.io.IOException;

public class Pg implements Runnable {
    //public int controller.pathx,controller.pathy;
    public char nodex,nodey;
    /*public int tX=0;  //Translate x
    public int tY=0;  //Translate y*/
    public int vel=200;//Inversa in ms*/
    public char ldir;
    public BufferedImage[] death=new BufferedImage[13];//1 immagine trasparente
    public BufferedImage[] pac=new BufferedImage[10];
    public BufferedImage Pac;
    private boolean controlled;
    private Boolean c=true;
    /*public char controller.Direction=' ';*/
    //Serve per sincronizzare i nemici ed il Pg
    static Boolean Restart=false;
    public Controller controller;
    //Serve per dire alla classe frame quando il Pacman deve fermare l'animazione e quando no
    //All'inizio l'animazione è ferma, poi si muove finche non trova un muro
    /*public boolean stop=true;*/

    public Pg(int x,int y,boolean controlled){
        controller=new Controller(x,y,vel);
        this.controlled=controlled;
        FirstLunch(x,y);
    }

    public void FirstLunch(int x,int y){
        //Resetto la posizione
        controller.pathx=x;
        controller.pathy=y;
        //Resetto ultimo nodo letto
        nodex=6;
        nodey=9;
        //Resetto variabili d'animazione
        controller.tX=0;
        controller.tY=0;


        pac[0]=Main.img.getSubimage(36,1,13,13);
        pac[1]=Main.img.getSubimage(20,1,13,13);
        pac[2]=Main.img.getSubimage(4,1,13,13);
        pac[3]=Main.img.getSubimage(20,17,13,13);
        pac[4]=Main.img.getSubimage(4,17,13,13);
        pac[5]=Main.img.getSubimage(20,33,13,13);
        pac[6]=Main.img.getSubimage(4,33,13,13);
        pac[7]=Main.img.getSubimage(20,49,13,13);
        pac[8]=Main.img.getSubimage(4,49,13,13);
        //Pacman invisibile
        pac[9]=Main.img.getSubimage(0,0,1,1);

        death[0]=Main.img.getSubimage(36,1,13,13);
        death[1]=Main.img.getSubimage(52,1,13,13);
        death[2]=Main.img.getSubimage(67,1,15,13);
        death[3]=Main.img.getSubimage(83,1,15,13);
        death[4]=Main.img.getSubimage(99,1,15,13);
        death[5]=Main.img.getSubimage(115,1,15,13);
        death[6]=Main.img.getSubimage(131,1,15,13);
        death[7]=Main.img.getSubimage(148,1,13,13);
        death[8]=Main.img.getSubimage(163,1,13,13);
        death[9]=Main.img.getSubimage(178,1,13,13);
        death[10]=Main.img.getSubimage(189,1,13,13);
        death[11]=Main.img.getSubimage(203,1,13,13);
        death[12]=Main.img.getSubimage(0,0,1,1);
    }

    public void run(){
        //Aspetto che il giocatore esca dal menu
        while(Main.stateOfGame==0||Main.stateOfGame==3){
            try{
                Thread.sleep(10);
            }catch (Exception e){}
        }
        if(controlled) {
            for (; ; ) {
                //Imposto direzione=k (killed)
                controller.Direction = 'k';
                //In questo modo la classe frame sà che non deve far animare il pacman
                //Verifico che tutti i fantasmi siano pronti
                for (int i = 0; i < Main.Ngiocatori; ) {
                    if (Main.ne[i].ready) {
                        i++;
                    }
                    try {
                        Thread.sleep(20);
                    } catch (Exception e) {
                    }
                }
                //Tutti i fantasmi sono pronti, li libero dal loop
                for (int i = 0; i < Main.Ngiocatori; i++) {
                    Main.ne[i].start = true;
                    Main.ne[i].ready = false;

                }
                //Quando esco dal ciclo, faccio partire tutti i nemici
                Restart = true;
                try {
                    //Aspetto 30 ms (Tempo massimo di sincronizzazione impostato su Ne)
                    Thread.sleep(30);
                } catch (Exception e) {
                }
                Restart = false;
                Main.gOver = false;
                Pac = pac[0];
                //Aspetto che l'audio beginning finisca prima di iniziare il gioco
                if (!Audio.beginningClip.isActive()) {
                    if (controller.changeDir == ' ') {
                        Main.startGame = true;
                    }
                    if (Main.startGame) {
                        //Invio al server le informazioni sui fantasmini
                        for(int i=0;i<Main.Ngiocatori;i++) {
                            try {
                                Main.server.sendDir("7-"+(i + 2) + ":" + Main.ne[i].controller.changeDir + "," + Main.ne[i].controller.pathx + "," + Main.ne[i].controller.pathy + "," + Main.ne[i].controller.tX + "," + Main.ne[i].controller.tY + "," + true + "," + Main.ne[i].controller.vel + "," + true + "," + false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Audio.stopDots = false;
                        try {
                            Main.server.sendDir("6:" +
                                    Main.gOver + "," +
                                    Restart + "," +
                                    Main.startGame + "," +
                                    Main.Life + "," +
                                    Pulse.win + "," +
                                    Main.Level
                            );
                        }catch (Exception e){}
                        Life();
                    } else {
                        //Tutti i fantasmi sono pronti, li libero dal loop
                        for (int i = 0; i < Main.Ngiocatori; i++) {
                            Main.ne[i].start = true;
                            Main.ne[i].ready = true;
                        }
                    }
                } else {
                    //Tutti i fantasmi sono pronti, li libero dal loop
                    for (int i = 0; i < Main.Ngiocatori; i++) {
                        Main.ne[i].start = true;
                        Main.ne[i].ready = true;
                    }
                }
                //Quando il pg muore o la partita si resetta, chiamo una funzione per resettare i valori e l'altra per dare
                //vita al PG
            }
        }
        else {
            for (; ; ) {
                //FirstLunch(6,9);
                Restart = false;
                Main.gOver = false;
                Pac = pac[0];
                System.out.println("Primo for");
                for (; ; ) {

                    if (Main.gOver) {
                        for (int i = 0; i < 13; i++) {
                            Pac = death[i];
                            try {
                                Thread.sleep(75);
                            } catch (Exception e) {
                            }
                            System.out.println("Ciclo");
                        }
                        System.out.println("Fine Ciclo");
                        Pac = pac[9];
                        if (Main.Life == 0) {
                            Pulse.resetAll();
                        } else {
                            //Se il gameover è causato da una vittoria, non diminuisco le vite
                            if (!Pulse.win) {
                                Main.Life--;
                            }
                            Pulse.win = false;
                        }
                        Main.gOver=false;
                        break;
                    }
                    if (Main.stop) {
                        return;
                    }
                    switch (controller.changeDir) {
                        case 'a': {
                            controller.Direction = 'a';
                            controller.MoveSx(false);
                            if (controller.Direction != 'a')
                                controller.changeDir = controller.Direction;
                        }
                        break;
                        case 'd': {
                            controller.Direction = 'd';
                            controller.MoveDx(false);
                            if (controller.Direction != 'd')
                                controller.changeDir = controller.Direction;
                        }
                        break;
                        case 's': {
                            controller.Direction = 's';
                            controller.MoveDw(false);

                            if (controller.Direction != 's')
                                controller.changeDir = controller.Direction;
                        }
                        break;
                        case 'w': {
                            controller.Direction = 'w';
                            controller.MoveUp(false);
                            if (controller.Direction != 'w')
                                controller.changeDir = controller.Direction;
                        }
                        break;
                    }
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {

                    }
                }
                FirstLunch(6,9);
                try{Thread.sleep(2000);}catch (Exception e){}
            }
        }
    }

    public void Life(){
        while(true){
            if(Audio.eatedClip.isActive()){
                //Se il suono da mangiato
            }
            if(Main.gOver){
                for(int i=0;i<13;i++){
                    Pac=death[i];
                    try{Thread.sleep(75);}catch(Exception e){}
                }
                Pac=pac[9];
                if(Main.Life==0){
                    Pulse.resetAll();
                }else {
                    //Se il gameover è causato da una vittoria, non diminuisco le vite
                    if(!Pulse.win) {
                        Main.Life--;
                    }
                    Pulse.win=false;
                }
                Main.gOver=false;
                break;
            }
            if (Main.stop){
                return;
            }
            //Se sono ad un incrocio, aggiorno nodex e nodey
            if(Map.maze[controller.pathy][controller.pathx]=='2'||Map.maze[controller.pathy][controller.pathx]=='3'||Map.maze[controller.pathy][controller.pathx]=='5'){
                nodex=(char)controller.pathx;
                nodey=(char)controller.pathy;
            }

            switch(controller.changeDir){
                case 'a':{
                    controller.MoveSx(true);
                    ldir='a';
                    if(controller.Direction!='a')
                        controller.changeDir=controller.Direction;}
                break;
                case 'd':{
                    controller.MoveDx(true);
                    ldir='d';
                    if(controller.Direction!='d')
                        controller.changeDir=controller.Direction;}break;
                case 's':{
                    controller.MoveDw(true);
                    ldir='s';
                    if(controller.Direction!='s')
                        controller.changeDir=controller.Direction;}break;
                case 'w':{
                    controller.MoveUp(true);
                    ldir='w';
                    if(controller.Direction!='w')
                        controller.changeDir=controller.Direction;}break;
                default:{
                    //Se ho premuto un tasto diverso dai consentiti, ripristino la variabile changeDir su l'ultima direzione consentita
                    controller.changeDir=ldir;
                }
            }
            try {
                Thread.sleep(10);
            }catch (Exception e){


            }
        }
        //METTERE UN CICLO CHE ASPETTA DI RIGENERARE IL THREAD

        //Quando finisci il ciclo vitale, richiama life chiudendo l'attuale
        //TEMPORANEAMENTE ASPETTO 2 SECONDI PERCHE SI REAVII LA PARTITA
        //DOVRO METTERE UN MENU!!
        FirstLunch(6,9);
        try{Thread.sleep(2000);}catch (Exception e){}
        return ;
    }

    public void stopThread(double g){
        try{
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}