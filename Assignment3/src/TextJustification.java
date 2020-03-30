/*******************************************************************************************************************************
 *
 *  Pace University
 *  Spring 2020
 *  Algorithms and Computing Theory
 *
 *  Course: CS 242
 *  Team members: Karl Eshwer
 *  Collaborators: NONE
 *  References: https://www.cs.carleton.edu/faculty/dmusican/cs117s03/iocheat.html (Used to refresh memory of file i/o)
 *
 *  Assignment: 3
 *  Problem: Text Justification.
 *  Description: Compare experimentally the result obtained justifying text with a greedy algorithm (as used by MS
 *				 Word) against doing the same using LATEX rules and Dynamic Programming.
 *
 *  Input: Number of words expected in output & Page width
 *  Output: Justified .txt file (just.txt) & Unjustified .txt file (unjust.txt) 
 *
 *  Visible data fields:
 *  -
 *
 *  Visible methods:
 *  public static int badness(String[] W, int i, int j, int width)
 *  public static int l(String[] W, int i, int j)
 *  public static ArrayList<Integer> split(int width, String[] W)
 *  public static ArrayList<Integer> MinimumBadness(String[] W, int width)
 *  private static int MemoizedMinmumBadness(String[] W, int i, int[] memo, int width)
 *  public static void justify(int width, String[] W, ArrayList<Integer> L) throws IOException
 *  public static void main(String[] args) throws IOException
 *  public static String getRandomString(int length)
 *
 *  Remarks
 *  ------
 *
 *  5) First, I counted the number of characters on a single line with the unjust.txt file opened in MS Word. I found that 
 *     each line holds the space for 78 characters. Following this I used the value of 78 as my entered page width and used the
 *     number of words as 15. Additionally, the string length I used was the same mentioned as in the specification which is 11 
 *     ("aaaaaaaaaaa").
 *     
 *     Then, I compared the results I received in the form of the just.txt file side by side with the unjust.txt file (now justified 
 *     in MS Word) and can confirm from this that MS Word does indeed use the greedy approach.
 *
 *****************************************************************************************************************************/

import java.io.File;
import java.io.FileWriter;
import java.util.Random;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.util.ArrayList;


public class TextJustification {


	//Badness method (1)

	public static int badness(String[] W, int i, int j, int width) {
		int temp = width - l(W, i, j); 
		if (temp >= 0)
			return (int) Math.pow(temp, 3);

		return Integer.MAX_VALUE;
	}

	public static int l(String[] W, int i, int j) {

		int num = 0;
		for (int x = i; x < j; x++) {
			num += W[x].length();
		}
		num += j - i - 1;
		return num;
	}

	//Split method (2)

	public static ArrayList<Integer> split(int width, String[] W) {
		return MinimumBadness(W, width);     
	}

	public static ArrayList<Integer> MinimumBadness(String[] W, int width) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		int[] memo = new int[W.length + 1];
		for (int i = 0; i < memo.length; i++) { 
			memo[i] = -1;
		}


		MemoizedMinmumBadness(W, 0, memo, width);
		for (int j = 0; j < W.length; j++) {
			list.add(memo[j]);
		}
		ArrayList<Integer> splitList = new ArrayList<Integer>();
		splitList.add(0);
		int i = 0;
		int indices = 1;

		while (indices < W.length) {

			boolean flag = (list.get(i) == badness(W, i, indices, width) + list.get(indices));
			if (flag) {
				splitList.add(indices);
				i = indices;
			}
			indices++;
		}

		return splitList; 
	}

	private static int MemoizedMinmumBadness(String[] W, int i, int[] memo, int width) {
		int n = memo.length;
		if (memo[i] > 0)
			return memo[i]; 
		if (i == n)
			memo[i] = 0; 
		else {
			int min = Integer.MAX_VALUE; 
			int temp;
			for (int j = i + 1; j < n; j++) { 
				temp = badness(W, i, j, width);
				temp += MemoizedMinmumBadness(W, j, memo, width);
				if (temp < min)
					min = temp;
			}
			memo[i] = min;
		}
		return memo[i];
	}

	//Justify method (3)

	public static void justify(int width, String[] W, ArrayList<Integer> L) throws IOException {

		File file = new File("just.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		BufferedWriter bw = new BufferedWriter(new FileWriter(file));


		for (int k = 0; k < L.size(); k++) {

			int i = L.get(k);
			int j;
			if (k == L.size() - 1) {
				j = W.length;
			}
			else {
				j = L.get(k + 1);
			}

			int spaces = width - l(W, i, j) + j - i - 1;

			int quotient;
			int remainder;

			if ((j - i) == 1) {
				remainder = 0;
				quotient = 0;
			} 
			else {
				quotient = spaces / (j - i - 1);
				remainder = spaces % (j - i - 1);
			}

			remainder += i;


			String str = new String();
			str += W[i];
			while (i < j - 1) {
				for (int a = 0; a < quotient; a++) {
					str += " ";
				}
				if (i < remainder)
					str += " ";

				i++;
				str += W[i];
			}

			bw.write(str);
			bw.newLine();
		}

		bw.close();
	}

	//Main method + RandomeString buffer method (4)

	public static String RandomString(int length) {						

		String str = "aaaaaaaaaaa";

		Random random = new Random();										
		StringBuffer buff = new StringBuffer();

		for (int i = 0; i < length; ++i) {									
			int number = random.nextInt(11);								
			buff.append(str.charAt(number));      							
		}

		return buff.toString();												
	}

	public static void main(String[] args) throws IOException {

		System.out.print("Please enter number of words: ");
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);

		int n = scan.nextInt(); 
		System.out.print("Please enter page width: ");
		int width = scan.nextInt();				 

		String[] W = new String[n];              

		int lengthW;                       
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			lengthW = random.nextInt(15) + 1;
			W[i] = RandomString(lengthW); 
		}

		ArrayList<Integer> L = split(width, W);  
		justify(width, W, L);					 


		File file = new File("unjust.txt");					  
		if (!file.exists()) {
			file.createNewFile();
		}
		String str = "";     
		for (int i = 0; i < W.length; i++) {
			str += W[i];
			if (i != W.length - 1) {
				str += " ";
			}
		}

		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(str);
		bw.close();
	}

}