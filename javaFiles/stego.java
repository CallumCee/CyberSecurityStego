import java.io.*;
import java.util.*;

class Steg
{
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

		// --------- TEST ---------

		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			// Input and Output streams
			in = new FileInputStream("baboon.bmp");
			out = new FileOutputStream("output.bmp");

			// copy header to new file
			for(int i = 0; i < 54; i++){
				out.write(in.read());
			}
			
			//1 - Convert payload to binary string
			String binaryPayload = "";
			int curChar = 0;
			String curCharString = "";
			
			for (int i = 0; i < payload.length(); i++){
				curChar = (int) payload.charAt(i);
				//0 added to the front as function returns ascii as 7-bit binary
				curCharString = "0" + Integer.toBinaryString(curChar);
				binaryPayload += curCharString;
			}
			
			//2 - Obtain the 'size' figure
			int sizeToHide = binaryPayload.length();
			//2.5 - Create the binary of this and pad as necessary (32bit)
			String sizeString = Integer.toBinaryString(sizeToHide);
			//check how many bits need to be padded for 32bit
			int leftToPad = sizeBitsLength - sizeString.length();
			//TODO: add in failstate here if size is too large
			for(int i = 0; i < leftToPad; i++){
				sizeString = "0" + sizeString;
			}
			
			
			//3 - Obtain the 'extension'
			//3.5 - Create the binary of this and pad as necessary (64bit)
			//TODO: implement this for when a file extension is in use (currently set to pad)
			String extensionString = "";
			for (int i = 0; i < extBitsLength; i++){
				extensionString = "0" + extensionString;
			}
			
			
			//4 - apply these strings bit by bit to each byte for the 'extend-header'
			//4.5 - write this to file
			int currentByte;
			String curByteString;
			
			//size concat
			for (int i = 0; i < (sizeBitsLength); i++){
				currentByte = in.read();
				
				//break if end of file
				if (currentByte == -1){
					break;
				}
				
				//TODO: Swap LSB to sizeString.charAt(i);
				//TODO: write this byte out to file	
			}
			
			//extension concat
			for (int i = 0; i < (extBitsLength); i++){
				currentByte = in.read();
				
				//break if end of file
				if (currentByte == -1){
					break;
				}
				
				//TODO: Swap LSB to extensionString.charAt(i);
				//TODO: write this byte out to file	
			}
			
			
			//5 - write the binary_payload_string bit by bit to each byte
			//5.5 - loop until payload is written
			for (int i = 0; i < (sizeToHide); i++){
				currentByte = in.read();
				
				//break if end of file
				if (currentByte == -1){
					break;
				}
				
				//TODO: Swap LSB to binaryPayload.charAt(i);
				//TODO: write this byte out to file	
			}
			
			//6 - write the rest of the file as normal and close


//			// go through payload
//			int x = 1;
//			int c;
//			while ((c = in.read()) != -1) {
//
//				// get red byte (LSB)
//				if(x%3 == 0){
//					// get LSB and change to string bit
//					// this is only a test for now
//					out.write((byte) (c & ~(1 << 0)));
//				}else{
//					out.write(c);
//				}
//				x++;
//			}

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
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// --------- TEST ---------

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
		return -1;
	}
}