package nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import nz.ac.massey.httpmockskeletons.scripts.commons.HTTPTransaction;
import nz.ac.massey.httpmockskeletons.scripts.commons.HeaderLabel;
import nz.ac.massey.httpmockskeletons.scripts.commons.Utilities;
import nz.ac.massey.httpmockskeletons.scripts.commons.URITokeniser;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.*;
import static nz.ac.massey.httpmockskeletons.scripts.commons.HeaderLabel.*;

/**
 * this class allows to check what are the attributes and their indexes
 * for selecting which attribute to learn and which to remove
 */

public class OWLFileGeneratorForGoogleTasks {
    static BufferedReader br = null;
    //create a central object to manage the ontology
    static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    static OWLOntology ontology;
    static OWLIndividual T = null;
    static Logger LOGGER = Logging.getLogger(OWLFileGeneratorForGoogleTasks.class);
    static OWLClass Transaction;
    static OWLSubClassOfAxiom OWLSubClass;
    static JSONArray googleHeaderLabelsArray = new JSONArray();

    public enum FeatureType {
        RequestMethod,
        RequestHeader,
        RequestAuthToken,
        RequestURI,
        ResponseHeader,
        ResponseStatusCode,
        ResponseBody
    }

    public static List<OWLClass> RequestMethodOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> RequestHeaderOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> RequestUriOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> RequestAuthTokenOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> ResponseHeaderOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> ResponseStatusCodeOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> ResponseBodyOwlClassList = new ArrayList<OWLClass>();

    public static List<OWLClass> owlClassToDisjointRequestHeaderList = new ArrayList<OWLClass>();
    public static List<OWLClass> owlClassToDisjointResponseHeaderList = new ArrayList<OWLClass>();
    public static List<OWLClass> owlClassToDisjointResponseBodyList = new ArrayList<OWLClass>();

    public static void generateOwlFile(String owlFileName, String subDataFileName) throws Exception {
        LOGGER.info("Generating OWL file");

        try {
            //IRI stands for Internationalised Resource Identifier
            //provides link to the ontology on the web, every class, every property, every individual has one
            IRI IOR = IRI.create("http://owl.api/httptransactions.owl");

            //ask manager to create a new, empty ontology
            ontology = manager.createOntology(IOR);

            //to create ontology building blocks
            OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();

            RequestMethodOwlClassList = getGoogleOwlClassList(factory, ontology, manager, IOR, "RequestMethod");
            RequestHeaderOwlClassList = getGoogleOwlClassList(factory, ontology, manager, IOR, "RequestHeader");
            ResponseHeaderOwlClassList = getGoogleOwlClassList(factory, ontology, manager, IOR, "ResponseHeader");
            ResponseStatusCodeOwlClassList = getGoogleOwlClassList(factory, ontology, manager, IOR, "ResponseStatusCode");
            ResponseBodyOwlClassList = getGoogleOwlClassList(factory, ontology, manager, IOR, "ResponseBody");
            RequestUriOwlClassList = getGoogleOwlClassList(factory, ontology, manager, IOR, "RequestURI");

            // OBJECT PROPERTIES
            OWLObjectProperty isPrecededBy = factory.getOWLObjectProperty((IRI.create(IOR + "#isPrecededBy")));
            OWLTransitiveObjectPropertyAxiom transitive = factory.getOWLTransitiveObjectPropertyAxiom(isPrecededBy);
            AddAxiom addAxiomChangeTransitive = new AddAxiom(ontology, transitive);
            manager.applyChange(addAxiomChangeTransitive);

            OWLObjectProperty hasPrevious = factory.getOWLObjectProperty((IRI.create(IOR + "#hasPrevious")));
            OWLSubPropertyAxiom hasPrevious_sub_isPrecededBy = factory.getOWLSubObjectPropertyOfAxiom(hasPrevious, isPrecededBy);
            AddAxiom addAxiomChangehasPrevious_sub_isPrecededBy = new AddAxiom(ontology, hasPrevious_sub_isPrecededBy);
            manager.applyChange(addAxiomChangehasPrevious_sub_isPrecededBy);

            OWLFunctionalObjectPropertyAxiom hasPrevious_functional = factory.getOWLFunctionalObjectPropertyAxiom(hasPrevious);
            AddAxiom addAxiomChangehasPrevious_functional = new AddAxiom(ontology, hasPrevious_functional);
            manager.applyChange(addAxiomChangehasPrevious_functional);

            OWLAsymmetricObjectPropertyAxiom hasPrevious_asymmetric = factory.getOWLAsymmetricObjectPropertyAxiom(hasPrevious);
            AddAxiom addAxiomChangehasPrevious_asymmetric = new AddAxiom(ontology, hasPrevious_asymmetric);
            manager.applyChange(addAxiomChangehasPrevious_asymmetric);

            OWLIrreflexiveObjectPropertyAxiom hasPrevious_irreflexive = factory.getOWLIrreflexiveObjectPropertyAxiom(hasPrevious);
            AddAxiom addAxiomChangehasPrevious_irreflexive = new AddAxiom(ontology, hasPrevious_irreflexive);
            manager.applyChange(addAxiomChangehasPrevious_irreflexive);

            HTTPTransaction.read("src/resources/" + subDataFileName + ".csv");

            List<String> lastIdList = new ArrayList<>();

            // CREATE LIST OF LAST ID'S OF EACH TRANSACTION
            for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
                List<String> n = new LinkedList<String>();

                for (List<HTTPTransaction> mm : m.getValue().values()) {
                    n.add(mm.get(0).transaction);
                }

                lastIdList.add(n.get(n.size() - 1));
            }

            // CREATE DISJOINT CLASS LISTS
            for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
                List<String> n = new LinkedList<String>();
                for (List<HTTPTransaction> mm : m.getValue().values()) {
                    CreateDisjointClassList(mm, factory);
                }
            }

            //
            for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
                List<String> n = new LinkedList<String>();
                for (List<HTTPTransaction> mm : m.getValue().values()) {

                    T = factory.getOWLNamedIndividual(IRI.create(IOR + "#T" + mm.get(0).transaction));

                    // CHECK PRECEDING
                    n.add(mm.get(0).transaction);
                    if (n.size() > 1) {
                        int a = n.size() - 2;

                        OWLIndividual TT = factory.getOWLNamedIndividual(IRI.create(IOR + "#T" + n.get(a)));
                        OWLObjectPropertyAssertionAxiom ax = factory.getOWLObjectPropertyAssertionAxiom(isPrecededBy, T, TT);
                        OWLObjectPropertyAssertionAxiom ax1 = factory.getOWLObjectPropertyAssertionAxiom(hasPrevious, T, TT);

                        AddAxiom addAxiomChange = new AddAxiom(ontology, ax);
                        AddAxiom addAxiomChange1 = new AddAxiom(ontology, ax1);

                        manager.applyChange(addAxiomChange);
                        manager.applyChange(addAxiomChange1);
                    }

                    if (lastIdList.contains(mm.get(0).transaction)) {
                        SpecifyExamples(mm, factory);
                    } else {
                        SpecifyIndividuals(mm, factory);
                    }
                }
            }

            // WriteToJson(); // has already created. don't populate again!

            // SAVE IN RDF/XML FORMAT
            File fileOut = new File("src/resources/" + owlFileName + ".owl");

            manager.saveOntology(ontology, new FileOutputStream(fileOut));

        } catch (OWLOntologyCreationException x) {
            LOGGER.warn("Exception writing details to log ", x);
        } finally {

            if (br != null) {
                try {
                    br.close();
                } catch (IOException x) {
                    LOGGER.warn("Exception writing details to log ", x);
                }
            }
        }
    }

    public static String RefineValue(String value) {
        if (value.contains(";") || value.contains("=") || value.contains(" ") || value.contains("/") || value.contains("@")) {
            value = "\'" + value + "\'";
            value = value.replace(" ", "");

            if (value.contains("/")) {
                int valueCount = value.split("/").length;
                if (value.contains("\'")) {
                    value = "\'" + value.replace("\'", "") + "\'";
                }
            }
        }

        return value;
    }

    public static String RefineValueGoogle(String value) {
        if (value.contains("application/json")) {
            value = "json";
        }

        if (value.contains("Apache-HttpClient")) {
            value = "Apache-HttpClient";
        }

        if (value.contains("quic=:443")) {
            value = "quic";
        }

        if (value.contains("no-cache, no-store, max-age=0, must-revalidate")) {
            value = "no-cache-must-revalidate";
        } else if (value.contains("no-cache")) {
            value = "no-cache";
        }

        if (value.contains("private, max-age=0, must-revalidate, no-transform")) {
            value = "must-revalidate";
        }

        if (value.contains("private, max-age=0")) {
            value = "private";
        }

        if (value.contains("Accept-Encoding")) {
            value = "origin";
        }

        if (value.contains("1; mode=block")) {
            value = "block";
        }

        if (value.contains("taskList")) {
            value = "taskList";
        }

        return value;
    }

    public static boolean isFeatureContain(List<HTTPTransaction> mm, String HeaderKey, String HeaderValue, FeatureType featureType) {
        if (featureType == FeatureType.RequestHeader) {
            String RequestHeaderValue = "";
            if (HeaderKey.equals("HasAuthorisationToken")) {
                RequestHeaderValue = String.valueOf(HasAuthorizationToken(mm));
            } else if (HeaderKey.equals("HasRequestPayload")) {
                RequestHeaderValue = String.valueOf(HasRequestPayloadGoogle(String.valueOf(mm.get(0).getMethod()), String.valueOf(mm.get(0).getCode())));
            } else if (HeaderKey.equals("HasValidRequestPayload")) {
                RequestHeaderValue = String.valueOf(HasValidRequestPayloadGoogle(String.valueOf(mm.get(0).getMethod()), String.valueOf(mm.get(0).getCode())));
            } else {
                RequestHeaderValue = String.valueOf(Utilities.RequestHeaderGoogle(mm, HeaderKey));
            }

            if (RequestHeaderValue.contains("\'")) {
                RequestHeaderValue = RequestHeaderValue.replaceAll("\'", "");
            }
            if (HeaderValue.contains(",")) {
                HeaderValue = HeaderValue.replaceAll(",", "");
            }

            return RequestHeaderValue.equals(HeaderValue);
        } else if (featureType == FeatureType.ResponseHeader) {
            String responseHeaderValue = String.valueOf(Utilities.ResponseHeaderGoogle(mm, HeaderKey));
            if (responseHeaderValue.contains("\'")) {
                responseHeaderValue = responseHeaderValue.replaceAll("\'", "");
            }
//            if (HeaderValue.contains(",")) {
//                HeaderValue = HeaderValue.replaceAll(",", "");
//            }

            return responseHeaderValue.equals(HeaderValue);
        } else if (featureType == FeatureType.ResponseBody && !HeaderKey.contains(".")) {
            return String.valueOf(Utilities.ResponseBodyGoogle(mm, HeaderKey, null, null)).toUpperCase().equals(HeaderValue.toUpperCase());
        } else if (featureType == FeatureType.ResponseBody && StringUtils.countMatches(HeaderKey, ".") == 1) {
            String message = HeaderKey.split("\\.")[0];
            String bot_id = HeaderKey.split("\\.")[1];

            if (String.valueOf(Utilities.ResponseBodyGoogle(mm, message, bot_id, null)).equals(HeaderValue)) {
                return true;
            }
        } else if (featureType == FeatureType.ResponseBody && StringUtils.countMatches(HeaderKey, ".") == 2) {
            String message = HeaderKey.split("\\.")[0];
            String edited = HeaderKey.split("\\.")[1];
            String user = HeaderKey.split("\\.")[2];

            if (String.valueOf(Utilities.ResponseBodyGoogle(mm, message, edited, user)).equals(HeaderValue)) {
                return true;
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.contains("RequestUriSchema")) {
            try {
                if (URITokeniser.GetURLScheme(mm.get(0).getURL()).equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.contains("RequestUriHost")) {
            try {
                if (URITokeniser.GetUriHost(mm.get(0).getURL()).equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("requesturipathtoken1")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken1");
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("requesturipathtoken2")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken2");
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("requesturipathtoken3")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken3");
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("requesturipathtoken4")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken4");
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("requesturipathtoken5")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken5");
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("requesturiquerytoken1")) {
            try {
                String URLQueryTokenMap = URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken1");
                if (URLQueryTokenMap == null) {
                    URLQueryTokenMap = "not-exist";
                }
                if (URLQueryTokenMap.equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("requesturiquerytoken2")) {
            try {
                String URLQueryTokenMap = URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken2");
                if (URLQueryTokenMap == null) {
                    URLQueryTokenMap = "not-exist";
                }
                if (URLQueryTokenMap.equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("requesturiquerytoken3")) {
            try {
                String URLQueryTokenMap = URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken3");
                if (URLQueryTokenMap == null) {
                    URLQueryTokenMap = "not-exist";
                }
                if (URLQueryTokenMap.equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("requesturiquerytoken4")) {
            try {
                String URLQueryTokenMap = URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken4");
                if (URLQueryTokenMap == null) {
                    URLQueryTokenMap = "not-exist";
                }
                if (URLQueryTokenMap.equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("requesturifragmenttoken1")) {
            try {
                String FragmentMap = URITokeniser.GetFragmentMap(mm.get(0).getURL()).get("fragmentToken1");
                if (FragmentMap == null) {
                    FragmentMap = "not-exist";
                }
                if (FragmentMap.equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("requesturifragmenttoken2")) {
            try {
                String FragmentMap = URITokeniser.GetFragmentMap(mm.get(0).getURL()).get("fragmentToken2");
                if (FragmentMap == null) {
                    FragmentMap = "not-exist";
                }
                if (FragmentMap.equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestMethod) {
            return mm.get(0).getMethod().equals(HeaderValue);
        } else if (featureType == FeatureType.ResponseStatusCode) {
            return mm.get(0).getCode().equals(HeaderValue);
        }

        return false;
    }

    public static void SpecifyIndividuals(List<HTTPTransaction> mm, OWLDataFactory factory) {

        IRI IOR = IRI.create("http://owl.api/httptransactions.owl");
        String refinedValue = "";
        String refinedValueForLabel = "";

        Transaction = factory.getOWLClass(IRI.create(IOR + "#Transaction"));

        for (OWLClass owlClass : RequestMethodOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_")[0];
            String value = owlClass.toString().split("#")[1].split("_")[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestMethod)) {
                refinedValue = RefineValue(value);
                String finalString = ("#" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));

                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                /////// DISJOINT
                String firstKey = RequestMethodOwlClassList.get(0).toString().split("#")[1].split("_")[0];
                Set<OWLClass> c1 = new HashSet<OWLClass>();

                for (OWLClass owlClass1 : RequestMethodOwlClassList) {
                    if (firstKey.equals(key)) {
                        c1.add(owlClass1);
                    }
                }

                if (c1.size() > 0) {
                    OWLDisjointClassesAxiom disjointClassesAxiom = factory.getOWLDisjointClassesAxiom(c1);
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }
                /////// DISJOINT END

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                AddLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }

        for (OWLClass owlClass : RequestHeaderOwlClassList) {
            String key = "";
            String value = "";

            if (StringUtils.countMatches(owlClass.toString(), "_") == 1) {
                key = owlClass.toString().split("#")[1].split("_", 2)[0];
                value = owlClass.toString().split("#")[1].split("_", 2)[1].split(">")[0];
            } else if (StringUtils.countMatches(owlClass.toString(), "_") > 1) {
                key = owlClass.toString().split("#")[1].split("_", 3)[1];
                value = owlClass.toString().split("#")[1].split("_", 3)[2].split(">")[0];
            }

            if (StringUtils.countMatches(value, "\'") > 2) {
                value = value.split("'")[1];
            } else {
                value = value.split("'")[0];
            }

            if (value.contains("\"")) {
                value = value.replace("\"", "");
            }

            if (isFeatureContain(mm, key, value, FeatureType.RequestHeader)) {
                refinedValue = RefineValueGoogle(value);
                refinedValueForLabel = RefineValue(value);

                String finalString = ("#RequestHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //////// ADD DISJOINT CLASSES TO ONTOLOGY

                OWLDisjointClassesAxiom disjointClassesAxiom = DisjointFeature(factory, key, owlClassToDisjointRequestHeaderList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                AddLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }

        for (OWLClass owlClass : RequestUriOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_", 2)[0];
            String value = owlClass.toString().split("#")[1].split("_", 2)[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestURI)) {
                refinedValue = RefineValue(value);

                String finalString = ("#" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                AddLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }

        for (OWLClass owlClass : RequestAuthTokenOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_", 2)[0];
            String value = owlClass.toString().split("#")[1].split("_", 2)[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestAuthToken)) {

                String finalString = ("#" + key + "_" + value).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                AddLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }

        for (OWLClass owlClass : ResponseHeaderOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_", 3)[1];
            String value = owlClass.toString().split("#")[1].split("_", 3)[2].split(">")[0];

            if (StringUtils.countMatches(value, "\'") > 2) {
                value = value.split("'")[1];
            } else {
                value = value.split("'")[0];
            }

            if (value.contains("\"")) {
                value = value.replace("\"", "");
            }

            if (isFeatureContain(mm, key, value, FeatureType.ResponseHeader)) {
                refinedValue = RefineValueGoogle(value);

                String finalString = ("#ResponseHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //////// ADD DISJOINT CLASSES TO ONTOLOGY

                OWLDisjointClassesAxiom disjointClassesAxiom = DisjointFeature(factory, key, owlClassToDisjointResponseHeaderList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                AddLabelToAnnotation(factory, value, owlClassToAdd);
                googleHeaderLabelsArray.add(refinedValue + ":" + value);
            }
        }

        for (OWLClass owlClass : ResponseStatusCodeOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_")[0];
            String value = owlClass.toString().split("#")[1].split("_")[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.ResponseStatusCode)) {
                String finalString = ("#" + key + "_" + value).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                /////// DISJOINT
                String firstKey = ResponseStatusCodeOwlClassList.get(0).toString().split("#")[1].split("_")[0];
                Set<OWLClass> c1 = new HashSet<OWLClass>();

                for (OWLClass owlClass1 : ResponseStatusCodeOwlClassList) {
                    if (firstKey.equals(key)) {
                        c1.add(owlClass1);
                    }
                }

                if (c1.size() > 0) {
                    OWLDisjointClassesAxiom disjointClassesAxiom = factory.getOWLDisjointClassesAxiom(c1);
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }
                /////// DISJOINT END

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                AddLabelToAnnotation(factory, value, owlClassToAdd);
                googleHeaderLabelsArray.add(refinedValue + ":" + value);
            }
        }

        for (OWLClass owlClass : ResponseBodyOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_", 3)[1];
            String value = owlClass.toString().split("#", 2)[1].split("_", 3)[2].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.ResponseBody)) {
                refinedValue = RefineValueGoogle(value);

                if (refinedValue.contains("#")) {
                    refinedValue = refinedValue.split("#")[1];
                }

                String finalString = ("#ResponseBody_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //////// ADD DISJOINT CLASSES TO ONTOLOGY

                OWLDisjointClassesAxiom disjointClassesAxiom = DisjointFeature(factory, key, owlClassToDisjointResponseBodyList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                AddLabelToAnnotation(factory, value, owlClassToAdd);
                googleHeaderLabelsArray.add(refinedValue + ":" + value);
            }
        }
    }

    public static void SpecifyExamples(List<HTTPTransaction> mm, OWLDataFactory factory) {

        IRI IOR = IRI.create("http://owl.api/httptransactions.owl");
        String refinedValue = "";
        String refinedValueForLabel = "";

        Transaction = factory.getOWLClass(IRI.create(IOR + "#Transaction"));

        for (OWLClass owlClass : RequestMethodOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_")[0];
            String value = owlClass.toString().split("#")[1].split("_")[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestMethod)) {
                refinedValue = RefineValue(value);
                String finalString = ("#" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));

                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                /////// DISJOINT
                String firstKey = RequestMethodOwlClassList.get(0).toString().split("#")[1].split("_")[0];
                Set<OWLClass> c1 = new HashSet<OWLClass>();

                for (OWLClass owlClass1 : RequestMethodOwlClassList) {
                    if (firstKey.equals(key)) {
                        c1.add(owlClass1);
                    }
                }

                if (c1.size() > 0) {
                    OWLDisjointClassesAxiom disjointClassesAxiom = factory.getOWLDisjointClassesAxiom(c1);
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }
                /////// DISJOINT END

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                AddLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }

        for (OWLClass owlClass : RequestHeaderOwlClassList) {
            String key = "";
            String value = "";

            if (StringUtils.countMatches(owlClass.toString(), "_") == 1) {
                key = owlClass.toString().split("#")[1].split("_", 2)[0];
                value = owlClass.toString().split("#")[1].split("_", 2)[1].split(">")[0];
            } else if (StringUtils.countMatches(owlClass.toString(), "_") > 1) {
                key = owlClass.toString().split("#")[1].split("_", 3)[1];
                value = owlClass.toString().split("#")[1].split("_", 3)[2].split(">")[0];
            }

            if (StringUtils.countMatches(value, "\'") > 2) {
                value = value.split("'")[1];
            } else {
                value = value.split("'")[0];
            }

            if (value.contains("\"")) {
                value = value.replace("\"", "");
            }

            if (isFeatureContain(mm, key, value, FeatureType.RequestHeader)) {
                refinedValue = RefineValueGoogle(value);
                refinedValueForLabel = RefineValue(value);

                String finalString = ("#RequestHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //////// ADD DISJOINT CLASSES TO ONTOLOGY

                OWLDisjointClassesAxiom disjointClassesAxiom = DisjointFeature(factory, key, owlClassToDisjointRequestHeaderList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                AddLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }

        for (OWLClass owlClass : RequestUriOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_", 2)[0];
            String value = owlClass.toString().split("#")[1].split("_", 2)[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestURI)) {
                refinedValue = RefineValue(value);

                String finalString = ("#" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                AddLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }

        for (OWLClass owlClass : RequestAuthTokenOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_", 2)[0];
            String value = owlClass.toString().split("#")[1].split("_", 2)[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestAuthToken)) {

                String finalString = ("#" + key + "_" + value).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                AddLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }

    }

    public static void AddLabelToAnnotation(OWLDataFactory factory, String Label, OWLClass owlClass) {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLAnnotation labelAnnotation = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(Label, "en"));
        OWLAxiom axiom = factory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnnotation);
        manager.applyChange(new AddAxiom(ontology, axiom));
    }

    public static void CreateDisjointClassList(List<HTTPTransaction> mm, OWLDataFactory factory) {
        IRI IOR = IRI.create("http://owl.api/httptransactions.owl");
        String refinedValue = "";
        String currentValue = "";

        // REQUEST HEADER
        for (int i = 0; i < RequestHeaderOwlClassList.size(); i++) {
            for (int j = 0; j < RequestHeaderOwlClassList.size(); j++) {
                String currentKey = "";
                String nextKey = "";

                if (StringUtils.countMatches(RequestHeaderOwlClassList.get(i).toString(), "_") == 1) {
                    currentKey = RequestHeaderOwlClassList.get(i).toString().split("#")[1].split("_", 2)[0];
                    nextKey = RequestHeaderOwlClassList.get(j).toString().split("#")[1].split("_", 2)[0];
                    if (StringUtils.countMatches(RequestHeaderOwlClassList.get(j).toString(), "_") == 1) {
                        currentValue = RequestHeaderOwlClassList.get(i).toString().split("#")[1].split("_", 2)[1].split(">")[0];
                    }
                } else if (StringUtils.countMatches(RequestHeaderOwlClassList.get(i).toString(), "_") > 1) {
                    currentKey = RequestHeaderOwlClassList.get(i).toString().split("#")[1].split("_", 3)[1];
                    nextKey = RequestHeaderOwlClassList.get(j).toString().split("#")[1].split("_", 3)[1];
                    if (StringUtils.countMatches(RequestHeaderOwlClassList.get(j).toString(), "_") > 1) {
                        currentValue = RequestHeaderOwlClassList.get(i).toString().split("#")[1].split("_", 3)[2].split(">")[0];
                    }
                }

                if (currentKey.equals(nextKey) && i != j) {
                    if (StringUtils.countMatches(currentValue, "\'") > 2) {
                        currentValue = currentValue.split("'")[1];
                    } else {
                        currentValue = currentValue.split("'")[0];
                    }

                    if (currentValue.contains("\"")) {
                        currentValue = currentValue.replace("\"", "");
                    }

                    if (isFeatureContain(mm, currentKey, currentValue, FeatureType.RequestHeader)) {
                        refinedValue = RefineValueGoogle(currentValue);
                        String finalString = ("#RequestHeader_" + nextKey + "_" + refinedValue).toString().replace("\"", "");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        owlClassToDisjointRequestHeaderList.add(owlClassToDisjoint);
                    }
                }
            }
        }

        // RESPONSE HEADER
        for (int i = 0; i < ResponseHeaderOwlClassList.size(); i++) {
            for (int j = 0; j < ResponseHeaderOwlClassList.size(); j++) {
                String currentKey = ResponseHeaderOwlClassList.get(i).toString().split("#")[1].split("_", 3)[1];
                String nextKey = ResponseHeaderOwlClassList.get(j).toString().split("#")[1].split("_", 3)[1];
                currentValue = ResponseHeaderOwlClassList.get(i).toString().split("#")[1].split("_", 3)[2].split(">")[0];

                if (currentKey.equals(nextKey) && i != j) {
                    if (StringUtils.countMatches(currentValue, "\'") > 2) {
                        currentValue = currentValue.split("'")[1];
                    } else {
                        currentValue = currentValue.split("'")[0];
                    }

                    if (currentValue.contains("\"")) {
                        currentValue = currentValue.replace("\"", "");
                    }

                    if (isFeatureContain(mm, currentKey, currentValue, FeatureType.ResponseHeader)) {
                        refinedValue = RefineValueGoogle(currentValue);
                        String finalString = ("#ResponseHeader_" + nextKey + "_" + refinedValue).toString().replace("\"", "");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        owlClassToDisjointResponseHeaderList.add(owlClassToDisjoint);
                    }
                }
            }
        }

        // RESPONSE BODY
        for (int i = 0; i < ResponseBodyOwlClassList.size(); i++) {
            for (int j = 0; j < ResponseBodyOwlClassList.size(); j++) {
                String currentKey = ResponseBodyOwlClassList.get(i).toString().split("#")[1].split("_", 3)[1];
                String nextKey = ResponseBodyOwlClassList.get(j).toString().split("#")[1].split("_", 3)[1];
                currentValue = ResponseBodyOwlClassList.get(i).toString().split("#", 2)[1].split("_", 3)[2].split(">")[0];

                if (currentKey.equals(nextKey) && i != j) {
                    if (isFeatureContain(mm, currentKey, currentValue, FeatureType.ResponseBody)) {
                        refinedValue = RefineValueGoogle(currentValue);

                        if (refinedValue.contains("#")) {
                            refinedValue = refinedValue.split("#")[1];
                        }

                        String finalString = ("#ResponseBody_" + nextKey + "_" + refinedValue).toString().replace("\"", "");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        owlClassToDisjointResponseBodyList.add(owlClassToDisjoint);
                    }
                }
            }
        }
    }

    public static OWLDisjointClassesAxiom DisjointFeature(OWLDataFactory factory, String originalKey, List<OWLClass> owlClassToDisjointList) {
        Set<OWLClass> c1 = new HashSet<OWLClass>();

        for (OWLClass owlClass : owlClassToDisjointList) {
            String key = owlClass.toString().split("#")[1].split("_", 3)[1].toString();
            if (key.equals(originalKey)) {
                c1.add(owlClass);
            }
        }

        if (c1.size() > 0) {
            OWLDisjointClassesAxiom disjointClassesAxiom = factory.getOWLDisjointClassesAxiom(c1);
            return disjointClassesAxiom;
        }

        return null;
    }
}
