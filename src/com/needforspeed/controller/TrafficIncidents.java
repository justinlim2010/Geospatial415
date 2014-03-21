package com.needforspeed.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class TrafficIncidents
 */
@WebServlet("/TrafficIncidents")
public class TrafficIncidents extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public TrafficIncidents() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
      IOException {
    System.out.println("In servlet");
    // TODO Auto-generated method stub
    final ArrayList<String> list = new ArrayList<String>();
    int skip = 0;
    boolean cont = true;
    String line = null;
    final StringBuilder strBuilder = new StringBuilder();
    final PrintWriter out = response.getWriter();
    final JSONArray array = new JSONArray();
    final JSONObject convertedJSONObj = new JSONObject();
    while (cont) {
      final String apiUrl = "http://datamall.mytransport.sg/ltaodataservice.svc/IncidentSet?$skip=" + skip;
      try {

        final URL url = new URL(apiUrl);
        final URLConnection conn = url.openConnection();
        conn.setRequestProperty("accept", "*/*");
        conn.addRequestProperty("AccountKey", "JeS9FEbhVVVni7Mzu6HS4A==");
        conn.addRequestProperty("UniqueUserID", "69805977-2bfd-47c6-873e-88fadd92c06a");
        final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        // while ((line = br.readLine()) != null) {
        // wholeXML += line;
        // strBuilder.append(line);
        //
        // }
        // System.out.println(wholeXML);
        // // Method use to get the numbers of entry, if >50 entries, attempt to call REST service to datamall
        // final String[] splitString = strBuilder.toString().split("<m:entry>");
        // for (final String str : splitString) {
        // if (str.contains("<d:IncidentID m:type=\"Edm.Int32\">")) {
        // list.add("");
        // }
        // }

        while ((line = br.readLine()) != null) {

          strBuilder.append(line);
          // System.out.println(line);
        }

        // System.out.println(strBuilder);
        final String[] splitString = strBuilder.toString().split("<entry>");
        for (final String str : splitString) {

          if (str.contains("<d:IncidentID m:type=\"Edm.Int32\">")) {
            int a = str.indexOf("<d:IncidentID m:type=\"Edm.Int32\">") + 36;
            int b = str.indexOf("</d:IncidentID>");
            final String incidentID = str.substring(a, b);
            a = str.indexOf("<geo:long xmlns:geo=\"http://www.georss.org/georss\">") + 51;
            b = str.indexOf("</geo:long>");
            final String geoLong = str.substring(a, b);
            a = str.indexOf("<geo:lat xmlns:geo=\"http://www.georss.org/georss\">") + 50;
            b = str.indexOf("</geo:lat>");
            final String geoLat = str.substring(a, b);
            a = str.indexOf("<d:Type>") + 8;
            b = str.indexOf("</d:Type>");
            final String type = str.substring(a, b);
            a = str.indexOf("<title type=\"text\">") + 19;
            b = str.indexOf("</title>");
            final String description = str.substring(a, b);
            final JSONObject obj = new JSONObject();
            obj.put("incidentID", incidentID);
            obj.put("type", type);
            obj.put("long", geoLong);
            obj.put("lat", geoLat);
            obj.put("description", description);
            array.put(obj);
            convertedJSONObj.put("traffic", array);

            list.add("");
          }
        }

        // If entries <50, stop REST call to datamall.
        if (list.size() % 50 == 0) {
          skip += 50;
        } else {
          cont = false;
        }

      } catch (final MalformedURLException ex) {
        ex.printStackTrace();
      } catch (final IOException ex) {
        ex.printStackTrace();
      } catch (final Exception ex) {
        ex.printStackTrace();
      } finally {
        //
        // Conversion to XML using org.json library
        // final JSONObject xmlJSONObj = XML.toJSONObject(strBuilder.toString());
        // final JSONArray array = new JSONArray();
        // array.put(xmlJSONObj);
        // final String jsonPrettyPrintString = xmlJSONObj.toString();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers",
            "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");

        out.println(convertedJSONObj);
        out.flush();
        out.close();
      }
    }
  }
  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
      IOException {
    // TODO Auto-generated method stub
  }

}
