import java.io.File;
import javax.sound.sampled.*;
import java.time.Clock;

public class Audio implements Runnable{

    //Variabile che serve per capire quando smettere di far suonare chompClip
    static boolean stopDots=true;

    private Clock clock=Clock.systemDefaultZone();
    static boolean eatedGhost=false;
    private double timerBeginning=0;//File lenght=4126 ms
    File beginningFile= new File("res/beginning.wav");
    AudioInputStream beginning;
    static Clip beginningClip;
    private double timerChomp=0;//File lenght=716 ms
    File dotEatFile= new File("res/chomp.wav");
    AudioInputStream chomp;
    Clip chompClip;
    private double timerEat=0;//File lenght=573 ms
    File ghostEatFile= new File("res/eatghost.wav");
    AudioInputStream ghost;
    Clip ghostClip;
    private double timerDeath=0;//File lenght=1534 ms
    File deathFile= new File("res/death.wav");
    AudioInputStream death;
    Clip deathClip;

    //variabile tmp
    private double tmp=0;
    static int dots=Main.dots;

    AudioFormat format;
    DataLine.Info info;

    public void run(){
        //Ho mangiato una qualunque pallina, avvio il suono
        while(true) {
            if(!Main.startGame){
                timerBeginning= clock.millis();
                beginningClip.start();
            }
            if ((dots-Main.dots>1) && timerChomp == 0 && !stopDots) {
                dots=Main.dots;
                timerChomp = clock.millis();
                chompClip.start();
                System.out.println(clock.millis() - tmp);
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
            if(clock.millis()-timerChomp>716){
                chompClip.stop();
                chompClip.setMicrosecondPosition(0);
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


}
