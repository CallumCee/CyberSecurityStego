import java.io.*;
import java.util.*;

class Steg{
	/**
	 * A constant to hold the number of bits per byte
	 */
	private final int byteLength=8;

	/**
	 * A constant to hold the number of bits used to store the size of the file extracted
	 */
	protected final int sizeBitsLength=32;
	/**
	 * A constant to hold the number of bits used to store the extension of the file extracted
	 */
	protected final int extBitsLength=64;


	/**
	 * Default constructor to create a steg object, doesn't do anything - so we actually don't need to declare it explicitly. Oh well. 
	 */
	public Steg(){}

	//TODO you must write this method
	/**
	 * A method for hiding a string in an uncompressed image file such as a .bmp or .png
	 * You can assume a .bmp will be used
	 * @param cover_filename - the filename of the cover image as a string 
	 * param payload - the string which should be hidden in the cover image.
	 * @return a string which either contains 'Fail' or the name of the stego image which has been 
	 * written out as a result of the successful hiding operation. 
	 * You can assume that the images are all in the same directory as the java files
	 */
	public String hideString(String payload, String cover_filename){
		return null;
	} 

	//TODO you must write this method
	/**
	 * The extractString method should extract a string which has been hidden in the stegoimage
	 * @param the name of the stego image 
	 * @return a string which contains either the message which has been extracted or 'Fail' which indicates the extraction
	 * was unsuccessful
	 */
	public String extractString(String stego_image){
		return null;
	}

	//TODO you must write this method
	/**
	 * The hideFile method hides any file (so long as there's enough capacity in the image file) in a cover image
	 * @param file_payload - the name of the file to be hidden, you can assume it is in the same directory as the program
	 * @param cover_image - the name of the cover image file, you can assume it is in the same directory as the program
	 * @return String - either 'Fail' to indicate an error in the hiding process, or the name of the stego image written out as a
	 * result of the successful hiding process
	 */
	public String hideFile(String file_payload, String cover_image){

		FileReader fr = new FileReader(file_payload);

		// Open Cover Image
		FileInputStream in = null;
		try {
			in = new FileInputStream(cover_image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Unable to open " + cover_image + ".");
			return "Fail";
		}

		// Read Header
		List<Integer> header = new ArrayList<Integer>();
		try {
			for(int i = 0; i <54; i++){
				header.add(in.read());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Unable to read header of " + cover_image + ".");
			return "Fail";
		}

		// Read Body
		List<Integer> body = new ArrayList<Integer>();
		int byt;
		try {
			while((byt = in.read()) != -1){
				body.add(byt);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Unable to read body of " + cover_image + ".");
			return "Fail";
		}




		// Create Output File
		FileOutputStream out = null;
		String outName = "output.bmp";
		try {
			out = new FileOutputStream(outName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Unable to create output file.");
			return "Fail";
		}

		// Copy Header To Output File (Unchanged)
		try {
			for(int i : header){
				out.write(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Unable to write header to output file.");
			return "Fail";
		}

		ArrayList<Integer> payload = new ArrayList<Integer>();

		for(int i = 0; i < 96; i++){
			payload.add(fr.getNextBit());
		}

		String a = "";
		while(fr.hasNextBit()){
			a += "" + fr.getNextBit();
			if(a.length() == 8){
				a = new StringBuilder(a).reverse().toString();
				for(char c : a.toCharArray()){
					payload.add(Character.getNumericValue(c));
				}
				a = "";
			}
		}

		for(int i : payload){
			System.out.print(i);
		}
		// Copy Body To Output File (Modified)
		try {
			for(int i : body){
				if(!payload.isEmpty()){
					i = swapLsb(payload.remove(0),i);
				}
				out.write(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Unable to write modified body to output file.");
			return "Fail";
		}

		try {
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Fail";
		}

		//if successful then return the filename of outfile
		return outName;
	}

	//TODO you must write this method
	/**
	 * The extractFile method hides any file (so long as there's enough capacity in the image file) in a cover image
	 * @param stego_image - the name of the file to be hidden, you can assume it is in the same directory as the program
	 * @return String - either 'Fail' to indicate an error in the extraction process, or the name of the file written out as a
	 * result of the successful extraction process
	 */
	public String extractFile(String stego_image){
		//Create FileInputStream
		FileInputStream in = null;
		try {
			in = new FileInputStream(stego_image);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.err.println("Unable to open " + stego_image + ".");
			return "Fail";
		}

		try {
			in.skip(54);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Unable to skip header.");
			return "Fail";
		}

		// Read In File Body
		List<Integer> fileBytes = new ArrayList<Integer>();
		int c;
		try{
			while((c = in.read()) != -1){
				fileBytes.add(c);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Unable to read body of " + stego_image + ".");
			return "Fail";
		}


		System.out.println(fileBytes.size());

		// Extract File Size
		String binaryFileSize = "";
		for(int i = 0; i < 32; i++){
			binaryFileSize += extractLsb(fileBytes.remove(0));
		}
		

		int newFileSize = Integer.parseInt(binaryFileSize,2);
		System.out.println("\n" + newFileSize+1);
		// Extract Extension
		String binaryExtension = "";
		for(int i = 0; i < 64; i++){
			binaryExtension += extractLsb(fileBytes.remove(0));
		}
		String extension = "";
		for(int i = 0; i <= binaryExtension.length()-8; i += 8)
		{
			extension += (char)Integer.parseInt(binaryExtension.substring(i, i+8), 2);
		}



		//Create outfile name
		String outName = stego_image.substring(0, stego_image.length() - 4) + extension;
		//Strip whitespace if any
		outName = outName.replaceAll("\0", "");

		//DO A CHECK HERE TO MAKE SURE fileBytes.size() >= stringSize
		if (fileBytes.size() < newFileSize){
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "Fail";
		}


		//CREATE OUTPUT FILE
		FileOutputStream out = null;

		try {
			out = new FileOutputStream("out.md");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Unable to create output file.");
			return "Fail";
		}

System.out.println();
		// Extract File

		String byt = "";
		//--------PROBLEM COULD LIE HERE?-----------
		try{
			for(int i = 0; i < newFileSize; i++){
				byt += extractLsb(fileBytes.remove(0));
				if(byt.length() == 8){
					System.out.println(byt);
					out.write(Integer.parseInt(byt,2));
					byt = "";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "Fail";
		}

		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "Fail";
		}

		return "";
	}

	//TODO you must write this method
	/**
	 * This method swaps the least significant bit with a bit from the filereader
	 * @param bitToHide - the bit which is to replace the lsb of the byte of the image
	 * @param byt - the current byte
	 * @return the altered byte
	 */
	public int swapLsb(int bitToHide,int byt){		
		return (byt >> 1 << 1) + bitToHide;
	}

	/**
	 * This method extracts the least significant bit of a byte
	 * @param by
	 * @return least significant bit
	 */
	public int extractLsb(int byt){
		return byt%2;
	}
}