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
import java.util.Map;
import java.util.TreeMap;

import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.*;

/**
 * Generate OWL Ontology for Slack
 *
 * @author Thilini Bhagya
 **/

public class OWLFileGeneratorForSlack {
    static BufferedReader br = null;
    static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    static OWLOntology ontology;
    static OWLIndividual T = null;
    static Logger LOGGER = Logging.getLogger(OWLFileGeneratorForSlack.class);
    static OWLClass Transaction;
    static OWLSubClassOfAxiom OWLSubClass;
    static JSONArray slackHeaderLabelsArray = new JSONArray();

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
    public static List<OWLClass> ResponseHeaderOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> ResponseStatusCodeOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> ResponseBodyOwlClassList = new ArrayList<OWLClass>();

    public static List<OWLClass> owlClassToDisjointRequestHeaderList = new ArrayList<OWLClass>();
    public static List<OWLClass> owlClassToDisjointRequestUriList = new ArrayList<OWLClass>();
    public static List<OWLClass> owlClassToDisjointResponseHeaderList = new ArrayList<OWLClass>();
    public static List<OWLClass> owlClassToDisjointResponseBodyList = new ArrayList<OWLClass>();

    public static void generateOwlFile(String owlFileName, String SubdataFileName, String subTrainingFileName) throws Exception {
        try {
            //IRI stands for Internationalised Resource Identifier
            //provides link to the ontology on the web, every class, every property, every individual has one
            IRI IOR = IRI.create("http://owl.api/httptransactions.owl");

            //ask manager to create a new, empty ontology
            ontology = manager.createOntology(IOR);
            //to create ontology building blocks
            OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();

            createOwlClassLists(IOR, factory, subTrainingFileName);

            LOGGER.info("Building OWL knowledge base for Slack data");

            // OBJECT PROPERTIES
            OWLObjectProperty isPrecededBy = factory.getOWLObjectProperty(IRI.create((IOR + "#isPrecededBy")));
            OWLTransitiveObjectPropertyAxiom transitive = factory.getOWLTransitiveObjectPropertyAxiom(isPrecededBy);
            AddAxiom addAxiomChangeTransitive = new AddAxiom(ontology, transitive);
            manager.applyChange(addAxiomChangeTransitive);

            OWLObjectProperty hasPrevious = factory.getOWLObjectProperty(IRI.create(IOR + "#hasPrevious"));
            OWLSubPropertyAxiom hasPrevious_sub_isPrecededBy = factory.getOWLSubObjectPropertyOfAxiom(hasPrevious, isPrecededBy);
            AddAxiom addAxiomChangeHasPrevious_sub_isPrecededBy = new AddAxiom(ontology, hasPrevious_sub_isPrecededBy);
            manager.applyChange(addAxiomChangeHasPrevious_sub_isPrecededBy);

            OWLFunctionalObjectPropertyAxiom hasPrevious_functional = factory.getOWLFunctionalObjectPropertyAxiom(hasPrevious);
            AddAxiom addAxiomChangeHasPrevious_functional = new AddAxiom(ontology, hasPrevious_functional);
            manager.applyChange(addAxiomChangeHasPrevious_functional);

            OWLAsymmetricObjectPropertyAxiom hasPrevious_asymmetric = factory.getOWLAsymmetricObjectPropertyAxiom(hasPrevious);
            AddAxiom addAxiomChangeHasPrevious_asymmetric = new AddAxiom(ontology, hasPrevious_asymmetric);
            manager.applyChange(addAxiomChangeHasPrevious_asymmetric);

            OWLIrreflexiveObjectPropertyAxiom hasPreviousIrreflexive = factory.getOWLIrreflexiveObjectPropertyAxiom(hasPrevious);
            AddAxiom addAxiomChangeHasPreviousIrreflexive = new AddAxiom(ontology, hasPreviousIrreflexive);
            manager.applyChange(addAxiomChangeHasPreviousIrreflexive);

            HTTPTransaction.read("src/resources/" + SubdataFileName + ".csv");

            List<String> lastIdList = new ArrayList<>();

            // get the list of transaction ids happened lastly in each resource
            for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
                List<String> n = new LinkedList<String>();

                for (List<HTTPTransaction> mm : m.getValue().values()) {
                    n.add(mm.get(0).transaction);
                }

                lastIdList.add(n.get(n.size() - 1));
            }

            // create disjoint class lists
            createDisjointClassList(factory);

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
                        specifyExamples(mm, factory);
                    } else if (!lastIdList.contains(mm.get(0).transaction)) {
                        specifyIndividuals(mm, factory);
                    }
                }
            }

            // save in RDF/XML format
            File fileOut = new File("src/resources/" + owlFileName + ".owl");
            manager.saveOntology(ontology, new FileOutputStream(fileOut));

            LOGGER.info("Slack OWL knowledge base generated");

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

    public static void specifyIndividuals(List<HTTPTransaction> mm, OWLDataFactory factory) {

        IRI IOR = IRI.create("http://owl.api/httptransactions.owl");
        String refinedValue = "";
        String refinedValueForLabel = "";

        Transaction = factory.getOWLClass(IRI.create(IOR + "#Transaction"));

        for (OWLClass owlClass : RequestMethodOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_")[0];
            String value = owlClass.toString().split("#")[1].split("_")[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestMethod)) {
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
                refinedValue = refineValueSlack(value);
                refinedValueForLabel = refineValue(value);

                String finalString = ("#RequestHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //////// ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointRequestHeaderList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, refinedValueForLabel, owlClassToAdd);
            }
        }

        for (OWLClass owlClass : RequestUriOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_", 2)[0];
            String value = owlClass.toString().split("#")[1].split("_", 2)[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestURI)) {
                refinedValue = refineValueSlack(value);
                refinedValueForLabel = refineValue(value);

                String finalString = ("#" + key + "_" + refinedValue).toString().replace("''", "\'");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //////// ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointRequestUriList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, refinedValueForLabel, owlClassToAdd);
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
                refinedValue = refineValueSlack(value);
                refinedValueForLabel = refineValue(value);

                String finalString = ("#ResponseHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //////// ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointResponseHeaderList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, refinedValueForLabel, owlClassToAdd);
                slackHeaderLabelsArray.add(refinedValue + ":" + value);
            }
        }

        for (OWLClass owlClass : ResponseStatusCodeOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_")[0];
            String value = owlClass.toString().split("#")[1].split("_")[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.ResponseStatusCode)) {
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClass, Transaction);

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClass, T);
                manager.addAxiom(ontology, classAssertion);
                addLabelToAnnotation(factory, value, owlClass);
            }
        }

        for (OWLClass owlClass : ResponseBodyOwlClassList) {
            String key = "";
            String value = "";

            if (StringUtils.countMatches(owlClass.toString().split("#")[1], "_") > 2) {
                key = owlClass.toString().split("#")[1].split("_", 4)[1] + "_" + owlClass.toString().split("#")[1].split("_", 4)[2];
                value = owlClass.toString().split("#", 2)[1].split("_", 4)[3].split(">")[0];
            } else {
                key = owlClass.toString().split("#")[1].split("_", 3)[1];
                value = owlClass.toString().split("#", 2)[1].split("_", 3)[2].split(">")[0];
            }

            if (isFeatureContain(mm, key, value, FeatureType.ResponseBody)) {
                refinedValue = refineValue(value);

                if (refinedValue.contains("#")) {
                    refinedValue = refinedValue.split("#")[1];
                }

                if (refinedValue.contains("_")) {
                    refinedValue = refinedValue.replace("_", "");
                }

                String finalString = ("#ResponseBody_" + key + "_" + refinedValue).toString().replace("\"", "");
                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //////// ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointResponseBodyList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }
    }

    public static void specifyExamples(List<HTTPTransaction> mm, OWLDataFactory factory) {
        IRI IOR = IRI.create("http://owl.api/httptransactions.owl");
        String refinedValue = "";
        String refinedValueForLabel = "";

        Transaction = factory.getOWLClass(IRI.create(IOR + "#Transaction"));

        for (OWLClass owlClass : RequestMethodOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_")[0];
            String value = owlClass.toString().split("#")[1].split("_")[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestMethod)) {
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
                refinedValue = refineValueSlack(value);
                refinedValueForLabel = refineValue(value);

                String finalString = ("#RequestHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //////// ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointRequestHeaderList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, refinedValueForLabel, owlClassToAdd);
            }
        }

        for (OWLClass owlClass : RequestUriOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_", 2)[0];
            String value = owlClass.toString().split("#")[1].split("_", 2)[1].split(">")[0];

            if (isFeatureContain(mm, key, value, FeatureType.RequestURI)) {
                refinedValue = refineValueSlack(value);
                refinedValueForLabel = refineValue(value);

                String finalString = ("#" + key + "_" + refinedValue).toString().replace("''", "\'");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //////// ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointRequestUriList.stream().distinct().collect(Collectors.toList()));

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, refinedValueForLabel, owlClassToAdd);
            }
        }
    }

    public static boolean isFeatureContain(List<HTTPTransaction> mm, String HeaderKey, String HeaderValue, FeatureType featureType) {

        // REQUEST HEADER
        if (featureType == FeatureType.RequestHeader) {
            String RequestHeaderValue = "";
            if (HeaderKey.equals("HasAuthorisationToken")) {
                RequestHeaderValue = String.valueOf(HasAuthorizationToken(mm));
            } else if (HeaderKey.equals("HasRequestPayload")) {
                RequestHeaderValue = String.valueOf(HasRequestPayload());
            } else if (HeaderKey.equals("HasValidRequestPayload")) {
                RequestHeaderValue = String.valueOf(HasValidRequestPayload());
            } else {
                RequestHeaderValue = String.valueOf(Utilities.RequestHeaderSlack(mm, HeaderKey));
            }

            if (RequestHeaderValue.contains("\'")) {
                RequestHeaderValue = RequestHeaderValue.replaceAll("\'", "");
            }
            if (HeaderValue.contains(",")) {
                HeaderValue = HeaderValue.replaceAll(",", "");
            }

            return RequestHeaderValue.equals(HeaderValue);
        }

        // RESPONSE HEADER
        else if (featureType == FeatureType.ResponseHeader) {
            String responseHeaderValue = String.valueOf(Utilities.ResponseHeaderSlack(mm, HeaderKey));
            if (responseHeaderValue.contains("\'")) {
                responseHeaderValue = responseHeaderValue.replaceAll("\'", "");
            }
            if (HeaderValue.contains(",")) {
                HeaderValue = HeaderValue.replaceAll(",", "");
            }

            if (responseHeaderValue.contains(",")) {
                responseHeaderValue = responseHeaderValue.replaceAll(",", "");
            }

            return responseHeaderValue.equals(HeaderValue);

        }

        // RESPONSE BODY
        else if (featureType == FeatureType.ResponseBody && !HeaderKey.contains(".")) {
            String HeaderValueToCompare = String.valueOf(Utilities.ResponseBodySlack(mm, HeaderKey, null, null));

            if (HeaderValueToCompare.contains("_")) {
                HeaderValueToCompare = HeaderValueToCompare.replaceAll("_", "");
            }

            return HeaderValueToCompare.toUpperCase().equals(HeaderValue.toUpperCase());
        } else if (featureType == FeatureType.ResponseBody && StringUtils.countMatches(HeaderKey, ".") == 1) {
            String message = HeaderKey.split("\\.")[0];
            String bot_id = HeaderKey.split("\\.")[1];

            if (String.valueOf(Utilities.ResponseBodySlack(mm, message, bot_id, null)).equals(HeaderValue)) {
                return true;
            }
        } else if (featureType == FeatureType.ResponseBody && StringUtils.countMatches(HeaderKey, ".") == 2) {
            String message = HeaderKey.split("\\.")[0];
            String edited = HeaderKey.split("\\.")[1];
            String user = HeaderKey.split("\\.")[2];

            if (String.valueOf(Utilities.ResponseBodySlack(mm, message, edited, user)).equals(HeaderValue)) {
                return true;
            }
        }

        // REQUEST URI
        else if (featureType == FeatureType.RequestURI && HeaderKey.contains("RequestUriSchema")) {
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.contains("RequestUriPathToken1")) {
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.contains("RequestUriPathToken2")) {
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.contains("RequestUriPathToken3")) {
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.contains("RequestUriPathToken4")) {
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.contains("RequestUriPathToken5")) {
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.contains("RequestUriPathToken6")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken6");
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.contains("RequestUriQueryToken1")) {
            try {
                if (HeaderValue.contains("\'")) {
                    HeaderValue = HeaderValue.replaceAll("\'", "");
                }
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.contains("RequestUriQueryToken2")) {
            try {
                String URLQueryTokenMap = URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken2");
                if (URLQueryTokenMap == null) {
                    URLQueryTokenMap = "not-exist";
                }
                if (HeaderValue.contains("\'")) {
                    HeaderValue = HeaderValue.replaceAll("\'", "");
                }
                if (URLQueryTokenMap.equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.contains("RequestUriFragmentToken1")) {
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.contains("RequestUriFragmentToken2")) {
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
        }

        // REQUEST METHOD
        else if (featureType == FeatureType.RequestMethod) {
            return mm.get(0).getMethod().equals(HeaderValue);
        }

        // RESPONSE STATUS CODE
        else if (featureType == FeatureType.ResponseStatusCode) {
            return mm.get(0).getCode().equals(HeaderValue);
        }

        // REQUEST AUTH TOKEN
        else if (featureType == FeatureType.RequestAuthToken) {
            return String.valueOf(Utilities.HasAuthorizationToken(mm)).toLowerCase().equals(HeaderValue);
        }

        return false;
    }

    public static String refineValue(String value) {
        if (value.contains(";") || value.contains("=") || value.contains(" ") || value.contains("/") || value.contains("@")) {
            value = "\'" + value + "\'";
            value = value.replace(" ", "");

            if (value.contains("/")) {
                int valueCount = value.split("/").length;
                // value = valueCount > 1? value.split("/",2)[1] : value.split("/")[1];
                if (value.contains("\'")) {
                    value = "\'" + value.replace("\'", "") + "\'";
                }
            }
        }

        return value;
    }

    public static String refineValueSlack(String value) {
        if (value.contains("application/x-www")) {
            value = "x-www";
        }
        if (value.contains("Apache-HttpClient")) {
            value = "Apache-HttpClient";
        }
        if (value.contains("private, no-cache, no-store, must-revalidate")) {
            value = "private";
        }
        if (value.contains("application/json")) {
            value = "json";
        }
        if (value.contains("max-age=31536000; includeSubDomains")) {
            value = "max-age";
        }
        if (value.contains("channels:write,chat:write:user")) {
            value = "chat";
        } else if (value.contains("chat:write:user")) {
            value = "accepted-chat";
        }
        if (value.contains("token=xoxp")) {
            value = "xoxp";
        }
        if (value.contains("channel=CCGRWTRKQ")) {
            value = "channel";
        }
        if (value.contains("Mon, 26 Jul 1997 05:00:00 GMT")) {
            value = "26Jul1997";
        }
        if (value.contains("Miss from cloudfront")) {
            value = "cloudfront";
        }

        return value;
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
                }
                if (StringUtils.countMatches(RequestHeaderOwlClassList.get(i).toString(), "_") > 1) {
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
                        refinedValue = refineValueSlack(currentValue);
                        String finalString = ("#RequestHeader_" + nextKey + "_" + refinedValue).toString().replace("\"", "");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        owlClassToDisjointRequestHeaderList.add(owlClassToDisjoint);
                    // }
                }
            }
        }

        // REQUEST URI
        for (int i = 0; i < RequestUriOwlClassList.size(); i++) {
            for (int j = 0; j < RequestUriOwlClassList.size(); j++) {
                String currentKey = "";
                String nextKey = "";

                currentKey = RequestUriOwlClassList.get(i).toString().split("#")[1].split("_", 2)[0];
                nextKey = RequestUriOwlClassList.get(j).toString().split("#")[1].split("_", 2)[0];
                currentValue = RequestUriOwlClassList.get(i).toString().split("#")[1].split("_", 2)[1].split(">")[0];

                if (currentKey.equals(nextKey) && i != j) {
                    // if (isFeatureContain(mm, currentKey, currentValue, FeatureType.RequestURI)) {
                        refinedValue = refineValueSlack(currentValue);
                        String finalString = ("#" + currentKey + "_" + refinedValue).toString().replace("''", "\'");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        owlClassToDisjointRequestUriList.add(owlClassToDisjoint);
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
                        refinedValue = refineValueSlack(currentValue);
                        String finalString = ("#ResponseHeader_" + nextKey + "_" + refinedValue).toString().replace("\"", "");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        owlClassToDisjointResponseHeaderList.add(owlClassToDisjoint);
                    //}
                }
            }
        }

        // RESPONSE BODY
        for (int i = 0; i < ResponseBodyOwlClassList.size(); i++) {
            for (int j = 0; j < ResponseBodyOwlClassList.size(); j++) {

                String currentKey = "";
                String nextKey = "";

                if (StringUtils.countMatches(ResponseBodyOwlClassList.get(i).toString().split("#")[1], "_") > 2) {
                    currentKey = ResponseBodyOwlClassList.get(i).toString().split("#")[1].split("_", 4)[1] + "_" + ResponseBodyOwlClassList.get(i).toString().split("#")[1].split("_", 4)[2];
                    ;
                    nextKey = ResponseBodyOwlClassList.get(j).toString().split("#")[1].split("_", 4)[1] + "_" + ResponseBodyOwlClassList.get(j).toString().split("#")[1].split("_", 4)[2];
                    ;
                    currentValue = ResponseBodyOwlClassList.get(i).toString().split("#", 2)[1].split("_", 4)[3].split(">")[0];
                } else {
                    currentKey = ResponseBodyOwlClassList.get(i).toString().split("#")[1].split("_", 3)[1];
                    nextKey = ResponseBodyOwlClassList.get(j).toString().split("#")[1].split("_", 3)[1];
                    currentValue = ResponseBodyOwlClassList.get(i).toString().split("#", 2)[1].split("_", 3)[2].split(">")[0];
                }

                if (currentKey.equals(nextKey) && i != j) {
                    // if (isFeatureContain(mm, currentKey, currentValue, FeatureType.ResponseBody)) {
                        refinedValue = refineValueSlack(currentValue);

                        if (refinedValue.contains("#")) {
                            refinedValue = refinedValue.split("#")[1];
                        }

                        if (refinedValue.contains("_")) {
                            refinedValue = refinedValue.replace("_", "");
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
            String key = "";
            if (StringUtils.countMatches(owlClass.toString(), "_") > 2) {
                key = owlClass.toString().split("#")[1].split("_", 4)[1] + "_" + owlClass.toString().split("#")[1].split("_", 4)[2];
                if (key.equals(originalKey)) {
                    c1.add(owlClass);
                }
            } else if (owlClass.toString().contains("RequestUriPathToken") && StringUtils.countMatches(owlClass.toString(), "_") == 1) {
                key = owlClass.toString().split("#")[1].split("_", 2)[0].toString();
                if (key.equals(originalKey)) {
                    c1.add(owlClass);
                }
            } else {
                key = owlClass.toString().split("#")[1].split("_", 3)[1].toString();
                if (key.equals(originalKey)) {
                    c1.add(owlClass);
                }
            }
        }

        if (c1.size() > 0) {
            OWLDisjointClassesAxiom disjointClassesAxiom = factory.getOWLDisjointClassesAxiom(c1);
            return disjointClassesAxiom;
        }

        return null;
    }

    public static void createOWLClassList(IRI IOR, OWLDataFactory factory, FeatureType featureType, List<String> valueLinesList, String[] headerText) throws IOException {
        LOGGER.info("Creating OWL class list for " + featureType+ " from extracted Slack data");

        // set index of first and last columns to loop through each feature type
        int colStart = getColNumber(featureType, "start");
        int colEnd = getColNumber(featureType, "end");

        for (int i = colStart; i <= colEnd; i++) {
            List<String> distinctFeatureValueList = getDistinctFeatureValuesFromCSV(i, headerText, valueLinesList);
            for (String headerItem : distinctFeatureValueList) {

                if (distinctFeatureValueList.size() >= 10) { // don't add classes to the list those who have 10 or more distinct values
                } else {
                    if (headerItem.indexOf(" ") >= 0 || headerItem.contains(",") || headerItem.contains(";") || headerItem.contains("=") || headerItem.contains("/")) {
                        headerItem = "\'" + headerItem + "\'";
                    }

                    switch (featureType) {
                        case RequestHeader:
                            if(headerItem.contains("Content-Length")){
                            } else {
                                RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
                            }
                            break;
                        case ResponseHeader:
                            ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
                            break;
                        case ResponseBody:
                            if (headerItem.contains("#") && StringUtils.countMatches(headerItem, "#") == 2) {
                                headerItem = "#" + headerItem.split("#", 2)[1];
                            }
                            ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
                            break;
                        case RequestURI:
                            RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
                            break;
                        case ResponseStatusCode:
                            ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
                            break;
                        case RequestMethod:
                            RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
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
                endPoint = 47;
                break;
            case ResponseStatusCode:
                startPoint = 1;
                endPoint = 1;
                break;
            case ResponseBody:
                startPoint = 48;
                endPoint = 59;
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

    public static List<String> getDistinctFeatureValuesFromCSV(int i, String[] headerText, List<String> valueLinesList) throws IOException {

        String cvsSplitBy = ",(?=(?:[^\\\']*\\\'[^\\\']*\\\')*[^\\\']*$)";
        List<String> rhConnectionList = new ArrayList<>();

        String header = "";

        for (String valueLine : valueLinesList) {
            header = headerText[i];
            // new - replace ':' with '_'
            if (header.contains(":")) {
                header = header.replace(":", "_");
            }
            String[] valueText = valueLine.split(cvsSplitBy);
            String finalString = "";

            // to remove "_" from value part
            if (valueText[i].contains("message_not_found")) {
                valueText[i] = valueText[i].replaceAll("_", "");
            }

            finalString = ("#" + header + "_" + valueText[i]).toString().replace("\"", "");

            rhConnectionList.add(finalString);
        }

        List<String> rhConnectionFinalList = rhConnectionList.stream().distinct().collect(Collectors.toList());

        return rhConnectionFinalList;
    }

    public static void createOwlClassLists(IRI IOR, OWLDataFactory factory, String subTrainingFileName) throws IOException {
        String csvFile = "src/resources/"+ subTrainingFileName + ".csv";
        BufferedReader br = null;
        String line = "";

        String cvsSplitBy = ",(?=(?:[^\\\']*\\\'[^\\\']*\\\')*[^\\\']*$)";

        br = new BufferedReader(new FileReader(csvFile));
        line = br.readLine();
        String[] headerText = line.split(cvsSplitBy);

        List<String> valueLinesList = br.lines().distinct().collect(Collectors.toList());

        createOWLClassList(IOR, factory, FeatureType.RequestMethod, valueLinesList, headerText);
        createOWLClassList(IOR, factory, FeatureType.RequestHeader, valueLinesList, headerText);
        createOWLClassList(IOR, factory, FeatureType.ResponseHeader, valueLinesList, headerText);
        createOWLClassList(IOR, factory, FeatureType.ResponseStatusCode, valueLinesList, headerText);
        createOWLClassList(IOR, factory, FeatureType.ResponseBody, valueLinesList, headerText);
        createOWLClassList(IOR, factory, FeatureType.RequestURI, valueLinesList, headerText);
    }
}
