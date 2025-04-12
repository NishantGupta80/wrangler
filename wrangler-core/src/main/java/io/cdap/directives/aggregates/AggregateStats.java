package io.cdap.directives.aggregate;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.DirectiveExecutionException;
import io.cdap.wrangler.api.DirectiveParseException;
import io.cdap.wrangler.api.ErrorRowException;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.annotations.Categories;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;
import io.cdap.wrangler.api.parser.Optional;

import java.util.List;

/**
 * A directive to aggregate byte sizes and time durations.
 */
@Plugin(type = Directive.TYPE)
@Name("aggregate-stats")
@Categories(categories = {"aggregate", "statistics"})
@Description("Aggregates byte sizes and time durations from source columns and stores the results in target columns.")
public class AggregateStats implements Directive {
  private String byteSizeColumn;
  private String timeDurationColumn;
  private String totalSizeColumn;
  private String totalTimeColumn;

  private long totalBytes;
  private long totalTime;

  @Override
  public UsageDefinition define() {
    UsageDefinition.Builder builder = UsageDefinition.builder("aggregate-stats");
    builder.define("byteSizeColumn", TokenType.COLUMN_NAME);
    builder.define("timeDurationColumn", TokenType.COLUMN_NAME);
    builder.define("totalSizeColumn", TokenType.COLUMN_NAME);
    builder.define("totalTimeColumn", TokenType.COLUMN_NAME);
    return builder.build();
  }

  @Override
  public void initialize(Arguments args) throws DirectiveParseException {
    this.byteSizeColumn = ((ColumnName) args.value("byteSizeColumn")).value();
    this.timeDurationColumn = ((ColumnName) args.value("timeDurationColumn")).value();
    this.totalSizeColumn = ((ColumnName) args.value("totalSizeColumn")).value();
    this.totalTimeColumn = ((ColumnName) args.value("totalTimeColumn")).value();

    this.totalBytes = 0;
    this.totalTime = 0;
  }

  @Override
  public void destroy() {
    // No-op
  }

  @Override
  public List<Row> execute(List<Row> rows, ExecutorContext context)
    throws DirectiveExecutionException, ErrorRowException {
    for (Row row : rows) {
      // Process byte size column
      int byteSizeIdx = row.find(byteSizeColumn);
      if (byteSizeIdx != -1) {
        Object byteSizeValue = row.getValue(byteSizeIdx);
        if (byteSizeValue instanceof Long) {
          totalBytes += (long) byteSizeValue;
        } else if (byteSizeValue instanceof String) {
          try {
            totalBytes += Long.parseLong((String) byteSizeValue);
          } catch (NumberFormatException e) {
            throw new ErrorRowException("aggregate-stats", "Invalid byte size value: " + byteSizeValue, e);
          }
        }
      }

      // Process time duration column
      int timeDurationIdx = row.find(timeDurationColumn);
      if (timeDurationIdx != -1) {
        Object timeDurationValue = row.getValue(timeDurationIdx);
        if (timeDurationValue instanceof Long) {
          totalTime += (long) timeDurationValue;
        } else if (timeDurationValue instanceof String) {
          try {
            totalTime += Long.parseLong((String) timeDurationValue);
          } catch (NumberFormatException e) {
            throw new ErrorRowException("aggregate-stats", "Invalid time duration value: " + timeDurationValue, e);
          }
        }
      }
    }

    // Add the aggregated results to the first row
    if (!rows.isEmpty()) {
      Row resultRow = rows.get(0);
      resultRow.addOrSet(totalSizeColumn, totalBytes);
      resultRow.addOrSet(totalTimeColumn, totalTime);
    }

    return rows;
  }
}