package client;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import service.SimulationClock;

public class ClockPanel extends JPanel {

	private static final long serialVersionUID = -2166709692460369850L;
	SimulationClock simClock;
	Timer tm;
	
	public ClockPanel() {
		super();
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JLabel clock = new JLabel("00", SwingConstants.CENTER);
		clock.setPreferredSize(new Dimension(75,50));
		clock.setBorder(BorderFactory.createTitledBorder("Clock"));
		add(clock);
		
		JButton start =new JButton("Start");
		JButton stop =new JButton("Stop");
		JButton save = new JButton("Save Report");
		
		add(start);
		add(stop);
		add(save);
		
	}
	
	public void setClock(Timer tm, SimulationClock simClock)
	{
		this.tm = tm;
		this.simClock = simClock;
	}

}
