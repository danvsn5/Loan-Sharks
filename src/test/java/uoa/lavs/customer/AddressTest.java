package uoa.lavs.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddressTest {
  Address address;

  @BeforeEach
  public void setUp() {
    String customerId = "123";
    address =
        new Address(customerId, "Commercial", "123 Guy St", "Apt 1", "Muntown", "12345", "Tingcity", "TMG", true, false);
  }

  @Test
  public void testGetCustomerId() {
    assertEquals("123", address.getCustomerId());
  }

  @Test
  public void testSetCustomerId() {
    address.setCustomerId("456");
    assertEquals("456", address.getCustomerId());
  }

  @Test
  public void testGetAddressType() {
    assertEquals("Commercial", address.getAddressType());
  }

  @Test
  public void testSetAddressType() {
    address.setAddressType("Residential");
    assertEquals("Residential", address.getAddressType());
  }

  @Test
  public void testGetAddressLineOne() {
    assertEquals("123 Guy St", address.getAddressLineOne());
  }

  @Test
  public void testSetAddressLineOne() {
    address.setAddressLineOne("456 Guy Rd");
    assertEquals("456 Guy Rd", address.getAddressLineOne());
  }

  @Test
  public void testGetAddressLineTwo() {
    assertEquals("Apt 1", address.getAddressLineTwo());
  }

  @Test
  public void testSetAddressLineTwo() {
    address.setAddressLineTwo("Apt 2");
    assertEquals("Apt 2", address.getAddressLineTwo());
  }

  @Test
  public void testGetSuburb() {
    assertEquals("Muntown", address.getSuburb());
  }

  @Test
  public void testSetSuburb() {
    address.setSuburb("Muncity");
    assertEquals("Muncity", address.getSuburb());
  }

  @Test
  public void testGetPostCode() {
    assertEquals("12345", address.getPostCode());
  }

  @Test
  public void testSetPostCode() {
    address.setPostCode("54321");
    assertEquals("54321", address.getPostCode());
  }

  @Test
  public void testGetCity() {
    assertEquals("Tingcity", address.getCity());
  }

  @Test
  public void testSetCity() {
    address.setCity("Tongcity");
    assertEquals("Tongcity", address.getCity());
  }

  @Test
  public void testGetCountry() {
    assertEquals("TMG", address.getCountry());
  }

  @Test
  public void testSetCountry() {
    address.setCountry("GMT");
    assertEquals("GMT", address.getCountry());
  }

  @Test void testGetIsPrimary() {
    assertEquals(true, address.getIsPrimary());
  }

  @Test void testSetIsPrimary() {
    address.setIsPrimary(false);
    assertEquals(false, address.getIsPrimary());
  }

  @Test void testGetIsMailing() {
    assertEquals(false, address.getIsMailing());
  }

  @Test void testSetIsMailing() {
    address.setIsMailing(true);
    assertEquals(true, address.getIsMailing());
  }
}
