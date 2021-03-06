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

import client.Renderer;
import service.DemandMatrix;
import service.DemandMatrixException;
import service.ReportGenerator;
import service.RoadNetwork;
import service.SimulationClock;
import core.endpoints.Destination;
import core.endpoints.EndPointException;
import core.network.Road;
import core.vehicle.Bus;
import core.vehicle.Car;
import core.vehicle.Vehicle;

/*
 * AM > This is the network for a straight road
 */
public class Network1 extends Network {
	private JPanel view;
	private ControlPanel controls;
	private Timer tm;
	private ActionListener actionListener;
	private RoadNetwork roadNetwork;
	private Road ra_b;
	private Road rb_a;
	private Destination A;
	private Destination B;
	private SimulationClock clock;
	private DemandMatrix dm_cars;
	private DemandMatrix dm_buses;
	private List<Vehicle> vehicleList;
	private int roadLength = 25; 
	private int numOfLanes = 2;
	private int carWidth = 20;
	private int vehicleHeight = 10;
	private int busWidth = 30;



	public Network1() {
		super();

		//AM > Every time the clock ticks move cars
		actionListener = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				clock.incrementClock();
				view.repaint();
			}
		};
		clock = new SimulationClock();
		tm = new Timer(1000, actionListener);
		
		controls = new ControlPanel(tm,clock);

		//AM > Create a road
		ra_b= new Road(numOfLanes,roadLength);
		rb_a = new Road(numOfLanes, roadLength);
		A = new Destination("A");
		B = new Destination("B");
		
		A.setClock(clock);
		B.setClock(clock);

		ra_b.setSource(A);
		ra_b.setSink(B);

		rb_a.setSource(B);
		rb_a.setSink(A);

		roadNetwork = new RoadNetwork();
		roadNetwork.addRoad(ra_b);
		roadNetwork.addRoad(rb_a);

		dm_cars = new DemandMatrix();
		dm_cars.addDestination(A);
		dm_cars.addDestination(B);
		dm_cars.setVehicleType(Car.class);
		try {
			dm_cars.initializeMatrix();
			dm_cars.setDemand(A, B, 1.0);
			dm_cars.setDemand(B, A, 0.5);
		} catch (DemandMatrixException e1) {
			e1.printStackTrace();
		}

		dm_buses = new DemandMatrix();
		dm_buses.addDestination(A);
		dm_buses.addDestination(B);
		dm_buses.setVehicleType(Bus.class);

		try {
			dm_buses.initializeMatrix();
			dm_buses.setDemand(A, B, 0.5);
			dm_buses.setDemand(B, A, 1.0);
		} catch (DemandMatrixException e) {
			e.printStackTrace();
		}
		
		clock.addObserver(roadNetwork);
		clock.addObserver(dm_cars);
		clock.addObserver(dm_buses);

		controls.setDemandMatrixCars(dm_cars);
		controls.setDemandMatrixBuses(dm_buses);
		controls.addDestinations(A);
		controls.addDestinations(B);

		vehicleList = new ArrayList<Vehicle>();

		ReportGenerator generator = new ReportGenerator();
		generator.addDestination(A);
		generator.addDestination(B);
		controls.setReportGenerator(generator);
		
		view = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int panelWidth = (int) getSize().getWidth();
				int panelHeight = (int) getSize().getHeight();
				int roadHeight = 150;
				int destinationWidth = 75;
				int roadStartX = 0 + destinationWidth;
				int roadStartY = panelHeight/2 - roadHeight/2;
				int roadWidth = panelWidth - destinationWidth*2;
				int roadEndX = roadStartX+roadWidth;
				int roadEndY = roadStartY;
				int upperLaneDividerY = panelHeight/2 - roadHeight/4;
				int lowerLaneDividerY = panelHeight/2 + roadHeight/4;

				Renderer.renderRoad(g, "A", "B", roadStartX, roadStartY, roadWidth, roadHeight, Renderer.Direction.EAST);

				//AM > Draw cars on road A to B 
				int blockWidth = (int)roadWidth/roadLength;
				vehicleList = ra_b.getVehiclesOnRoad();

				//For each vehicle on the road get its co-ordinates
				for(Vehicle v : vehicleList)
				{
					if(v instanceof Car){
						g.setColor(Color.RED);
					}
					else if(v instanceof Bus){
						g.setColor(Color.YELLOW);
					}
					//For each vehicle calculate its X and Y co-ordinates
					int carX = 0;
					int carY = 0;
					if(ra_b.getVehicleNodeIndex(v) != -1)
					{
						carX = roadStartX + blockWidth*ra_b.getVehicleNodeIndex(v);
						if(ra_b.getVehicleLaneIndex(v) == 0)
							carY =  upperLaneDividerY - roadHeight/8 - vehicleHeight/2;
						else
							carY =  (panelHeight/2 - roadHeight/8) - vehicleHeight/2;
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

				List<Vehicle>vehicleListRb_a = rb_a.getVehiclesOnRoad();
				for(Vehicle v : vehicleListRb_a)
				{
					if(v instanceof Car){
						g.setColor(Color.RED);
					}
					else if(v instanceof Bus){
						g.setColor(Color.YELLOW);
					}
					//For each vehicle calculate its X and Y co-ordinates
					int carX = 0;
					int carY = 0;
					if(rb_a.getVehicleNodeIndex(v) != -1)
					{	
						carX = roadEndX - blockWidth*rb_a.getVehicleNodeIndex(v) - carWidth;
						if(rb_a.getVehicleLaneIndex(v) == 0)
							carY =  upperLaneDividerY - roadHeight/8 - vehicleHeight/2+ roadHeight/2;
						else
							carY =  (panelHeight/2 - roadHeight/8) - vehicleHeight/2+ roadHeight/2;
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
				Image legend = new ImageIcon(getClass().getResource("res/legend.png")).getImage();
				g.drawImage(legend, 0, 0, null);
			}
		};
	}

	@Override
	public JPanel getView() {
		return view;
	}


	@Override
	public JPanel getControls() {
		return controls;
	}
}
