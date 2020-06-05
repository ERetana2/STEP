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
package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Contact;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final Gson GSON = new Gson();
  private static final String NUM_CONTACTS_TO_DISPLAY = "numContactsToDisplay";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(Contact.CONTACT).addSort(Contact.TIMESTAMP, SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    int numContactsToDisplay = Integer.parseInt(request.getParameter(NUM_CONTACTS_TO_DISPLAY));
    List<Contact> contacts = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      // Display x amounts of contacts
      if (numContactsToDisplay == contacts.size()) {
        break;
      }
      // create a new contact from current entity
      contacts.add(Contact.fromEntity(entity));
    }
    response.setContentType("application/json");
    response.getWriter().println(GSON.toJson(contacts));
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // request form information from servlet
    String firstName = getParameter(request, Contact.FIRST_NAME, "");
    String lastName = getParameter(request, Contact.LAST_NAME, "");
    String email = getParameter(request, Contact.EMAIL, "");
    String subject = getParameter(request, Contact.SUBJECT, "");
    long timestamp = System.currentTimeMillis();
    // Create new entities with new contact properties
    Entity contactEntity = new Entity(Contact.CONTACT);
    contactEntity.setProperty(Contact.FIRST_NAME, firstName);
    contactEntity.setProperty(Contact.LAST_NAME, lastName);
    contactEntity.setProperty(Contact.EMAIL, email);
    contactEntity.setProperty(Contact.SUBJECT, subject);
    contactEntity.setProperty(Contact.TIMESTAMP, timestamp);
    // Insert entities into datastore then redirect user
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(contactEntity);

    response.sendRedirect("/contact.html");
  }
}
