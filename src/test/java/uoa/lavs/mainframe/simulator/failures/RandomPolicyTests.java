package uoa.lavs.mainframe.simulator.failures;

import org.junit.jupiter.api.Test;

import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Response;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.FindCustomer;
import uoa.lavs.legacy.mainframe.simulator.InMemoryConnection;
import uoa.lavs.legacy.mainframe.simulator.IntermittentConnection;
import uoa.lavs.legacy.mainframe.simulator.failures.RandomPolicy;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomPolicyTests {

    @Test
    public void nonCumulativeTest() {
        // arrange
        Connection conn = new IntermittentConnection(
                new InMemoryConnection(
                        new Response(
                                new Status(1),
                                new HashMap<>()
                        )
                ),
                new RandomPolicy(50, false)
        );
        FindCustomer message = new FindCustomer();
        int failureCount = 0;
        int successCount = 0;

        // act
        for (int i = 0; i < 10; i++) {
            Status status = message.send(conn);
            if (!status.getWasSuccessful()) {
                failureCount++;
            } else {
                successCount++;
            }
        }

        // assert
        int actualFailureCount = failureCount;
        int actualSuccessCount = successCount;
        assertAll(
                () -> assertTrue(actualFailureCount > 0),
                () -> assertTrue(actualSuccessCount > 0)
        );
    }

    @Test
    public void cumulativeTest() {
        // arrange
        Connection conn = new IntermittentConnection(
                new InMemoryConnection(
                        new Response(
                                new Status(1),
                                new HashMap<>()
                        )
                ),
                new RandomPolicy(100, true)
        );
        FindCustomer message = new FindCustomer();
        int failureCount = 0;
        int successCount = 0;

        // act
        for (int i = 0; i < 10; i++) {
            Status status = message.send(conn);
            if (!status.getWasSuccessful()) {
                failureCount++;
            } else {
                successCount++;
            }
        }

        // assert
        int actualFailureCount = failureCount;
        int actualSuccessCount = successCount;
        assertAll(
                () -> assertTrue(actualFailureCount > 0),
                () -> assertTrue(actualSuccessCount > 0)
        );
    }
}