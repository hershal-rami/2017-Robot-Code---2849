package org.usfirst.frc.team2849.robot;

import java.util.List;

// TODO driveDirection: 0 moves gear towards DS, 180 moves gear towards field
// TODO turnToAngle: 0 moves gear towards field, 180 moves gear towards DS
public class Autonomous implements Runnable {

	private static Drive drive;
	private static List<AutoMode> mode;
	private static Thread autoRunner = null;
	private static EndCondition ending = null;
	private static Boolean threadOneUse = false;
	private static StartPosition position = null;
	private static String team = null;
	private static final int GEAR_LIFT_TIME = 5000;

	@Override
	public void run() {
		AutoMode previousMode;
		AutoMode currentMode;
		// if there is no auto methods in the mode array, return
		if (mode == null || mode.size() == 0) {
			return;
		}
		previousMode = mode.get(0);
		// if there are still auto modes left in the array, continue to do them
		for (int i = 0; i < mode.size(); i++) {
			if (isKilled())
				break;
			currentMode = mode.get(i);
			switch (currentMode) {
			case CROSS:
				cross(previousMode);
				break;
			case SHOOT:
				shoot(previousMode);
				break;
			case GEAR:
				gear(previousMode, i);
				break;
			case NONE:
				break;
			default:
				// do nothing
				break;
			}
			previousMode = currentMode;
		}
		synchronized (threadOneUse) {
			threadOneUse = false;
		}
	} // end run

	// for crossing the line at the beginning of auto
	public void cross(AutoMode previousMode) {
		// if auto ends stop autoing
		if (isKilled()) {
			return;
		}
		if (previousMode == AutoMode.GEAR) {
		} else if (previousMode == AutoMode.CROSS) {
			if (position != StartPosition.CENTER) {
				// moves forward straight from the wall on the left or right
				// side, no gear
				drive.driveDirection(180, 3000);
			}
		} else {
		}

	} // end cross

	public void shoot(AutoMode previousMode) {
		if (isKilled()) {
			return;
		}
		if (previousMode == AutoMode.GEAR) {
			// get from gear to boiler and then ethod to dump fuel
			// remember boiler is on a different side depending on team
			// (right for red, left for blue?)
			if (team == "blue") {

			} else {

			}
		} else {
		}
	}

	public void gear(AutoMode previousMode, int i) {
		if (isKilled()) {
			return;
		}

		if (previousMode == AutoMode.GEAR) {
			// if we're doing gear and this is the 1st time (1st selector)
			if (i == 0) {
				if (position == StartPosition.LEFT) {
					wallToGear("left");
				} else if (position == StartPosition.RIGHT) {
					wallToGear("right");
				} else if (position == StartPosition.CENTER) {
					centerToGear();
				}

				Vision.setRunAutoAlign(true);

				// if not center & theres a second auto thats not gear,
				// straighten
				if (position != StartPosition.CENTER) {
					if (mode.size() > 1) {
						if (mode.get(1) != AutoMode.GEAR) {
							gearToStraight();
						}
					}
				}
			} else if (i == 1) {
				/*
				 * this is supposed to back the robot up and redo autoalign if
				 * we call gear twice for autonomous, then straightens out
				 */

				gearToGear();
				gearToStraight();
			}
		} else {
		}
	} // end of gear

	private Autonomous(EndCondition ending, List<AutoMode> mode, StartPosition position, String team, Drive drive) {
		Autonomous.ending = ending;
		Autonomous.mode = mode;
		Autonomous.position = position;
		Autonomous.drive = drive;
		Autonomous.team = team;
	}

	public static void auto(EndCondition ending, List<AutoMode> mode, StartPosition position, String team,
			Drive drive) {
		// creates a new auto thread and makes sure it doesn't get run more than
		// once
		synchronized (threadOneUse) {
			if (threadOneUse)
				return;
			threadOneUse = true;
		}
		autoRunner = new Thread(new Autonomous(ending, mode, position, team, drive), "auto");
		autoRunner.start();
	}

	enum AutoMode {
		CROSS, SHOOT, GEAR, NONE;
	}

	enum StartPosition {
		LEFT, RIGHT, CENTER
	}

	public static boolean isKilled() {
		return ending.done();
	}

	/**
	 * Backs the robot up and auto aligns twice again to attempt auto gear again
	 */
	public static void gearToGear() {
		drive.driveDirection(0, 300);
		Vision.setRunAutoAlign(true);
		Vision.setRunAutoAlign(true);
	}

	/**
	 * Moves the robot from the gear peg and orients it to make it straight,
	 * then moves forward
	 */
	public static void gearToStraight() {
		drive.driveDirection(0, 1000);
		drive.turnToAngle(180);
		drive.driveDirection(180, 100);
	}

	/**
	 * Moves the robot from the wall on the left or right to the gear peg by
	 * moving forward, turning to an angle depending on side, moving forward
	 * again, then auto aligning twice
	 * 
	 * @param side
	 *            String "left" or "right" that tells you which side peg the
	 *            robot is on. Used to determine which angle to turn to
	 */
	public static void wallToGear(String side) {
		// initial drive
		drive.driveDirection(180, 1750);
		// turn, angle depends on side: right or left
		if (side.equals("right")) {
			drive.turnToAngle(-42.5);
		} else if (side.equals("left")) {
			drive.turnToAngle(42.5);
		}
		// second drive towards peg
		drive.driveDirection(180, 750);

		// wait for the robot to stop rolling
		try {
			Thread.sleep(100);
		} catch (Exception e) {
		}

		// auto align twice, moves forward slightly after each auto align
		Vision.setRunAutoAlign(true);
		Vision.setRunAutoAlign(true);

		// waits for 5 seconds for the pilot to pick up the gear
		try {
			Thread.sleep(GEAR_LIFT_TIME);
		} catch (Exception e) {
		}
	}

	/**
	 * Moves the robot from the center wall to the gear by moving forward (3.5
	 * secs), waiting (2 seconds), moving backwards 3 inches (50 ms), and then
	 * stopping until auto ends
	 * 
	 */
	public static void centerToGear() {

		// initial drive forward from wall for 3.5 seconds
		drive.driveDirection(180, 3500);
		try {
			// waits two seconds so pilot can get gear or wait for readjustment
			Thread.sleep(2000);
		} catch (Exception e) {
		}

		// back of robot moves backwards for 50 ms, should be about 3 in
		/*
		 * this is in case the gear is put in too slanted and needs to get
		 * lifted up a bit by having the robot move back and letting the peg
		 * pull the gear forward
		 */
		drive.driveDirection(0, 100);

		// waits 5 seconds for driver to get peg
		try {
			Thread.sleep(GEAR_LIFT_TIME);
		} catch (InterruptedException e) {
		}
	}
}
