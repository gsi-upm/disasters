package security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

/**
 * Calcula diferentes tipos de hash para un texto.
 *
 * @author Juan Luis Molina Nogales
 */
public class HashAlgorithm{
	/**
	 * Devuelde el hash MD5.
	 * 
	 * @param valor texto normal
	 * @return texto codificado
	 */
	public static String MD5(String valor){
		return Hash(valor, "", "MD5");
	}
	
	/**
	 * Devuelde el hash MD5.
	 * 
	 * @param valor texto normal
	 * @param sal sal para el texto
	 * @return texto codificado
	 */
	public static String MD5(String valor, String sal){
		return Hash(valor, sal, "MD5");
	}
	
	/**
	 * Devuelde el hash SHA-1.
	 * 
	 * @param valor texto normal
	 * @return texto codificado
	 */
	public static String SHA1(String valor){
		return Hash(valor, "", "SHA-1");
	}
	
	/**
	 * Devuelde el hash SHA-1.
	 * 
	 * @param valor texto normal
	 * @param sal sal para el texto
	 * @return texto codificado
	 */
	public static String SHA1(String valor, String sal){
		return Hash(valor, sal, "SHA-1");
	}
	
	/**
	 * Devuelde el hash SHA-256.
	 * 
	 * @param valor texto normal
	 * @return texto codificado
	 */
	public static String SHA256(String valor){
		return Hash(valor, "", "SHA-256");
	}
	
	/**
	 * Devuelde el hash SHA-256.
	 * 
	 * @param valor texto normal
	 * @param sal sal para el texto
	 * @return texto codificado
	 */
	public static String SHA256(String valor, String sal){
		return Hash(valor, sal, "SHA-256");
	}
	
	/**
	 * Devuelde el hash SHA-512.
	 * 
	 * @param valor texto normal
	 * @return texto codificado
	 */
	public static String SHA512(String valor){
		return Hash(valor, "", "SHA-512");
	}
	
	/**
	 * Devuelde el hash SHA-512.
	 * 
	 * @param valor texto normal
	 * @param sal sal para el texto
	 * @return texto codificado
	 */
	public static String SHA512(String valor, String sal){
		return Hash(valor, sal, "SHA-512");
	}
	
	/**
	 * Devuelde el hash.
	 * 
	 * IMPORTANTE!!
	 * Si se modifica el modo de insertar la sal
	 * o si se agrega un valor fijo a la encriptaci&oacute;n
	 * hay que cambiar las contrase&ntilde;as de la base de datos.
	 * 
	 * @param valor texto normal
	 * @param sal sal para el texto
	 * @param tipo tipo de algoritmo
	 * @return texto codificado
	 */
	private static String Hash(String valor, String sal, String tipo){
		String hash = "";
		try{
			MessageDigest md = MessageDigest.getInstance(tipo);
			byte[] valorHash = md.digest((sal + valor).getBytes("UTF-8")); // Encriptacion: sal + texto
			
			for(int i = 0; i < valorHash.length; i++){
				// %[flags][width]conversion
				hash += String.format("%02x", valorHash[i]); // 0: relleno de ceros, x: entero hexadecimal
			}
		}catch(NoSuchAlgorithmException ex){
			System.out.println("NoSuchAlgorithmExcepcion: " + ex);
		}catch(UnsupportedEncodingException ex){
			System.out.println("UnsupportedEncodingExcepcion: " + ex);
		}
		
		return hash;
	}
	
	/**
	 * Genera un texto pseudoaleatorio para agregar a la contrase&ntilde;a.
	 * 
	 * @param longitud longitud del texto
	 * @return texto aleatorio
	 */
	public static String newSalt(int longitud){
		Random rnd = new Random(new Date().getTime());
		//String abc = "abcdefghijklmnopqrstuvwxyz0123456789"; // abc.length() != 2^n
		//String salt = "";
		byte[] salt = new byte[longitud];
		for(int i = 0; i < longitud; i++){
			//salt += abc.charAt(rnd.nextInt(abc.length()));
			// Codigos ASCII
			int min = 0x21; // (!)
			int max = 0x7e; // (~)
			int[] exc = {0x22, 0x27, 0x5c}; // excepto ("), (') y (\)
			int size = max - min - exc.length + 1;
			int num = rnd.nextInt(size) + min;
			if(num < exc[0]){
				salt[i] = (byte)num;
			}else if(num < exc[1] - 1){
				salt[i] = (byte)(num + 1);
			}else if(num < exc[2] - 2){
				salt[i] = (byte)(num + 2);
			}else{
				salt[i] = (byte)(num + 3);
			}
		}
		return new String(salt);
	}
}