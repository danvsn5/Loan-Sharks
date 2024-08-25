package uoa.lavs.oop.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uoa.lavs.backend.oop.customer.Email;

public class EmailTest {
  Email email;

  @BeforeEach
  public void setUp() {
    String customerId = "123";
    email = new Email(customerId, "abc@gmail.com", true);
  }

  @Test
  public void testGetCustomerId() {
    assertEquals("123", email.getCustomerId());
  }

  @Test
  public void testSetCustomerId() {
    email.setCustomerId("456");
    assertEquals("456", email.getCustomerId());
  }

  @Test
  public void testGetEmailAddress() {
    assertEquals("abc@gmail.com", email.getEmailAddress());
  }

  @Test
  public void testSetEmailAddress() {
    email.setEmailAddress("cba@gmail.com");
    assertEquals("cba@gmail.com", email.getEmailAddress());
  }

  @Test
  public void testGetIsPrimary() {
    assertEquals(true, email.getIsPrimary());
  }

  @Test
  public void testSetIsPrimary() {
    email.setIsPrimary(false);
    assertEquals(false, email.getIsPrimary());
  }
}
