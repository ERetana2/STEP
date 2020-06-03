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
import com.google.sps.data.Task;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    private static final Gson gson = new Gson();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query("Task").addSort("timestamp", SortDirection.DESCENDING);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        List<Task> tasks = new ArrayList<>();
        int displayTasks = Integer.parseInt(getParameter(request, "quantity", ""));
        for (Entity entity : results.asIterable()) {
            //Display x amount of entities taken from user input
            String firstName = (String) entity.getProperty("firstname");
            String lastName = (String) entity.getProperty("lastname");
            String email = (String) entity.getProperty("email");
            String subject = (String) entity.getProperty("subject");

            tasks.add(new Task(firstName,lastName,email,subject));
            
            if (displayTasks == 0){
                break;
            }
            else{
                displayTasks--;
            }
        }

        response.setContentType("application/json");
        response.getWriter().println(gson.toJson(tasks));
    }

    private String convertToJsonUsingGson(List <Task> tasks) {
        String json = gson.toJson(tasks);
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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long timestamp = System.currentTimeMillis();

        // Get the input from the form.
        String firstName = getParameter(request, "firstname", "");
        String lastName = getParameter(request, "lastname", ""); 
        String email = getParameter(request, "email", ""); 
        String subject = getParameter(request, "subject", "");
        //Create new entities with contact properties
        Entity taskEntity = new Entity("Task");
        taskEntity.setProperty("firstname", firstName);
        taskEntity.setProperty("lastname",lastName);
        taskEntity.setProperty("email", email);
        taskEntity.setProperty("subject", subject);
        //Insert entities into datastore then redirect user
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(taskEntity);

        response.sendRedirect("/contact.html");
    }
}
