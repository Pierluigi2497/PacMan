public class Controller {
    public int pathx,pathy;
    public char nodex,nodey;
    public int tX=0;  //Translate x
    public int tY=0;  //Translate y
    public int vel;//Inversa in ms
    public char Direction=' ';//Serve per capire quando cambio direzione
    public char changeDir;//Serve per il listener
    public boolean stop=true;

    //Si deve chiamere solo Move()
    public Controller(int x,int y,int vel){
        this.pathx=x;
        this.pathy=y;
        this.vel=vel;
    }

    //Aumenta i pixel di un quadrato di array grafico(la grandezza di uno spostamento reale) per creare una transizione
    //toSend serve per capire se devo inviare le informazioni al server o no, non le devo mandare quando l'animazione non è del mio personaggio
    public void Trans(char dir,boolean toSend){
        int v=(int)(vel/Main.dY);
        if(dir=='w'||dir=='s'){
            if(dir=='w'){
                for(tY=0;Math.abs(tY)!=(int)Main.dY;tY--){
                    //Invio informazioni server quando mi muovo con l'animazione
                    if(toSend){
                        sendInfo(dir);
                    }
                    //Se ricevo un cambio direzione, me ne frego dell'animazione solo se posso farlo
                    if(changeDir!=dir){
                        if(ControlloDir(changeDir)){
                            Direction=changeDir;
                            while(tY!=0){
                                tY++;
                                if(Main.pause){
                                    pauseUntillAudio();
                                }
                                //Se muoio, termino l'animazione
                                if(Main.gOver){
                                    return ;
                                }
                                //Invio informazioni server quando mi muovo con l'animazione
                                if(toSend){
                                    sendInfo(dir);
                                }

                                try{Thread.sleep(v);}catch(Exception e){}
                            }
                            return ;
                        }}
                    if(Main.pause){
                        pauseUntillAudio();
                    }
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
                    //Invio informazioni server quando mi muovo con l'animazione
                    if(toSend){
                        sendInfo(dir);
                    }

                    if(changeDir!=dir){
                        if(ControlloDir(changeDir)){
                            Direction=changeDir;
                            while(tY!=0){
                                //aSprite('w');
                                tY--;
                                if(Main.pause){
                                    pauseUntillAudio();
                                }
                                //Se muoio, termino l'animazione
                                if(Main.gOver){
                                    return ;
                                }

                                //Invio informazioni server quando mi muovo con l'animazione
                                if(toSend){
                                    sendInfo(dir);
                                }
                                try{Thread.sleep(v);}catch(Exception e){}
                            }
                            return ;
                        }
                    }
                    if(Main.pause){
                        pauseUntillAudio();
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

                    //Invio informazioni server quando mi muovo con l'animazione
                    if(toSend){
                        sendInfo(dir);
                    }
                    if(changeDir!=dir) {
                        if (ControlloDir(changeDir)) {
                            Direction=changeDir;
                            while (tX != 0) {
                                tX++;
                                if(Main.pause){
                                    pauseUntillAudio();
                                }
                                //Se muoio, termino l'animazione
                                if(Main.gOver){
                                    return ;
                                }

                                //Invio informazioni server quando mi muovo con l'animazione
                                if(toSend){
                                    sendInfo(dir);
                                }
                                try {
                                    Thread.sleep(v);
                                } catch (Exception e) {
                                }
                            }
                            return;
                        }
                    }
                    if(Main.pause){
                        pauseUntillAudio();
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

                    //Invio informazioni server quando mi muovo con l'animazione
                    if(toSend){
                        sendInfo(dir);
                    }
                    if(changeDir!=dir){
                        if(ControlloDir(changeDir))
                        //Se cambio direzione faccio un'animazione inversa per poi fare l'animazione giusta
                        {
                            Direction=changeDir;
                            while(tX!=0){
                                tX--;
                                if(Main.pause){
                                    pauseUntillAudio();
                                }
                                //Se muoio, termino l'animazione
                                if(Main.gOver){
                                    return ;
                                }
                                //Invio informazioni server quando mi muovo con l'animazione
                                if(toSend){
                                    sendInfo(dir);
                                }
                                try {
                                    Thread.sleep(v);
                                } catch (Exception e) {
                                }
                            }
                            return;
                        }}
                    if(Main.pause){
                        pauseUntillAudio();
                    }
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

    public void MoveDw(boolean toSend){
        if(Map.maze[pathy+1][pathx]!='1'&&!Main.gOver){
            Direction='s';
            stop=false;
            //Invio informazioni al server
            //Ed invio l'informazione al server
            if(toSend) {
                try {
                    Main.server.sendDir(Main.server.getID() + ":" +
                            's' + "," +
                            pathx + "," +
                            pathy + "," +
                            tX + "," +
                            tY + "," +
                            stop + "," +
                            vel);
                } catch (Exception e) {
                }
            }
            Trans('s',toSend);
        }else{
            //Ed invio l'informazione al server
            if(stop==false) {
                stop=true;
                if(toSend) {
                    try {
                        Main.server.sendDir(Main.server.getID() + ":" +
                                's' + "," +
                                pathx + "," +
                                pathy + "," +
                                tX + "," +
                                tY + "," +
                                stop + "," +
                                vel);
                    } catch (Exception e) {
                    }
                }
            }
            //Se trovo un muro fermo l'animazione
        }
    }

    public void MoveUp(boolean toSend){
        if(Map.maze[pathy-1][pathx]!='1'&&!Main.gOver){
            Direction='w';
            stop=false;
            //Ed invio l'informazione al server
            if(toSend) {
                try {
                    Main.server.sendDir(Main.server.getID() + ":" +
                            'w' + "," +
                            pathx + "," +
                            pathy + "," +
                            tX + "," +
                            tY + "," +
                            stop + "," +
                            vel);
                } catch (Exception e) {
                }
            }
            Trans('w',toSend);
        }else{
            //Ed invio l'informazione al server
            if(stop==false) {
                stop=true;
                if(toSend) {
                    try {
                        Main.server.sendDir(Main.server.getID() + ":" +
                                'w' + "," +
                                pathx + "," +
                                pathy + "," +
                                tX + "," +
                                tY + "," +
                                stop + "," +
                                vel);
                    } catch (Exception e) {
                    }
                }
            }
            //Se trovo un muro fermo l'animazione
        }
    }

    public void MoveSx(boolean toSend){
        try {
            //PRIMA FUNZIONAVA
            //Se mi trovo all'inizio della mappa(per l'asse x)
            //Per evitare IndexOut
            //Sono sicuro che posso andare a destra o a sinistra(Unica possibilità per i tunnel)
            //Quindi salto il primo controllo
            if (pathx == 0 && !Main.gOver) {
                Direction = 'a';
                Trans('a',toSend);
            } else {
                if (Map.maze[pathy][pathx - 1] != '1' && !Main.gOver) {
                    Direction = 'a';
                    stop=false;
                    //Ed invio l'informazione al server
                    if(toSend) {
                        try {
                            Main.server.sendDir(Main.server.getID() + ":" +
                                    'a' + "," +
                                    pathx + "," +
                                    pathy + "," +
                                    tX + "," +
                                    tY + "," +
                                    stop + "," +
                                    vel);
                        } catch (Exception e) {
                        }
                    }
                    Trans('a',toSend);
                }else {
                    //Se trovo un muro fermo l'animazione
                    //Ed invio l'informazione al server
                    if(stop==false) {
                        stop=true;
                        if(toSend) {
                            try {
                                Main.server.sendDir(Main.server.getID() + ":" +
                                        'a' + "," +
                                        pathx + "," +
                                        pathy + "," +
                                        tX + "," +
                                        tY + "," +
                                        stop + "," +
                                        vel);
                            } catch (Exception e) {
                            }
                        }
                    }
                    //Se trovo un muro fermo l'animazione

                }
            }
        }catch(Exception e){Trans('a',toSend);}
    }

    public void MoveDx(boolean toSend){
        try{
            //Se mi trovo all'inizio della mappa(per l'asse x)
            //Per evitare IndexOut
            //Sono sicuro che posso andare a destra o a sinistra(Unica possibilità per i tunnel)
            //Quindi salto il primo controllo
            if(pathx==27&&!Main.gOver){
                Direction='d';
                Trans('d',toSend);
            }else{
                if(Map.maze[pathy][pathx+1]!='1'&&!Main.gOver){
                    Direction='d';
                    stop=false;
                    if(toSend) {
                        //Ed invio l'informazione al server
                        try {
                            Main.server.sendDir(Main.server.getID() + ":" +
                                    'd' + "," +
                                    pathx + "," +
                                    pathy + "," +
                                    tX + "," +
                                    tY + "," +
                                    stop + "," +
                                    vel);
                        } catch (Exception e) {
                        }
                    }
                    Trans('d',toSend);
                }else{
                    //Ed invio l'informazione al server
                    if(stop==false) {
                        stop=true;
                        if(toSend) {
                            try {
                                Main.server.sendDir(Main.server.getID() + ":" +
                                        'd' + "," +
                                        pathx + "," +
                                        pathy + "," +
                                        tX + "," +
                                        tY + "," +
                                        stop + "," +
                                        vel);
                            } catch (Exception e) {
                            }
                        }
                    }
                    //Se trovo un muro fermo l'animazione

                }
            }
        }
        catch(Exception e){Trans('d',toSend);}
    }

    public int getPathX(){
        return pathx;
    }

    public int getPathY(){
        return pathy;
    }

    public int getTX(){
        return tX;
    }

    public int getTY(){
        return tY;
    }

    public void sendInfo(char dir){
        try{Main.server.sendDir(Main.server.getID() +":"+
                dir+","+
                pathx+","+
                pathy+","+
                tX+","+
                tY);}catch(Exception e){}
    }

    public void pauseUntillAudio(){
        while(true){
            if(!Audio.ghostClip.isActive()){
                Main.pause=false;
                break;
            }
            try{
                Thread.sleep(20);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
