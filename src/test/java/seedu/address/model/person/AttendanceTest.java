package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AttendanceTest {

    @Test
    public void constructor_createsEmptyAttendance() {
        Attendance attendance = new Attendance();
        // All weeks should be unmarked by default
        for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
            assertFalse(attendance.isMarked(week));
        }
    }

    @Test
    public void markWeek_validWeek_marksSuccessfully() {
        Attendance attendance = new Attendance();
        attendance.markWeek(1);
        assertTrue(attendance.isMarked(1));
    }

    @Test
    public void markWeek_multipleValidWeeks_marksSuccessfully() {
        Attendance attendance = new Attendance();
        attendance.markWeek(1);
        attendance.markWeek(3);
        attendance.markWeek(5);

        assertTrue(attendance.isMarked(1));
        assertTrue(attendance.isMarked(3));
        assertTrue(attendance.isMarked(5));
        assertFalse(attendance.isMarked(2));
        assertFalse(attendance.isMarked(4));
    }

    @Test
    public void markWeek_allValidWeeks_marksAll() {
        Attendance attendance = new Attendance();
        for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
            attendance.markWeek(week);
        }

        for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
            assertTrue(attendance.isMarked(week));
        }
    }

    @Test
    public void markWeek_invalidWeekZero_doesNotMark() {
        Attendance attendance = new Attendance();
        assertThrows(AssertionError.class, () -> attendance.markWeek(0));
    }

    @Test
    public void markWeek_invalidWeekNegative_doesNotMark() {
        Attendance attendance = new Attendance();
        assertThrows(AssertionError.class, () -> attendance.markWeek(-1));
    }

    @Test
    public void markWeek_invalidWeekAboveMax_doesNotMark() {
        Attendance attendance = new Attendance();
        assertThrows(AssertionError.class, () -> attendance.markWeek(14));
    }

    @Test
    public void markWeek_boundaryWeekOne_marksSuccessfully() {
        Attendance attendance = new Attendance();
        attendance.markWeek(1);
        assertTrue(attendance.isMarked(1));
    }

    @Test
    public void markWeek_boundaryWeekMax_marksSuccessfully() {
        Attendance attendance = new Attendance();
        attendance.markWeek(Attendance.MAX_WEEKS);
        assertTrue(attendance.isMarked(Attendance.MAX_WEEKS));
    }

    @Test
    public void isMarked_invalidWeekZero_returnsFalse() {
        Attendance attendance = new Attendance();
        assertThrows(AssertionError.class, () -> attendance.isMarked(0));
    }

    @Test
    public void isMarked_invalidWeekNegative_returnsFalse() {
        Attendance attendance = new Attendance();
        assertThrows(AssertionError.class, () -> attendance.isMarked(-5));
    }

    @Test
    public void isMarked_invalidWeekAboveMax_returnsFalse() {
        Attendance attendance = new Attendance();
        assertThrows(AssertionError.class, () -> attendance.isMarked(14));
    }

    @Test
    public void isMarked_unmarkedWeek_returnsFalse() {
        Attendance attendance = new Attendance();
        attendance.markWeek(2);
        assertFalse(attendance.isMarked(1));
    }

    @Test
    public void toString_emptyAttendance_displaysAbsentForAllWeeks() {
        Attendance attendance = new Attendance();
        String result = attendance.toString();

        for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
            assertTrue(result.contains("Week " + week + ": Absent"));
        }
    }

    @Test
    public void toString_markedWeeks_displaysAttendedForMarkedWeeks() {
        Attendance attendance = new Attendance();
        attendance.markWeek(1);
        attendance.markWeek(5);
        attendance.markWeek(13);

        String result = attendance.toString();

        assertTrue(result.contains("Week 1: Attended"));
        assertTrue(result.contains("Week 2: Absent"));
        assertTrue(result.contains("Week 5: Attended"));
        assertTrue(result.contains("Week 13: Attended"));
    }

    @Test
    public void toString_allMarkedWeeks_displaysAttendedForAll() {
        Attendance attendance = new Attendance();
        for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
            attendance.markWeek(week);
        }

        String result = attendance.toString();

        for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
            assertTrue(result.contains("Week " + week + ": Attended"));
        }
    }

    @Test
    public void markWeek_sameWeekMultipleTimes_remainsMarked() {
        Attendance attendance = new Attendance();
        attendance.markWeek(5);
        attendance.markWeek(5);
        attendance.markWeek(5);

        assertTrue(attendance.isMarked(5));
    }

    @Test
    public void unmarkWeek_validWeek_unmarksSuccessfully() {
        Attendance attendance = new Attendance();
        attendance.markWeek(2);
        attendance.unmarkWeek(2);
        assertFalse(attendance.isMarked(2));
    }

    @Test
    public void unmarkWeek_invalidWeek_doesNotChange() {
        Attendance attendance = new Attendance();
        attendance.markWeek(1);
        assertThrows(AssertionError.class, () -> attendance.unmarkWeek(0));
        assertThrows(AssertionError.class, () -> attendance.unmarkWeek(-1));
        assertThrows(AssertionError.class, () -> attendance.unmarkWeek(Attendance.MAX_WEEKS + 1));
        assertTrue(attendance.isMarked(1));
    }

    @Test
    public void createCopyWithUnmarkedWeek_unmarksCopyOnly() {
        Attendance attendance = new Attendance();
        attendance.markWeek(3);

        Attendance updatedAttendance = attendance.createCopyWithUnmarkedWeek(3);
        assertFalse(updatedAttendance.isMarked(3));
        assertTrue(attendance.isMarked(3));
    }

    @Test
    public void maxWeeksConstant_isThirteen() {
        assertEquals(Attendance.MAX_WEEKS, 13);
    }
}
