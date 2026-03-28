package XDocTests;

import org.example.personalpractice.XDoc.Sqr;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqrTest {

    // Method that returns a stream of Arguments for parameterized tests
    public static Stream<Arguments> getSqrTestCases() {

        // Create a list to store all test cases
        List<Arguments> list = new ArrayList<>();

        try {
            // Create a File object pointing to the XML file
            // in resources directory
            var file = new File(
                    Objects.requireNonNull(SqrTest.class
                                    .getClassLoader()
                                    .getResource("Sharbel"))
                            .getFile()
            );

            // Create a Document object by parsing the XML file
            Document doc = DocumentBuilderFactory
                    .newInstance() // Create a new factory instance
                    .newDocumentBuilder() // Create a document builder
                    .parse(file); // Parse the file into a Document

            // Get all <TestCase> elements from the XML
            NodeList nodes = doc.getElementsByTagName("TestCase");

            // Loop over all TestCase nodes
            for (var i = 0; i < nodes.getLength(); i++) {

                // Get the current node
                Node node = nodes.item(i);

                // Check if the node is an Element (skip text/comments)
                if (node instanceof Element e) {

                    // Read values from XML and convert them to double / boolean
                    var len = parseDouble(e.getElementsByTagName("Len").item(0).getTextContent());
                    var width = parseDouble(e.getElementsByTagName("Width").item(0).getTextContent());

                    var expLen = parseDouble(e.getElementsByTagName("ExpLen").item(0).getTextContent());
                    var expWidth = parseDouble(e.getElementsByTagName("ExpWidth").item(0).getTextContent());
                    var expArea = parseDouble(e.getElementsByTagName("ExpArea").item(0).getTextContent());

                    var input = parseDouble(e.getElementsByTagName("Inp").item(0).getTextContent());
                    var expRes = parseBoolean(e.getElementsByTagName("ExpRes").item(0).getTextContent());

                    // Add all values as one test case into the list
                    list.add(Arguments.of(len, width, expLen, expWidth, expArea, input, expRes));
                }
            }

        }
        // Print stack trace if any error occurs while reading XML
        catch (Exception e) { e.printStackTrace(); }

        return list.stream(); // Convert list into a Stream and return it
    }

    @DisplayName("Constructor + Area Tests From XML")
    @ParameterizedTest(name = "Test case #{index}")
    @MethodSource("getSqrTestCases")
    public void volumeTest(double len, double width, double expLen, double expWidth,
                    double expArea, double input, boolean expRes) {

        // Act
        var b = new Sqr(len, width);
        var result = b.isVolumeGreaterThanInput(input);

        // Assert for constructor()
        assertEquals(expLen, b.getLength());
        assertEquals(expWidth, b.getWidth());

        // Assert for getArea()
        assertEquals(expArea, b.getArea());

        // Assert for method()
        assertEquals(expRes, result);
    }
}