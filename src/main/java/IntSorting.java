import java.util.*;

/**
 * Comparison of sorting methods. The same array of non-negative int values is
 * used for all methods.
 *
 * @author Jaanus
 * @version 3.0
 * @since 1.6
 */
public class IntSorting {

   /** maximal array length */
   static final int MAX_SIZE = 512000;

   /** number of competition rounds */
   static final int NUMBER_OF_ROUNDS = 4;

   /**
    * Main method.
    *
    * @param args
    *           command line parameters
    */
   public static void main(String[] args) {
      int numberRolls = 10;
      long[][] in_sort = new long[NUMBER_OF_ROUNDS][numberRolls];
      long[][] bin_in_sort = new long[NUMBER_OF_ROUNDS][numberRolls];
      long[][] quicksort = new long[NUMBER_OF_ROUNDS][numberRolls];
      long[][] java_api_sort = new long[NUMBER_OF_ROUNDS][numberRolls];
      long[][] radix_sort = new long[NUMBER_OF_ROUNDS][numberRolls];
      for (int x = 0; x < numberRolls; x++) {
         final int[] origArray = new int[MAX_SIZE];
         Random generator = new Random();
         for (int i = 0; i < MAX_SIZE; i++) {
            origArray[i] = generator.nextInt(1000);
         }
         int rightLimit = MAX_SIZE / (int) Math.pow(2., NUMBER_OF_ROUNDS);

         // Start a competition
         for (int round = 0; round < NUMBER_OF_ROUNDS; round++) {
            int[] acopy;
            long stime, ftime, diff;
            rightLimit = 2 * rightLimit;
            System.out.println();
            System.out.println("Length: " + rightLimit);

            acopy = Arrays.copyOf(origArray, rightLimit);
            stime = System.nanoTime();
            insertionSort(acopy);
            ftime = System.nanoTime();
            diff = ftime - stime;
            in_sort[round][x] = diff;
            System.out.printf("%34s%11d%n", "Insertion sort: time (ms): ", diff / 1000000);
            checkOrder(acopy);

            acopy = Arrays.copyOf(origArray, rightLimit);
            stime = System.nanoTime();
            binaryInsertionSort(acopy);
            ftime = System.nanoTime();
            diff = ftime - stime;
            bin_in_sort[round][x] = diff;
            System.out.printf("%34s%11d%n", "Binary insertion sort: time (ms): ", diff / 1000000);
            checkOrder(acopy);

            acopy = Arrays.copyOf(origArray, rightLimit);
            stime = System.nanoTime();
            quickSort(acopy, 0, acopy.length);
            ftime = System.nanoTime();
            diff = ftime - stime;
            quicksort[round][x] = diff;
            System.out.printf("%34s%11d%n", "Quicksort: time (ms): ", diff / 1000000);
            checkOrder(acopy);

            acopy = Arrays.copyOf(origArray, rightLimit);
            stime = System.nanoTime();
            Arrays.sort(acopy);
            ftime = System.nanoTime();
            diff = ftime - stime;
            java_api_sort[round][x] = diff;
            System.out.printf("%34s%11d%n", "Java API  Arrays.sort: time (ms): ", diff / 1000000);
            checkOrder(acopy);

            acopy = Arrays.copyOf(origArray, rightLimit);
            stime = System.nanoTime();
            radixSort(acopy);
            ftime = System.nanoTime();
            diff = ftime - stime;
            radix_sort[round][x] = diff;
            System.out.printf("%34s%11d%n", "Radix sort: time (ms): ", diff / 1000000);
            checkOrder(acopy);
         }
      }

      System.out.println(getAvgMs(in_sort));
      System.out.println(getAvgMs(bin_in_sort));
      System.out.println(getAvgMs(quicksort));
      System.out.println(getAvgMs(java_api_sort));
      System.out.println(getAvgMs(radix_sort));
   }

   private static String getAvgMs(long[][] results){
      String result = "";
      for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
         long sum = 0;
         for (int j = 0; j < results[i].length; j++) {
            sum += results[i][j];
         }
         long avg = (sum / results[i].length) / 1000000;
         result += avg + " ";
      }
      return result;
   }

   /**
    * Insertion sort.
    *
    * @param a
    *           array to be sorted
    */
   public static void insertionSort(int[] a) {
      if (a.length < 2)
         return;
      for (int i = 1; i < a.length; i++) {
         int b = a[i];
         int j;
         for (j = i - 1; j >= 0; j--) {
            if (a[j] <= b)
               break;
            a[j + 1] = a[j];
         }
         a[j + 1] = b;
      }
   }

   /**
    * Binary insertion sort.
    *
    * @param a
    *           array to be sorted
    */
   public static void binaryInsertionSort(int[] a) {
      // get next element (key)
      // take granted that so far has been sorted
      // do binary search on sorted part to find 2 elements that where one is greater
      // and other is smaller or element is equal.
      // shift elements to right
      // insert key in place

      for (int i = 1; i < a.length; i++) {

         // a[i] --> key

         // binary search new place
         // left end of sorted array
         int l = 0;

         // right end of sorted array, one less than key.
         // must mind that key might stay in place.
         int r = i - 1;

         // new index for key
         int resultIndex = i;

         // loop until left and right meet
         while (l <= r) {

            // check left end
            if (a[i] <= a[l]) {
               resultIndex = l;
               break;
            }
            // check right end
            if (a[i] >= a[r]) {
               resultIndex = r + 1;
               break;
            }

            int k = (r + l) / 2;
            if (a[i] > a[k]) {
               // move more right
               l = k + 1;
            } else if (a[i] < a[k]) {
               // move more left
               r = k - 1;
            } else {
               resultIndex = k;
               break;
            }
         }

         // shift right
         int key = a[i];
         for (int j = i; j > resultIndex; j--) {
            a[j] = a[j - 1];
         }
         a[resultIndex] = key;
      }
   }

   public static void binaryInsertionSortHugoBork(int[] a) {
      if (a.length < 2)
         return;
      for (int i = 1; i < a.length; i++) {
         int b = a[i];
         int j;
         int left = 0;
         int right = i - 1;
         while (left <= right) {
            j = (left + right) / 2;
            if (b > a[j]) {
               left = j + 1;
            } else {
               right = j - 1;
            }
         }
         System.arraycopy(a, left, a, left + 1, i - left);
         a[left] = b;
      }
   }

   /**
    * Sort a part of the array using quicksort method.
    *
    * @param array
    *           array to be changed
    * @param l
    *           starting index (included)
    * @param r
    *           ending index (excluded)
    */
   public static void quickSort (int[] array, int l, int r) {
      if (array == null || array.length < 1 || l < 0 || r <= l)
         throw new IllegalArgumentException("quickSort: wrong parameters");
      if ((r - l) < 2)
         return;
      int i = l;
      int j = r - 1;
      int x = array[(i + j) / 2];
      do {
         while (array[i] < x)
            i++;
         while (x < array[j])
            j--;
         if (i <= j) {
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
            i++;
            j--;
         }
      } while (i < j);
      if (l < j)
         quickSort(array, l, j + 1); // recursion for left part
      if (i < r - 1)
         quickSort(array, i, r); // recursion for right part
   }

   /** frequency of the byte */
   public static int[] freq = new int[256];

   /** number of positions */
   public static final int KEYLEN = 4;

   /** Get the value of the position i. */
   public static int getValue(int key, int i) {
      return (key >>> (8 * i)) & 0xff;
   }

   /** Sort non-negative keys by position i in a stable manner. */
   public static int[] countSort(int[] keys, int i) {
      if (keys == null)
         return null;
      int[] res = new int[keys.length];
      for (int k = 0; k < freq.length; k++) {
         freq[k] = 0;
      }
      for (int key : keys) {
         freq[getValue(key, i)]++;
      }
      for (int k = 1; k < freq.length; k++) {
         freq[k] = freq[k - 1] + freq[k];
      }
      for (int j = keys.length - 1; j >= 0; j--) {
         int ind = --freq[getValue(keys[j], i)];
         res[ind] = keys[j];
      }
      return res;
   }

   /** Radix sort for non-negative integers. */
   public static void radixSort(int[] keys) {
      if (keys == null)
         return;
      int[] res = keys;
      for (int p = 0; p < KEYLEN; p++) {
         res = countSort(res, p);
      }
      System.arraycopy(res, 0, keys, 0, keys.length);
   }

   /**
    * Check whether an array is ordered.
    *
    * @param a
    *           sorted (?) array
    * @throws IllegalArgumentException
    *            if an array is not ordered
    */
   static void checkOrder(int[] a) {
      if (a.length < 2)
         return;
      for (int i = 0; i < a.length - 1; i++) {
         if (a[i] > a[i + 1])
            throw new IllegalArgumentException(
                    "array not ordered: " + "a[" + i + "]=" + a[i] + " a[" + (i + 1) + "]=" + a[i + 1]);
      }
   }

}
