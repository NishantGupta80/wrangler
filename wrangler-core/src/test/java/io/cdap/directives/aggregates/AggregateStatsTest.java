package io.cdap.directives.aggregate;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.TestingRig;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the AggregateStats directive using TestingRig.
 */
public class AggregateStatsTest {

  @Test
  public void testAggregateStatsWithTestingRig() throws Exception {
    // Input data: Create a list of Row objects
    List<Row> rows = new ArrayList<>();
    rows.add(new Row("data_transfer_size", 1048576L).add("response_time", 1500L)); // 1MB, 1.5s
    rows.add(new Row("data_transfer_size", 2097152L).add("response_time", 2500L)); // 2MB, 2.5s
    rows.add(new Row("data_transfer_size", 524288L).add("response_time", 1000L)); // 0.5MB, 1s

    // Recipe: Aggregate size (output MB), total time (output seconds)
    String[] recipe = new String[] {
      "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec"
    };

    // Execute the recipe using TestingRig
    List<Row> results = TestingRig.execute(recipe, rows);

    // Expected output
    double expectedTotalSizeInMB = 3.5; // 1MB + 2MB + 0.5MB
    double expectedTotalTimeInSeconds = 5.0; // 1.5s + 2.5s + 1s

    // Assertions
    Assert.assertEquals(1, results.size()); // Ensure only one row is returned
    Row resultRow = results.get(0);
    Assert.assertEquals(expectedTotalSizeInMB, resultRow.getValue("total_size_mb"), 0.001); // Tolerance for double
    Assert.assertEquals(expectedTotalTimeInSeconds, resultRow.getValue("total_time_sec"), 0.001); // Tolerance for double
  }

  @Test
  public void testAggregateStatsWithDifferentUnits() throws Exception {
    // Input data: Create a list of Row objects
    List<Row> rows = new ArrayList<>();
    rows.add(new Row("data_transfer_size", 104857600L).add("response_time", 1500000L)); // 100MB, 1.5s
    rows.add(new Row("data_transfer_size", 209715200L).add("response_time", 2500000L)); // 200MB, 2.5s

    // Recipe: Aggregate size (output GB), total time (output minutes)
    String[] recipe = new String[] {
      "aggregate-stats :data_transfer_size :response_time total_size_gb total_time_min --outputSizeUnit GB --outputTimeUnit minutes"
    };

    // Execute the recipe using TestingRig
    List<Row> results = TestingRig.execute(recipe, rows);

    // Expected output
    double expectedTotalSizeInGB = 0.3; // 100MB + 200MB = 300MB = 0.3GB
    double expectedTotalTimeInMinutes = 0.0667; // 1.5s + 2.5s = 4s = 0.0667 minutes

    // Assertions
    Assert.assertEquals(1, results.size()); // Ensure only one row is returned
    Row resultRow = results.get(0);
    Assert.assertEquals(expectedTotalSizeInGB, resultRow.getValue("total_size_gb"), 0.001); // Tolerance for double
    Assert.assertEquals(expectedTotalTimeInMinutes, resultRow.getValue("total_time_min"), 0.001); // Tolerance for double
  }

  @Test
  public void testAggregateStatsWithEmptyRows() throws Exception {
    // Input data: Empty list of rows
    List<Row> rows = new ArrayList<>();

    // Recipe: Aggregate size (output MB), total time (output seconds)
    String[] recipe = new String[] {
      "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec"
    };

    // Execute the recipe using TestingRig
    List<Row> results = TestingRig.execute(recipe, rows);

    // Assertions
    Assert.assertEquals(0, results.size()); // Ensure no rows are returned
  }
}