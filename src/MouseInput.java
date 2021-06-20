import java.awt.event.*;

public class MouseInput implements MouseListener, MouseMotionListener {
    int buttonsMinArea = Main.startx+(Main.dX*9);
    int buttonsMaxArea = (buttonsMinArea)+Main.f.xDimButton;
    int singleMinArea = Main.f.yPos;
    int singleMaxArea = (singleMinArea)+Main.f.yDimButton;
    int multiMinArea = Main.f.yPos+(Main.f.yDimButton)+(Main.f.yDimButton/3);
    int multiMaxArea = (multiMinArea)+Main.f.yDimButton;
    int optionsMinArea = Main.f.yPos+(Main.f.yDimButton*2)+(Main.f.yDimButton/3)*2;
    int optionsMaxArea = (optionsMinArea)+Main.f.yDimButton;
    int exitMinArea = Main.f.yPos+(Main.f.yDimButton*3)+(Main.f.yDimButton/3)*3;
    int exitMaxArea = (exitMinArea)+Main.f.yDimButton;
    public MouseInput(){

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        //Menu
        if(Main.stateOfGame==0) {
            if (x > buttonsMinArea && x < buttonsMaxArea) {
                //Se ho cliccato nell'area dei pulsanti
                //Ora controllo se ho cliccato un pulsante e mi comporto di conseguenza
                if (y > singleMinArea && y < singleMaxArea) {
                    Main.stateOfGame = 1;
                } else {
                    if (y > multiMinArea && y < multiMaxArea) {
                        Main.stateOfGame = 2;
                    } else {
                        if (y > optionsMinArea && y < optionsMaxArea) {
                            Main.stateOfGame = 3;
                        } else {
                            if (y > exitMinArea && y < exitMaxArea) {
                                //Esco dal gioco
                                System.exit(0);
                            }
                        }
                    }
                }
            }
        }
        else {
            //Options
            if (Main.stateOfGame == 3) {
                if (x > buttonsMinArea && x < buttonsMaxArea) {
                    //Se ho cliccato nell'area dei pulsanti
                    //Ora controllo se ho cliccato un pulsante e mi comporto di conseguenza
                    if (y > singleMinArea && y < singleMaxArea) {
                        //Zona difficoltà
                        switch (Main.difficulty){
                            case "easy":{Main.difficulty="medium";}break;
                            case "medium":{Main.difficulty="hard";}break;
                            case "hard":{Main.difficulty="easy";}break;
                        }
                    } else {
                        if (y > multiMinArea && y < multiMaxArea) {
                            //Zona audio
                            if(Main.volume=="false"){
                                Main.volume="true";
                            }else{
                                Main.volume="false";
                            }
                        } else {
                            if (y > exitMinArea && y < exitMaxArea) {
                                //Torno indietro
                                Main.stateOfGame=0;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        if(Main.stateOfGame==0) {
            if (x > buttonsMinArea && x < buttonsMaxArea) {
                //Se ho cliccato nell'area dei pulsanti
                //Ora controllo se ho cliccato un pulsante e mi comporto di conseguenza
                if (y > singleMinArea && y < singleMaxArea) {
                    Frame.timer.start();
                    Frame.timer.setActionCommand("Single");
                } else {
                    if (y > multiMinArea && y < multiMaxArea) {
                        Frame.timer.start();
                        Frame.timer.setActionCommand("Multi");
                    } else {
                        if (y > optionsMinArea && y < optionsMaxArea) {
                            Frame.timer.start();
                            Frame.timer.setActionCommand("Options");
                        } else {
                            if (y > exitMinArea && y < exitMaxArea) {
                                Frame.timer.start();
                                Frame.timer.setActionCommand("Exit");
                            } else {
                                Frame.timer.start();
                                Frame.timer.setActionCommand("Stop");
                            }
                        }
                    }
                }
            } else {
                Frame.timer.start();
                Frame.timer.setActionCommand("Stop");
            }
        }
        else{
            if(Main.stateOfGame==3){
                if (x > buttonsMinArea && x < buttonsMaxArea) {
                    //Se ho cliccato nell'area dei pulsanti
                    //Ora controllo se ho cliccato un pulsante e mi comporto di conseguenza
                    if (y > singleMinArea && y < singleMaxArea) {
                        Frame.timer.start();
                        Frame.timer.setActionCommand("Difficulty");
                    } else {
                        if (y > multiMinArea && y < multiMaxArea) {
                            Frame.timer.start();
                            Frame.timer.setActionCommand("Audio");
                        } else {
                            if (y > exitMinArea && y < exitMaxArea) {
                                Frame.timer.start();
                                Frame.timer.setActionCommand("Back");
                            } else {
                                Frame.timer.start();
                                Frame.timer.setActionCommand("Stop");
                            }

                        }
                    }
                } else {
                    Frame.timer.start();
                    Frame.timer.setActionCommand("Stop");
                }
            }
        }
    }
}
