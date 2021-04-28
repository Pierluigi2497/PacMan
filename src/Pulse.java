import java.awt.image.BufferedImage;
import java.io.Serial;
import java.time.Clock;

public class Pulse implements Runnable{
    public Clock clock=Clock.systemDefaultZone();
    public long dotMillis;
    public Boolean c=true;
    public long eatableMillis;
    public long almostEatableMillis;
    public long pulseWhite;
    static int situation=0; //0-Nothing  1-Blue  2-Pulsing White-Blue
    private BufferedImage[] i=new BufferedImage[4];

    public Pulse(){

    }

    public void run(){
        i[0]=Main.img.getSubimage(132,65,14,14);
        i[1]=Main.img.getSubimage(148,65,14,14);
        i[2]=Main.img.getSubimage(164,65,14,14);
        i[3]=Main.img.getSubimage(180,65,14,14);
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
        if(Map.maze[Main.pg.pathx][Main.pg.pathy]==5){Main.Eat=1;Map.maze[Main.pg.pathx][Main.pg.pathy]=0;Main.score=Main.score+50;} //Se mangia la palla grossa lui può mangiare i fantasmi e la palla diventa uno spazio vuoto 0
        else if(Map.maze[Main.pg.pathx][Main.pg.pathy]==4){Map.maze[Main.pg.pathx][Main.pg.pathy]=0;Main.score=Main.score+10;}
        else if(Map.maze[Main.pg.pathx][Main.pg.pathy]==2){Map.maze[Main.pg.pathx][Main.pg.pathy]=3;Main.score=Main.score+10;}
        //Controllo Morte
        for(int i=0;i<4;i++){
            if(Main.ne[i].pathy==Main.pg.pathy&&Main.ne[i].pathx==Main.pg.pathx){
                if(Main.Eat==1)
                    Main.ne[i].Fuga();
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
            System.out.println(clock.millis()-eatableMillis);
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

    /*
    public void notSureEatable(){
        if(pulseWhite==0){
            pulseWhite=clock.millis();
        }
        if(almostEatableMillis==0){
            almostEatableMillis=clock.millis();
        }
        if((clock.millis()-pulseWhite)>350){
            if(c==true){
                c=false;

                for(int i=0;i<4;i++){
                    Main.ne[i].i[8]=this.i[2];
                    Main.ne[i].i[9]=this.i[3];
                }
            }
            else if(c==false){
                c=true;
                for(int i=0;i<4;i++){
                    Main.ne[i].i[8]=this.i[0];
                    Main.ne[i].i[9]=this.i[1];
                }

            }
            pulseWhite=0;

        }
        if((clock.millis()-almostEatableMillis)>2000){
            for(int i=0;i<4;i++){
                Main.ne[i].i[8]=this.i[0];
                Main.ne[i].i[9]=this.i[1];
            }
            situation=0;
        }
    }
*/

}