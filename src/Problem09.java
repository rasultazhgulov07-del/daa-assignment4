public class Problem09 {
    public int maxSubArray(int[] nums) {
        // The required subarray is nonempty, including for all-negative input.
        int endingHere = nums[0];
        int best = nums[0];

        for (int i = 1; i < nums.length; i++) {
            endingHere = Math.max(nums[i], endingHere + nums[i]);
            best = Math.max(best, endingHere);
        }

        return best;
    }
}
