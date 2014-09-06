import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.robotics.subsumption.Behavior;


public class CollisionAvoidance implements Behavior{

	private boolean suppressed = false;
	
	@Override
	public boolean takeControl() {    	
		return Utils.myRobotPos.getSonar1Reading() < 100;// || Utils.myRobotPos.getSonar2Reading() < 100;
	}

	@Override
	public void action() {
		suppressed = false;
		
		LCD.clear();
		LCD.drawString("Collision avoid.", 0, 0);
		LCD.drawInt(Utils.myRobotPos.getSonar1Reading(), 0, 1);
    	LCD.drawInt(Utils.myRobotPos.getSonar2Reading(), 0, 2);
		
    	Motor.A.rotate(-180, true);
		Motor.B.rotate(-360, true);
		
		while(Motor.B.isMoving() && !suppressed)
			Thread.yield();
		
		Motor.A.stop(true);
		Motor.B.stop(true);
		
		LCD.refresh();
		Utils.myRobotPos.computePosition();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
