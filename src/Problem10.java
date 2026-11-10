public class Problem10 {
    public int rob(int[] nums) {
        // Before house i: previous = F(i), twoBack = F(i - 1).
        int twoBack = 0;
        int previous = 0;

        for (int amount : nums) {
            int current = Math.max(previous, twoBack + amount);
            twoBack = previous;
            previous = current;
        }

        return previous;
    }
}
