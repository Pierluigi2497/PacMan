import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.time.Clock;

public class Frame extends JPanel implements Runnable, ActionListener {
    public JFrame f=new JFrame();
    private double timeForDots;
    private double timeForGhosts;
    private double timeForPacman;
    private double timeForWhiteGhosts;
    private double speedGhost=100;
    private double speedPacman=100;
    private double speedWhiteGhosts=300;
    private double speedDots=350;
    private int[] j=new int[4];
    Clock clock=Clock.systemDefaultZone();
    private int i;//Controllo per score
    private int l;//Controllo per invertire score
    private Font font;
    private Font font1;
    int xPos,xDimButton;
    int yPos,yDimButton;
    int[] fontSizeButton = new int[4];
    Color[] colorButton = new Color[4];
    static Timer timer;
    static Timer timer1;
    int dotForWaiting=0;
    BufferedImage[] images = new BufferedImage[6];
    private String waitingString = "Waiting players ";


    public Frame(){
        timer = new Timer(20,this);
        timer1 = new Timer(600,this);
        fontSizeButton[0]=fontSizeButton[1]=fontSizeButton[2]=fontSizeButton[3]=15;
        colorButton[0]=colorButton[1]=colorButton[2]=colorButton[3]= Color.WHITE;
        try{
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            font1 = Font.createFont(Font.TRUETYPE_FONT,new File("res/PacfontGood-yYye.ttf")).deriveFont(50f);
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,new File("res/PacfontGood-yYye.ttf")));
        }catch (Exception e){

        }
        setBackground(Color.BLACK);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        repaint();
    /*  f.setResizable(false);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setUndecorated(true);
        f.setVisible(true);
        this.setSize(644,742);
        f.setSize(644,742);
    */
    }

    public void run(){
        f.setTitle("Pacman");
        f.setIconImage(Main.img.getSubimage(20,1,13,13));
        timeForDots=clock.millis();
        timeForGhosts=clock.millis();
        timeForPacman=clock.millis();
        timeForWhiteGhosts=clock.millis();

        //Inizializzazione menu
        //Inizializzo le posizioni dei pulsanti
        xPos = Main.startx+(Main.dX*9);
        yPos = Main.starty+(Main.dY*10);
        xDimButton = Main.trueWidth/5;
        yDimButton = Main.trueHeight/20;
        //Inizializzo le immagini per il background del menu
        //images[0]= Main.img.getSubimage(0,0,0,0);//Scritta Pacman
        images[1]= Main.img.getSubimage(20,17,13,13);//Pacman
        images[2]= Main.img.getSubimage(36,65,14,14);//Fantasma-0
        images[3]= Main.img.getSubimage(36,81,14,14);//Fantasma-1
        images[4]= Main.img.getSubimage(36,97,14,14);//Fantasma-2
        images[5]= Main.img.getSubimage(36,113,14,14);//Fantasma-3
        for(;;){
            repaint();
            try{
                //33=30 FPS
                //16=60 FPS
                Thread.sleep(16);
            } catch(Exception e) {}
            if(Main.stop)
                break;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        font = new Font("Dialog",Font.BOLD,15);
        g.setColor(Color.WHITE);
        //drawMatrix(g);
        if(Main.stateOfGame==2||Main.stateOfGame==1) {
            drawClip(g);
            drawMap(g);
            //Per disegnare al centro(di ogno quadrato corrispettivo), devo aggiundere la metà di dX e dY ad ogni print
            drawDots(g);
            drawGhosts(g);
            drawPacman(g);
            drawBlack(g);
            drawScore(g);
            drawGameOver(g);
            drawLife(g);
            drawLevel(g);
            drawReady(g);
        }else{
            if(Main.stateOfGame==0)
                drawMenu(g);
            else{
               if(Main.stateOfGame==4){
                   drawLobby(g);
               }
            }

        }
    }

    public void drawMap(Graphics g){
        //Stampa la mappa a partire dalla posizione 0,0 con una grandezza calcolata da (grandezza quadratino matrice * grandezza matrice)
        //Ho tolto dei pixel perchè, per qualche strano motivo, la mappa veniva tagliata
        //g.drawImage(Main.map,0,0,(Main.dX*Map.x),(Main.dY*Map.y),null);
        g.drawImage(Main.map,Main.startx,Main.starty,Main.trueWidth,Main.trueHeight,null);
    }

    public void drawDots(Graphics g){
        for(int i=0;i<Map.y;i++)
            for(int l=0;l<Map.x;l++)
                if(Map.maze[i][l]=='2'||Map.maze[i][l]=='4') {

                    g.drawOval(Main.startx+(int)(Main.dX*l)+(Main.dX/2)-2,Main.starty+(int)(Main.dY*i)+(Main.dY/2)-2,4,4);
                    g.fillOval(Main.startx+(int)(Main.dX*l)+(Main.dX/2)-2,Main.starty+(int)(Main.dY*i)+(Main.dY/2)-2,4,4);
                    // g.drawImage(dot,Main.startx+(Main.dX*l),Main.dY*i,Main.dX,Main.dY,null);
                }
                else if(Map.maze[i][l]=='5'){
                    if((clock.millis()-timeForDots)<speedDots) {
                        //Finchè non passa un secondo, stampo
                        g.drawOval(Main.startx + (int) (Main.dX * l) + (Main.dX / 2) - 5, Main.starty + (int) Main.dY * i + (Main.dY / 2) - 5, 10, 10);
                        g.fillOval(Main.startx + (int) (Main.dX * l) + (Main.dX / 2) - 5, Main.starty + (int) Main.dY * i + (Main.dY / 2) - 5, 10, 10);
                    }else{
                        if(clock.millis()-timeForDots<(speedDots*2)){
                            //Quando è passato il secondo, non stampo
                        }else{
                            //Quando sono passati 2 secondi e quindi un ciclo di (vedo non vedo), resetto
                            timeForDots= clock.millis();
                        }
                    }
                }
    }

    public void drawGhosts(Graphics g){
        for(int i=0;i<4;i++) {
            changeNeSprite(i);
            g.drawImage(Main.ne[i].n, Main.startx + (Main.dX * Main.ne[i].controller.pathx) + Main.ne[i].controller.getTX(), Main.starty + (Main.dY * Main.ne[i].controller.pathy) + Main.ne[i].controller.getTY(), Main.dX, Main.dY, null);
        }
    }

    public void drawPacman(Graphics g){
        changePgSprite();
        g.drawImage(Main.pg.Pac,Main.startx+(Main.dX * Main.pg.controller.pathx)+Main.pg.controller.tX,Main.starty+(Main.dY * Main.pg.controller.pathy)+Main.pg.controller.tY,Main.dX,Main.dY,null);
    }

    public void drawScore(Graphics g){
        for(i=3,l=0;i>=0;i--,l++){
            g.drawImage(Main.sf[i],Main.startx+(l*11)+(int)(Main.dX*2)+(Main.dX/2)-2,(int)(Main.trueHeight*0.38),11,10,null);
        }
    }

    public void drawGameOver(Graphics g){
        if(Main.gOver)
            g.drawImage(Main.s[10],(this.getWidth() / 2)-85,(this.getHeight() / 2)-38,169,17,null);
    }

    public void changeNeSprite(int i) {
        //Se il gioco non è finito, continuo con le animazione normali

        if (!Main.gOver) {
            if (Main.ne[i].eated) {
                eatedSprite(Main.ne[i].controller.changeDir, i);
                return;
            }
            if (Pulse.situation == 0 && !Main.ne[i].controller.stop) {
                switch (Main.ne[i].controller.Direction) {
                    case 'w': {
                        //Ogni 100 ms cambio le gambe del fantasma
                        if (clock.millis() - timeForGhosts < speedGhost) {
                            Main.ne[i].n = Main.ne[i].i[4];
                        } else if (clock.millis() - timeForGhosts < (speedGhost * 2)) {
                            Main.ne[i].n = Main.ne[i].i[5];
                        } else {
                            timeForGhosts = clock.millis();
                            Main.ne[i].n = Main.ne[i].i[4];
                        }
                    }
                    break;
                    case 'a': {
                        //Main.ne[i].n=Main.ne[i].i[2];
                        //Ogni 100 ms cambio le gambe del fantasma
                        if (clock.millis() - timeForGhosts < speedGhost) {
                            Main.ne[i].n = Main.ne[i].i[2];
                        } else if (clock.millis() - timeForGhosts < (speedGhost * 2)) {
                            Main.ne[i].n = Main.ne[i].i[3];
                        } else {
                            timeForGhosts = clock.millis();
                            Main.ne[i].n = Main.ne[i].i[2];
                        }
                    }
                    break;
                    case 'd': {
                        //Ogni 100 ms cambio le gambe del fantasma
                        if (clock.millis() - timeForGhosts < speedGhost) {
                            Main.ne[i].n = Main.ne[i].i[0];
                        } else if (clock.millis() - timeForGhosts < (speedGhost * 2)) {
                            Main.ne[i].n = Main.ne[i].i[1];
                        } else {
                            timeForGhosts = clock.millis();
                            Main.ne[i].n = Main.ne[i].i[0];
                        }
                    }
                    break;
                    case 's': {
                        //Ogni 100 ms cambio le gambe del fantasma
                        if (clock.millis() - timeForGhosts < speedGhost) {
                            Main.ne[i].n = Main.ne[i].i[6];
                        } else if (clock.millis() - timeForGhosts < (speedGhost * 2)) {
                            Main.ne[i].n = Main.ne[i].i[7];
                        } else {
                            timeForGhosts = clock.millis();
                            Main.ne[i].n = Main.ne[i].i[6];
                        }
                    }
                    break;

                }
            } else {
                if (Pulse.situation == 1) {
                    blueSprite(i);
                }
                else if (Pulse.situation == 2) {
                    whiteSprite(i);
                }
            }
        }else{

            //Main.ne[i].n=Main.ne[i].i[0];
        }
    }

    public void blueSprite(int i){
        if(!Main.ne[i].controller.stop) {
            if (clock.millis() - timeForGhosts < speedGhost) {
                Main.ne[i].n = Main.ne[i].i[8];
            } else if (clock.millis() - timeForGhosts < (speedGhost * 2)) {
                Main.ne[i].n = Main.ne[i].i[9];
            } else {
                timeForGhosts = clock.millis();
                Main.ne[i].n = Main.ne[i].i[8];
            }
        }else{
            Main.ne[i].n = Main.ne[i].i[9];
        }
    }

    public void eatedSprite(char dir,int i){
        switch (dir){
            case 'w':{Main.ne[i].n=Main.ne[i].dead[2];}break;
            case 'a':{Main.ne[i].n=Main.ne[i].dead[1];}break;
            case 's':{Main.ne[i].n=Main.ne[i].dead[3];}break;
            case 'd':{Main.ne[i].n=Main.ne[i].dead[0];}break;
        }
    }

    public void whiteSprite(int i){
        //Ogni 500 ms cambio colore, da blu a bianco
        //
        //Prima divento bianco, supponendo di essere già blu, e poi ritorno blu
        //Ogni x ms cambio colore, da bianco a blu

        //Se non ho la testa contro il muro, cambio colore e cammino
        if(!Main.ne[i].controller.stop) {
            //E' la prima volta che entro?
            if (timeForWhiteGhosts == 0) {
                timeForWhiteGhosts = clock.millis();
            }
            //Se devo cambiare colore
            if (clock.millis() - timeForWhiteGhosts < speedWhiteGhosts) {
                //Animazione piedi
                if (clock.millis() - timeForGhosts < speedGhost) {
                    Main.ne[i].n = Main.ne[i].i[10];
                } else if (clock.millis() - timeForGhosts < (speedGhost * 2)) {
                    Main.ne[i].n = Main.ne[i].i[11];
                } else {
                    timeForGhosts = clock.millis();
                    Main.ne[i].n = Main.ne[i].i[10];
                }
            } else {
                //Divento blu e faccio l'animazione
                if (clock.millis() - timeForWhiteGhosts < (speedWhiteGhosts * 2)) {
                    blueSprite(i);
                } else {
                    //Se non devo essere più blu, resetto il timer
                    timeForWhiteGhosts = clock.millis();
                }
            }
        }else{
            //Altrimenti cambio solo il colore
            //E' la prima volta che entro?
            if (timeForWhiteGhosts == 0) {
                timeForWhiteGhosts = clock.millis();
            }
            //Se devo cambiare colore
            if (clock.millis() - timeForWhiteGhosts < speedWhiteGhosts) {
                Main.ne[i].n = Main.ne[i].i[11];
            }
            else {
                //Divento blu e faccio l'animazione
                if (clock.millis() - timeForWhiteGhosts < (speedWhiteGhosts * 2)) {
                    Main.ne[i].n = Main.ne[i].i[9];
                } else {
                    //Se non devo essere più blu, resetto il timer
                    timeForWhiteGhosts = clock.millis();
                }
            }
        }

    }

    public void changePgSprite() {
        if (Main.gOver) {
            //Se GameOver, l'animazione della morte la gestisce il Pacman
            //Sarebbe meglio che la gestisse la classe Frame
        } else {
            //Se il Pacman non ha incontrato un muro, faccio l'animazione altrimenti lascio l'ultima sprite
            if(!Main.pg.controller.stop) {
                switch (Main.pg.controller.Direction) {
                    case 'w': {
                        if (clock.millis() - timeForPacman < speedPacman) {
                            Main.pg.Pac = Main.pg.pac[5];

                        } else if (clock.millis() - timeForPacman < (speedPacman * 2)) {
                            Main.pg.Pac = Main.pg.pac[6];

                        } else {
                            timeForPacman = clock.millis();
                            Main.pg.Pac = Main.pg.pac[5];
                        }
                    }
                    break;
                    case 'd': {
                        if (clock.millis() - timeForPacman < speedPacman) {
                            Main.pg.Pac = Main.pg.pac[1];
                        } else if (clock.millis() - timeForPacman < (speedPacman * 2)) {
                            Main.pg.Pac = Main.pg.pac[2];
                        } else {
                            timeForPacman = clock.millis();
                            Main.pg.Pac = Main.pg.pac[1];
                        }
                    }
                    break;
                    case 's': {
                        if (clock.millis() - timeForPacman < speedPacman) {
                            Main.pg.Pac = Main.pg.pac[7];
                        } else if (clock.millis() - timeForPacman < (speedPacman * 2)) {
                            Main.pg.Pac = Main.pg.pac[8];
                        } else {
                            timeForPacman = clock.millis();
                            Main.pg.Pac = Main.pg.pac[7];
                        }
                    }
                    break;
                    case 'a': {
                        if (clock.millis() - timeForPacman < speedPacman) {
                            Main.pg.Pac = Main.pg.pac[3];
                        } else if (clock.millis() - timeForPacman < (speedPacman * 2)) {
                            Main.pg.Pac = Main.pg.pac[4];
                        } else {
                            timeForPacman = clock.millis();
                            Main.pg.Pac = Main.pg.pac[3];
                        }
                    }
                    break;
                    default: {
                        Main.pg.Pac = Main.pg.pac[0];
                    }
                }
            }
        }
    }

    public void drawLife(Graphics g){
        for(int i=0;i<3;i++){
            if(i<Main.Life){
                g.drawImage(Main.LifeImage,(int)((Main.startx+(Main.trueWidth*0.02))+(i*Main.dX)),(int)(Main.trueHeight*0.565),Main.dX,Main.dY,null);
            }
        }
    }

    public void drawLevel(Graphics g){
        j[0]=Main.Level%10;
        j[1]=Main.Level%100;
        j[1]=j[1]/10;
        j[2]=Main.Level%1000;
        j[2]=j[2]/100;
        j[3]=Main.Level/1000;

        for(int l=0;l<4;l++)
            switch(j[l]){
                case 0:{Main.le[l]=Main.s[0];}break;
                case 1:{Main.le[l]=Main.s[1];}break;
                case 2:{Main.le[l]=Main.s[2];}break;
                case 3:{Main.le[l]=Main.s[3];}break;
                case 4:{Main.le[l]=Main.s[4];}break;
                case 5:{Main.le[l]=Main.s[5];}break;
                case 6:{Main.le[l]=Main.s[6];}break;
                case 7:{Main.le[l]=Main.s[7];}break;
                case 8:{Main.le[l]=Main.s[8];}break;
                case 9:{Main.le[l]=Main.s[9];}break;
            }
        int j=0;
        for(int l=3;l>=0;l--,j++){
            g.drawImage(Main.le[l],(int)(Main.startx+(j*11)+(Main.trueWidth*0.91)),(int)(Main.trueHeight*0.38),11,10,null);
        }

    }

    public void drawBlack(Graphics g){
        //Stampo delle bande nere vicino al tunnel, per non far vedere i fantasmini o il pacman quando lo attraversano
        g.setColor(Color.BLACK);
        g.drawRect((Main.startx-Main.dX),(int)(Main.starty+(Main.trueHeight*0.435)),Main.dX,Main.dY*2);
        g.fillRect((Main.startx-Main.dX),(int)(Main.starty+(Main.trueHeight*0.435)),Main.dX,Main.dY*2);
        g.drawRect((Main.startx+Main.trueWidth),(int)(Main.starty+(Main.trueHeight*0.435)),Main.dX,Main.dY*2);
        g.fillRect((Main.startx+Main.trueWidth),(int)(Main.starty+(Main.trueHeight*0.435)),Main.dX,Main.dY*2);

    }

    public void drawReady(Graphics g){
        if(!Main.startGame){
            g.drawImage(Main.readyImg,(int)(Main.startx+(Main.trueWidth*0.43)),(int)(Main.trueHeight*0.57),100,18,null);
        }
    }

    public void drawClip(Graphics g){
        for(int i=0;i<Main.Ngiocatori;i++) {
            if (Main.server.getID() == (i + 2)) {
                //Imposto un ellisse di visione partendo dalla posizione del fantasmino - la grandezza in pixel del raggio di visione ed aggiungo metà porzione di quadrato di array grafico
                if(!Main.gOver) {
                    g.setClip(new Ellipse2D.Double((Main.startx + (Main.dX * Main.ne[i].controller.pathx) + Main.ne[i].controller.tX) - ((Main.range * Main.dX) / 2) + (Main.dX / 2), (Main.starty + (Main.dY * Main.ne[i].controller.pathy) + Main.ne[i].controller.tY) - ((Main.range * Main.dX) / 2) + (Main.dY / 2), (Main.range * Main.dX), (Main.range * Main.dX)));
                }
                //g.fillOval((Main.startx + (Main.dX * Main.ne[i].controller.pathx) + Main.ne[i].tX)-150, (Main.starty + (Main.dY * Main.ne[i].controller.pathy) + Main.ne[i].tY)-150, 300, 300);
            }
        }
    }

    public void drawMenu(Graphics g){
        g.setFont(font1);
        g.setColor(Color.YELLOW);
        g.drawString("pac man",Main.startx+(Main.dX*7),Main.starty+(Main.dY*3));
        g.setColor(Color.WHITE);

        g.drawOval(Main.startx+(Main.dX/2)-5+(Main.dX*8), Main.starty+(Main.dY/2)-5+(Main.dY*6), 10, 10);
        g.fillOval(Main.startx+(Main.dX/2)-5+(Main.dX*8), Main.starty+(Main.dY/2)-5+(Main.dY*6), 10, 10);
        g.drawImage(images[1],Main.startx+(Main.dX*9)+(Main.dX/3),Main.starty+(Main.dY*6),Main.dX,Main.dY,null);
        g.drawImage(images[2],Main.startx+(Main.dX*10)+(Main.dX/3)*2,Main.starty+(Main.dY*6),Main.dX,Main.dY,null);
        g.drawImage(images[3],Main.startx+(Main.dX*11)+(Main.dX/3)*3,Main.starty+(Main.dY*6),Main.dX,Main.dY,null);
        g.drawImage(images[4],Main.startx+(Main.dX*12)+(Main.dX/3)*4,Main.starty+(Main.dY*6),Main.dX,Main.dY,null);
        g.drawImage(images[5],Main.startx+(Main.dX*13)+(Main.dX/3)*5,Main.starty+(Main.dY*6),Main.dX,Main.dY,null);

        font = font.deriveFont((float)fontSizeButton[0]);
        g.setColor(colorButton[0]);
        g.setFont(font);
        g.drawRect(xPos,yPos,xDimButton,yDimButton);
        g.drawString("Single-Player",xPos+(xDimButton/5),yPos+font.getSize()+yDimButton/5);

        font = font.deriveFont((float)fontSizeButton[1]);
        g.setColor(colorButton[1]);
        g.setFont(font);
        g.drawRect(xPos,yPos+(yDimButton*1)+(yDimButton/3)*1,xDimButton,yDimButton);
        g.drawString("Multi-Player",xPos+(xDimButton/5),yPos+font.getSize()+yDimButton/5+(yDimButton*1)+(yDimButton/3)*1);

        font = font.deriveFont((float)fontSizeButton[2]);
        g.setColor(colorButton[2]);
        g.setFont(font);
        g.drawRect(xPos,yPos+(yDimButton*2)+(yDimButton/3)*2,xDimButton,yDimButton);
        g.drawString("Options",xPos+(xDimButton/5),yPos+font.getSize()+yDimButton/5+(yDimButton*2)+(yDimButton/3)*2);

        font = font.deriveFont((float)fontSizeButton[3]);
        g.setColor(colorButton[3]);
        g.setFont(font);
        g.drawRect(xPos,yPos+(yDimButton*3)+(yDimButton/3)*3,xDimButton,yDimButton);
        g.drawString("Exit",xPos+(xDimButton/5),yPos+font.getSize()+yDimButton/5+(yDimButton*3)+(yDimButton/3)*3);
    }

    public void drawMatrix(Graphics g){
        for(int i=0;i<Map.y;i++){
            for(int l=0;l<Map.x;l++){
                g.drawRect((Main.startx)+l*Main.dY,(Main.starty)+i*Main.dX,Main.dX,Main.dY);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       switch(e.getActionCommand()){
           case "Single":{
               if(fontSizeButton[0]<20){
                   fontSizeButton[0]++;
               }else{
                   timer.stop();
               }
               colorButton[0]=Color.YELLOW;

               if(fontSizeButton[1]>15){
                   fontSizeButton[1]--;
               }
               colorButton[1]=Color.WHITE;

               if(fontSizeButton[2]>15){
                   fontSizeButton[2]--;
               }
               colorButton[2]=Color.WHITE;

               if(fontSizeButton[3]>15){
                   fontSizeButton[3]--;
               }
               colorButton[3]=Color.WHITE;
           }break;
           case "Multi":{
               if(fontSizeButton[0]>15){
                   fontSizeButton[0]--;
               }
               colorButton[0]=Color.WHITE;

               if(fontSizeButton[1]<20){
                   fontSizeButton[1]++;
               }else{
                   timer.stop();
               }
               colorButton[1]=Color.YELLOW;

               if(fontSizeButton[2]>15){
                   fontSizeButton[2]--;
               }
               colorButton[2]=Color.WHITE;

               if(fontSizeButton[3]>15){
                   fontSizeButton[3]--;
               }
               colorButton[3]=Color.WHITE;
           }break;
           case "Options":{
               if(fontSizeButton[0]>15){
                   fontSizeButton[0]--;
               }
               colorButton[0]=Color.WHITE;

               if(fontSizeButton[1]>15){
                   fontSizeButton[1]--;
               }
               colorButton[1]=Color.WHITE;

               if(fontSizeButton[2]<20){
                   fontSizeButton[2]++;
               }else{
                   timer.stop();
               }
               colorButton[2]=Color.YELLOW;

               if(fontSizeButton[3]>15){
                   fontSizeButton[3]--;
               }
               colorButton[3]=Color.WHITE;
           }break;
           case "Exit":{
               if(fontSizeButton[0]>15){
                   fontSizeButton[0]--;
               }
               colorButton[0]=Color.WHITE;

               if(fontSizeButton[1]>15){
                   fontSizeButton[1]--;
               }
               colorButton[1]=Color.WHITE;

               if(fontSizeButton[2]>15){
                   fontSizeButton[2]--;
               }
               colorButton[2]=Color.WHITE;

               if(fontSizeButton[3]<20){
                   fontSizeButton[3]++;
               }else{
                   timer.stop();
               }
               colorButton[3]=Color.YELLOW;
           }break;
           case "Waiting":{
               if(dotForWaiting<5){
                   dotForWaiting++;

               }else{
                   dotForWaiting=0;
               }
           }break;
           default: {
               //Se non tocco nessuno, ritrasformo il colore e la dimensione dei pulsanti in quella originale
               if(fontSizeButton[0]>15){
                   fontSizeButton[0]--;
               }
               colorButton[0]=Color.WHITE;


               if(fontSizeButton[1]>15){
                   fontSizeButton[1]--;
               }
               colorButton[1]=Color.WHITE;

               if(fontSizeButton[2]>15){
                   fontSizeButton[2]--;
               }
               colorButton[2]=Color.WHITE;

               if(fontSizeButton[3]>15){
                   fontSizeButton[3]--;
               }
               colorButton[3]=Color.WHITE;
               timer.stop();
           }
       }
    }

    public void drawLobby(Graphics g){
        font = font.deriveFont((float)20);
        g.setColor(Color.WHITE);
        g.setFont(font);

        switch(dotForWaiting) {
            case 0:{g.drawString(waitingString, Main.startx + (Main.dX / 2) - 5 + (Main.dX * 8), yPos + font.getSize() + yDimButton / 5);}break;
            case 1:{g.drawString(waitingString + " .", Main.startx + (Main.dX / 2) - 5 + (Main.dX * 8), yPos + font.getSize() + yDimButton / 5);}break;
            case 2:{g.drawString(waitingString + " . .", Main.startx + (Main.dX / 2) - 5 + (Main.dX * 8), yPos + font.getSize() + yDimButton / 5);}break;
            case 3:{g.drawString(waitingString + " . . .", Main.startx + (Main.dX / 2) - 5 + (Main.dX * 8), yPos + font.getSize() + yDimButton / 5);}break;
            case 4:{g.drawString(waitingString + " . . . .", Main.startx + (Main.dX / 2) - 5 + (Main.dX * 8), yPos + font.getSize() + yDimButton / 5);}break;
            case 5:{g.drawString(waitingString + " . . . . .", Main.startx + (Main.dX / 2) - 5 + (Main.dX * 8), yPos + font.getSize() + yDimButton / 5);}break;

        }
        g.drawOval(Main.startx+(Main.dX/2)-5+(Main.dX*8), Main.starty+(Main.dY/2)-5+(Main.dY*6), 10, 10);
        g.fillOval(Main.startx+(Main.dX/2)-5+(Main.dX*8), Main.starty+(Main.dY/2)-5+(Main.dY*6), 10, 10);
        try {
            for(int i=0;i<Main.server.numberOfPlayer;i++) {
                g.drawImage(images[i + 1], Main.startx + (Main.dX * (9 + i)) + (Main.dX / 3) * (i + 1), Main.starty + (Main.dY * 6), Main.dX, Main.dY, null);
            }
        }catch (Exception e){}
    }
}