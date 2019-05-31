package br.com.vinicius.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.vinicius.enums.UtilEnum;

public class Util {
	
	private List<File> allDirectories = new ArrayList<File>();

	public List<String> getFileExtensions() {
		List<String> correctFileExtensions = new ArrayList<String>();
		
		File exclusionExtensionFile = new File(	UtilEnum.BASE_URL_RESOURCES.getValue()
				.concat( UtilEnum.URL_RESOURCES.getValue() )
				.concat( UtilEnum.EXCLUSION_EXTENSIONS_FILE_NAME.getValue() )
				.concat( UtilEnum.EXTENSION_EXCLUSION_EXTENSIONS_FILE.getValue()) );
		
		BufferedReader bufferInputExclusionExtensions;
		
		try {
			bufferInputExclusionExtensions = new BufferedReader( new FileReader(exclusionExtensionFile.getAbsolutePath()) );
			
			while ( bufferInputExclusionExtensions.ready() ) {
				String linha = bufferInputExclusionExtensions.readLine().trim();

				if ( !linha.isEmpty() ) {
					correctFileExtensions.add( linha );
				}
			}
			
			bufferInputExclusionExtensions.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return correctFileExtensions;
	}

	public List<File> getAllFiles(File Directory) {
		List<String> invalidFileExtensions = getFileExtensions();
		List<File> directories = getAllDirectories(Directory);

		List<File> allFilesList = new ArrayList<File>();

		for (File directory : directories) {

			for (File file : directory.listFiles()) {

				if ( !file.isDirectory() ) {

					boolean isInvalidFile = invalidFileExtensions.stream().anyMatch(
							invalidExtension -> file.getAbsolutePath().toUpperCase().endsWith(invalidExtension.toUpperCase()));

					if ( !isInvalidFile ) {
						allFilesList.add(file);
					}
				}
			}
		}

		return allFilesList;
	}

	public List<File> getAllDirectories(File directory) {
		this.allDirectories.clear();
		
		listDirectories(directory);

		return this.allDirectories;
	}

	public void listDirectories(File directory) {		
		
		if ( directory.isDirectory() 
				&& !directory.getAbsolutePath().contains(".metadata")
				&& !directory.getAbsolutePath().contains(".recommenders")
				&& !directory.getAbsolutePath().contains(".settings")
				&& !directory.getAbsolutePath().contains(".git") 
				&& !directory.getAbsolutePath().contains("META-INF")
				&& !directory.getAbsolutePath().contains("target")
				&& !directory.getAbsolutePath().contains("vivere-app.cdc.db")
				&& !directory.getAbsolutePath().contains("\\src\\test\\")
				&& directory.canRead() ) {
			
			this.allDirectories.add(directory);
			
			String[] subDirectory = directory.list();
			
			if ( subDirectory != null ) {
				for (String dir : subDirectory) {
					listDirectories( new File(directory + File.separator + dir) );
				}
			}
		}
	}

	public List<String> getInputDataParameters() throws IOException {
		List<String> inputData = new ArrayList<>();
		
		File inputDataFile = new File( 
				UtilEnum.BASE_URL_RESOURCES.getValue()
				.concat( UtilEnum.URL_RESOURCES.getValue() )
				.concat( UtilEnum.INPUT_FILE_NAME.getValue() )
				.concat( UtilEnum.EXTENSION_INPUT_FILE.getValue() ));

		BufferedReader br = new BufferedReader( new FileReader(inputDataFile.getAbsolutePath()) );

		while ( br.ready() ) {
			String linha = br.readLine().trim();

			if ( !linha.isEmpty() ) {
				inputData.add( linha );
			}
		}
		br.close();
		
		return inputData;
	}

	public boolean isCsv() {
		if ( UtilEnum.EXTENSION_OUTPUT_FILE.getValue().equalsIgnoreCase(".csv") ) {
			return true;
		} else {
			return false;
		}
	}

	public String headerCsvConstruct() throws IOException {		
		String separator = UtilEnum.CSV_SEPARATOR.getValue();
		String header = String.format("PARAMETER %s LINE %s FILE", separator, separator, separator);
		
		return header;
	}

	public String formatLineContent(String stringToSearch, int fileLine, String path, String contentSeparator) {
		String line;

		if ( isCsv() ) {
			line = String.format("[%s] %s [%s] %s [%s]", 
					stringToSearch.toUpperCase(), 
					contentSeparator, 
					String.format("%010d", fileLine),
					contentSeparator, 
					path);
		} else {
			line = String.format("[%s] %s Line: [%s] %s File: [%s]", 
					stringToSearch.toUpperCase(), 
					contentSeparator, 
					String.format("%010d", fileLine),
					contentSeparator, 
					path);
		}
		
		return line;
	}
}