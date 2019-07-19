package br.com.vinicius.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.vinicius.enums.UtilEnum;

public class FileUtils {

	private static final Logger log = LogManager.getLogger(FileUtils.class);
	private static final String EMPTY_STRING = "";
	
	private Utils utils = new Utils();
	private DirectoryUtils directoryUtils = new DirectoryUtils();
	
	private BufferedReader bufferReader;
	
	public List<File> getAllFiles() throws IOException {
		log.info("Getting all files list");
		
		File workSpace = new File( UtilEnum.URL_WORKSPACE.getValue() );
		List<String> invalidFileExtensionsUpperCased = this.getFileExtensions();
		List<File> directories = directoryUtils.getAllDirectories(workSpace);

		List<File> allFilesList = new ArrayList<>();

		for ( File directory : directories ) {

			for ( File file : directory.listFiles() ) {

				boolean isValidFile = invalidFileExtensionsUpperCased.stream()
						.noneMatch( invalidExtension -> file.getAbsolutePath().toUpperCase().endsWith(invalidExtension) );

				if ( isValidFile && !file.isDirectory() ) {
					allFilesList.add(file);
				}
			}
		}

		log.info("done");
		return allFilesList;
	}
	
	public List<String> getFileExtensions() throws IOException {
		List<String> incorrectFileExtensions = new ArrayList<>();

		File exclusionExtensionFile = new File( UtilEnum.BASE_URL_RESOURCES.getValue()
				.concat( UtilEnum.URL_RESOURCES.getValue() )
				.concat( UtilEnum.EXCLUSION_EXTENSIONS_FILE_NAME.getValue() )
				.concat( UtilEnum.EXCLUSION_EXTENSIONS_FILE.getValue()) );

		bufferReader = utils.getBufferFromFile(exclusionExtensionFile);
		String line;

		while ( null != (line = utils.readBufferLinesUpperCasedOrNull(bufferReader)) ) {
			incorrectFileExtensions.add(line);
		}

		utils.closeBuffer(bufferReader);
		return incorrectFileExtensions;
	}

	
	public List<String> getInputDataParameters() throws IOException {
		log.info("Getting input parameteres");
		
		List<String> inputData = new ArrayList<>();
		
		File inputDataFile = new File( UtilEnum.BASE_URL_RESOURCES.getValue()
				.concat( UtilEnum.URL_RESOURCES.getValue() )
				.concat( UtilEnum.INPUT_FILE_NAME.getValue() )
				.concat( UtilEnum.EXTENSION_INPUT_FILE.getValue()) );

		bufferReader = utils.getBufferFromFile(inputDataFile);
		String line;
		
		while ( null != (line = utils.readBufferLinesUpperCasedOrNull(bufferReader))  ) {

			if ( line.startsWith("APPLICATION.") ) {
				line = line.replace("APPLICATION.", EMPTY_STRING);
			} 
			
			if ( line.startsWith("FINANCEIRA.") ) {
				line = line.replace("FINANCEIRA.", EMPTY_STRING);
			}
			
			inputData.add( line );
		}
		
		log.info("done");
		utils.closeBuffer(bufferReader);
		return inputData;
	}
}
