package org.usfirst.frc.team2849.robot;

import edu.wpi.first.wpilibj.Joystick;

public class LogitechFlightStick extends Joystick {

	public static final int BUTTON_Trigger = 1;
	public static final int BUTTON_Side = 2;
	public static final int BUTTON_BL3 = 3;
	public static final int BUTTON_BR4 = 4;
	public static final int BUTTON_TL5 = 5;
	public static final int BUTTON_TR6 = 6;
	public static final int BUTTON_Side7 = 7;
	public static final int BUTTON_Side8 = 8;
	public static final int BUTTON_Side9 = 9;
	public static final int BUTTON_Side10 = 10;
	public static final int BUTTON_Side11 = 11;
	public static final int BUTTON_Side12 = 12;

	public static final int AXIS_TILT_X = 0;
	public static final int AXIS_TILT_Y = 1;
	public static final int AXIS_ROTATE_Z = 2;
	public static final int AXIS_SLIDE = 3;

	public static final int POV_NONE = -1;
	public static final int POV_UP = 0;
	public static final int POV_DIAUPRIGHT = 45;
	public static final int POV_RIGHT = 90;
	public static final int POV_DIADOWNRIGHT = 135;
	public static final int POV_DOWN = 180;
	public static final int POV_DIADOWNLEFT = 225;
	public static final int POV_LEFT = 270;
	public static final int POV_DIAUPLEFT = 315;

	public LogitechFlightStick (int port) {
		super(port);
	}

	public boolean getButton(int buttonNumber) {
		return this.getRawButton(buttonNumber);
	}

	public double getAxis(int axisNumber) {
		return this.getRawAxis(axisNumber);
	}

	public double getAxisGreaterThan(int axisNumber, double greaterThan) {
		if(Math.abs(this.getRawAxis(axisNumber)) > greaterThan){
			return this.getRawAxis(axisNumber);
		}else{
			return 0;
		}
	}
	public double getXAxis() {
		return Math.pow(this.getAxisGreaterThan(AXIS_TILT_X, .1), 2) * Math.signum(this.getAxis(AXIS_TILT_X));
	}
	public double getYAxis() {
		return Math.pow(this.getAxisGreaterThan(AXIS_TILT_Y, .1), 2) * Math.signum(this.getAxis(AXIS_TILT_Y));
	}
	public double getZAxis() {
		return Math.pow(this.getAxisGreaterThan(AXIS_ROTATE_Z, .1), 2) * Math.signum(this.getAxis(AXIS_ROTATE_Z))*.5;
	}
	public boolean getAxisLessThan(int axisNumber, double lessThan) {
		return this.getRawAxis(axisNumber) < lessThan;
	}

	public boolean getDPad(int dPadNumber) {
		return this.getPOV(0) == dPadNumber;
	}
}