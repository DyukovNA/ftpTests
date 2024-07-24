import org.students.TextEditor;
import org.testng.Assert;
import org.testng.annotations.*;

public class TextEditorTests {
    private TextEditor textEditor = new TextEditor();

    @DataProvider
    public Object[][] createEndOfEntry() {
        return new Object[][]{
                {" },"},
                {"      },"},
                {"\t},"},
                {"\t\t},"}
        };
    }

    @DataProvider
    public Object[][] createFields() {
        return new Object[][]{
                {"\t\"name\": \"Alex\""},
                {"\t\t\"name\": \"Alex\","},
                {" \"name\": \"G\""},
                {"\t\"age\": 3"},
                {"\t\"age\": 3,"},
                {"\t\t\"age\": 80"}
        };
    }

    @DataProvider
    public Object[][] createBeginnings() {
        return new Object[][]{
                {" {"},
                {"      {"},
                {"\t{"},
                {"\t\t{"}
        };
    }

    @DataProvider
    public Object[][] createEndOfLastEntry() {
        return new Object[][]{
                {" }"},
                {"      }"},
                {"\t}"},
                {"\t\t}"}
        };
    }

    @DataProvider
    public Object[][] createId() {
        return new Object[][]{
                {" \"id\": 1", 1},
                {"\t\"id\": 6,", 6},
                {"\t\"id\": 1024,", 1024},
                {"\t\"id\": 128", 128}
        };
    }

    @DataProvider
    public Object[][] createValidInput() {
        return new Object[][]{
                {"name Richard"},
                {"name Richard, surname Johnson"},
                {"name Richard, surname Johnson, age 20"},
                {"age 20"},
                {"name Richard, surname Johnson, avgGrade 4.5"}
        };
    }

    @DataProvider
    public Object[][] createToClean() {
        return new Object[][]{
                {" \"id\": 1", "id:1"},
                {"\t\"id\": 1024,", "id:1024"},
                {"\t\t\"age\": 80", "age:80"},
                {"\t\t\"name\": \"Alex\",", "name:Alex"},
                {"\t\t},", ""},
                {"", ""}
        };
    }

    @DataProvider
    public Object[][] createNewLine() {
        return new Object[][]{
                {" \"id\": 1"},
                {"\t\"id\": 1024,"},
                {"\t\t\"age\": 80"},
                {"\t\t\"name\": \"Alex\","},
                {"\t\t},"},
                {""}
        };
    }

    @DataProvider
    public static Object[][] createInfoPairs() {
        return new Object[][]{
                {1, "Name Richard", "\t\t\t\"name\": \"Richard\","},
                {0, "name Richard", "\t\t\t\"name\": \"Richard\""},
                {9, "Age 19", "\t\t\t\"age\": 19,"},
                {0, "Age 19", "\t\t\t\"age\": 19"}
        };
    }

    @DataProvider
    public static Object[][] createStudentInfo() {
        return new Object[][]{
                {1, "name Richard"},
                {5, "name Eric, surname Jackson, age 20, grade 4"},
        };
    }

    @Test(dataProvider = "createEndOfEntry")
    public void testisEndOfEntry(String line) {
        Assert.assertTrue(textEditor.isEndOfEntry(line));
    }

    @Test(dataProvider = "createFields")
    public void testIsField(String line) {
        Assert.assertTrue(textEditor.isField(line));
    }

    @Test(dataProvider = "createBeginnings")
    public void testIsBeginning(String line) {
        Assert.assertTrue(textEditor.isBeginning(line));
    }

    @Test(dataProvider = "createEndOfLastEntry")
    public void testIsEndOfLastEntry(String line) {
        Assert.assertTrue(textEditor.isEndOfLastEntry(line));
    }

    @Test(dataProvider = "createId")
    public void testIsId(String line, int ignored_) {
        Assert.assertTrue(textEditor.isId(line));
    }

    @Test(dataProvider = "createValidInput")
    public void testIsValidInput(String line) {
        Assert.assertTrue(textEditor.isValidInput(line));
    }

    @Test(dataProvider = "createToClean")
    public void testClean(String line, String result) {
        Assert.assertEquals(textEditor.clean(line), result);
    }

    @Test(dataProvider = "createId")
    public void testParseId(String line, int result) {
        Assert.assertEquals(textEditor.parseId(line), result);
    }

    @Test(dataProvider = "createNewLine")
    public void testWriteNewLine(String line) {
        StringBuilder sb = new StringBuilder();
        textEditor.writeNewLine(sb, line);
        Assert.assertTrue(sb.lastIndexOf(line + "\n") != -1);
    }

    @Test(dataProvider = "createInfoPairs")
    public void testWriteNewField(int i, String infoPair, String toFind) {
        StringBuilder sb = new StringBuilder();
        textEditor.writeNewField(i, infoPair, sb);
        String s = sb.toString();
        boolean result = s.contains(toFind);
        result = result && s.matches(TextEditor.fieldRegex);
        Assert.assertTrue(result);
    }

    @Test(dataProvider = "createStudentInfo")
    public void testWriteNewStudent(int id, String info) {
        StringBuilder sb = new StringBuilder();
        textEditor.writeNewStudent(id, sb, info);
        String s = sb.toString();
        String[] toFind = info.replace(",", "").split(" ");
        boolean result = s.contains(("\"id\": " + id));
        for (String value : toFind) {
            result = result && s.contains(value);
        }
        result = s.matches("\\s+\\{(\\s+\"[\\w\\d]+\":\\s+\"?[\\w\\d\\s]+\"?,?\n)+\\s+}\n\\s+]\n}");
        Assert.assertTrue(result);
    }
}
