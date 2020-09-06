package nz.ac.massey.httpmockskeletons.scripts.datapreparator.rawdatapreprocessor;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * This class extracts resource,time,method,status,url,request/response body and headers data of each xml record
 * in raw output-googletasklists.xml file
 * sorts data using resource identifiers
 * saves data in CSV format
 *
 * @author thilini bhagya
 */

public class RawXMLToCSVConverterForGoogleTasks {
    static org.apache.log4j.Logger LOGGER = Logging.getLogger(RawXMLToCSVConverterForGoogleTasks.class);
    // Read request.xml data
    public static void fileWriter(String fileName, String xmlFileName) throws Exception {
        LOGGER.info("Cleaning Google Tasks dataset");
        FileWriter writer = new FileWriter(fileName);

        //csv file headers
        writer.append("resource");
        writer.append('|');
        writer.append("transaction");
        writer.append('|');
        writer.append("time");
        writer.append('|');
        writer.append("method");
        writer.append('|');
        writer.append("status");
        writer.append('|');
        writer.append("url");
        writer.append('|');
        writer.append("responseBody");
        writer.append('|');
        writer.append("requestHeaders");
        writer.append('|');
        writer.append("responseHeaders");
        writer.append('\n');

        int i = 1;

        File fXmlFile = new File(System.getProperty("user.dir") + File.separator + "src/resources/" + xmlFileName);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        NodeList nList = doc.getElementsByTagName("httpSample");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                String responseHeader1 = eElement.getElementsByTagName("responseHeader").item(0).getTextContent();
                String responseHeaderDate = "";

                Scanner scanner = new Scanner(responseHeader1);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains("Date")) {
                        responseHeaderDate = (line.split(":", 2)[1]).trim();
                    }
                }
                scanner.close();

                DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
                Date date = dateFormat.parse(responseHeaderDate);
                long unixTime = date.getTime() / 1000;

                //status
                String string = eElement.getElementsByTagName("responseHeader").item(0).getTextContent();
                String[] a = string.split(" ");
                String content = eElement.getElementsByTagName("responseData").item(0).getTextContent();
                //resource
                if ((eElement.getElementsByTagName("method").item(0).getTextContent().matches("POST"))) {
                    if (a[1].matches("200")) {

                        String c = content.substring(37, 89);
                        //System.out.println(c);
                        //System.out.println(content);
                        writer.append(c);
                        writer.append('|');
                        writer.append(String.valueOf(i++));
                        writer.append('|');
                        //time
                        writer.append(String.valueOf(unixTime));
                        writer.append('|');
                        //method
                        writer.append(eElement.getElementsByTagName("method").item(0).getTextContent());
                        writer.append('|');
                        //status
                        writer.append(a[1]);
                        writer.append('|');
                        //url
                        String url = eElement.getElementsByTagName("java.net.URL").item(0).getTextContent();
                        writer.append(url);
                        writer.append('|');
                        //responseBody
                        writer.append(content.replaceAll("\n", "").replaceAll(" ", "").replaceAll("\\\\", ""));
                        writer.append('|');
                        //request headers
                        String requestHeader = eElement.getElementsByTagName("requestHeader").item(0).getTextContent();
                        requestHeader = requestHeader.replaceAll("\n", "\t").replaceAll(": ", "~");
                        if (requestHeader.endsWith("\t")) {
                            requestHeader = requestHeader.substring(0, requestHeader.length() - 1);
                            writer.append(requestHeader);
                        }
                        //response header
                        writer.append('|');
                        String responseHeader = eElement.getElementsByTagName("responseHeader").item(0).getTextContent();
                        responseHeader = responseHeader.replaceAll("\n", "\t").replaceAll(": ", "~");

                        if (responseHeader.endsWith("\t")) {
                            responseHeader = responseHeader.substring(0, responseHeader.length() - 1);
                            writer.append(responseHeader);
                        }

                        //new line
                        writer.append('\n');
                    } else if (a[1].matches("500")) {
                        //System.out.println(c);

                    }
                } else if ((eElement.getElementsByTagName("method").item(0).getTextContent().matches("GET")) && (a[1].matches("200")) || (a[1].matches("404")) || (a[1].matches("503"))) {

                    String url = eElement.getElementsByTagName("java.net.URL").item(0).getTextContent();
                    //System.out.println(url);
                    String id = url.substring(url.indexOf("lists/") + 6);
                    //System.out.println(id);
                    writer.append(id);
                    writer.append('|');
                    writer.append(String.valueOf(i++));
                    writer.append('|');
                    //time
                    writer.append(String.valueOf(unixTime));
                    writer.append('|');
                    //method
                    writer.append(eElement.getElementsByTagName("method").item(0).getTextContent());
                    writer.append('|');
                    //status
                    writer.append(a[1]);
                    writer.append('|');
                    //url
                    writer.append(url);
                    writer.append('|');
                    //responseBody
                    writer.append(content.replaceAll("\n", "").replaceAll(" ", "").replaceAll("\\\\", ""));
                    //request headers
                    writer.append('|');
                    String requestHeader = eElement.getElementsByTagName("requestHeader").item(0).getTextContent();
                    requestHeader = requestHeader.replaceAll("\n", "\t").replaceAll(": ", "~");
                    if (requestHeader.endsWith("\t")) {
                        requestHeader = requestHeader.substring(0, requestHeader.length() - 1);
                        writer.append(requestHeader);
                    }
                    //response header
                    writer.append('|');
                    String responseHeader = eElement.getElementsByTagName("responseHeader").item(0).getTextContent();
                    responseHeader = responseHeader.replaceAll("\n", "\t").replaceAll(": ", "~");

                    if (responseHeader.endsWith("\t")) {
                        responseHeader = responseHeader.substring(0, responseHeader.length() - 1);
                        writer.append(responseHeader);


                    }
                    //new line
                    writer.append('\n');
                } else if ((eElement.getElementsByTagName("method").item(0).getTextContent().matches("PATCH")) && (a[1].matches("200")) || (a[1].matches("404")) || (a[1].matches("503"))) {

                    String url = eElement.getElementsByTagName("java.net.URL").item(0).getTextContent();
                    //System.out.println(url);
                    String id = url.substring(url.indexOf("lists/") + 6);
                    //System.out.println(id);
                    writer.append(id);
                    writer.append('|');
                    writer.append(String.valueOf(i++));
                    writer.append('|');
                    //time
                    writer.append(eElement.getAttribute("ts"));
                    writer.append('|');
                    //method
                    writer.append(eElement.getElementsByTagName("method").item(0).getTextContent());
                    writer.append('|');
                    //status
                    writer.append(a[1]);
                    writer.append('|');
                    //url
                    writer.append(url);
                    writer.append('|');
                    //responseBody
                    writer.append(content.replaceAll("\n", "").replaceAll(" ", "").replaceAll("\\\\", ""));
                    //request headers
                    writer.append('|');
                    String requestHeader = eElement.getElementsByTagName("requestHeader").item(0).getTextContent();
                    requestHeader = requestHeader.replaceAll("\n", "\t").replaceAll(": ", "~");
                    if (requestHeader.endsWith("\t")) {
                        requestHeader = requestHeader.substring(0, requestHeader.length() - 1);
                        writer.append(requestHeader);
                    }
                    //response header
                    writer.append('|');
                    String responseHeader = eElement.getElementsByTagName("responseHeader").item(0).getTextContent();
                    responseHeader = responseHeader.replaceAll("\n", "\t").replaceAll(": ", "~");

                    if (responseHeader.endsWith("\t")) {
                        responseHeader = responseHeader.substring(0, responseHeader.length() - 1);
                        writer.append(responseHeader);


                    }
                    //new line
                    writer.append('\n');
                } else if ((eElement.getElementsByTagName("method").item(0).getTextContent().matches("DELETE")) && (a[1].matches("204")) || (a[1].matches("404")) || (a[1].matches("503"))) {

                    String url = eElement.getElementsByTagName("java.net.URL").item(0).getTextContent();
                    //System.out.println(url);
                    String id = url.substring(url.indexOf("lists/") + 6);
                    //System.out.println(id);
                    writer.append(id);
                    writer.append('|');
                    writer.append(String.valueOf(i++));
                    writer.append('|');
                    //time
                    writer.append(String.valueOf(unixTime));
                    writer.append('|');
                    //method
                    writer.append(eElement.getElementsByTagName("method").item(0).getTextContent());
                    writer.append('|');
                    //status
                    writer.append(a[1]);
                    writer.append('|');
                    //url
                    writer.append(url);
                    writer.append('|');
                    //responseBody
                    writer.append(content.replaceAll("\n", "").replaceAll(" ", "").replaceAll("\\\\", ""));
                    //request headers
                    writer.append('|');
                    String requestHeader = eElement.getElementsByTagName("requestHeader").item(0).getTextContent();
                    requestHeader = requestHeader.replaceAll("\n", "\t").replaceAll(": ", "~");
                    if (requestHeader.endsWith("\t")) {
                        requestHeader = requestHeader.substring(0, requestHeader.length() - 1);
                        writer.append(requestHeader);
                    }
                    //response header
                    writer.append('|');
                    String responseHeader = eElement.getElementsByTagName("responseHeader").item(0).getTextContent();
                    responseHeader = responseHeader.replaceAll("\n", "\t").replaceAll(": ", "~");

                    if (responseHeader.endsWith("\t")) {
                        responseHeader = responseHeader.substring(0, responseHeader.length() - 1);
                        writer.append(responseHeader);
                    }
                    //new line
                    writer.append('\n');
                }
            }
        }

        writer.flush();
        writer.close();
    }
}
