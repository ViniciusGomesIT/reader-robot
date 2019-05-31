package br.com.vinicius.robot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.vinicius.util.Util;

public class Robot {

	private static final Logger log = LogManager.getLogger(Robot.class);
	
	private Util util = new Util();
	private String fullExitFileName = util.getExitFileName();
	
	private File exitFile = new File(fullExitFileName);
	private StringBuilder contentBuild = new StringBuilder();
	
	private BufferedWriter bufferWriter;
	private BufferedReader bufferReader;
		
	public void run() throws IOException {
		log.info("Starting ReaderRobot");
		
		List<String> inputList = util.getInputDataParameters();
		List<File> allFilesList = util.getAllFiles();
		
		for (String inputData : inputList) {			
			searchParameterInWorkSpace(allFilesList, inputData);
		}
	}

	private void searchParameterInWorkSpace(List<File> allFilesList, String stringToSearch) throws IOException {

		log.info( String.format("Searching for: %s", stringToSearch) );
		
		boolean isOutputFileCSV = util.isOutputFileCsv();
		
		for (File file : allFilesList) {

			bufferReader = this.util.getBufferFromFile(file);
			int contLinha = 0;
			String line;

			while ( null != (line = util.readBufferLinesUpperCasedOrNull(bufferReader)) ) {
				contLinha++;

				if ( !line.startsWith("package") 
						&& !line.startsWith("import")
						&& line.contains(stringToSearch.toUpperCase()) ) {

					if ( isOutputFileCSV ) {
						contentBuild.append(util.formatLineContent( stringToSearch, contLinha, file.getPath() ));
						contentBuild.append("\n");
					} else {
						contentBuild.append(util.formatLineContent( stringToSearch, contLinha, file.getPath() ));
						contentBuild.append("\n");
					}
					
					log.info( String.format("Added: %s, Line: %s", file.getName(), contLinha) );
				}
			}			
		}
	}

	public void finish() {
		log.info("Trying to finish robot");
		
		try {
			bufferWriter = new BufferedWriter( new FileWriter(exitFile, true) );
			
			if ( util.isOutputFileCsv() ) {
				bufferWriter.write(util.getCsvHeader());
				bufferWriter.newLine();
			}
			
			bufferWriter.write( contentBuild.toString() );
			
			util.closeBuffer(bufferReader);
			bufferWriter.close();
		} catch (IOException e) {
			log.error( String.format("There was an error while trying to finish robot: %s", e.getMessage()) );
			log.error(e);
		}
		
		log.info( ">>>>> Finished. <<<<<" );
	}
}