package uoa.lavs.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerContactTest {
  CustomerContact contact;

  @BeforeEach
  public void setUp() {
    contact = new CustomerContact("email", "1234567890", "0987654321", "mobile sms", "email");
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
    assertEquals("1234567890", contact.getPhoneOne());
  }

  @Test
  public void testSetPhoneOne() {
    contact.setPhoneOne("0987654321");
    assertEquals("0987654321", contact.getPhoneOne());
  }

  @Test
  public void testGetPhoneTwo() {
    assertEquals("0987654321", contact.getPhoneTwo());
  }

  @Test
  public void testSetPhoneTwo() {
    contact.setPhoneTwo("1234567890");
    assertEquals("1234567890", contact.getPhoneTwo());
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
