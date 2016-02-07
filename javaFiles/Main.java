import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {

	public static void main(String[] args) throws IOException {
		Steg s = new Steg();
		//s.hideString("a", "white.bmp");
		System.out.println(s.hideString("this is a test message","baboon1.bmp"));
		System.out.println(s.extractString("hiddenString.bmp"));
		System.out.println(s.hideFile("../README.md", "baboon2.bmp"));
		System.out.println(s.extractFile("hiddenFile.bmp"));
	}

}
