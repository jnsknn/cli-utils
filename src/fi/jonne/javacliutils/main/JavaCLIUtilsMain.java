package fi.jonne.javacliutils.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fi.jonne.javacliutils.core.Communicator;

public class JavaCLIUtilsMain {
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(true){
			Communicator.getInstance().handleInput(br.readLine());
		}
		
	}
}
