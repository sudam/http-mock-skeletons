package nz.ac.massey.httpmockskeletons.scripts.datapreparator.rawdatapreprocessor;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import nz.ac.massey.httpmockskeletons.scripts.commons.URITokeniser;
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

import org.json.*;

/**
 * this class extracts resource,time,method,status,url,request/response body and headers data of each xml record
 * in raw Slack dataset
 * it then sorts data using resource identifiers
 * and saves data in CSV format
 *
 * @author thilini bhagya
 */

public class RawXMLToCSVConverterForSlack {
    static org.apache.log4j.Logger LOGGER = Logging.getLogger(RawXMLToCSVConverterForSlack.class);
    //read xml file, extract http features and write to a csv file
    public static void csvFileWriter(String fileName, String xmlFileName) throws Exception {
        LOGGER.info("Cleaning Slack dataset");
        int transactionIndex = 0;

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

        //read xml file via DOM parser
        File fXmlFile = new File(System.getProperty("user.dir") + File.separator + "src/resources/"+ xmlFileName);
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

                //status code
                String statusLine = eElement.getElementsByTagName("responseHeader").item(0).getTextContent();
                String[] status = statusLine.split(" ");
                //resource
                if ((eElement.getElementsByTagName("method").item(0).getTextContent().matches("POST"))) {
                    if (status[1].matches("200")) {

                        String content = eElement.getElementsByTagName("responseData").item(0).getTextContent();
                        if (content.contains("\"ok\":false")) {
                            if (eElement.getElementsByTagName("java.net.URL").item(0).getTextContent().contains("https://slack.com/api/chat.update")
                                    || eElement.getElementsByTagName("java.net.URL").item(0).getTextContent().contains("https://slack.com/api/chat.postMessage")) {
                                String query = eElement.getElementsByTagName("queryString").item(0).getTextContent();
                                writer.append(getQueryTokenMap(query).get("ts"));
                                writer.append('|');
                            } else {
                                String url = eElement.getElementsByTagName("java.net.URL").item(0).getTextContent();
                                writer.append(URITokeniser.getURLQueryTokenMap(url).get("queryToken3").substring(3));
                                writer.append('|');
                            }
                        } else {
                            JSONObject jsonObject2 = new JSONObject(content);
                            writer.append(jsonObject2.get("ts").toString());
                            writer.append('|');
                        }
                        writer.append(String.valueOf(transactionIndex++));
                        writer.append('|');
                        //time
                        writer.append(String.valueOf(unixTime));
                        writer.append('|');
                        //method
                        writer.append(eElement.getElementsByTagName("method").item(0).getTextContent());
                        writer.append('|');
                        //status
                        writer.append(status[1]);
                        writer.append('|');
                        if (eElement.getElementsByTagName("java.net.URL").item(0).getTextContent().contains("https://slack.com/api/chat.update")
                                || eElement.getElementsByTagName("java.net.URL").item(0).getTextContent().contains("https://slack.com/api/chat.postMessage")) {
                            //url
                            String url = eElement.getElementsByTagName("java.net.URL").item(0).getTextContent() + "?" + eElement.getElementsByTagName("queryString").item(0).getTextContent();
                            writer.append(url);
                            writer.append('|');
                            //responseBody
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
                        } else {
                            //url
                            String url = eElement.getElementsByTagName("java.net.URL").item(0).getTextContent();
                            writer.append(url);
                            writer.append('|');
                            //responseBody
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

