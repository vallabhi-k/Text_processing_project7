package project7;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Minhash {
  private static final int NUM_HASH_FUNCTIONS = 10000000;  // Number of hash functions (permutations)
  private static final int MIN_SHINGLE_LENGTH = 100000;  // Minimum length of shingles

  private static Set<Integer> generateShingles(String file) throws NoSuchAlgorithmException {
    Set<Integer> shingles = new HashSet<>();
    int fileLength = file.length();

    for (int i = 0; i <= fileLength - MIN_SHINGLE_LENGTH; i++) {
        String shingle = file.substring(i, i + MIN_SHINGLE_LENGTH);
        int shingleHash = getSHA1Hash(shingle);
        shingles.add(shingleHash);
    }

    return shingles;
}

private static int[][] generateMinhashMatrix(int numHashFunctions, Set<Integer> shinglesFile1, Set<Integer> shinglesFile2) {
    int[][] minhashMatrix = new int[numHashFunctions][2];
    Random random = new Random();

    for (int i = 0; i < numHashFunctions; i++) {
        int a = random.nextInt();
        int b = random.nextInt();
        minhashMatrix[i] = new int[] { a, b };
    }

    return minhashMatrix;
}

private static int countMatchingMinhashes(int[][] minhashMatrix) {
    int numMatches = 0;

    for (int i = 0; i < NUM_HASH_FUNCTIONS; i++) {
        int hash1 = minhashMatrix[i][0];
        int hash2 = minhashMatrix[i][1];
        if (hash1 == hash2) {
            numMatches++;
        }
    }

    return numMatches;
}

private static int getSHA1Hash(String input) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    byte[] hashedBytes = md.digest(input.getBytes());

    int hashValue = 0;
    for (int i = 0; i < 4; i++) {
        hashValue |= (hashedBytes[i] & 0xff) << (24 - 8 * i);
    }

    return hashValue;
}
  public  double jaccard(String fA, String fB) throws NoSuchAlgorithmException {
    /**
     * fA: Name of first file
     * fB: Name of second file
     */

    // Your code goes here 
    Set<Integer> shinglesFile1 = generateShingles(fA);
    Set<Integer> shinglesFile2 = generateShingles(fB);

    int[][] minhashMatrix = generateMinhashMatrix(NUM_HASH_FUNCTIONS, shinglesFile1, shinglesFile2);

    int numMatches = countMatchingMinhashes(minhashMatrix);
    double jaccardSimilarity = (double) numMatches / NUM_HASH_FUNCTIONS;

    return jaccardSimilarity;
    
  }
}



