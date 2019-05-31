package br.com.vinicius.enums;

public enum UtilEnum {

	BASE_URL_RESOURCES(System.getProperty("user.dir")),
	URL_RESOURCES("\\src\\main\\resources\\"),
	URL_WORKSPACE("C:\\vivere_desenv\\financeira\\"),
	
	INPUT_FILE_NAME("input"),
	EXTENSION_INPUT_FILE(".txt"),
	
	OUTPUT_FILE_NAME("outPutFile"),
	EXTENSION_OUTPUT_FILE(".csv"),
	
	EXCLUSION_EXTENSIONS_FILE_NAME("exclusionExtensionFile"),
	EXTENSION_EXCLUSION_EXTENSIONS_FILE(".txt"),
	
	CSV_SEPARATOR(";"),
	TXT_SERPARATOR(": ");
	
	private UtilEnum(String value) {
		this.value = value;
	}

	public String value;

	public String getValue() {
		return value;
	}
}
