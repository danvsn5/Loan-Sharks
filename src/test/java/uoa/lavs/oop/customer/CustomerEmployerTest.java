package uoa.lavs.oop.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uoa.lavs.backend.oop.customer.CustomerEmployer;

public class CustomerEmployerTest {
  private CustomerEmployer employer;

  @BeforeEach
  public void setUp() {
    employer =
        new CustomerEmployer(
            "000001",
            "Countdown",
            "123 Stonesuckle Ct",
            "",
            "Sunnynook",
            "Auckland",
            "12345",
            "Zimbabwe",
            null,
            null,
            null,
            false);
  }

  @Test
  public void testGetCustomerId() {
    assertEquals("000001", employer.getCustomerId());
  }

  @Test
  public void testSetCustomerId() {
    employer.setCustomerId("000002");
    assertEquals("000002", employer.getCustomerId());
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
  public void testGetLineOne() {
    assertEquals("123 Stonesuckle Ct", employer.getLineOne());
  }

  @Test
  public void testSetLineOne() {
    employer.setLineOne("456 Appletree St");
    assertEquals("456 Appletree St", employer.getLineOne());
  }

  @Test
  public void testGetLineTwo() {
    assertEquals("", employer.getLineTwo());
  }

  @Test
  public void testSetLineTwo() {
    employer.setLineTwo("Apt 2");
    assertEquals("Apt 2", employer.getLineTwo());
  }

  @Test
  public void testGetSuburb() {
    assertEquals("Sunnynook", employer.getSuburb());
  }

  @Test
  public void testSetSuburb() {
    employer.setSuburb("Glenfield");
    assertEquals("Glenfield", employer.getSuburb());
  }

  @Test
  public void testGetCity() {
    assertEquals("Auckland", employer.getCity());
  }

  @Test
  public void testSetCity() {
    employer.setCity("Wellington");
    assertEquals("Wellington", employer.getCity());
  }

  @Test
  public void testGetPostCode() {
    assertEquals("12345", employer.getPostCode());
  }

  @Test
  public void testSetPostCode() {
    employer.setPostCode("54321");
    assertEquals("54321", employer.getPostCode());
  }

  @Test
  public void testGetCountry() {
    assertEquals("Zimbabwe", employer.getCountry());
  }

  @Test
  public void testSetCountry() {
    employer.setCountry("New Zealand");
    assertEquals("New Zealand", employer.getCountry());
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
