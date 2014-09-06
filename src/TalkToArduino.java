import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/* International System quantities! [millimeters, milliseconds, degrees] */

public class TalkToArduino {

    private static byte [] tmp = new byte[1];
    private static int res = 0;
    //private static String name;
    
    public static void main (String[] args) {
    	
//    	LCD.setAutoRefresh(false);
//    	LCD.drawString("Initializing...", 0, 0);
//    	int counter = 0;
//    	while(Button.ESCAPE.isUp()) {
//    		res = Utils.arduino.getData(0x05, tmp, 1);
//    		if (res == -5) {
//    			LCD.drawString("Not connected", 0, 2);
//    		}
//    		if (res == -3) {
//    			LCD.drawString("Bus error", 0, 2);
//    		}
//    		else if (res == 0) {
//    			LCD.drawString("Name: " + Utils.arduino.getProductID(), 0, 0);
//    			LCD.drawString("Vendor: " + Utils.arduino.getVendorID(), 0, 1);
//    			LCD.drawString("Version: " + Utils.arduino.getVersion(), 0, 2);
//    		}
//    		try { Thread.sleep(1000); } catch(InterruptedException ex) { Thread.currentThread().interrupt(); }
//    		counter ++;
//    		LCD.drawInt(counter, 0, 3); LCD.drawString(" cycle", 5, 3);
//    		LCD.refresh();
//    	}
    	tmp[0] = 0;
    	int counter = 0;
    	
    	//Initialising
//	  	LCD.setAutoRefresh(false);
    	LCD.drawString("Initializing...", 0, 0);
    	
    	//Reset compass
    	Utils.compass.resetCartesianZero();
    	LCD.drawString("Compass reset!", 0, 1);
    	
    	//wait for the rest of the system to boot up!
    	while(Button.ESCAPE.isUp()) {
    		res = Utils.arduino.getData(0x05, tmp, 1);
    		if (res == -5) {
    			LCD.drawString("Not connected", 0, 2);
    		}
    		if (res == -3) {
    			LCD.drawString("Bus error", 0, 2);
    		}
    		else if (res == 0 && tmp[0] == 1) {
    			LCD.drawString("Connected to Arduino!", 0, 2);
    			break;
    		}
    		
    		try { Thread.sleep(1000); } catch(InterruptedException ex) { Thread.currentThread().interrupt(); }
    		counter ++;
    		LCD.drawInt(counter, 0, 3); LCD.drawString(" cycle", 5, 3);
    		LCD.refresh();
    	}
    	
    	//First sonar update
    	Utils.myRobotPos.sonarUpdate((byte) 0);
    	
    	LCD.drawString("Initialized!", 0, 4);
    	
    	Behavior collisionAvoidance = new CollisionAvoidance();
    	Behavior motorControl = new MotorControl();
    	Behavior i2cCommunication = new I2CCommunication();
    	Behavior [] behaviorArray = {/*motorControl, collisionAvoidance, */i2cCommunication};	//
    	Arbitrator arb = new Arbitrator(behaviorArray);
    	LCD.drawString("Start arbitr...", 0, 5);

    	Utils.startTime = System.currentTimeMillis();
    	LCD.drawString("T Start:" + String.valueOf(Utils.startTime), 0, 6);

    	while(Button.ESCAPE.isUp()) {
    		if (Utils.arduino.getData(0x06, tmp, 1) == 0) {
    			arb.start();
//    			Utils.myRobotPos.sonarUpdate((byte) 0);
//            	LCD.refresh();
    		}
    	}
    }
    
}