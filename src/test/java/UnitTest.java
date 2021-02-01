import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.ArrayList;
import java.util.concurrent.*;

class UnitTests {
    @BeforeAll
    public static void setUp() {
        
    }
    
    @AfterAll
    public static void tearDown() {
        
    }
    
    
    @Test
    public void test_neural_network_should_pass() {
        // Re-direct System.out to buffer.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        
        String[] args = new String[] {};
        Titanic.main(args);
  
        // Reconnect System.out to stdout.
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        
        String content = buffer.toString();
        String[] lines = content.split("\n");
        
        // Assert training epochs run and the training error is decreasing.
        assert(content.indexOf("Epoch:") > 0);
        assert(content.indexOf("Training Error:") > 0);
        assert(Double.valueOf(lines[2].split(" ")[8]) >= Double.valueOf(lines[3].split(" ")[8]));
        assert(Double.valueOf(lines[3].split(" ")[8]) >= Double.valueOf(lines[4].split(" ")[8]));
        
        // Assert program exits normally and captured output indicates completion.
        assert(content.indexOf("Hit Count:") > 0);
        assert(content.indexOf("Miss Count:") > 0);
        assert(content.indexOf("Total:") > 0);
        assert(content.indexOf("Percentage:") > 0);
        buffer.reset();  // Discard captured output.
    }
}
