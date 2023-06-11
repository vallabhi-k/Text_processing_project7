package project7;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class Minhash {

  public double jaccard(String fA, String fB) {
    ArrayList<String> file1_words  = readFile(fA);
    ArrayList<String> file2_words = readFile(fB);
    HashSet<Integer> file1_shingles = new HashSet<>();
    HashSet<Integer> file2_shingles = new HashSet<>();
    
    for (int i = 0; i < file1_words.size(); i++)
    {
      String shingle = file1_words.get(i);
      file1_shingles.add(shingle.hashCode());
    }

    for (int i = 0; i < file2_words.size(); i++)
    {
      String shingle = file2_words.get(i);
      file2_shingles.add(shingle.hashCode());
    }

    int total_shingles = file1_shingles.size() + file2_shingles.size();

    int maxShingleID = (int)Math.pow(2,32)-1;
    long nextPrime = 4294967311L;

    int num_hashes = 99;

    List<Integer> a = pickRandomCoeff(num_hashes, maxShingleID);
    List<Integer> b = pickRandomCoeff(num_hashes, maxShingleID);
        
    List<Long> sig_a = new ArrayList<>();
    List<Long> sig_b = new ArrayList<>();

    List<Integer> file1_shingles_arr = new ArrayList<Integer>(file1_shingles);
    List<Integer> file2_shingles_arr = new ArrayList<Integer>(file2_shingles);
    
        
    for (int i = 0; i < num_hashes; i++) {
      long minHash = nextPrime + 1;
      for (int j = 0; j < file1_shingles_arr.size(); j++) {
          long hash = (long)((long) (a.get(i) * file1_shingles_arr.get(j) + b.get(i)) % nextPrime);
          if (hash < minHash) {
              minHash = hash;
          }
      }
      sig_a.add(minHash);
    }

  for (int i = 0; i < num_hashes; i++) {
    long minHash = nextPrime + 1;
    for (int j = 0; j < file2_shingles_arr.size(); j++) {
        long hash = (long)((long) (a.get(i) * file2_shingles_arr.get(j) + b.get(i)) % nextPrime);
        if (hash < minHash) {
            minHash = hash;
        }
    }
    sig_b.add(minHash);
  }


  int count = 0;
  for (int i = 0; i < num_hashes; i++)
  {
    if (sig_a.get(i).equals(sig_b.get(i)))
    {
      count++;
    }
  }

  float sim = (float)(count) / (float)(num_hashes);
  return sim;


  //  return 0.0;
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

  public static ArrayList<String> readFile(String fileName){
    ArrayList<String> file_words = new ArrayList<>();
    try {
      File myObj = new File(fileName);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        file_words.add(myReader.nextLine());        
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return file_words;
  }

}