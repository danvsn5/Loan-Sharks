package uoa.lavs.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerEmployerTest {
  private CustomerEmployer employer;
  private Address employerAddress;

  @BeforeEach
  public void setUp() {
    employerAddress =
        new Address(
            "Commercial", "123 Stonesuckle Ct", "", "Sunnynook", "12345", "Auckland", "Zimbabwe");
    employer = new CustomerEmployer("Countdown", employerAddress, null, null, null, false);
  }

  @Test
  public void testGetEmployerName() {
    assertEquals("Countdown", employer.getEmployerName());
  }

  @Test
  public void testSetEmployerName() {
    employer.setEmployerName("Pak'nSave");
    assertEquals("Pak'nSave", employer.getEmployerName());
  }

  @Test
  public void testGetEmployerAddress() {
    assertEquals(employerAddress, employer.getEmployerAddress());
  }

  @Test
  public void testSetEmployerAddress() {
    Address newAddress =
        new Address(
            "Commercial", "456 Stonesuckle Ct", "34", "Sunnynook", "12345", "Auckland", "Zimbabwe");
    employer.setEmployerAddress(newAddress);
    assertEquals(newAddress, employer.getEmployerAddress());
  }

  @Test
  public void testGetEmployerEmail() {
    assertEquals(null, employer.getEmployerEmail());
  }

  @Test
  public void testSetEmployerEmail() {
    employer.setEmployerEmail("abc@gmail.com");
    assertEquals("abc@gmail.com", employer.getEmployerEmail());
  }

  @Test
  public void testGetEmployerWebsite() {
    assertEquals(null, employer.getEmployerWebsite());
  }

  @Test
  public void testSetEmployerWebsite() {
    employer.setEmployerWebsite("www.countdown.co.nz");
    assertEquals("www.countdown.co.nz", employer.getEmployerWebsite());
  }

  @Test
  public void testGetEmployerPhone() {
    assertEquals(null, employer.getEmployerPhone());
  }

  @Test
  public void testSetEmployerPhone() {
    employer.setEmployerPhone("123456789");
    assertEquals("123456789", employer.getEmployerPhone());
  }

  @Test
  public void testGetOwnerOfCompany() {
    assertEquals(false, employer.getOwnerOfCompany());
  }

  @Test
  public void testSetOwnerOfCompany() {
    employer.setOwnerOfCompany(true);
    assertEquals(true, employer.getOwnerOfCompany());
  }
}
