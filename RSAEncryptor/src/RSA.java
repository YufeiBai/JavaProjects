
/**
 * RSA.java
 * RSA Encryption/Decryption object. 
 */

import java.math.BigInteger;
import java.util.Random;
 
public class RSA {
	private BigInteger p, q, N, phi, e, d;
	private int bits = 1024;
	private Random r;
	 
	public RSA() {
		// set up the random seed
		r = new Random();
		
		// generate primes
		p = BigInteger.probablePrime(bits, r);
		q = BigInteger.probablePrime(bits, r);
		
		// get our N, phi, and e
		N = p.multiply(q);
		phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		e = BigInteger.probablePrime(bits / 2, r);
		
		// make sure we dont get bugs
		while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
			e.add(BigInteger.ONE);
		}
		
		// find the mod inverse
		d = e.modInverse(phi);
	}
	 
	public RSA(BigInteger _e, BigInteger _d, BigInteger _N) {
		this.e = _e;
		this.d = _d;
		this.N = _N;
	}
	
	public static String bytesToString(byte[] encrypted) {
		String test = "";
		for (byte b : encrypted) {
			test += Byte.toString(b);
		}
		return test;
	}
	
	public String toString() {
		// get our data
		return "p: " + p + "\n"
				+ "q: " + q + "\n"
				+ "N: " + N + "\n"
				+ "phi: " + phi + "\n"
				+ "e: " + e + "\n"
				+ "d: " + d + "\n"
				+ "bits: " + bits + "\n";
	}
	
	public byte[] encrypt(byte[] message) {
		return (new BigInteger(message)).modPow(e, N).toByteArray();
	}
	
	public byte[] decrypt(byte[] message) {
		return (new BigInteger(message)).modPow(d, N).toByteArray();
	}
}
