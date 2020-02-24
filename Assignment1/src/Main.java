
/*****************************************************************************************************************************
 *
 *  Pace University
 *  Spring 2020
 *  Algorithms and Computing Theory
 *
 *  Course: CS 242
 *  Team members: Karl Eshwer
 *  Collaborators: NONE
 *  References: To implement Kadane: https://en.wikipedia.org/wiki/Maximum_subarray_problem
 *
 *  Assignment: 1
 *  Problem: The Maximum Subarray Problem
 *  Description: It is the task of finding the contiguous subarray, within an array of numbers, that has the largest sum.
 *
 *  Input: a vector A of n numbers.
 *  Output: maximum subarray sum.
 *
 *  Visible data fields:
 *  -
 *
 *  Visible methods:
 *  public static int maximumSubArrayBF(int[] A)
 *  public static int maximumSubArray(int[] A, int low, int high)
 *  public static int maxCrossingSubarray(int[] A, int low, int mid, int high)
 *  public static int maxSubArraySumK(int[] A, int size)
 *  public static void main(String[] args)
 *
 *
 *   Remarks
 *   -------
 *
 *| Algorithm                                       | n = 10^3 | n = 5000 | n = 10^4 | n = 50000  | n = 10^5   |
 *|-------------------------------------------------|----------|----------|----------|------------|------------|
 *| 1) Brute Force- O(n^2) (nanoseconds)            | 11255250 | 31442680 | 88814394 | 1960258104 | 7868616485 |
 *| 2) Divide and conquer- O(n log n) (nanoseconds) | 1548232  | 2310650  | 3963930  | 7108936    | 21661966   |
 *| 3) Kadane- O(n) (nanoseconds)                   | 779200   | 968530   | 1053942  | 3284327    | 3356628    |
 *
 *
 * You need to show how your measurements prove that Brute Force is O(n2) and Divide and Conquer is O(nlogn) on these inputs.
 *
 * Brute Force: This algorithm runs in quadratic time because of the nested for loop being used which theoretically
 *              results in a time complexity of O(n^2). Additionally, as seen in the table above, this theoretical measurement
 *              of O(n^2) is confirmed by the quadratic increase in runtime's from 11255250 for an input of 10^3 to
 *              7868616485 for an input of 10^5! These values are also typically representative of the worst case because
 *              of the fact that the brute force approach looks at every POSSIBLE combination of the subarray before providing
 *              a result.
 *
 * Divide and Conquer : This algorithm incurs a time complexity of O(n log n) because: first it divides the problem into
 *                      sub-problems which in our case is the array of random numbers giving rise to the "log n" part of
 *                      the big-O equation as each time we recurse we divide the problem in half. It then "works on" these
 *                      sub-problems which in our case is addition of the sub-arrays to find the maximum contiguous sum sub-array
 *                      of the numbers. The algorithm does this "n" times and sets the appropriate values in "maxCrossingSubarray".
 *                      Finally, the algorithm combines the sub-problems and gives us the expected output in the form of
 *                      the totalSum. The theory behind this number is also confirmed as seen in the table above with the algorithm
 *                      exhibiting a slower growth in run times per each increased input as compared to that of the Brute
 *                      Force algorithm which we have seen to function in O(n^2). Furthermore per each increase in input
 *                      size we observe a slower growth(close to linear) as seen by the incremental growth with the difference
 *                      between n = 10^3 and n = 5000 being 762418 nanoseconds, n = 10^4 and n = 5000 being 1653280, n = 50000
 *                      and n = 10^4 being 15563253, and n = 10^5 and n = 50000 being 14553030 which falls within the bound of
 *                      experimental error.
 *
 ************************************************************************************************************************************/

import java.util.*;

class Algorithm1 {
    public static int maximumSubArrayBF(int[] A) { //Brute force: O(n2).

        int s_max = Integer.MIN_VALUE;

        for (int i = 1; i < A.length; i++) {
            int sum = 0;
            for (int j = i; j < A.length; j++) {
                sum = sum + A[j];
                s_max = Math.max(s_max, sum);
            }
        }

        return s_max;
    }
}

class Algorithm2 { // Divide and conquer: O(n log n).
    public static int maximumSubArray(int[] A, int low, int high) {
        if (low == high) {
            return A[low];
        }
            int mid = (low + high) / 2;
            int leftsum = (maximumSubArray(A, low, mid));
            int rightsum = (maximumSubArray(A, mid + 1, high));
            int crosssum = (maxCrossingSubarray(A, low, mid, high));
            int tempSum = Math.max(leftsum,rightsum);
            return Math.max(crosssum, tempSum);

    }

    public static int maxCrossingSubarray(int[] A, int low, int mid, int high) {

        int leftSum = Integer.MIN_VALUE;
        int sum = 0;

        for (int i = mid; i >= low; i--) {
            sum = sum + A[i];

            if (sum > leftSum) {
                leftSum = sum;
            }
        }

        int rightSum = Integer.MIN_VALUE;
        sum=0;

        for (int i = mid + 1; i <= high; i++) {
            sum = sum + A[i];
            if (sum > rightSum) {
                rightSum = sum;
            }
        }

        return (leftSum + rightSum);
    }
}

class Algorithm3 { //Kadane: O(n

    public static int maxSubArraySumK(int[] A) {

        int size = A.length;
        int s_max = Integer.MIN_VALUE;
        int last = 0;

        for (int i = 0; i < size; i++)
        {
            last = last + A[i];
            if (s_max < last){
                s_max = last;
            }
            if (last < 0) {
                last = 0;
            }

        }
        return s_max;
    }
}


public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random rd = new Random();
        System.out.print("Enter the size of the vector n: ");
        System.out.println("");

        int n = sc.nextInt();
        int[] a = new int[n+1];

        for (int i = 1; i <= n; i++) {
            a[i] = rd.nextInt(100) - 50;
        }

        //For Testing

/*        int maxSum = Algorithm1.maximumSubArrayBF(a);
        System.out.println("\nMaximum Brute Force sub array sum is: "+maxSum);

        int maxSumDC = Algorithm2.maximumSubArray(a, 0, n-1);
        System.out.println("\nMaximum DC sub array sum is: "+maxSumDC);

        int maxSumKad = Algorithm3.maxSubArraySumK(a);
        System.out.println("\nMaximum Kadane sub array sum is: "+maxSumKad);*/

        sc.close();

        long startTimeBF = System.nanoTime();
        Algorithm1.maximumSubArrayBF(a);
        System.out.println("Time Taken by Algorithm 1 (BruteForce) = " + (System.nanoTime()-startTimeBF) + " nanosecs.\n");

        long startTimeDC = System.nanoTime();
        Algorithm2.maximumSubArray(a, 0, n-1);
        System.out.println("Time Taken by Algorithm 2 (Divide & Conquer) = " + (System.nanoTime()-startTimeDC) + " nanosecs.\n");

        long startTimeKad = System.nanoTime();
        Algorithm3.maxSubArraySumK(a);
        System.out.println("Time Taken by Algorithm 3 (Kadane's Algorithm) = " + (System.nanoTime()-startTimeKad) + " nanosecs.\n");
    }

}







