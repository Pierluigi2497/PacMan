//IL PACMAN NON E BEN SINCRONIZZATO CON IL REPAINT, A CAUSA DI CIO L'ANIMAZIONE NON E BELLISSIMA
import java.awt.image.BufferedImage;

public class Pg implements Runnable{
    public int pathx,pathy;
    public char nodex,nodey;
    public int tX=0;  //Translate x
    public int tY=0;  //Translate y
    public int vel=200;//Inversa in ms
    public BufferedImage[] death=new BufferedImage[13];//1 immagine trasparente
    public BufferedImage[] pac=new BufferedImage[10];
    public BufferedImage Pac;
    private Boolean c=true;
    public char Direction=' ';
    //Serve per sincronizzare i nemici ed il Pg
    static Boolean Restart=false;
    //Serve per dire alla classe frame quando il Pacman deve fermare l'animazione e quando no
    //All'inizio l'animazione è ferma, poi si muove finche non trova un muro
    public boolean stop=true;

    public Pg(int x,int y){
        FirstLunch(x,y);
    }

    public void FirstLunch(int x,int y){
        //Resetto la posizione
        pathx=x;
        pathy=y;
        //Resetto ultimo nodo letto
        nodex=6;
        nodey=9;
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
            //Se mi trovo all'inizio della mappa(per l'asse x)
            //Per evitare IndexOut
            //Sono sicuro che posso andare a destra o a sinistra(Unica possibilità per i tunnel)
            //Quindi salto il primo controllo
            if(pathx==27&&!Main.gOver){
                Direction='d';
                Trans('d');
            }else{
                 if(Map.maze[pathy][pathx+1]!='1'&&!Main.gOver){
                     Direction='d';
                     stop=false;
                     Trans('d');
                 }else{
                     //Se trovo un muro fermo l'animazione
                     stop=true;
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
                if (Map.maze[pathy][pathx - 1] != '1' && !Main.gOver) {
                    Direction = 'a';
                    stop=false;
                    Trans('a');
                }else {
                    //Se trovo un muro fermo l'animazione
                    stop=true;
                }
            }
        }catch(Exception e){Trans('a');}
    }

    public void MoveUp(){
        if(Map.maze[pathy-1][pathx]!='1'&&!Main.gOver){
            Direction='w';
            stop=false;
            Trans('w');
        }else{
            //Se trovo un muro fermo l'animazione
            stop=true;
        }
    }

    public void MoveDw(){
        if(Map.maze[pathy+1][pathx]!='1'&&!Main.gOver){
            Direction='s';
            stop=false;
            Trans('s');
        }else{
            //Se trovo un muro fermo l'animazione
            stop=true;
        }
    }

    public void run(){
        for(;;) {
            //Imposto direzione=k (killed)
            Direction='k';
            //In quuesto modo la classe frame sà che non deve far animare il pacman
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
            if(!Audio.beginningClip.isActive()) {
                if (Main.cdir == ' ') {
                    Main.startGame = true;
                }
                if (Main.startGame) {
                    Audio.stopDots = false;
                    Life();
                } else {
                    //Tutti i fantasmi sono pronti, li libero dal loop
                    for (int i = 0; i < Main.Ngiocatori; i++) {
                        Main.ne[i].start = true;
                        Main.ne[i].ready = true;
                    }
                }
            }else{
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

    public void Life(){
        while(true){
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
                break;
            }
            if (Main.stop){
                return;
            }
            //Se sono ad un incrocio, aggiorno nodex e nodey
            if(Map.maze[pathy][pathx]=='2'||Map.maze[pathy][pathx]=='3'||Map.maze[pathy][pathx]=='5'){
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
                    //Se ricevo un cambio direzione, me ne frego dell'animazione solo se posso farlo
                    if(Main.cdir!=dir){
                        if(ControlloDir(Main.cdir)){
                            Direction=Main.cdir;
                            while(tY!=0){
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
                    //Se ricevo un cambio direzione, me ne frego dell'animazione solo se posso farlo
                    if(Main.cdir!=dir){
                        if(ControlloDir(Main.cdir)){
                            Direction=Main.cdir;
                            while(tY!=0){
                                //aSprite('w');
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
                    //Se ricevo un cambio direzione, me ne frego dell'animazione solo se posso farlo
                    if(Main.cdir!=dir) {
                        if (ControlloDir(Main.cdir)) {
                            Direction=Main.cdir;
                            while (tX != 0) {
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
                    //Se ricevo un cambio direzione, me ne frego dell'animazione solo se posso farlo
                    if(Main.cdir!=dir){
                        if(ControlloDir(Main.cdir))
                            //Se cambio direzione faccio un'animazione inversa per poi fare l'animazione giusta
                        {
                            Direction=Main.cdir;
                        while(tX!=0){
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

    public Boolean ControlloDir(char dir){
        switch(dir){
            case 'w': {
                if(Map.maze[pathy-1][pathx]!='1'&&!Main.gOver){
                    return true;
                }
            }break;
            case 'a': {
                //Sono al tunnel? per evitare guai ritorno true
                if(pathx==0)
                    return true;
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
                //Sono al tunnel? per evitare guai ritorno true
                if(pathx==27)
                    return true;
                if (Map.maze[pathy][pathx + 1] != '1' && !Main.gOver) {
                    return true;
                }
            }
        }
        return false;
    }





}