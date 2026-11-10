# DAA Assignment 4 — Design, correctness and complexity

Prepared for **Rassul Tazhgulov** for code review and defense.

This report studies why each chosen DP state is sufficient, why its recurrence is correct, and how its dependency structure determines implementation order and resource costs. Each section links to the original task description and the actual Java method. `n` denotes a single input length; `n` and `m` denote two string lengths. Complexity describes the submitted implementation, including its allocated DP structures.

**Provenance.** The permitted reference is the supplied `daa-assignment4-korkem07-main.zip`, whose project identifier is `daa-assignment4-korkem07-main`. The reviewed source implementations for Problems 01–07 are retained unchanged. AI assistance completed Problems 08–10 and helped review the algorithms, prepare automated tests and write this report. The reference report was consulted, but its personal authorship narrative is not asserted here. No independent-authorship, online-submission, score or judge-acceptance claim is made. Actual local test evidence is in [tests/RESULTS.md](tests/RESULTS.md).

## A. Reducing integers and traversing forward states

## 01 — Counting Bits

[Task/reference](Problem01.md) · [Code](src/Problem01.java)

**Specification and design.** Given a nonnegative `n`, return a count for every integer in `[0,n]`. The binary representation supplies a natural smaller subproblem: deleting the least significant bit divides a positive integer by two. This reduction avoids independently scanning every binary representation in the solution.

**State, initialization and rule.** Define `C(i)` as the number of ones in `i`. Its base is `C(0) = 0`; for positive `i`,

```text
C(i) = C(floor(i / 2)) + (i mod 2).
```

The method stores `C(i)` in `ans[i]`, implements division as `i >> 1` and remainder as `i & 1`, and returns the complete array.

**Correctness argument.** Binary digits separate into the prefix and final digit. Their one-counts add, so the recurrence is exact. Strong induction on `i` proves correctness: the base is right, the prefix index is smaller, and its correct stored answer plus the final bit gives the right answer at `i`. The ascending loop respects this induction.

**Worked example: `n = 8`.** The returned counts are `[0,1,1,2,1,2,2,3,1]`. At the boundary, `7 = 111₂` uses `C(3) + 1 = 3`, whereas `8 = 1000₂` uses `C(4) + 0 = 1`. This illustrates both repeated prefix reuse and the reset in count across a power of two.

**Resource analysis.** There is one constant-time update per positive integer and one allocation of `n + 1` entries. Time and output-inclusive space are `O(n + 1)`, conventionally `O(n)`. Auxiliary space excluding output is `O(1)`. The method itself uses no built-in population count.

**Domain and edge cases.** Zero requires an output entry even though the loop does not run. Powers of two and values immediately below them check the bit decomposition. Negative inputs are outside the specified domain; input validation is not part of this implementation.

**Defense point.** “The recurrence is justified by a disjoint decomposition of binary digits. Since the prefix is smaller, induction also gives the exact computation order.”

## 02 — Jump Game II

[Task/reference](Problem02.md) · [Code](src/Problem02.java)

**Specification and design.** Find the minimum jumps from the first to last index, given maximum forward jump lengths and guaranteed reachability. Interpret indices as vertices of a directed acyclic graph. An allowed jump is a unit-cost edge, so this is a shortest-path DP in index order.

**State and recurrence.** `dp[v]` is the minimum number of edges from index 0 to `v`. Initialize `dp[0] = 0`; other vertices have infinity. Mathematically,

```text
dp[v] = 1 + min { dp[u] : 0 <= u < v and v - u <= nums[u] }.
```

The code evaluates this by outgoing-edge relaxation: for every valid positive jump `j` from reachable `u`, set `dp[u + j] = min(dp[u + j], dp[u] + 1)`. Unreachable states are skipped before arithmetic on infinity.

**Correctness invariant.** At the beginning of outer iteration `u`, all incoming edges to `u` have been considered because their sources are strictly smaller. Thus `dp[u]` is already the shortest distance. Relaxing its outgoing edges incorporates all paths whose final processed edge starts there. Induction over vertices proves all final distances, including the returned `dp[n - 1]`.

**Worked example: `[2,0,2,0,0]`.** Index 0 gives states 1 and 2 distance 1. Index 1 has no outgoing positive jump. From index 2, states 3 and 4 receive distance 2. The final distances are `[0,1,1,2,2]`; route `0 → 2 → 4` realizes the optimum 2. Interior zero values do not imply failure when they can be bypassed.

**Implementation and resource analysis.** The outer loop visits all `n` states and the inner loop visits only valid destinations. With `E = sum(min(nums[u], n - 1 - u))`, time is bounded by `O(n + E)` and space by `O(n)`. For maximum jump `k`, this is `O(n(1 + min(k,n)))`, generally `O(n²)`. With the stated cap `k <= 1000`, at most about ten million candidate jumps are examined at the maximum input size. The solution uses the assignment's required DP approach.

**Domain and edge cases.** A length-one input requires zero jumps even if its value is zero. Overshooting lengths are clipped to existing indices. Unreachable arrays have no required result, because the task guarantees a path; no new failure convention is imposed.

**Defense point.** “This is shortest path on a forward DAG. Index order is a topological order, so one pass of outgoing relaxations is enough even though each state can have many edges.”

## 03 — Min Cost Climbing Stairs

[Task/reference](Problem03.md) · [Code](src/Problem03.java)

**Specification and design.** Reach the position after the last step at minimum total cost. Start at step 0 or 1; leaving a step requires its payment and permits a move of one or two positions. Distinguishing arrival from departure prevents charging the top or charging a starting step before it is used.

**State, bases and optimal substructure.** Let `D(i)` be the cheapest cost to arrive at position `i`. The free entry choices give `D(0) = D(1) = 0`. For `2 <= i <= n`,

```text
D(i) = min(D(i - 1) + cost[i - 1],
           D(i - 2) + cost[i - 2]).
```

The array `dp` stores these values; its final entry represents the unpriced top.

**Correctness argument.** Any path into `i` has a last departure at `i - 1` or `i - 2`. Replacing the path up to that departure with a cheaper one would improve the whole route, so an optimum uses an optimal predecessor. Conversely, appending the specified legal move to either optimal predecessor produces a valid candidate. These lower and upper bounds establish the minimum recurrence. Increasing-index evaluation begins from the two correct bases.

**Worked example: `cost = [4,2,7,3]`.** `D(2) = min(2,4) = 2`; `D(3) = min(2 + 7,0 + 2) = 2`; `D(4) = min(2 + 3,2 + 7) = 5`. The table is `[0,0,2,2,5]`. Starting at step 1 and paying 2, then leaving step 3 after paying 3, reaches top 4 with total 5.

**Implementation and resource analysis.** The loop starts at 2 and ends at `n`, using constant work at each position. The returned value is `dp[n]`, not `dp[n - 1]`. Time is `O(n)` and actual allocated space is `O(n)`. The recurrence permits constant-space compression, but this implementation deliberately retains the reference table.

**Domain and edge cases.** Two steps produce the minimum of their costs. Zero costs do not need special handling. The official minimum length is two; a one-element cost array is not a required boundary case. All values and total costs remain within `int`.

**Defense point.** “Optimal substructure follows by replacing the route before the final departure. My state excludes the payment for leaving the current position, which keeps both bases and the top consistent.”

## B. Selecting ordered subsequences and aligning prefixes

## 04 — Longest Increasing Subsequence

[Task/reference](Problem04.md) · [Code](src/Problem04.java)

**Specification and design.** Return the maximum length of a strictly increasing subsequence. An arbitrary best subsequence in a prefix does not identify its final value, so it cannot alone decide whether the next element is appendable. Fixing the endpoint gives a sufficient state.

**State and recurrence.** Let `L(i)` be the maximum length ending at index `i`. Every element alone is valid, so initialize each state to 1. Then

```text
L(i) = max({1} union {L(j) + 1 : j < i and nums[j] < nums[i]}).
answer = max_i L(i).
```

**Correctness argument.** Any subsequence counted by `L(i)` either has length one or has a penultimate element at an earlier smaller position `j`. Removing its last element leaves a candidate bounded by `L(j)`. Conversely, appending `nums[i]` to an optimal subsequence ending at such `j` is legal. Thus the candidates are sound and complete. The order `j < i` makes ascending evaluation valid.

**Worked example: `[0,1,0,3,2,3]`.** The endpoint lengths are `[1,2,1,3,3,4]`. For the final 3, the earlier 2 provides length 3 and can be extended to length 4. The earlier 3 cannot be used because increase must be strict. Indices 0, 1, 4 and 5 give the witness `[0,1,2,3]`.

**Implementation and resource analysis.** `Arrays.fill` sets all singleton bases; `max` starts at 1 under the nonempty-array contract. The nested loops examine `n(n - 1)/2` index pairs, giving `O(n²)` time and `O(n)` space. Keeping the maximum across all endpoints is necessary: for `[1,2,0]`, the final endpoint length is 1 but the overall answer is 2.

**Domain and edge cases.** Repeated values cannot extend each other. Decreasing inputs and singletons return 1. Negative values need no separate logic. Empty arrays are outside the contract; the retained initialization assumes at least one element.

**Defense point.** “The endpoint is the information needed to decide an extension. The recurrence enumerates all legal penultimate elements, and maximizing over endpoints removes that constraint at the end.”

## 05 — Longest Common Subsequence

[Task/reference](Problem05.md) · [Code](src/Problem05.java)

**Specification and design.** Find the longest ordered character sequence shared by two strings, allowing deletions. A two-prefix state retains order information that character sets or frequency counts would lose.

**State, bases and transitions.** `dp[i][j]` is the LCS length for prefixes of lengths `i` and `j`. Set the entire zero row and column to 0, since an empty prefix has no nonempty common subsequence.

```text
matching final characters: dp[i][j] = dp[i - 1][j - 1] + 1
different final characters: dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
```

**Correctness argument.** For matching endpoints, a best common subsequence of the shorter prefixes can be extended by the common last character. An optimal subsequence can be chosen to pair those matching final positions; removing the pair leaves a subsequence of the shorter prefixes, establishing the reverse bound. When endpoints differ, both cannot be paired to each other as the last matched position; at least one endpoint is omitted, so one of the two dropped-prefix states covers an optimum. Induction over prefix lengths proves the table.

**Worked example: `"ab"` and `"ba"`.**

| Prefix of first string | empty | b | ba |
|---|---|---|---|
| empty | 0 | 0 | 0 |
| a | 0 | 0 | 1 |
| ab | 0 | 1 | 1 |

The last characters differ, so the final cell is `max(1,1) = 1`. Either `a` or `b` is a witness. Sharing the same two letters does not produce length 2 because their order is reversed.

**Implementation and resource analysis.** The Java array supplies zero bases automatically. Row-major filling makes the upper, left and diagonal neighbors available. Return the bottom-right cell, `dp[n][m]`. For the nonempty lengths in this problem, time and allocated space are `O(nm)`. No reconstruction is stored, since only a length is requested.

**Domain and edge cases.** Disjoint alphabets give 0. Identical strings give their length. Repeated letters are constrained by the available ordered occurrences. Empty prefixes are internal bases even though official original inputs are nonempty.

**Defense point.** “The two indices encode order. Matching endpoints extend a diagonal optimum; different endpoints require dropping at least one, which explains both branches and all dependencies.”

## 06 — Edit Distance

[Task/reference](Problem06.md) · [Code](src/Problem06.java)

**Specification and design.** Minimize the number of unit-cost insertions, deletions and replacements that transform the source into the target. Use the same prefix grid shape as LCS, but optimize edit cost rather than matched length.

**State and bases.** `dp[i][j]` is the cheapest conversion of the source prefix of length `i` to the target prefix of length `j`. Set `dp[i][0] = i`, `dp[0][j] = j`, including `dp[0][0] = 0`.

**Recurrence.** If the final characters match, copy `dp[i - 1][j - 1]`. Otherwise use

```text
dp[i][j] = min(dp[i - 1][j - 1] + 1,  // replacement
               dp[i - 1][j] + 1,      // source deletion
               dp[i][j - 1] + 1).     // target-character insertion
```

**Correctness argument.** Align an optimal edit script to the two prefixes. Its last alignment action consumes both final characters, only a source character, or only a target character. These are exactly replacement/match, deletion and insertion. Removing that action leaves the corresponding smaller-prefix conversion; replacing it with a cheaper conversion would contradict optimality. Every recurrence candidate also constructs a legal script, proving equality. Matching endpoints admit a zero-cost diagonal alignment. The increasing loops follow the grid dependencies.

**Worked example: `"ab" → "ba"`.** The rows for empty, `a`, and `ab`, against columns empty, `b`, and `ba`, are `[0,1,2]`, `[1,1,1]`, and `[2,1,2]`. The final minimum is 2. Two replacements give `ab → bb → ba`; a single transposition is not an allowed edit. This distinguishes the algorithm from a distance measure that permits swaps.

**Implementation and resource analysis.** Two loops explicitly initialize the boundary row and column, followed by the prefix-pair loops. The returned cell is `dp[n][m]`. Time and space are `O((n + 1)(m + 1))`, which remains accurate when either input is empty; with both lengths positive, this is the usual `O(nm)`. The full table, not a rolling-row optimization, is allocated.

**Domain and edge cases.** Empty strings are valid in this problem: one empty input costs the length of the other, and two empty inputs cost 0. Identical words also cost 0. A single differing character costs one replacement.

**Defense point.** “The three neighboring cells correspond to the three permitted edit operations. The last-action argument proves completeness, and the boundary row and column represent actual insertion-only or deletion-only scripts.”

## C. Interval structure: subsequence versus substring

## 07 — Longest Palindromic Subsequence

[Task/reference](Problem07.md) · [Code](src/Problem07.java)

**Specification and design.** Return the longest palindrome obtainable by deleting characters in order. Endpoint decisions suggest an interval state rather than a prefix state, because a palindrome relates both ends of the remaining range.

**State, bases and recurrence.** `dp[i][j]` is the best palindromic subsequence length inside inclusive interval `s[i..j]`. A singleton has value 1; an empty interval has value 0.

```text
s[i] == s[j]: dp[i][j] = 2 + dp[i + 1][j - 1]
s[i] != s[j]: dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])
```

**Correctness argument.** Equal endpoints can surround an optimal interior palindrome. An optimum using both endpoints cannot improve on this value. If an optimum uses just one endpoint, remove its matching partner too; what remains is an interior palindrome, so its length is at most the interior optimum plus two. An optimum using neither is also bounded by this candidate. If endpoints differ, no palindromic subsequence can use both as its outermost selected characters, so it must lie in one of the two reduced intervals. Induction on interval length proves the rule.

**Worked example: `"cbbd"`.** Adjacent interval `bb` has value 2, while `cb` and `bd` each have value 1. Intervals `cbb` and `bbd` each inherit value 2 by omitting a mismatched endpoint. The full interval has different endpoints `c` and `d`, so its value is `max(2,2) = 2`, witnessed by `bb`.

**Implementation and dependency order.** Rows run with decreasing `i`; columns within a row run with increasing `j`. Thus the lower-row interior and left neighbor are available. For adjacent equal endpoints, the array cell below the diagonal is a valid zero-initialized representation of the empty interior. The final return is `dp[0][n - 1]`.

**Resource analysis and edge cases.** Time and space are both `O(n²)`. A singleton returns 1; an equal pair returns 2; distinct letters may force the answer down to 1. Empty original strings are outside the official domain, and this retained implementation assumes nonempty input.

**Defense point.** “Deletion is permitted, so mismatched endpoints lead to maximization over two omissions. Equal endpoints contribute two around the best interior; interval length supplies the induction.”

## 08 — Palindromic Substrings

[Task/reference](Problem08.md) · [Code](src/Problem08.java)

**Specification and design.** Count palindromic contiguous occurrences, identified by their start and end positions. Optimizing a subsequence length would answer a different question. A boolean predicate for each interval directly represents what must be counted.

**State, bases and recurrence.** Let `P(i,j)` mean that `s[i..j]` is a palindrome. For `i <= j`,

```text
P(i,j) = (s[i] == s[j]) AND (j - i < 2 OR P(i + 1,j - 1)).
answer = sum of 1 for every true P(i,j).
```

Length-one intervals are true. Length-two intervals require only equal ends. Longer intervals require an already solved interior. The Java boolean table starts false and the counter starts zero.

**Correctness argument.** For length at least three, equal ends and a palindromic interior are necessary and sufficient for the whole interval to be a palindrome. The short-length clause gives the correct bases. Induction on interval length proves every boolean result. Every nonempty substring corresponds to exactly one pair `(i,j)`, and the loops visit that pair exactly once; incrementing for each true state therefore counts occurrences without omission or double counting.

**Worked example: `"abba"`.** There are four length-one palindromes, one length-two palindrome `bb`, no length-three palindromes, and one length-four palindrome `abba`. The final answer is `4 + 1 + 0 + 1 = 6`. In particular, the two occurrences of `a` are different intervals and both count. The full interval is true because its ends are `a` and its interior `bb` is true.

**Implementation and resource analysis.** Decreasing start indices ensure the interior row is ready; increasing end indices enumerate only `i <= j`. Short-circuit evaluation avoids an interior lookup for length one or two. The code visits exactly `n(n + 1)/2` intervals and allocates an `n × n` boolean table: `O(n²)` time and `O(n²)` space. At maximum length 1000, even an all-equal string has only 500,500 counted intervals, within `int`.

**Domain and edge cases.** Equal adjacent letters exercise the even-length base; distinct letters contribute only singleton intervals. Overlapping or textually equal substrings are still separate occurrences. Original empty strings are outside the stated domain.

**Defense point.** “There is a bijection between substring occurrences and endpoint pairs. The predicate is exact by induction, so counting its true states is exact as well.”

## D. Prefix optimization with constant storage

## 09 — Maximum Subarray

[Task/reference](Problem09.md) · [Code](src/Problem09.java)

**Specification and design.** Return the largest sum of a nonempty contiguous subarray. Contiguity forces an interval ending here to extend from the immediately preceding position, making a single endpoint optimum sufficient.

**States and bases.** `E(i)` is the largest sum among nonempty intervals ending at `i`; `B(i)` is the largest sum among all intervals within indices `0..i`. Initialize `E(0) = B(0) = nums[0]`.

```text
E(i) = max(nums[i], E(i - 1) + nums[i])
B(i) = max(B(i - 1), E(i))
```

This is Kadane's dynamic programming recurrence, with its state array compressed to two scalars.

**Correctness invariant.** Every interval ending at `i` is either a singleton or a previous interval extended by `nums[i]`. The greatest predecessor sum remains greatest after adding the same value, so `E(i)` follows exactly. All intervals in the new prefix either ended earlier or end now, proving `B(i)`. These facts, with the first-element base, prove the invariant after every loop iteration. Returning `best` therefore returns the global optimum.

**Worked example: `[-8,-2,-5]`.** Start with `endingHere = best = -8`. At -2, restart because `max(-2,-10) = -2`, and update best to -2. At -5, `max(-5,-7) = -5`; best remains -2. The result is the singleton `[-2]`. A zero-initialized best would incorrectly allow an empty interval and fail this example.

**Implementation and resource analysis.** The loop begins at index 1 because the first element establishes the base. It updates `endingHere` first, then includes that new value in `best`. One constant-time update per remaining element gives `O(n)` time; two scalar states give `O(1)` auxiliary space. The published maximum length and value magnitude bound any subarray-sum magnitude by `10^9`, so `int` arithmetic is sufficient.

**Domain and edge cases.** All-negative arrays return their maximum element; zero-filled arrays return 0; all-positive arrays select the whole input. A singleton returns that element directly. A prefix or suffix can be optimal. Nonempty input is required, so an empty-array result is not invented.

**Defense point.** “The endpoint state is sufficient because adding one value preserves the ordering of predecessor sums. A separate prefix optimum remembers an interval that may have ended earlier.”

## 10 — House Robber

[Task/reference](Problem10.md) · [Code](src/Problem10.java)

**Specification and design.** Select houses with maximum total value while forbidding adjacent selections. The final house is either selected or omitted, creating two exhaustive cases. Choosing the larger parity sum is insufficient because a valid optimum may use both parities.

**State, bases and recurrence.** Let `F(k)` be the optimum for the first `k` houses. The empty prefix gives `F(0) = 0`; nonnegative values give `F(1) = nums[0]`. For `k >= 2`,

```text
F(k) = max(F(k - 1), F(k - 2) + nums[k - 1]).
```

A virtual `F(-1) = 0` allows the same formula for the first house, matching the code's two zero-initialized rolling variables.

**Correctness argument.** If an optimal selection omits house `k - 1`, its value is at most `F(k - 1)` and that optimum remains legal. If it includes house `k - 1`, it excludes house `k - 2`; all remaining choices lie in the first `k - 2` houses and have optimum `F(k - 2)`. Combining that optimum with the new house is legal. Both candidates are achievable and cover all possibilities, so taking their maximum is exact.

**Worked example: `[2,1,1,2]`.** The prefix optima are `F(0..4) = [0,2,2,3,4]`. The final transition compares skipping for 3 with taking for `F(2) + 2 = 4`. House indices 0 and 3 give 4 and are nonadjacent. Taking all even indices or all odd indices would each give only 3, demonstrating why the recurrence is needed.

**Implementation invariant and resource analysis.** Before zero-based house `i`, `previous = F(i)` and `twoBack = F(i - 1)`. The code computes `current` using both old values before shifting them. This preserves the invariant at the next house, and the final `previous` is `F(n)`. One loop gives `O(n)` time and only three local accumulator variables give `O(1)` auxiliary space. At most 50 houses of value 400 can be selected in a length-100 line, so the answer is at most 20,000.

**Domain and edge cases.** A single zero gives 0; a single positive amount gives that amount. For two houses the answer is their maximum, not their sum. Nonnegative values allow the empty selection and handle zero amounts naturally. Official inputs have at least one house; the implementation incidentally also returns 0 for an empty array.

**Defense point.** “The take/skip partition is exhaustive and both candidates are feasible. Only the two preceding prefix optima are needed, so rolling storage preserves the same DP with constant memory.”

## Verification scope and reviewer checklist

This folder was compiled separately under Java 17 and tested locally. The [test results](tests/RESULTS.md) and [test design](tests/README.md) document actual commands, boundary coverage and independent small-input solvers. Randomized checks support the implementations but do not replace the correctness arguments above, and local success is not an online judge result.

Before submission, the named student should inspect the retained reference code, explain each state without relying on a memorized formula, check the course's attribution requirements, and review the AI-assistance disclosure. A defense should connect the recurrence's cases to the loop order and to the precise time and memory bounds of the code provided here.
