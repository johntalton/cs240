/**************************************************************
*   @(#)LifeGUI.java	
*
*   @author  ¥NöP, ynop@acm.org, jta001@coes.latech.edu
*   @version 1.0
*   Copyright 1999 by The Abstract Company
**************************************************************/
import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**************************************************************
*   This is the GUI for the game of Life - it is independent of
*      the LifePanel that we draw to.  So if we ever nead to 
*      change the graphics aound we can do so pritty easy.
*
*  Our program is multy thread and runns totaly inefficiently
*     so I don't sugest useing it.
**************************************************************/
public class LifeGUI extends Panel {
   static Frame frame;
   LifeCanvas GenePool;
   TextField GenCounter;
   int numLifeforms;
   int NumGens;
   private static ResourceBundle resources;
   static {
      try {
         resources = ResourceBundle.getBundle("Life",Locale.getDefault());
      } catch (MissingResourceException mre) {
         System.err.println("File Life.properties not found");
         MyExit();
      }      
   }
   private static Color LightBlue = new Color(132,192,214);
   private static Color Grey = new Color(155,155,155);
   Label Title = new Label("             Watson Life Lab             ");
   Font WatsonFont = new Font("Helvetica", Font.PLAIN,24);
   
   public LifeGUI() {
      try {
         numLifeforms = Integer.parseInt(resources.getString("NumLifeForms"));
      } catch (MissingResourceException mre) {
         numLifeforms = 8; // tipical
	   }
	   try {
	      NumGens = Integer.parseInt(resources.getString("NumGenerations"));
	   } catch(MissingResourceException mre){
	      NumGens = 10; // a nice default
	   }
  
      System.out.println("Lifeform count: " + numLifeforms + "\n");
      ButtonListener myButtonListener = new ButtonListener();
      frame.setBackground(Grey);
      Panel top_panel = new Panel();
      top_panel.setLayout(new BorderLayout(15,15));
      Panel top_panel_left = new Panel();
      top_panel_left.setLayout(new FlowLayout(FlowLayout.LEFT, 15,15));
      
      Button Activity = new Button("  Activity ");
      Activity.setBackground(LightBlue);
      Activity.setFont(WatsonFont);
      Activity.addActionListener(myButtonListener);      
      top_panel_left.add(Activity);
      
      Button What = new Button("What Is?");
      What.setBackground(LightBlue);
      What.setFont(WatsonFont);
      What.addActionListener(myButtonListener);      
      top_panel_left.add(What);
      //  The top_panel center region
      Panel top_panel_center = new Panel();
      top_panel_center.setLayout(new FlowLayout(FlowLayout.CENTER, 15,15));
      Title.setFont(WatsonFont);
      top_panel_center.add(Title);
      //  The top panel right region
      Panel top_panel_right = new Panel();
      top_panel_right.setLayout(new FlowLayout(FlowLayout.RIGHT, 15,15));
      Button Controls = new Button("Controls");
      Controls.setBackground(LightBlue);
      Controls.setFont(WatsonFont);
      Controls.addActionListener(myButtonListener);
      top_panel_right.add(Controls);
      Button Close = new Button(" Exit ");
      Close.setBackground(LightBlue);
      Close.setFont(WatsonFont);
      Close.addActionListener(myButtonListener);
      top_panel_right.add(Close);
 
      top_panel.add("West", top_panel_left);
      top_panel.add("Center", top_panel_center);
      top_panel.add("East", top_panel_right);
      /////////////////////////////////
      //  Finished top panel         //
      /////////////////////////////////
      Panel center_panel = new Panel();
      center_panel.setLayout(new BorderLayout(15,15));

      Panel MainPanel = new Panel();
      MainPanel.setLayout(new GridLayout(1,2,15,5));

      Panel Left = new Panel();
      Left.setLayout(new BorderLayout(15,15));
      Left.setBackground(Grey);
           
      Panel Description = new Panel();
      Description.setLayout(new BorderLayout(15,15));
      Description.setBackground(Color.blue);
 
      TextArea  msg = new TextArea("",1,1,TextArea.SCROLLBARS_NONE);
      msg.setText("\n"+
                  "\n"+
                  "\n"+
                  "\n"+
                  "\n"+
                  "                  The Game Of Life\n"+
                  "                       by YNOP\n");
      msg.setFont(WatsonFont);
      msg.setForeground(Color.yellow);
      msg.setEditable(false);
      Description.add(msg);

      Left.add(Description);
      
      Panel Right = new Panel();
      Right.setLayout(new BorderLayout(15,15));
      Right.setBackground(Grey);
    
      Panel DrawWindow = new Panel();
      DrawWindow.setLayout(new BorderLayout(15,15));
      DrawWindow.setBackground(Color.blue);
      Panel TitlePanel = new Panel(new BorderLayout(15,15));
      Label DWTitle = new Label("  Life");
      DWTitle.setFont(WatsonFont);
      DWTitle.setForeground(Color.yellow);
      TitlePanel.add(DWTitle,"West");
      /////////////////////////////////////////////
      GenePool = new LifeCanvas(numLifeforms);//THIS COED IS SPESIFIC TO LIFE
      /////////////////////////////////////////////
      Label genTitle = new Label("  Current Generation:");
      genTitle.setFont(WatsonFont);
      genTitle.setForeground(Color.yellow);
      
      GenCounter = new TextField("  0");
      GenCounter.setFont(WatsonFont);
      GenCounter.setBackground(Grey);
      GenCounter.setEditable(false);
      
      Panel GenPanel = new Panel(new BorderLayout(15,15));
      GenPanel.add(genTitle,"West");      
      GenPanel.add(GenCounter,"Center");
      GenPanel.add(new Label(""),"South");
      GenPanel.add(new Label(""),"East");
      
      DrawWindow.add(TitlePanel,"North");
      DrawWindow.add(GenePool,"Center");
      DrawWindow.add(GenPanel,"South");
       
      Panel ControlWindow = new Panel();
      ControlWindow.setLayout(new BorderLayout(15,15));
      ControlWindow.setBackground(Color.blue);
      Panel ControlButtons = new Panel(new FlowLayout(FlowLayout.CENTER, 15,15));
      Button Run = new Button(" Run ");
      Run.setBackground(LightBlue);
      Run.setFont(WatsonFont);
      Run.addActionListener(myButtonListener);
      Button Walk = new Button(" Walk ");
      Walk.setBackground(LightBlue);
      Walk.setFont(WatsonFont);
      Walk.addActionListener(myButtonListener);
      Button Clear = new Button(" Clear ");
      Clear.setBackground(LightBlue);
      Clear.setFont(WatsonFont);
      Clear.addActionListener(myButtonListener);
      ControlButtons.add(Run);
      ControlButtons.add(Walk);     
      ControlButtons.add(Clear);
      Label ControlTitle = new Label("  Program Execution Controls");
      ControlTitle.setFont(WatsonFont);
      ControlTitle.setForeground(Color.yellow);
      ControlWindow.add(ControlTitle,"North");
      ControlWindow.add(ControlButtons,"Center");
     
      Right.add(DrawWindow,"Center");
      Right.add(ControlWindow,"South");
    
      MainPanel.add(Left);
      MainPanel.add(Right);

      center_panel.add("Center", MainPanel);
      center_panel.add("West", new Label(""));
      center_panel.add("East", new Label(""));

      frame.add("North", top_panel);
      frame.add("Center", center_panel);
      frame.add("South", new Label(""));
   }

   class ButtonListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         Life newLife;
	      String Name = e.getActionCommand();
         if(Name == " Run "){
            newLife = new Life(numLifeforms,NumGens,GenePool,GenCounter);
         }else if(Name == " Walk "){
            
         }else if(Name == " Clear "){
            GenePool.clear();
         }else if(Name == "  Activity "){
         
         }else if(Name == "What Is?"){
         
         }else if(Name == "Controls"){
            
         }else if(Name == " Exit "){
            MyExit();
         }
      }
   }


	   
   public static void MyExit(){
      //Shutdown all threads if running and clean up
      System.exit(0);
   }

   public static void main(String s[]) {
      frame = new Frame("Life");
      LifeGUI panel = new LifeGUI();
	   frame.addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) { MyExit();}
      });
      frame.pack();
      frame.setSize(1000,750);
	   frame.setVisible(true);
   }
}
