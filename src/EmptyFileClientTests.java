import org.students.ConnectionHandler;
import org.students.ConsoleClient;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.BufferedWriter;
import java.io.IOException;

public class EmptyFileClientTests {
    ConnectionHandler connectionHandler;
    ConsoleClient client;

    @BeforeClass()
    @Parameters({"loginInfo", "filename"})
    public void getConnData(String loginInfo, String filename) {
        connectionHandler = new ConnectionHandler(loginInfo, filename);
        client = new ConsoleClient(connectionHandler);
    }

    @BeforeMethod()
    public void restoreFile() throws IOException {
        BufferedWriter writer = connectionHandler.getWriter();
        writer.write("");
        writer.close();
    }

    @DataProvider
    public Object[][] createId() {
        return new Object[][] {
                {"1"},
                {"2"},
                {"3"},
                {"4"},
                {"5"},
                {"6"}
        };
    }

    @DataProvider
    public Object[][] createStudentInfo() {
        return new Object[][]{
                {"name Rosa, surname Davidson, age 19", "id: 1, name: Rosa, surname: Davidson, age: 19"},
                {"name Mike, surname Pankraz, age 18", "id: 1, name: Mike, surname: Pankraz, age: 18"},
                {"name Margarita, surname Harvey, age 20", "id: 1, name: Margarita, surname: Harvey, age: 20"},
                {"name Nikolas, surname McAdams, age 20", "id: 1, name: Nikolas, surname: McAdams, age: 20"},
                {"name Boris, surname Fedorov, age 19", "id: 1, name: Boris, surname: Fedorov, age: 19"}
        };
    }

    @Test(priority = 1)
    public void testGetSortedListOfStudents() throws IOException {
        Assert.assertEquals(client.getSortedListOfStudents(), "");
    }

    @Test(dataProvider = "createId", priority = 2)
    public void testGetStudentInfo(String id) throws IOException {
        Assert.assertEquals(client.getStudentInfo(id), "No information found");
    }

    @Test(dataProvider = "createStudentInfo", priority = 3)
    public void testAddStudent(String info, String getInfoRes) throws IOException {
        client.addStudent(info);
        String s = client.getStudentInfo(String.valueOf(1));
        Assert.assertEquals(s, getInfoRes);
    }
}
