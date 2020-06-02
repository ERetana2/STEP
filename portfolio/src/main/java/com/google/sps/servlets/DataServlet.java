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

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query("Task");

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        List<Task> tasks = new ArrayList<>();
        for (Entity entity : results.asIterable()) {
            String firstName = (String) entity.getProperty("firstname");
            String lastName = (String) entity.getProperty("lastname");
            String email = (String) entity.getProperty("email");
            String subject = (String) entity.getProperty("subject");

            Task task = new Task(firstName,lastName,email,subject);
            tasks.add(task);
        }

        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().println(convertToJsonUsingGson(tasks));
    }

    private String convertToJsonUsingGson(List <Task> tasks) {
        Gson gson = new Gson();
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

   //@Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the input from the form.
        String firstName = getParameter(request, "firstname", "");
        String lastName = getParameter(request, "lastname", ""); 
        String email = getParameter(request, "email", ""); 
        String subject = getParameter(request, "subject", "");

        Entity taskEntity = new Entity("Task");
        taskEntity.setProperty("firstname", firstName);
        taskEntity.setProperty("lastname",lastName);
        taskEntity.setProperty("email", email);
        taskEntity.setProperty("subject", subject);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(taskEntity);

        response.sendRedirect("/contact.html");
    }
}
