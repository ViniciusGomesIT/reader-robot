package br.com.vinicius.robot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.vinicius.util.Util;

public class Robot {

	//CONFIGURAR OS DIRETÓRIOS E A STRING A PROCURAR
	private static File WORKSPACE = new File("E:\\WorkSpace Java\\workspace-1\\");
	
	private Util util = new Util();
	private File exitFile;
	
	private String exitFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\";
		
	public void run() throws IOException {
		
		List<String> inputList = util.getInputData();
		
		for (String inputData : inputList) {
			
			exitFile = new File(exitFilePath + inputData.trim() + ".txt");	
			
			searchStringInWorkSpace(WORKSPACE, inputData);
		}
	}

	private void searchStringInWorkSpace(File directory, String stringToSearch) throws IOException {

		List<File> allFilesList = new ArrayList<File>();
		allFilesList = util.getAllFiles(directory);

		for (File file : allFilesList) {
			BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
			int contLinha = 1;

			while (br.ready()) {
				String linha = br.readLine();
				
				if (linha.isEmpty() || linha == null) {					
					contLinha++;
					
					continue;
					
				} else if (linha.contains(stringToSearch)) {
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(exitFile, true));				

					bufferedWriter.write("[" + stringToSearch + "]" + " ");
					bufferedWriter.write("Line: " + util.getFormatedLineNumber(contLinha));
					bufferedWriter.write(" - ");
					bufferedWriter.write("FileName: " + file.getPath());
					bufferedWriter.newLine();

					bufferedWriter.close();
				}
				contLinha++;
			}
			br.close();
		}
	}
}