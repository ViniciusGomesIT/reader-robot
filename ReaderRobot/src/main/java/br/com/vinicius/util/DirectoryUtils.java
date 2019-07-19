package br.com.vinicius.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryUtils {
	
	private List<File> allDirectories = new ArrayList<>();
	
	public List<File> getAllDirectories(File directory) {
		this.allDirectories.clear();
		
		listDirectories(directory);

		return this.allDirectories;
	}
	
	public void listDirectories(File directory) {
		
		if ( isValidDirectory(directory) ) {
			
			this.allDirectories.add(directory);
			
			String[] subDirectory = directory.list();
			
			if ( subDirectory != null ) {
				for (String dir : subDirectory) {
					listDirectories( new File(directory + File.separator + dir) );
				}
			}
		}
	}

	public boolean isValidDirectory(File directory) {
		return directory.isDirectory() 
				&& !directory.getAbsolutePath().contains(".metadata")
				&& !directory.getAbsolutePath().contains(".recommenders")
				&& !directory.getAbsolutePath().contains(".settings")
				&& !directory.getAbsolutePath().contains(".git") 
				&& !directory.getAbsolutePath().contains("META-INF")
				&& !directory.getAbsolutePath().contains("target")
				&& !directory.getAbsolutePath().contains("vivere-app.cdc.db")
				&& !directory.getAbsolutePath().contains("\\src\\test\\")
				&& directory.canRead();
	}
}
