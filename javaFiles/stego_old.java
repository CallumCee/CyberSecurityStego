//import java.io.*;
//import java.util.*;
//
//class Steg
//{
//	/**
//	 * A constant to hold the number of bits per byte
//	 */
//	private final int byteLength=8;
//
//	/**
//	 * A constant to hold the number of bits used to store the header of the file extracted
//	 */
//	protected final int headerBitsLength=54;
//	/**
//	 * A constant to hold the number of bits used to store the size of the file extracted
//	 */
//	protected final int sizeBitsLength=32;
//	/**
//	 * A constant to hold the number of bits used to store the extension of the file extracted
//	 */
//	protected final int extBitsLength=64;
//
//
//
//	/**
//	 * Default constructor to create a steg object, doesn't do anything - so we actually don't need to declare it explicitly. Oh well. 
//	 */
//
//	public Steg()
//	{
//
//	}
//
//	/**
//	 * A method for hiding a string in an uncompressed image file such as a .bmp or .png
//	 * You can assume a .bmp will be used
//	 * @param cover_filename - the filename of the cover image as a string 
//	 * @param payload - the string which should be hidden in the cover image.
//	 * @return a string which either contains 'Fail' or the name of the stego image which has been 
//	 * written out as a result of the successful hiding operation. 
//	 * You can assume that the images are all in the same directory as the java files
//	 */
//	//TODO you must write this method
//	public String hideString(String payload, String cover_filename)
//	{
//
//
//		FileInputStream in = null;
//		FileOutputStream out = null;
//
//		String outName = "output.bmp";
//
//
//		// ---------- Input Cover File ----------
//
//		// Open Input File
//		try {
//			in = new FileInputStream(cover_filename);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			System.err.println("Unable to open " + cover_filename + ".");
//			return "Fail";
//		}
//
//		// Read Header
//		List<Integer> header = new ArrayList<Integer>();
//		try {
//			for(int i = 0; i <54; i++){
//				header.add(in.read());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("Unable to read header of " + cover_filename + ".");
//			return "Fail";
//		}
//
//		// Read Body
//		List<Integer> body = new ArrayList<Integer>();
//		int byt;
//		try {
//			while((byt = in.read()) != -1){
//				body.add(byt);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("Unable to read body of " + cover_filename + ".");
//			return "Fail";
//		}
//
//
//		// ---------- Prepare Secret Output ----------
//
//		// Convert Payload To Binary String (8 byte chunks)
//		String binaryPayload = "";
//		for(char c : payload.toCharArray()){
//			binaryPayload += Integer.toBinaryString(0x100 + c).substring(1);
//		}
//
//		// Convert Payload Size to Binary String (32 bits)
//		String binaryPayloadSize = Long.toBinaryString(0x100000000L + binaryPayload.length()).substring(1);
//
//		// No File Extension, Therefore 64 0's
//		String binaryExtension = String.format("%064d", 0);
//
//		// Secret Output
//		String binarySecretOutput = binaryPayloadSize + binaryExtension + binaryPayload;
//
//
//		// ---------- Output File ----------
//
//		// Create Output File
//		try {
//			out = new FileOutputStream(outName);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			System.err.println("Unable to create output file.");
//			return "Fail";
//		}
//
//		// Copy Header To Output File (Unchanged)
//		try {
//			for(int i : header){
//				out.write(i);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("Unable to write header to output file.");
//			return "Fail";
//		}
//
//		// Copy Body To Output File (Modified)
//		try {
//			int x = 0;
//			for(int i : body){
//				if(x < binarySecretOutput.length()){
//					i = swapLsb(binarySecretOutput.charAt(x++), i);
//				}
//				out.write(i);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("Unable to write modified body to output file.");
//			return "Fail";
//		}
//
//		try {
//			in.close();
//			out.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return "Fail";
//		}
//
//		//if successful then return the filename of outfile
//		return outName;
//	} 
//	//TODO you must write this method
//	/**
//	 * The extractString method should extract a string which has been hidden in the stegoimage
//	 * @param the name of the stego image 
//	 * @return a string which contains either the message which has been extracted or 'Fail' which indicates the extraction
//	 * was unsuccessful
//	 */
//	public String extractString(String stego_image)
//	{
//
//		// Create FileInputStream
//		FileInputStream in = null;
//		try {
//			in = new FileInputStream(stego_image);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//			System.err.println("Unable to open " + stego_image + ".");
//			return "Fail";
//		}
//
//		try {
//			in.skip(headerBitsLength);
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("Unable to skip header.");
//			return "Fail";
//		}
//
//		// Read In File Body
//		List<Integer> fileBytes = new ArrayList<Integer>();
//		int c;
//		try{
//			while((c = in.read()) != -1){
//				fileBytes.add(c);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("Unable to read body of " + stego_image + ".");
//			return "Fail";
//		}
//
//		//DO A CHECK HERE TO MAKE SURE fileBytes.size() > (sizeBitLength + extBitLength)
//		if (fileBytes.size() < (sizeBitsLength + extBitsLength)){
//			try {
//				in.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return "Fail";
//		}
//
//		// Extract String Size
//		String binaryStringSize = "0";
//		for(int i = 0; i < sizeBitsLength; i++){
//			binaryStringSize += extractLsb(fileBytes.remove(0));
//		}
//		int stringSize = Integer.parseInt(binaryStringSize,2);
//
//		// Extract Extension
//		String binaryExtension = "";
//		for(int i = 0; i < extBitsLength; i++){
//			binaryExtension += extractLsb(fileBytes.remove(0));
//		}
//
//		String extension = "";
//		for(int i = 0; i <= binaryExtension.length()-8; i += 8)
//		{
//			extension += (char)Integer.parseInt(binaryExtension.substring(i, i+8), 2);
//		}
//
//		//DO A CHECK HERE TO MAKE SURE fileBytes.size() >= stringSize
//		if (fileBytes.size() < stringSize){
//			try {
//				in.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return "Fail";
//		}
//
//		// Extract Secret Message
//		String binaryMessage = "";
//		for(int i = 0; i < stringSize; i++){
//			binaryMessage += extractLsb(fileBytes.remove(0));
//		}
//
//		String message = "";
//		for(int i = 0; i <= binaryMessage.length()-8; i += 8)
//		{
//			message += (char)Integer.parseInt(binaryMessage.substring(i, i+8), 2);
//		}
//
//
//		try {
//			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return "Fail";
//		}
//
//		return message;
//	}
//
//	//TODO you must write this method
//	/**
//	 * The hideFile method hides any file (so long as there's enough capacity in the image file) in a cover image
//	 * 
//	 * @param file_payload - the name of the file to be hidden, you can assume it is in the same directory as the program
//	 * @param cover_image - the name of the cover image file, you can assume it is in the same directory as the program
//	 * @return String - either 'Fail' to indicate an error in the hiding process, or the name of the stego image written out as a
//	 * result of the successful hiding process
//	 */
//	public String hideFile(String file_payload, String cover_image)
//	{
//		FileReader fr = new FileReader(file_payload);
//
//		// Open Cover Image
//		FileInputStream in = null;
//		try {
//			in = new FileInputStream(cover_image);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			System.err.println("Unable to open " + cover_image + ".");
//			return "Fail";
//		}
//
//		// Read Header
//		List<Integer> header = new ArrayList<Integer>();
//		try {
//			for(int i = 0; i <54; i++){
//				header.add(in.read());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("Unable to read header of " + cover_image + ".");
//			return "Fail";
//		}
//
//		// Read Body
//		List<Integer> body = new ArrayList<Integer>();
//		int byt;
//		try {
//			while((byt = in.read()) != -1){
//				body.add(byt);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("Unable to read body of " + cover_image + ".");
//			return "Fail";
//		}
//
//
//
//
//		// Create Output File
//		FileOutputStream out = null;
//		String outName = "output2.txt";
//		try {
//			out = new FileOutputStream(outName);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			System.err.println("Unable to create output file.");
//			return "Fail";
//		}
//
//		// Copy Header To Output File (Unchanged)
//		try {
//			for(int i : header){
//				out.write(i);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("Unable to write header to output file.");
//			return "Fail";
//		}
//
//		
//		// Copy Body To Output File (Modified)
//		try {
//			for(int i : body){
//				if (fr.hasNextBit()){
//					i = swapLsb(fr.getNextBit(),i);
//				}
//				out.write(i);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("Unable to write modified body to output file.");
//			return "Fail";
//		}
//
//		try {
//			in.close();
//			out.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return "Fail";
//		}
//
//		//if successful then return the filename of outfile
//		return outName;
//
//	}
//
//	//TODO you must write this method
//	/**
//	 * The extractFile method extracts any hidden file (so long as there was enough capacity in the image file) in a cover image
//	 * 
//	 * @param stego_image - the name of the hidden file to be extracted, you can assume it is in the same directory as the program
//	 * @return String - either 'Fail' to indicate an error in the extraction process, or the name of the file written out as a
//	 * result of the successful extraction process
//	 */
//	public String extractFile(String stego_image)
//	{
//
//		// Create FileInputStream
//		FileInputStream in = null;
//		try {
//			in = new FileInputStream(stego_image);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//			System.err.println("Unable to open " + stego_image + ".");
//			return "Fail";
//		}
//
//		try {
//			in.skip(headerBitsLength);
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("Unable to skip header.");
//			return "Fail";
//		}
//
//		// Read In File Body
//		List<Integer> fileBytes = new ArrayList<Integer>();
//		int c;
//		try{
//			while((c = in.read()) != -1){
//				fileBytes.add(c);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("Unable to read body of " + stego_image + ".");
//			return "Fail";
//		}
//
//		//DO A CHECK HERE TO MAKE SURE fileBytes.size() > (sizeBitLength + extBitLength)
//		if (fileBytes.size() < (sizeBitsLength + extBitsLength)){
//			try {
//				in.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return "Fail";
//		}
//
//		// Extract File Size
//		String binaryFileSize = "";
//		for(int i = 0; i < sizeBitsLength; i++){
//			binaryFileSize += extractLsb(fileBytes.remove(0));
//		}
//
//		int newFileSize = Integer.parseInt(binaryFileSize,2);
//
//		// Extract Extension
//		String binaryExtension = "";
//		for(int i = 0; i < extBitsLength; i++){
//			binaryExtension += extractLsb(fileBytes.remove(0));
//		}
//		String extension = "";
//		for(int i = 0; i <= binaryExtension.length()-8; i += 8)
//		{
//			extension += (char)Integer.parseInt(binaryExtension.substring(i, i+8), 2);
//		}
//		
//		System.out.println(newFileSize);
//
//
//		//Create outfile name
//		String outName = stego_image.substring(0, stego_image.length() - 4) + extension;
//		//Strip whitespace if any
//		outName = outName.replaceAll("\0", "");
//
//		//DO A CHECK HERE TO MAKE SURE fileBytes.size() >= stringSize
//		if (fileBytes.size() < newFileSize){
//			try {
//				in.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return "Fail";
//		}
//
//
//		//CREATE OUTPUT FILE
//		FileOutputStream out = null;
//
//		try {
//			out = new FileOutputStream("out");
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			System.err.println("Unable to create output file.");
//			return "Fail";
//		}
//
//
//		// Extract File
//
//		String byt = "";
//		//--------PROBLEM COULD LIE HERE?-----------
//		try{
//		for(int i = 1; i < newFileSize; i++){
//			byt += extractLsb(fileBytes.remove(0));
//			if(i%8 == 0){
//				out.write(Integer.parseInt(byt,2));
//				byt = "";
//			}
//			//out.write(0);
//		}
//		} catch (IOException e) {
//			e.printStackTrace();
//			return "Fail";
//		}
//
//		try {
//			in.close();
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return "Fail";
//		}
//
//		return outName;
//
//	}
//
//	//TODO you must write this method
//	/**
//	 * This method swaps the least significant bit with a bit from the filereader
//	 * @param bitToHide - the bit which is to replace the lsb of the byte of the image
//	 * @param byt - the current byte
//	 * @return the altered byte
//	 */
//	public int swapLsb(int bitToHide,int byt)
//	{
//		return (byt >> 1 << 1) + bitToHide;
//	}
//
//	/** This method extracts the LSB of a byte
//	 * 
//	 */
//	public int extractLsb(int byt){
//		return byt%2;
//	}
//}