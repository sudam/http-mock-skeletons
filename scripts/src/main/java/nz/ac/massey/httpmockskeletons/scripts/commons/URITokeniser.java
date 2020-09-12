package nz.ac.massey.httpmockskeletons.scripts.commons;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * this class tokenise urls and returns values
 *
 * @author thilini bhagya
 */

public class URITokeniser {
    public static String getURLScheme(String uri) throws URISyntaxException {
        //check existence of value
        URI url = new URI(uri);
        return url.getScheme();
    }

    public static String getUriHost(String uri) throws URISyntaxException {
        //check existence of value
        URI url = new URI(uri);
        return url.getHost();
    }

    public static Map<String, String> getURLCoreTokenMap(String uri) throws URISyntaxException {
        int i=1;
        URI url = new URI(uri);
        //core in a url
        StringTokenizer st = new StringTokenizer(url.getPath(), "://-");
        Map<String, String> maps = new HashMap<String, String>();
        while ( st.hasMoreElements() ) {
            String key = "pathToken"+ i++;
            String value = st.nextToken();

            maps.put(key, value);
        }

        return maps;

    }

    public static Map<String, String> getURLQueryTokenMap(String uri) throws URISyntaxException, UnsupportedEncodingException {
        int i=1;
        URI url = new URI(uri);
        Map<String, String> maps = new HashMap<String, String>();
        String s = url.getQuery();
        //core in a url
        if(s != null){
            //decord the query string
            String decoded = URLDecoder.decode(String.valueOf(url.getQuery()), "UTF-8");
            StringTokenizer st = new StringTokenizer(decoded, ":&");
            while (st.hasMoreElements()) {
                String key = "queryToken" + i++;
                String value = st.nextToken();

                maps.put(key, value);
            }
        }

        return maps;
    }

    public static boolean checkURLCoreTokenMapContainsValue(String uri, String value) throws URISyntaxException {
        //check existence of value
        Map<String, String> newmap = getURLCoreTokenMap(uri);
        return newmap.containsValue(value);
    }

    public static Map<String, String> getFragmentMap(String uri) throws URISyntaxException, UnsupportedEncodingException {
        int i=1;
        URI url = new URI(uri);
        Map<String, String> maps = new HashMap<String, String>();
        String s = url.getFragment();
        //core in a url
        if(s != null){
            //decord the query string
            String decoded = URLDecoder.decode(String.valueOf(url.getFragment()), "UTF-8");
            StringTokenizer st = new StringTokenizer(decoded, ":&");
            System.out.println("The file is not empty");
            while (st.hasMoreElements()) {
                String key = "fragmentToken" + i++;
                String value = st.nextToken();

                maps.put(key, value);
            }
        }



        return maps;
    }
}
