import java.io.File;
import javax.sound.sampled.*;
import java.time.Clock;

public class Audio implements Runnable{

    //Variabile che serve per capire quando smettere di far suonare chompClip
    static boolean stopDots=true;

    private Clock clock=Clock.systemDefaultZone();
    static boolean eatedGhost=false;
    private double timerBeginning=0;//File lenght=4126 ms
    File beginningFile= new File("res/game_start.wav");
    AudioInputStream beginning;
    static Clip beginningClip;

    private double timerChomp=0;//File lenght=250 ~ ms
    File dotEatFile= new File("res/munch.wav");
    AudioInputStream chomp;
    static Clip chompClip;

    private static double timerEat=0;//File lenght=573 ms
    File ghostEatFile= new File("res/eat_ghost.wav");
    AudioInputStream ghost;
    static Clip ghostClip;

    private double timerDeath=0;//File lenght=1534 ms
    File deathFile= new File("res/death.wav");
    AudioInputStream death;
    static Clip deathClip;

    File sirenFile= new File("res/siren_1.wav");
    AudioInputStream siren;
    static Clip sirenClip;

    File eatedFile= new File("res/power_pellet.wav");
    AudioInputStream eated;
    static Clip eatedClip;

    File eatableFile= new File("res/retreating.wav");
    AudioInputStream eatable;
    static Clip eatableClip;

    //variabile tmp
    private double tmp=0;
    static int dots=Main.dots;

    AudioFormat format;
    DataLine.Info info;

    public void run(){
        //Ho mangiato una qualunque pallina, avvio il suono
        //Aspetto che il giocatore esca dal menu
        while(Main.stateOfGame==0||Main.stateOfGame==3){
            try{
                Thread.sleep(10);
            }catch (Exception e){}
        }
        while(true) {
            if(!Main.startGame){
                timerBeginning= clock.millis();
                beginningClip.start();
            }
            if(!beginningClip.isActive()&&!deathClip.isActive()&&Main.startGame){
                sirenClip.loop(Clip.LOOP_CONTINUOUSLY);
            }else{
                sirenClip.stop();
            }
            if ((dots-Main.dots>=1) && timerChomp == 0 && !stopDots) {
                dots=Main.dots;
                timerChomp = clock.millis();
                chompClip.start();
            }
            if (Main.gOver) {
                timerDeath = clock.millis();
                deathClip.start();
            }
            if(eatedGhost){
                timerEat= clock.millis();
                ghostClip.start();
                eatedGhost=false;
            }
            checkAudio();
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
    }

    public Audio(){
        try{beginning= AudioSystem.getAudioInputStream(beginningFile);}
        catch(Exception e){System.out.println("Errore apertura audio");}
        format = beginning.getFormat();
        info = new DataLine.Info(Clip.class, format);
        try {
            beginningClip = (Clip) AudioSystem.getLine(info);
            beginningClip.open(beginning);
        }catch (Exception e){System.out.println("Errore riproduzione");}

        try{chomp= AudioSystem.getAudioInputStream(dotEatFile);}
        catch(Exception e){System.out.println("Errore apertura audio");}
        format = chomp.getFormat();
        info = new DataLine.Info(Clip.class, format);
        try {
            chompClip = (Clip) AudioSystem.getLine(info);
            chompClip.open(chomp);
        }catch (Exception e){System.out.println("Errore riproduzione");}

        try{ghost= AudioSystem.getAudioInputStream(ghostEatFile);}
        catch(Exception e){System.out.println("Errore apertura audio");}
        format = ghost.getFormat();
        info = new DataLine.Info(Clip.class, format);
        try {
            ghostClip = (Clip) AudioSystem.getLine(info);
            ghostClip.open(ghost);
        }catch (Exception e){System.out.println("Errore riproduzione");}

        try{death= AudioSystem.getAudioInputStream(deathFile);}
        catch(Exception e){System.out.println("Errore apertura audio");}
        format = death.getFormat();
        info = new DataLine.Info(Clip.class, format);
        try {
            deathClip = (Clip) AudioSystem.getLine(info);
            deathClip.open(death);
        }catch (Exception e){System.out.println("Errore riproduzione");}

        try{siren= AudioSystem.getAudioInputStream(sirenFile);}
        catch(Exception e){e.printStackTrace();System.out.println("Errore apertura audio");}
        format = siren.getFormat();
        info = new DataLine.Info(Clip.class, format);
        try {
            sirenClip = (Clip) AudioSystem.getLine(info);
            sirenClip.open(siren);
        }catch (Exception e){System.out.println("Errore riproduzione");}

        try{eatable= AudioSystem.getAudioInputStream(eatableFile);}
        catch(Exception e){System.out.println("Errore apertura audio");}
        format = eatable.getFormat();
        info = new DataLine.Info(Clip.class, format);
        try {
            eatableClip = (Clip) AudioSystem.getLine(info);
            eatableClip.open(eatable);
        }catch (Exception e){System.out.println("Errore riproduzione");}

        try{eated= AudioSystem.getAudioInputStream(eatedFile);}
        catch(Exception e){System.out.println("Errore apertura audio");}
        format = eated.getFormat();
        info = new DataLine.Info(Clip.class, format);
        try {
            eatedClip = (Clip) AudioSystem.getLine(info);
            eatedClip.open(eated);
        }catch (Exception e){System.out.println("Errore riproduzione");}
    }

    private void checkAudio(){
        if(timerDeath!=0){
            if(clock.millis()-timerDeath>1534){
                deathClip.stop();
                deathClip.setMicrosecondPosition(0);
                timerDeath=0;
            }
        }
        if(timerChomp!=0){
            if(clock.millis()-timerChomp>250){
                chompClip.stop();
                chompClip.setMicrosecondPosition(0);
/*                chompClip1.stop();
                chompClip1.setMicrosecondPosition(0);*/
                timerChomp=0;
                tmp= clock.millis();
            }
        }
        if(timerBeginning!=0){
            if(clock.millis()-timerBeginning>4126){
                beginningClip.stop();
                beginningClip.setMicrosecondPosition(0);
                timerBeginning=0;
            }
        }
        if(timerEat!=0){
            if(clock.millis()-timerEat>573){
                ghostClip.stop();
                ghostClip.setMicrosecondPosition(0);
                timerEat=0;
            }
        }
    }

    public static void playEated(){
        if(!eatedClip.isActive()){
            eatedClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public static void stopPlayEated(){
        boolean stop=true;
        for(int i=0;i<Main.Ngiocatori;i++){
            if(Main.ne[i].eated){
                stop=false;
                break;
            }
        }
        if(stop){
            eatedClip.stop();
            eatedClip.setMicrosecondPosition(0);
        }
    }

    public static void playEatable(){
        if(!eatableClip.isActive()){
            eatableClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public static void stopPlayEatable(){
        eatableClip.stop();
        eatableClip.setMicrosecondPosition(0);
    }

    public static double getEatTime(){
        return timerEat;
    }

    public static void stopAllSounds(){
                deathClip.stop();


                chompClip.stop();

                beginningClip.stop();


                ghostClip.stop();

                eatedClip.stop();

                eatableClip.stop();

                sirenClip.stop();

    }


}
