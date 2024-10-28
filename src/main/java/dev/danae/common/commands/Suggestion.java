package dev.danae.common.commands;

import java.util.stream.Stream;
import java.util.function.Consumer;


public class Suggestion implements Comparable<Suggestion>
{
  // The string of the search result
  private final String input;

  // The start index of the search result
  private final int startIndex;

  // The end index of the search result
  private final int endIndex;


  // Constructor
  public Suggestion(String input, int startIndex, int endIndex)
  {
    this.input = input;
    this.startIndex = startIndex;
    this.endIndex = endIndex;
  }

  // Return the input of the search result
  public String getInput()
  {
    return this.input;
  }

  // Return the start index of the search result
  public int getStartIndex()
  {
    return this.startIndex;
  }

  // Return the end index of the search result
  public int getEndIndex()
  {
    return this.endIndex;
  }


  // Compare the search result to another search result
  @Override
  public int compareTo(Suggestion other) 
  {
    var startIndexComparison = Integer.compare(this.startIndex, other.startIndex);
    if (startIndexComparison != 0)
      return startIndexComparison;
    else
      return Integer.compare(this.endIndex, other.endIndex);
  }


  // Find all suggestions in the specified stream that match the specified query
  public static Stream<String> find(String query, Stream<String> suggestions)
  {
    if (query == null || query.isEmpty())
      return suggestions;

    return suggestions
      .mapMulti((String suggestion, Consumer<Suggestion> consumer) -> find(query, suggestion, consumer))
      .sorted()
      .map(suggestion -> suggestion.getInput());
  }

  // Find a query in the specified suggestion and provide it to the consumer if found
  private static void find(String query, String suggestion, Consumer<Suggestion> consumer)
  {
    var startIndex = suggestion.indexOf(query);
    if (startIndex > -1)
      consumer.accept(new Suggestion(suggestion, startIndex, startIndex + query.length() - 1));
  }
}
