import java.time.Clock;

public class Pulse implements Runnable{
    public Clock clock=Clock.systemDefaultZone();
    public long dotMillis;
    public long eatableMillis;
    public long almostEatableMillis;
    //Time of eateble as blue, the white is (timeofEateable+4000)
    public int timeOfEateable=6000;
    public long pulseWhite;
    private int[] i=new int[4];
    public static boolean win=false;
    static int situation=0; //0-Nothing  1-Blue  2-Pulsing White-Blue

    public Pulse(){

    }

    public void run(){
        //Aspetto che il giocatore esca dal menu
        dotMillis=clock.millis();
        almostEatableMillis=0;
        pulseWhite=0;
        eatableMillis=0;
        countDots();
        for(;;){
            pacControl();
            sureEatable();
            checkScore();
            checkDots();
            try {
                Thread.sleep(1);
            }catch (Exception e){
                e.printStackTrace();
            }
            //Quando chiudo finestra, esco dal Thread
            //Non funziona
            if (Main.stop)
                break;
        }
    }


    public void pacControl(){
        //Se mangia la palla grossa lui può mangiare i fantasmi e la palla diventa uno spazio vuoto 0
        if(Map.maze[Main.pg.controller.pathy][Main.pg.controller.pathx]=='5'){
            Main.Eat=1;Map.maze[Main.pg.controller.pathy][Main.pg.controller.pathx]='0';Main.score=Main.score+50;Main.dots--;
            if(Main.audioThread.isAlive())
                Audio.playEatable();
            //Quando mangio la palla grossa, resetto ldir così i fantasmi si possono girare indietro
            //Imposto una velocità più alta(più alto il valore, più lenti sono i fantasmi)
            for(int i=0;i<Main.Ngiocatori;i++){
                Main.ne[i].ldir=' ';
                Main.ne[i].controller.vel=350;
            }
        }
        else if(Map.maze[Main.pg.controller.pathy][Main.pg.controller.pathx]=='4'){
            Map.maze[Main.pg.controller.pathy][Main.pg.controller.pathx]='0';Main.score=Main.score+10;Main.dots--;}
        else if(Map.maze[Main.pg.controller.pathy][Main.pg.controller.pathx]=='2'){Map.maze[Main.pg.controller.pathy][Main.pg.controller.pathx]='3';Main.score=Main.score+10;Main.dots--;}
        else
            //Controllo Morte
            for(int i=0;i<4;i++){
                if(Main.ne[i].controller.pathy==Main.pg.controller.pathy&&Main.ne[i].controller.pathx==Main.pg.controller.pathx&&!Main.ne[i].eated){
                    if(Main.Eat==1)
                    {
                        //Setto una variabile che possono vedere tutti i nemici
                        //in questo modo possono tornare a casa mangiati e uscirne interi
                        if(Main.audioThread.isAlive())
                            Audio.playEated();
                        Main.ne[i].eated=true;
                        Main.pause=true;
                        Main.ne[i].controller.vel=135;
                        Main.score+=100;
                        Audio.eatedGhost=true;
                    }
                    else {
                        if(Main.audioThread.isAlive())
                            Audio.playEated();
                        Main.gOver = true;
                    }
                }}
    }




    public void sureEatable(){
        if(Main.Eat==1){
            //Condizione dalla palla mangiata
            if(eatableMillis==0){
                //E' la prima volta che entro?
                eatableMillis=clock.millis();
                situation=1;
            }
            if((clock.millis()-eatableMillis)<timeOfEateable){
                situation=1;
                return;
            }
            if((clock.millis()-eatableMillis)<(timeOfEateable+4000)){
                situation=2;
                return;
            }
            situation=0;
            Main.Eat=0;
            eatableMillis=0;
            Audio.stopPlayEatable();
            //Reimposto tutte le velocità dei fantasmini a quella originale
            for(int i=0;i<Main.Ngiocatori;i++){
                //Se il tempo è scaduto e non sono stato mangiato, reimposto la velocità
                if(!Main.ne[i].eated) {
                    Main.ne[i].controller.vel = 250;
                }
            }
        }
    }

    public static void resetAll(){
        //Resetto matrice
        for(int i=0;i<Map.y;i++){
            for(int j=0;j<Map.x;j++){
                Map.maze[i][j]=Map.originalMaze[i][j];
            }
        }
        //Resetto score
        Main.score=0;
        //Resetto vite
        Main.Life=3;
        //Resetto livelli
        Main.Level=1;

        Main.startGame=false;

    }

    public void checkScore(){
        i[0]=Main.score%10;
        i[1]=Main.score%100;
        i[1]=i[1]/10;
        i[2]=Main.score%1000;
        i[2]=i[2]/100;
        i[3]=Main.score/1000;

        for(int l=0;l<4;l++)
            switch(i[l]){
                case 0:{Main.sf[l]=Main.s[0];}break;
                case 1:{Main.sf[l]=Main.s[1];}break;
                case 2:{Main.sf[l]=Main.s[2];}break;
                case 3:{Main.sf[l]=Main.s[3];}break;
                case 4:{Main.sf[l]=Main.s[4];}break;
                case 5:{Main.sf[l]=Main.s[5];}break;
                case 6:{Main.sf[l]=Main.s[6];}break;
                case 7:{Main.sf[l]=Main.s[7];}break;
                case 8:{Main.sf[l]=Main.s[8];}break;
                case 9:{Main.sf[l]=Main.s[9];}break;
            }
    }

    private void countDots(){
        //Conto quante palline ci stanno
        Main.dots=0;
        for(int i = 0;i<Map.y;i++){
            for(int j=0;j<Map.x;j++){
                if(Map.maze[i][j]=='2'||Map.maze[i][j]=='4'||Map.maze[i][j]=='5'){
                    Main.dots++;
                }
            }
        }
        Audio.dots=Main.dots;
    }

    private void checkDots(){

        if(Main.dots==0){
            //Ho vinto
            win=true;
            Main.gOver=true;
            //Resetto matrice
            for(int i=0;i<Map.y;i++){
                for(int j=0;j<Map.x;j++){
                    Map.maze[i][j]=Map.originalMaze[i][j];
                }
            }
            //Aumento Livello
            Main.Level++;
            countDots();
        }
    }

}