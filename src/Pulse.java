import java.time.Clock;

public class Pulse implements Runnable{
    public Clock clock=Clock.systemDefaultZone();
    public long dotMillis;
    public Boolean c=true;
    public long eatableMillis;
    public long almostEatableMillis;
    public long pulseWhite;
    static int situation=0; //0-Nothing  1-Blue  2-Pulsing White-Blue

    public Pulse(){
    }

    public void run(){
        dotMillis=clock.millis();
        almostEatableMillis=0;
        pulseWhite=0;
        eatableMillis=0;
        for(;;){
            pacControl();
            dotPulse();
            sureEatable();
            try {
                Thread.sleep(10);
            }catch (Exception e){

            }
            //Quando chiudo finestra, esco dal Thread
            //Non funziona
            if (Main.stop)
                break;
        }
    }


    public void pacControl(){
        //Se mangia la palla grossa lui può mangiare i fantasmi e la palla diventa uno spazio vuoto 0
        if(Map.maze[Main.pg.pathy][Main.pg.pathx]=='5'){
            Main.Eat=1;Map.maze[Main.pg.pathy][Main.pg.pathx]='0';Main.score=Main.score+50;
            //Quando mangio la palla grossa, resetto ldir così i fantasmi si possono girare indietro
            //Imposto una velocità più alta(più alto il valore, più lenti sono i fantasmi)
            for(int i=0;i<Main.Ngiocatori;i++){
                Main.ne[i].ldir=' ';
                Main.ne[i].vel=350;
            }
        }
        else if(Map.maze[Main.pg.pathy][Main.pg.pathx]=='4'){
            Map.maze[Main.pg.pathy][Main.pg.pathx]='0';Main.score=Main.score+10;}
        else if(Map.maze[Main.pg.pathy][Main.pg.pathx]=='2'){Map.maze[Main.pg.pathy][Main.pg.pathx]='3';Main.score=Main.score+10;}
        //Controllo Morte
        for(int i=0;i<4;i++){
            if(Main.ne[i].pathy==Main.pg.pathy&&Main.ne[i].pathx==Main.pg.pathx&&!Main.ne[i].eated){
                if(Main.Eat==1)
                {
                    //Setto una variabile che possono vedere tutti i nemici
                    //in questo modo possono tornare a casa mangiati e uscirne interi
                    Main.ne[i].eated=true;
                    Main.ne[i].vel=135;
                    Main.score+=100;
                }
                else
                    Main.gOver=true;
            }}
    }


    public void dotPulse(){
        if((clock.millis()-this.dotMillis)>=375){
            if(c){
                c=!c;
                Frame.Dot=Main.img.getSubimage(0,0,1,1);}
            else{c=!c;
                Frame.Dot=Main.img.getSubimage(152,31,16,16);}
            dotMillis=clock.millis();
        }


    }


    public void sureEatable(){
        if(Main.Eat==1){
            //Condizione dalla palla mangiata
            if(eatableMillis==0){
                //E' la prima volta che entro?
                eatableMillis=clock.millis();
                situation=1;
            }
            if((clock.millis()-eatableMillis)<4000){
                //Se non sono passati ancora 4 secondi, i fantasmi devono essere blu
                //La classe Ne gestisce il colore
                //Faccio gestire la morte del fantasma alla sua classe così da non appesantire Pulse

            }else if((clock.millis()-eatableMillis)<6000){
                situation=2;
                //Se non sono passati ancora 6 secondi ma ne sono passati solo 4, i fantasmi devono lampeggiare
            }else{
                situation=0;
                Main.Eat=0;
                eatableMillis=0;
            }
        }
    }
}