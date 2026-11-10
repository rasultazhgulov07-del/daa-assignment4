# DAA Assignment 4 — Rassul Tazhgulov

Prepared for **Rassul Tazhgulov**: ten complete dynamic programming methods, original task specifications and a [design and correctness report](REPORT.md). The report groups the problems by dependency structure and explains optimal substructure, loop invariants and the resource costs of the actual implementations. It is intended for review before defense and submission.

## Assignment contents

The supplied assignment assigns 5 points to each of ten DP problems and 50 points to the report and defense. The original `Problem01.md` through `Problem10.md` files are preserved byte-for-byte, including their official problem links. Problem 02's original heading says “Jump Game”; its linked task and implemented method are Jump Game II.

| No. | Task description | Implementation | Required method |
|---|---|---|---|
| 01 | [Counting Bits](Problem01.md) | [Problem01.java](src/Problem01.java) | `int[] countBits(int n)` |
| 02 | [Jump Game II](Problem02.md) | [Problem02.java](src/Problem02.java) | `int jump(int[] nums)` |
| 03 | [Min Cost Climbing Stairs](Problem03.md) | [Problem03.java](src/Problem03.java) | `int minCostClimbingStairs(int[] cost)` |
| 04 | [Longest Increasing Subsequence](Problem04.md) | [Problem04.java](src/Problem04.java) | `int lengthOfLIS(int[] nums)` |
| 05 | [Longest Common Subsequence](Problem05.md) | [Problem05.java](src/Problem05.java) | `int longestCommonSubsequence(String text1, String text2)` |
| 06 | [Edit Distance](Problem06.md) | [Problem06.java](src/Problem06.java) | `int minDistance(String word1, String word2)` |
| 07 | [Longest Palindromic Subsequence](Problem07.md) | [Problem07.java](src/Problem07.java) | `int longestPalindromeSubseq(String s)` |
| 08 | [Palindromic Substrings](Problem08.md) | [Problem08.java](src/Problem08.java) | `int countSubstrings(String s)` |
| 09 | [Maximum Subarray](Problem09.md) | [Problem09.java](src/Problem09.java) | `int maxSubArray(int[] nums)` |
| 10 | [House Robber](Problem10.md) | [Problem10.java](src/Problem10.java) | `int rob(int[] nums)` |

Each Java file declares its original `public class ProblemXX` and required method. There are no packages, external libraries, build-system dependencies or network calls. All source is Java 17 compatible. For LeetCode's editor, its wrapper commonly uses the class name `Solution`; follow that editor's instructions separately. The local assignment files retain the required `ProblemXX` class names.

## Compile and test

Use a JDK version 17 or newer and run this PowerShell command **from this folder**:

```powershell
.\tests\run-tests.ps1
```

If the correct JDK is not on `PATH`, the installed JDK used for this review can be selected explicitly on the current computer:

```powershell
.\tests\run-tests.ps1 -JavaHome 'C:\Users\ПК\.jdks\temurin-17.0.17'
```

The script invokes `javac --release 17 -encoding UTF-8 -Xlint:all -Werror`, compiling only this folder's ten source files and its plain Java test harness. It then runs `Assignment4Tests`. Compilation or a failed assertion stops the script with an error. Class files are placed in a unique temporary directory and removed in `finally`; no compiled output is distributed. Tests use explicit `AssertionError` checks, so `-ea` is not required. There are no application `main` methods in the solution classes; the test harness provides the executable entry point.

The review run passed compilation and **102,675 assertions** for this project, including all 23 requested input/output cases. See [test methodology](tests/README.md) and [exact commands and captured output](tests/RESULTS.md). These are local results, not LeetCode submissions or a promised grade.

## Reference reuse and AI assistance

The provided archive `daa-assignment4-korkem07-main.zip` is permitted classmate reference material. Its source project identifier is `daa-assignment4-korkem07-main`; this identifies the source, not the author of this student's report. The original archive remains unchanged in the parent workspace and is not needed to build or test this self-contained folder.

Original archive SHA-256:

```text
D50116F0DE3DC55DB7274F8D832D83A7B45D81C6C6E649DB2EAEE7973EA673B6
```

Problems 01–07 retain the reference Java implementations unchanged after review and local testing. Problems 08–10 replace the reference stubs with interval DP, Kadane's DP and rolling-prefix DP respectively. The reference README and report informed the review; this folder has a newly prepared README and report. AI assistance contributed implementation work for 08–10, algorithm review, report drafting, test development and packaging. Shared implementations and tests may match those in the other student's folder; no artificial variations were introduced to disguise reuse.

This is material for the named student's review and defense. It makes no claim of independent authorship or personal judge activity. Before submission, review the code and report, confirm the course's permitted-use and attribution rules, and be ready to explain every recurrence. The supplied task files do not establish any further teacher requirements.

## Folder map

```text
README.md                   project identity, task links and instructions
REPORT.md                   all ten explanations and defense notes
Problem01.md ... Problem10.md original task specifications and references
src/Problem01.java ... Problem10.java
tests/Assignment4Tests.java  standalone Java harness
tests/run-tests.ps1          isolated compilation, execution and cleanup
tests/README.md              test design and official input domains
tests/RESULTS.md             actual validation commands and results
```

The supplied folder and its ZIP contain the same files. No sibling project is needed. No Git commits, remotes, repositories or pushes were created for this work.
