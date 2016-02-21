import java.io.*;
import java.util.*;

class Steg{
	/**
	 * A constant to hold the number of bits per byte
	 */
	private final int byteLength=8;
	/**
	 * A constant to hold the number of bits used for the bmp header
	 */
	protected final int headerBitsLength = 54;
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

		FileInputStream in = null;
		FileOutputStream out = null;
		String outName = "hiddenString.bmp";
		File f = new File(cover_filename);
		
		// Open file streams
		in = openInputStream(cover_filename);
		out = openOutputStream(outName);
		
		// Check streams are working
		if (in == null) return "Fail - error opening input file stream";
		if (out == null) return "Fail - error opening output file stream";
		
		// Check if payloa is too large or file too small
		if((payload.length() * byteLength) > f.length() - 54){
			return "Fail - message too long or file too small";
		}
		
		// Copy header to output file
		if (copyHeader(in,out) == false) return "Fail - unable to copy header to file";

		// Populate ArrayList with bits
		ArrayList<Integer> binaryPayload = getBinaryPayload(payload);
		
		// Write payload to file
		int byt;
		try {
			// Copy entire file
			while((byt = in.read()) != -1){
				if(binaryPayload.size() != 0){
					out.write(swapLsb((binaryPayload.remove(0)).intValue(), byt));
				}else{
					out.write(byt);
				}
			}
		} catch (IOException e) {
			return "Fail - reading/writing error";
		}
		
		// Closing files
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			return "Fail - unable to close file streams";
		}

		return outName;
	} 

	//TODO you must write this method
	/**
	 * The extractString method should extract a string which has been hidden in the stegoimage
	 * @param the name of the stego image 
	 * @return a string which contains either the message which has been extracted or 'Fail' which indicates the extraction
	 * was unsuccessful
	 */
	public String extractString(String stego_image){
		FileInputStream in = null;
		String byt = "";
		String message = "";
		int messageSize = 0;
		
		// Open file stream
		in = openInputStream(stego_image);
		
		// Check stream is working
		if (in == null) return "Fail - error opening input file stream";

		// Skip image header
		try {
			in.skip(headerBitsLength);
		} catch (IOException e) {
			return "Fail - unable to skip the header";
		}

		// Get message size
		try {
			messageSize = getSize(in);
		} catch (IOException e1) {
			return "Fail - unable to obtain file size";
		}
		
		// Extracting string
		try {
			for(int i = 0; i < messageSize; i++){
				byt += extractLsb(in.read());
				if(byt.length() == 8){
					message += (char)Integer.parseInt(byt, 2);
					byt = "";
				}
			}
		} catch (IOException e) {
			return "Fail - unable to extract string";
		}

		// Closing File
		try {
			in.close();
		} catch (IOException e) {
			return "Fail - unable to close file";
		}

		return message;
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
		FileInputStream in = null;
		FileOutputStream out = null;
		File f_payload;
		File f_cover;

		// 	Open Cover Image
		in = openInputStream(cover_image);

		// Create Output File Stream
		out = openOutputStream("hiddenFile.bmp");
		
		// Check streams are working
		if (in == null) return "Fail - error opening input file stream";
		if (out == null) return "Fail - error opening output file stream";

		// Create Files
		f_payload = new File(file_payload);
		f_cover = new File(cover_image);
		
		// Check if payload file is too large or cover image too small
		if((f_payload.length() * byteLength) > f_cover.length() -54){
			return "Fail - file payload too large or cover image too small";
		}
		
		// Copy header
		if (copyHeader(in,out) == false) return "Fail - unable to copy header";

		// Write payload file to output file
		int byt;
		try {
			// Copy entire file
			while((byt = in.read()) != -1){
				if(fr.hasNextBit()){
					out.write(swapLsb(fr.getNextBit(),byt));
				}else{
					out.write(byt);
				}
			}
		} catch (IOException e) {
			return "Fail - unable to copy body";
		}

		// Closing Files
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			return "Fail - unable to close file";
		}
		return "hiddenFile.bmp";
	}

	//TODO you must write this method
	/**
	 * The extractFile method hides any file (so long as there's enough capacity in the image file) in a cover image
	 * @param stego_image - the name of the file to be hidden, you can assume it is in the same directory as the program
	 * @return String - either 'Fail' to indicate an error in the extraction process, or the name of the file written out as a
	 * result of the successful extraction process
	 */
	public String extractFile(String stego_image){

		FileInputStream in = null;
		FileOutputStream out = null;
		int fileSize = 0;
		String extension = "";

		// Open file stream
		in = openInputStream(stego_image);
		
		// Check stream is working
		if (in == null) return "Fail - error opening input file stream";

		// Skip Image Header
		try {
			in.skip(headerBitsLength);
		} catch (IOException e) {
			return "Fail - unable to skip header";
		}

		// Get file size
		try {
			fileSize = getSize(in);
		} catch (IOException e) {
			return "Fail - unable to obtain file size";
		}
		
		// Get extension
		try {
			extension = getExtension(in);
		} catch (IOException e) {
			return "Fail - unable to obtain file extension";
		}

		// Open output file stream
		out = openOutputStream(("output" + extension).replaceAll("\0", ""));
		
		// Check stream is working
		if (out == null) return "Fail - error opening output file stream";

		// Extract File
		String byt = "";
		try{
			for(int i = 0; i < fileSize; i++){
				byt += extractLsb(in.read());
				if(byt.length() == 8){
					byt = new StringBuilder(byt).reverse().toString();
					out.write(Integer.parseInt(byt,2));
					byt = "";
				}
			}
		} catch (IOException e) {
			return "Fail - unable to extract file from image";
		}


		// Closing Files
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			return "Fail - unable to close file";
		}
		
		// Return the name of the extracted file
		return ("output" + extension).replaceAll("\0", "");
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

	/**
	 * This method creates a new FileInputStream
	 * @param filename
	 * @return FileInputStream
	 */
	public FileInputStream openInputStream(String filename){
		try {
			return new FileInputStream(filename);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	/**
	 * This method creates a new FileOutputStream
	 * @param filename
	 * @return FileOutputStream
	 */
	public FileOutputStream openOutputStream(String filename){
		try {
			return new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	/**
	 * This method copies the header of one file to another
	 * @param in, out
	 * @return boolean - failed or passed
	 */
	public boolean copyHeader(FileInputStream in, FileOutputStream out){
		// Copy Header To Output File
		for(int i = 0; i < 54; i++){
			try {
				out.write(in.read());
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method gets the size hidden in a file
	 * @param in
	 * @return int - size
	 */
	public int getSize(FileInputStream in) throws IOException{
		String binarySize = "";
		for(int i = 0; i < sizeBitsLength; i++){
			binarySize += extractLsb(in.read());
		}
		return Integer.parseInt(binarySize,2);
	}

	/**
	 * This method gets the extension hidden in file
	 * @param in
	 * @return String - extension
	 */
	public String getExtension(FileInputStream in) throws IOException{
		String binaryExtension = "";
		// Read in bits
		for(int i = 0; i < extBitsLength; i++){
			binaryExtension += extractLsb(in.read());
		}
		String extension = "";
		// Covert bytes into characters
		for(int i = 0; i <= binaryExtension.length()-8; i += 8)
		{
			extension += (char)Integer.parseInt(binaryExtension.substring(i, i+8), 2);
		}
		return extension;
	}

	public ArrayList<Integer> getBinaryPayload(String payload){
		ArrayList<Integer> payLoadList = new ArrayList<Integer>();

		// Convert Payload To Binary String (8 byte chunks)
		String binaryPayload = "";
		for(char c : payload.toCharArray()){
			binaryPayload += Integer.toBinaryString(0x100 + c).substring(1);
		}

		// Convert Payload Size to Binary String (32 bits)
		String binaryPayloadSize = Long.toBinaryString(0x100000000L + binaryPayload.length()).substring(1);

		// Add bits to ArrayList
		for(char c : (binaryPayloadSize + binaryPayload).toCharArray()){
			payLoadList.add(Character.getNumericValue(c));
		}

		return payLoadList;
	}
}