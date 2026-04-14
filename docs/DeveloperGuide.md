---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project adapts the architecture and tooling from [se-edu/addressbook-level3](https://github.com/se-edu/addressbook-level3).
* Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit 5](https://github.com/junit-team/junit5)

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<div class="diagram-scroll">
  <img src="images/ArchitectureDiagram.png" alt="Architecture diagram of CLI-Tacts" />
</div>

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S2-CS2103T-T13-2/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S2-CS2103T-T13-2/tp/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<div class="diagram-scroll">
  <img src="images/ArchitectureSequenceDiagram.png" alt="Sequence diagram for delete command" />
</div>

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point).

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent other components from being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<div class="diagram-scroll">
  <img src="images/ComponentManagers.png" alt="Component Manager classes" />
</div>

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S2-CS2103T-T13-2/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

<div class="diagram-scroll">
  <img src="images/UiClassDiagram.png" alt="Structure of the UI Component" />
</div>

The UI consists of a `MainWindow` that is made up of parts e.g. `CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFX UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S2-CS2103T-T13-2/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S2-CS2103T-T13-2/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S2-CS2103T-T13-2/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<div class="diagram-scroll">
  <img src="images/LogicClassDiagram.png" alt="Partial class diagram of the Logic component" />
</div>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a student).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<div class="diagram-scroll">
  <img src="images/ParserClasses.png" alt="Parser classes in the Logic component" />
</div>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2526S2-CS2103T-T13-2/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<div class="diagram-scroll">
  <img src="images/ModelClassDiagram.png" alt="Class diagram of the Model component" />
</div>


The `Model` component,

* stores the CLI-Tacts data i.e., all `Person` objects (which are contained in a `UniquePersonList` object). Each `Person` corresponds to a CS2040S student and has `Name`, `Phone`, `Email`, `TeleHandle`, `StudentId`, `TutorialGroup`, and `Attendance` (per-week flags).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPrefs` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** AddressBook Level 3 discussed an alternative model with a shared `Tag` list. CLI-Tacts instead uses a single `TutorialGroup` value per `Person`, which simplifies the model for the CS2040S context where each student belongs to one tutorial group at a time.

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S2-CS2103T-T13-2/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<div class="diagram-scroll">
  <img src="images/StorageClassDiagram.png" alt="Class diagram of the Storage component" />
</div>

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefsStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

CLI-Tacts keeps the same overall architecture as AddressBook Level 3 (UI → Logic → Model; Logic → Storage) while using a CS2040S–oriented domain (`Person` with student ID, tutorial group, and per-week attendance).

Feature behaviour (**add**, **delete**, **edit**, **find**, **mark**, **unmark**, **list**, **clear**, etc.) is specified in the [User Guide](UserGuide.md) and summarised under [Appendix: Requirements](#appendix-requirements) (user stories and use cases). **Undo/redo** and **versioned address book history** are not part of this product; each modifying command updates the current `Model` and `Storage` persists to JSON as usual.

### Find command

The `find` command is parsed by [`FindCommandParser`](https://github.com/AY2526S2-CS2103T-T13-2/tp/tree/master/src/main/java/seedu/address/logic/parser/FindCommandParser.java). It builds a [`NameAndTutorialGroupPredicate`](https://github.com/AY2526S2-CS2103T-T13-2/tp/tree/master/src/main/java/seedu/address/model/person/NameAndTutorialGroupPredicate.java) (and [`FindCommand`](https://github.com/AY2526S2-CS2103T-T13-2/tp/tree/master/src/main/java/seedu/address/logic/commands/FindCommand.java) updates the model’s filtered student list).

**Name (`n\`)**

* Search terms are **prefix-matched** against the words in each student's full name, **case-insensitively**. The full name is split on whitespace into words.
* A student matches only if **all** provided search words match (**AND** logic): for every word, [`StringUtil#containsWordPrefixIgnoreCase`](https://github.com/AY2526S2-CS2103T-T13-2/tp/tree/master/src/main/java/seedu/address/commons/util/StringUtil.java) must succeed on the full name. Each search word must be a prefix of **at least one** word in that name; mid-word substrings do not count (e.g. `ohn` does not match `John`).
* Multiple `n\` fields are handled the same as writing all those words in a single `n\` value: the parser collects **all** whitespace-separated tokens from every `n\` argument into one keyword list.

**Other filters**

* `t\`: exact tutorial group string (case-insensitive for the value).
* `e\` and `th\`: one or more prefix tokens each (OR within that filter type); [`StringUtil#startsWithIgnoreCase`](https://github.com/AY2526S2-CS2103T-T13-2/tp/tree/master/src/main/java/seedu/address/commons/util/StringUtil.java) on the full email string and on the optional Telegram handle respectively.

**Combining filters**

* When `n\`, `t\`, `e\`, and/or `th\` are used together, a student must match **every** category that has at least one token (logical **AND** across those categories).

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a university student Teaching Assistant for CS2040S managing multiple tutorial/lab groups
* needs to track student details and mark or unmark attendance quickly during live classes
* prefers keyboard-only workflows and can type fast
* finds GUI-based portals/spreadsheets too slow for real-time classroom administration
* needs to organize students by tutorial/lab session for quick lookup

**Value proposition**: CLI-Tacts helps CS2040S Teaching Assistants manage students and attendance quickly via CLI by centralising student details and tutorial groupings locally, enabling fast administrative actions (including marking and unmarking attendance) during tutorials without disrupting teaching flow.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​ | I want to …​ | So that I can…​ |
| --- | --- | --- | --- |
| `* * *` | CS2040S Teaching Assistant | add a student with name, student ID, email, phone, Telegram handle, and tutorial group | set up my tutorial groups at the start of the semester |
| `* * *` | CS2040S Teaching Assistant | edit a student’s contact details | keep records accurate when details change |
| `* * *` | CS2040S Teaching Assistant | delete a student by their index in the displayed list | remove students who drop the module or switch classes |
| `* * *` | CS2040S Teaching Assistant | find students by name (word-prefix match, case-insensitive) | locate a student quickly during class |
| `* * *` | CS2040S Teaching Assistant | find students by tutorial group, email, or Telegram handle | locate a student using different criteria |
| `* * *` | CS2040S Teaching Assistant | combine multiple find filters in one command | narrow down student search results precisely |
| `* * *` | CS2040S Teaching Assistant | list all students | get an overview of who is under my care |
| `* * *` | CS2040S Teaching Assistant | filter the student list by tutorial group | focus only on the current class I'm teaching |
| `* * *` | CS2040S Teaching Assistant | mark a student as present for a specific week | track attendance quickly during live tutorials |
| `* * *` | CS2040S Teaching Assistant | mark multiple specific students in one command | record attendance for selected students without repeating single-student marks |
| `* * *` | CS2040S Teaching Assistant | mark every student in a tutorial group for a week in one command | record whole-class attendance without repeating single-student marks |
| `* * *` | CS2040S Teaching Assistant | unmark a student's attendance for a specific week | correct attendance mistakes quickly during live tutorials |
| `* * *` | CS2040S Teaching Assistant | unmark every student in a tutorial group for a week in one command | correct bulk attendance mistakes without repeating single-student unmarks |
| `* *` | CS2040S Teaching Assistant | navigate through command history using arrow keys | repeat or modify previous commands quickly without retyping |
| `* *` | CS2040S Teaching Assistant | view an attendance statistics panel by tutorial group | monitor attendance rates at a glance |
| `* *` | CS2040S Teaching Assistant | export student and attendance records as a CSV file | back up data or submit attendance reports |
| `*` | CS2040S Teaching Assistant | clear all entries from the application | reset the app cleanly for a new semester |

### Use cases

(For all use cases below, the **System** is the `CLI-Tacts` and the **Actor** is the `user`, unless specified otherwise)

#### Use case: Delete a student
<br>

**MSS**

1.  User requests to list students
2.  CLI-Tacts shows the student list
3.  User requests to delete a specific student in the list
4.  CLI-Tacts removes the student and saves

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. CLI-Tacts shows an error message.

      Use case resumes at step 2.

---

#### Use case: Add student to tutorial group
<br>

**MSS**

1. User requests to add a student, providing name, student ID, email, phone, Telegram handle (optional), and tutorial group (e.g. `add n\John Doe i\A0123456X e\john@u.nus.edu p\98765432 th\@johndoe t\T01`).
2. CLI-Tacts validates all required fields are present and in correct format.
3. CLI-Tacts checks that the student ID, email, and phone are not already in use.
4. CLI-Tacts adds the student to the address book and saves the data locally.
5. CLI-Tacts displays a success message with the student's details.

      Use case ends.

**Extensions**

* 1a. User omits any required field (`n\`, `i\`, `e\`, `p\`, `t\`).
    * 1a1. CLI-Tacts shows the command format with an error message.

      Use case ends.

* 1b. User specifies multiple values for a single-valued field (e.g., `n\John n\Doe` or `i\A0123456X i\A0123456Y`).
    * 1b1. CLI-Tacts shows the duplicate-prefix error (`Multiple values specified for the following single-valued field(s):` …), listing the duplicated prefix(es).

      Use case ends.

* 2a. Any field has an invalid format (e.g., invalid name, invalid student ID, invalid email, invalid phone, invalid tutorial group, invalid Telegram handle).
    * 2a1. CLI-Tacts shows a specific error message describing the constraint violation.

      Use case ends.

* 3a. A student with the same student ID already exists.
    * 3a1. CLI-Tacts shows an error message: "This student ID is already used by another student."

      Use case ends.

* 3b. A student with the same email already exists.
    * 3b1. CLI-Tacts shows an error message: "This email is already used by another student."

      Use case ends.

* 3c. A student with the same phone number already exists.
    * 3c1. CLI-Tacts shows an error message: "This phone number is already used by another student."

      Use case ends.

---

#### Use case: Edit student details
<br>

**MSS**

1. User requests to view students (e.g. `list` or `find`).
2. CLI-Tacts shows the student list.
3. User requests to edit a specific student by index, providing one or more fields to update (e.g., `edit 2 p\99272758 e\newemail@u.nus.edu`).
4. CLI-Tacts validates the provided fields.
5. CLI-Tacts checks that new student ID, email, and phone (if provided) are not already in use by other students.
6. CLI-Tacts updates the student's details and saves.
7. CLI-Tacts displays a success message with the updated student details.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.
    * 3a1. CLI-Tacts shows an error message.

      Use case resumes at step 2.

* 3b. No field is provided for editing.
    * 3b1. CLI-Tacts shows an error message: "At least one field to edit must be provided."

      Use case resumes at step 3.

* 3c. User specifies multiple values for a single-valued field (e.g., `n\John n\Doe` or `p\98765432 p\87654321`).
    * 3c1. CLI-Tacts shows the duplicate-prefix error (`Multiple values specified for the following single-valued field(s):` …), listing the duplicated prefix(es).

      Use case resumes at step 3.

* 4a. Any field has an invalid format.
    * 4a1. CLI-Tacts shows a specific error message describing the constraint violation.

      Use case resumes at step 3.

* 5a. The new student ID is already used by another student.
    * 5a1. CLI-Tacts shows an error message: "This student ID is already used by another student."

      Use case resumes at step 3.

* 5b. The new email is already used by another student.
    * 5b1. CLI-Tacts shows an error message: "This email is already used by another student."

      Use case resumes at step 3.

* 5c. The new phone is already used by another student.
    * 5c1. CLI-Tacts shows an error message: "This phone number is already used by another student."

      Use case resumes at step 3.

---

#### Use case: Mark student attendance
<br>

**MSS**

1. User requests to view students (e.g. `list` or `find`).
2. CLI-Tacts shows the filtered student list with indexes.
3. User requests to mark one or more students using `mark INDEX w\WEEK` or `mark INDEX1 INDEX2 ... w\WEEK`.
4. CLI-Tacts updates the attendance record for the specified student(s).
5. CLI-Tacts confirms the change and refreshes the list display.

      Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The index is invalid.
    * 3a1. CLI-Tacts shows an error message.

      Use case resumes at step 2.

* 3b. The student is already marked for that week (single-student command).
    * 3b1. CLI-Tacts shows an error message.

      Use case resumes at step 2.

* 3c. User marks multiple students by indices with `mark INDEX1 INDEX2 ... w\WEEK`.
    * 3c1. CLI-Tacts validates all provided indices are within bounds.
    * 3c2. CLI-Tacts updates each student in the list; already-marked students for that week are skipped.
    * 3c3. CLI-Tacts reports counts of updated vs already-recorded students.

      Use case ends.

* 3d. Any index provided in the multiple-index command is invalid.
    * 3d1. CLI-Tacts shows an error message and makes no changes.

      Use case resumes at step 2.

* 3e. User specifies multiple values for a single-valued field (e.g., `w\1 w\2`).
    * 3e1. CLI-Tacts shows the duplicate-prefix error (`Multiple values specified for the following single-valued field(s):` …), listing `w\` as a duplicated prefix.

      Use case resumes at step 2.

---

#### Use case: Mark tutorial group attendance
<br>

**MSS**

1. User requests to mark all students in a tutorial group for a specific week (e.g., `mark t\T01 w\3`).
2. CLI-Tacts validates the tutorial group format (3–5 alphanumeric characters).
3. CLI-Tacts checks that at least one student exists with that tutorial group in storage.
4. CLI-Tacts marks each student in that tutorial group for the specified week; students already marked for that week are skipped without error.
5. CLI-Tacts confirms the action and reports: how many students were newly marked and how many were already recorded for that week.

   Use case ends.

**Extensions**

* 2a. The tutorial group format is invalid (e.g., `t\T1` or `t\T01234` or `t\T-01`).
    * 2a1. CLI-Tacts shows an error message describing the valid format.

      Use case ends.

* 3a. No student in storage has the specified tutorial group.
    * 3a1. CLI-Tacts shows an error message of the form `No students found in tutorial group T01.` (the tutorial group label is substituted; no colon after `group`).

      Use case ends.

* 3b. User specifies multiple values for a single-valued field (e.g., `w\1 w\2` or `t\T01 t\T02`).
    * 3b1. CLI-Tacts shows the duplicate-prefix error (`Multiple values specified for the following single-valued field(s):` …), listing the duplicated prefix(es).

      Use case ends.

* 4a. The week number is invalid (not between 1 and 13).
    * 4a1. CLI-Tacts shows: `Week must be a positive integer between 1 and 13.`

      Use case resumes at step 1.

* 4b. All students in the group are already marked for that week.
    * 4b1. CLI-Tacts reports that 0 students were updated and all were already recorded.

      Use case ends.

---

#### Use case: Unmark student attendance
<br>

**MSS**

1. User requests to view students (e.g. `list` or `find`).
2. CLI-Tacts shows the filtered student list with indexes.
3. User requests to unmark a specific student using `unmark INDEX w\WEEK`.
4. CLI-Tacts updates the attendance record for that student.
5. CLI-Tacts confirms the attendance status change.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The user provides an invalid index or week.
  * 3a1. CLI-Tacts shows an error message.

   Use case resumes at step 2.

* 3b. The student has already been unmarked for the specified week.
  * 3b1. CLI-Tacts shows: `This student is already unmarked for this week.`

   Use case resumes at step 2.

* 3c. User specifies multiple values for a single-valued field (e.g., `w\1 w\2`).
  * 3c1. CLI-Tacts shows the duplicate-prefix error (`Multiple values specified for the following single-valued field(s):` …), listing `w\` as a duplicated prefix.

   Use case resumes at step 2.

---

#### Use case: Unmark tutorial group attendance
<br>

**MSS**

1. User requests to unmark all students in a tutorial group for a specific week (e.g., `unmark t\T01 w\2`).
2. CLI-Tacts validates the tutorial group format (3–5 alphanumeric characters).
3. CLI-Tacts checks that at least one student exists with that tutorial group in storage.
4. CLI-Tacts unmarks each student in that tutorial group for the specified week; students already unmarked for that week are skipped without error.
5. CLI-Tacts confirms the action and reports: how many students were updated and how many were already unmarked for that week.

   Use case ends.

**Extensions**

* 2a. The tutorial group format is invalid (e.g., `t\T1` or `t\T01234` or `t\T-01`).
    * 2a1. CLI-Tacts shows an error message describing the valid format.

      Use case ends.

* 3a. No student in storage has the specified tutorial group.
    * 3a1. CLI-Tacts shows an error message of the form `No students found in tutorial group: T01.` (colon after `group`; tutorial group label substituted).

      Use case ends.

* 3b. User specifies multiple values for a single-valued field (e.g., `w\1 w\2` or `t\T01 t\T02`).
    * 3b1. CLI-Tacts shows the duplicate-prefix error (`Multiple values specified for the following single-valued field(s):` …), listing the duplicated prefix(es).

      Use case ends.

* 4a. The week number is invalid (not between 1 and 13).
    * 4a1. CLI-Tacts shows: `Week must be a positive integer between 1 and 13.`

      Use case resumes at step 1.

* 4b. All students in the group are already unmarked for that week.
    * 4b1. CLI-Tacts reports that 0 students were updated and all were already unmarked.

      Use case ends.

---

#### Use case: Find students
<br>

**MSS**

1. User requests to find students using one or more filters (e.g., `find n\john`, `find t\T01`, `find e\alice@`, `find th\@bot`).
2. CLI-Tacts parses the command and validates filter formats.
3. CLI-Tacts updates the displayed list to show only students matching all specified filter criteria.
4. CLI-Tacts displays the filtered list with student details and index numbers in **insertion order** (the order students were added to the application).

   Use case ends.

**Extensions**

* 1a. User provides no filters.
    * 1a1. CLI-Tacts shows the invalid-format message (`Invalid command format!` plus a newline, then the full `find` usage text from `FindCommand.MESSAGE_USAGE`).

      Use case ends.

* 2a. Any filter has invalid format (e.g., invalid tutorial group, invalid email prefix).
    * 2a1. CLI-Tacts shows a specific error message.

      Use case ends.

* 3a. No student matches all the specified criteria.
    * 3a1. CLI-Tacts displays an empty list with status text `0 students listed!` (see `Messages#getPersonsListedOverview` in code).

      Use case ends.

---

#### Use case: List all students
<br>

**MSS**

1. User requests to list all students (e.g., `list`).
2. CLI-Tacts displays the complete student list with index numbers, names, student IDs, emails, phone numbers, Telegram handles (if present), and tutorial groups.
3. The list is displayed in **insertion order** (the order students were added to the application).
4. The list is shown in the main display area.

   Use case ends.

**Extensions**

* 2a. No students are in the address book.
    * 2a1. CLI-Tacts displays an empty list. The status message is `Listed all students`.

      Use case ends.

---

#### Use case: Clear all student data
<br>

**MSS**

1. User requests to clear all entries (e.g., `clear`).
2. CLI-Tacts immediately deletes all student records from the address book.
3. CLI-Tacts saves the empty state to storage.
4. CLI-Tacts displays confirmation message and shows an empty list.

   Use case ends.

**Extensions**

* 1a. User appends extra text after `clear` (e.g. `clear all`).
    * 1a1. CLI-Tacts still executes `clear` and clears all data; the parser does not reject extra arguments (same idea as in the [User Guide](UserGuide.md) for commands that take no parameters).

      Use case ends.

---

#### Use case: Export student data to CSV
<br>

**MSS**

1. User requests to export all student data as a CSV file (e.g., `export`).
2. CLI-Tacts validates the command.
3. CLI-Tacts collects all student data (name, ID, email, tutorial group, attendance for weeks 1-13).
4. CLI-Tacts creates a file named `export.csv` in the application's directory.
5. CLI-Tacts writes all student records as CSV rows with proper formatting (quoted strings, comma-separated values).
6. CLI-Tacts displays a success message of the form `Exported N student(s) to: <file path>` (see `ExportCommand.MESSAGE_SUCCESS`).

   Use case ends.

**Extensions**

* 3a. No students are in the address book.
    * 3a1. CLI-Tacts still creates `export.csv` with only the header row (column names).

      Use case ends.

* 4a. The file `export.csv` already exists.
    * 4a1. CLI-Tacts overwrites the existing file without prompting.

      Use case ends.

* 4b. The application lacks file write permissions in its directory.
    * 4b1. CLI-Tacts shows an error message about file write failure.

      Use case ends.

### Non-Functional Requirements

**Usability**
1.  The system must be fully operable using keyboard-only commands without requiring mouse interaction.
2.  All commands must follow a consistent prefix format where applicable (e.g., `n\`, `i\`, `p\`, `e\`, `t\`, `th\`, `w\`) to ensure predictable command usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  When a command fails, the system must provide clear and informative error messages explaining the issue and the correct command format.

---

**Reliability**

1.  All modifications to student data (`add`, `delete`, `edit`, `mark`, `unmark`, `clear`) must be automatically saved immediately after execution.
2.  On application startup, the system must automatically load previously saved data if the storage file exists and is valid.
3.  After a modifying command completes successfully, changes are persisted; a crash mid-command may leave that command’s effects unsaved.
4.  The system should gracefully handle invalid input commands without crashing.

---

**Compatibility**
1.  The application should run on any mainstream OS.
2.  The application should run on systems with Java 17 or above installed.

---

**Storage**
1.  All student data must be stored locally on the user's machine in a structured format such as **JSON**.

---

**System Constraints**
1.  The system is designed for single-user operation only and does not support concurrent multi-user access.
2.  The application should function fully without requiring an internet connection.

---

**Security**
1.  Student data should remain stored locally and not be transmitted externally.
2.  The application must not transmit any student data to external servers.

---

**Quality**
1. The system should be usable by a teaching assistant who has no prior experience with command line applications after reading the user guide once.
2. A new user should be able to successfully add a student and mark or unmark attendance within 5 minutes of first launching the application.
3. After the relevant list is shown, common actions such as marking or unmarking a student by index should be doable in one command.

### Glossary


* Command: A text instruction typed by the user into the command box to perform an action (e.g., add, delete, find, mark, unmark).

* Command prefix: A token that identifies the value of a field in a command (e.g., `n\` name, `i\` student ID, `p\` phone, `e\` email, `t\` tutorial group, `th\` Telegram handle, `w\` week for mark/unmark).

* Command result: The message returned by the system after executing a command, indicating success or failure.

* Core features: The main functions needed for typical use in tutorials, including add, delete, edit, find, list, mark, unmark, clear, export, loading, and saving.

* CS2040S: A National University of Singapore (NUS) module on Data Structures and Algorithms. CLI-Tacts is designed specifically to assist Teaching Assistants managing student groups and attendance for this module.

* CSV files: Comma-Separated Values files, a simple text-based format for storing tabular data where each row represents a record and columns are separated by commas. CLI-Tacts uses CSV format for exporting student and attendance data.

* Error message: A message displayed to inform the user that a command failed and to explain the reason for the failure.

* Filtered list: A temporary view of the student list that shows only students matching certain criteria (e.g., search results or a tutorial group filter).

* Invalid command format: A command that does not match the required structure or is missing required prefixes or parameters.

* Java runtime: The software environment required to run the application, specifically Java version 17 or above.

* Mainstream OS: Windows, macOS, or Linux operating systems.

* Modifying command: Any command that changes stored data (e.g., add, delete, edit, mark, unmark, clear).

* Offline: The application can be used without an internet connection and without relying on any online services.

* Single-user operation: A usage model where the application is intended to be used by one user on one machine without concurrent access from multiple users.

* Storage file: The local file used to store student records so that data persists between application sessions.

* Structured format: A machine-readable data format with defined fields and structure, such as JSON.

* Student ID: The unique identifier for a student (e.g., A0123456X) used as the primary key for identifying a student in the application.

* Telegram: A cloud-based instant messaging application. In CLI-Tacts, students can have an optional Telegram handle (@-prefixed username, 5–32 characters) for contact purposes.

* Tutorial group: A label used to group students by tutorial or lab session (3–5 alphanumeric characters; letter casing ignored on input, stored uppercase; e.g., `T01`, `CS204`) for filtering and attendance marking/unmarking.

* User guide: The documentation that explains how to use the application's commands and features.

* Validation: The process of checking user input to ensure it matches the required format before executing a command.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test some features of the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file. <br>
     Expected: Shows the GUI with a set of sample students. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Deleting a student

1. Deleting a student while all students are being shown

   1. Prerequisites: List all students using the `list` command. Multiple students in the list.

   1. Test case: `delete 1`<br>
      Expected: First student is deleted from the list. Details of the deleted student shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No student is deleted. Error details shown in the status message.

   1. Other incorrect delete commands to try: `delete`<br>
      Expected: Similar to previous.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

**Team size:** 5

1. **Allow clearing a student’s Telegram handle via `edit`:** Telegram is optional on `add`, but once a handle is saved there is still no supported way to remove it from the CLI.<br><br>
   **Current behaviour (v1.6):** If the user supplies the prefix with **no value** after it (e.g. `edit 3 th\` or only spaces, after trimming), the app still feeds that argument through `TeleHandle` validation. The empty string is invalid, so the command **fails** and the status area shows: `Telegram handle should start with '@' followed by 5 to 32 characters (letters, numbers, underscores).` (see `TeleHandle#MESSAGE_CONSTRAINTS`). The student’s Telegram field is **unchanged**.<br><br>
   The only workarounds are to replace the handle with another valid one or to edit the JSON data file manually.<br><br>
   **Planned change:** Treat a present `th\` prefix with a **trimmed-empty** value as an explicit instruction to **clear** the handle (store as absent / null, same as a student who never had one). Non-empty values will still use existing validation (e.g. `th\@` alone remains invalid). **Sample input (after fix):** `edit 3 th\` for a student who had `th\@alice_ta`. **Sample outcome:** Success message; Telegram hidden in the UI; save omits `TeleHandle` in JSON. Update the User Guide with the new rule and example.
