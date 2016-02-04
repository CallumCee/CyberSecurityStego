import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {

	public static void main(String[] args) throws IOException {
		Steg s = new Steg();
		//s.hideString("a", "white.bmp");
		System.out.println(s.hideString("this is a test message","baboon.bmp"));
		System.out.println(s.extractString("output.bmp"));
	}

}
