//I FANTASMI NON SONO BEN SINCRONIZZATI CON IL REPAINT, A CAUSA DI CIO L'ANIMAZIONE NON E BELLISSIMA
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import java.awt.image.BufferedImage;
import java.time.Clock;
public class Ne  implements Runnable {
    public int bornx,borny; //Queste variabili serviranno per rigenerare il thread
    public int vel=250;
    public BufferedImage[] i=new BufferedImage[13];
    public BufferedImage[] dead=new BufferedImage[4];
    public BufferedImage n;
    private Boolean c=true;
    private Boolean d=true;
    public char ldir=' ';
    private Clock clock=Clock.systemDefaultZone();
    //Serve per quando il fantasma deve flashare di bianco
    private long checkms=0;
    private boolean nuovo,uscito;
    private String color;
    //Serve per capire quando il fantasma è pronto per rigenerarsi
    //Serve alla class Pg
    public boolean ready=true;
    //variabile gestita da Pg per rigenerare Ne
    public boolean start=false;
    public boolean eated=false;
    private DijkstraShortestPath<String, DefaultEdge> dijkstra;
    private boolean controlled;
    private boolean activated;
    public Controller controller;


    public Ne(int x,int y,String a,boolean controlled,boolean activated){
        bornx=x;
        borny=y;
        color=a;
        start=false;
        this.controlled=controlled;
        this.activated=activated;
        controller=new Controller(bornx,borny,vel);
        FirstLaunch(bornx,borny,color);
    }

    public void FirstLaunch(int x,int y,String a){
        controller.tX=0;
        controller.tY=0;
        controller.pathx=x;
        controller.pathy=y;
        controller.stop=true;
        color=a;
        eated=false;
        controller.vel=250;
        nuovo=true;
        uscito=false;
        //0-1 Destra  2-3 Sinistra  4-5 Sopra  6-7 Sotto  8-9 Blu  10-11 Bianco-Blu  12 Trasparente
        if(a=="red"){
            i[0]=Main.img.getSubimage(4,65,14,14);
            i[1]=Main.img.getSubimage(20,65,14,14);
            i[2]=Main.img.getSubimage(36,65,14,14);
            i[3]=Main.img.getSubimage(52,65,14,14);
            i[4]=Main.img.getSubimage(68,65,14,14);
            i[5]=Main.img.getSubimage(84,65,14,14);
            i[6]=Main.img.getSubimage(100,65,14,14);
            i[7]=Main.img.getSubimage(116,65,14,14);
        }
        else if(a=="pink"){
            i[0]=Main.img.getSubimage(4,81,14,14);
            i[1]=Main.img.getSubimage(20,81,14,14);
            i[2]=Main.img.getSubimage(36,81,14,14);
            i[3]=Main.img.getSubimage(52,81,14,14);
            i[4]=Main.img.getSubimage(68,81,14,14);
            i[5]=Main.img.getSubimage(84,81,14,14);
            i[6]=Main.img.getSubimage(100,81,14,14);
            i[7]=Main.img.getSubimage(116,81,14,14);
        }
        else if(a=="blue"){
            i[0]=Main.img.getSubimage(4,97,14,14);
            i[1]=Main.img.getSubimage(20,97,14,14);
            i[2]=Main.img.getSubimage(36,97,14,14);
            i[3]=Main.img.getSubimage(52,97,14,14);
            i[4]=Main.img.getSubimage(68,97,14,14);
            i[5]=Main.img.getSubimage(84,97,14,14);
            i[6]=Main.img.getSubimage(100,97,14,14);
            i[7]=Main.img.getSubimage(116,97,14,14);
        }
        else if(a=="yellow"){
            i[0]=Main.img.getSubimage(4,113,14,14);
            i[1]=Main.img.getSubimage(20,113,14,14);
            i[2]=Main.img.getSubimage(36,113,14,14);
            i[3]=Main.img.getSubimage(52,113,14,14);
            i[4]=Main.img.getSubimage(68,113,14,14);
            i[5]=Main.img.getSubimage(84,113,14,14);
            i[6]=Main.img.getSubimage(100,113,14,14);
            i[7]=Main.img.getSubimage(116,113,14,14);
        }
        i[8]=Main.img.getSubimage(132,65,14,14);
        i[9]=Main.img.getSubimage(148,65,14,14);
        i[10]=Main.img.getSubimage(164,65,14,14);
        i[11]=Main.img.getSubimage(180,65,14,14);
        i[12]=Main.img.getSubimage(0,0,1,1);
        //Immagini per la morte dei fantasmini
        dead[0]=Main.img.getSubimage(132,81,14,14);
        dead[1]=Main.img.getSubimage(148,81,14,14);
        dead[2]=Main.img.getSubimage(164,81,14,14);
        dead[3]=Main.img.getSubimage(180,81,14,14);
        //n=i[0];
    }

    public void Life(){
        //Sezione dell'intelligenza artificiale

        this.start=false;
        //AZIONI PRELIMINARI
        if(nuovo){                                      //aggiungere nella mappa posizioni particolari "nodi/incroci" dove i nemici possono decidere di girare
            exitGhost();
        }
        //SVOLGIMENTO
        for(;;){
            //MaingOver non è sincronizzato quindi alcuni fanno in tempo ad uscire ed altri no
            //Pg aggiorna la variabile quando si deve resettare il gioco
            //QUASI FIXATO
            if(Main.gOver||Main.stop){
                //Quando muore pacman, i fantasmi spariscono fino alla sua rinascita
                n=i[12];
                break;
            }
            //L'oppure 4 è inserito solo per i nodi a 2 vie
            //Probabilmente mettendo un != 1 funzionerebbe meglio
            //Map.maze[controller.getPathY()][controller.getPathX()]=='2'||Map.maze[controller.getPathY()][controller.getPathX()]=='3'||Map.maze[controller.getPathY()][controller.getPathX()]=='4'||Map.maze[controller.getPathY()][controller.getPathX()]=='0'||Map.maze[controller.getPathY()][controller.getPathX()]=='7'||Map.maze[controller.getPathY()][controller.getPathX()]=='5'
            if(nuovo&&!uscito){
                exitGhost();
            }
            if(Map.maze[controller.getPathY()][controller.getPathX()]!='1'){ //SE INCONTRA UNNODO
                //Se non sono mangiabile e riesco a vedere Pacman OPPURE se non sono mangiato
                if(eated){
                    if(goToHome()) {
                        //Sono arrivato a casa
                        this.start=true;
                        this.ready=true;
                        controller.tX=0;
                        controller.tY=0;
                        controller.vel=250;
                        nuovo=true;
                        uscito=false;
                        eated=false;
                        return;
                    }
                }else{
                    if((radar()&&Main.Eat==0)){
                        try {
                            corri(Follow(Main.pg.nodex, Main.pg.nodey), true);
                        }catch (Exception e){
                            corri(ldir,true);
                        }
                    }
                    else{
                        //Se sono mangiabile, fuggo
                        //Altrimenti corro a cazzo
                        if(Main.Eat==1)
                            Fuga();
                        else {
                            try {
                                corri(cieco(), true);
                            }catch (Exception e){corri(ldir,true);}
                        }
                    }
                }
            }


            try{
                Thread.sleep(10);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //ASPETTO 2 SECONDI PRIMA DI RIGENERARE PERCHE DEVO METTERE UN MENU
        //FIX!!!
        n=i[12];
        FirstLaunch(bornx,borny,color);

        this.start=false;
        this.ready=true;

    }

    public void run(){
        if(Main.stateOfGame==0||Main.stateOfGame==3){
            try{
                Thread.sleep(10);
            }catch (Exception e){}
        }
        //Se devo utilizzare l'intelligenza artificiale per muovermi
        if(activated) {
            for (; ; ) {
                uscito = false;
                Audio.stopPlayEated();
                nuovo = true;
                Map.maze[12][13] = Map.maze[12][14] = '3';
                //Quando Pg vuole, e tutti sono pronti, tutti i nemici si rigenerano
                if (!this.start) {
                    try {
                        Thread.sleep(20);
                    } catch (Exception e) {
                    }
                } else {
                    n = i[0];
                    //Do qualche secondo di vantaggio
                    try {
                        Thread.sleep(1500);
                    } catch (Exception e) {

                    }
                    if (Main.startGame) {
                        Life();
                    }
                }
            }
        }
        else{
            if (controlled) {
                n = i[0];
                for (; ; ) {
                    Audio.stopPlayEated();
                    controller.stop=true;
                    if (!this.start) {
                        try {
                            Thread.sleep(20);
                        } catch (Exception e) {
                        }
                    } else {
                        //Do qualche secondo di vantaggio
                        n = i[0];
                        try {
                            Thread.sleep(1500);
                        } catch (Exception e) {

                        }
                        if (Main.startGame) {
                            //Seguo le indicazioni di controller.changeDir
                            controller.stop=false;
                            start = false;
                            ready = false;
                            for (; ; ) {
                                if(eated&&Map.maze[controller.pathy][controller.pathx]=='7'){
                                    //Rigenero
                                    this.start=true;
                                    this.ready=true;
                                    controller.tX=0;
                                    controller.tY=0;
                                    controller.vel=250;
                                    nuovo=true;
                                    uscito=false;
                                    eated=false;
                                }
                                if (Main.gOver) {
                                    //Quando muore pacman, i fantasmi spariscono fino alla sua rinascita
                                    n = i[12];
                                    //Ed azzerano la loro direzione
                                    controller.changeDir='k';
                                    break;
                                }
                                switch (controller.changeDir) {
                                    case 'a': {
                                        controller.MoveSx(true);
                                        ldir = 'a';
                                        if (controller.Direction != 'a')
                                            controller.changeDir = controller.Direction;
                                    }
                                    break;
                                    case 'd': {
                                        controller.MoveDx(true);
                                        ldir = 'd';
                                        if (controller.Direction != 'd')
                                            controller.changeDir = controller.Direction;
                                    }
                                    break;
                                    case 's': {
                                        controller.MoveDw(true);
                                        ldir = 's';
                                        if (controller.Direction != 's')
                                            controller.changeDir = controller.Direction;
                                    }
                                    break;
                                    case 'w': {
                                        controller.MoveUp(true);
                                        ldir = 'w';
                                        if (controller.Direction != 'w')
                                            controller.changeDir = controller.Direction;
                                    }
                                    break;
                                    default: {
                                        //Se viene premuto un tasto non consentito, changeDir viene cambiata nell'ultima direzione
                                        controller.changeDir = ldir;
                                        //Per qualche motivo, il buffered out causa problemi quindi eseguo un flush
                                        System.out.flush();
                                    }
                                    break;
                                }
                                try {
                                    Thread.sleep(10);
                                } catch (Exception e) {

                                }
                            }
                            n = i[12];
                            FirstLaunch(bornx, borny, color);
                            this.start = false;
                            this.ready = true;
                        }
                    }
                }
            }
            else {
                n = i[0];
                while(true){
                    Audio.stopPlayEated();
                    controller.stop=true;
                    if (!this.start) {
                        try {
                            Thread.sleep(20);
                        } catch (Exception e) {
                        }
                    } else {
                        n = i[0];
                        //Do qualche secondo di vantaggio
                        try {
                            Thread.sleep(1500);
                        } catch (Exception e) {

                        }
                        if (Main.startGame) {
                            controller.stop=false;
                            start = false;
                            ready = false;
                            for (; ; ) {
                                if(eated&&Map.maze[controller.pathy][controller.pathx]=='7'){
                                    //Rigenero
                                    this.start=true;
                                    this.ready=true;
                                    controller.tX=0;
                                    controller.tY=0;
                                    controller.vel=250;
                                    nuovo=true;
                                    uscito=false;
                                    eated=false;
                                }
                                if (Main.gOver || Main.stop) {
                                    //Quando muore pacman, i fantasmi spariscono fino alla sua rinascita
                                    n = i[12];
                                    break;
                                }
                                corri(controller.changeDir, false);
                                try {
                                    Thread.sleep(10);
                                } catch (Exception e) {

                                }
                            }
                            n = i[12];
                            FirstLaunch(bornx, borny, color);

                            this.start = false;
                            this.ready = true;

                        }
                    }
                        }
                    }


                }
                    //Seguo le indicazioni di dir(COLORE)


    }


    //X-pacman Y-pacman X-fantasma Y-fantasma Distanza_percorsa Distanza_da_pacman
    public char Follow(int x,int y) {
        GraphPath<String,DefaultEdge> graphPath=null;
        try{graphPath = dijkstra.findPathBetween(Map.graph,(controller.getPathY()+","+controller.getPathX()),(y+","+x));}
        catch (IllegalArgumentException e){ldir=cieco();return ldir;}

        //prendo solo il secondo elemento della lista di archi ritornata
        //perchè il primo corrisponde a se stesso
        //se riscontro un errore, vuol dire che il Pacman è di fronte a me
        //quindi prendo il primo elemento
        int gox;
        int goy;
        try {
            gox = Integer.parseInt(graphPath.getVertexList().get(1).split(",")[1]);
            goy = Integer.parseInt(graphPath.getVertexList().get(1).split(",")[0]);
        }catch(Exception e){
            gox = Integer.parseInt(graphPath.getVertexList().get(0).split(",")[1]);
            goy = Integer.parseInt(graphPath.getVertexList().get(0).split(",")[0]);
        }
        //Se il fantasma si dovrebbe muovere sull'asse x, procedo con l'asse x altrimenti con y
        if(gox!=controller.getPathX()){
            //Sopra o sotto
            if(gox>controller.getPathX()){
                ldir='d';
                return 'd';
            }else{
                ldir='a';
                return 'a';
            }
        }else
        if(goy!=controller.getPathY()){
            //Destra o sinistra
            if(goy>controller.getPathY()){
                ldir='s';
                return 's';
            }else{
                ldir='w';
                return 'w';
            }
        }
        //Se sono a casa ritorno r e sono già spawnato
        if((controller.getPathX()==14&&controller.getPathY()==14)&&nuovo==false)
            return 'r';

        //Se non so dove andare(sono nella direzione del tunnel), vado nell'ultima direzione presa
        return cieco();
    }


    public char esci() {
        if(controller.getPathY()==11){uscito=true;return 'o';}


        if(((controller.getPathX()==13)||(controller.getPathX()==14))){
            return('w');}

        else{
            if(Map.maze[controller.getPathY()-1][controller.getPathX()]!='1'&&controller.getPathY()>11){
                return('w');}
            else
            if(Map.maze[controller.getPathY()+1][controller.getPathX()]!='1'&&controller.getPathY()<11){
                return('s');}
            else
            if(Map.maze[controller.getPathY()][controller.getPathX()+1]!='1'&&controller.getPathX()<13){
                return('d');}
            else
            if(Map.maze[controller.getPathY()][controller.getPathX()-1]!='1'&&controller.getPathX()>13){
                return('a');}
        }
        return 'o';
    }


    public char cieco(){                            //funzione che trova una direzione da prendere per il png in maniera casuale
        int l=0;
        char w[]=new char[4];
        //Azzero l'array w
        for(int i=0;i<4;i++){
            w[i]=' ';
        }
        //Quante direzioni ho disponibili?
        //Controllo tutte e 4 ed aggiorno il counter
        if(Map.maze[controller.getPathY()][controller.getPathX()+1]!='1'){
            if(ldir!='a') {
                l++;
                w[0]='d';
            }
        }
        if(Map.maze[controller.getPathY()+1][controller.getPathX()]!='1'){
            if(ldir!='w') {
                l++;
                w[1]='s';
            }
        }
        if(Map.maze[controller.getPathY()][controller.getPathX()-1]!='1'){
            if(ldir!='d') {
                l++;
                w[2]='a';
            }
        }
        if(Map.maze[controller.getPathY()-1][controller.getPathX()]!='1'){
            if(ldir!='s') {
                l++;
                w[3]='w';
            }
        }
        //Ordino l'array cosicchè l'ultima posizione sia occupata da null
        for(int i=0;i<3;i++) {
            if(w[i]==' '){
                for(int j=i+1;j<4;j++) {
                    if(w[j]!=' ') {
                        //Scambio di posto
                        w[i] = w[j];
                        w[j] = ' ';
                        //esco dal secondo for
                        break;
                    }
                }
            }
        }
        //Per prendere una direzione a caso, uso una variabile con Math.rand() e faccio il modulo di 3 o nell'altro caso di 2
        //Il massimo delle direzioni che posso prendere è 3 perchè non posso andare indietro(caso peggiore), sono ad un incrocio 4 way
        //Da eliminare |
        //             V
        if(l==4)
            System.out.println("PROBLEMA NE RIGA 310");
        if(l==3){
            //Se sono su un incrocio 4 way
            //Scelgo una direzione a caso tra le 3
            int d=(int)(Math.random()*10);
            d=d%3;
            switch (d){
                case 0:{
                    ldir=w[0];
                    return w[0];
                }
                case 1:{
                    ldir=w[1];
                    return w[1];
                }
                case 2:{
                    ldir=w[2];
                    return w[2];
                }
            }
        }
        //Se ho 2 direzioni, scelgo a caso tra queste due
        if(l==2){
            //Scelgo una direzione tra le 2
            //Scelgo una direzione a caso tra le 3
            int d=(int)(Math.random()*10);
            d=d%2;
            switch (d){
                case 0:{
                    ldir=w[0];
                    return w[0];
                }
                case 1:{
                    ldir=w[1];
                    return w[1];
                }
            }

        }
        //Se ho solo 1 direzioni disponibili, scelgo solo quella
        if(l==1){//Vado nella direzione opposta a quella di partenza
            ldir=w[0];
            return w[0];
        }
        //NON CI SONO VICOLI CIECHI IN QUESTA MAPPA
        //SE DEVONO ESSERE AGGIUNTI, QUI CI VA UN ALTRO IF
        return 'e';
    }

    public boolean radar(){

        //Se rientra nel raggio
        if(Math.abs(controller.getPathX()-Main.pg.controller.getPathX())<(Main.range/2)&&Math.abs(controller.getPathY()-Main.pg.controller.getPathY())<(Main.range/2))
            return true;
        else
            return false;
    }

    public void corri(char direzione,boolean toSend){
        if(Main.server.getID()==99){
            //Se sono nel singleplayer, per evitare problemi imposto changeDir
            controller.changeDir=direzione;
        }
        switch(direzione){
            case 'a':{
                controller.MoveSx(toSend);
                ldir='a';
                if(controller.Direction!='a')
                    controller.changeDir=controller.Direction;}break;
            case 'd':{
                controller.MoveDx(toSend);
                ldir='d';
                if(controller.Direction!='d')
                    controller.changeDir=controller.Direction;}break;
            case 's':{
                controller.MoveDw(toSend);
                ldir='s';
                if(controller.Direction!='s')
                    controller.changeDir=controller.Direction;}break;
            case 'w':{
                controller.MoveUp(toSend);
                ldir='w';
                if(controller.Direction!='w')
                    controller.changeDir=controller.Direction;}break;
            default: {
                //Se viene premuto un tasto non consentito, changeDir viene cambiata nell'ultima direzione
                controller.changeDir=ldir;
                //Per qualche motivo, il buffered out causa problemi quindi eseguo un flush
                System.out.flush();
            }break;
        }
    }


    //DA SISTEMARE
    //Prima di sistemare questo metodo, probabilmente bisogna sistemare il metodo Follow
    public void Fuga(){
        try{corri(cieco(),true);}
        catch (Exception e){
            corri(ldir,true);
        }
    }

    public boolean goToHome(){
        char c=Follow(14,14);
        //Se Follow ritorna 'r' ovvero, che è arrivato a casa
        //ritorno true, quindi rigenero il fantasma senza cambiare la sua posizione
        if(c=='r')
            return true;
        corri(c,true);
        return false;
    }

    public void exitGhost(){
        for(;uscito!=true;){corri(esci(),true);}
        Map.maze[12][13]=Map.maze[12][14]='6'; // Muro valicabile solo da morto
        nuovo=false;
        //Scelgo una direzione a caso (sx o dx)
        //chiamo cieco() per scegliere una direzione a caso che non sia su o giu
        int d;
        d=(int)(Math.random()*10);
        d++;
        if(d%2==0){
            corri('a',true);
        }else{
            corri('d',true);
        }
    }

    public void stopThread(double g){
        try{
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}