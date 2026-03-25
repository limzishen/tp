package seedu.address.model.person;

/**
 * Represents a person's attendance record for a semester.
 * Each week can be marked as attended or not attended.
 */
public class Attendance {
    public static final int MAX_WEEKS = 13;
    private final boolean[] attendance;

    public Attendance() {
        this.attendance = new boolean[MAX_WEEKS];
    }

    /**
     * Creates a copy of the given attendance record.
     * @param attendanceRecord array of attendance status for each week
     */
    private Attendance(boolean[] attendanceRecord) {
        this.attendance = attendanceRecord.clone();
    }

    /**
     * Returns a copy of this Attendance with the specified week marked.
     * @param week the week number to mark (1-13)
     * @return a new Attendance instance with the week marked
     */
    public Attendance createCopyWithMarkedWeek(int week) {
        boolean[] newRecord = attendance.clone();
        if (week >= 1 && week <= MAX_WEEKS) {
            newRecord[week - 1] = true;
        }
        return new Attendance(newRecord);
    }

    /**
     * Returns a copy of this Attendance with the specified week unmarked.
     * @param week the week number to unmark (1-13)
     * @return a new Attendance instance with the week unmarked
     */
    public Attendance createCopyWithUnmarkedWeek(int week) {
        boolean[] newRecord = attendance.clone();
        if (week >= 1 && week <= MAX_WEEKS) {
            newRecord[week - 1] = false;
        }
        return new Attendance(newRecord);
    }

    /**
     * Marks the specified week as attended.
     * @param week the week number to mark (1-13)
     */
    public void markWeek(int week) {
        if (week >= 1 && week <= MAX_WEEKS) {
            attendance[week - 1] = true;
        }
    }

    /**
     * Unmarks the specified week as attended.
     * @param week the week number to unmark (1-13)
     */
    public void unmarkWeek(int week) {
        if (week >= 1 && week <= MAX_WEEKS) {
            attendance[week - 1] = false;
        }
    }

    /**
     * Checks if the specified week is marked as attended.
     * @param week the week number to check (1-13)
     * @return true if the week is marked as attended, false otherwise
     */
    public boolean isMarked(int week) {
        if (week >= 1 && week <= MAX_WEEKS) {
            return attendance[week - 1];
        }
        return false;
    }

    public boolean[] getAttendanceRecord() {
        return attendance.clone();
    }

    /**
     * Returns a string representation of the attendance record.
     * Each week is represented as "Attended" or "Absent" based on the
     * attendance status for that week.
     */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MAX_WEEKS; i++) {
            sb.append("Week ").append(i + 1).append(": ").append(attendance[i] ? "Attended" : "Absent").append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Attendance)) {
            return false;
        }

        Attendance otherAttendance = (Attendance) other;
        for (int i = 0; i < MAX_WEEKS; i++) {
            if (this.attendance[i] != otherAttendance.attendance[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (boolean weekAttendance : attendance) {
            result = 31 * result + (weekAttendance ? 1 : 0);
        }
        return result;
    }
}
