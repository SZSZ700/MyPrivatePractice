import org.example.Card;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    private Card card;

    @BeforeEach
    public void init() {
        // Arrange
        card = new Card('R', 5);
    }

    @Test
    public void testGetters_AAA() {
        // Act
        char color = card.getColor();
        int digit = card.getDigit();

        // Assert
        assertEquals('R', color);
        assertEquals(5, digit);
    }

    @Test
    public void testSetters_AAA() {
        // Act
        card.setColor('G');
        card.setDigit(9);

        // Assert
        assertEquals('G', card.getColor());
        assertEquals(9, card.getDigit());
    }

    @Test
    public void testCompareTo_SameCard_AAA() {
        // Arrange
        Card other = new Card('R', 5);

        // Act
        int result = card.compareTo(other);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testCompareTo_HigherDigit_AAA() {
        // Arrange
        Card weaker = new Card('R', 3);

        // Act
        int result = card.compareTo(weaker);

        // Assert
        assertEquals(-1, result); // card (5) > weaker (3)
    }

    @Test
    public void testCompareTo_LowerDigit_AAA() {
        // Arrange
        Card stronger = new Card('R', 8);

        // Act
        int result = card.compareTo(stronger);

        // Assert
        assertEquals(1, result); // card (5) < stronger (8)
    }

    @Test
    public void testCompareTo_SameDigit_DifferentColor_AAA() {
        // Arrange
        Card gCard = new Card('G', 5); // צבע 'G' חזק מ־'R'

        // Act
        int result = card.compareTo(gCard);

        // Assert
        assertEquals(1, result); // card R < G לפי דירוג צבעים
    }

    @Test
    public void testToString_AAA() {
        // Act
        String output = card.toString();

        // Assert
        assertTrue(output.contains("color=R"));
        assertTrue(output.contains("digit=5"));
    }
}

