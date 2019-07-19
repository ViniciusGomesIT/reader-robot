package br.com.vinicius.enums;

public enum ErrorMessagesEnum {

	NO_IMPUT_PARAMETERS_FOUND_ERROR("No input parameters found."),
	
	FINISH_ROBOT_ERROR("There was an error while trying to finish robot: %s"),
	
	GET_FILE_ERROR("There was a error while trying to get a file: %s"),
	NULL_FILE_ERROR("Cannot read a file because it's null"),
	
	CLOSE_BUFFER_ERROR("There was a error while trying to close buffer: %s"),
	NULL_BUFFER("Buffer is null"),
	
	READ_LINE_ERROR("There was a error while trying to read line of buffer: %s");
	
	private ErrorMessagesEnum(String message) {
		this.message = message;
	}

	private String message;

	public String getMessage() {
		return message;
	}
	
}
