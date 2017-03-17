package games;

import java.io.*;

public class Games{
	
	public static battle batalla;
	
	public static void main(String args[]) {
		batalla = new battle();
		try {
			batalla.main();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}