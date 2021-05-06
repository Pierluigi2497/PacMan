//IL PACMAN NON E BEN SINCRONIZZATO CON IL REPAINT, A CAUSA DI CIO L'ANIMAZIONE NON E BELLISSIMA
import java.awt.image.BufferedImage;

public class Pg implements Runnable{
    public int pathx,pathy;
    public char nodex,nodey;
    public int tX=0;  //Translate x
    public int tY=0;  //Translate y
    public int vel=200;//Inversa in ms
    private BufferedImage[] death=new BufferedImage[13];//1 immagine trasparente
    private BufferedImage[] pac=new BufferedImage[10];
    public BufferedImage Pac;
    private Boolean c=true;
    private char Direction=' ';
    private int i[]=new int[4];
    //Serve per sincronizzare i nemici ed il Pg
    static Boolean Restart=false;

    public Pg(int x,int y){
        FirstLunch(x,y);
    }

    public void FirstLunch(int x,int y){
        //Resetto la posizione
        pathx=x;
        pathy=y;
        //Resetto ultimo nodo letto
        nodex=6;
        nodey=8;
        //Resetto variabili d'animazione
        tX=0;
        tY=0;


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

    public void MoveDx(){
        try{
            //PRIMA FUNZIONAVA
            //Se mi trovo all'inizio della mappa(per l'asse x)
            //Per evitare IndexOut
            //Sono sicuro che posso andare a destra o a sinistra(Unica possibilità per i tunnel)
            //Quindi salto il primo controllo
            if(pathx==27&&!Main.gOver){
                Direction='d';
                Trans('d');
            }else{
            //UFFA!


            if(Map.maze[pathy][pathx+1]!='1'&&!Main.gOver){
                Direction='d';
                Trans('d');
            }
            }
        }
        catch(Exception e){Trans('d');}
    }

    public void MoveSx(){
        try {
            //PRIMA FUNZIONAVA
            //Se mi trovo all'inizio della mappa(per l'asse x)
            //Per evitare IndexOut
            //Sono sicuro che posso andare a destra o a sinistra(Unica possibilità per i tunnel)
            //Quindi salto il primo controllo
            if (pathx == 0 && !Main.gOver) {
                Direction = 'a';
                Trans('a');
            } else {
                //UFFA!

                if (Map.maze[pathy][pathx - 1] != '1' && !Main.gOver) {
                    Direction = 'a';
                    Trans('a');
                }
            }
        }catch(Exception e){Trans('a');}
    }

    public void MoveUp(){
        if(Map.maze[pathy-1][pathx]!='1'&&!Main.gOver){
            Direction='w';
            Trans('w');
        }
    }

    public void MoveDw(){
        if(Map.maze[pathy+1][pathx]!='1'&&!Main.gOver){
            Direction='s';
            Trans('s');
        }
    }

    public void run(){
        for(;;) {
            //Verifico che tutti i fantasmi siano pronti
            for(int i=0;i<Main.Ngiocatori;){
                if(Main.ne[i].ready){
                    i++;
                }
                try {
                    Thread.sleep(20);
                }catch (Exception e){}
            }
            //Tutti i fantasmi sono pronti, li libero dal loop
            for(int i=0;i<Main.Ngiocatori;i++){
                Main.ne[i].start=true;
                Main.ne[i].ready=false;
            }
            //Quando esco dal ciclo, faccio partire tutti i nemici
            Restart=true;
            try{
                //Aspetto 30 ms (Tempo massimo di sincronizzazione impostato su Ne)
                Thread.sleep(30);
            }catch (Exception e){}
            Restart=false;
            Main.gOver=false;
            Pac=pac[0];
            Life();
            //Quando il pg muore o la partita si resetta, chiamo una funzione per resettare i valori e l'altra per dare
            //vita al PG
        }
    }

    public void Life(){
        while(true){
            if(Main.gOver){
                for(int i=0;i<13;i++){
                    Pac=death[i];
                    try{Thread.sleep(75);}catch(Exception e){}
                }
                Pac=pac[9];
                break;
            }
            if (Main.stop){
                return;
            }
            //Se sono ad un incrocio, aggiorno nodex e nodey
            if(Map.maze[pathy][pathx]=='2'||Map.maze[pathy][pathx]=='3'){
                nodex=(char)pathx;
                nodey=(char)pathy;
            }
            switch(Main.cdir){
                case 'a':{
                    MoveSx();
                    if(Direction!='a')
                        Main.cdir=Direction;}
                break;
                case 'd':{
                    MoveDx();
                    if(Direction!='d')
                        Main.cdir=Direction;}break;
                case 's':{
                    MoveDw();

                    if(Direction!='s')
                        Main.cdir=Direction;}break;
                case 'w':{
                    MoveUp();
                    if(Direction!='w')
                        Main.cdir=Direction;}break;
            }
            Score();
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

    //Aumenta i pixel di un quadrato di array grafico(la grandezza di uno spostamento reale) per creare una transizione
    public void Trans(char dir){
        int v=8; //8-default
        if(dir=='w'||dir=='s'){
            if(dir=='w'){
                for(tY=0;Math.abs(tY)!=(int)Main.dY;tY--){
                    aSprite(dir);
                    //Se ricevo un cambio direzione, me ne frego dell'animazione solo se posso farlo
                    //TO FIX
                    if(Main.cdir!=dir){
                        if(ControlloDir(Main.cdir)){
                            while(tY!=0){
                                aSprite('s');
                                tY++;
                                //Se muoio, termino l'animazione
                                if(Main.gOver){
                                    return ;
                                }
                                try{Thread.sleep(v);}catch(Exception e){}
                            }
                            return ;
                        }}
                    //Se muoio, termino l'animazione
                    if(Main.gOver){
                        return ;
                    }
                    try{Thread.sleep(v);}catch(Exception e){}
                }
                tY=0;
                pathy--;}
            else{
                for(tY=0;Math.abs(tY)!=(int)Main.dY;tY++){
                    aSprite(dir);
                    //Se ricevo un cambio direzione, me ne frego dell'animazione solo se posso farlo
                    //TO FIX
                    if(Main.cdir!=dir){
                        if(ControlloDir(Main.cdir)){
                            while(tY!=0){
                                aSprite('w');
                                tY--;
                                //Se muoio, termino l'animazione
                                if(Main.gOver){
                                    return ;
                                }
                                try{Thread.sleep(v);}catch(Exception e){}
                            }
                            return ;
                        }
                        }
                    if(Main.gOver){
                        return ;
                    }
                    try{Thread.sleep(v);}catch(Exception e){}
                }
                tY=0;
                pathy++;
            }
        }

        else {
            if(dir=='a'){
                for(tX=0;Math.abs(tX)!=(int)Main.dX;tX--){
                    aSprite(dir);
                    //Se ricevo un cambio direzione, me ne frego dell'animazione solo se posso farlo
                    //TO FIX
                    if(Main.cdir!=dir) {
                        if (ControlloDir(Main.cdir)) {
                            while (tX != 0) {
                                aSprite('d');
                                tX++;
                                //Se muoio, termino l'animazione
                                if(Main.gOver){
                                    return ;
                                }
                                try {
                                    Thread.sleep(v);
                                } catch (Exception e) {
                                }
                            }
                            return;
                        }
                    }
                    if(Main.gOver){
                        return ;
                    }
                    try{Thread.sleep(v);}catch(Exception e){}
                }
                tX=0;
                if((pathx-1)!=-1)
                    pathx--;
                else
                    pathx=27;
            }
            else{
                for(tX=0;Math.abs(tX)!=(int)Main.dX;tX++){
                    aSprite(dir);
                    //Se ricevo un cambio direzione, me ne frego dell'animazione solo se posso farlo
                    //TO FIX
                    if(Main.cdir!=dir){
                        if(ControlloDir(Main.cdir))
                            //Se cambio direzione faccio un'animazione inversa per poi fare l'animazione giusta
                        {
                        while(tX!=0){
                            aSprite('a');
                            tX--;
                            //Se muoio, termino l'animazione
                            if(Main.gOver){
                                return ;
                            }
                            try{Thread.sleep(v);}catch(Exception e){}
                        }
                            return;
                        }}
                    if(Main.gOver){
                        return ;
                    }
                    try{Thread.sleep(v);}catch(Exception e){}
                }
                tX=0;
                if((pathx+1)!=28)
                    pathx++;
                else
                    pathx=0;
            }
        }




    }

    //Alternate sprite
    public void aSprite(char dir){
        switch(dir){
            case 'd':{if(c){
                Pac=pac[1];
                c=!c;
            }else{
                Pac=pac[2];
                c=!c;
            }}break;
            case 'a':{if(c){
                Pac=pac[3];
                c=!c;
            }else{
                Pac=pac[4];
                c=!c;
            }}break;
            case 'w':{if(c){
                Pac=pac[5];
                c=!c;
            }else{
                Pac=pac[6];
                c=!c;
            }}break;
            case 's':{if(c){
                Pac=pac[7];
                c=!c;
            }else{
                Pac=pac[8];
                c=!c;
            }}break;
        }
    }


    public void Score(){

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

    public Boolean ControlloDir(char dir){
        switch(dir){
            case 'w': {
                if(Map.maze[pathy-1][pathx]!='1'&&!Main.gOver){
                    return true;
                }
            }break;
            case 'a': {
                if(Map.maze[pathy][pathx-1]!='1'&&!Main.gOver){
                    return true;
                }
            }break;
            case 's': {
                if(Map.maze[pathy+1][pathx]!='1'&&!Main.gOver){
                    return true;
                }
            }break;
            case 'd': {
                if (Map.maze[pathy][pathx + 1] != '1' && !Main.gOver) {
                    return true;
                }
            }
        }
        return false;
    }





}