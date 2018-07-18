package br.com.vinicius.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {

	private List<File> allDirectories = new ArrayList<File>();

	public List<String> correctFileExtensions() {
		List<String> correctFileExtensions = new ArrayList<String>();

		correctFileExtensions.add(".java");
		correctFileExtensions.add(".xml");
		correctFileExtensions.add(".yml");
		correctFileExtensions.add(".html");
		correctFileExtensions.add(".css");
		correctFileExtensions.add(".jvm");
		correctFileExtensions.add(".xsd");

		return correctFileExtensions;
	}

	public List<File> getAllFiles(File Directory) {
		List<File> allFilesList = new ArrayList<File>();

		// obtendo todos os diretórios
		for (File directory : getAllDirectories(Directory)) {
			// percorrendo todos os arquivos de cada diretório
			for (File file : directory.listFiles()) {
				// percorrendo a lista de extensões à considerar
				for (String extension : correctFileExtensions()) {
					// checando se o arquivo contém a extensão desejada
					if (file.getAbsolutePath().contains(extension)) {
						allFilesList.add(file);
					}
				}

			}
		}

		return allFilesList;
	}

	public List<File> getAllDirectories(File directory) {
		listDirectories(directory);

		return this.allDirectories;
	}

	// @METHOD
	// Obtendo todos os diretórios
	public void listDirectories(File directory) {

		// desconsiderando pastas de configuração
		if (directory.isDirectory() 
				&& !directory.getAbsolutePath().contains(".metadata")
				&& !directory.getAbsolutePath().contains(".recommenders")
				&& !directory.getAbsolutePath().contains(".settings")
				&& !directory.getAbsolutePath().contains(".git") 
				&& !directory.getAbsolutePath().contains("META-INF")
				&& !directory.getAbsolutePath().contains("target")) {

			// considerando apenas os diretórios que contém src\main\java ou src\main\resources
			if (directory.getPath().contains("src\\main\\java") 
					|| directory.getPath().contains("src\\main\\resources") ) {
				allDirectories.add(directory);
			}

			String[] subDirectory = directory.list();

			if (subDirectory != null) {
				for (String dir : subDirectory) {
					listDirectories(new File(directory + File.separator + dir));
				}
			}
		}
	}

	public String getFormatedLineNumber(int contLinha) {

		String cont = String.valueOf(contLinha);
		int length = 4 - cont.length();

		if (length != 0) {
			for (int i = 0; i < length; i++) {
				cont = "0" + cont;
			}
		}

		return cont;
	}

	public List<String> getInputData() throws IOException {
		List<String> inputData = new ArrayList<String>();
		File inputDataFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\" + "input.txt");

		BufferedReader br = new BufferedReader(new FileReader(inputDataFile.getAbsolutePath()));

		while (br.ready()) {
			String linha = br.readLine();

			if (linha.isEmpty() || linha == null) {
				continue;
			} else {
				inputData.add(linha);
			}
		}
		
		return inputData;
	}
}