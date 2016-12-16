import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by saranshsharma on 5/10/16.
 **/
public class Dispatcher
{

    public static void main(String[] args) throws EmailException {


        ArrayList<String> headerList = new ArrayList<String>();
        ArrayList<String> passedInfo = new ArrayList<String>();
        String vBoxName = "unknown";
        String appVersion = "unknown";
        headerList.add("S.no");
        headerList.add("Test Name");
        headerList.add("Result");
        headerList.add("Time taken (in seconds)");
        headerList.add("Test Description");


        ArrayList<String> rowList = new ArrayList<String>();

        try{
            FileInputStream fstream = new FileInputStream("outResults_RAW.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                if (!strLine.isEmpty() && strLine.contains("test")) {
                    rowList.add(strLine);
                } else {
                    if (!strLine.isEmpty()) {
                        passedInfo.add(strLine);
                    }
                }

            }
            in.close();
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        vBoxName = passedInfo.get(0);
        appVersion = passedInfo.get(1);


//        String htmlMessage = "";

        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("vostokmailer@gmail.com","TAGcow@7391"));
        email.setSSLOnConnect(true);
        email.setFrom("saransh1337@gmail.com");
        email.setSubject("Vostok test report 19th May");
        email.setHtmlMsg(
                "<html>" +
                        "<body>" +

                        "<p>Hi all,</p>"+
                        "<p>A total of " +rowList.size()+
                        " tests ran on build " +appVersion+
                        " on device " +vBoxName+
                        "</p>"+
                        "<table dir=\"ltr\" width=\"500\" border=\"1\"" +
                        "tsummary=\"test result summary\">" +
                        "<caption>" +
                        "Here is a list of all the tests that ran:" +
                        "</caption>" +
                        "<colgroup width=\"50%\" />" +
                        "<colgroup id=\"colgroup\" class=\"colgroup\" align=\"center\" \n" +
                        "tvalign=\"middle\" title=\"title\" width=\"1*\"" +
                        "span=\"2\" style=\"background:#ddd;\" />"

                        +buildTable(headerList,rowList)+

                        "</body>" +
                        "</html>");

        email.setTextMsg("Hi all,\n For some reason HTML isn't rendering in your mail client. Please fix this to see Vostok test results.\n");

        email.addTo("saransh@riva.co");
//        email.addTo("droid-testers@riva.co");
        email.send();

    }

    public static String buildTable(ArrayList<String> headerList, ArrayList<String> rowList) {
        String tableHTMLString = "";

        tableHTMLString = "" +
                "<thead>\n" +
                "\t\t<tr>";

        for(String i : headerList) {
            tableHTMLString = tableHTMLString+"<th scope=\"col\">"+i+"</th>";
        }
        tableHTMLString = tableHTMLString +
                "</tr>" +
                "</thead>";

        int counter = 0;
        tableHTMLString = tableHTMLString + "<tbody>";
        for(String i : rowList) {
            tableHTMLString = tableHTMLString + "<tr>";
            counter++;
            tableHTMLString = tableHTMLString+"<td>"+counter+"</td>";
            i = i.replaceAll("^\\s+", "");
            String strArr[] = i.split("\\s");
            for (String str : strArr){
                tableHTMLString = tableHTMLString+"<td>"+str+"</td>";
            }
            tableHTMLString = tableHTMLString + "</tr>";
        }

        tableHTMLString = tableHTMLString + "</tbody>";

        return tableHTMLString;
    }


}
