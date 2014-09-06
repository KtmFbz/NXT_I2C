import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.robotics.subsumption.Behavior;


public class MotorControl implements Behavior{

	private boolean suppressed = false;
	
	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		suppressed = false;
		LCD.drawString("Motor control", 0, 0);
		while(!suppressed) {
			//Formulas of wheel speed given robot speed, for unicycle-like robot
			//SpeedRight = (DesSpeed - omega*TRACK)/WheelRadius
			//SpeedLeft  = (DesSpeed + omega*TRACK)/WheelRadius
			//omega found through simple algebraic error (Pos_des - Pos_act)
			//double omega = (myRobotPos.actualPhi-myRobotPos.desPhi)*myRobotPos.desSpeed/
			//		(Math.sqrt((myRobotPos.actualX - myRobotPos.desX)^2 + (myRobotPos.actualY - myRobotPos.desY)^2) );
			//omega imposed by Lyapunov convergence to desired line
			//double omega = -myRobotPos.desY*(Math.sin(myRobotPos.desPhi)/myRobotPos.desPhi)*myRobotPos.desSpeed - KAPPA_PHI*myRobotPos.desPhi;

			//Wheel speed calculation with raw low level orientation tracking/antiskid
			int SpeedRight = (int) ((Utils.myRobotPos.getDesSpeed() - Utils.myRobotPos.getOmega()*Utils.TRACK)/
					Utils.WHEEL_RADIUS -Utils.KAPPA_EPS*(Utils.myRobotPos.getActualPhi()-Utils.myRobotPos.getDesPhi()));
			int SpeedLeft  = (int) ((Utils.myRobotPos.getDesSpeed() + Utils.myRobotPos.getOmega()*Utils.TRACK)/
					Utils.WHEEL_RADIUS +Utils.KAPPA_EPS*(Utils.myRobotPos.getActualPhi()-Utils.myRobotPos.getDesPhi()));

			Motor.A.setSpeed((int) (SpeedRight*1.8/Utils.PI));
			Motor.B.setSpeed((int) (SpeedLeft*1.8/Utils.PI));

//			Motor.A.forward();
//			Motor.B.forward();

			//		while(!suppressed)
			//			Thread.yield();

			Utils.myRobotPos.computePosition();
		}
		Motor.A.stop(true);
		Motor.B.stop(true);
    	
		LCD.refresh();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
