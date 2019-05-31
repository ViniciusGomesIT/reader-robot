package br.com.vinicius.main;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.vinicius.robot.Robot;

public class MainRobot {
	private static final Logger log = LogManager.getLogger(MainRobot.class);

	public static void main(String[] args) {

		Robot robot = new Robot();
		
		try {
			robot.run();
			robot.finish();
		} catch (IOException e) {
			log.error(String.format("There was a error while trying to execute robot: %s", e.getMessage()));
			log.error(e);
			
			robot.finish();
		}
	}
}
