---
layout: page
title: User Guide
---

CLI-Tacts is a **desktop app for managing CS2040S tutorial groups and student information, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, CLI-Tacts helps you keep up with real-time teaching better than traditional GUI-only grading portals.

The primary users are **CS2040S Teaching Assistants** who:

- manage multiple tutorial or lab groups concurrently
- need to **take attendance quickly** and look up student details on the spot
- prefer keyboard-driven workflows during lab sessions

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest CLI-Tacts `.jar` file from your module team or release page.

1. Copy the file to the folder you want to use as the _home folder_ for CLI-Tacts.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar clitacts.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe i/A0123456X e/johnd@u.nus.edu p/98765432 th/@johndoe t/T01` : Adds student `John Doe` to CLI-Tacts.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [th/TELE_HANDLE]` can be used as `n/John Doe th/@johndoe` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  Currently, CLI-Tacts uses a **single tutorial group** per student, so you will not see repeated `t/` prefixes.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a student to CLI-Tacts with all fields required for CS2040S administration.

Format:

`add n/NAME i/STUDENT_ID e/EMAIL p/PHONE_NUMBER th/TELE_HANDLE t/TUTORIAL_GROUP`

Where:

- `STUDENT_ID` must match `AxxxxxxxY` (e.g. `A0123456X`)
- `EMAIL` must end with `@u.nus.edu`
- `PHONE_NUMBER` must be exactly 8 digits
- `TELE_HANDLE` must start with `@`
- `TUTORIAL_GROUP` must be `T` followed by 2 digits (e.g. `T01`, `T12`)

Examples:

* `add n/Amy Bee i/A0123456X e/amy@u.nus.edu p/11111111 th/@amy_bee t/T01`
* `add n/Bob Chan i/A0765432Y e/bobchan@u.nus.edu p/99998888 th/@bobchan t/T02`

![add command](images/addCommand.png)

### Listing all persons : `list`

Shows a list of all students currently in CLI-Tacts.

Format: `list`

### Editing a person : `edit`

Edits an existing student in CLI-Tacts.

Format:

`edit INDEX [n/NAME] [i/STUDENT_ID] [e/EMAIL] [p/PHONE] [th/TELE_HANDLE] [t/TUTORIAL_GROUP]`

* Edits the student at the specified `INDEX`. The index refers to the index number shown in the displayed student list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* `STUDENT_ID`, `EMAIL`, `PHONE`, `TELE_HANDLE` and `TUTORIAL_GROUP` are validated with the same rules as in `add`.

Examples:

*  `edit 1 p/91234567 e/alice_new@u.nus.edu` Edits the phone number and email address of the 1st student.
*  `edit 2 t/T03` Moves the 2nd student to tutorial group `T03`.

![edit command](images/editCommand.png)

### Locating students by name and/or tutorial group: `find`

Allows a TA to **filter the student list** to find specific individuals based on their names and/or tutorial group. This is useful when the matric number (student ID) is not immediately known.

Formats:

* `find n/NAME_KEYWORD`
* `find t/TUTORIAL_GROUP`
* `find n/NAME_KEYWORD t/TUTORIAL_GROUP`

At least one of `n/` or `t/` must be present.

#### Name (`n/`) filter

* **Characters**: the search term should only contain alphanumeric characters, spaces, hyphens (`-`), commas (`,`), and apostrophes (`'`).
* **Case sensitivity**: case-insensitive. `find n/ALEX` is the same as `find n/alex`.
* **Spacing**: leading and trailing spaces are ignored. Multiple internal spaces are treated as one.
* **Prefix matching**: each word you provide after `n/` is treated as a **prefix**. A student matches if their name contains a word that starts with that prefix (case-insensitive). For example, `find n/j` matches students with a name word starting with `j` (e.g. **John Doe**, **Mary Jane**).
* **Multiple words**: if you enter more than one word after `n/` (separated by spaces), **every prefix must match** somewhere in the student's name (logical AND). For example, `find n/John Do` matches **John Doe** but not **John Ong**.

If an invalid name keyword is supplied, CLI-Tacts shows an error similar to:

> Invalid name! Search terms should only contain alphanumeric characters, spaces, hyphens (-), commas (,), and apostrophes (') only.

#### Tutorial group (`t/`) filter

* **Format**: must start with `T` followed by exactly 2 digits, e.g. `T01`, `T12`.  
  (This is the same format used when adding or editing a student.)
* **Case sensitivity**: case-sensitive. `t12` is different from `T12`.

If an invalid tutorial group is supplied, CLI-Tacts shows an error similar to:

> Invalid tutorial group. Format should be T followed by two digits (e.g., T01).

#### Combined filters

* When both `n/` and `t/` are present, CLI-Tacts returns students who **match the name filter AND belong to the specified tutorial group**.
* On success, the status bar shows e.g. `5 persons listed!` and the list shows only matching students.
* If no students match, the list becomes empty and the status shows `0 persons listed!`.

Examples:
* `find n/j` — finds students with a name word starting with `j`.
* `find n/John` — finds students with a name word starting with `John`.
* `find n/John Do` — finds students whose name contains a word starting with `John` and a word starting with `Do`.
* `find t/T01` — finds all students from tutorial group `T01`.
* `find n/Tan` — finds all students with “Tan” in their name (surname or given name).
* `find n/john t/T01` — finds all students with “John” in their name **and** from tutorial group `T01`.

### Deleting a person : `delete`

Deletes the specified student from CLI-Tacts.

Format: `delete INDEX`

* Deletes the student at the specified `INDEX`.
* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Marking attendance : `mark`

Marks a student as present for a specific week during a tutorial session.

Format: `mark INDEX w/WEEK`

Where:
* `INDEX` refers to the index number shown in the displayed student list and **must be a positive integer** 1, 2, 3, …​
* `WEEK` is the week number to mark attendance for and **must be a positive integer**

#### Important notes:
* Each student can be marked as attended only once per week. Attempting to mark the same student for the same week twice will result in an error.
* After marking, the attendance status is updated immediately and saved automatically.
* The `mark` command works with the currently displayed list. If you need to mark a specific student, use `find` first to filter the list, then use the appropriate index.

Examples:
* `mark 1 w/2` — marks the 1st student in the displayed list as attended for week 2.
* `find n/John` followed by `mark 1 w/1` — finds all students named "John" and marks the 1st result as attended for week 1.
* `find t/T01` followed by `mark 3 w/4` — finds all students in tutorial group T01 and marks the 3rd result as attended for week 4.

#### Attendance tracking:
CLI-Tacts tracks attendance on a **per-week basis**. Each week's attendance is stored separately, allowing you to manage attendance records across the entire semester with a single command.

### Clearing all entries : `clear`

Clears all entries from CLI-Tacts.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

CLI-Tacts data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

CLI-Tacts data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file make its format invalid, CLI-Tacts will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause CLI-Tacts to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous CLI-Tacts home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME i/STUDENT_ID e/EMAIL p/PHONE_NUMBER th/TELE_HANDLE t/TUTORIAL_GROUP` <br> e.g., `add n/James Ho i/A0123456X e/jamesho@u.nus.edu p/22224444 th/@jamesho t/T01`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [i/STUDENT_ID] [e/EMAIL] [p/PHONE_NUMBER] [th/TELE_HANDLE] [t/TUTORIAL_GROUP]`<br> e.g.,`edit 2 n/James Lee t/T03`
**Find** | `find n/NAME_KEYWORD [t/TUTORIAL_GROUP]`<br> e.g., `find n/James t/T01`
**List** | `list`
**Mark** | `mark INDEX w/WEEK`<br> e.g., `mark 1 w/2`
**Help** | `help`
