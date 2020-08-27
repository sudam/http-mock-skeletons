package nz.ac.massey.httpmockskeletons.scripts.commons;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.util.*;

public class HeaderLabel {

    public static JSONObject getSlackArray(){
        JSONObject item = new JSONObject();
        JSONArray array = new JSONArray();

        item.put("*","*");
        item.put("private","private, no-cache, no-store, must-revalidate");
        item.put("keep-alive","keep-alive");
        item.put("json","application/json; charset=utf-8");
        item.put("26Jul1997","Mon, 26 Jul 1997 05:00:00 GMT");
        item.put("no-cache","no-cache");
        item.put("no-referrer","no-referrer");
        item.put("Apache","Apache");
        item.put("max-age","max-age=31536000; includeSubDomains; preload");
        item.put("chunked","chunked");
        item.put("Accept-Encoding","Accept-Encoding");
        item.put("accepted-chat","chat:write:user");
        item.put("cloudfront","Miss from cloudfront");
        item.put("nosniff","nosniff");
        item.put("chat","identify,channels:write,chat:write:user");
        item.put("h","h");
        item.put("1","1");
        item.put("0","0");
        item.put("p","p");
        item.put("not-exist","not-exist");

        array.add(item);

        return item;
    }

    public static JSONObject getGoogleArray(){
        JSONObject item = new JSONObject();
        JSONArray array = new JSONArray();

        item.put("none","none");
        item.put("quic","quic=:443; ma=2592000; v=44,43,39,35");
        item.put("no-cache-must-revalidate","no-cache, no-store, max-age=0, must-revalidate");
        item.put("json","application/json; charset=UTF-8");
        item.put("no-cache","no-cache");
        item.put("GSE","GSE");
        item.put("chunked","chunked");
        item.put("origin","Origin,Accept-Encoding");
        item.put("nosniff","nosniff");
        item.put("SAMEORIGIN","SAMEORIGIN");
        item.put("block","1; mode=block");
        item.put("not-exist","not-exist");
        item.put("taskList","tasks#taskList");
        item.put("must-revalidate","private, max-age=0, must-revalidate, no-transform");
        item.put("X-Origin","X-Origin");
        item.put("private","private, max-age=0");
        item.put("404","404");
        item.put("global","global");
        item.put("NotFound","NotFound");
        item.put("notFound","notFound");
        item.put("503","503");
        item.put("BackendError","BackendError");
        item.put("backendError","backendError");

        array.add(item);

        return item;
    }

    public static JSONObject getTwitterArray(){
        JSONObject item = new JSONObject();
        JSONArray array = new JSONArray();

        item.put("no-cache","no-cache, no-store, must-revalidate, pre-check=0, post-check=0");
        item.put("attachment-json","attachment; filename=json.json");
        item.put("json","application/json;charset=utf-8");
        item.put("Tue31Mar1981","Tue, 31 Mar 1981 05:00:00 GMT");
        item.put("tsa-l","tsa_l");
        item.put("200OK","200 OK");
        item.put("max-age","max-age=631138519");
        item.put("nosniff","nosniff");
        item.put("SAMEORIGIN","SAMEORIGIN");
        item.put("not-exist","not-exist");
        item.put("BouncerCompliant","BouncerCompliant");
        item.put("mode-block","1; mode=block; report=https://twitter.com/i/xss_report");
        item.put("null","null");
        item.put("empty-list","[]");
        item.put("0","0");
        item.put("false","false");
        item.put("WedMar0709","Wed Mar 07 09:41:33 +0000 2012");
        item.put("64","64");
        item.put("185","185");
        item.put("249","249");
        item.put("true","true");
        item.put("517417816","517417816");
        item.put("en","en");
        item.put("3","3");
        item.put("Kurunegala","Kurunegala");
        item.put("ThiliniBhagya","ThiliniBhagya");
        item.put("1A1B1F","1A1B1F");
        item.put("http-abc.twimg.com","http://abs.twimg.com/images/themes/theme9/bg.gif");
        item.put("https-abc.twimg.com","https://abs.twimg.com/images/themes/theme9/bg.gif");
        item.put("http-pbs.twimg.com-profile-images","http://pbs.twimg.com/profile_images/950100192048562176/LKr7Ay21_normal.jpg");
        item.put("https-pbs.twimg.com-profile-images","https://pbs.twimg.com/profile_images/950100192048562176/LKr7Ay21_normal.jpg");
        item.put("https-pbs.twimg.com","https://pbs.twimg.com");
        item.put("https-pbs.twimg.com-profile-banners","https://pbs.twimg.com/profile_banners/517417816/1399047954");
        item.put("3E4547","3E4547");
        item.put("FFFFFF","FFFFFF");
        item.put("252429","252429");
        item.put("666666","666666");
        item.put("bhagyasl","bhagyasl");
        item.put("none","none");
        item.put("http-t.co","http://t.co/sQduiwqJiy");
        item.put("900","900");
        item.put("404NotFound","404 Not Found");
        item.put("144","144");
        item.put("no-status-found","No status found with that ID.");
        item.put("184","184");
        item.put("187","187");
        item.put("1","1");

        array.add(item);

        return item;
    }

    public static JSONObject getGHTrafficArray(){
        JSONObject item = new JSONObject();
        JSONArray array = new JSONArray();

        item.put("allow-origin-all","*");
        item.put("oauth","ETag, X-OAuth-Scopes, X-Accepted-OAuth-Scopes");
        item.put("not-exist","not-exist");
        item.put("json","application/json; charset=utf-8");
        item.put("GitHub.com","GitHub.com");
        item.put("repo","repo");
        item.put("v3-json","github.v3; format=json");
        item.put("accepted-public-repo","public_repo, repo");
        item.put("public-repo","public_repo");
        item.put("v3","https://developer.github.com/v3");
        item.put("not-found","Not Found");
        item.put("create-an-issue","https://developer.github.com/v3/issues/#create-an-issue");
        item.put("problems-passing-json","Problems parsing JSON");
        item.put("max-age","private, max-age=60");
        item.put("accept","Accept, Authorization, Cookie");
        item.put("false","false");
        item.put("avatar-v3","https://avatars.githubusercontent.com/u/101568?v=3");
        item.put("events-privacy","https://api.github.com/users/cgdecker/events{/privacy}");
        item.put("followers-cgdecker","https://api.github.com/users/cgdecker/followers");
        item.put("following-cgdecker","https://api.github.com/users/cgdecker/following{/other_user}");
        item.put("gists-cgdecker","https://api.github.com/users/cgdecker/gists{/gist_id}");
        item.put("html-cgdecker","https://github.com/cgdecker");
        item.put("101568","101568");
        item.put("organizations-cgdecker","https://api.github.com/users/cgdecker/orgs");
        item.put("events-cgdecker","https://api.github.com/users/cgdecker/received_events");
        item.put("repos-cgdecker","https://api.github.com/users/cgdecker/repos");
        item.put("starred-cgdecker","https://api.github.com/users/cgdecker/starred{/owner}{/repo}");
        item.put("subscriptions-cgdecker","https://api.github.com/users/cgdecker/subscriptions");
        item.put("User","User");
        item.put("creator-cgdecker","https://api.github.com/users/cgdecker");
        item.put("cgdecker","cgdecker");
        item.put("null","null");
        item.put("0","0");
        item.put("closed","closed");
        item.put("open","open");
        item.put("lock-an-issue","https://developer.github.com/v3/issues/#lock-an-issue");
        item.put("requires-authentication","Requires authentication");
        item.put("edit-an-issue","https://developer.github.com/v3/issues/#edit-an-issue");
        item.put("internal-server-error","Internal Server Error");
        item.put("1","1");
        item.put("unlock-an-issue","https://developer.github.com/v3/issues/#unlock-an-issue");
        item.put("invalid-request","Invalid request");
        item.put("avatar-v2","https://avatars.githubusercontent.com/u/101568?v=2");
        item.put("2","2");
        item.put("avatar-7139661","https://avatars.githubusercontent.com/u/7139661?v=3");
        item.put("events-rgabbard-bbn","https://api.github.com/users/rgabbard-bbn/events{/privacy}");
        item.put("followers-rgabbard-bbn","https://api.github.com/users/rgabbard-bbn/followers");
        item.put("following-rgabbard-bbn","https://api.github.com/users/rgabbard-bbn/following{/other_user}");
        item.put("gists-rgabbard-bbn","https://api.github.com/users/rgabbard-bbn/gists{/gist_id}");
        item.put("html-rgabbard-bbn","https://github.com/rgabbard-bbn");
        item.put("7139661","7139661");
        item.put("organizations-rgabbard-bbn","https://api.github.com/users/rgabbard-bbn/orgs");
        item.put("received-events-cpovirk-rgabbard-bbn","https://api.github.com/users/rgabbard-bbn/received_events");
        item.put("repos-rgabbard-bbn","https://api.github.com/users/rgabbard-bbn/repos");
        item.put("starred-rgabbard-bbn","https://api.github.com/users/rgabbard-bbn/starred{/owner}{/repo}");
        item.put("subscriptions-rgabbard-bbn","https://api.github.com/users/rgabbard-bbn/subscriptions");
        item.put("rgabbard-bbn","https://api.github.com/users/rgabbard-bbn");
        item.put("avatar-1703908","https://avatars.githubusercontent.com/u/1703908?v=3");
        item.put("events-cpovirk","https://api.github.com/users/cpovirk/events{/privacy}");
        item.put("followers-cpovirk","https://api.github.com/users/cpovirk/followers");
        item.put("gists-cpovirk","https://api.github.com/users/cpovirk/gists{/gist_id}");
        item.put("html-cpovirk","https://github.com/cpovirk");
        item.put("1703908","1703908");
        item.put("ThiliniBhagya","ThiliniBhagya");
        item.put("organizations-cpovirk","https://api.github.com/users/cpovirk/orgs");
        item.put("received-events-cpovirk","https://api.github.com/users/cpovirk/received_events");
        item.put("repos-cpovirk","https://api.github.com/users/cpovirk/repos");
        item.put("starred-cpovirk","https://api.github.com/users/cpovirk/starred{/owner}{/repo}");
        item.put("subscriptions-cpovirk","https://api.github.com/users/cpovirk/subscriptions");
        item.put("cpovirk","https://api.github.com/users/cpovirk");
        item.put("avatar-544569","https://avatars.githubusercontent.com/u/544569?v=3");
        item.put("events-lowasser","https://api.github.com/users/lowasser/events{/privacy}");
        item.put("followers-lowasser","https://api.github.com/users/lowasser/followers");
        item.put("following-lowasser","https://api.github.com/users/lowasser/following{/other_user}");
        item.put("gists-lowasser","https://api.github.com/users/lowasser/gists{/gist_id}");
        item.put("html-lowasser","https://github.com/lowasser");
        item.put("544569","544569");
        item.put("organizations-lowasser","https://api.github.com/users/lowasser/orgs");
        item.put("received-events-cpovirk-lowasser","https://api.github.com/users/lowasser/received_events");
        item.put("repos-lowasser","https://api.github.com/users/lowasser/repos");
        item.put("starred-lowasser","https://api.github.com/users/lowasser/starred{/owner}{/repo}");
        item.put("subscriptions-lowasser","https://api.github.com/users/lowasser/subscriptions");
        item.put("lowasser","https://api.github.com/users/lowasser");

        array.add(item);

        return item;
    }

    // OWL CLASS LISTS

    public static List<OWLClass> getGoogleOwlClassList(OWLDataFactory factory, OWLOntology ontology, OWLOntologyManager manager, IRI IOR, String featureType) throws OWLOntologyCreationException {

        List<OWLClass> RequestMethodOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> RequestHeaderOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> RequestUriOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> ResponseHeaderOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> ResponseStatusCodeOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> ResponseBodyOwlClassList = new ArrayList<OWLClass>();

        RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestMethod_POST")));
        RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestMethod_GET")));
        RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestMethod_PATCH")));
        RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestMethod_DELETE")));

        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasRequestPayload_true")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasRequestPayload_false")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasValidRequestPayload_true")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasValidRequestPayload_false")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasAuthorisationToken_true")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Connection_keep-alive")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestHeader_Content-Type_application/json'")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Type_not-exist")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Host_www.googleapis.com")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestHeader_User-Agent_Apache-HttpClient/4.5.5 (Java/1.8.0_131)'")));

        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Accept-Ranges_none")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Accept-Ranges_not-exist")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Alt-Svc_'quic=:443; ma=2592000; v=44,43,39,35''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Cache-Control_'no-cache, no-store, max-age=0, must-revalidate''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Cache-Control_'private, max-age=0, must-revalidate, no-transform''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Cache-Control_'private, max-age=0''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Content-Type_application/json; charset=UTF-8'")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Content-Type_not-exist")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Pragma_no-cache")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Pragma_not-exist")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Server_GSE")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Transfer-Encoding_chunked")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Transfer-Encoding_not-exist")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Vary_'Origin,Accept-Encoding''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Vary_X-Origin")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-Content-Type-Options_nosniff")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-Content-Type-Options_not-exist")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-Frame-Options_SAMEORIGIN")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-Frame-Options_not-exist")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_X-XSS-Protection_1; mode=block'")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-XSS-Protection_not-exist")));

        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_200")));
        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_204")));
        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_404")));
        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_503")));

        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.code_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.code_404")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.code_503")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.errors.domain_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.errors.domain_global")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.errors.message_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.errors.message_NotFound")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.errors.message_BackendError")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.errors.reason_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.errors.reason_notFound")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.errors.reason_backendError")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.message_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.message_NotFound")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error.message_BackendError")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_kind_tasks#taskList")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_kind_not-exist")));

        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriSchema_https")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriHost_www.googleapis.com")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken1_tasks")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken2_v1")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken3_users")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken4_@me")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken5_lists")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriQueryToken1_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriQueryToken2_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriQueryToken3_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriQueryToken4_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriFragmentToken1_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriFragmentToken2_not-exist")));

        switch (featureType){
            case "RequestMethod":
                return RequestMethodOwlClassList;
            case "RequestHeader":
                return RequestHeaderOwlClassList;
            case "ResponseHeader":
                return ResponseHeaderOwlClassList;
            case "ResponseStatusCode":
                return ResponseStatusCodeOwlClassList;
            case "ResponseBody":
                return ResponseBodyOwlClassList;
            case "RequestURI":
                return RequestUriOwlClassList;
            default:{
            }
        }

        return null;
    }

    public static List<OWLClass> getSlackOwlClassList(OWLDataFactory factory, OWLOntology ontology, OWLOntologyManager manager, IRI IOR, String featureType) throws OWLOntologyCreationException {

        List<OWLClass> RequestMethodOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> RequestHeaderOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> RequestUriOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> ResponseHeaderOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> ResponseStatusCodeOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> ResponseBodyOwlClassList = new ArrayList<OWLClass>();

        RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestMethod_POST")));

        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasRequestPayload_false")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasValidRequestPayload_false")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasAuthorisationToken_true")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Connection_keep-alive")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestHeader_Content-Type_application/x-www-form-urlencoded'")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Type_not-exist")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Host_slack.com")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestHeader_User-Agent_Apache-HttpClient/4.5.5 (Java/1.8.0_131)'")));

        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriSchema_https")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriHost_slack.com")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken1_api")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken2_chat.update")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken2_chat.delete")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken2_chat.postMessage")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken3_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken4_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken5_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken6_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestUriQueryToken1_token=xoxp-415149719555-416618363702-422804431936-b30a935a430030bec72e399b896b0bcc'")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestUriQueryToken2_channel=CCGRWTRKQ'")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriFragmentToken1_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriFragmentToken2_not-exist")));

        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Access-Control-Allow-Origin_*")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Cache-Control_'private, no-cache, no-store, must-revalidate''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Connection_keep-alive")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Content-Type_application/json; charset=utf-8'")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Expires_'Mon, 26 Jul 1997 05:00:00 GMT''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Pragma_no-cache")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Referrer-Policy_no-referrer")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Server_Apache")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Strict-Transport-Security_max-age=31536000; includeSubDomains; preload'")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Transfer-Encoding_chunked")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Vary_Accept-Encoding")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-Accepted-OAuth-Scopes_chat:write:user")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_X-Cache_Miss from cloudfront'")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-Content-Type-Options_nosniff")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_X-OAuth-Scopes_'identify,channels:write,chat:write:user''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-Slack-Backend_h")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-Slack-Exp_1")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-XSS-Protection_0")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-slack-router_p")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-slack-router_not-exist")));

        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_200")));

        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_channel_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_channel_CCGRWTRKQ")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error_messagenotfound")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_error_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message.bot_id_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message.bot_id_BCEPNCQDN")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message.edited.user_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message.edited.user_UC8J6APLN")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message.type_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message.type_message")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message.user_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message.user_UC8J6APLN")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_ok_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_ok_true")));

        switch (featureType){
            case "RequestMethod":
                return RequestMethodOwlClassList;
            case "RequestHeader":
                return RequestHeaderOwlClassList;
            case "ResponseHeader":
                return ResponseHeaderOwlClassList;
            case "ResponseStatusCode":
                return ResponseStatusCodeOwlClassList;
            case "ResponseBody":
                return ResponseBodyOwlClassList;
            case "RequestURI":
                return RequestUriOwlClassList;
            default:{
            }
        }

        return null;
    }

    public static List<OWLClass> getTwitterOwlClassList(OWLDataFactory factory, OWLOntology ontology, OWLOntologyManager manager, IRI IOR, String featureType) throws OWLOntologyCreationException {

        List<OWLClass> RequestMethodOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> RequestHeaderOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> RequestUriOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> ResponseHeaderOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> ResponseStatusCodeOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> ResponseBodyOwlClassList = new ArrayList<OWLClass>();

        RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestMethod_POST")));
        RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestMethod_GET")));

        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasRequestPayload_false")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasValidRequestPayload_false")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasAuthorisationToken_true")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Connection_keep-alive")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestHeader_Content-Type_application/x-www-form-urlencoded'")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Type_not-exist")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Host_api.twitter.com")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestHeader_User-Agent_Apache-HttpClient/4.5.5 (Java/1.8.0_131)'")));

        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_cache-control_'no-cache, no-store, must-revalidate, pre-check=0, post-check=0''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_content-disposition_attachment; filename=json.json'")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_content-type_application/json;charset=utf-8'")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_expires_'Tue, 31 Mar 1981 05:00:00 GMT''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_pragma_no-cache")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_server_tsa_l")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_status_200 OK'")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_status_404 Not Found'")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_strict-transport-security_max-age=631138519'")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-content-type-options_nosniff")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-frame-options_SAMEORIGIN")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-rate-limit-limit_not-exist")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-rate-limit-limit_900")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-twitter-response-tags_BouncerCompliant")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_x-xss-protection_1; mode=block; report=https://twitter.com/i/xss_report'")));

        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_200")));
        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_404")));

        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_contributors_null")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_contributors_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_coordinates_null")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_coordinates_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.hashtags_[]")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.hashtags_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.symbols_[]")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.symbols_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.urls_[]")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.urls_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.user_mentions_[]")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.user_mentions_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_errors.code_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_errors.code_144")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_errors.message_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_errors.message_No status found with that ID.'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_favorite_count_0")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_favorite_count_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_favorited_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_favorited_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_geo_null")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_geo_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_screen_name_null")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_screen_name_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_status_id_null")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_status_id_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_status_id_str_null")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_status_id_str_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_user_id_null")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_user_id_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_user_id_str_null")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_user_id_str_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_is_quote_status_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_is_quote_status_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_place_null")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_place_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_retweet_count_0")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_retweet_count_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_retweeted_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_retweeted_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_truncated_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_truncated_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.contributors_enabled_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.contributors_enabled_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_user.created_at_Wed Mar 07 09:41:33 +0000 2012'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.created_at_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.default_profile_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.default_profile_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.default_profile_image_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.default_profile_image_not-exist")));
        // ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.description_")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.description_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.favourites_count_64")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.favourites_count_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.follow_request_sent_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.follow_request_sent_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.followers_count_185")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.followers_count_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.following_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.following_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.friends_count_249")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.friends_count_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.geo_enabled_true")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.geo_enabled_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.has_extended_profile_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.has_extended_profile_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.id_517417816")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.id_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.id_str_517417816")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.id_str_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.is_translation_enabled_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.is_translation_enabled_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.is_translator_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.is_translator_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.lang_en")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.lang_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.listed_count_3")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.listed_count_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.location_Kurunegala")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.location_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_user.name_Thilini Bhagya'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.name_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.notifications_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.notifications_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_color_1A1B1F")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_color_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_user.profile_background_image_url_http://abs.twimg.com/images/themes/theme9/bg.gif'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_image_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_user.profile_background_image_url_https_https://abs.twimg.com/images/themes/theme9/bg.gif'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_image_url_https_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_tile_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_tile_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_user.profile_banner_url_https://pbs.twimg.com/profile_banners/517417816/1399047954'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_banner_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_user.profile_image_url_http://pbs.twimg.com/profile_images/950100192048562176/LKr7Ay21_normal.jpg'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_image_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_user.profile_image_url_https_https://pbs.twimg.com/profile_images/950100192048562176/LKr7Ay21_normal.jpg'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_image_url_https_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_link_color_3E4547")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_link_color_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_sidebar_border_color_FFFFFF")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_sidebar_border_color_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_sidebar_fill_color_252429")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_sidebar_fill_color_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_text_color_666666")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_text_color_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_use_background_image_true")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_use_background_image_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.protected_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.protected_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.screen_name_bhagyasl")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.screen_name_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.time_zone_null")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.time_zone_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.translator_type_none")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.translator_type_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_user.url_http://t.co/sQduiwqJiy'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.utc_offset_null")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.utc_offset_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.verified_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.verified_not-exist")));

        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriSchema_https")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriHost_api.twitter.com")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken1_1.1")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken2_statuses")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken3_update.json")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken3_show.json")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken3_destroy")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken5_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken6_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriQueryToken2_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriQueryToken3_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriQueryToken4_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriFragmentToken1_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriFragmentToken2_not-exist")));

        switch (featureType){
            case "RequestMethod":
                return RequestMethodOwlClassList;
            case "RequestHeader":
                return RequestHeaderOwlClassList;
            case "ResponseHeader":
                return ResponseHeaderOwlClassList;
            case "ResponseStatusCode":
                return ResponseStatusCodeOwlClassList;
            case "ResponseBody":
                return ResponseBodyOwlClassList;
            case "RequestURI":
                return RequestUriOwlClassList;
            default:{
            }
        }

        return null;
    }

    public static List<OWLClass> getGHTrafficOwlClassList(OWLDataFactory factory, OWLOntology ontology, OWLOntologyManager manager, IRI IOR, String featureType) throws OWLOntologyCreationException {

        List<OWLClass> RequestBodyOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> RequestMethodOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> RequestHeaderOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> RequestUriOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> ResponseHeaderOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> ResponseStatusCodeOwlClassList = new ArrayList<OWLClass>();
        List<OWLClass> ResponseBodyOwlClassList = new ArrayList<OWLClass>();

        RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestMethod_HEAD")));
        RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestMethod_DELETE")));
        RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestMethod_POST")));
        RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestMethod_GET")));
        RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestMethod_PUT")));
        RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestMethod_PATCH")));

        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasRequestPayload_false")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasRequestPayload_true")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasValidRequestPayload_false")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasValidRequestPayload_true")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasAuthorisationToken_true")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#HasAuthorisationToken_false")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestHeader_Accept_*/*'")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Type_not-exist")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestHeader_Content-Type_application/json; charset=utf-8'")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Host_api.github.com")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestHeader_User-Agent_Opera/9.50 (J2ME/MIDP; Opera Mini/4.1.11320/534; U; en)'")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestHeader_User-Agent_'Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3''")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestHeader_User-Agent_Opera/9.27 (Windows NT 5.1; U; en)'")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestHeader_User-Agent_Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9) Gecko/2008052906 Firefox/3.0'")));
        RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#RequestHeader_User-Agent_Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 1.0.3705)'")));

        RequestBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestBody_state_not-exist")));
        RequestBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestBody_state_closed")));

        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Access-Control-Allow-Origin_*")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Access-Control-Expose-Headers_'ETag, X-OAuth-Scopes, X-Accepted-OAuth-Scopes''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Cache-Control_not-exist")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Cache-Control_'private, max-age=60''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Content-Type_application/json; charset=utf-8'")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Server_GitHub.com")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Vary_not-exist")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_Vary_'Accept, Authorization, Cookie''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-Accepted-OAuth-Scopes_repo")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-Accepted-OAuth-Scopes_not-exist")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_X-Accepted-OAuth-Scopes_'public_repo, repo''")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseHeader_X-GitHub-Media-Type_github.v3; format=json'")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-OAuth-Scopes_public_repo")));
        ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-OAuth-Scopes_not-exist")));

        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignee.gravatar_id_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignee.site_admin_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignee.site_admin_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignee.type_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignee.type_User")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignees.gravatar_id_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignees.site_admin_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignees.site_admin_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignees.type_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignees.type_User")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.avatar_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.avatar_url_https://avatars.githubusercontent.com/u/7139661?v=3'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.avatar_url_https://avatars.githubusercontent.com/u/1703908?v=3'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.avatar_url_https://avatars.githubusercontent.com/u/544569?v=3'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.events_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.events_url_https://api.github.com/users/rgabbard-bbn/events{/privacy}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.events_url_https://api.github.com/users/cpovirk/events{/privacy}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.events_url_https://api.github.com/users/lowasser/events{/privacy}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.followers_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.followers_url_https://api.github.com/users/rgabbard-bbn/followers'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.followers_url_https://api.github.com/users/cpovirk/followers'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.followers_url_https://api.github.com/users/lowasser/followers'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.following_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.following_url_https://api.github.com/users/rgabbard-bbn/following{/other_user}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.following_url_https://api.github.com/users/cpovirk/following{/other_user}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.following_url_https://api.github.com/users/lowasser/following{/other_user}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.gists_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.gists_url_https://api.github.com/users/rgabbard-bbn/gists{/gist_id}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.gists_url_https://api.github.com/users/cpovirk/gists{/gist_id}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.gists_url_https://api.github.com/users/lowasser/gists{/gist_id}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.gravatar_id_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.html_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.html_url_https://github.com/rgabbard-bbn'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.html_url_https://github.com/cpovirk'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.html_url_https://github.com/lowasser'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.id_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.id_7139661")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.id_1703908")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.id_544569")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.login_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.login_rgabbard-bbn")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.login_cpovirk")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.login_lowasser")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.organizations_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.organizations_url_https://api.github.com/users/rgabbard-bbn/orgs'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.organizations_url_https://api.github.com/users/cpovirk/orgs'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.organizations_url_https://api.github.com/users/lowasser/orgs'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.received_events_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.received_events_url_https://api.github.com/users/rgabbard-bbn/received_events'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.received_events_url_https://api.github.com/users/cpovirk/received_events'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.received_events_url_https://api.github.com/users/lowasser/received_events'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.repos_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.repos_url_https://api.github.com/users/rgabbard-bbn/repos'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.repos_url_https://api.github.com/users/cpovirk/repos'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.repos_url_https://api.github.com/users/lowasser/repos'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.site_admin_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.site_admin_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.starred_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.starred_url_https://api.github.com/users/rgabbard-bbn/starred{/owner}{/repo}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.starred_url_https://api.github.com/users/cpovirk/starred{/owner}{/repo}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.starred_url_https://api.github.com/users/lowasser/starred{/owner}{/repo}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.subscriptions_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.subscriptions_url_https://api.github.com/users/rgabbard-bbn/subscriptions'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.subscriptions_url_https://api.github.com/users/cpovirk/subscriptions'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.subscriptions_url_https://api.github.com/users/lowasser/subscriptions'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.type_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.type_User")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.url_https://api.github.com/users/rgabbard-bbn'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.url_https://api.github.com/users/cpovirk'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_closed_by.url_https://api.github.com/users/lowasser'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_documentation_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_documentation_url_https://developer.github.com/v3'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_documentation_url_https://developer.github.com/v3/issues/create-an-issue'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_documentation_url_https://developer.github.com/v3/issues/edit-an-issue'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_documentation_url_https://developer.github.com/v3/issues/unlock-an-issue'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_documentation_url_https://developer.github.com/v3/issues/lock-an-issue'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_locked_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_locked_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_locked_true")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_message_Not Found'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_message_Problems parsing JSON'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_message_Invalid request'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_message_Requires authentication'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_message_Internal Server Error'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.avatar_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_milestone.creator.avatar_url_https://avatars.githubusercontent.com/u/101568?v=3'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_milestone.creator.avatar_url_https://avatars.githubusercontent.com/u/101568?v=2'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.events_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_milestone.creator.events_url_https://api.github.com/users/cgdecker/events{/privacy}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.followers_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_milestone.creator.followers_url_https://api.github.com/users/cgdecker/followers'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.following_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_milestone.creator.following_url_https://api.github.com/users/cgdecker/following{/other_user}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.gists_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_milestone.creator.gists_url_https://api.github.com/users/cgdecker/gists{/gist_id}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.gravatar_id_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.html_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_milestone.creator.html_url_https://github.com/cgdecker'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.id_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.id_101568")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.login_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.login_cgdecker")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.organizations_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_milestone.creator.organizations_url_https://api.github.com/users/cgdecker/orgs'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.received_events_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_milestone.creator.received_events_url_https://api.github.com/users/cgdecker/received_events'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.repos_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_milestone.creator.repos_url_https://api.github.com/users/cgdecker/repos'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.site_admin_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.site_admin_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.starred_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_milestone.creator.starred_url_https://api.github.com/users/cgdecker/starred{/owner}{/repo}'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.subscriptions_url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_milestone.creator.subscriptions_url_https://api.github.com/users/cgdecker/subscriptions'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.type_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.type_User")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.url_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "'#ResponseBody_milestone.creator.url_https://api.github.com/users/cgdecker'")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.description_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.due_on_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.due_on_null")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.open_issues_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.open_issues_1")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.open_issues_0")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.open_issues_2")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.state_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.state_closed")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.state_open")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_state_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_state_open")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_state_closed")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.gravatar_id_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.site_admin_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.site_admin_false")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.type_not-exist")));
        ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.type_User")));

        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_404")));
        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_400")));
        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_201")));
        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_200")));
        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_422")));
        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_401")));
        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_204")));
        ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseStatusCode_500")));

        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriSchema_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriHost_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken1_repos")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken2_google")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken3_guava")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken4_issues")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken6_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken6_lock")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriQueryToken1_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriQueryToken2_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriQueryToken3_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriQueryToken4_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriFragmentToken1_not-exist")));
        RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriFragmentToken2_not-exist")));

        switch (featureType){
            case "RequestMethod":
                return RequestMethodOwlClassList;
            case "RequestBody":
                return RequestBodyOwlClassList;
            case "RequestHeader":
                return RequestHeaderOwlClassList;
            case "ResponseHeader":
                return ResponseHeaderOwlClassList;
            case "ResponseStatusCode":
                return ResponseStatusCodeOwlClassList;
            case "ResponseBody":
                return ResponseBodyOwlClassList;
            case "RequestURI":
                return RequestUriOwlClassList;
            default:{
            }
        }

        return null;
    }

    // DISJOINT OWL CLASS LIST

    public static List<OWLClass> getTwitterDisjointOwlClassList(OWLDataFactory factory, OWLOntology ontology, OWLOntologyManager manager, IRI IOR, String featureType) throws OWLOntologyCreationException {

        List<OWLClass> owlClassToDisjointRequestHeaderList = new ArrayList<OWLClass>();
        List<OWLClass> owlClassToDisjointRequestUriList = new ArrayList<OWLClass>();
        List<OWLClass> owlClassToDisjointResponseHeaderList = new ArrayList<OWLClass>();
        List<OWLClass> owlClassToDisjointResponseBodyList = new ArrayList<OWLClass>();

        owlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Type_form-urlencoded")));
        owlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Type_not-exist")));
        owlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Length_23")));
        owlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Length_17")));
        owlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Length_21")));
        owlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Length_0")));
        owlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Length_20")));
        owlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Length_not-exist")));
        owlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Length_24")));
        owlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Length_25")));
        owlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Length_19")));
        owlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Length_26")));

        owlClassToDisjointRequestUriList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken3_update.json")));
        owlClassToDisjointRequestUriList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken3_destroy")));
        owlClassToDisjointRequestUriList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken3_show.json")));

        owlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_status_200OK")));
        owlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-rate-limit-limit_not-exist")));
        owlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_status_404NotFound")));
        owlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-rate-limit-limit_900")));
        owlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-rate-limit-remaining_not-exist")));
        owlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-rate-limit-reset_not-exist")));
        owlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-rate-limit-remaining_897")));
        owlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-rate-limit-reset_1528767779")));
        owlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_x-rate-limit-remaining_895")));

        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_contributors_null")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_coordinates_null")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.hashtags_empty-list")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.symbols_empty-list")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.urls_empty-list")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.user_mentions_empty-list")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_errors.code_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_errors.message_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_favorite_count_0")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_favorited_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_geo_null")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_screen_name_null")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_status_id_null")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_status_id_str_null")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_user_id_null")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_user_id_str_null")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_is_quote_status_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_place_null")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_retweet_count_0")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_retweeted_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_truncated_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.contributors_enabled_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.created_at_WedMar0709")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.default_profile_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.default_profile_image_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.favourites_count_64")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.follow_request_sent_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.followers_count_184")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.following_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.friends_count_249")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.geo_enabled_true")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.has_extended_profile_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.id_517417816")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.id_str_517417816")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.is_translation_enabled_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.is_translator_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.lang_en")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.listed_count_3")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.location_Kurunegala")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.name_ThiliniBhagya")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.notifications_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_color_1A1B1F")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_image_url_http-abc.twimg.com")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_image_url_https_https-abc.twimg.com")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_tile_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_banner_url_https-pbs.twimg.com-profile-banners")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_image_url_http-pbs.twimg.com-profile-images")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_image_url_https_https-pbs.twimg.com-profile-images")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_link_color_3E4547")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_sidebar_border_color_FFFFFF")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_sidebar_fill_color_252429")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_text_color_666666")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_use_background_image_true")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.protected_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.screen_name_bhagyasl")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.time_zone_null")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.translator_type_none")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.url_http-t.co")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.utc_offset_null")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.verified_false")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.followers_count_185")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_contributors_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_coordinates_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.hashtags_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.symbols_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.urls_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_entities.user_mentions_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_errors.code_144")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_errors.message_no-status-found")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_favorite_count_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_favorited_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_geo_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_screen_name_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_status_id_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_status_id_str_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_user_id_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_in_reply_to_user_id_str_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_is_quote_status_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_place_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_retweet_count_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_retweeted_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_truncated_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.contributors_enabled_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.created_at_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.default_profile_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.default_profile_image_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.description_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.favourites_count_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.follow_request_sent_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.followers_count_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.following_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.friends_count_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.geo_enabled_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.has_extended_profile_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.id_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.id_str_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.is_translation_enabled_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.is_translator_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.lang_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.listed_count_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.location_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.name_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.notifications_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_color_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_image_url_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_image_url_https_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_background_tile_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_banner_url_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_image_url_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_image_url_https_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_link_color_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_sidebar_border_color_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_sidebar_fill_color_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_text_color_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.profile_use_background_image_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.protected_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.screen_name_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.time_zone_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.translator_type_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.url_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.utc_offset_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.verified_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.followers_count_187")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_favorite_count_1")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_created_at_Mon Jun 11 21:54:37 +0000 2018")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_created_at_Mon Jun 11 21:54:40 +0000 2018")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_created_at_Mon Jun 11 21:54:36 +0000 2018")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_created_at_not-exist")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_created_at_Mon Jun 11 21:54:35 +0000 2018")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_created_at_Mon Jun 11 21:54:38 +0000 2018")));
        owlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_created_at_Mon Jun 11 21:54:39 +0000 2018")));

        switch (featureType){
            case "RequestHeader":
                return owlClassToDisjointRequestHeaderList;
            case "ResponseHeader":
                return owlClassToDisjointResponseHeaderList;
            case "ResponseBody":
                return owlClassToDisjointResponseBodyList;
            case "RequestURI":
                return owlClassToDisjointRequestUriList;
            default:{
            }
        }

        return null;
    }

    public static List<OWLClass> getGHTrafficDisjointOwlClassList(OWLDataFactory factory, OWLOntology ontology, OWLOntologyManager manager, IRI IOR, String featureType) throws OWLOntologyCreationException {

        List<OWLClass> OwlClassToDisjointRequestHeaderList = new ArrayList<OWLClass>();
        List<OWLClass> OwlClassToDisjointRequestUriList = new ArrayList<OWLClass>();
        List<OWLClass> OwlClassToDisjointResponseHeaderList = new ArrayList<OWLClass>();
        List<OWLClass> OwlClassToDisjointResponseBodyList = new ArrayList<OWLClass>();

        OwlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_HasRequestPayload_true")));
        OwlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_HasValidRequestPayload_true")));
        OwlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_HasAuthorisationToken_true")));
        OwlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Type_json")));
        OwlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_User-Agent_mozilla4-winNT5")));
        OwlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_HasRequestPayload_false")));
        OwlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_HasValidRequestPayload_false")));
        OwlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_Content-Type_not-exist")));
        OwlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_User-Agent_opera-winNT5")));
        OwlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_User-Agent_mozilla5-winNT5")));
        OwlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_User-Agent_mozilla5-Mac")));
        OwlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_HasAuthorisationToken_false")));
        OwlClassToDisjointRequestHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#RequestHeader_User-Agent_opera-mini")));

        OwlClassToDisjointRequestUriList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken6_not-exist")));
        OwlClassToDisjointRequestUriList.add(factory.getOWLClass(IRI.create(IOR + "#RequestUriPathToken6_lock")));

        OwlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Cache-Control_not-exist")));
        OwlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Vary_not-exist")));
        OwlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-Accepted-OAuth-Scopes_repo")));
        OwlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-OAuth-Scopes_public-repo")));
        OwlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-Accepted-OAuth-Scopes_not-exist")));
        OwlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-OAuth-Scopes_not-exist")));
        OwlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Cache-Control_max-age")));
        OwlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_Vary_accept")));
        OwlClassToDisjointResponseHeaderList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseHeader_X-Accepted-OAuth-Scopes_accepted-public-repo")));

        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignee.site_admin_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignee.type_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignees.site_admin_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignees.type_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.avatar_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.events_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.followers_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.following_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.gists_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.html_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.id_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.login_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.organizations_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.received_events_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.repos_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.site_admin_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.starred_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.subscriptions_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.type_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_documentation_url_v3")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_locked_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message_not-found")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.avatar_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.events_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.followers_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.following_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.gists_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.html_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.id_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.login_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.organizations_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.received_events_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.repos_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.site_admin_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.starred_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.subscriptions_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.type_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.due_on_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.open_issues_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.state_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_state_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.site_admin_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.type_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_documentation_url_create-an-issue")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message_problems-passing-json")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_documentation_url_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message_not-exist")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_locked_false")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.avatar_url_avatar-v3")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.events_url_events-privacy")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.followers_url_followers-cgdecker")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.following_url_following-cgdecker")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.gists_url_gists-cgdecker")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.html_url_html-cgdecker")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.id_101568")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.login_cgdecker")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.organizations_url_organizations-cgdecker")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.received_events_url_events-cgdecker")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.repos_url_repos-cgdecker")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.site_admin_false")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.starred_url_starred-cgdecker")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.subscriptions_url_subscriptions-cgdecker")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.type_User")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.url_cgdecker")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.due_on_null")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.open_issues_0")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.state_closed")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_state_open")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.site_admin_false")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_user.type_User")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_state_closed")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_documentation_url_lock-an-issue")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message_requires-authentication")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_documentation_url_unlock-an-issue")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_documentation_url_edit-an-issue")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignee.site_admin_false")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignee.type_User")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignees.site_admin_false")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_assignees.type_User")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message_invalid-request")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_message_internal-server-error")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.open_issues_1")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.creator.avatar_url_avatar-v2")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.open_issues_2")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_milestone.state_open")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.avatar_url_avatar-7139661")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.events_url_events-rgabbard-bbn")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.followers_url_followers-rgabbard-bbn")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.following_url_following-rgabbard-bbn")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.gists_url_gists-rgabbard-bbn")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.html_url_html-rgabbard-bbn")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.id_7139661")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.login_rgabbard-bbn")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.organizations_url_organizations-rgabbard-bbn")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.received_events_url_received-events-cpovirk-rgabbard-bbn")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.repos_url_repos-rgabbard-bbn")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.site_admin_false")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.starred_url_starred-rgabbard-bbn")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.subscriptions_url_subscriptions-rgabbard-bbn")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.type_User")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.url_rgabbard-bbn")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.avatar_url_avatar-1703908")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.events_url_events-cpovirk")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.followers_url_followers-cpovirk")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.following_url_cpovirk")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.gists_url_gists-cpovirk")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.html_url_html-cpovirk")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.id_1703908")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.login_cpovirk")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.organizations_url_organizations-cpovirk")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.received_events_url_received-events-cpovirk")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.repos_url_repos-cpovirk")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.starred_url_starred-cpovirk")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.subscriptions_url_subscriptions-cpovirk")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.url_cpovirk")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.avatar_url_avatar-544569")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.events_url_events-lowasser")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.followers_url_followers-lowasser")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.following_url_following-lowasser")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.gists_url_gists-lowasser")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.html_url_html-lowasser")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.id_544569")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.login_lowasser")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.organizations_url_organizations-lowasser")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.received_events_url_received-events-cpovirk-lowasser")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.repos_url_repos-lowasser")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.starred_url_starred-lowasser")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.subscriptions_url_subscriptions-lowasser")));
        OwlClassToDisjointResponseBodyList.add(factory.getOWLClass(IRI.create(IOR + "#ResponseBody_closed_by.url_lowasser")));

        switch (featureType){
            case "RequestHeader":
                return OwlClassToDisjointRequestHeaderList;
            case "ResponseHeader":
                return OwlClassToDisjointResponseHeaderList;
            case "ResponseBody":
                return OwlClassToDisjointResponseBodyList;
            case "RequestURI":
                return OwlClassToDisjointRequestUriList;
            default:{
            }
        }

        return null;
    }
}
