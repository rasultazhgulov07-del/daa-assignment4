public class Problem08 {
    public int countSubstrings(String s) {
        int n = s.length();
        // palindrome[i][j] describes the contiguous interval s[i..j].
        boolean[][] palindrome = new boolean[n][n];
        int count = 0;

        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)
                        && (j - i < 2 || palindrome[i + 1][j - 1])) {
                    palindrome[i][j] = true;
                    count++;
                }
            }
        }

        return count;
    }
}
