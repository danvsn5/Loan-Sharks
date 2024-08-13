package uoa.lavs.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PhoneTest {
  private Phone phone;

  @BeforeEach
  public void setUp() {
    phone = new Phone("mobile", "1234567890");
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
  public void testGetPhoneNumber() {
    assertEquals("1234567890", phone.getPhoneNumber());
  }

  @Test
  public void testSetPhoneNumber() {
    phone.setPhoneNumber("0987654321");
    assertEquals("0987654321", phone.getPhoneNumber());
  }
}
