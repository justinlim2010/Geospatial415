<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.MalformedURLException"%>
<%@page import="java.net.URLConnection"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.IOException"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
  <%

  //final ArrayList<busStopCodeSet> list = new ArrayList<busStopCodeSet>();
  ArrayList<String> list = new ArrayList<String>();
  int skip = 0;
  boolean cont = true;
  while (cont) {
    final String apiUrl = "http://datamall.mytransport.sg/ltaodataservice.svc/IncidentSet?$skip=" + skip;
    /* final String apiUrl = "http://datamall.mytransport.sg/ltaodataservice.svc/IncidentSet"; */
    try {

      final URL url = new URL(apiUrl);
      final URLConnection conn = url.openConnection();
      conn.setRequestProperty("accept", "*/*");
      conn.addRequestProperty("AccountKey", "JeS9FEbhVVVni7Mzu6HS4A==");
      conn.addRequestProperty("UniqueUserID", "69805977-2bfd-47c6-873e-88fadd92c06a");
      final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line = null;
      final StringBuilder strBuilder = new StringBuilder();
      String wholeXML = "";
      while ((line = br.readLine()) != null) {
        if (skip < 50) {
          wholeXML += line;
          out.println(line);
        }
        strBuilder.append(line);
        //System.out.println(line);
      }

      final String[] splitString = strBuilder.toString().split("<m:properties>");

      for(String str : splitString){
        if (str.contains("<d:IncidentID m:type=\"Edm.Int32\">")) {
          int a = str.indexOf("<d:IncidentID m:type=\"Edm.Int32\">") + 36;
          int b = str.indexOf("</d:IncidentID>");
          final String incidentID = str.substring(a, b);
          out.println("");
          out.println("id is: " + incidentID);
        }
      }

      // final String[] splitString = strBuilder.toString().split("<m:properties>");
      // for (final String str : splitString) {
      // System.out.println("the size is: " + list.size());
      // if (str.contains("<d:BusStopCodeID m:type=\"Edm.Int32\">")) {
      // int a = str.indexOf("<d:BusStopCodeID m:type=\"Edm.Int32\">") + 36;
      // int b = str.indexOf("</d:BusStopCodeID>");
      // final String busStopCodeID = str.substring(a, b);
      // a = str.indexOf("<d:Code>") + 8;
      // b = str.indexOf("</d:Code>");
      // final String code = str.substring(a, b);
      // a = str.indexOf("<d:Road>") + 8;
      // b = str.indexOf("</d:Road>");
      // final String road = str.substring(a, b);
      // a = str.indexOf("<d:Description>") + 15;
      // b = str.indexOf("</d:Description>");
      // final String description = str.substring(a, b);
      // a = str.indexOf("<d:Layout_Num>") + 14;
      // b = str.indexOf("</d:Layout_Num>");
      // final String layout_num = str.substring(a, b);
      // a = str.indexOf("<d:Max_Pages>") + 13;
      // b = str.indexOf("</d:Max_Pages>");
      // final String max_pages = str.substring(a, b);
      // a = str.indexOf("<d:Summary xml:space=\"preserve\">") + 32;
      // b = str.indexOf("</d:Summary>");
      // final String summary = str.substring(a, b);
      // a = str.indexOf("<d:CreateDate m:type=\"Edm.DateTime\">") + 36;
      // b = str.indexOf("</d:CreateDate>");
      // final String createDate = str.substring(a, b);
      // final busStopCodeSet bs = new busStopCodeSet(busStopCodeID, code, road, description, layout_num, max_pages,
      // summary, createDate);
      // list.add(bs);
      // System.out.println("id is: " + bs.getBusStopCodeID());
      // }
      // }
      if (list.size() % 50 == 0) {
        skip += 50;
      } else {
        cont = false;
      }

      /* request.getSession().setAttribute("list", list);
      request.getSession().setAttribute("xml", wholeXML);
      request.getSession().setAttribute("test", "<p>this is a test</p>"); */
      //response.sendRedirect("index.jsp");
    } catch (final MalformedURLException ex) {
      ex.printStackTrace();
    } catch (final IOException ex) {
      ex.printStackTrace();
    } catch (final Exception ex) {
      ex.printStackTrace();
    } finally {
      out.close();
    }
  }

  %>
</body>
</html>
