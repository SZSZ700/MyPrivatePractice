import org.example.Date;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

public class DateTest {

    private Date date;

    @BeforeEach
    public void init() {
        // Arrange
        date = new Date(15, 5, 2024);
    }

    @Test
    public void testConstructorAndGetters_AAA() {
        // Act
        int day = date.getDay();
        int month = date.getMonth();
        int year = date.getYear();

        // Assert
        assertEquals(15, day);
        assertEquals(5, month);
        assertEquals(2024, year);
    }

    @Test
    public void testDefaultConstructor_AAA() {
        // Arrange
        Date d = new Date();

        // Assert
        assertEquals(0, d.getDay());
        assertEquals(0, d.getMonth());
        assertEquals(0, d.getYear());
    }

    @Test
    public void testSettersValid_AAA() {
        // Act
        date.setDay(28);
        date.setMonth(2);
        date.setYear(2023);

        // Assert
        assertEquals(28, date.getDay());
        assertEquals(2, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    @Test
    public void testSetInvalidDay_AAA() {
        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            date.setDay(32); // מאי = 31 ימים
        });
        assertTrue(ex.getMessage().contains("היום חייב"));
    }

    @Test
    public void testSetInvalidMonth_AAA() {
        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            date.setMonth(13);
        });
        assertTrue(ex.getMessage().contains("החודש חייב"));
    }

    @Test
    public void testSetInvalidFebruaryDayInNonLeap_AAA() {
        // Arrange
        date = new Date(28, 2, 2023); // שנה לא מעוברת

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            date.setDay(29);
        });
        assertTrue(ex.getMessage().contains("היום חייב"));
    }

    @Test
    public void testLeapYearFebruary29_AAA() {
        // Arrange + Act
        Date leap = new Date(29, 2, 2024); // שנה מעוברת

        // Assert
        assertEquals(29, leap.getDay());
        assertEquals(2, leap.getMonth());
        assertEquals(2024, leap.getYear());
    }

    @Test
    public void testToString_AAA() {
        // Act
        String str = date.toString();

        // Assert
        assertEquals("15/05/2024", str);
    }

    @Test
    public void testCompareTo_BeforeAfterEqual_AAA() {
        // Arrange
        Date earlier = new Date(14, 5, 2024);
        Date same = new Date(15, 5, 2024);
        Date later = new Date(16, 5, 2024);

        // Assert
        assertEquals(1, date.compareTo(earlier));
        assertEquals(0, date.compareTo(same));
        assertEquals(-1, date.compareTo(later));
    }

    @Test
    public void testCompareToDifferentMonthAndYear_AAA() {
        // Arrange
        Date d1 = new Date(1, 1, 2022);
        Date d2 = new Date(1, 12, 2021);

        // Assert
        assertEquals(1, d1.compareTo(d2));
        assertEquals(-1, d2.compareTo(d1));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 2024",
            "29, 2, 2024",    // שנה מעוברת
            "28, 2, 2023",    // שנה רגילה
            "31, 12, 1999",
            "30, 4, 2020"
    })
    public void testValidDates_CsvSource(int day, int month, int year) {
        // Act
        Date d = new Date(day, month, year);

        // Assert
        assertEquals(day, d.getDay());
        assertEquals(month, d.getMonth());
        assertEquals(year, d.getYear());
    }

    static Stream<Arguments> invalidDatesProvider() {
        return Stream.of(
                Arguments.of(31, 4, 2023),  // אפריל = 30 ימים
                Arguments.of(29, 2, 2023),  // שנה לא מעוברת
                Arguments.of(0, 5, 2022),   // יום 0 לא חוקי
                Arguments.of(15, 13, 2021), // חודש 13
                Arguments.of(32, 1, 2020)  // ינואר = מקסימום 31
        );
    }

    @ParameterizedTest
    @MethodSource("invalidDatesProvider")
    public void testInvalidDates_MethodSource(int day, int month, int year) {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Date(day, month, year));
    }


}

