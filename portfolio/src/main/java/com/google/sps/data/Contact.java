// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

/** An item on a todo list. */
public final class Contact {
  private final long id;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final String subject;
  private final long timestamp;
  /**
   * set of constants that are POST request parameter keys and Entity property keys
   */
  public static final String CONTACT = "Contact";
  public static final String ID = "id";
  public static final String FIRST_NAME = "firstname";
  public static final String LAST_NAME = "lastname";
  public static final String EMAIL = "email";
  public static final String SUBJECT = "subject";
  public static final String TIMESTAMP = "timestamp";

  private Contact(
      long id, String firstName, String lastName, String email, String subject, long timestamp) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.subject = subject;
    this.timestamp = timestamp;
  }

  /**
   * @return a new contact containing the properties of the passed entity in the parameter
   */
  public static Contact fromEntity(Entity entity) {
    long id = entity.getKey().getId();
    String firstName = (String) entity.getProperty(FIRST_NAME);
    String lastName = (String) entity.getProperty(LAST_NAME);
    String email = (String) entity.getProperty(EMAIL);
    String subject = (String) entity.getProperty(SUBJECT);
    long timestamp = (long) entity.getProperty(TIMESTAMP);

    return new Contact(id, firstName, lastName, email, subject, timestamp);
  }
}
