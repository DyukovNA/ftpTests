import org.testng.Assert;
import org.testng.annotations.*;
import org.students.ConnectionHandler;
import java.io.IOException;

public class ConnectionTests {
    @Test ()
    @Parameters({"loginInfo", "filename"})
    void testSetConnection(String userInput, String filename) {
        ConnectionHandler connectionHandler = new ConnectionHandler(userInput, filename);
        Assert.assertNotEquals(connectionHandler.setConnection().getContentLength(), 0);
    }

    @Test ()
    @Parameters({"loginInfo", "filename"})
    void testWriter(String userInput, String filename) throws IOException {
        ConnectionHandler connectionHandler = new ConnectionHandler(userInput, filename);
        Assert.assertNotNull(connectionHandler.getWriter());
    }

    @Test ()
    @Parameters({"loginInfo", "filename"})
    void testReader(String userInput, String filename) throws IOException {
        ConnectionHandler connectionHandler = new ConnectionHandler(userInput, filename);
        Assert.assertNotNull(connectionHandler.getReader());
    }
}
