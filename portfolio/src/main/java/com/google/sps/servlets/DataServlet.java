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
import java.io.IOException;
import java.util. *;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.util.concurrent.LinkedBlockingQueue; 
 
/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  private ArrayList<String> names = new ArrayList<String>();
 
  @Override
 public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Task").addSort("comment", SortDirection.DESCENDING);
 
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
 
    ArrayList<String> commentsFromDB = new ArrayList<String>();
    for (Entity entity : results.asIterable()) {
        String entityComment = (String) entity.getProperty("comment");
        commentsFromDB.add(entityComment);
    }
 
    String json = convertToJsonUsingGson(commentsFromDB);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
 
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String inputText = getParameter(request, "text-area", "");
    names.add(inputText);
 
    Entity taskEntity = new Entity("Task");
    taskEntity.setProperty("comment", inputText);
 
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);
    
    StringBuilder text = new StringBuilder();
    text.append(request.getReader());
    
    log("testing to see if i could get the string from body: " + text.toString());
    // String json = convertToJsonUsingGson(names);
    // response.setContentType("application/json;");
    // response.getWriter().println(names);
 
    doGet(request,response);
    }


  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
      String value = request.getParameter(name);
      if (value == null) {
          return defaultValue;
      }
      return value;
  }

  private String convertToJsonUsingGson(ArrayList<String> namesArr) {
    Gson gson = new Gson();
    String json = gson.toJson(namesArr);
    return json;
  }
}
