package nz.ac.massey.httpmockskeletons.scripts.commons;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.*;

/**
 * this class forms http transactions with their properties
 *
 * @author thilini bhagya
 */

public class HTTPTransaction {

    public String resource, transaction, time, method, code, url, responseBody, requestHeaders, responseHeaders, requestBody;

    public HTTPTransaction(String string) throws URISyntaxException {

        //delimiter has to be changed if csv file formed with comma
        StringTokenizer tok = null;

        if(string.contains("€")){
            tok = new StringTokenizer(string, "€", false);
        }else{
            tok = new StringTokenizer(string, "|", false);
        }

        resource = tok.nextToken();
        transaction = tok.nextToken();
        time = tok.nextToken();
        method = tok.nextToken();
        code = tok.nextToken();
        url = tok.nextToken();
        responseBody = tok.nextToken();
        requestHeaders = tok.nextToken();
        responseHeaders = tok.nextToken();

        if(string.contains("€")){
            requestBody = tok.nextToken();
        }
    }

    public String getResource() {
        return resource;
    }
    public String getTransaction() {
        return transaction;
    }
    public String getMethod() {
        return method;
    }
    public String getTime() {
        return time;
    }
    public String getCode() {
        return code;
    }
    public String getURL() {
        return url;
    }
    public String getResponseBody() {
        return responseBody;
    }
    public String getRequestHeaders() {
        return requestHeaders;
    }
    public String getResponseHeaders() {
        return responseHeaders;
    }
    public String getRequestBody() {
        return requestBody;
    }

    @Override
    public String toString() {
        return "transactionId=" + transaction
                + ", method=" + method + ", code=" + code + ", url=" + url;
    }

    public static Map<String, TreeMap<String, List<HTTPTransaction>>> transactions = new HashMap();

    //read data from csv file
    public static void read(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            String line;
            //skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                HTTPTransaction tr = new HTTPTransaction(line);
                add(tr);
            }
        }
        finally {
            br.close();
        }
    }

    //add transactions to a map with same resource
    public static void add(HTTPTransaction tr) {
        if (!transactions.containsKey(tr.getResource())) {
            transactions.put(tr.getResource(), new TreeMap());

        }
        if (!transactions.get(tr.getResource()).containsKey(tr.getTransaction())) {
            transactions.get(tr.getResource()).put(tr.getTime(), new ArrayList());

        }
        transactions.get(tr.getResource()).get(tr.getTime()).add(tr);
    }

}
