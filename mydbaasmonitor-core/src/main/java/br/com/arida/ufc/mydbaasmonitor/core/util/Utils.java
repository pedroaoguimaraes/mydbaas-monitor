package main.java.br.com.arida.ufc.mydbaasmonitor.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class Utils {
	
	private static final Logger logger = Logger.getLogger(Utils.class);
	
	public static String decoderText(String text) {
		try {
			byte[] bytes = text.getBytes("ISO-8859-1");
			text = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			return text;
		}

		return text;
	}

	public static String encrypt(String text) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
			byte messageDigest[] = algorithm.digest(text.getBytes("UTF-8"));

			StringBuilder hexBuild = new StringBuilder();

			for (byte b : messageDigest) {
				hexBuild.append(String.format("%02X", 0xFF & b));
			}

			return hexBuild.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}

		return text;
	}

	public static String i18n(String text) {
		try {
			return ResourceBundle.getBundle("messages", Locale.getDefault()).getString(text);
		} catch (MissingResourceException e) {
			return "???" + text + "???";
		}
	}
	
	public static boolean validateEmail(String email){
		
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");  
	   
	    Matcher m = p.matcher(email);  

	    boolean matchFound = m.matches();  
	   
	    if (matchFound){  
	    	return true;  
	    }else{  
	    	return false;
	    }		
	}
}
