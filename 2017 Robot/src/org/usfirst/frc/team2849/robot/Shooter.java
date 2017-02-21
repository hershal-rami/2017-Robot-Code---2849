package org.usfirst.frc.team2849.robot;

/*
 * "What does IDE stand for?" -Hershal 
 * "Uhhh Integrated Development Environment. But I'm not sure, so don't quote me on that." -Charlie
 * "I'm gonna quote you on that." -Hershal
 * UPDATE: "Oh it is Integrated Development Environment! You can quote me on that now!" -Charlie
*/
import edu.wpi.first.wpilibj.Spark;

public class Shooter implements Runnable {

	private static Spark leftShooter = new Spark(7);
	private static Spark rightShooter = new Spark(6);

	private static Spark leftFeeder = new Spark(4);
	private static Spark rightFeeder = new Spark(5);

	private static Spark intake = new Spark(3);

	private static EndCondition ending = null;

	// TODO please clean up your code!!! -Sheldon
	// AnalogInput encoder = new AnalogInput(0);

	// PIDController pid = new PIDController(.1, .1, .1, encoder, upperShooter);

	private static double leftPower = 0.5;
	private static double rightPower = 0.5;

	private static boolean powerSet = true;
	private static Boolean shooterLock = false;

	private static Drive drive;

	/**
	 * Initialize a new shooter. Only use inside the Shooter class to pass into
	 * a new Thread().
	 */
	private Shooter(EndCondition ending) {
		rightShooter.setInverted(true);
		rightFeeder.setInverted(true);
		Shooter.ending = ending;
	}
	
	public static void shooterInit(Drive drive) {
		Shooter.drive = drive;
	}

	/**
	 * Run method for Shooter. Will run only ONCE after
	 * Shooter.shoot(EndCondition ending) is called.
	 */
	@Override
	public void run() {
		leftShooter.set(leftPower);
		rightShooter.set(rightPower);
		feed(true);
		while (!ending.done()) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		leftShooter.set(0);
		rightShooter.set(0);
		feed(false);
		synchronized (shooterLock) {
			System.out.println("Releasing shooter thread");
			shooterLock = false;
		}
	}

	/**
	 * Start the shooter thread. Automatically runs run() after starting the
	 * thread.
	 */
	public static void startShoot(EndCondition ending) {
		synchronized (shooterLock) {
			if (shooterLock) {
				return;
			}
			System.out.println("Taking shooter thread");
			shooterLock = true;
		}
		new Thread(new Shooter(ending), "shooter").start();
	}

	/**
	 * Sets power to one side according to the last call to switchPower().
	 * 
	 * @param power
	 *            Power to set the motor.
	 */
	public static void setPowerSided(double power) {
		if (powerSet) {
			setLeftPower(power);
		} else {
			setRightPower(power);
		}
	}

	public static void switchPower(boolean switched) {
		if (switched)
			powerSet = !powerSet;
	}

	/**
	 * Sets power to both motors.
	 * 
	 * @param power
	 *            Power to set the motors.
	 */
	public static void setPower(double power) {
		setLeftPower(power);
		setRightPower(power);
	}

	/**
	 * Sets the power of the left shooter motor.
	 * 
	 * @param leftPower
	 *            Power of the left motor. Should be in the range 0-1.
	 */
	public static void setLeftPower(double leftPower) {
		System.out.println("Left: " + leftPower);
		Shooter.leftPower = leftPower;
	}

	/**
	 * Sets the power of the right shooter motor.
	 * 
	 * @param rightPower
	 *            Power of the right motor. Should be in range 0-1.
	 */
	public static void setRightPower(double rightPower) {
		System.out.println("Right: " + rightPower);
		Shooter.rightPower = rightPower;
	}

	/**
	 * If the angle the joystick is at is within 30 degrees of the robot's
	 * heading, turn on ball intake.
	 * 
	 * @param xaxis
	 *            Reading from the x-axis of the joystick.
	 * @param yaxis
	 *            Reading from the y-axis of the joystick.
	 */
	public static void ballIntake(double xaxis, double yaxis) {
		try {
			if (Math.abs(joystickAngle(xaxis, yaxis) - (drive.getHeading())) <= 30) {
				intake.set(1.0);
			} else {
				intake.set(0.0);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	public static void ballIntake(double val) {
		intake.set(val);
	}

	/**
	 * Gets the joystick's x and y values and does arctan then converts to
	 * degrees from radians to find the joystick's angle
	 * 
	 * @param joyX
	 *            X-coordinate of the joystick
	 * @param joyY
	 *            Y-coordinate of the joystick
	 * @return The angle of the joystick
	 */
	public static double joystickAngle(double joyX, double joyY) {

		return Math.atan((-joyY / joyX) * (180 / Math.PI));
	}

	/**
	 * Clears the intake in case of a ball problem.
	 * 
	 * @param The
	 *            joystick
	 * 
	 */
	public static void clearIntake(LogitechFlightStick joy) {

		if (joy.getButton(11)) {
			intake.set(-1.0);
		}

		intake.set(0.0);
	}

	public static void feed(boolean feeding) {
		if (feeding) {
			leftFeeder.set(1);
			rightFeeder.set(1);
		} else {
			leftFeeder.set(0);
			rightFeeder.set(0);
		}
	}
}
