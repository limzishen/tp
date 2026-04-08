[![codecov](https://codecov.io/gh/AY2526S2-CS2103T-T13-2/tp/graph/badge.svg?token=M0OYDBZOB6)](https://codecov.io/gh/AY2526S2-CS2103T-T13-2/tp)

[![Java CI](https://github.com/AY2526S2-CS2103T-T13-2/tp/actions/workflows/gradle.yml/badge.svg)](https://github.com/AY2526S2-CS2103T-T13-2/tp/actions/workflows/gradle.yml)

# CLI-Tacts

![Ui](docs/images/Ui.png)

**CLI-Tacts** (Command Line Interface Contacts) is a desktop application designed for **CS2040S Teaching Assistants** who manage multiple tutorial groups and prefer fast, keyboard-driven workflows.

Instead of juggling fragmented spreadsheets or slow GUI-based portals, CLI-Tacts gives TAs a single, locally-stored system to manage student records and track attendance in real time — without ever leaving the keyboard.

---

## Target User

CLI-Tacts is built for university Teaching Assistants (TAs) in CS2040S who:

- Manage 3–5 tutorial groups of ~20 students each
- Need to mark or unmark attendance quickly during live sessions
- Are comfortable with CLI tools and value speed over clicking
- Prefer locally-stored data over cloud-dependent portals

---

## Value Proposition

CLI-Tacts helps TAs efficiently manage student details and attendance across multiple tutorial groups through a fast CLI. By centralising student records locally, TAs can perform administrative tasks — adding, finding, marking/unmarking attendance, and removing students — without disrupting their real-time teaching flow.

**What CLI-Tacts does:**
- Add and manage student records (name, student ID, email, phone, Telegram handle, tutorial group)
- Edit or delete students using the displayed list index
- Search by name keywords and/or tutorial group (`find`)
- Mark attendance for one student (by index) or **mark an entire tutorial group at once**
- Automatically save all changes to disk

**What CLI-Tacts does NOT do:**
- Replace university administrative or grading systems
- Handle assignment grading or submissions
- Communicate directly with students
- Manage students outside a TA's assigned groups
- Integrate with university authentication systems

---

## Features

### 1. Add a Student

```
add n/<NAME> i/<STUDENT_ID> e/<EMAIL> p/<PHONE> th/<TELE_HANDLE> t/<TUTORIAL_GROUP>
```

Example: `add n/John Doe i/A0123456X e/johnd@u.nus.edu p/98765432 th/@john_doe t/T12`

All fields are required. Duplicate student IDs (same identity in the address book) are rejected.

---

### 2. Delete a Student

```
delete INDEX
```

Example: `delete 2` — deletes the 2nd student in the **currently displayed** list (`list` or `find` first as needed).

---

### 3. Find Students

```
find [n/<NAME_KEYWORD>] [t/<TUTORIAL_GROUP>] [e/<EMAIL>] [th/<TELE_HANDLE>]
```

At least one of `n/`, `t/`, `e/`, or `th/` must be present. Filters can be combined.

**Name filter:** Each search term matches the **start** of any word in the student's name (case-insensitive).
- Example: `find n/john` matches "John Doe"

**Tutorial group filter:**
- Example: `find t/T01` finds all students in tutorial group T01

**Email & Telegram handle filters:** Prefix matching (case-insensitive).
- Example: `find e/cha` matches `charlotte@u.nus.edu`; `find th/@ro` matches `@roybala`

**Combined filters:**
- Same field type (multiple `n/` fields): **OR** logic
- Different field types: **AND** logic
- Example: `find n/john t/T01` finds students named "john" **in** T01

Examples:
- `find n/John`
- `find n/John Do`
- `find t/T01`
- `find e/alice@u.nus.edu`
- `find n/john t/T01`

---

### 4. Mark Attendance

CLI-Tacts supports three ways to mark attendance for a given week (1–13):

**One student** (index is relative to the **current** filtered list):

```
mark INDEX w/<WEEK>
```

Example: `mark 1 w/3`

**Multiple students** (space-separated indices):

```
mark INDEX1 INDEX2 ... w/<WEEK>
```

Example: `mark 1 2 3 w/5` — marks students at positions 1, 2, 3. Students already marked are **skipped** (no error).

**All students in a tutorial group** (everyone in storage with that group; does not depend on `find`):

```
mark t/<TUTORIAL_GROUP> w/<WEEK>
```

Example: `mark t/T02 w/2` — marks all students in `T02` for week 2. Already-marked students for that week are **skipped** (no error). If no student has that group, an error is shown.

Tutorial groups use `T` plus two digits (e.g. `T01`, `T12`).

---

### 5. Unmark Attendance

**Unmark one student** (by index):

```
unmark INDEX w/<WEEK>
```

Example: `unmark 1 w/2` — unmarks the 1st student for week 2.

**Unmark all students in a tutorial group:**

```
unmark t/<TUTORIAL_GROUP> w/<WEEK>
```

Example: `unmark t/T01 w/4` — unmarks all students in T01 for week 4.

---

### 6. Attendance Statistics Panel

The attendance statistics panel at the bottom of the main window shows:
- Attendance rate per tutorial group for each week (W1–W13)
- Overall attendance rate per group
- Global attendance statistics

The panel updates automatically whenever the student list changes.

---

### 7. Export Student Data

```
export
```

Exports all student data to a CSV file named `export.csv` in the same folder as the JAR file.

The CSV contains columns: Student, StudentID, Email, Tutorial, Week1–Week13
- Each week column shows `1` (present) or `0` (absent)
- The export includes **all** students regardless of any active `find` filter
- If `export.csv` exists, it will be overwritten

---

### 8. Other Commands

- **List:** `list` — show all students
- **Edit:** `edit INDEX [n/NAME] [i/STUDENT_ID] [e/EMAIL] [p/PHONE] [th/TELE_HANDLE] [t/TUTORIAL_GROUP]` — at least one field required
- **Clear:** `clear` — remove all entries
- **Help:** `help` — open help
- **Exit:** `exit`

---

### Command History

Press `↑` (Up Arrow) to cycle to older commands, or `↓` (Down Arrow) to cycle to newer commands. This makes it easy to repeat and edit recently used commands.

---

### 9. Auto-Save

All data-changing commands save automatically. By default the address book is stored at **`data/addressbook.json`** (under the app’s home folder).
If the data file is missing, CLI-Tacts initializes it from **`data/addressbook.sample.json`**.

---

## Command Summary

| Action | Format |
|--------|--------|
| Add | `add n/NAME i/STUDENT_ID e/EMAIL p/PHONE [th/TELE_HANDLE] t/TUTORIAL_GROUP` |
| Delete | `delete INDEX` |
| Edit | `edit INDEX [n/NAME] [i/STUDENT_ID] [e/EMAIL] [p/PHONE] [th/TELE_HANDLE] [t/TUTORIAL_GROUP]` |
| Find | `find [n/NAME] [t/TUTORIAL_GROUP] [e/EMAIL] [th/TELE_HANDLE]` |
| List | `list` |
| Mark (one) | `mark INDEX w/WEEK` |
| Mark (multiple) | `mark INDEX1 INDEX2 ... w/WEEK` |
| Mark (group) | `mark t/TUTORIAL_GROUP w/WEEK` |
| Unmark (one) | `unmark INDEX w/WEEK` |
| Unmark (group) | `unmark t/TUTORIAL_GROUP w/WEEK` |
| Export | `export` |
| Clear | `clear` |
| Help | `help` |
| Exit | `exit` |

---

## Notes on Command Format

- Words in `UPPER_CASE` are parameters (e.g. `n/NAME` → `n/John Doe`).
- Unless stated otherwise, parameters can be reordered.
- Extraneous parameters on `list`, `help`, `exit`, `clear` are ignored.

---

## Documentation

For full documentation, see the [CLI-Tacts Product Website](https://AY2526S2-CS2103T-T13-2.github.io/tp/).

---

## Acknowledgements

This project is based on the [AddressBook-Level3](https://se-education.org/addressbook-level3) project created by the [SE-EDU initiative](https://se-education.org).
