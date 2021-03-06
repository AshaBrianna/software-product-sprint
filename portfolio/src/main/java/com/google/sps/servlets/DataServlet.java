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

import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import java.io.IOException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;    

@WebServlet("/text")
public final class DataServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        List<Comment> comments = new ArrayList<>();
        for (Entity entity : results.asIterable()) {
            long id = entity.getKey().getId();
            String message = (String) entity.getProperty("message");
            String author = (String) entity.getProperty("author");
            long timestamp = (long) entity.getProperty("timestamp");
            Comment comment = new Comment(id, author, message, timestamp);
            comments.add(comment);
        }
        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(comments));
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the input from the form.
        String message = request.getParameter("message");
        String author = request.getParameter("author");
        long timestamp = System.currentTimeMillis();
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("message", message);
        commentEntity.setProperty("author", author);
        commentEntity.setProperty("timestamp", timestamp);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);
        response.sendRedirect("/commentPage.html");
    }
}