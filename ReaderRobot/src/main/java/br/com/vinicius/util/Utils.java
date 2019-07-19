package br.com.vinicius.util;

import static br.com.vinicius.enums.ErrorMessagesEnum.CLOSE_BUFFER_ERROR;
import static br.com.vinicius.enums.ErrorMessagesEnum.NULL_BUFFER;
import static br.com.vinicius.enums.ErrorMessagesEnum.GET_FILE_ERROR;
import static br.com.vinicius.enums.ErrorMessagesEnum.NULL_FILE_ERROR;
import static br.com.vinicius.enums.ErrorMessagesEnum.READ_LINE_ERROR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.vinicius.enums.UtilEnum;

public class Utils {
	
	private static final Logger log = LogManager.getLogger(Utils.class);
		
	private BufferedReader bufferReader;

	public String getCsvHeader() {		
		String separator = UtilEnum.CSV_SEPARATOR.getValue();
		
		return String.format("PARAMETER %s LINE %s FILE", separator, separator);
	}

	public String formatLineContent(String stringToSearch, int fileLine, String path) {
		String line;
		String contentSeparator = UtilEnum.TXT_SERPARATOR.getValue();

		if ( isOutputFileCsv() ) {
			contentSeparator = UtilEnum.CSV_SEPARATOR.getValue();
			
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
	
	public boolean isOutputFileCsv() {
		return UtilEnum.EXTENSION_OUTPUT_FILE.getValue().equalsIgnoreCase(".csv");
	}
	
	public BufferedReader getBufferFromFile(File file) throws FileNotFoundException {
		String message;
		
		if ( null != file ) {
			try {			
				this.bufferReader = new BufferedReader (new FileReader(file.getAbsolutePath()) );
			} catch (FileNotFoundException e) {
				message = String.format( GET_FILE_ERROR.getMessage(), file.getAbsolutePath() );
				log.error( message );
				log.error( e );
				throw new FileNotFoundException(message);
			}
		} else {
			log.error( NULL_FILE_ERROR.getMessage() );
			throw new IllegalArgumentException( NULL_FILE_ERROR.getMessage() );
		}
		
		return bufferReader;
	}
	
	public void closeBuffer(BufferedReader buffer) throws IOException  {
		String message;
		
		if ( null != buffer ) {
			try {
				buffer.close();
			} catch (IOException e) {
				message = String.format( CLOSE_BUFFER_ERROR.getMessage(), e.getCause() );
				log.error( message );
				log.error(e);
				throw new IOException( message );
			}
		} else {
			log.error( NULL_BUFFER.getMessage() );
			throw new IllegalArgumentException( NULL_BUFFER.getMessage() );
		}
	}
	
	public String readBufferLinesUpperCasedOrNull(BufferedReader buffer) throws IOException {
		try {
			
			if ( null != buffer && buffer.ready() ) {
				return buffer.readLine().toUpperCase().trim();
			} else {
				return null;
			}
		} catch (IOException e) {
			String message = String.format( READ_LINE_ERROR.getMessage(), e.getCause() );
			log.error( message );
			log.error(e);
			throw new IOException(message);
		}
	}

	public String getExitFileName() {		
		String exitFileName = UtilEnum.OUTPUT_FILE_NAME.getValue()
					.concat("_")
					.concat( LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy")) ) 
					.concat( UtilEnum.EXTENSION_OUTPUT_FILE.getValue() );
		
		return UtilEnum.BASE_URL_RESOURCES.getValue()
				.concat( UtilEnum.URL_RESOURCES.getValue() )
				.concat( exitFileName );
	}
}