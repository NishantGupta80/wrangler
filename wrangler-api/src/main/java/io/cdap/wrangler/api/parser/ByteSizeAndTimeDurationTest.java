package io.cdap.wrangler.api.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for ByteSize and TimeDuration classes.
 */
public class ByteSizeAndTimeDurationTest {

  @Test
  public void testByteSizeParsing() {
    ByteSize byteSize1 = new ByteSize("10KB");
    Assert.assertEquals(10240L, byteSize1.getBytes());

    ByteSize byteSize2 = new ByteSize("1.5MB");
    Assert.assertEquals(1572864L, byteSize2.getBytes());

    ByteSize byteSize3 = new ByteSize("2GB");
    Assert.assertEquals(2147483648L, byteSize3.getBytes());

    ByteSize byteSize4 = new ByteSize("512B");
    Assert.assertEquals(512L, byteSize4.getBytes());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidByteSize() {
    new ByteSize("invalid");
  }

  @Test
  public void testTimeDurationParsing() {
    TimeDuration timeDuration1 = new TimeDuration("150ms");
    Assert.assertEquals(150L, timeDuration1.getMilliseconds());

    TimeDuration timeDuration2 = new TimeDuration("2.5s");
    Assert.assertEquals(2500L, timeDuration2.getMilliseconds());

    TimeDuration timeDuration3 = new TimeDuration("1m");
    Assert.assertEquals(60000L, timeDuration3.getMilliseconds());

    TimeDuration timeDuration4 = new TimeDuration("1.5h");
    Assert.assertEquals(5400000L, timeDuration4.getMilliseconds());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTimeDuration() {
    new TimeDuration("invalid");
  }
}