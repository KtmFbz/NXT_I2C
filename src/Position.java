import lejos.nxt.Motor;


public class Position {
	//---------------------------------------
		private int actualX = 0;
		private int actualY = 0;
		private int actualPhi = 0;
		private byte actualSpeed = 0;
		private byte omega = 0;
		private int desPhi = 0;
		private byte desSpeed = 0;
		private int sonar1Reading = 0;
		private int sonar2Reading = 0;
	//---------------------------------------
		
		public void resetPosition() {
			actualX = 0;
			actualY = 0;
			actualPhi = 0;
		}
		
		public void resetSpeed() {
			actualSpeed = 0;
			omega = 0;
		}
		
		//Sonar update
	    public void sonarUpdate(byte sonar) {
	    	switch(sonar) {
	    	case 1:
	    		this.sonar1Reading = Utils.sonar1.getDistance()*10;
	    		break;
	    	case 2:
	    		this.sonar2Reading = Utils.sonar2.getDistance()*10;
	    		break;
	    	default:
	    		this.sonar1Reading = Utils.sonar1.getDistance()*10;
	    		this.sonar2Reading = Utils.sonar2.getDistance()*10;
	    		break;
	    	}
	    }
	    
//	    public int getSonar(int sonar) {
//	    	switch(sonar) {
//	    	case 1:
//	    		return this.sonar1Reading;
//	    		break;
//	    	case 2:
//	    		return this.sonar2Reading;
//	    		break;
//	    	default:
//	    		return 0;
//	    		break;
//	    	}
//	    }
		
		public void computePosition() {
			actualPhi = (int) Utils.compass.getDegreesCartesian();
			actualX += (int) (Math.cos(actualPhi)*Utils.PI*
					(Motor.A.getTachoCount()*Utils.WHEEL_RADIUS + 
							Motor.B.getTachoCount()*Utils.WHEEL_RADIUS)/Utils.ENC_COUNT);
			actualY += (int) (Math.sin(actualPhi)*Utils.PI*
					(Motor.A.getTachoCount()*Utils.WHEEL_RADIUS + 
							Motor.B.getTachoCount()*Utils.WHEEL_RADIUS)/Utils.ENC_COUNT);
			
			actualSpeed = (byte) (Utils.WHEEL_RADIUS*Utils.PI*(Motor.A.getSpeed()+Motor.B.getSpeed())/(2*180));

	    	//Save sonar readings in MILLIMETERS
	    	sonar1Reading = Utils.sonar1.getDistance()*10;
	    	sonar2Reading = Utils.sonar2.getDistance()*10;
		}
		
	//Setters and getters	
		public int getActualX() {
			return actualX;
		}
		public void setActualX(int actualX) {
			this.actualX = actualX;
		}
		public int getActualY() {
			return actualY;
		}
		public void setActualY(int actualY) {
			this.actualY = actualY;
		}
		public int getActualPhi() {
			return actualPhi;
		}
		public void setActualPhi(int actualPhi) {
			this.actualPhi = actualPhi;
		}
		public byte getActualSpeed() {
			return actualSpeed;
		}
		public void setActualSpeed(byte actualSpeed) {
			this.actualSpeed = actualSpeed;
		}
		public int getDesPhi() {
			return desPhi;
		}
		public void setDesPhi(int desPhi) {
			this.desPhi = desPhi;
		}
		public byte getDesSpeed() {
			return desSpeed;
		}
		public void setDesSpeed(byte desSpeed) {
			this.desSpeed = desSpeed;
		}
		public byte getOmega() {
			return omega;
		}
		public void setOmega(byte omega) {
			this.omega = omega;
		}

		public int getSonar1Reading() {
			return this.sonar1Reading;
		}

		public int getSonar2Reading() {
			return sonar2Reading;
		}
		
}
