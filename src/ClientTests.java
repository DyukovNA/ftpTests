import org.students.ConnectionHandler;
import org.students.ConsoleClient;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.*;

public class ClientTests {
    ConnectionHandler connectionHandler;
    ConsoleClient client;
    String testFile;

    @BeforeClass
    public void readSourceFile() throws IOException {
        FileReader source = new FileReader("src/resources/students.json");
        BufferedReader reader = new BufferedReader(source);
        StringBuilder text = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            text.append(line).append("\n");
        }
        testFile = text.toString();
        reader.close();
        source.close();
    }

    @BeforeClass()
    @Parameters({"loginInfo", "filename"})
    public void getConnData(String loginInfo, String filename) {
        connectionHandler = new ConnectionHandler(loginInfo, filename);
        client = new ConsoleClient(connectionHandler);
    }

    @BeforeMethod()
    public void restoreFile() throws IOException {
        BufferedWriter writer = connectionHandler.getWriter();
        writer.write(testFile);
        writer.close();
    }

    @DataProvider
    public Object[][] createId() {
        return new Object[][] {
                {"1", "id: 1, name: Rosa, surname: Davidson, age: 19"},
                {"2", "id: 2, name: Mike, surname: Pankraz, age: 18"},
                {"3", "id: 3, name: Margarita, surname: Harvey, age: 20"},
                {"4", "id: 4, name: Nikolas, surname: McAdams, age: 20"},
                {"5", "id: 5, name: Boris, surname: Fedorov, age: 19"},
                {"6", "No information found"}
        };
    }

    @DataProvider
    public Object[][] createStudentInfo() {
        return new Object[][]{
                {"name Rosa, surname Davidson, age 19", "id: 6, name: Rosa, surname: Davidson, age: 19"},
                {"name Mike, surname Pankraz, age 18", "id: 6, name: Mike, surname: Pankraz, age: 18"},
                {"name Margarita, surname Harvey, age 20", "id: 6, name: Margarita, surname: Harvey, age: 20"},
                {"name Nikolas, surname McAdams, age 20", "id: 6, name: Nikolas, surname: McAdams, age: 20"},
                {"name Boris, surname Fedorov, age 19", "id: 6, name: Boris, surname: Fedorov, age: 19"}
        };
    }

    @DataProvider
    public Object[][] createIdToDelete() {
        return new Object[][]{
                {"1", "id: 1, name: Mike, surname: Pankraz, age: 18"},
                {"2", "id: 2, name: Margarita, surname: Harvey, age: 20"},
                {"3", "id: 3, name: Nikolas, surname: McAdams, age: 20"},
                {"4", "id: 4, name: Boris, surname: Fedorov, age: 19"},
                {"5", "No information found"}
        };
    }

    @Test(priority = 1)
    public void testGetSortedListOfStudents() throws IOException {
        String result = "1. Boris,\n2. Margarita,\n3. Mike,\n4. Nikolas,\n5. Rosa";
        Assert.assertEquals(client.getSortedListOfStudents(), result);
    }

    @Test(dataProvider = "createId", priority = 2)
    public void testGetStudentInfo(String id, String result) throws IOException {
        Assert.assertEquals(client.getStudentInfo(id), result);
    }

    @Test(dataProvider = "createStudentInfo", priority = 3)
    public void testAddStudent(String info, String getInfoRes) throws IOException {
        client.addStudent(info);
        String s = client.getStudentInfo(String.valueOf(6));
        Assert.assertEquals(s, getInfoRes);
    }

    @Test(dataProvider = "createIdToDelete", priority = 4)
    public void testDeleteStudent(String id, String result) throws IOException {
        String before = client.getStudentInfo(id);
        client.deleteStudent(id);
        String after = client.getStudentInfo(id);
        Assert.assertNotEquals(before, after);
        Assert.assertEquals(after, result);
    }
}
