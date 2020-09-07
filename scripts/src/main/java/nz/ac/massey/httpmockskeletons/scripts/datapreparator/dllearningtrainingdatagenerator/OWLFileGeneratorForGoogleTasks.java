package nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import nz.ac.massey.httpmockskeletons.scripts.commons.HTTPTransaction;
import nz.ac.massey.httpmockskeletons.scripts.commons.Utilities;
import nz.ac.massey.httpmockskeletons.scripts.commons.URITokeniser;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.*;

/**
 * this class generates owl knowledge base
 * for Google Tasks dataset
 *
 * @author thilini bhagya
 */

public class OWLFileGeneratorForGoogleTasks {
    static BufferedReader br = null;
    static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    static OWLOntology ontology;
    static OWLIndividual T = null;
    static Logger LOGGER = Logging.getLogger(OWLFileGeneratorForGoogleTasks.class);
    static OWLClass Transaction;
    static OWLSubClassOfAxiom OWLSubClass;
    static JSONArray googleHeaderLabelsArray = new JSONArray();
    static List<String> lastIdList = new ArrayList<>();

    public enum FeatureType {
        RequestMethod,
        RequestHeader,
        RequestAuthToken,
        RequestURI,
        ResponseHeader,
        ResponseStatusCode,
        ResponseBody
    }

    public static List<OWLClass> RequestMethodOwlClassList = new ArrayList<>();
    public static List<OWLClass> RequestHeaderOwlClassList = new ArrayList<>();
    public static List<OWLClass> RequestUriOwlClassList = new ArrayList<>();
    public static List<OWLClass> RequestAuthTokenOwlClassList = new ArrayList<>();
    public static List<OWLClass> ResponseHeaderOwlClassList = new ArrayList<>();
    public static List<OWLClass> ResponseStatusCodeOwlClassList = new ArrayList<>();
    public static List<OWLClass> ResponseBodyOwlClassList = new ArrayList<>();

    public static List<OWLClass> owlClassToDisjointRequestHeaderList = new ArrayList<>();
    public static List<OWLClass> owlClassToDisjointResponseHeaderList = new ArrayList<>();
    public static List<OWLClass> owlClassToDisjointResponseBodyList = new ArrayList<>();

    public static void generateOwlFile(String owlFileName, String subDataFileName, String trainingFileName) throws Exception {
        try {
            //IRI stands for Internationalised Resource Identifier
            //provides link to the ontology on the web, every class, every property, every individual has one
            IRI IOR = IRI.create("http://owl.api/httptransactions.owl");

            //ask manager to create a new, empty ontology
            ontology = manager.createOntology(IOR);

            // to create ontology building blocks
            OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();

            // create owl class lists to files
            createOwlClassLists(IOR, factory, trainingFileName);

            // object properties
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

            // get the list of transaction ids happened lastly in each resource
            for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
                List<String> n = new LinkedList<>();

                for (List<HTTPTransaction> mm : m.getValue().values()) {
                    n.add(mm.get(0).transaction);
                }

                lastIdList.add(n.get(n.size() - 1));
            }

            // create disjoint class lists
            createDisjointClassList(factory);

            LOGGER.info("Building OWL knowledge base for Google Tasks data");

            for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
                List<String> n = new LinkedList<>();
                for (List<HTTPTransaction> mm : m.getValue().values()) {

                    T = factory.getOWLNamedIndividual(IRI.create(IOR + "#T" + mm.get(0).transaction));

                    // check preceding
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
                        specifyExamples(mm, factory);
                    } else {
                        specifyIndividuals(mm, factory);
                    }
                }
            }

            // save in rdf/xml format
            File fileOut = new File("src/resources/" + owlFileName + ".owl");

            manager.saveOntology(ontology, new FileOutputStream(fileOut));

            LOGGER.info("Google Tasks OWL knowledge base generated");

        } catch (OWLOntologyCreationException x) {
            LOGGER.warn(x);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException x) {
                    LOGGER.warn(x);
                }
            }
        }
    }

    public static String refineValue(String value) {
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

    public static String refineValueGoogle(String value) {
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

    public static void specifyIndividuals(List<HTTPTransaction> mm, OWLDataFactory factory) {

        IRI IOR = IRI.create("http://owl.api/httptransactions.owl");
        String refinedValue = "";

        Transaction = factory.getOWLClass(IRI.create(IOR + "#Transaction"));

        for (OWLClass owlClass : RequestMethodOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_")[0];
            String value = owlClass.toString().split("#")[1].split("_")[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestMethod)) {
                refinedValue = refineValue(value);
                String finalString = ("#" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));

                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                // add disjoint classes to ontology
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

                // add disjoint classes - end

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClassToAdd);
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
                refinedValue = refineValueGoogle(value);

                String finalString = ("#RequestHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                // add disjoint classes to ontology
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointRequestHeaderList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }

        for (OWLClass owlClass : RequestUriOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_", 2)[0];
            String value = owlClass.toString().split("#")[1].split("_", 2)[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestURI)) {
                refinedValue = refineValue(value);

                String finalString = ("#" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClassToAdd);
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

                addLabelToAnnotation(factory, value, owlClassToAdd);
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
                refinedValue = refineValueGoogle(value);

                String finalString = ("#ResponseHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                // add disjoint classes to ontology

                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointResponseHeaderList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                // add disjoint classes - end

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClassToAdd);
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

                // add disjoint classes to ontology
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
                // add disjoint classes - end

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClassToAdd);
                googleHeaderLabelsArray.add(refinedValue + ":" + value);
            }
        }

        for (OWLClass owlClass : ResponseBodyOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_", 3)[1];
            String value = owlClass.toString().split("#", 2)[1].split("_", 3)[2].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.ResponseBody)) {
                refinedValue = refineValueGoogle(value);

                if (refinedValue.contains("#")) {
                    refinedValue = refinedValue.split("#")[1];
                }

                String finalString = ("#ResponseBody_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                // add disjoint classes to ontology

                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointResponseBodyList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                // add disjoint classes - end

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClassToAdd);
                googleHeaderLabelsArray.add(refinedValue + ":" + value);
            }
        }
    }

    public static void specifyExamples(List<HTTPTransaction> mm, OWLDataFactory factory) {

        IRI IOR = IRI.create("http://owl.api/httptransactions.owl");
        String refinedValue = "";

        Transaction = factory.getOWLClass(IRI.create(IOR + "#Transaction"));

        for (OWLClass owlClass : RequestMethodOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_")[0];
            String value = owlClass.toString().split("#")[1].split("_")[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestMethod)) {
                refinedValue = refineValue(value);
                String finalString = ("#" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));

                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                // add disjoint classes to ontology
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

                // add disjoint classes - end

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClassToAdd);
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
                refinedValue = refineValueGoogle(value);

                String finalString = ("#RequestHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                // add disjoint classes to ontology
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointRequestHeaderList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }

        for (OWLClass owlClass : RequestUriOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_", 2)[0];
            String value = owlClass.toString().split("#")[1].split("_", 2)[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestURI)) {
                refinedValue = refineValue(value);

                String finalString = ("#" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClassToAdd);
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

                addLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }

    }

    public static void addLabelToAnnotation(OWLDataFactory factory, String Label, OWLClass owlClass) {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLAnnotation labelAnnotation = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(Label, "en"));
        OWLAxiom axiom = factory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnnotation);
        manager.applyChange(new AddAxiom(ontology, axiom));
    }

    public static void createDisjointClassList(OWLDataFactory factory) {
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

                    // if (isFeatureContain(mm, currentKey, currentValue, FeatureType.RequestHeader)) {
                        refinedValue = refineValueGoogle(currentValue);
                        String finalString = ("#RequestHeader_" + nextKey + "_" + refinedValue).toString().replace("\"", "");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        owlClassToDisjointRequestHeaderList.add(owlClassToDisjoint);
                    // }
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

                    // if (isFeatureContain(mm, currentKey, currentValue, FeatureType.ResponseHeader)) {
                        refinedValue = refineValueGoogle(currentValue);
                        String finalString = ("#ResponseHeader_" + nextKey + "_" + refinedValue).toString().replace("\"", "");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        owlClassToDisjointResponseHeaderList.add(owlClassToDisjoint);
                    // }
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
                    // if (isFeatureContain(mm, currentKey, currentValue, FeatureType.ResponseBody)) {
                        refinedValue = refineValueGoogle(currentValue);

                        if (refinedValue.contains("#")) {
                            refinedValue = refinedValue.split("#")[1];
                        }

                        String finalString = ("#ResponseBody_" + nextKey + "_" + refinedValue).toString().replace("\"", "");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        owlClassToDisjointResponseBodyList.add(owlClassToDisjoint);
                    // }
                }
            }
        }
    }

    public static OWLDisjointClassesAxiom disjointFeature(OWLDataFactory factory, String originalKey, List<OWLClass> owlClassToDisjointList) {
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

    public static void createOwlClassLists(IRI IOR, OWLDataFactory factory, String trainingFileName) throws IOException {
        String csvFile = "src/resources/" + trainingFileName + ".csv";
        BufferedReader br = null;
        String headerLine = "";
        String cvsSplitBy = ",(?=(?:[^\\\']*\\\'[^\\\']*\\\')*[^\\\']*$)";

        br = new BufferedReader(new FileReader(csvFile));
        headerLine = br.readLine();
        String[] headerText = headerLine.split(cvsSplitBy);

        List<String> valueLinesList = br.lines().distinct().collect(Collectors.toList());

        createClassList(IOR, factory, FeatureType.RequestMethod, valueLinesList, headerText);
        createClassList(IOR, factory, FeatureType.RequestHeader, valueLinesList, headerText);
        createClassList(IOR, factory, FeatureType.ResponseHeader, valueLinesList, headerText);
        createClassList(IOR, factory, FeatureType.ResponseStatusCode, valueLinesList, headerText);
        createClassList(IOR, factory, FeatureType.ResponseBody, valueLinesList, headerText);
        createClassList(IOR, factory, FeatureType.RequestURI, valueLinesList, headerText);
        createClassList(IOR, factory, FeatureType.RequestAuthToken, valueLinesList, headerText);
    }

    public static void createClassList(IRI IOR, OWLDataFactory factory, FeatureType featureType, List<String> valueLinesList, String[] headerText) throws IOException {
        LOGGER.info("Creating OWL class list for " + featureType+ " from extracted Google Tasks data");

        // set index of first and last columns to loop through each feature type
        int colStart = getColNumber(featureType, "start");
        int colEnd = getColNumber(featureType, "end");

        for (int i = colStart; i <= colEnd; i++) {
            List<String> distinctFeatureValueList = getDistinctFeatureValuesFromCSV(i, headerText, valueLinesList);
            for (String featureValue : distinctFeatureValueList) {

                if (distinctFeatureValueList.size() >= 10) { // we don't add classes to the list those who have 10 or more distinct values
                } else {
                    if (featureValue.indexOf(" ") >= 0 || featureValue.contains(",") || featureValue.contains(";") || featureValue.contains("=") || featureValue.contains("/")) {
                        featureValue = "\'" + featureValue + "\'";
                    }

                    switch (featureType) {
                        case RequestHeader:
                            RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + featureValue)));
                            break;
                        case ResponseHeader:
                            ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + featureValue)));
                            break;
                        case ResponseBody:
                            if (featureValue.contains("#") && StringUtils.countMatches(featureValue, "#") == 2) {
                                featureValue = "#" + featureValue.split("#", 2)[1];
                            }
                            ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + featureValue)));
                            break;
                        case RequestURI:
                            RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + featureValue)));
                            break;
                        case ResponseStatusCode:
                            ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + featureValue)));
                            break;
                        case RequestMethod:
                            RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + featureValue)));
                            break;
                        default: {
                        }
                    }
                }
            }
        }
    }

    public static int getColNumber(FeatureType featureType, String startOrEnd) {
        int startPoint;
        int endPoint;

        switch (featureType) {
            case RequestMethod:
                startPoint = 0;
                endPoint = 0;
                break;
            case RequestHeader:
                startPoint = 16;
                endPoint = 23;
                break;
            case ResponseHeader:
                startPoint = 24;
                endPoint = 38;
                break;
            case ResponseStatusCode:
                startPoint = 1;
                endPoint = 1;
                break;
            case ResponseBody:
                startPoint = 39;
                endPoint = 49;
                break;
            case RequestURI:
                startPoint = 2;
                endPoint = 15;
                break;
            default:
                return 0;
        }

        if (startOrEnd == "start")
            return startPoint;
        return endPoint;
    }

    public static List<String> getDistinctFeatureValuesFromCSV(int i, String[] headerText, List<String> valueLinesList) {

        List<String> rhConnectionList = new ArrayList<>();
        String cvsSplitBy = ",(?=(?:[^\\\']*\\\'[^\\\']*\\\')*[^\\\']*$)";

        String header = "";

        for (String valueLine : valueLinesList) {
            header = headerText[i];
            if (header.contains(":")) {
                header = header.replace(":", "_");
            }
            String[] valueText = valueLine.split(cvsSplitBy);
            String finalString = "";

            finalString = ("#" + header + "_" + valueText[i]).toString().replace("\"", "");
            rhConnectionList.add(finalString);
        }

        List<String> rhConnectionFinalList = rhConnectionList.stream().distinct().collect(Collectors.toList());

        return rhConnectionFinalList;
    }

}
