/**************************************************************
*   @(#)Life.java	
*
*   Copyright 1999 by The Abstract Company
**************************************************************/
import java.awt.*;
import java.util.*;
/**************************************************************
*   
*   @author  ¥NöP, ynop@acm.org, jta001@coes.latech.edu
*   @version 1.0
**************************************************************/
public class Life {
   ThreadGroup g = new ThreadGroup("AllLifeForms");
   MailBox[][][] mBox;
   int numThreads;
   int test= 0;
   int blah=0;
   int Gen = 10;
   LifeCanvas OutCanvas;
   TextField GenText;

   /**************************************************************
   * Life setus up our vars and initilizes the mailboxes for doin
   *    or simulating message passing
   **************************************************************/
   public Life(int numLifes,int gens,LifeCanvas LifeOut,TextField GenText){
      this.GenText = GenText;
      this.OutCanvas = LifeOut;
      Gen = gens;
      boolean tempbool;
      char tempchar;
      Thread me;
      Thread p;
      int count=0;
      numThreads = numLifes;
      mBox = new MailBox[numThreads+2][numThreads+2][10];
      g.setDaemon(true);
      for(int i=0; i < numThreads+2;i++){
         for(int j=0;j < numThreads+2;j++){
            
            for(int k=0;k < 10;k++){
               mBox[i][j][k] = new MailBox();
            }
         }
      }
      for(int i=0; i < numThreads+2;i++){
         for(int j=0;j < numThreads+2;j++){
            tempbool = OutCanvas.get(i,j);
            if(tempbool){tempchar='1';}else{tempchar='0';}
            
            p = new Thread(g,new LifeForm(i,j,tempchar,Gen),i+"-"+j);//initState.charAt(count++)
            p.start();
         }
      }
      p = new Thread(g,new Out(),"PrintThread");
      p.start();
      
   }
   
   /**************************************************************
   * Out is a thread that runns around and when all mail boxes
   *    have been setup and writen to reads them in and outputs
   *    the contents to a graphics panel
   **************************************************************/
   public class Out implements Runnable{   
      public Out(){ System.out.println("Printer started");} 
      
      public void run(){
         String newgen = new String("");
         int k= 0;
         char t;
         while(k < Gen){
            System.out.println("This is generation: "+k+". Long live generation "+(int)(k-1));
            
            GenText.setText("  "+(int)(k+1));
            
            newgen= new String("");
            for(int i=1; i <= numThreads;i++){
               for(int j=1;j <= numThreads;j++){
                  t = mBox[i][j][0].retreve();
                  if((t)=='1'){
                     OutCanvas.setxy(j-1,i-1);
                  }
                  System.out.print(t);
                 //newgen.concat(String.valueOf(mBox[i][j][0].retreve()));
               }System.out.println("");
            }
            k++;
            System.out.println("");
            try{
               Thread.sleep(numThreads*75);// 8 = 600
            }catch(Exception e){ System.out.println("I am not tired.");}
            //System.out.println("NextGeneration "+newgen+"  "+test); 
         }
      }
   }

   /**************************************************************
   * Life form is our main runnabel thread .. he does all the stuff
   *    that neads to be done - there is one for every square and 
   *    one for each border. he wirtes to his neibors mail boxxes 
   *    and then reads all his incoming mail. then he figures out
   *    whether he should die or getbusy with two of his neibors
   **************************************************************/
   public class LifeForm implements Runnable{
      int x,y,generation;
      char MyNextState ='0';
      char MyState = '0';
      
      public LifeForm(int x, int y,char initState,int gen){
         this.x = x;
         this.y = y;
         this.generation = gen;
         this.MyState = initState;
      }
            
      public void run(){
         char N,E,S,W,NE,SE,SW,NW;
         int i = 0;
         int count = 0;
         while(i < generation){
            i++;
            if((x!=0) && (x!=numThreads+1) && (y!=0) && (y!=numThreads+1)){
               mBox[x][y][0].store(MyState);
               //System.out.println(x+"  "+y+"  G "+i);
            }
         
            //System.out.println("I am lifeform "+ Thread.currentThread().getName() +" num "+x+"  "+y);
            //System.out.println("   to the north is "+x+"  "+((int)y+(int)1));
            
            if((y<(numThreads)) && (x>0) && (x<(numThreads+1))){
               test++; mBox[x][y+1][1].store(MyState);   }//MyState in Souths north MB
            
            if((x>1) && (y>0) && (y<(numThreads+1))){  
               test++; mBox[x-1][y][2].store(MyState);   }//Mystate in Wests east MB
            
            if((y>1) && (x>0) && (x<(numThreads+1))){
               test++; mBox[x][y-1][3].store(MyState);}//Mystate in Norths south MB
            
            if((x<(numThreads)) && (y>0) && (y<(numThreads+1))){
               test++; mBox[x+1][y][4].store(MyState);}//Mystate in Easts west MB
            
            if((x>1) && (y<numThreads)){ //SW
               try { test++; mBox[x-1][y+1][5].store(MyState);   } catch(NullPointerException e){System.out.println((int)(x-1)+" "+(int)(y+1)+e);}}//Mystate in SWs north-east MB
            
            if((x>1) && (y>1)){ //NW
               try { test++; mBox[x-1][y-1][6].store(MyState);   } catch(NullPointerException e){System.out.println((int)(x-1)+" "+(int)(y-1)+e);}}//Mystate in NWs south-east MB
            
            if((x<numThreads) && (y>1)){ //NE
               try { test++; mBox[x+1][y-1][7].store(MyState);  } catch(NullPointerException e){System.out.println((int)(x+1)+" "+(int)(y-1)+e);}} //Mystate in NEs south-west MB
            
            if((x<numThreads) && (y<numThreads)){ //SE
               try { test++; mBox[x+1][y+1][8].store(MyState);  } catch(NullPointerException e){System.out.println((int)(x+1)+" "+(int)(y+1)+e);}} //Mystate in SEs north-west MB
            
            blah++;
            //Check our mailboxes to see what our neibors have to say
            if((x>0) && (x<numThreads+1) && (y>0) && (y<numThreads+1)){  
               N =  mBox[x][y][1].retreve();  //north
               E =  mBox[x][y][2].retreve();  //east
               S =  mBox[x][y][3].retreve();  //south
               W =  mBox[x][y][4].retreve();  //west
               NE = mBox[x][y][5].retreve();  //north-east
               SE = mBox[x][y][6].retreve();  //south-east
               SW = mBox[x][y][7].retreve();  //south-west
               NW = mBox[x][y][8].retreve();  //north-west
         
               ///count up the number of neibors
               count=0;
               if(N=='1'){count++;}
               if(E=='1'){count++;}
               if(S=='1'){count++;}
               if(W=='1'){count++;}
               if(NE=='1'){count++;}
               if(SE=='1'){count++;}
               if(SW=='1'){count++;}
               if(NW=='1'){count++;}
                       
               // see if I live die or birth        
               if((count==3)){
                  MyState = '1';   // Birth
               }else if((count<2)){
                  MyState = '0';   // Lownlyness
               }else if((count>3)){
                  MyState = '0';     // Overcrouding
               }
             
               //MyState = MyNextState;
               
               // Sleep for a while .. not too long.
              // try{
              //    Thread.sleep(300);
              // }catch(Exception e){
              //    System.out.println(e);
              // }
            }
         
         }        
         
      }
      
   }

   /**************************************************************
   * Mailbox is our way of emulating message passing and stuff.
   **************************************************************/
   public class MailBox extends Object{
      char i;
      Semaphore s1 = new Semaphore(1);
      Semaphore s2 = new Semaphore(0);
      public MailBox(){
      }
      public void store(char i){
         s1.p(); // semapore
         this.i = i;
         //System.out.println("store");
         s2.v();
      }
      public char retreve(){
         s2.p();
         char temp = this.i;
         //System.out.println("retreve");
         s1.v();
         return temp;
      }
   }


   /**************************************************************
   * this is our semaphore .. it make the mailboxes atoming and 
   *    keeps thing running smoth.
   **************************************************************/
   public class Semaphore {
      int value;
      public Semaphore() {
         this(1);
      }
      public Semaphore(int i) {
         value = i;
      }
      public void p() {
         p(1);
      }
      public synchronized void p(int c) {
         while (value < c) {
            try {
               wait();
            }catch (InterruptedException e) {
            }
         }
         value -= c;
      }
      public void v() {
         v(1);
      }
      public synchronized void v (int count) {
         value += count;
         notify();
      }
      
   }
}
