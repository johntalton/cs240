import java.awt.*;
import java.awt.event.*;
import java.util.*;
 
   /**************************************************************
   * This is the class that does the actuall threads and stuff
   *    it created threads and a printer thread .. It takes in
   *    a Panel to draw to. Thas a bad way to do input output.
   **************************************************************/
   class LifeCanvas extends Canvas implements MouseListener{
      int x,y,w,h;
      Dimension d;
      int numLifeforms = 0;
      boolean grid[][];
      boolean oldgrid[][];
      float pixW,pixH;
      boolean edit = true;
      boolean b = true;
      
      /**************************************************************
      * This is our set up stuff. Sets up a canvas and stuff
      **************************************************************/
      public LifeCanvas(int NumLifes){
         this.numLifeforms = NumLifes;
         grid = new boolean[numLifeforms][numLifeforms];
         oldgrid = new boolean[numLifeforms][numLifeforms];
         this.setBackground(new Color(30,30,30));
         addMouseListener(this);
         for(int i=0; i < numLifeforms;i++){
            for(int j=0;j < numLifeforms;j++){
               grid[i][j] = false;
            }
         }
      }     
      
      /**************************************************************
      * A function we dont use but should because if you click while
      *    it is running you get weired stuff
      **************************************************************/
      public void setEditable(boolean b){
         this.edit = b;
      }     

      /**************************************************************
      * This is the paint that does everything - we really nead to
      *    make this Double Buffered
      **************************************************************/
      public void paint(Graphics g){
         d = getSize();
         pixW = (d.width / numLifeforms);
         pixH = (d.height / numLifeforms);
         g.setColor(new Color(255,0,0));
         for(int i = 0;i <= d.width;i+=pixW){
            g.drawLine(i,0,i,d.height);
         }
         for(int i = 0;i <= d.height;i+=pixH){
            g.drawLine(0,i,d.width,i);
         }
         
         g.setColor(new Color(255,255,255));
         for(int i=0; i < numLifeforms;i++){
            for(int j=0;j < numLifeforms;j++){
               if(grid[i%(numLifeforms)][j]){ g.fillRect((int)pixW*i,(int)pixH*(j),(int)pixW,(int)pixH); if(b){grid[i][j] = false;}}
            }
         }
      }
      
      /**************************************************************
      * Sets the x,y to paint.. and does it
      **************************************************************/
      public void setxy(int x,int y){
         this.b = true;
         grid[x][y] = true;
         this.x = (int)(pixH * x);
         this.y = (int)(pixW * y);
         w = (int)pixW;
         h = (int)pixH;
         repaint();
      }

      /**************************************************************
      * Sets the x,y  for mouse click input and stuff. 
      **************************************************************/
      public void setxy(int x,int y,boolean b){
         this.b = b;
         if(grid[x][y]){ grid[x][y] = false;}else{grid[x][y] = true;}
         this.x = (int)(pixH * x);
         this.y = (int)(pixW * y);
         w = (int)pixW;
         h = (int)pixH;
         repaint();
      }
      
      /**************************************************************
      * Clears the drawing screen
      **************************************************************/
      public void clear(){
         this.b = true;
         repaint();
      }
      
      /**************************************************************
      * Gets the valuse of a position for passing in to and initing 
      *    threads.
      **************************************************************/
      public boolean get(int y,int x){
         if((x==0) || (x==(numLifeforms+1)) || (y==0) || (y==(numLifeforms+1))){ return false; }
         return grid[x-1][y-1];
      }
      
      /**************************************************************
      * All this stuff if for mouse input
      **************************************************************/
      public void mousePressed(MouseEvent e){
         if((edit) && ((int)(e.getX()/pixW)!=(numLifeforms)) && ((int)(e.getY()/pixH)!=(numLifeforms))){  //toggle position on or off
            
            setxy((int)(e.getX()/pixW),(int)(e.getY()/pixH),false);
         }
         
      }
      public void mouseMoved(MouseEvent e) {
      }
      public void mouseReleased(MouseEvent e) {
      }
      public void mouseEntered(MouseEvent e) {
      }
      public void mouseExited(MouseEvent e) {
      }
      public void mouseClicked(MouseEvent e) {
      }
      public void mouseDragged(MouseEvent e) {
      }
   }