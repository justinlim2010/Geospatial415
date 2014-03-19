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

import org.json.JSONObject;
import org.json.XML;

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
    while (cont) {
      final String apiUrl = "http://datamall.mytransport.sg/ltaodataservice.svc/IncidentSet?$skip=" + skip;
      try {

        final URL url = new URL(apiUrl);
        final URLConnection conn = url.openConnection();
        conn.setRequestProperty("accept", "*/*");
        conn.addRequestProperty("AccountKey", "JeS9FEbhVVVni7Mzu6HS4A==");
        conn.addRequestProperty("UniqueUserID", "69805977-2bfd-47c6-873e-88fadd92c06a");
        final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        while ((line = br.readLine()) != null) {
          strBuilder.append(line);

        }

        // Method use to get the numbers of entry, if >50 entries, attempt to call REST service to datamall
        final String[] splitString = strBuilder.toString().split("<m:entry>");
        for (final String str : splitString) {
          if (str.contains("<d:IncidentID m:type=\"Edm.Int32\">")) {
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

        // Conversion to XML using org.json library
        final JSONObject xmlJSONObj = XML.toJSONObject(strBuilder.toString());
        final String jsonPrettyPrintString = xmlJSONObj.toString();
        out.println(jsonPrettyPrintString);
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
