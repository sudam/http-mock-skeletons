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

}
