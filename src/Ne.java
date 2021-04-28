import java.awt.image.BufferedImage;
public class Ne implements Runnable {
    public int pathx,pathy;
    public int bornx,borny; //Queste variabili serviranno per rigenerare il thread
    private int vel=250;
    public int tX=0;  //Translate x
    public int tY=0;  //Translate y
    public BufferedImage[] i=new BufferedImage[13];
    public BufferedImage n;
    private Boolean c=true;
    private boolean nuovo,uscito;
    private String color;
    private char ldir;	//last direction (l'ultima rotta che ha preso il png)
    //Serve per capire quando il fantasma è pronto per rigenerarsi
    //Serve alla class Pg
    public boolean ready=true;
    //variabile gestita da Pg per rigenerare Ne
    public boolean start=true;

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
        //n=i[0];
    }

    public void Life(){
        this.start=false;
        //AZIONI PRELIMINARI
        if(nuovo){										//aggiungere nella mappa posizioni particolari "nodi/incroci" dove i nemici possono decidere di girare
            for(;uscito!=true;){corri(esci());}
            Map.maze[13][12]=Map.maze[14][12]=1;
            nuovo=false;}
        //corri(cieco());									//fa un ciclo di cieco per trovare la direzione in cui muoversi e andare in quella
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

            if(Map.maze[pathx][pathy]==2||Map.maze[pathx][pathy]==3){ //SE INCONTRA UNNODO
                if(radar()&&Main.Eat==0){
                    corri(Follow(Main.pg.pathx,Main.pg.pathy));
                }
                else{
                    corri(cieco());
                }
            }

            if(radar()&&Main.Eat==0){
                corri(Follow(Main.pg.pathx,Main.pg.pathy));
            }
            else{
                corri(cieco());

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
            Map.maze[13][12]=Map.maze[14][12]=3;
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
            Life();
          }
        }
    }

    public char Follow(int x,int y) {
        try{
            if(Map.maze[pathx+1][pathy]!=1&&pathx<x){
                return('d');
            }}catch(Exception e){return 'd';}
        try{
            if(Map.maze[pathx-1][pathy]!=1&&pathx>x){
                return('a');
            }}catch(Exception e){return 'a';}
        if(Map.maze[pathx][pathy+1]!=1&&pathy<y){
            return('s');
        }
        if(Map.maze[pathx][pathy-1]!=1&&pathy>y){
            return('w');
        }
        {return cieco();}
    }


    public char esci() {
        if(pathy==11){uscito=true;return 'o';}


        if(Map.maze[pathx][pathy]==3){
            return('w');}

        else{
            if(Map.maze[pathx][pathy-1]!=1&&pathy>11){
                return('w');}
            else
            if(Map.maze[pathx][pathy+1]!=1&&pathy<11){
                return('s');}
            else
            if(Map.maze[pathx+1][pathy]!=1&&pathx<13){
                return('d');}
            else
            if(Map.maze[pathx-1][pathy]!=1&&pathx>13){
                return('a');}
        }
        return 'o';
    }


    public char cieco(){							//funzione che trova una direzione da prendere per il png in maniera casuale
        int d;
        d=(int)(Math.random()*10);
        d++;					//d=decisione, numero casuale che determinerà la direzione del nemico in base a questi criteri:
        if((d%2)==0){								//se pari si muoverà su asse x
            d=(int)(Math.random()*10);
            d++;									//ricalcolo
            if((d%2)==0){
                try{									//se di nuovo pari andrà a destra
                    if(Map.maze[pathx+1][pathy]!=1)return 'd';}catch(Exception e){return 'd';}}
            else
                try{									//se è dispari andrà a sinistra
                    if(Map.maze[pathx-1][pathy]!=1)return'a';}catch(Exception e){return 'a';}}
        else{										//se dispari si muoverà su asse y
            d=(int)(Math.random()*10);
            d++;									//ricalcolo
            if((d%2)==0){							//se pari andrà in su
                if(Map.maze[pathx][pathy-1]!=1)return'w';
            }
            else{									//se dispari andra in giù
                if(Map.maze[pathx][pathy+1]!=1)return's';
            }
        }
        return 'o';
    }

    public boolean radar(){
        int r;
        r=50;			//range

        //Se rientra nel raggio
        if(Math.abs(pathx-Main.pg.pathx)<(r/2)&&Math.abs(pathy-Main.pg.pathy)<(r/2))
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
                while(Map.maze[pathx][pathy-1]!=1){
                    for(tY=0;Math.abs(tY)!=Main.dY;tY--){
                        aSprite(dir);
                        if(Main.gOver)
                            return ;
                        try{Thread.sleep(v);}catch(Exception e){}
                    }
                    tY=0;
                    pathy--;
                    if(Map.maze[pathx][pathy]==2||Map.maze[pathx][pathy]==3)
                        break;}

            }
            else{
                while(Map.maze[pathx][pathy+1]!=1){
                    for(tY=0;Math.abs(tY)!=Main.dY;tY++){
                        aSprite(dir);
                        if(Main.gOver)
                            return ;
                        try{Thread.sleep(v);}catch(Exception e){}
                    }
                    tY=0;
                    pathy++;
                    if(Map.maze[pathx][pathy]==2||Map.maze[pathx][pathy]==3)
                        break;
                }

            }
        }
        else {
            v=vel/Main.dX;
            if(dir=='a'){
                try{
                    while(Map.maze[pathx-1][pathy]!=1){
                        for(tX=0;Math.abs(tX)!=Main.dX;tX--){
                            aSprite(dir);
                            if(Main.gOver)
                                return ;
                            try{Thread.sleep(v);}catch(Exception e){}
                        }
                        tX=0;
                        pathx--;
                        if(Map.maze[pathx][pathy]==2||Map.maze[pathx][pathy]==3)
                            break;
                    }
                }catch(Exception e){
                    //Sono qui perchè sono uscito dalla mappa, se ho preso un tunnel, mi teletrasporto altrimento ti
                    //banno perchè usi i cheat
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
                    while(Map.maze[pathx+1][pathy]!=1){
                        for(tX=0;Math.abs(tX)!=Main.dX;tX++){
                            aSprite(dir);
                            if(Main.gOver)
                                return ;
                            try{Thread.sleep(v);}catch(Exception e){}
                        }
                        tX=0;
                        pathx++;
                        if(Map.maze[pathx][pathy]==2||Map.maze[pathx][pathy]==3)
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
        //e=eatable Mangiabile		q=Quasi Mangiabile

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
        else if(Pulse.situation==1){
            blueSprite();
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

    public void Fuga(){
        while(((pathx!=13)||(pathx!=14))&&(pathy!=13))
            Follow(13,13);
        /*
        if(color=="red"){
            i[0]=Main.img.getSubimage(4,65,14,14);
            i[1]=Main.img.getSubimage(20,65,14,14);
            i[2]=Main.img.getSubimage(36,65,14,14);
            i[3]=Main.img.getSubimage(52,65,14,14);
            i[4]=Main.img.getSubimage(68,65,14,14);
            i[5]=Main.img.getSubimage(84,65,14,14);
            i[6]=Main.img.getSubimage(100,65,14,14);
            i[7]=Main.img.getSubimage(116,65,14,14);
        }
        else if(color=="pink"){
            i[0]=Main.img.getSubimage(4,81,14,14);
            i[1]=Main.img.getSubimage(20,81,14,14);
            i[2]=Main.img.getSubimage(36,81,14,14);
            i[3]=Main.img.getSubimage(52,81,14,14);
            i[4]=Main.img.getSubimage(68,81,14,14);
            i[5]=Main.img.getSubimage(84,81,14,14);
            i[6]=Main.img.getSubimage(100,81,14,14);
            i[7]=Main.img.getSubimage(116,81,14,14);
        }
        else if(color=="blue"){
            i[0]=Main.img.getSubimage(4,97,14,14);
            i[1]=Main.img.getSubimage(20,97,14,14);
            i[2]=Main.img.getSubimage(36,97,14,14);
            i[3]=Main.img.getSubimage(52,97,14,14);
            i[4]=Main.img.getSubimage(68,97,14,14);
            i[5]=Main.img.getSubimage(84,97,14,14);
            i[6]=Main.img.getSubimage(100,97,14,14);
            i[7]=Main.img.getSubimage(116,97,14,14);
        }
        else if(color=="yellow"){
            i[0]=Main.img.getSubimage(4,113,14,14);
            i[1]=Main.img.getSubimage(20,113,14,14);
            i[2]=Main.img.getSubimage(36,113,14,14);
            i[3]=Main.img.getSubimage(52,113,14,14);
            i[4]=Main.img.getSubimage(68,113,14,14);
            i[5]=Main.img.getSubimage(84,113,14,14);
            i[6]=Main.img.getSubimage(100,113,14,14);
            i[7]=Main.img.getSubimage(116,113,14,14);
        }*/
    }

}



