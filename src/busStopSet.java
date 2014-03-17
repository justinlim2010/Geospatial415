import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class busStopSet
 */
@WebServlet("/busStopSet")
public class busStopSet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public busStopSet() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
      IOException {

    final ArrayList<busStopCodeSet> list = new ArrayList<busStopCodeSet>();
    int skip = 0;
    boolean cont = true;
    while (cont) {
      final String apiUrl = "http://datamall.mytransport.sg/ltaodataservice.svc/IncidentSet?$skip=" + skip;
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
          }
          strBuilder.append(line);
          System.out.println(line);
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
        request.getSession().setAttribute("list", list);
        request.getSession().setAttribute("xml", wholeXML);
        request.getSession().setAttribute("test", "<p>this is a test</p>");
        response.sendRedirect("index.jsp");
      } catch (final MalformedURLException ex) {
        ex.printStackTrace();
      } catch (final IOException ex) {
        ex.printStackTrace();
      } catch (final Exception ex) {
        ex.printStackTrace();
      } finally {
        // out.close();
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
