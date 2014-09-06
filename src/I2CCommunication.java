import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;


public class I2CCommunication implements Behavior{

	@SuppressWarnings("unused")
	private boolean suppressed = false;
	
	private byte [] tmp = new byte[4];
//	private static long currentTime = System.currentTimeMillis();
//	private static long previousTime = 0;
//	private static long interval = 1000;
	
	@Override
	public boolean takeControl() {
//		if(currentTime > previousTime + interval) {
//			previousTime = currentTime;
//			return true;
//		}
//		currentTime = System.currentTimeMillis();
//		return false;
		return ((System.currentTimeMillis()-Utils.startTime)%1000 == 0);	// ? true : false);
	}

	@Override
	public void action() {
		suppressed = false;
		
		LCD.drawString("I2C comm.", 0, 0);
		
		if (Utils.arduino.getData(0x18, tmp, 1) == -5) {
            LCD.drawString("Not connected", 0, 0);
        }
        else if (Utils.arduino.getData(0x18, tmp, 1) == -3) {
            LCD.drawString("Bus error", 0, 0);
        }
        else {
            //LCD.drawString("Name:    " + arduino.getProductID(), 0, 0);
            //LCD.drawString("Vendor:  " + arduino.getVendorID(), 0, 1);
            //LCD.drawString("Version: " + arduino.getVersion(), 0, 2);
        	LCD.drawString("Sonar 1: " + String.valueOf(Utils.myRobotPos.getSonar1Reading()), 0, 2);
        	LCD.drawString("Sonar 2: " + String.valueOf(Utils.myRobotPos.getSonar2Reading()), 0, 3);
            LCD.drawString("x: " + String.valueOf(Utils.myRobotPos.getActualX()), 0, 4);
            LCD.drawString("y: " + String.valueOf(Utils.myRobotPos.getActualY()), 0, 5);
            LCD.drawString("phi: " + String.valueOf(Utils.myRobotPos.getActualPhi()), 0, 6);
            
            //Real polling cycle            
            Utils.arduino.getData(0x19, tmp, 1);
            Utils.myRobotPos.setOmega(tmp[0]);
            Utils.arduino.getData(0x20, tmp, 1);
            Utils.myRobotPos.setDesSpeed(tmp[0]);
            Utils.arduino.getData(0x04, tmp, 4);
            Utils.myRobotPos.setDesPhi(Utils.toInt(tmp));
            
            Utils.arduino.sendData(0x16, Utils.toBytes(Utils.myRobotPos.getActualX()), 4);
            Utils.arduino.sendData(0x17, Utils.toBytes(Utils.myRobotPos.getActualY()), 4);
            Utils.arduino.sendData(0x18, Utils.toBytes(Utils.myRobotPos.getActualPhi()), 4);
            Utils.arduino.sendData(0x21, Utils.myRobotPos.getActualSpeed());
            Utils.arduino.sendData(0x22, Utils.toBytes(Utils.myRobotPos.getSonar1Reading()), 4);
            Utils.arduino.sendData(0x23, Utils.toBytes(Utils.myRobotPos.getSonar2Reading()), 4);
            
            Utils.myRobotPos.computePosition();
        }

        LCD.refresh();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
