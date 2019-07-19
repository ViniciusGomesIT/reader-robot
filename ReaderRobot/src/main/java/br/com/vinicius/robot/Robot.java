package br.com.vinicius.robot;

import static br.com.vinicius.enums.ErrorMessagesEnum.FINISH_ROBOT_ERROR;
import static br.com.vinicius.enums.ErrorMessagesEnum.NO_IMPUT_PARAMETERS_FOUND_ERROR;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.vinicius.util.FileUtils;
import br.com.vinicius.util.Utils;

public class Robot {

	private static final Logger log = LogManager.getLogger(Robot.class);
	
	private Utils util = new Utils();
	private FileUtils fileUtils = new FileUtils();
	
	private String fullExitFileName = util.getExitFileName();
	
	private File exitFile = new File(fullExitFileName);
	private StringBuilder contentBuild = new StringBuilder();
	
	private BufferedReader bufferReader;
		
	public void run() throws IOException {
		log.info("Starting ReaderRobot");
		
		List<String> inputPamatersList = fileUtils.getInputDataParameters();
		
		if ( inputPamatersList.isEmpty() ) {			
			log.error( NO_IMPUT_PARAMETERS_FOUND_ERROR.getMessage() );
			throw new IllegalArgumentException( NO_IMPUT_PARAMETERS_FOUND_ERROR.getMessage() ) ;
		}
		
		List<File> allFiles = fileUtils.getAllFiles();
		
		for (String inputData : inputPamatersList) {			
			searchParameterInWorkSpace(allFiles, inputData);
		}
	}

	private void searchParameterInWorkSpace(List<File> allFilesList, String stringToSearch) throws IOException {

		log.info( String.format("Searching for: %s", stringToSearch) );

		for (File file : allFilesList) {

			bufferReader = this.util.getBufferFromFile(file);
			int numeroLinha = 0;
			String line;

			while ( null != (line = util.readBufferLinesUpperCasedOrNull(bufferReader)) ) {
				numeroLinha++;
				if ( !line.startsWith("package") 
						&& !line.startsWith("import")
						&& !line.isEmpty()
						&& line.contains( stringToSearch ) ) {

					contentBuild.append( util.formatLineContent(stringToSearch, numeroLinha, file.getPath()) );
					contentBuild.append("\n");
					
					log.info(String.format("Added: %s, Line: %s", file.getName(), numeroLinha));
				}
			}
		}
	}

	public void finish() {
		log.info("Trying to finish robot");
		
		try( BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(exitFile, true)) ) {
			
			if ( util.isOutputFileCsv() ) {
				bufferWriter.write(util.getCsvHeader());
				bufferWriter.newLine();
			}
			
			bufferWriter.write( contentBuild.toString() );			
			util.closeBuffer(bufferReader);
			
		} catch (IOException e) {
			log.error( String.format(FINISH_ROBOT_ERROR.getMessage(), e.getMessage()) );
			log.error(e);
		}
		
		log.info( ">>>>> Finished. <<<<<" );
	}
}