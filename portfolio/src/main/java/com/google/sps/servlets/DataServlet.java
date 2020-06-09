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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
 
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
    String inputText = getBodyData(request);
    names.add(inputText);
 
    Entity taskEntity = new Entity("Task");
    taskEntity.setProperty("comment", inputText);
 
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);
 
    doGet(request,response);
    }

  private String getBodyData(HttpServletRequest request) throws IOException {
    String body = null;
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader bufferedReader = null;

    try{
        InputStream inputStream = request.getInputStream();
        if (inputStream != null) {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        } else {
            stringBuilder.append("");
        }
    } catch (IOException ex) {
        throw ex;
    } finally {
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                throw ex;
            }
        }
    }

    body = stringBuilder.toString();
    return body;
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
