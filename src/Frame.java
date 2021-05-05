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
        f.setResizable(false);

    }

    public void run(){
        dot=Main.img.getSubimage(137,33,12,12);
        Dot=Main.img.getSubimage(152,31,16,16);
        for(;;){
            upD();
            Main.dots=0;
            repaint();
            try{ Thread.sleep(40);} catch(Exception e) {}
            if(Main.stop)
                break;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.setSize(616,713);
        f.setSize(622,742);
        g.drawImage(Main.map,0,0,Main.dX*Map.x,Main.dY*Map.y,null);
        g.drawString("CIAO",0,0);

        //Disegno Palline
        for(int i=0;i<Map.y;i++)
            for(int l=0;l<Map.x;l++)
                if(Map.maze[i][l]=='2'||Map.maze[i][l]=='4') {
                    Main.dots++;
                    g.drawImage(dot,Main.dX*l,Main.dY*i,Main.dX,Main.dY,null);
                }
                else if(Map.maze[i][l]=='5'){
                    Main.dots++;
                    g.drawImage(Dot,Main.dX*l,Main.dY*i,Main.dX,Main.dY,null);
                }

                //Disegno fantasmi
        for(int i=0;i<4;i++)
            g.drawImage(Main.ne[i].n,(Main.dX * Main.ne[i].pathx)+Main.ne[i].tX,(Main.dY * Main.ne[i].pathy)+Main.ne[i].tY,Main.dX,Main.dY,null);

                //Disegno Pacman
        g.drawImage(Main.pg.Pac,(Main.dX * Main.pg.pathx)+Main.pg.tX,(Main.dY * Main.pg.pathy)+Main.pg.tY,Main.dX,Main.dY,null);


        for(i=3,l=0;i>=0;i--,l++){
            g.drawImage(Main.sf[i],6+(l*11),255,11,10,null);
        }
            //Disegno scritta Gameover
        if(Main.gOver)
            g.drawImage(Main.s[10],(this.getWidth() / 2)-85,(this.getHeight() / 2)-38,169,17,null);

    }




    public void upD(){
        Main.dX=(Main.f.getWidth()/Map.x);

        Main.dY=(Main.f.getHeight()/Map.y);
    }
}