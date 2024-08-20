package uoa.lavs.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PhoneTest {
  private Phone phone;

  @BeforeEach
  public void setUp() {
    phone = new Phone("1", "mobile", "027", "1234567890", true, true);
  }

  @Test
  public void testGetCustomerId() {
    assertEquals("1", phone.getCustomerId());
  }

  @Test
  public void testSetCustomerId() {
    phone.setCustomerId("2");
    assertEquals("2", phone.getCustomerId());
  }

  @Test
  public void testGetType() {
    assertEquals("mobile", phone.getType());
  }

  @Test
  public void testSetType() {
    phone.setType("home");
    assertEquals("home", phone.getType());
  }

  @Test
  public void testGetPrefix() {
    assertEquals("027", phone.getPrefix());
  }

  @Test
  public void testSetPrefix() {
    phone.setPrefix("021");
    assertEquals("021", phone.getPrefix());
  }

  @Test
  public void testGetPhoneNumber() {
    assertEquals("1234567890", phone.getPhoneNumber());
  }

  @Test
  public void testSetPhoneNumber() {
    phone.setPhoneNumber("0987654321");
    assertEquals("0987654321", phone.getPhoneNumber());
  }

  @Test
  public void testIsPrimary() {
    assertEquals(true, phone.getIsPrimary());
  }

  @Test
  public void testSetPrimary() {
    phone.setIsPrimary(false);
    assertEquals(false, phone.getIsPrimary());
  }

  @Test
  public void testGetCanSendText() {
    assertEquals(true, phone.getCanSendText());
  }

  @Test
  public void testSetCanSendText() {
    phone.setCanSendText(false);
    assertEquals(false, phone.getCanSendText());
  }
}
