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
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet{
    /**
    * set of constants that are POST request parameter keys and Entity property keys
    */
    private static final String CONTACT = "Contact";
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "lastname";
    private static final String EMAIL = "email";
    private static final String SUBJECT = "subject";
    private static final String TIMESTAMP = "timestamp";
    private static final Gson gson = new Gson();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query(CONTACT);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        int displayTasks = Integer.parseInt(request.getParameter("displayComments"));
        List<Contact> contacts = new ArrayList<>();
        for (Entity entity : results.asIterable()) {
            //Display x amounts of tasks
            if (displayTasks == 0){
                break;
            }else{
                displayTasks--;
            }
            //create a new task from current entity
            contacts.add(fromEntity(entity));
        }
        response.setContentType("application/json");
        response.getWriter().println(gson.toJson(contacts));
    }
    private static String convertToJsonUsingGson(List <Contact> contacts) {
        String json = gson.toJson(contacts);
        return json;
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
    /**
    * @return a new contact containing the properties of the passed entity in the parameter
    */
    public static Contact fromEntity(Entity entity){
        String firstName = (String) entity.getProperty(FIRST_NAME);
        String lastName = (String) entity.getProperty(LAST_NAME);
        String email = (String) entity.getProperty(EMAIL);
        String subject = (String) entity.getProperty(SUBJECT);

        return new Contact(firstName, lastName, email, subject);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = getParameter(request, FIRST_NAME, "");
        String lastName = getParameter(request, LAST_NAME, ""); 
        String email = getParameter(request, EMAIL, ""); 
        String subject = getParameter(request, SUBJECT, "");
        //Create new entities with contact properties
        Entity contactEntity = new Entity(CONTACT);
        contactEntity.setProperty(FIRST_NAME, firstName);
        contactEntity.setProperty(LAST_NAME,lastName);
        contactEntity.setProperty(EMAIL, email);
        contactEntity.setProperty(SUBJECT, subject);
        //Insert entities into datastore then redirect user
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(contactEntity);

        response.sendRedirect("/contact.html");
    }
}
