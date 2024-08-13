package uoa.lavs.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerContactTest {
  private CustomerContact contact;
  private Phone phoneOne;
  private Phone phoneTwo;

  @BeforeEach
  public void setUp() {
    phoneOne = new Phone("mobile", "1234567890");
    phoneTwo = new Phone("home", "0987654321");
    contact = new CustomerContact("email", phoneOne, phoneTwo, "mobile sms", "email");
  }

  @Test
  public void testGetCustomerEmail() {
    assertEquals("email", contact.getCustomerEmail());
  }

  @Test
  public void testSetCustomerEmail() {
    contact.setCustomerEmail("newemail");
    assertEquals("newemail", contact.getCustomerEmail());
  }

  @Test
  public void testGetPhoneOne() {
    assertEquals(phoneOne, contact.getPhoneOne());
  }

  @Test
  public void testSetPhoneOne() {
    contact.setPhoneOne(phoneTwo);
    assertEquals(phoneTwo, contact.getPhoneOne());
  }

  @Test
  public void testGetPhoneTwo() {
    assertEquals(phoneTwo, contact.getPhoneTwo());
  }

  @Test
  public void testSetPhoneTwo() {
    contact.setPhoneTwo(phoneOne);
    assertEquals(phoneOne, contact.getPhoneTwo());
  }

  @Test
  public void testGetPreferredContact() {
    assertEquals("mobile sms", contact.getPreferredContact());
  }

  @Test
  public void testSetPreferredContact() {
    contact.setPreferredContact("email");
    assertEquals("email", contact.getPreferredContact());
  }

  @Test
  public void testGetAlternateContact() {
    assertEquals("email", contact.getAlternateContact());
  }

  @Test
  public void testSetAlternateContact() {
    contact.setAlternateContact("mobile sms");
    assertEquals("mobile sms", contact.getAlternateContact());
  }
}
