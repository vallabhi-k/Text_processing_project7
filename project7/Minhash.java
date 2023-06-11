package project7;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class Minhash {

  public double calculateJaccardSimilarity(String fileA, String fileB) {
    ArrayList<String> file1Words  = readFile(fileA);
    ArrayList<String> file2Words = readFile(fileB);
    HashSet<Integer> file1Shingles = new HashSet<>();
    HashSet<Integer> file2Shingles = new HashSet<>();
    
    for (int i = 0; i < file1Words.size(); i++) {
      String shingle = file1Words.get(i);
      file1Shingles.add(shingle.hashCode());
    }

    for (int i = 0; i < file2Words.size(); i++) {
      String shingle = file2Words.get(i);
      file2Shingles.add(shingle.hashCode());
    }

    int totalShingles = file1Shingles.size() + file2Shingles.size();

    int maxShingleID = (int) Math.pow(2, 32) - 1;
    long nextPrime = 4294967311L;

    int numHashes = 99;

    List<Integer> a = pickRandomCoeff(numHashes, maxShingleID);
    List<Integer> b = pickRandomCoeff(numHashes, maxShingleID);
        
    List<Long> sigA = new ArrayList<>();
    List<Long> sigB = new ArrayList<>();

    List<Integer> file1ShinglesArr = new ArrayList<>(file1Shingles);
    List<Integer> file2ShinglesArr = new ArrayList<>(file2Shingles);
    
    for (int i = 0; i < numHashes; i++) {
      long minHash = nextPrime + 1;
      for (int j = 0; j < file1ShinglesArr.size(); j++) {
        long hash = (a.get(i) * file1ShinglesArr.get(j) + b.get(i)) % nextPrime;
        if (hash < minHash) {
          minHash = hash;
        }
      }
      sigA.add(minHash);
    }

    for (int i = 0; i < numHashes; i++) {
      long minHash = nextPrime + 1;
      for (int j = 0; j < file2ShinglesArr.size(); j++) {
        long hash = (a.get(i) * file2ShinglesArr.get(j) + b.get(i)) % nextPrime;
        if (hash < minHash) {
          minHash = hash;
        }
      }
      sigB.add(minHash);
    }

    int count = 0;
    for (int i = 0; i < numHashes; i++) {
      if (sigA.get(i).equals(sigB.get(i))) {
        count++;
      }
    }

    float similarity = (float) count / (float) numHashes;
    return similarity;
  }

  public static List<Integer> pickRandomCoeff(int k, int maxShingleID) {
    Set<Integer> randList = new HashSet<>();
    Random rand = new Random();
    
    while (randList.size() < k) {
      int randIndex = rand.nextInt(maxShingleID);
      randList.add(randIndex);
    }
    
    return new ArrayList<>(randList);
  }

  public static ArrayList<String> readFile(String fileName) {
    ArrayList<String> fileWords = new ArrayList<>();
    try {
      File file = new File(fileName);
     
      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        fileWords.add(scanner.nextLine());        
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return fileWords;
  }
}
