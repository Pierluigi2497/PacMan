import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import java.awt.image.BufferedImage;
import java.time.Clock;
import java.util.*;
public class Ne implements Runnable {
    public int pathx,pathy;
    public int bornx,borny; //Queste variabili serviranno per rigenerare il thread
    public int vel=250;
    public int tX=0;  //Translate x
    public int tY=0;  //Translate y
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
    public boolean start=true;
    public boolean eated=false;
    private DijkstraShortestPath<String, DefaultEdge> dijkstra;

    public Ne(int x,int y,String a){
        bornx=x;
        borny=y;
        color=a;
        FirstLaunch(bornx,borny,color);
    }

    public void FirstLaunch(int x,int y,String a){
        tX=0;
        tY=0;
        color=a;
        pathx=x;
        pathy=y;
        eated=false;
        vel=250;
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
        this.start=false;
        //AZIONI PRELIMINARI
        if(nuovo){										//aggiungere nella mappa posizioni particolari "nodi/incroci" dove i nemici possono decidere di girare
            for(;uscito!=true;){corri(esci());}
            Map.maze[12][13]=Map.maze[12][14]='6'; // Muro valicabile solo da morto
            nuovo=false;
            //Scelgo una direzione a caso (sx o dx)
            //chiamo cieco() per scegliere una direzione a caso che non sia su o giu
            int d;
            d=(int)(Math.random()*10);
            d++;
            if(d%2==0){
                corri('a');
            }else{
                corri('d');
            }
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
            if(Map.maze[pathy][pathx]=='2'||Map.maze[pathy][pathx]=='3'||Map.maze[pathy][pathx]=='4'||Map.maze[pathy][pathx]=='0'||Map.maze[pathy][pathx]=='7'){ //SE INCONTRA UNNODO
                //Se non sono mangiabile e riesco a vedere Pacman OPPURE se non sono mangiato
                if(eated){
                    if(goToHome()) {
                        this.start=true;
                        this.ready=true;
                        tX=0;
                        tY=0;
                        vel=250;
                        nuovo=true;
                        uscito=false;
                        eated=false;
                        return;
                    }
                }else{
                    if((radar()&&Main.Eat==0)){
                      corri(Follow(Main.pg.nodex,Main.pg.nodey));
                    }
                 else{
                     //Se sono mangiabile, fuggo
                     //Altrimenti corro a cazzo
                        if(Main.Eat==1)
                            Fuga();
                        else
                            corri(cieco());
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
        for(;;){
            uscito=false;
            nuovo=true;
            Map.maze[12][13]=Map.maze[12][14]='3';
            //Quando Pg vuole, e tutti sono pronti, tutti i nemici si rigenerano
            if(!this.start){
                try {
                    //Nascosto bug diminuendo millis, potrebbe capitare che la variabile sync non faccia il suo dovere
                    //a causa di una errata coordinazione
                    //NON FUNZIONA UGUALE :(
                    //TO FIX
                    //Forse Fixato

                    Thread.sleep(20);
                }catch (Exception e){}
            }
            else{
            n=i[0];
            //Do qualche secondo di vantaggio
            try {
                Thread.sleep(1500);
            }catch (Exception e){

            }
            Life();
          }
        }
    }


    //X-pacman Y-pacman X-fantasma Y-fantasma Distanza_percorsa Distanza_da_pacman
    public char Follow(int x,int y) {
        GraphPath<String,DefaultEdge> graphPath;
        try{graphPath = dijkstra.findPathBetween(Map.graph,(pathy+","+pathx),(y+","+x));}
        catch (IllegalArgumentException e){return cieco();}

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
        if(gox!=pathx){
            //Sopra o sotto
            if(gox>pathx){
                ldir='d';
                return 'd';
            }else{
                ldir='a';
                return 'a';
            }
        }else
        if(goy!=pathy){
            //Destra o sinistra
            if(goy>pathy){
                ldir='s';
                return 's';
            }else{
                ldir='w';
                return 'w';
            }
        }
        //Se sono a casa ritorno r e sono già spawnato
        if((pathx==14&&pathy==14)&&nuovo==false)
            return 'r';

        //Se non so dove andare(sono nella direzione del tunnel), vado nell'ultima direzione presa
        return cieco();
    }


    public char esci() {
        if(pathy==11){uscito=true;return 'o';}


        if(((pathx==13)||(pathx==14))){
            return('w');}

        else{
            if(Map.maze[pathy-1][pathx]!='1'&&pathy>11){
                return('w');}
            else
            if(Map.maze[pathy+1][pathx]!='1'&&pathy<11){
                return('s');}
            else
            if(Map.maze[pathy][pathx+1]!='1'&&pathx<13){
                return('d');}
            else
            if(Map.maze[pathy][pathx-1]!='1'&&pathx>13){
                return('a');}
        }
        return 'o';
    }


    public char cieco(){							//funzione che trova una direzione da prendere per il png in maniera casuale
        int d;
        d=(int)(Math.random()*10);
        d++;					//d=decisione, numero casuale che determinerà la direzione del nemico in base a questi criteri:
        //Il fantasma NON TORNA MAI INDIETRO
        if((d%2)==0){								//se pari si muoverà su asse x
            d=(int)(Math.random()*10);
            d++;									//ricalcolo
            if((d%2)==0){
                try{
                    //se di nuovo pari andrà a destra
                    //se vengo da quella direzione, vado dall'altra parte
                    if(ldir!='a'){
                        if(Map.maze[pathy][pathx+1]!='1') {
                            ldir='d';
                            return 'd';
                        }
                    }
                }catch(Exception e){ldir='d';return 'd';}
            }
                try {
                    //se è dispari andrà a sinistra
                    if (ldir != 'd') {
                        if (Map.maze[pathy][pathx - 1] != '1')
                            ldir='a';
                            return 'a';
                    }
                }
                catch(Exception e){ldir='a';return 'a';}
        }
        //se dispari si muoverà su asse y
            d=(int)(Math.random()*10);
            d++;									//ricalcolo
            if((d%2)==0){
                //se pari andrà in su
                if(ldir!='s') {
                    if (Map.maze[pathy - 1][pathx] != '1'){
                        ldir='w';
                        return 'w';
                    }
                }
            }
            //se dispari andra in giù
            if(ldir!='w') {
                if (Map.maze[pathy + 1][pathx] != '1'){
                    ldir='s';
                    return 's';
                }
            }
        return 'o';
    }

    public boolean radar(){

        //Se rientra nel raggio
        if(Math.abs(pathx-Main.pg.pathx)<(Main.range/2)&&Math.abs(pathy-Main.pg.pathy)<(Main.range/2))
            return true;
        else
            return false;
    }

    //Aumenta i pixel di un quadrato di array grafico(la grandezza di uno spostamento reale) per creare una transizione
    public void Trans(char dir){
        //Inserisco all'interno di ogni for un controllo di morte Pacman per accellerare la rigeneragione del gioco
        int v;


        if(dir=='w'||dir=='s'){
            v=vel/Main.dY;

            if(dir=='w'){
                while(Map.maze[pathy-1][pathx]!='1'){
                    for(tY=0;Math.abs(tY)!=Main.dY;tY--){
                        aSprite(dir);
                        if(Main.gOver)
                            return ;

                        try{Thread.sleep(v);}catch(Exception e){}
                    }
                    tY=0;
                    pathy--;
                    if(Map.maze[pathy][pathx]=='2'||Map.maze[pathy][pathx]=='3')
                        break;}

            }
            else{
                while(Map.maze[pathy+1][pathx]!='1'){
                    for(tY=0;Math.abs(tY)!=Main.dY;tY++){
                        aSprite(dir);
                        if(Main.gOver)
                            return ;
                        try{Thread.sleep(v);}catch(Exception e){}
                    }
                    tY=0;
                    pathy++;
                    if(Map.maze[pathy][pathx]=='2'||Map.maze[pathy][pathx]=='3')
                        break;
                }

            }
        }
        else {
            v=vel/Main.dX;
            if(dir=='a'){
                try{
                    while(Map.maze[pathy][pathx-1]!='1'){
                        for(tX=0;Math.abs(tX)!=Main.dX;tX--){
                            aSprite(dir);
                            if(Main.gOver)
                                return ;
                            try{Thread.sleep(v);}catch(Exception e){}
                        }
                        tX=0;
                        pathx--;
                        //Se incontro un incrocio, esco dalla transizione e vedo quale direzione prendere
                        //Esco pure se incontro un 7(Punto di rigenerazione) perchè il fantasmino deve uscire da casa
                        if(Map.maze[pathy][pathx]=='2'||Map.maze[pathy][pathx]=='3'||Map.maze[pathy][pathx]=='7')
                            break;
                    }
                }catch(Exception e){
                    //Sono qui perchè sono uscito dalla mappa, se ho preso un tunnel, mi teletrasporto
                    if(pathx==0){

                    }
                    for(tX=0;Math.abs(tX)!=Main.dX;tX--){
                        aSprite(dir);
                        if(Main.gOver)
                            return ;
                        try{Thread.sleep(v);}catch(Exception a){}
                    }
                    tX=0;
                    pathx=27;Trans('a');}
            }
            else{
                try{
                    while(Map.maze[pathy][pathx+1]!='1'){
                        for(tX=0;Math.abs(tX)!=Main.dX;tX++){
                            aSprite(dir);
                            if(Main.gOver)
                                return ;
                            try{Thread.sleep(v);}catch(Exception e){}
                        }
                        tX=0;
                        pathx++;
                        if(Map.maze[pathy][pathx]=='2'||Map.maze[pathy][pathx]=='3'||Map.maze[pathy][pathx]=='7')
                            break;
                    }}catch(Exception e){
                    for(tX=0;Math.abs(tX)!=Main.dX;tX++){
                        aSprite(dir);
                       if(Main.gOver)
                            return ;
                        try{Thread.sleep(v);}catch(Exception a){}
                    }
                    pathx=0;
                    tX=0;
                    Trans('d');}
            }
        }
    }

    public void aSprite(char dir){
        if(eated){
            eatedSprite(dir);
            return;
        }
        if(Pulse.situation==0){
            switch(dir){
                case 'w':{if(c){
                    n=i[4];
                    c=!c;
                }else{
                    n=i[5];
                    c=!c;
                }}break;
                case 'a':{if(c){
                    n=i[2];
                    c=!c;
                }else{
                    n=i[3];
                    c=!c;
                }}break;
                case 'd':{if(c){
                    n=i[0];
                    c=!c;
                }else{
                    n=i[1];
                    c=!c;
                }}break;
                case 's':{if(c){
                    n=i[6];
                    c=!c;
                }else{
                    n=i[7];
                    c=!c;
                }}break;

            }
        }
        else{
            if(Pulse.situation==1){
                blueSprite();
            }
            else if(Pulse.situation==2){
                //Se situation==2
                //Lo si da per scontato quindi
                //Evito di fare un if
                whiteSprite();
            }
        }


    }


    public void corri(char direzione){

        //Valori Direzione:
        //w=su  d=destra  s=giù  a=sinistra

        switch(direzione){
            case 'w':{Trans('w');}break;

            case 'd':{
                Trans('d');}break;

            case 's':{
                Trans('s');}break;

            case 'a':{
                Trans('a');}break;
        }
    }

    public void blueSprite(){
        if(c){
            n=i[8];
            c=!c;
        }else{
            n=i[9];
            c=!c;
        }
    }

    public void whiteSprite(){
        //Ogni 500 ms cambio colore, da bianco a blu
        //E' la prima volta che entro?
        if(checkms==0){
            checkms= clock.millis();
            c=true;

        }else{
            if((clock.millis()-checkms)<200){
                //se devo cambiare colore, se d==true,faccio l'animazione del bianco altrimenti
                // divento blue
                if(d) {
                    if (c) {
                        n = i[10];
                        c = !c;
                    } else {
                        n = i[11];
                        c = !c;
                    }
                }else{
                    if (c) {
                        n = i[8];
                        c = !c;
                    } else {
                        n = i[9];
                        c = !c;
                    }
                }
            }else{
                //Quando finisco l'animazione di un colore, resetto checkms
                //e cambio la variabile per il colore
                checkms=clock.millis();
                d=!d;
            }


        }
    }

    public void eatedSprite(char dir){
        switch (dir){
            case 'w':{n=dead[2];}break;
            case 'a':{n=dead[1];}break;
            case 's':{n=dead[3];}break;
            case 'd':{n=dead[0];}break;
        }
    }


    //DA SISTEMARE
    //Prima di sistemare questo metodo, probabilmente bisogna sistemare il metodo Follow
    public void Fuga(){
        corri(cieco());
    }

    public boolean goToHome(){
        char c=Follow(14,14);
        //Se Follow ritorna 'r' ovvero, che è arrivato a casa
        //ritorno true, quindi rigenero il fantasma senza cambiare la sua posizione
        if(c=='r')
            return true;
        corri(c);
        return false;
    }

}



