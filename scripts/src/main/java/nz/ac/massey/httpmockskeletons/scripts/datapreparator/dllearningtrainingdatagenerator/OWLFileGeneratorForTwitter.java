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

import static nz.ac.massey.httpmockskeletons.scripts.commons.HeaderLabel.*;
import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.*;

/**
 * Script to generate OWL Ontology for Twitter
 *
 * @author thilini bhagya
 */

public class OWLFileGeneratorForTwitter {
    static BufferedReader br = null;
    //create a central object to manage the ontology
    static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    static OWLOntology ontology;
    static OWLIndividual T = null;
    static Logger LOGGER = Logging.getLogger(OWLFileGeneratorForTwitter.class);
    static OWLClass Transaction;
    static OWLSubClassOfAxiom OWLSubClass;
    static JSONArray twitterHeaderLabelsArray = new JSONArray();

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
    public static List<OWLClass> owlClassToDisjointRequestUriList = new ArrayList<OWLClass>();
    public static List<OWLClass> owlClassToDisjointResponseHeaderList = new ArrayList<OWLClass>();
    public static List<OWLClass> owlClassToDisjointResponseBodyList = new ArrayList<OWLClass>();

    public static void generateOwlFile(String owlFileName, String subdataFileName) throws Exception {
        try {
            //IRI stands for Internationalised Resource Identifier
            //provides link to the ontology on the web, every class, every property, every individual has one
            IRI IOR = IRI.create("http://owl.api/httptransactions.owl");

            //ask manager to create a new, empty ontology
            ontology = manager.createOntology(IOR);
            //to create ontology building blocks
            OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();

            RequestMethodOwlClassList = getTwitterOwlClassList(factory, ontology, manager, IOR, "RequestMethod");
            RequestHeaderOwlClassList = getTwitterOwlClassList(factory, ontology, manager, IOR, "RequestHeader");
            ResponseHeaderOwlClassList = getTwitterOwlClassList(factory, ontology, manager, IOR, "ResponseHeader");
            ResponseStatusCodeOwlClassList = getTwitterOwlClassList(factory, ontology, manager, IOR, "ResponseStatusCode");
            ResponseBodyOwlClassList = getTwitterOwlClassList(factory, ontology, manager, IOR, "ResponseBody");
            RequestAuthTokenOwlClassList = getTwitterOwlClassList(factory, ontology, manager, IOR, "RequestAuthToken");
            RequestUriOwlClassList = getTwitterOwlClassList(factory, ontology, manager, IOR, "RequestURI");

            owlClassToDisjointRequestHeaderList = getTwitterDisjointOwlClassList(factory, ontology, manager, IOR, "RequestHeader");
            owlClassToDisjointRequestUriList = getTwitterDisjointOwlClassList(factory, ontology, manager, IOR, "RequestURI");
            owlClassToDisjointResponseHeaderList = getTwitterDisjointOwlClassList(factory, ontology, manager, IOR, "ResponseHeader");
            owlClassToDisjointResponseBodyList = getTwitterDisjointOwlClassList(factory, ontology, manager, IOR, "ResponseBody");

            // Object properties
            OWLObjectProperty isPrecededBy = factory.getOWLObjectProperty(IRI.create(IOR + "#isPrecededBy"));
            OWLTransitiveObjectPropertyAxiom transitive = factory.getOWLTransitiveObjectPropertyAxiom(isPrecededBy);
            AddAxiom addAxiomChangeTransitive = new AddAxiom(ontology, transitive);
            manager.applyChange(addAxiomChangeTransitive);

            OWLObjectProperty hasPrevious = factory.getOWLObjectProperty(IRI.create(IOR + "#hasPrevious"));
            OWLSubPropertyAxiom hasPrevious_sub_isPrecededBy = factory.getOWLSubObjectPropertyOfAxiom(hasPrevious, isPrecededBy);
            AddAxiom addAxiomChangeHasPreviousSubIsPrecededBy = new AddAxiom(ontology, hasPrevious_sub_isPrecededBy);
            manager.applyChange(addAxiomChangeHasPreviousSubIsPrecededBy);

            OWLFunctionalObjectPropertyAxiom hasPrevious_functional = factory.getOWLFunctionalObjectPropertyAxiom(hasPrevious);
            AddAxiom addAxiomChangeHasPreviousFunctional = new AddAxiom(ontology, hasPrevious_functional);
            manager.applyChange(addAxiomChangeHasPreviousFunctional);

            OWLAsymmetricObjectPropertyAxiom hasPrevious_asymmetric = factory.getOWLAsymmetricObjectPropertyAxiom(hasPrevious);
            AddAxiom addAxiomChangeHasPreviousAsymmetric = new AddAxiom(ontology, hasPrevious_asymmetric);
            manager.applyChange(addAxiomChangeHasPreviousAsymmetric);

            OWLIrreflexiveObjectPropertyAxiom hasPrevious_irreflexive = factory.getOWLIrreflexiveObjectPropertyAxiom(hasPrevious);
            AddAxiom addAxiomChangeHasPreviousIrreflexive = new AddAxiom(ontology, hasPrevious_irreflexive);
            manager.applyChange(addAxiomChangeHasPreviousIrreflexive);

            HTTPTransaction.read("src/resources/" + subdataFileName + ".csv");

            List<String> lastIdList = new ArrayList<>();

            for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
                List<String> n = new LinkedList<String>();
                for (List<HTTPTransaction> mm : m.getValue().values()) {
                    n.add(mm.get(0).transaction);
                }

                lastIdList.add(n.get(n.size() - 1));
            }

            for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
                List<String> n = new LinkedList<String>();
                for (List<HTTPTransaction> mm : m.getValue().values()) {
                    n.add(mm.get(0).transaction);
                    T = factory.getOWLNamedIndividual(IRI.create(IOR + "#T" + mm.get(0).transaction));

                    // CHECK PRECEDING
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

            // Save in RDF/XML format
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

    public static void specifyIndividuals(List<HTTPTransaction> mm, OWLDataFactory factory) {

        IRI IOR = IRI.create("http://owl.api/httptransactions.owl");
        String refinedValue = "";
        String refinedValueForLabel = "";

        Transaction = factory.getOWLClass(IRI.create(IOR + "#Transaction"));

        for (OWLClass owlClass : RequestMethodOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_")[0];
            String value = owlClass.toString().split("#")[1].split("_")[1].split(">")[0];

            if (isFeatureExists(mm, key, value, FeatureType.RequestMethod)) {
                refinedValue = refineValue(value);
                String finalString = ("#" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                /// DISJOINT
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
                /// DISJOINT END

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

            if (isFeatureExists(mm, key, value, FeatureType.RequestHeader)) {
                refinedValue = refineValueTwitter(value);
                refinedValueForLabel = refineValue(value);

                String finalString = ("#RequestHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                // ADD DISJOINT CLASSES TO ONTOLOGY

                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointRequestHeaderList.stream().distinct().collect(Collectors.toList()), FeatureType.RequestHeader);

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                //

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

            if (isFeatureExists(mm, key, value, FeatureType.RequestURI)) {
                refinedValue = refineValue(value);

                String finalString = ("#" + key + "_" + refinedValue).toString().replace("''", "\'");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //// ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointRequestUriList.stream().distinct().collect(Collectors.toList()), FeatureType.RequestURI);

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////

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

            if (isFeatureExists(mm, key, value, FeatureType.ResponseHeader)) {
                refinedValue = refineValueTwitter(value);
                refinedValueForLabel = refineValue(value);

                String finalString = ("#ResponseHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //// ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointResponseHeaderList.stream().distinct().collect(Collectors.toList()), FeatureType.ResponseHeader);

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClassToAdd);
                twitterHeaderLabelsArray.add(refinedValue + ":" + value);
            }
        }

        for (OWLClass owlClass : ResponseStatusCodeOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_")[0];
            String value = owlClass.toString().split("#")[1].split("_")[1].split(">")[0];

            if (isFeatureExists(mm, key, value, FeatureType.ResponseStatusCode)) {
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClass, Transaction);

                /// DISJOINT
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
                /// DISJOINT END

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClass, T);
                manager.addAxiom(ontology, classAssertion);
                addLabelToAnnotation(factory, value, owlClass);
                twitterHeaderLabelsArray.add(refinedValue + ":" + value);
            }
        }

        for (OWLClass owlClass : ResponseBodyOwlClassList) {
            String key = "";
            String value = "";

            key = getResponseBodyKeyValue(owlClass.toString()).split("~")[0];
            value = getResponseBodyKeyValue(owlClass.toString()).split("~")[1];

            if (isFeatureExists(mm, key, value, FeatureType.ResponseBody)) {
                refinedValue = refineValueTwitter(value);
                refinedValueForLabel = refineValue(value);

                if (refinedValue.contains("#")) {
                    refinedValue = refinedValue.split("#")[1];
                }

                if (refinedValue.contains("_")) {
                    refinedValue = refinedValue.replace("_", "");
                }

                String finalString = ("#ResponseBody_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);


                // ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointResponseBodyList.stream().distinct().collect(Collectors.toList()), FeatureType.ResponseBody);

                if (disjointClassesAxiom != null) {
                    if (key.contains("user.profile_banner_url") || key.contains("user.profile_image_url")) {
                    } else {
                        manager.addAxiom(ontology, disjointClassesAxiom);
                    }
                }


                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, refinedValueForLabel, owlClassToAdd);
                twitterHeaderLabelsArray.add(refinedValue + ":" + value);
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

            if (isFeatureExists(mm, key, value, FeatureType.RequestMethod)) {
                refinedValue = refineValue(value);
                String finalString = ("#" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                /// DISJOINT
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
                /// DISJOINT END

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

            if (isFeatureExists(mm, key, value, FeatureType.RequestHeader)) {
                refinedValue = refineValueTwitter(value);
                refinedValueForLabel = refineValue(value);

                String finalString = ("#RequestHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //// ADD DISJOINT CLASSES TO ONTOLOGY

                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointRequestHeaderList.stream().distinct().collect(Collectors.toList()), FeatureType.RequestHeader);

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////

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

            if (isFeatureExists(mm, key, value, FeatureType.RequestURI)) {
                refinedValue = refineValue(value);

                String finalString = ("#" + key + "_" + refinedValue).toString().replace("''", "\'");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                //// ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, owlClassToDisjointRequestUriList.stream().distinct().collect(Collectors.toList()), FeatureType.RequestURI);

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                ////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }
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

    public static String refineValueTwitter(String value) {
        if (value.contains("Apache-HttpClient")) {
            value = "Apache-HttpClient";
        }

        if (value.contains("No status found with that ID")) {
            value = "no-status-found";
        }

        if (value.contains("[]")) {
            value = "empty-list";
        }

        if (value.contains("tsa_l")) {
            value = "tsa-l";
        }

        if (value.contains("Wed Mar 07 09:41:33")) {
            value = "WedMar0709";
        }

        if (value.contains("http://abs.twimg.com/")) {
            value = "http-abc.twimg.com";
        }

        if (value.contains("https://abs.twimg.com/")) {
            value = "https-abc.twimg.com";
        }

        if (value.contains("https://pbs.twimg.com/profile_banners")) {
            value = "https-pbs.twimg.com-profile-banners";
        } else if (value.contains("https://pbs.twimg.com/profile_images")) {
            value = "https-pbs.twimg.com-profile-images";
        } else if (value.contains("https://pbs.twimg.com")) {
            value = "https-pbs.twimg.com";
        }

        if (value.contains("http://pbs.twimg.com/profile_images")) {
            value = "http-pbs.twimg.com-profile-images";
        }

        if (value.contains("http://t.co/sQduiwqJiy")) {
            value = "http-t.co";
        }

        if (value.contains("Thilini Bhagya")) {
            value = "ThiliniBhagya";
        }

        if (value.contains("no-cache, no-store, must-revalidate")) {
            value = "no-cache";
        }

        if (value.contains("attachment; filename=json.json")) {
            value = "attachment-json";
        }

        if (value.contains("application/json")) {
            value = "json";
        }

        if (value.contains("Tue, 31 Mar 1981")) {
            value = "Tue31Mar1981";
        }

        if (value.contains("max-age=631138519")) {
            value = "max-age";
        }

        if (value.contains("1; mode=block")) {
            value = "mode-block";
        }

        if (value.contains("404 Not Found")) {
            value = "404NotFound";
        }

        if (value.contains("200 OK")) {
            value = "200OK";
        }

        if (value.contains("application/x-www-form-urlencoded")) {
            value = "form-urlencoded";
        }

        return value;
    }

    public static boolean isFeatureExists(List<HTTPTransaction> mm, String HeaderKey, String HeaderValue, FeatureType featureType) {

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
                RequestHeaderValue = String.valueOf(Utilities.RequestHeadersTwitter(mm, HeaderKey));
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
            String responseHeaderValue = String.valueOf(Utilities.ResponseHeadersTwitter(mm, HeaderKey));
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
            String HeaderValueToCompare = String.valueOf(Utilities.ResponseBodyTwitter(mm, HeaderKey));
            if (HeaderValueToCompare.contains("_")) {
                HeaderValueToCompare = HeaderValueToCompare.replaceAll("_", "");
            }
            return HeaderValueToCompare.toUpperCase().equals(HeaderValue.toUpperCase());
        } else if (featureType == FeatureType.ResponseBody && HeaderKey.contains("errors")) {
            String error = HeaderKey.split("\\.")[0];
            String value = HeaderKey.split("\\.")[1];

            return String.valueOf(Utilities.ResponseBodyInsideArrayTwitter(mm, error, value)).toUpperCase().equals(HeaderValue.toUpperCase());
        } else if (featureType == FeatureType.ResponseBody && StringUtils.countMatches(HeaderKey, ".") == 1) {
            String message = HeaderKey.split("\\.")[0];
            String bot_id = HeaderKey.split("\\.")[1];

            if (String.valueOf(Utilities.ResponseBodyInsideTwitter(mm, message, bot_id)).equals(HeaderValue)) {
                return true;
            }
        } else if (featureType == FeatureType.ResponseBody && StringUtils.countMatches(HeaderKey, ".") == 2) {
            String message = HeaderKey.split("\\.")[0];
            String edited = HeaderKey.split("\\.")[1];
            String user = HeaderKey.split("\\.")[2];

//            if (String.valueOf(Utilities.bodyinsideinside(mm, message, edited, user)).equals(HeaderValue)) {
//                return true;
//            }
        }

        // REQUEST URI
        else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("urischema")) {
            try {
                if (URITokeniser.GetURLScheme(mm.get(0).getURL()).equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("urihost")) {
            try {
                if (URITokeniser.GetUriHost(mm.get(0).getURL()).equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("uripathtoken1")) {
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("uripathtoken2")) {
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("uripathtoken3")) {
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("uripathtoken4")) {
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("uripathtoken5")) {
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("uripathtoken6")) {
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
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("requesturifragmenttoken")) {
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
            return mm.get(0).getMethod().toLowerCase().equals(HeaderValue.toLowerCase());
        }

        // RESPONSE STATUS CODE
        else if (featureType == FeatureType.ResponseStatusCode) {
            return mm.get(0).getCode().equals(HeaderValue);
        }

        // REQUEST AUTH TOKEN
        else if (featureType == FeatureType.RequestAuthToken) {
            return String.valueOf(Utilities.HasAuthorizationToken(mm)).toUpperCase().equals(HeaderValue.toUpperCase());
        }

        return false;
    }

    public static void addLabelToAnnotation(OWLDataFactory factory, String Label, OWLClass owlClass) {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLAnnotation labelAnnotation = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(Label, "en"));
        OWLAxiom axiom = factory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), labelAnnotation);
        manager.applyChange(new AddAxiom(ontology, axiom));
    }

    public static OWLDisjointClassesAxiom disjointFeature(OWLDataFactory factory, String originalKey, List<OWLClass> owlClassToDisjointList, FeatureType featureType) {
        Set<OWLClass> c1 = new HashSet<OWLClass>();

        for (OWLClass owlClass : owlClassToDisjointList) {
            String key = "";

            if (owlClass.toString().contains("followers_count_186") || owlClass.toString().contains("followers_count_184") || owlClass.toString().contains("followers_count_187")) {
            } else if (owlClass.toString().contains("content-type_json")) {
            } else if (featureType.equals(FeatureType.RequestHeader) && owlClass.toString().contains("Content-Length_21")) {
            } else if (featureType.equals(FeatureType.ResponseBody) && owlClass.toString().contains("favorite_count_1")) {
            } else if (featureType == FeatureType.ResponseBody) {
                if (!owlClass.toString().contains("user.description_not-exist") && !owlClass.toString().contains("pbs.twimg.com")) {
                    key = getResponseBodyKeyValue(owlClass.toString()).split("~")[0];
                    if (key.equals(originalKey)) {
                        c1.add(owlClass);
                    }
                }
            } else {
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
        }

        if (c1.size() > 0) {
            OWLDisjointClassesAxiom disjointClassesAxiom = factory.getOWLDisjointClassesAxiom(c1);
            return disjointClassesAxiom;
        }

        return null;
    }

    public static String getResponseBodyKeyValue(String owlClass) {
        String key = "";
        String value = "";

        if (StringUtils.countMatches(owlClass.toString(), "_") == 2) {
            key = owlClass.toString().split("#")[1].replace("ResponseBody_", "").split("_", 2)[0];
            value = owlClass.toString().split("#")[1].replace("ResponseBody_", "").split("_", 2)[1];
        } else if (StringUtils.countMatches(owlClass.toString(), "_") > 2) {
            if (owlClass.toString().contains("pbs.twimg.com")) {
                if (StringUtils.countMatches(owlClass.toString().split("#")[1].replace("ResponseBody_", ""), "_") == 6) {
                    String[] bits = owlClass.toString().split("#")[1].replace("ResponseBody_", "").split("_", 5);
                    key = bits[0] + "_" + bits[1] + "_" + bits[2] + "_" + bits[3];
                    value = bits[4];
                } else if (StringUtils.countMatches(owlClass.toString().split("#")[1].replace("ResponseBody_", ""), "_") == 5) {
                    String[] bits = owlClass.toString().split("#")[1].replace("ResponseBody_", "").split("_", 4);
                    key = bits[0] + "_" + bits[1] + "_" + bits[2];
                    value = bits[3];
                } else {
                    String[] bits = owlClass.toString().split("#")[1].replace("ResponseBody_", "").split("_", 4);
                    key = bits[0] + "_" + bits[1] + "_" + bits[2];
                    value = bits[3];
                }
            } else {
                String[] valueBits = owlClass.toString().split("#")[1].replace("ResponseBody_", "").split("_");
                String[] keyBits = Arrays.copyOf(valueBits, valueBits.length - 1);
                key = String.join("_", keyBits);
                value = valueBits[valueBits.length - 1];
            }
        }

        if (String.valueOf(value).equals(">")) {
            value = "some-text";
        } else {
            // No status found with that ID.'
            value = value.split(">")[0].replace("'", "");
            value = value.replace(" '", "");
            if (!value.contains("http")) {
                value = value.replace(".", "");
            }
            if (value.contains("No status found with that ID")) {
                value = value + ".";
            }
        }

        if (key == "" && value == "") {
            key = "INVALIDKEY";
            value = "INVALIDVALUE";
        }

        return key + "~" + value;
    }
}
