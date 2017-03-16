package org.usfirst.frc.team2849.robot;
import org.usfirst.frc.team2849.robot.Autonomous.AutoMode;
import org.usfirst.frc.team2849.robot.Autonomous.StartPosition;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//my code works 10x as well as it used to, meaning it doesn't
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoSelector {

	public SendableChooser<String> autoModeChooser1;
	public SendableChooser<String> autoModeChooser2;
	public SendableChooser<String> sideChooser;
	public SendableChooser<String> cameraChooser;
	public SendableChooser<String> cameraChooser2;
	public SendableChooser<String> teamChooser;

	public void initialize(){
		//7-8 inches from the end of the tape (where it intersects w/ wall) to the closest bumper
		autoModeChooser1 = new SendableChooser<String>();
		autoModeChooser1.addDefault("1: Cross", "0");
		autoModeChooser1.addObject("1: Shoot", "1");
		autoModeChooser1.addObject("1: Gear", "2");
		autoModeChooser1.addObject("1: None", "3");
		SmartDashboard.putData("Auto Mode Chooser 1", autoModeChooser1);
		
		autoModeChooser2 = new SendableChooser<String>();
		autoModeChooser2.addDefault("2: Cross", "0");
		autoModeChooser2.addObject("2: Shoot", "1");
		autoModeChooser2.addObject("2: Gear", "2");
		autoModeChooser2.addObject("2: None", "3");
		SmartDashboard.putData("Auto Mode Chooser 2", autoModeChooser2);
		
		sideChooser = new SendableChooser<String>();
		sideChooser.addDefault("Left", "0");
		sideChooser.addObject("Center", "1");
		sideChooser.addObject("Right", "2");
		SmartDashboard.putData("Side Chooser", sideChooser);
		
		cameraChooser = new SendableChooser<String>();
		cameraChooser.addDefault("if starts on shooter/", "0");
		cameraChooser.addObject("if starts on gear/", "1");
		SmartDashboard.putData("Camera Chooser", cameraChooser);
		
		teamChooser = new SendableChooser<String>();
		teamChooser.addDefault("Blue Team", "0");
		teamChooser.addObject("Red Team", "1");
		SmartDashboard.putData("Team Chooser",teamChooser);
	}
	
	public AutoMode getAutoMode1(){
		String auto = autoModeChooser1.getSelected();
		if(auto.equals("0")){
			return AutoMode.CROSS;
		} else if(auto.equals("1")){
			return AutoMode.SHOOT;
		} else if(auto.equals("2")){
			return AutoMode.GEAR;
		} else {
			return AutoMode.NONE;
		}
	}
	
	public AutoMode getAutoMode2(){
		String auto = autoModeChooser2.getSelected();
		if(auto.equals("0")){
			return AutoMode.CROSS;
		} else if(auto.equals("1")){
			return AutoMode.SHOOT;
		} else if(auto.equals("2")){
			return AutoMode.GEAR;
		} else {
			return AutoMode.NONE;
		}
	}
	
	public StartPosition getStartPosition(){
		String auto = sideChooser.getSelected();
		if(auto.equals("0")){
			return StartPosition.LEFT;
		} else if(auto.equals("1")){
			return StartPosition.CENTER;
		} else {
			return StartPosition.RIGHT;
		}
	}
	
	public int getCameras(){
		String auto = cameraChooser.getSelected();
		if(auto.equals("0")){
			return 0;
		} else {
			return 1;
		}
	}
	
	public String getTeam(){
		String auto = teamChooser.getSelected();
		if(auto.equals("Blue Team")){
			return "blue";
		} else {
			return "red";
		}
	}
}
