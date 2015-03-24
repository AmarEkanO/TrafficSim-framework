package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import core.endpoints.EndPointException;
import core.network.Road;
import core.vehicle.Bus;
import core.vehicle.Car;
import core.vehicle.Vehicle;

public class Network2 extends Network
{

	int counter;
	Timer timer_change_lights;
	Timer timer_move_cars;
	
	private JPanel view;
	private JPanel controls;
	
	
	private ActionListener actionListener_move_cars;
	private Road r1;
	private List<Vehicle> vehicleList;
	private int roadLength = 40; 
	private int carWidth = 20;
	private int vehicleHeight = 10;
	private int busWidth = 30;
	
	public Network2() {

		super();
		counter=0;
		ActionListener actionListener = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(counter==3){
					counter=-1;
				}
				counter++;
				view.repaint();
			}
		};
		
		timer_change_lights = new Timer(1000, actionListener);
		
		
		r1 = new Road(2,roadLength);

		vehicleList = new ArrayList<Vehicle>();
		

		//AM > Every time the clock ticks move cars
		actionListener_move_cars = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					r1.moveTraffic();
					Vehicle v = new Car();
					vehicleList.add(v);
					r1.addVehicle(v);
					view.repaint();
					
					Vehicle b = new Bus();
					vehicleList.add(b);
					r1.addVehicle(b);
					view.repaint();
				} catch (EndPointException e) {
					e.printStackTrace();
				}
			}
		};
		
		timer_move_cars = new Timer(1000, actionListener_move_cars);
		
		controls = new ControlPanel();
		view = new JPanel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				int panelWidth = (int) getSize().getWidth();
				int panelHeight = (int) getSize().getHeight();
				int hroadHeight = 60;
				int hdestinationWidth = 20;
				int vdestinationHeight = 20;
				
				//AM > Draw a horizontal road
				g.setColor(Color.BLACK);
				int hroadStartX = 0 + hdestinationWidth;
				int roadStartY = panelHeight/2 - hroadHeight/2;
				int roadWidth = panelWidth - hdestinationWidth*2;
				int roadEndX = hroadStartX+roadWidth;
				int roadEndY = roadStartY;
		 		g.fillRect(hroadStartX, roadStartY, roadWidth, hroadHeight);
		 		
		 		//AM > Draw a vertical road
		 		g.setColor(Color.BLACK);
				int vroadStartY = 0;
				int vroadStartX = panelWidth/2-hroadHeight/2;
				int vroadWidth = hroadHeight;
				int vroadHeight= panelHeight - vdestinationHeight;
				int vroadEndY = vroadStartY + vroadHeight;
				int vroadEndX = vroadStartX;
		 		g.fillRect(vroadStartX, vroadStartY, vroadWidth, vroadHeight);
				
				//AM > Draw destination A
		 		int textOffsetX = 5;
				int textOffsetY = 5;
				g.setColor(Color.GRAY);
				g.fillRect(0,roadStartY, hdestinationWidth,hroadHeight);
				g.setColor(Color.BLACK);
				g.drawString("A", hdestinationWidth/2 - textOffsetX, roadStartY + hroadHeight/2 + textOffsetY);
				
				//AM > Draw destination B
				g.setColor(Color.GRAY);
				g.fillRect(roadEndX, roadEndY, hdestinationWidth, hroadHeight);
				g.setColor(Color.BLACK);
				g.drawString("B", roadEndX +hdestinationWidth/2 - textOffsetX, roadStartY + hroadHeight/2 + textOffsetY);
				
				//AM > Draw destination C
				g.setColor(Color.GRAY);
				g.fillRect(vroadStartX,0,vroadWidth ,vdestinationHeight);
				g.setColor(Color.BLACK);
				g.drawString("C",vroadStartX + hroadHeight/2 - textOffsetX, hdestinationWidth/2 + textOffsetY);
				
				
				//AM > Draw destination D
				g.setColor(Color.GRAY);
				g.fillRect(vroadEndX, vroadEndY, vroadWidth,vdestinationHeight);
				g.setColor(Color.BLACK);
				textOffsetX = 5;
				textOffsetY = 5;
				g.drawString("D", vroadEndX +vroadWidth/2 - textOffsetX, vroadEndY + vdestinationHeight/2 + textOffsetY);
				
				//AM > Draw road divider
				g.setColor(Color.WHITE);
				g.drawLine(hroadStartX , panelHeight/2, roadEndX -1, panelHeight/2);
				
				//AM > Draw horizontal lane separators
				Graphics2D g2d = (Graphics2D) g.create();
				Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
		        g2d.setStroke(dashed);
		        g2d.setColor(Color.WHITE);
		        int upperLaneDividerY = panelHeight/2 - hroadHeight/4;
		        int lowerLaneDividerY = panelHeight/2 + hroadHeight/4;
		        g2d.drawLine(hroadStartX, upperLaneDividerY, roadEndX -1, upperLaneDividerY);
		        g2d.drawLine(hroadStartX, lowerLaneDividerY, roadEndX -1, lowerLaneDividerY);
		        
		        //AM > Draw a center line between lane boundaries
		        //g.setColor(Color.RED);
		        //g.drawLine(roadStartX, upperLaneDividerY - roadHeight/8, roadEndX, upperLaneDividerY - roadHeight/8);
		        //g.drawLine(roadStartX, panelHeight/2 - roadHeight/8, roadEndX, panelHeight/2 - roadHeight/8);
		        
		        //AM > Draw Vertical divider
		        g.setColor(Color.WHITE);
		        g.drawLine(panelWidth/2, vdestinationHeight, panelWidth/2, vroadEndY);
		        
		        //AM > Draw vertical lane separators
		        int leftLaneDividerX = panelWidth/2 - vroadWidth/4;
		        int rightLaneDividerX = panelWidth/2 + vroadWidth/4;
		        g2d.drawLine(leftLaneDividerX, vroadStartY+vdestinationHeight, leftLaneDividerX, vroadEndY);
		        g2d.drawLine(rightLaneDividerX, vroadStartY+vdestinationHeight, rightLaneDividerX, vroadEndY);
		        
		        
		        int blockWidth = (int)roadWidth/roadLength;
		      //For each vehicle on the road get its co-ordinates
		        for(Vehicle v : vehicleList)
		        {
		        	//Random r = new Random();
		        	//g.setColor(new Color(r.nextFloat(), r.nextFloat(), r.nextFloat()));
		        	if(v instanceof Car){
		        		g.setColor(Color.RED);
		        	}
		        	else if(v instanceof Bus){
		        		g.setColor(Color.YELLOW);
		        	}
		        	//For each vehicle calculate its X and Y co-ordinates
		            int carX = 0;
		            int carY = 0;
		            if(r1.getVehicleNodeIndex(v) != -1)
		            {
		            	carX = hroadStartX + blockWidth*r1.getVehicleNodeIndex(v);
		            	if(r1.getVehicleLaneIndex(v) == 0)
		            		carY =  upperLaneDividerY - hroadHeight/8 - vehicleHeight/2;
		            	else
		            		carY =  (panelHeight/2 - hroadHeight/8) - vehicleHeight/2;
		            	carWidth =  (int) (blockWidth*0.5);
		            	busWidth = (int)(blockWidth*0.75);
		            	if(v instanceof Car){
		            		g.fillRect(carX,carY,carWidth, vehicleHeight);
		            	}
		            	else if(v instanceof Bus){
		            		g.fillRect(carX,carY,busWidth, vehicleHeight);
		            	}
		            	
		            	
		            	carX = roadEndX - blockWidth*r1.getVehicleNodeIndex(v)-carWidth;
		            	if(r1.getVehicleLaneIndex(v) == 0)
		            		carY =  upperLaneDividerY - hroadHeight/8 - vehicleHeight/2+ hroadHeight/2;
		            	else
		            		carY =  (panelHeight/2 - hroadHeight/8) - vehicleHeight/2+ hroadHeight/2;
		            	carWidth =  (int) (blockWidth*0.5);
		            	busWidth = (int)(blockWidth*0.75);
		            	if(v instanceof Car){
		            		g.fillRect(carX,carY,carWidth, vehicleHeight);
		            	}
		            	else if(v instanceof Bus){
		            		g.fillRect(carX,carY,busWidth, vehicleHeight);
		            	}
		            }   
		        }
		        
		        
		        //AM > Draw junction box
		        //g.setColor(Color.GRAY);
		        //g.fillRect(panelWidth/2 - vroadWidth/2, panelHeight/2 - hroadHeight/2,vroadWidth, hroadHeight);
		        if(counter==0){
		        	Image img = new ImageIcon("res/cycle0.png").getImage();
		        	g.drawImage(img,panelWidth/2 - vroadWidth/2, panelHeight/2 - hroadHeight/2, vroadWidth, hroadHeight, this);
		        }
		        if(counter==1){
		        	Image img = new ImageIcon("res/cycle1.png").getImage();
		        	g.drawImage(img,panelWidth/2 - vroadWidth/2, panelHeight/2 - hroadHeight/2, vroadWidth, hroadHeight, this);
		        }
		        if(counter==2){
		        	Image img = new ImageIcon("res/cycle2.png").getImage();
		        	g.drawImage(img,panelWidth/2 - vroadWidth/2, panelHeight/2 - hroadHeight/2, vroadWidth, hroadHeight, this);
		        }
		        if(counter==3){
		        	Image img = new ImageIcon("res/cycle3.png").getImage();
		        	g.drawImage(img,panelWidth/2 - vroadWidth/2, panelHeight/2 - hroadHeight/2, vroadWidth, hroadHeight, this);
		        }
		        
		        timer_move_cars.start();
		        timer_change_lights.start();
			  }
		};
	}
	
	@Override
	public JPanel getView() {
		// TODO Auto-generated method stub
		return view;
	}

	@Override
	public JPanel getControls() {
		// TODO Auto-generated method stub
		return controls;
	}
}