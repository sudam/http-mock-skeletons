package nz.ac.massey.httpmockskeletons.scripts.datapreparator.rawdatapreprocessor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.text.*;

/**
 * This class extracts resource,time,method,status,url,request/response body and headers data of each json record
 * in raw ghtraffic-S-1.0.0.json file
 * sorts data using resource identifiers
 * saves data in CSV format
 *
 * @author thilini bhagya
 */

public class RawJSONToCSVConverterForGHTraffic {
    //read request.xml data
    public static File fileWriter(String fileName, String xmlFileName) throws IOException, java.text.ParseException, ParseException {
        FileWriter writer = new FileWriter(fileName);

        //csv file headers
        writer.append("resource");
        //use € character as column separator (because many commas are in response bodies)
        writer.append("€");
        writer.append("transaction");
        writer.append("€");
        writer.append("time");
        writer.append("€");
        writer.append("method");
        writer.append("€");
        writer.append("status");
        writer.append("€");
        writer.append("url");
        writer.append("€");
        writer.append("responseBody");
        writer.append("€");
        writer.append("requestHeaders");
        writer.append("€");
        writer.append("responseHeaders");
        writer.append("€");
        writer.append("requestBody");
        writer.append('\n');

        int i = 0;

        //remove comma and add square brackets
        //converted one by one separate files

        //get JSONObjects from the array
        JSONParser parser = new JSONParser();
        JSONArray array = null;

        FileReader xmlFileReader = new FileReader("src/resources/" + xmlFileName);
        File xmlFile = new File("src/resources/" + xmlFileName);

        array = (JSONArray) parser.parse(xmlFileReader);

        //read each record and extract data
        for (Object object : array) {
            JSONObject transaction = (JSONObject) object;

            JSONObject request = (JSONObject) transaction.get("Request");
            JSONObject response = (JSONObject) transaction.get("Response");
            JSONObject responseHeader = (JSONObject) ((JSONObject) transaction.get("Response")).get("Message-Header");
            JSONObject requestHeader = (JSONObject) ((JSONObject) transaction.get("Request")).get("Message-Header");

            String resource = null;
            if (request.get("Method").toString().equals("GET") || request.get("Method").toString().equals("HEAD") || request.get("Method").toString().equals("PATCH")) {
                String segments[] = request.get("Request-URI").toString().split("/");
                resource = segments[segments.length - 1];
            } else if (request.get("Method").toString().equals("POST")) {
                String segments[] = responseHeader.get("Location").toString().split("/");
                resource = segments[segments.length - 1];
            } else if (request.get("Method").toString().equals("PUT") || request.get("Method").toString().equals("DELETE")) {
                String segments[] = request.get("Request-URI").toString().split("/");
                resource = segments[segments.length - 2];
            }
            DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
            Date date = dateFormat.parse(String.valueOf(responseHeader.get("Date")));
            long unixTime = date.getTime() / 1000;

            writer.append(resource);
            writer.append("€");
            writer.append(String.valueOf(i++));
            writer.append("€");
            writer.append(String.valueOf(unixTime));
            writer.append("€");
            //method
            writer.append(String.valueOf(request.get("Method")));
            writer.append("€");
            //status
            writer.append(String.valueOf(response.get("Status-Code")));
            writer.append("€");
            //url
            writer.append(String.valueOf(request.get("Request-URI")));
            writer.append("€");
            //responseBody
            writer.append(String.valueOf(response.get("Message-Body")));
            //request headers
            writer.append("€");
            writer.append(String.valueOf(requestHeader));
            //response header
            writer.append("€");
            writer.append(String.valueOf(responseHeader));
            //request body
            writer.append("€");
            writer.append(String.valueOf(request.get("Message-Body")));

            //new line
            writer.append('\n');
        }

        writer.flush();
        writer.close();

        return xmlFile;

    }
}



