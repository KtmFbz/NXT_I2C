import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;


public class Utils {
	  
	//System constants------------------------------------
	static final int ARDUINO_ADDRESS = 0x01 << 1;
	static final double PI = 3.14159265359;
	static final int WHEEL_RADIUS = 28;	//millimeters
	static final int TRACK = 120;			//millimeters
	static final int ENC_COUNT = 360;
	static final double KAPPA_EPS = 0.1;	//Orientation P-controller gain
	//----------------------------------------------------
	  
	//Sensor definition-----------------------------------
//	static final I2CSensor arduino = new I2CSensor(SensorPort.S4, ARDUINO_ADDRESS, 
//												   I2CSensor.TYPE_LOWSPEED, I2CPort.LEGO_MODE);
	static final I2CSensor arduino = new I2CSensor(SensorPort.S4, I2CPort.LEGO_MODE);
	static final UltrasonicSensor sonar1 = new UltrasonicSensor(SensorPort.S2);
	static final UltrasonicSensor sonar2 = new UltrasonicSensor(SensorPort.S3);
	static final CompassHTSensor compass = new CompassHTSensor(SensorPort.S1);
	//----------------------------------------------------

    //System variables------------------------------------
    public static Position myRobotPos = new Position();
    
    private static byte tmp[] = new byte [1];
	private static long currentTime = System.currentTimeMillis();
	private static long previousTime = 0;
	private static long interval = 1000;
	public static long startTime;
    //----------------------------------------------------
    
    //Data conversion-------------------------------------
    //From byte to int
    public static int toInt(byte[] bytes) {
    	return 	(bytes[0]<<24)&0xff000000|
    		    (bytes[1]<<16)&0x00ff0000|
    		    (bytes[2]<< 8)&0x0000ff00|
    		    (bytes[3]<< 0)&0x000000ff;
    }
    
    //From int to byte array
    public static byte[] toBytes(int i) {
      byte[] result = new byte[4];

      result[0] = (byte) (i >> 24);
      result[1] = (byte) (i >> 16);
      result[2] = (byte) (i >> 8);
      result[3] = (byte) (i /*>> 0*/);

      return result;
    }
    //----------------------------------------------------

    public static boolean getInterval() {
		currentTime = System.currentTimeMillis();
		
		//If the time stamp is right return true, given that the slave device is reachable
		if(currentTime - previousTime > interval) {
			previousTime = currentTime;
			return true && (!(arduino.getData(0x18, tmp, 1) == -5) || !(arduino.getData(0x18, tmp, 1) == -3));
		}
		else
			return false;
    }
}
