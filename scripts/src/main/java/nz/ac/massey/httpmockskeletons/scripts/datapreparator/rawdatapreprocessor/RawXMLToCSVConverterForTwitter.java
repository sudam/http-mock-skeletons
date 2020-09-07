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
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class extracts resource,time,method,status,url,request/response body and headers data of each xml record
 * in raw Twitter dataset
 * it then sorts data using resource identifiers
 * and saves data in CSV format
 *
 * @author thilini bhagya
 */
public class RawXMLToCSVConverterForTwitter {
    static org.apache.log4j.Logger LOGGER = Logging.getLogger(RawXMLToCSVConverterForTwitter.class);
    //read request.xml data
    public static void fileWriter(String fileName, String xmlFileName) throws Exception {
        LOGGER.info("Cleaning Twitter dataset");
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
        writer.append('|');
        writer.append("requestBody");
        writer.append('\n');

        int i = 0;

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
                    if (line.contains("date:")) {
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
                String urls = eElement.getElementsByTagName("java.net.URL").item(0).getTextContent();
                //resource
                if ((eElement.getElementsByTagName("method").item(0).getTextContent().matches("POST"))) {
                    if (a[1].matches("200") && urls.matches("https://api.twitter.com/1.1/statuses/update.json")) {
                        String content = eElement.getElementsByTagName("responseData").item(0).getTextContent();
                        String c = content.substring(52, 71);
                        writer.append(c);
                        writer.append('|');
                        writer.append(String.valueOf(i++));
                        writer.append('|');
                        //time
                        writer.append(String.valueOf(unixTime));
                        //writer.append(eElement.getAttribute("ts"));
                        writer.append('|');
                        //method
                        writer.append(eElement.getElementsByTagName("method").item(0).getTextContent());
                        writer.append('|');
                        //status
                        writer.append(a[1]);
                        writer.append('|');
                        //url
                        String url = eElement.getElementsByTagName("java.net.URL").item(0).getTextContent() + "?" + eElement.getElementsByTagName("queryString").item(0).getTextContent();
                        writer.append(url);
                        writer.append('|');
                        //response body
                        writer.append(content);
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
                    } else if (a[1].matches("200") && urls.contains("https://api.twitter.com/1.1/statuses/destroy/")) {
                        String content = eElement.getElementsByTagName("responseData").item(0).getTextContent();
                        String c = content.substring(52, 71);
                        writer.append(c);
                        writer.append('|');
                        writer.append(String.valueOf(i++));
                        writer.append('|');
                        //time
                        writer.append(String.valueOf(unixTime));
                        //writer.append(eElement.getAttribute("ts"));
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
                        //response body
                        writer.append(content);
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
                    } else if (a[1].matches("404")) {
                        String content = eElement.getElementsByTagName("responseData").item(0).getTextContent();
                        String url = eElement.getElementsByTagName("java.net.URL").item(0).getTextContent();
                        String id = url.substring(url.indexOf("destroy/") + 8).substring(url.substring(url.indexOf("destroy/") + 8).indexOf("1"), url.substring(url.indexOf("destroy/") + 8).indexOf(".json"));
                        writer.append(id);
                        writer.append('|');
                        writer.append(String.valueOf(i++));
                        writer.append('|');
                        //time
                        writer.append(String.valueOf(unixTime));
                        //writer.append(eElement.getAttribute("ts"));
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
                        //response body
                        writer.append(content);
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
                } else if ((eElement.getElementsByTagName("method").item(0).getTextContent().matches("GET")) && (a[1].matches("200")) || (a[1].matches("404"))) {
                    String content = eElement.getElementsByTagName("responseData").item(0).getTextContent();
                    String url = eElement.getElementsByTagName("java.net.URL").item(0).getTextContent();
                    String id = url.substring(url.indexOf("=") + 1);
                    writer.append(id);
                    writer.append('|');
                    writer.append(String.valueOf(i++));
                    writer.append('|');
                    //time
                    writer.append(String.valueOf(unixTime));
                    //writer.append(eElement.getAttribute("ts"));
                    writer.append('|');
                    //method
                    writer.append(eElement.getElementsByTagName("method").item(0).getTextContent());
                    writer.append('|');
                    //status
                    writer.append(a[1]);
                    writer.append('|');
                    //action
                    writer.append(url);
                    writer.append('|');
                    //response body
                    writer.append(content);
                    //request headers
                    writer.append('|');
                    String requestHeader = eElement.getElementsByTagName("requestHeader").item(0).getTextContent();
                    requestHeader = requestHeader.replaceAll("\n", "\t").replaceAll(": ", "~");
                    if (requestHeader.endsWith("\t")) {
                        requestHeader = requestHeader.substring(0, requestHeader.length() - 1);
                        writer.append(requestHeader);
                    }
                    //response headers
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

    //modify query string as a map
    public static Map<String, String> getQueryTokenMap(String a) throws URISyntaxException, UnsupportedEncodingException {
        Map<String, String> maps = new HashMap<String, String>();
        //decode query string
        String decoded = URLDecoder.decode(a, "UTF-8");
        StringTokenizer st = new StringTokenizer(decoded, ",=&");
        while (st.hasMoreElements()) {
            String key = st.nextToken();
            String value = st.nextToken();
            maps.put(key, value);
        }
        return maps;
    }
}
