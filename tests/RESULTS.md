# Executed validation — Rassul Tazhgulov

Date: **2026-11-10** (Asia/Qyzylorda client date).

Working directory:

```text
C:\Users\ПК\Desktop\DAA_Assignment4_TwoStudents
```

The following command was executed in PowerShell for this project. Its complete output, including the exact compiler and runtime commands emitted by the runner, follows below. The processes completed successfully; the compiler produced no diagnostics under `-Xlint:all -Werror`. Runtime: Temurin OpenJDK 17.0.17. Both the project and this evidence are local; no LeetCode submission was made.

```powershell
& '.\Rassul_Tazhgulov\tests\run-tests.ps1' -JavaHome 'C:\Users\ПК\.jdks\temurin-17.0.17' *> '.\.assignment4_work\Rassul_test_log.txt'; $passed = $?; Get-Content -LiteralPath '.\.assignment4_work\Rassul_test_log.txt'; if (-not $passed) { exit 1 }
```

The redirect destination above was a temporary review log outside this project; the captured contents are preserved here. For a portable rerun after moving/extracting this project, follow [README.md](../README.md) from the project folder and use its own runner. Temporary class output directories shown below were removed automatically after execution.

## Captured output

```text
& 'C:\Users\ПК\.jdks\temurin-17.0.17\bin\javac.exe' '-version'
javac 17.0.17
& 'C:\Users\ПК\.jdks\temurin-17.0.17\bin\java.exe' '--version'
openjdk 17.0.17 2025-10-21
OpenJDK Runtime Environment Temurin-17.0.17+10 (build 17.0.17+10)
OpenJDK 64-Bit Server VM Temurin-17.0.17+10 (build 17.0.17+10, mixed mode, sharing)
& 'C:\Users\ПК\.jdks\temurin-17.0.17\bin\javac.exe' '--release' '17' '-encoding' 'UTF-8' '-Xlint:all' '-Werror' '-d' 'C:\Users\ПК\AppData\Local\Packages\openai.codex_2p2nqsd0c76g0\AC\#!sandbox.{10bb455c-af25-4b35-9a36-1836fae03520}\Temp\DAA_Assignment4_f77c9d4f008941828df26d1a464ba973' 'C:\Users\ПК\Desktop\DAA_Assignment4_TwoStudents\Rassul_Tazhgulov\src\Problem01.java' 'C:\Users\ПК\Desktop\DAA_Assignment4_TwoStudents\Rassul_Tazhgulov\src\Problem02.java' 'C:\Users\ПК\Desktop\DAA_Assignment4_TwoStudents\Rassul_Tazhgulov\src\Problem03.java' 'C:\Users\ПК\Desktop\DAA_Assignment4_TwoStudents\Rassul_Tazhgulov\src\Problem04.java' 'C:\Users\ПК\Desktop\DAA_Assignment4_TwoStudents\Rassul_Tazhgulov\src\Problem05.java' 'C:\Users\ПК\Desktop\DAA_Assignment4_TwoStudents\Rassul_Tazhgulov\src\Problem06.java' 'C:\Users\ПК\Desktop\DAA_Assignment4_TwoStudents\Rassul_Tazhgulov\src\Problem07.java' 'C:\Users\ПК\Desktop\DAA_Assignment4_TwoStudents\Rassul_Tazhgulov\src\Problem08.java' 'C:\Users\ПК\Desktop\DAA_Assignment4_TwoStudents\Rassul_Tazhgulov\src\Problem09.java' 'C:\Users\ПК\Desktop\DAA_Assignment4_TwoStudents\Rassul_Tazhgulov\src\Problem10.java' 'C:\Users\ПК\Desktop\DAA_Assignment4_TwoStudents\Rassul_Tazhgulov\tests\Assignment4Tests.java'
PASS compilation: Java 17 release, all lint warnings treated as errors
& 'C:\Users\ПК\.jdks\temurin-17.0.17\bin\java.exe' '-cp' 'C:\Users\ПК\AppData\Local\Packages\openai.codex_2p2nqsd0c76g0\AC\#!sandbox.{10bb455c-af25-4b35-9a36-1836fae03520}\Temp\DAA_Assignment4_f77c9d4f008941828df26d1a464ba973' 'Assignment4Tests'
PASS 01 Counting Bits: 100006 assertions
PASS 02 Jump Game II: 308 assertions
PASS 03 Min Cost Climbing Stairs: 306 assertions
PASS 04 Longest Increasing Subsequence: 306 assertions
PASS 05 Longest Common Subsequence: 307 assertions
PASS 06 Edit Distance: 209 assertions
PASS 07 Longest Palindromic Subsequence: 308 assertions
PASS 08 Palindromic Substrings: 307 assertions
PASS 09 Maximum Subarray: 309 assertions
PASS 10 House Robber: 309 assertions
PASS: 102675 assertions; all 10 problems; seed=20261110
```

## Outcome

- Separate compilation of this project's ten solution files and harness: **PASS**, Java 17 release target, all lint warnings treated as errors.
- Execution: **PASS**, all 10 problem groups, **102,675 assertions**, including **2,600 seeded small-input comparisons**.
- The prescribed 23 input/output cases are explicitly included. Full test-domain coverage and oracle design are documented in [tests/README.md](README.md).
- No failing case was suppressed, and no external library was required. This is local verification rather than online judge acceptance.
