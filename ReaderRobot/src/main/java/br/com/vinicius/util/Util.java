package br.com.vinicius.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.vinicius.enums.UtilEnum;

public class Util {
	
	private static final Logger log = LogManager.getLogger(Util.class);
		
	private List<File> allDirectories = new ArrayList<>();
	private BufferedReader bufferReader;
	private SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy");

	public List<File> getAllFiles() throws IOException {
		log.info("Getting all files list");
		
		File workSpace = new File( UtilEnum.URL_WORKSPACE.getValue() );
		List<String> invalidFileExtensions = getFileExtensions();
		List<File> directories = getAllDirectories(workSpace);

		List<File> allFilesList = new ArrayList<File>();

		for ( File directory : directories ) {

			for ( File file : directory.listFiles() ) {

				boolean isInvalidFile = invalidFileExtensions.stream().anyMatch(invalidExtension -> file
						.getAbsolutePath().toUpperCase().endsWith(invalidExtension.toUpperCase()));

				if ( !isInvalidFile && !file.isDirectory() ) {
					allFilesList.add(file);
				}
			}
		}

		log.info("done");
		return allFilesList;
	}
	
	public List<String> getFileExtensions() throws IOException {
		List<String> incorrectFileExtensions = new ArrayList<>();

		File exclusionExtensionFile = new File(UtilEnum.BASE_URL_RESOURCES.getValue()
				.concat(UtilEnum.URL_RESOURCES.getValue()).concat(UtilEnum.EXCLUSION_EXTENSIONS_FILE_NAME.getValue())
				.concat(UtilEnum.EXTENSION_EXCLUSION_EXTENSIONS_FILE.getValue()));

		BufferedReader bufferReader = this.getBufferFromFile(exclusionExtensionFile);
		String line;

		while ( null != (line = readBufferLinesUpperCasedOrNull(bufferReader))  ) {

				incorrectFileExtensions.add(line);
		}

		this.closeBuffer(bufferReader);
		return incorrectFileExtensions;
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
		log.info("Getting input parameteres");
		
		List<String> inputData = new ArrayList<>();
		
		File inputDataFile = new File( 
				UtilEnum.BASE_URL_RESOURCES.getValue()
				.concat( UtilEnum.URL_RESOURCES.getValue() )
				.concat( UtilEnum.INPUT_FILE_NAME.getValue() )
				.concat( UtilEnum.EXTENSION_INPUT_FILE.getValue() ));

		BufferedReader bufferReader = getBufferFromFile(inputDataFile);
		String line;
		
		while ( null != (line = readBufferLinesUpperCasedOrNull(bufferReader))  ) {

			inputData.add(line);
		}
		
		log.info("done");
		this.closeBuffer(bufferReader);
		return inputData;
	}

	public String getCsvHeader() {		
		String separator = UtilEnum.CSV_SEPARATOR.getValue();
		String header = String.format("PARAMETER %s LINE %s FILE", separator, separator, separator);
		
		return header;
	}

	public String formatLineContent(String stringToSearch, int fileLine, String path) {
		String line;

		if ( isOutputFileCsv() ) {
			line = String.format("[%s] %s [%s] %s [%s]", 
					stringToSearch.toUpperCase(), 
					UtilEnum.CSV_SEPARATOR.getValue(), 
					String.format("%010d", fileLine),
					UtilEnum.CSV_SEPARATOR.getValue(), 
					path);
		} else {
			line = String.format("[%s] %s Line: [%s] %s File: [%s]", 
					stringToSearch.toUpperCase(), 
					UtilEnum.TXT_SERPARATOR.getValue(), 
					String.format("%010d", fileLine),
					UtilEnum.TXT_SERPARATOR.getValue(), 
					path);
		}
		
		return line;
	}
	
	public boolean isOutputFileCsv() {
		if ( UtilEnum.EXTENSION_OUTPUT_FILE.getValue().equalsIgnoreCase(".csv") ) {
			return true;
		} 
		
		return false;
	}
	
	public BufferedReader getBufferFromFile(File file) throws FileNotFoundException {
		try {
			this.bufferReader = new BufferedReader (new FileReader(file.getAbsolutePath()) );
		} catch (FileNotFoundException e) {
			String message = String.format("There was a error while trying to get a file: %s", file.getAbsolutePath());
			log.error( message );
			log.error( e );
			throw new FileNotFoundException(message);
		}
		
		return bufferReader;
	}
	
	public void closeBuffer(BufferedReader buffer) throws IOException {
		try {
			buffer.close();
		} catch (IOException e) {
			String message = String.format("There was a error while trying to close buffer: %s", e.getCause());
			log.error( message );
			log.error(e);
			throw new IOException( message );
		}
	}
	
	public String readBufferLinesUpperCasedOrNull(BufferedReader buffer) throws IOException {
		try {
			
			if ( buffer != null && buffer.ready() ) {
				return buffer.readLine().toUpperCase().trim();
			} else {
				return null;
			}
		} catch (IOException e) {
			String message = String.format("There was a error while trying to read line of buffer", e.getCause());
			log.error( message );
			log.error(e);
			throw new IOException(message);
		}
	}

	public String getExitFileName() {		
		String exitFileName = UtilEnum.OUTPUT_FILE_NAME.getValue()
					.concat("_")
					.concat( format.format(new Date()) ) 
					.concat(UtilEnum.EXTENSION_OUTPUT_FILE.getValue());
		
		return UtilEnum.BASE_URL_RESOURCES.getValue()
				.concat( UtilEnum.URL_RESOURCES.getValue() )
				.concat(exitFileName);
	}
}