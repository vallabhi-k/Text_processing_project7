package project7;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Minhash {
  public static int getSHA1Hash(String input) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    byte[] hashedBytes = md.digest(input.getBytes());

    // Convert the byte array to an integer
    int hashValue = 0;
    for (int i = 0; i < 4; i++) {
        hashValue |= (hashedBytes[i] & 0xff) << (24 - 8 * i);
    }

    return hashValue;
}


  public static int[] generateMinhashSignature(String fileContent, int numHashes) throws NoSuchAlgorithmException {
    Set<String> shingles = new HashSet<>();
    int[] minhashSignature = new int[numHashes];

    // Generate shingles from file content
    String[] words = fileContent.split("\\s+");
    for (int i = 0; i < words.length - 2; i++) {
        String shingle = words[i] + " " + words[i + 1];
        shingles.add(shingle);
    }

    // Generate hash functions
    Random random = new Random();
    int[][] hashFunctions = new int[numHashes][2];
    for (int i = 0; i < numHashes; i++) {
        int a = random.nextInt(100) + 1;
        int b = random.nextInt(100) + 1;
        hashFunctions[i] = new int[] { a, b };
    }

    // Generate Minhash signature
    for (int i = 0; i < numHashes; i++) {
        int minhash = Integer.MAX_VALUE;
        for (String shingle : shingles) {
            int shingleHash = getSHA1Hash(shingle);
            int hash = (hashFunctions[i][0] * shingleHash + hashFunctions[i][1]) % shingles.size();
            minhash = Math.min(minhash, hash);
        }
        minhashSignature[i] = minhash;
    }

    return minhashSignature;
}

  public  double jaccard(String fA, String fB) throws NoSuchAlgorithmException {
    /**
     * fA: Name of first file
     * fB: Name of second file
     */

    // Your code goes here 
    int numHashes = 10;
    int[] minhashSigFile1 = generateMinhashSignature(fA, numHashes);
    int[] minhashSigFile2 = generateMinhashSignature(fB, numHashes);

    // Calculate Jaccard similarity
    int intersection = 0;
    for (int i = 0; i < numHashes; i++) {
        if (minhashSigFile1[i] == minhashSigFile2[i]) {
            intersection++;
        }
    }
    int union = numHashes;

    double jaccardSimilarity = (double) intersection / union;

    return jaccardSimilarity;
    
  }
}
