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
  /**
   * set of constants that are POST request parameter keys and Entity property keys
   */
  private static final String CONTACT = "Contact";
  private static final String FIRST_NAME = "firstname";
  private static final String LAST_NAME = "lastname";
  private static final String EMAIL = "email";
  private static final String SUBJECT = "subject";
  private static final Gson GSON = new Gson();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(CONTACT);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    int numContactsToDisplay = Integer.parseInt(request.getParameter("numContactsToDisplay"));
    List<Contact> contacts = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      // Display x amounts of tasks
      if (numContactsToDisplay == contacts.size()) {
        break;
      }
      // create a new task from current entity
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
    String firstName = getParameter(request, FIRST_NAME, "");
    String lastName = getParameter(request, LAST_NAME, "");
    String email = getParameter(request, EMAIL, "");
    String subject = getParameter(request, SUBJECT, "");
    // Create new entities with contact properties
    Entity contactEntity = new Entity(CONTACT);
    contactEntity.setProperty(FIRST_NAME, firstName);
    contactEntity.setProperty(LAST_NAME, lastName);
    contactEntity.setProperty(EMAIL, email);
    contactEntity.setProperty(SUBJECT, subject);
    // Insert entities into datastore then redirect user
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(contactEntity);

    response.sendRedirect("/contact.html");
  }
}
