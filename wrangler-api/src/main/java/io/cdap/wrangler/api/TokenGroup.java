/*
 *  Copyright © 2017-2019 Cask Data, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

 package io.cdap.wrangler.api;

 import io.cdap.wrangler.api.parser.ByteSize;
 import io.cdap.wrangler.api.parser.TimeDuration;
 import io.cdap.wrangler.api.parser.Token;
 
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;
 
 /**
  * Represents a group of tokens parsed from a directive.
  */
 public final class TokenGroup {
   private final SourceInfo info;
   private final List<Token> tokens;
 
   public TokenGroup() {
     this.info = null;
     this.tokens = new ArrayList<>();
   }
 
   public TokenGroup(SourceInfo info) {
     this.info = info;
     this.tokens = new ArrayList<>();
   }
 
   /**
    * Adds a token to the group.
    *
    * @param token The token to add.
    */
   public void add(Token token) {
     tokens.add(token);
   }
 
   /**
    * Returns the number of tokens in the group.
    *
    * @return The size of the token group.
    */
   public int size() {
     return tokens.size();
   }
 
   /**
    * Retrieves a token at a specific index.
    *
    * @param i The index of the token.
    * @return The token at the specified index.
    */
   public Token get(int i) {
     return tokens.get(i);
   }
 
   /**
    * Returns an iterator over the tokens in the group.
    *
    * @return An iterator for the tokens.
    */
   public Iterator<Token> iterator() {
     return tokens.iterator();
   }
 
   /**
    * Retrieves the source information for the token group.
    *
    * @return The source information.
    */
   public SourceInfo getSourceInfo() {
     return info;
   }
 
   /**
    * Retrieves all tokens of type ByteSize.
    *
    * @return A list of ByteSize tokens.
    */
   public List<ByteSize> getByteSizeTokens() {
     List<ByteSize> byteSizeTokens = new ArrayList<>();
     for (Token token : tokens) {
       if (token instanceof ByteSize) {
         byteSizeTokens.add((ByteSize) token);
       }
     }
     return byteSizeTokens;
   }
 
   /**
    * Retrieves all tokens of type TimeDuration.
    *
    * @return A list of TimeDuration tokens.
    */
   public List<TimeDuration> getTimeDurationTokens() {
     List<TimeDuration> timeDurationTokens = new ArrayList<>();
     for (Token token : tokens) {
       if (token instanceof TimeDuration) {
         timeDurationTokens.add((TimeDuration) token);
       }
     }
     return timeDurationTokens;
   }
 }