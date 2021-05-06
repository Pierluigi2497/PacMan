import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.*;

public class Frame extends JPanel implements Runnable {
    public JFrame f=new JFrame();
    private BufferedImage dot;
    static BufferedImage Dot;
    private int i;//Controllo per score
    private int l;//Controllo per invertire score

    public Frame(){
        setBackground(Color.BLACK);
        repaint();
    /*  f.setResizable(false);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setUndecorated(true);
        f.setVisible(true);
        this.setSize(644,742);
        f.setSize(644,742);
    */}

    public void run(){
        dot=Main.img.getSubimage(137,33,12,12);
        Dot=Main.img.getSubimage(152,31,16,16);
        f.setTitle("Pacman");
        f.setIconImage(Main.img.getSubimage(20,1,13,13));
        for(;;){
            Main.dots=0;
            repaint();
            try{
                //33=30 FPS
                //16=60 FPS
                Thread.sleep(16);
            } catch(Exception e) {}
            if(Main.stop)
                break;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        //622 742
        drawMap(g);
        //Per disegnare al centro(di ogno quadrato corrispettivo), devo aggiundere la metà di dX e dY ad ogni print
        drawDots(g);
        drawGhosts(g);
        drawPacman(g);
        drawScore(g);
        drawGameOver(g);
    }

    public void drawMap(Graphics g){
        //Stampa la mappa a partire dalla posizione 0,0 con una grandezza calcolata da (grandezza quadratino matrice * grandezza matrice)
        //Ho tolto dei pixel perchè, per qualche strano motivo, la mappa veniva tagliata
        //g.drawImage(Main.map,0,0,(Main.dX*Map.x),(Main.dY*Map.y),null);
        g.drawImage(Main.map,Main.startx,Main.starty,Main.trueWidth,Main.trueHeight,null);
    }

    public void drawDots(Graphics g){
        for(int i=0;i<Map.y;i++)
            for(int l=0;l<Map.x;l++)
                if(Map.maze[i][l]=='2'||Map.maze[i][l]=='4') {
                    Main.dots++;
                    g.drawOval(Main.startx+(int)(Main.dX*l)+(Main.dX/2)-2,Main.starty+(int)(Main.dY*i)+(Main.dY/2)-2,4,4);
                    g.fillOval(Main.startx+(int)(Main.dX*l)+(Main.dX/2)-2,Main.starty+(int)(Main.dY*i)+(Main.dY/2)-2,4,4);
                    // g.drawImage(dot,Main.startx+(Main.dX*l),Main.dY*i,Main.dX,Main.dY,null);
                }
                else if(Map.maze[i][l]=='5'){
                    Main.dots++;
                    g.drawOval(Main.startx+(int)(Main.dX*l)+(Main.dX/2)-5,Main.starty+(int)Main.dY*i+(Main.dY/2)-5,10,10);
                    g.fillOval(Main.startx+(int)(Main.dX*l)+(Main.dX/2)-5,Main.starty+(int)Main.dY*i+(Main.dY/2)-5,10,10);
                }
    }

    public void drawGhosts(Graphics g){
        for(int i=0;i<4;i++)
            g.drawImage(Main.ne[i].n,Main.startx+(Main.dX * Main.ne[i].pathx)+Main.ne[i].tX,Main.starty+(Main.dY * Main.ne[i].pathy)+Main.ne[i].tY,Main.dX,Main.dY,null);

    }

    public void drawPacman(Graphics g){
        g.drawImage(Main.pg.Pac,Main.startx+(Main.dX * Main.pg.pathx)+Main.pg.tX,Main.starty+(Main.dY * Main.pg.pathy)+Main.pg.tY,Main.dX,Main.dY,null);
    }

    public void drawScore(Graphics g){
        for(i=3,l=0;i>=0;i--,l++){
            g.drawImage(Main.sf[i],Main.startx+(l*11)+(int)(Main.dX*2)+(Main.dX/2)-2,(int)(Main.trueHeight*0.38),11,10,null);
        }
    }

    public void drawGameOver(Graphics g){
        if(Main.gOver)
            g.drawImage(Main.s[10],(this.getWidth() / 2)-85,(this.getHeight() / 2)-38,169,17,null);
    }
}