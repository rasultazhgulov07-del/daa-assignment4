# Test design and reproduction

This is the standalone test suite for **Rassul Tazhgulov**. Run it as described in the [project README](../README.md). The harness and runner are intentionally shared between the two permitted-reference projects; each copy compiles against only its own project's sources. No JUnit or downloaded dependency is needed.

The [captured results](RESULTS.md) record one actual execution with Temurin JDK 17.0.17 and the exact invoked commands. `Assignment4Tests.main` prints a line after each completed problem and throws `AssertionError` on the first mismatch. The runner checks native process exit codes as well as PowerShell errors. The seed is `20261110`; each randomized problem initializes `Random` with that seed plus its problem number.

## Valid input domains

The following domains were checked against the official problem descriptions linked in the supplied task files on 2026-11-10. This table describes test selection, not new assignment requirements. Strings below consist of lowercase English letters. No null inputs are required.

| Problem | Official domain | Boundary checks in this harness |
|---|---|---|
| [01](https://leetcode.com/problems/counting-bits/description/) | `0 <= n <= 100000` | zero; all bit counts through 100000; output length |
| [02](https://leetcode.com/problems/jump-game-ii/description/) | length 1–10000; values 0–1000; last index reachable | singleton zero; interior zeros; length 10000 with unit and maximum jumps |
| [03](https://leetcode.com/problems/min-cost-climbing-stairs/description/) | length 2–1000; costs 0–999 | length two; zero costs; length 1000 with cost 999 |
| [04](https://leetcode.com/problems/longest-increasing-subsequence/description/) | length 1–2500; values -10000–10000 | singleton; equal/decreasing values; value extremes; length 2500 increasing |
| [05](https://leetcode.com/problems/longest-common-subsequence/description/) | each length 1–1000 | single-character match/mismatch; reversed order; repeated letters; both lengths 1000 |
| [06](https://leetcode.com/problems/edit-distance/description/) | each length 0–500 | empty/empty, empty/nonempty and reverse; both lengths 500 |
| [07](https://leetcode.com/problems/longest-palindromic-subsequence/description/) | length 1–1000 | singleton; equal/unequal pairs; whole palindrome; length 1000 |
| [08](https://leetcode.com/problems/palindromic-substrings/description/) | length 1–1000 | singleton; equal pair; even and overlapping palindromes; length 1000, count 500500 |
| [09](https://leetcode.com/problems/maximum-subarray/description/) | length 1–100000; values -10000–10000 | positive/negative singleton; all negative; zeros; length 100000 at both value extremes |
| [10](https://leetcode.com/problems/house-robber/description/) | length 1–100; values 0–400 | zero/400 singleton; two houses in both orders; zeros; length 100 with value 400 |

Empty input arrays and empty strings for 05, 07 and 08 are not required behavior. Unreachable Jump Game II arrays are not used as required tests. Empty strings for Edit Distance are valid and are tested. The first seven reference implementations therefore need no extra out-of-domain guards for this assignment.

## Independent oracle checks

| Problem | Small-input comparison | Number of seeded trials |
|---|---|---|
| 01 | Independently count digits with arithmetic division for each value 0–100000 | exhaustive range, not random |
| 02 | Breadth-first search of the jump graph; generator plants a path and may leave zeros elsewhere | 300 |
| 03 | Enumerate all one-/two-step routes recursively, without a DP table or memoization | 300 |
| 04 | Enumerate all index subsets and check strict increase | 300 |
| 05 | Enumerate subsequences of one string and scan for membership in the other | 300 |
| 06 | Breadth-first search over actual insert/delete/replace operations on binary strings of lengths 0–3 | 200 |
| 07 | Enumerate subsequences and test each directly for palindrome symmetry | 300 |
| 08 | Enumerate contiguous intervals and check each by two-pointer comparisons | 300 |
| 09 | Enumerate and sum every nonempty contiguous interval | 300 |
| 10 | Enumerate all house subsets, reject masks containing adjacent selected bits | 300 |

There are 2,600 seeded randomized comparisons. Small alphabets and lengths make exhaustive or BFS oracles practical. The edit BFS limits intermediate length to the greater endpoint length: an optimal monotone alignment can schedule deletions before insertions and stay within that bound. It explores actual words and edit operations rather than copying the production DP recurrence.

All 23 requested examples are explicit assertions, including `n = 0,2,5,16`, both supplied stair cases, both edit-distance cases, the negative Maximum Subarray singleton, and both House Robber examples. Additional fixed cases and upper-bound inputs appear directly in [Assignment4Tests.java](Assignment4Tests.java).

The large bit-count range accounts for most assertions; the total assertion count is not a count of independently randomized inputs. The final per-project total is 102,675. These checks do not constitute exhaustive coverage of every permitted input or prove a grade, an online judge result, or a runtime guarantee on other machines.
