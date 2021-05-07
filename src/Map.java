import org.jgrapht.*;
import org.jgrapht.graph.*;
import java.io.*;
import java.io.File;
import java.io.FileWriter;

public class Map {
    static int x=28;
    static int y=31;
    public int i,j;
    public static Graph<String,DefaultEdge> graph;

    //0-Vuoto  1-Muro  2-IncrocioVisibile  3-IncrocioInvisibile  4-Palline  5-Palla  6-MuroValicabileDaMorto  7-PuntoRigenerazione




    //Leggo il file matrix.txt
    File f= new File("matrix.txt");
    //Non deve essere mai modificato, serve per il reset
    public static char originalMaze[][]= new char[y][x];
    //Può essere modificata
    public static char maze[][]= new char[y][x];
    public Map(){
        CreateMaze(x,y);
        makeOriginal(x,y);
        AddMoreNode(x,y);
        //Inserisco a mano un incrocio invisibile
        //che serve ad i fantasmini mangiati di tornare a casa

        //SI DEVONO AGGIUNGERE ANCHE LE PALLE GROSSE
        graph = CreateIntGraph();
        //Inserisco a mano un incrocio invisibile
        //che serve ad i fantasmini mangiati di tornare a casa
        maze[14][14]=maze[14][13]='3';
        //Inserisco a mano gli archi per questi nuovi nodi
        graph.addVertex("14,14");
        graph.addVertex("14,13");
        graph.addEdge("14,14","11,14");
        graph.addEdge("14,13","11,13");
        // Decommentare per vedere lo stato della matrice con un file chiamato matrixNew.txt
        // createFile();
    }

    public void createFile(){
        File f = new File("matrixNew.txt");
        try{
            f.createNewFile();
        }catch (Exception e){

        }
       try {
           FileWriter ff= new FileWriter("matrixNew.txt");

           for( i=0;i<y;i++){
               for(j=0;j<x;j++){
                   if(maze[i][j]=='4'||maze[i][j]=='5'){
                       if(maze[i][j]=='5')
                           ff.write("+");
                       else
                           ff.write(".");
                   }else
                       if(maze[i][j]=='2'||maze[i][j]=='3')
                           ff.write("+");
                       else
                            ff.write(String.valueOf(maze[i][j]));
                    ff.write(" ");
               }


               ff.write("\n");
           }


           ff.close();
       }catch (Exception e){

       }
    }

    private Graph<String, DefaultEdge> CreateIntGraph(){
         Graph<String,DefaultEdge> g = new SimpleWeightedGraph<>(DefaultEdge.class);
        //Creo tutti i vertici
         for(int i=0;i<y;i++){
             for(int j=0;j<x;j++){
                 //Scorro tutta la matrice ed aggiungo vertici al grafo solo quando incontro un nodo
                 if(maze[i][j]=='2'||maze[i][j]=='3'||maze[i][j]=='5'){
                     g.addVertex(new String(i+","+j));
                }
             }
         }

         //Creo tutti gli archi basandomi sulla matrice
        for(int i=0;i<y;i++){
            for(int j=0;j<x;j++) {
                //Se incontro un incrocio
                if (maze[i][j] == '2' || maze[i][j] == '3'||maze[i][j]=='5') {
                    //Per ogni nodo controllo, grazie alla matrice, se ha un altro nodo vicino
                    //2 direzioni
                    //Ho scelto di guardare solo in 2 direzioni per evitare ridondanza di archi
                    //Le uniche direzioni consentite sono, destra e giù
                    for (int l = 0; l < 2; l++) {
                        int w = 1;//Peso dal prossimo nodo
                        switch (l) {
                            case 0: {
                                //Destra
                                //Finche non incontro un muro, un incrocio(visibile o invisibile) oppure uno spazio vuoto(per il teleport)
                                //Aumento il peso

                                //Se immediatamente a destra ho un nodo, evito di fare il ciclo ed aggiungo l'arco
                                if(maze[i][j+w]=='2'||maze[i][j+w]=='3'||maze[i][j+w]=='5'){
                                    w += j;
                                    DefaultEdge e= g.addEdge(i + "," + j, i + "," + w);
                                    g.setEdgeWeight(e,w-j);
                                    break;
                                }

                                while (maze[i][j + w] != '1'&&maze[i][j + w] != '2'&&maze[i][j + w] != '3'&&maze[i][j + w] != '5'&&maze[i][j + w] != '0') {
                                    w++;
                                }
                                //Se ho trovato un arco con un peso maggiore di quello di partenza(1) e
                                //Se non incontro uno 0 quindi uno spazio vuoto, e quindi non c'è stato nessun problema
                                //con la mappa perchè è stata riempita correttamente di palline
                                //oppure non sto controllando la posizione di teletrasporto
                                if (w != 1&&maze[i][j+w]!='0') {
                                    w += j;
                                    DefaultEdge e =g.addEdge(i + "," + j, i + "," + w);
                                    g.setEdgeWeight(e,w-j);
                                }
                            }
                            break;
                            case 1: {
                                //Giu
                                //Finche non incontro un muro o un incrocio(visibile o invisibile)
                                //Aumento il peso

                                //Se immediatamente sotto ho un nodo, evito di fare il ciclo ed aggiungo l'arco
                                if(maze[i+w][j]=='2'||maze[i+w][j]=='3'||maze[i+w][j]=='5'){
                                    w += i;
                                    DefaultEdge e= g.addEdge(i + "," + j, w + "," + j);
                                    g.setEdgeWeight(e,w-i);
                                    break;
                                }
                                while (maze[i + w][j] != '1'&&maze[i + w][j] != '2'&&maze[i + w][j] != '3'&&maze[i + w][j] != '5'&&maze[i + w][j] != '0') {
                                    w++;
                                }
                                if (w != 1) {
                                    w += i;
                                    DefaultEdge e= g.addEdge(i + "," + j, w + "," + j);
                                    g.setEdgeWeight(e,w-i);
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }



         return g;
     }

    public void CreateMaze(int x,int y){
         File f =new File("matrix.txt");
         //Utilizzo 2 variabile per andatre avanti nel fgile della mappa così da escludere gli spazi
         int i=0;
         int l=0;
         try {
             FileReader fi = new FileReader(f);
             int c;
             boolean newLine;
             boolean eof=false;
             for (int j = 0;!eof; j++) {
                 newLine=false;
                 l=0;
                 //< di x+2 perchè conto pure i numeri degli a capo (13 10)
                  for(i=0;!newLine&&!eof;i++){
                      c = fi.read();
                      switch (c) {
                          case 13: {//A capo
                      }
                      break;
                      case 32: {//Spazio
                          //Non si conta
                      }
                      break;
                      case 43: {//Incrocio
                          maze[j][l]='2';
                          l++;
                      }
                      break;
                      case 46: {//Pallina
                          maze[j][l]='4';
                          l++;
                      }
                      break;
                      case 45: {//Muro per fantasmi
                          maze[j][l]='3';
                          l++;
                      }
                      break;
                      case 10: {//Seconda parte del a capo (DA IGNORARE)
                          newLine=true;
                      }
                      break;
                      case -1: {//EOF
                          eof=true;
                      }break;
                      default: {
                          //Il resto sono numeri quindi faccio c-48 per ottenere il numero corrispondente
                          maze[j][l]=(char)c;
                          l++;
                      }

                 }

             }
         }
         }catch (Exception e){e.printStackTrace();}

     }


    private void AddMoreNode(int x,int y){
         //Scorro tutta la matrice, per ogni nodo, controllo a 'mo di re degli scacchi, se posso inserire altri nodi intorno
         //I nodi che inseriscono sono identificati da 9
         //così appena finisco di inserire i nuovi nodi, li trasformo in 2
         //Se non proseguo per questa strada, la matrice sarà tutta piena di nodi
         for(int i=0;i<y;i++){
             for(int j=0;j<x;j++){
                 if(maze[i][j]=='2'||maze[i][j]=='3'||maze[i][j]=='5') {
                     for (int l = 0; l < 4; l++) {
                         switch (l) {
                             case 0: {
                                 //destra
                                 if (maze[i][j + 1] != '1') {
                                     maze[i][j+1]='9';
                                 }
                             }
                             break;
                             case 1: {
                                 //giu
                                 if (maze[i + 1][j] != '1') {
                                     maze[i+1][j]='9';
                                 }
                             }
                             break;
                             case 2: {
                                 //sinistra
                                 if (maze[i][j - 1] != '1') {
                                     maze[i][j-1]='9';
                                 }
                             }
                             break;
                             case 3: {
                                 //su
                                 if (maze[i-1][j]!='1'){
                                     maze[i-1][j]='9';
                                 }
                             } break;
                         }
                     }
                 }
             }
         }
         //Una volta inseriti i nodi temporanei, li rendo effettivi
         for(int i=0;i<y;i++){
             for(int j=0;j<x;j++){
                 if(maze[i][j]=='9'){
                     maze[i][j]='2';
                 }
             }
         }


     }

    private void makeOriginal(int x,int y){
        for(int i=0;i<y;i++){
            for(int j=0;j<x;j++){
                originalMaze[i][j]=maze[i][j];
            }
        }
    }
}