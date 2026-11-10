import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/** Independent, deterministic checks; no testing library or Java -ea flag needed. */
public class Assignment4Tests {
    private static final long SEED = 20261110L;
    private static int assertions = 0;

    public static void main(String[] args) {
        run("01 Counting Bits", Assignment4Tests::test01);
        run("02 Jump Game II", Assignment4Tests::test02);
        run("03 Min Cost Climbing Stairs", Assignment4Tests::test03);
        run("04 Longest Increasing Subsequence", Assignment4Tests::test04);
        run("05 Longest Common Subsequence", Assignment4Tests::test05);
        run("06 Edit Distance", Assignment4Tests::test06);
        run("07 Longest Palindromic Subsequence", Assignment4Tests::test07);
        run("08 Palindromic Substrings", Assignment4Tests::test08);
        run("09 Maximum Subarray", Assignment4Tests::test09);
        run("10 House Robber", Assignment4Tests::test10);
        System.out.println("PASS: " + assertions + " assertions; all 10 problems; seed=" + SEED);
    }

    private static void run(String name, Runnable test) {
        int before = assertions;
        test.run();
        System.out.println("PASS " + name + ": " + (assertions - before) + " assertions");
    }

    private static void equal(int expected, int actual, String context) {
        assertions++;
        if (expected != actual) {
            throw new AssertionError(context + ": expected " + expected + ", got " + actual);
        }
    }

    private static void array(int[] expected, int[] actual, String context) {
        assertions++;
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(context + ": expected " + Arrays.toString(expected)
                    + ", got " + Arrays.toString(actual));
        }
    }

    private static void test01() {
        Problem01 p = new Problem01();
        array(new int[]{0}, p.countBits(0), "n=0");
        array(new int[]{0, 1, 1}, p.countBits(2), "n=2");
        array(new int[]{0, 1, 1, 2, 1, 2}, p.countBits(5), "n=5");
        array(new int[]{0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4, 1},
                p.countBits(16), "n=16");
        // Independent per-number arithmetic counting, including the upper bound.
        int[] all = p.countBits(100000);
        equal(100001, all.length, "upper-bound output length");
        for (int value = 0; value <= 100000; value++) {
            int count = 0;
            for (int remaining = value; remaining > 0; remaining /= 2) {
                count += remaining % 2;
            }
            equal(count, all[value], "bit count at " + value);
        }
    }

    private static void test02() {
        Problem02 p = new Problem02();
        equal(2, p.jump(new int[]{2, 3, 1, 1, 4}), "required jump 1");
        equal(2, p.jump(new int[]{2, 3, 0, 1, 4}), "required jump 2");
        equal(0, p.jump(new int[]{0}), "already at goal");
        equal(1, p.jump(new int[]{1, 0}), "two cells");
        equal(1, p.jump(new int[]{1000, 0, 0, 0}), "jump beyond last index");
        equal(2, p.jump(new int[]{2, 0, 2, 0, 0}), "reachable with interior zeros");
        int[] chain = new int[10000];
        Arrays.fill(chain, 1);
        equal(9999, p.jump(chain), "maximum-length unit jumps");
        Arrays.fill(chain, 1000);
        equal(10, p.jump(chain), "maximum-length maximum jumps");
        Random random = new Random(SEED + 2);
        for (int trial = 0; trial < 300; trial++) {
            int n = 1 + random.nextInt(10);
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) nums[i] = random.nextInt(5);
            // Plant a path from 0 to n-1, preserving zeros off that path.
            for (int i = 0; i < n - 1;) {
                int step = 1 + random.nextInt(Math.min(4, n - 1 - i));
                nums[i] = Math.max(nums[i], step);
                i += step;
            }
            equal(jumpBfs(nums), p.jump(nums.clone()), "random reachable " + Arrays.toString(nums));
        }
    }

    private static int jumpBfs(int[] nums) {
        Queue<Integer> pending = new ArrayDeque<>();
        boolean[] seen = new boolean[nums.length];
        pending.add(0);
        seen[0] = true;
        for (int depth = 0; !pending.isEmpty(); depth++) {
            int size = pending.size();
            for (int k = 0; k < size; k++) {
                int i = pending.remove();
                if (i == nums.length - 1) return depth;
                for (int j = i + 1; j < nums.length && j <= i + nums[i]; j++) {
                    if (!seen[j]) {
                        seen[j] = true;
                        pending.add(j);
                    }
                }
            }
        }
        throw new AssertionError("Generator produced an unreachable jump array");
    }

    private static void test03() {
        Problem03 p = new Problem03();
        equal(15, p.minCostClimbingStairs(new int[]{10, 15, 20}), "required stairs 1");
        equal(6, p.minCostClimbingStairs(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1}),
                "required stairs 2");
        equal(0, p.minCostClimbingStairs(new int[]{0, 0}), "minimum length, free stairs");
        equal(3, p.minCostClimbingStairs(new int[]{3, 7}), "minimum length, choose start");
        equal(0, p.minCostClimbingStairs(new int[]{0, 999, 0, 999, 0}), "skip expensive stairs");
        int[] boundary = new int[1000];
        Arrays.fill(boundary, 999);
        equal(499500, p.minCostClimbingStairs(boundary), "maximum size and cost");
        Random random = new Random(SEED + 3);
        for (int trial = 0; trial < 300; trial++) {
            int[] cost = new int[2 + random.nextInt(9)];
            for (int i = 0; i < cost.length; i++) cost[i] = random.nextInt(21);
            int expected = Math.min(stairPaths(cost, 0), stairPaths(cost, 1));
            equal(expected, p.minCostClimbingStairs(cost.clone()), "random costs " + Arrays.toString(cost));
        }
    }

    // Exhaustively enumerate forward routes; no memoization or DP table.
    private static int stairPaths(int[] cost, int index) {
        if (index >= cost.length) return 0;
        return cost[index] + Math.min(stairPaths(cost, index + 1), stairPaths(cost, index + 2));
    }

    private static void test04() {
        Problem04 p = new Problem04();
        equal(4, p.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}), "required LIS 1");
        equal(1, p.lengthOfLIS(new int[]{7, 7, 7, 7}), "strict inequality");
        equal(1, p.lengthOfLIS(new int[]{-10000}), "singleton");
        equal(1, p.lengthOfLIS(new int[]{4, 3, 2, 1}), "decreasing");
        equal(4, p.lengthOfLIS(new int[]{-10000, -1, 0, 10000}), "negative and boundary values");
        int[] boundary = new int[2500];
        for (int i = 0; i < boundary.length; i++) boundary[i] = i;
        equal(2500, p.lengthOfLIS(boundary), "maximum increasing length");
        Random random = new Random(SEED + 4);
        for (int trial = 0; trial < 300; trial++) {
            int[] nums = new int[1 + random.nextInt(10)];
            for (int i = 0; i < nums.length; i++) nums[i] = random.nextInt(11) - 5;
            equal(lisSubsets(nums), p.lengthOfLIS(nums.clone()), "random LIS " + Arrays.toString(nums));
        }
    }

    private static int lisSubsets(int[] nums) {
        int best = 0;
        for (int mask = 1; mask < (1 << nums.length); mask++) {
            int length = 0;
            int previous = Integer.MIN_VALUE;
            boolean valid = true;
            for (int i = 0; i < nums.length; i++) {
                if ((mask & (1 << i)) != 0) {
                    if (nums[i] <= previous) valid = false;
                    previous = nums[i];
                    length++;
                }
            }
            if (valid) best = Math.max(best, length);
        }
        return best;
    }

    private static void test05() {
        Problem05 p = new Problem05();
        equal(3, p.longestCommonSubsequence("abcde", "ace"), "required LCS 1");
        equal(0, p.longestCommonSubsequence("abc", "def"), "required LCS 2");
        equal(1, p.longestCommonSubsequence("a", "a"), "single match");
        equal(0, p.longestCommonSubsequence("a", "b"), "single mismatch");
        equal(2, p.longestCommonSubsequence("aaaa", "aa"), "repeated characters");
        equal(1, p.longestCommonSubsequence("ab", "ba"), "order matters");
        equal(1000, p.longestCommonSubsequence("a".repeat(1000), "a".repeat(1000)), "maximum lengths");
        Random random = new Random(SEED + 5);
        for (int trial = 0; trial < 300; trial++) {
            String a = randomString(random, 1 + random.nextInt(8));
            String b = randomString(random, 1 + random.nextInt(8));
            equal(lcsSubsets(a, b), p.longestCommonSubsequence(a, b), "random LCS " + a + "/" + b);
        }
    }

    private static int lcsSubsets(String a, String b) {
        int best = 0;
        for (int mask = 0; mask < (1 << a.length()); mask++) {
            String candidate = subsequence(a, mask);
            int matched = 0;
            for (int i = 0; i < b.length() && matched < candidate.length(); i++) {
                if (b.charAt(i) == candidate.charAt(matched)) matched++;
            }
            if (matched == candidate.length()) best = Math.max(best, matched);
        }
        return best;
    }

    private static void test06() {
        Problem06 p = new Problem06();
        equal(3, p.minDistance("horse", "ros"), "required edit 1");
        equal(5, p.minDistance("intention", "execution"), "required edit 2");
        equal(0, p.minDistance("", ""), "both empty (valid)");
        equal(3, p.minDistance("", "abc"), "only insertions");
        equal(3, p.minDistance("abc", ""), "only deletions");
        equal(0, p.minDistance("same", "same"), "identical");
        equal(1, p.minDistance("a", "b"), "replacement");
        equal(2, p.minDistance("ab", "ba"), "transposition is not one operation");
        equal(500, p.minDistance("a".repeat(500), "b".repeat(500)), "maximum lengths");
        Random random = new Random(SEED + 6);
        for (int trial = 0; trial < 200; trial++) {
            // BFS over actual edited words is practical on this small alphabet/length.
            String a = randomBinaryString(random, random.nextInt(4));
            String b = randomBinaryString(random, random.nextInt(4));
            equal(editBfs(a, b), p.minDistance(a, b), "random edits " + a + "/" + b);
        }
    }

    private static int editBfs(String from, String to) {
        Queue<String> pending = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();
        pending.add(from);
        seen.add(from);
        // A monotone optimal alignment never needs a word longer than this.
        int bound = Math.max(from.length(), to.length());
        for (int depth = 0; !pending.isEmpty(); depth++) {
            int size = pending.size();
            for (int k = 0; k < size; k++) {
                String word = pending.remove();
                if (word.equals(to)) return depth;
                for (int i = 0; i < word.length(); i++) {
                    enqueue(word.substring(0, i) + word.substring(i + 1), pending, seen);
                    for (char c = 'a'; c <= 'b'; c++) {
                        enqueue(word.substring(0, i) + c + word.substring(i + 1), pending, seen);
                    }
                }
                if (word.length() < bound) {
                    for (int i = 0; i <= word.length(); i++) {
                        for (char c = 'a'; c <= 'b'; c++) {
                            enqueue(word.substring(0, i) + c + word.substring(i), pending, seen);
                        }
                    }
                }
            }
        }
        throw new AssertionError("Edit BFS did not reach target");
    }

    private static void enqueue(String word, Queue<String> pending, Set<String> seen) {
        if (seen.add(word)) pending.add(word);
    }

    private static void test07() {
        Problem07 p = new Problem07();
        equal(4, p.longestPalindromeSubseq("bbbab"), "required LPS 1");
        equal(2, p.longestPalindromeSubseq("cbbd"), "required LPS 2");
        equal(1, p.longestPalindromeSubseq("a"), "singleton");
        equal(2, p.longestPalindromeSubseq("aa"), "empty interior");
        equal(1, p.longestPalindromeSubseq("ab"), "unequal pair");
        equal(5, p.longestPalindromeSubseq("abcba"), "whole palindrome");
        equal(1, p.longestPalindromeSubseq("abc"), "distinct characters");
        equal(1000, p.longestPalindromeSubseq("a".repeat(1000)), "maximum length");
        Random random = new Random(SEED + 7);
        for (int trial = 0; trial < 300; trial++) {
            String s = randomString(random, 1 + random.nextInt(10));
            equal(lpsSubsets(s), p.longestPalindromeSubseq(s), "random LPS " + s);
        }
    }

    private static int lpsSubsets(String s) {
        int best = 0;
        for (int mask = 1; mask < (1 << s.length()); mask++) {
            String candidate = subsequence(s, mask);
            if (isPalindrome(candidate)) best = Math.max(best, candidate.length());
        }
        return best;
    }

    private static String subsequence(String s, int mask) {
        StringBuilder candidate = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if ((mask & (1 << i)) != 0) candidate.append(s.charAt(i));
        }
        return candidate.toString();
    }

    private static boolean isPalindrome(String s) {
        for (int left = 0, right = s.length() - 1; left < right; left++, right--) {
            if (s.charAt(left) != s.charAt(right)) return false;
        }
        return true;
    }

    private static void test08() {
        Problem08 p = new Problem08();
        equal(3, p.countSubstrings("abc"), "required substring 1");
        equal(6, p.countSubstrings("aaa"), "required substring 2");
        equal(1, p.countSubstrings("a"), "singleton");
        equal(3, p.countSubstrings("aa"), "even pair and occurrences");
        equal(6, p.countSubstrings("abba"), "even length palindrome");
        equal(9, p.countSubstrings("ababa"), "overlapping palindromes");
        equal(500500, p.countSubstrings("a".repeat(1000)), "maximum count");
        Random random = new Random(SEED + 8);
        for (int trial = 0; trial < 300; trial++) {
            String s = randomString(random, 1 + random.nextInt(12));
            int count = 0;
            for (int i = 0; i < s.length(); i++) {
                for (int j = i + 1; j <= s.length(); j++) {
                    if (isPalindrome(s.substring(i, j))) count++;
                }
            }
            equal(count, p.countSubstrings(s), "random substring " + s);
        }
    }

    private static void test09() {
        Problem09 p = new Problem09();
        equal(6, p.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}), "required subarray 1");
        equal(-3, p.maxSubArray(new int[]{-3}), "required negative singleton");
        equal(-2, p.maxSubArray(new int[]{-8, -2, -5}), "all negative");
        equal(0, p.maxSubArray(new int[]{0, 0, 0}), "all zero");
        equal(7, p.maxSubArray(new int[]{7}), "positive singleton");
        equal(6, p.maxSubArray(new int[]{1, 2, 3}), "all positive");
        equal(5, p.maxSubArray(new int[]{-10000, 5, -10000}), "interior best");
        int[] boundary = new int[100000];
        Arrays.fill(boundary, 10000);
        equal(1000000000, p.maxSubArray(boundary), "maximum size and positive sum");
        Arrays.fill(boundary, -10000);
        equal(-10000, p.maxSubArray(boundary), "maximum size all negative");
        Random random = new Random(SEED + 9);
        for (int trial = 0; trial < 300; trial++) {
            int[] nums = new int[1 + random.nextInt(12)];
            for (int i = 0; i < nums.length; i++) nums[i] = random.nextInt(21) - 10;
            equal(subarraySums(nums), p.maxSubArray(nums.clone()), "random subarray " + Arrays.toString(nums));
        }
    }

    private static int subarraySums(int[] nums) {
        int best = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int sum = 0;
            for (int j = i; j < nums.length; j++) {
                sum += nums[j];
                best = Math.max(best, sum);
            }
        }
        return best;
    }

    private static void test10() {
        Problem10 p = new Problem10();
        equal(4, p.rob(new int[]{1, 2, 3, 1}), "required houses 1");
        equal(12, p.rob(new int[]{2, 7, 9, 3, 1}), "required houses 2");
        equal(0, p.rob(new int[]{0}), "zero singleton");
        equal(400, p.rob(new int[]{400}), "maximum singleton");
        equal(9, p.rob(new int[]{2, 9}), "two houses, second better");
        equal(9, p.rob(new int[]{9, 2}), "two houses, first better");
        equal(0, p.rob(new int[]{0, 0, 0}), "all zero");
        equal(4, p.rob(new int[]{2, 1, 1, 2}), "nonadjacent endpoints");
        int[] boundary = new int[100];
        Arrays.fill(boundary, 400);
        equal(20000, p.rob(boundary), "maximum size and values");
        Random random = new Random(SEED + 10);
        for (int trial = 0; trial < 300; trial++) {
            int[] nums = new int[1 + random.nextInt(12)];
            for (int i = 0; i < nums.length; i++) nums[i] = random.nextInt(31);
            equal(houseSubsets(nums), p.rob(nums.clone()), "random houses " + Arrays.toString(nums));
        }
    }

    private static int houseSubsets(int[] nums) {
        int best = 0;
        for (int mask = 0; mask < (1 << nums.length); mask++) {
            if ((mask & (mask << 1)) != 0) continue;
            int sum = 0;
            for (int i = 0; i < nums.length; i++) {
                if ((mask & (1 << i)) != 0) sum += nums[i];
            }
            best = Math.max(best, sum);
        }
        return best;
    }

    private static String randomString(Random random, int length) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) s.append((char) ('a' + random.nextInt(3)));
        return s.toString();
    }

    private static String randomBinaryString(Random random, int length) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) s.append((char) ('a' + random.nextInt(2)));
        return s.toString();
    }
}
