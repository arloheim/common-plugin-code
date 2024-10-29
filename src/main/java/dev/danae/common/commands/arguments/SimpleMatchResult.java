package dev.danae.common.commands.arguments;

import java.util.regex.MatchResult;


final class SimpleMatchResult implements MatchResult 
{
  // The text of the match result
  private final String text;


  // Constructor
  public SimpleMatchResult(String text)
  {
    this.text = text;
  }


  // Return the start of the whole match
  @Override
  public int start()
  {
    return 0;
  }

  // Return the start of the specified match group
  @Override
  public int start(int group)
  {
    if (group != 0)
      throw new IndexOutOfBoundsException("No group " + group);
    return this.start();
  }

  // Return the end of the whole match
  @Override
  public int end() 
  {
    return this.text.length() - 1;
  }

  // Return the end of the specified match group
  @Override
  public int end(int group)
  {
    if (group != 0)
      throw new IndexOutOfBoundsException("No group " + group);
    return this.end();
  }

  // Return the group count
  @Override
  public int groupCount()
  {
    return 0;
  }

  // Return the whole match
  @Override
  public String group()
  {
    return this.text;
  }

  // Return the specified match group
  @Override
  public String group(int group)
  {
    if (group != 0)
      throw new IndexOutOfBoundsException("No group " + group);
    return this.group();
  }
}