//
//    // Helper method to load the content of a file as a string
//    private String loadFileAsString(String filePath) throws IOException {
//        return new String(Files.readAllBytes(Paths.get(filePath)));
//    }
//}

public class HorarioLoaderTest {

    private String testCsvFilePath;
    private String testHtmlFilePath;

    @Before
    public void setUp() {
        testCsvFilePath = "test-horario.csv";
        testHtmlFilePath = "test-output.html";
    }

    @Test
    public void testLoadHorarioFromCSVAndCompareWithCSVFile() throws IOException, CsvException {
        // Create a sample CSV file for testing
        createSampleCSVFile(testCsvFilePath);

        // Load the HTML content from the test CSV file
        String expectedHtml = "<html><body><table border='1'><tr><td>Test</td></tr></table></body></html>";

        // Run the loadHorarioFromCSV_OK method
        String actualHtml = HorarioLoader.loadHorarioFromCSV_OK(testCsvFilePath);

        // Compare the HTML content with the expected HTML
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testSaveHTMLToFileAndCompareWithHTMLFile() throws IOException {
        // Generate a sample HTML content for testing
        String expectedHtml = "<html><body><p>Test content</p></body></html>";

        // Save the HTML content to the test HTML file
        HorarioLoader.saveHTMLToFile(testHtmlFilePath, expectedHtml);

        // Compare the HTML content with the content from the HTML file
        String actualHtml = loadFileAsString(testHtmlFilePath);
        assertEquals(expectedHtml, actualHtml);
    }

    // Helper method to create a sample CSV file for testing
    private void createSampleCSVFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Test");
        }
    }

    // Helper method to load the content of a file as a string
    private String loadFileAsString(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }
}
