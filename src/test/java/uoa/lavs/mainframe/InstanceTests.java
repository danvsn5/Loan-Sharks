package uoa.lavs.mainframe;

import org.junit.jupiter.api.Test;

import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Instance;

import static org.junit.jupiter.api.Assertions.*;

class InstanceTests {
    @Test
    public void getInstanceReturnsConnection()
    {
        // Act
        Connection conn = Instance.getConnection();

        // Assert
        assertNotNull(conn);
    }

}