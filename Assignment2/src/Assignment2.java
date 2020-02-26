/***********************************************************************************************************************
 *
 *  Pace University
 *  Spring 2020
 *  Algorithms and Computing Theory
 *
 *  Course: CS 242
 *  Team members: Karl Eshwer
 *  Collaborators: NONE
 *  References: https://www.bigocheatsheet.com/ (Used to answer question 4)
 *
 *  Assignment: 2
 *  Problem: Evaluate experimentally the time performance of two sorting algorithms on inputs that have many repeated
 *           items.
 *  Description: The purpose of solving this problem is to see how good/ bad the algorithms are if we know that the
 *               input has a lot of repetitions
 *
 *  Input: size of the input 'n' & expected number of repetitions 'r'
 *  Output: Sorted arrays
 *
 *  Visible data fields:
 *  -
 *
 *  Visible methods:
 *  public static void main(String[] args)
 *  public static void quickSort(ArrayList<Double> arr, int begin, int end)
 *  private static int partition(ArrayList<Double> arr, int begin, int end)
 *  private static void bucketSort(ArrayList<Double> intArr, int noOfBuckets)
 *  private static int hashf(int num)
 *
 *  Remarks
 *  ------
 *
 *  3)
 *                                  QuickSort
 *
 * |         | n1(300)    | n2(400)    | n3(500)    | n4(600)    | n5(700)    | n6(800)     |
 * |---------|------------|------------|------------|------------|------------|-------------|
 * | r1(400) | 1114308816 | 1249684289 | 1502794044 | 1253084630 | 2067427686 | 2991515504  |
 * | r2(500) | 780266341  | 1357586978 | 2908468814 | 3720973693 | 1783979132 | 4837005667  |
 * | r3(550) | 1098971855 | 1595270403 | 2121166985 | 3962781697 | 4798751858 | 3246748966  |
 * | r4(700) | 1511351120 | 3304317754 | 4195732983 | 4089742430 | 4838500762 | 6231480892  |
 * | r5(800) | 2845150885 | 4718093375 | 5188968937 | 5108721920 | 5284412241 | 8673089206  |
 * | r6(900) | 3581453321 | 4433120325 | 8913422818 | 7829431764 | 9701100043 | 1832101097  |
 *
 *
 *                                   BucketSort
 *
 * |         | n1(300)  | n2(400)  | n3(500)   | n4(600)   | n5(700)   | n6(800)   |
 * |---------|----------|----------|-----------|-----------|-----------|-----------|
 * | r1(400) | 58967968 | 55913868 | 66197552  | 93289534  | 142310106 | 86802341  |
 * | r2(500) | 62667706 | 58495181 | 102268595 | 87148027  | 86522906  | 113478136 |
 * | r3(550) | 54280809 | 61162743 | 108268323 | 84923070  | 115012388 | 113273783 |
 * | r4(700) | 62975721 | 88019064 | 87215599  | 118348340 | 423058271 | 133001113 |
 * | r5(800) | 82998703 | 90789802 | 127541166 | 409324890 | 141913961 | 134128051 |
 * | r6(900) | 94663942 | 77613254 | 114677678 | 448890567 | 134494837 | 127202770 |
 *
 *
 *  4) As seen in the two tables above it is clear that under these experimental circumstances of having repeating
 *     doubles in the array, BucketSort fairs better than QuickSort in terms of speed. This happens despite the fact that
 *     under the worst case both BucketSort and QuickSort have the same time complexity of O(n^2). Therefore, the repetitions
 *     do make a huge difference and this is why I theorize it happens:
 *
 *     Although BucketSort and QuickSort have the same worst case time complexity of O(n^2) their average case time
 *     complexities vary by quite a large margin. That is, in the average case, QuickSort has a time complexity O(n log(n))
 *     whereas BucketSort has an average case time complexity of O(n+k). Therefore, since we are working with such a large
 *     combination of numbers in the calculation of run times it is safe to assume that we are in the average case instead
 *     of the worst case as facilitated by the n and r.
 *
 *     Hence shown why the results in the above tables are expected as per our experiment.
 *
 *  5) Line 127.
 *
 **********************************************************************************************************************/

import java.util.*;

public class Assignment2 {

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        Random random = new Random();
        int n;
        int r;
        ArrayList <Double> arr1 = new ArrayList<>();
        ArrayList <Double> arr2 = new ArrayList<>();

        System.out.println("enter the size of the input n: ");
        n = scan.nextInt();
        System.out.println("enter the expected number of repetitions r: ");
        r = scan.nextInt();
        System.out.println();
        scan.close();

        for(int x = 0; x < n ;x++){
            double temp = Math.random();
            int ran = random.nextInt((2*r))+1;
            for (int j = 0 ;j < ran;j++){
                arr1.add(temp);
                arr2.add(temp);
            }
        }

        long startTimeQS = System.nanoTime();
        quickSort(arr1,0,arr1.size()-1);
        System.out.println("Time Taken by QuickSort = " + (System.nanoTime()-startTimeQS) + " nanosecs.\n");

        long startTimeBS = System.nanoTime();
        bucketSort(arr2,10);
        System.out.println("Time Taken by BucketSort = " + (System.nanoTime()-startTimeBS) + " nanosecs.\n");

    }

    /*Quick Sort*/

    public static void quickSort(ArrayList<Double> arr1, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr1, begin, end);

            quickSort(arr1, begin, partitionIndex-1);
            quickSort(arr1, partitionIndex+1, end);
        }
    }

    private static int partition(ArrayList<Double> arr1, int begin, int end) {

        //choose pivot at random
        Random rand = new Random();
        int num = begin + rand.nextInt(end - begin);

        Double pivot = arr1.get(num);
        int i = (begin-1);

        for (int j = begin; j < end; j++) {
            if (arr1.get(j) <= pivot) {
                i++;

                Double swapTemp = arr1.get(i);
                arr1.set(i, arr1.get(j));
                arr1.set(j, swapTemp);
            }
        }


        Double swapTemp = arr1.get(i + 1);
        arr1.set(i + 1, arr1.get(end));
        arr1.set(end, swapTemp);

        return i+1;
    }

    /*Bucket Sort*/

    private static void bucketSort(ArrayList<Double> arr2, int noOfBuckets){

        List<Integer>[] buckets = new List[noOfBuckets];
        for(int i = 0; i < noOfBuckets; i++){
            buckets[i] = new LinkedList<>();
        }

        for(double num : arr2){
            buckets[hashf((int) num)].add((int) num);
        }
        // sort buckets
        for(List<Integer> bucket : buckets){
            Collections.sort(bucket);
        }
        int i = 0;
        // merge buckets
        for(List<Integer> bucket : buckets){
            for(int num : bucket){
                arr2.set(i++, (double) num);
            }
        }
    }
    /* Hash Func. */
    private static int hashf(int num){
        return num/10;
    }
}



