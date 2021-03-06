package core.vehicle;

public class Car extends Vehicle 
{
	private int length;
	
	public Car() {
		super();
		this.length=1;
	}
	
	public Car(int velocity, int acceleration, int max_velocity) {
		//NC > for cars the length is 1
		super(velocity, acceleration, max_velocity);
		this.length=1;
	}	
	
	@Override
	public int getLength()
	{
		return length;
	}
	
	@Override
	public Color getColor()
	{
		return this.color;
	}
}
