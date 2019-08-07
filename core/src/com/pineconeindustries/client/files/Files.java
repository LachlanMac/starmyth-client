package com.pineconeindustries.client.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Files {
	
	public static String loadFile(String path, long checksum) {
		
		StringBuilder dataBuilder = new StringBuilder();
		
		try (BufferedReader br = new BufferedReader(new FileReader(path)))
	    {
	 
	        String sCurrentLine;
	        while ((sCurrentLine = br.readLine()) != null)
	        {
	        	dataBuilder.append(sCurrentLine);
	        }
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
		
		String data = dataBuilder.toString().trim();
		
		long fileCS = 0;
		
		for(int i = 0; i < data.length(); i++) {
			
			fileCS += data.charAt(i);
		}
		
		if(checksum != fileCS) {
			System.out.println("CHECKSUM FAILED  " + fileCS + "  VS  " + checksum);
		}
		
	    return dataBuilder.toString().trim();
		
	}
	
	


}
