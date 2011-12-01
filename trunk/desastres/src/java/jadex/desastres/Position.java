package jadex.desastres;

public class Position{
	protected double x;
	protected double y;
	
	public Position(Double x, Double y){
		this.x = x;
		this.y = y;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
}