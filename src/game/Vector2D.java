package game;

public class Vector2D {
	
	private double magnitude;
	private double direction;
	private double xComponent;
	private double yComponent;
	
	public Vector2D(double xComponent, double yComponent) {
		this.xComponent = xComponent;
		this.yComponent = yComponent;
		computeMagnitudeDirection();
	}
	
	public void add(Vector2D vector) {
		xComponent += vector.getXComponent();
		yComponent += vector.getYComponent();
		computeMagnitudeDirection();
	}
	
	public void subtract(Vector2D vector) {
		xComponent -= vector.getXComponent();
		yComponent -= vector.getYComponent();
		computeMagnitudeDirection();
	}
	
	public void multiply(double value) {
		xComponent *= value;
		yComponent *= value;
		computeMagnitudeDirection();
	}
	
	public void divide(double value) {
		xComponent /= value;
		yComponent /= value;
		computeMagnitudeDirection();
	}
	
	public double dot(Vector2D vector) {
		return (xComponent * vector.getXComponent()) + (yComponent * vector.getYComponent());
	}
	
	public void reverse() {
		multiply(-1);
	}
	
	public double getAngleBetween(Vector2D vector) {
		return Math.acos(dot(vector) / (vector.getMagnitude() * magnitude));
	}

	public double getMagnitude() {
		return magnitude;
	}

	public double getDirection() {
		return direction;
	}

	public double getXComponent() {
		return xComponent;
	}

	public double getYComponent() {
		return yComponent;
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
		computeComponents();
	}

	public void setDirection(double direction) {
		this.direction = direction;
		computeComponents();
	}

	public void setXComponent(double xComponent) {
		this.xComponent = xComponent;
		computeMagnitudeDirection();
	}

	public void setYComponent(double yComponent) {
		this.yComponent = yComponent;
		computeMagnitudeDirection();
	}
	
	private void computeComponents() {
		xComponent = magnitude * Math.cos(direction);
		yComponent = magnitude * Math.sin(direction);
	}
	
	private void computeMagnitudeDirection() {
		if (xComponent == 0 && yComponent == 0) {
			direction = 0;
			magnitude = 0;
			return;
		}
		
		if (xComponent == 0) {
			magnitude = Math.abs(yComponent);
			if (yComponent < 0) {
				direction = 3 * Math.PI / 2.0;
			}
			else {
				
				direction = Math.PI / 2.0;
			}
			return;
		}
		
		if (yComponent == 0) {
			magnitude = Math.abs(xComponent);
			if (xComponent < 0) {
				direction = Math.PI;
			}
			else {
				direction = 0;
			}
			return;
		}
		direction = Math.atan(Math.abs(yComponent) / Math.abs(xComponent));
		magnitude = Math.sqrt(Math.pow(xComponent, 2) + Math.pow(yComponent, 2));
	}
	
	@Override
	public String toString() {
		return ("< " + xComponent + " , " + yComponent + " >" + " " + magnitude + " @ " + direction);
	}

}
