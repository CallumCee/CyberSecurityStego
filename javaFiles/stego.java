import java.io.*;
import java.util.*;

class Steg
{
	/**
	 * A constant to hold the number of bits per byte
	 */
	private final int byteLength=8;

	/**
	 * A constant to hold the number of bits used to store the header of the file extracted
	 */
	protected final int headerBitsLength=54;
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

	public Steg()
	{

	}

	/**
	 * A method for hiding a string in an uncompressed image file such as a .bmp or .png
	 * You can assume a .bmp will be used
	 * @param cover_filename - the filename of the cover image as a string 
	 * @param payload - the string which should be hidden in the cover image.
	 * @return a string which either contains 'Fail' or the name of the stego image which has been 
	 * written out as a result of the successful hiding operation. 
	 * You can assume that the images are all in the same directory as the java files
	 */
	//TODO you must write this method
	public String hideString(String payload, String cover_filename)
	{


		FileInputStream in = null;
		FileOutputStream out = null;


		// ---------- Input Cover File ----------

		// Open Input File
		try {
			in = new FileInputStream("baboon.bmp");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Unable to open " + cover_filename + ".");
			return "";
		}

		// Read Header
		List<Integer> header = new ArrayList<Integer>();
		try {
			for(int i = 0; i <54; i++){
				header.add(in.read());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Unable to read header of " + cover_filename + ".");
			return "";
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
			System.err.println("Unable to read body of " + cover_filename + ".");
			return "";
		}


		// ---------- Prepare Secret Output ----------

		// Convert Payload To Binary String (8 byte chunks)
		String binaryPayload = "";
		for(char c : payload.toCharArray()){
			binaryPayload += Integer.toBinaryString(0x100 + c).substring(1);
		}

		// Convert Payload Size to Binary String (32 bits)
		String binaryPayloadSize = Long.toBinaryString(0x100000000L + binaryPayload.length()).substring(1);

		// No File Extension, Therefore 64 0's
		String binaryExtension = String.format("%064d", 0);

		// Secret Output
		String binarySecretOutput = binaryPayloadSize + binaryExtension + binaryPayload;


		// ---------- Output File ----------

		// Create Output File
		try {
			out = new FileOutputStream("output.bmp");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Unable to create output file.");
			return "";
		}

		// Copy Header To Output File (Unchanged)
		try {
			for(int i : header){
				out.write(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Unable to write header to output file.");
		}

		// Copy Body To Output File (Modified)
		try {
			int x = 0;
			for(int i : body){
				if(x < binarySecretOutput.length()){
					i = swapLsb(binarySecretOutput.charAt(x++), i);
				}
				out.write(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Unable to write modified body to output file.");
		}


		return null;
	} 
	//TODO you must write this method
	/**
	 * The extractString method should extract a string which has been hidden in the stegoimage
	 * @param the name of the stego image 
	 * @return a string which contains either the message which has been extracted or 'Fail' which indicates the extraction
	 * was unsuccessful
	 */
	public String extractString(String stego_image)
	{

		FileInputStream in = null;
		try {

			// Input And Output Streams

			in = new FileInputStream("output.bmp");

			in.skip(54);

			String size = "";

			for(int i = 0; i < 32; i++){
				size += Integer.toBinaryString(0x100 + in.read()).substring(8);
			}

			int size1 = Integer.parseInt(size, 2);

			String extension = "";

			for(int i = 0; i < 64; i++){
				extension += Integer.toBinaryString(0x100 + in.read()).substring(8);
			}

			String recoveredText = "";
			String recoveredBinary = "";

			for(int i = 0; i < size1; i++){
				recoveredBinary += Integer.toBinaryString(0x100 + in.read()).substring(8);
			}

			for(int i = 0; i <= recoveredBinary.length()-8; i += 8)
			{
				recoveredText += (char)Integer.parseInt(recoveredBinary.substring(i, i+8), 2);
			}

			System.out.println(recoveredText);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}



		return null;
	}

	//TODO you must write this method
	/**
	 * The hideFile method hides any file (so long as there's enough capacity in the image file) in a cover image
	 * 
	 * @param file_payload - the name of the file to be hidden, you can assume it is in the same directory as the program
	 * @param cover_image - the name of the cover image file, you can assume it is in the same directory as the program
	 * @return String - either 'Fail' to indicate an error in the hiding process, or the name of the stego image written out as a
	 * result of the successful hiding process
	 */
	public String hideFile(String file_payload, String cover_image)
	{
		return null;
	}

	//TODO you must write this method
	/**
	 * The extractFile method extracts any hidden file (so long as there was enough capacity in the image file) in a cover image
	 * 
	 * @param stego_image - the name of the hidden file to be extracted, you can assume it is in the same directory as the program
	 * @return String - either 'Fail' to indicate an error in the extraction process, or the name of the file written out as a
	 * result of the successful extraction process
	 */
	public String extractFile(String stego_image)
	{
		return null;

	}

	//TODO you must write this method
	/**
	 * This method swaps the least significant bit with a bit from the filereader
	 * @param bitToHide - the bit which is to replace the lsb of the byte of the image
	 * @param byt - the current byte
	 * @return the altered byte
	 */
	public int swapLsb(int bitToHide,int byt)
	{
		return (byt >> 1 << 1) + bitToHide;
	}
}