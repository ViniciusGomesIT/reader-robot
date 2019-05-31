package br.com.vinicius.main;

import java.io.IOException;

import br.com.vinicius.robot.Robot;

public class MainRobot {

	public static void main(String[] args) {

		Robot robot = new Robot();
		
		try {
			robot.run();
			robot.finish();
		} catch (IOException e) {
			robot.finish();
			throw new RuntimeException( String.format("Houve um erro no processamento do robot. Cause %s, Message %s", e.getCause(), e.getMessage() ));
		}
	}
}
