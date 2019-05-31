package br.com.vinicius.robot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.vinicius.enums.UtilEnum;
import br.com.vinicius.util.Util;

public class Robot {

	private static File URL_WORKSPACE = new File( UtilEnum.URL_WORKSPACE.getValue() );
	private static final Logger log = LogManager.getLogger(Robot.class);
	private SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy");
	
	private Util util = new Util();
	
	private String exitFileName = UtilEnum.OUTPUT_FILE_NAME.getValue()
			.concat("_")
			.concat( format.format(new Date()) ) 
			.concat(UtilEnum.EXTENSION_OUTPUT_FILE.getValue());
	
	private String exitFilePath = UtilEnum.BASE_URL_RESOURCES.getValue()
			.concat( UtilEnum.URL_RESOURCES.getValue() )
			.concat(exitFileName);
	
	private File exitFile = new File(exitFilePath);
	
	private StringBuilder contentBuild = new StringBuilder();
	
	private BufferedWriter writerOutputFile = null;
	private BufferedReader readerPathFile = null;
		
	public void run() throws IOException {		
		List<String> inputList = util.getInputDataParameters();
		List<File> allFilesList = util.getAllFiles(URL_WORKSPACE);
		
		for (String inputData : inputList) {			
			searchParameterInWorkSpace(allFilesList, inputData);
		}
	}

	private void searchParameterInWorkSpace(List<File> allFilesList, String stringToSearch) throws IOException {

		System.out.println( String.format("Searching for: %s", stringToSearch) );
		
		boolean isOutputFileCSV = util.isCsv();
		
		for (File file : allFilesList) {

			readerPathFile = new BufferedReader(new FileReader(file.getAbsolutePath()));
			int contLinha = 0;

			while ( readerPathFile.ready() ) {
				contLinha++;
				String linha = readerPathFile.readLine().trim().toUpperCase();

				if ( !linha.isEmpty() 
						&& !linha.startsWith("package") 
						&& !linha.startsWith("import")
						&& linha.contains(stringToSearch.toUpperCase()) ) {

					if ( isOutputFileCSV ) {
						contentBuild.append(util.formatLineContent(stringToSearch, contLinha, file.getPath(),
								UtilEnum.CSV_SEPARATOR.getValue()));
						contentBuild.append("\n");
					} else {
						contentBuild.append(util.formatLineContent(stringToSearch, contLinha, file.getPath(),
								UtilEnum.TXT_SERPARATOR.getValue()));
						contentBuild.append("\n");
					}

					System.out.println( String.format("Added: %s, Line: %s", file.getName(), contLinha) );
				}
			}			
		}
	}

	public void finish() {
		try {
			writerOutputFile = new BufferedWriter(new FileWriter(exitFile, true));
			
			if ( util.isCsv() ) {
				writerOutputFile.write(util.headerCsvConstruct());
				writerOutputFile.newLine();
			}
			
			writerOutputFile.write( contentBuild.toString() );
			
			readerPathFile.close();
			writerOutputFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println( ">>>>> Finished. <<<<<" );
	}
	
}