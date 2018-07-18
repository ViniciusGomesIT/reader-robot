package br.com.vinicius.robot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import br.com.vinicius.util.Util;

public class Robot {

	private static File URL_WORKSPACE = new File("C:\\vivere_desenv\\financeira\\");
	
	private Util util = new Util();
		
	private String exitFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\" + "outPutFile.txt";
	private File exitFile = new File(exitFilePath);
		
	public void run() throws IOException {
		
		List<String> inputList = util.getInputData();
		List<File> allFilesList = util.getAllFiles(URL_WORKSPACE);
		
		for (String inputData : inputList) {
			
			searchStringInWorkSpace(allFilesList, inputData);
		}
	}

	private void searchStringInWorkSpace(List<File> allFilesList, String stringToSearch) throws IOException {		
		
		System.out.println("Searching for: " + stringToSearch);

		for (File file : allFilesList) {
			boolean added = false;
			BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
			int contLinha = 0;

			while (br.ready()) {
				contLinha++;
				String linha = br.readLine();
				
				if (linha.isEmpty() || linha == null) {					
					
					continue;
					
				} else if (linha.contains(stringToSearch)) {
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(exitFile, true));				

					bufferedWriter.write("[" + stringToSearch + "]");
					bufferedWriter.write(";");
					bufferedWriter.write("Line: " + util.getFormatedLineNumber(contLinha));
					bufferedWriter.write(";");
					bufferedWriter.write("FileName: " + file.getPath());
					bufferedWriter.newLine();

					bufferedWriter.close();
					added = true;
					System.out.println("Added: " + file.getName() + " Line: " + contLinha);
				}
			}
			br.close();
		}
	}
}