package br.com.vinicius.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {

	private List<File> allDirectories = new ArrayList<File>();

	public List<String> getFileExtensions() {
		List<String> correctFileExtensions = new ArrayList<String>();

		correctFileExtensions.add(".java");
		correctFileExtensions.add(".xml");
		correctFileExtensions.add(".yml");
		correctFileExtensions.add(".html");
		correctFileExtensions.add(".css");
		correctFileExtensions.add(".sql");
		correctFileExtensions.add(".xsd");
		correctFileExtensions.add(".js");
		correctFileExtensions.add(".vm");
		correctFileExtensions.add(".pco");

		return correctFileExtensions;
	}

	// @METHOD
	// Obtendo todos os arquivos com as extensões informadas
	public List<File> getAllFiles(File Directory) {
		List<File> allFilesList = new ArrayList<File>();

		// Obtendo todos os diretórios
		for (File directory : getAllDirectories(Directory)) {
			// Percorrendo todos os arquivos de cada diretório
			for (File file : directory.listFiles()) {
				// Percorrendo a lista de extensões à considerar
				for (String extension : getFileExtensions()) {
					// checando se o arquivo contém a extensão desejada
					if (file.getAbsolutePath().contains(extension)) {
						allFilesList.add(file);
					}
				}

			}
		}

		return allFilesList;
	}

	// @METHOD
	// Obtendo todos os diretórios
	public List<File> getAllDirectories(File directory) {
		this.allDirectories.clear();
		
		listDirectories(directory);

		return this.allDirectories;
	}

	// @METHOD
	// Listando todos os diretórios
	public void listDirectories(File directory) {		
		
		// Verificando se é um diretório e desconsiderando alguns
		if (directory.isDirectory() 
				&& !directory.getAbsolutePath().contains(".metadata")
				&& !directory.getAbsolutePath().contains(".recommenders")
				&& !directory.getAbsolutePath().contains(".settings")
				&& !directory.getAbsolutePath().contains(".git") 
				&& !directory.getAbsolutePath().contains("META-INF")
				&& !directory.getAbsolutePath().contains("target")
				&& !directory.getAbsolutePath().contains("vivere-app.cdc.db")
				&& !directory.getAbsolutePath().contains("\\src\\test\\")) {
		
			this.allDirectories.add(directory);
			
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
		int length = 5 - cont.length();

		if (length != 0) {
			for (int i = 0; i < length; i++) {
				cont = "0" + cont;
			}
		}

		return cont;
	}

	// @METHOD
	// Obtendo parâmetros de entrada a serem pesquisados
	public List<String> getInputDataParameters() throws IOException {
		List<String> inputData = new ArrayList<String>();
		File inputDataFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\" + "input.txt");

		BufferedReader br = new BufferedReader(new FileReader(inputDataFile.getAbsolutePath()));

		while (br.ready()) {
			String linha = br.readLine();

			if (linha.isEmpty() || linha == null) {
				continue;
			} else {
				inputData.add(linha.trim());
			}
		}
		
		return inputData;
	}
}