package br.com.vinicius.main;

import java.io.IOException;

import br.com.vinicius.robot.Robot;

public class MainRobot {

	public static void main(String[] args) {

		Robot robot = new Robot();
		
		try {
			robot.run();
		} catch (IOException e) {
			throw new RuntimeException("Houve um erro no processamento do robot" + e);
		}
	}
}
