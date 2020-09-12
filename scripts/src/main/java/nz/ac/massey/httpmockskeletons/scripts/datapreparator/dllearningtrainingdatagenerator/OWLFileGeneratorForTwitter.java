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
 * this class generates owl knowledge base
 * for Twitter dataset
 *
 * @author thilini bhagya
 */

public class OWLFileGeneratorForTwitter {
    static BufferedReader br = null;
    static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    static OWLOntology ontology;
    static OWLIndividual T = null;
    static Logger LOGGER = Logging.getLogger(OWLFileGeneratorForTwitter.class);
    static OWLClass Transaction;
    static OWLSubClassOfAxiom OWLSubClass;
    static JSONArray twitterHeaderLabelsArray = new JSONArray();
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

    public static void generateOwlFile(String owlFileName, String subdataFileName, String subTrainingFileName) throws Exception {
        try {
            //IRI stands for Internationalised Resource Identifier
            //provides link to the ontology on the web, every class, every property, every individual has one
            IRI IOR = IRI.create("http://owl.api/httptransactions.owl");

            //ask manager to create a new, empty ontology
            ontology = manager.createOntology(IOR);
            //to create ontology building blocks
            OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();

            createOwlClassLists(IOR, factory, subTrainingFileName);

            LOGGER.info("Building OWL knowledge base for Twitter data");

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

            // Create disjoint class lists
            preprocess(factory);

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

            LOGGER.info("Twitter OWL knowledge base generated");


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
        // escape special characters and notations from each OWL class name and
        // use the original class name as OWL class label when creating the OWL class

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
                RequestHeaderValue = String.valueOf(hasAuthorizationToken(mm));
            } else if (HeaderKey.equals("HasRequestPayload")) {
                RequestHeaderValue = String.valueOf(hasRequestPayload());
            } else if (HeaderKey.equals("HasValidRequestPayload")) {
                RequestHeaderValue = String.valueOf(hasValidRequestPayload());
            } else {
                RequestHeaderValue = String.valueOf(Utilities.requestHeadersTwitter(mm, HeaderKey));
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
            String responseHeaderValue = String.valueOf(Utilities.responseHeadersTwitter(mm, HeaderKey));
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
            String HeaderValueToCompare = String.valueOf(Utilities.responseBodyTwitter(mm, HeaderKey));
            if (HeaderValueToCompare.contains("_")) {
                HeaderValueToCompare = HeaderValueToCompare.replaceAll("_", "");
            }
            return HeaderValueToCompare.toUpperCase().equals(HeaderValue.toUpperCase());
        } else if (featureType == FeatureType.ResponseBody && HeaderKey.contains("errors")) {
            String error = HeaderKey.split("\\.")[0];
            String value = HeaderKey.split("\\.")[1];

            return String.valueOf(Utilities.responseBodyInsideArrayTwitter(mm, error, value)).toUpperCase().equals(HeaderValue.toUpperCase());
        } else if (featureType == FeatureType.ResponseBody && StringUtils.countMatches(HeaderKey, ".") == 1) {
            String message = HeaderKey.split("\\.")[0];
            String bot_id = HeaderKey.split("\\.")[1];

            if (String.valueOf(Utilities.responseBodyInsideTwitter(mm, message, bot_id)).equals(HeaderValue)) {
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
                if (URITokeniser.getURLScheme(mm.get(0).getURL()).equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("urihost")) {
            try {
                if (URITokeniser.getUriHost(mm.get(0).getURL()).equals(HeaderValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && HeaderKey.toLowerCase().contains("uripathtoken1")) {
            try {
                String URLCoreTokenMap = URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken1");
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
                String URLCoreTokenMap = URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken2");
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
                String URLCoreTokenMap = URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken3");
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
                String URLCoreTokenMap = URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken4");
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
                String URLCoreTokenMap = URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken5");
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
                String URLCoreTokenMap = URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken6");
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
                String URLQueryTokenMap = URITokeniser.getURLQueryTokenMap(mm.get(0).getURL()).get("queryToken1");
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
                String URLQueryTokenMap = URITokeniser.getURLQueryTokenMap(mm.get(0).getURL()).get("queryToken2");
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
                String URLQueryTokenMap = URITokeniser.getURLQueryTokenMap(mm.get(0).getURL()).get("queryToken3");
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
                String URLQueryTokenMap = URITokeniser.getURLQueryTokenMap(mm.get(0).getURL()).get("queryToken4");
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
                String FragmentMap = URITokeniser.getFragmentMap(mm.get(0).getURL()).get("fragmentToken1");
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
                String FragmentMap = URITokeniser.getFragmentMap(mm.get(0).getURL()).get("fragmentToken2");
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
            return String.valueOf(Utilities.hasAuthorizationToken(mm)).toUpperCase().equals(HeaderValue.toUpperCase());
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

    public static void createOwlClassList(IRI IOR, OWLDataFactory factory, FeatureType featureType, List<String> valueLinesList, String[] headerText) throws IOException {
        LOGGER.info("Creating OWL class list for " + featureType+ " from extracted Twitter data");

        // set index of first and last columns to loop through each feature type
        int colStart = getColumnNumber(featureType, "start");
        int colEnd = getColumnNumber(featureType, "end");

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
                            if (headerItem.contains("Content-Length")) {
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
                        case RequestAuthToken:
                            RequestAuthTokenOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
                            break;
                        default: {
                        }
                    }
                }
            }
        }
    }

    public static int getColumnNumber(FeatureType featureType, String startOrEnd) {
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
                endPoint = 44;
                break;
            case ResponseStatusCode:
                startPoint = 1;
                endPoint = 1;
                break;
            case ResponseBody:
                startPoint = 45;
                endPoint = 111;
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

    public static List<String> getDistinctFeatureValuesFromCSV(int i, String[] headerText, List<String> valueLineList) {

        String cvsSplitBy = ",(?=(?:[^\\\']*\\\'[^\\\']*\\\')*[^\\\']*$)";
        List<String> rhConnectionList = new ArrayList<>();

        String header = "";

        for (String valueLine : valueLineList) {
            header = headerText[i];
            if (header.contains(":")) {
                header = header.replace(":", "_");
            }

            String finalString = "";
            String[] valueText = valueLine.split(cvsSplitBy);

            finalString = ("#" + header + "_" + valueText[i]).toString().replace("\"", ""); // USE THIS LINE FOR ALL THE TYPES EXCEPT 'BODY'

            rhConnectionList.add(finalString);
        }

        List<String> rhConnectionFinalList = rhConnectionList.stream().distinct().collect(Collectors.toList());

        return rhConnectionFinalList;

    }

    public static void createDisjointClassList(List<HTTPTransaction> mm, OWLDataFactory factory) {
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

                    if (isFeatureExists(mm, currentKey, currentValue, FeatureType.RequestHeader)) {
                        refinedValue = refineValueTwitter(currentValue);
                        String finalString = ("#RequestHeader_" + nextKey + "_" + refinedValue).toString().replace("\"", "");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        owlClassToDisjointRequestHeaderList.add(owlClassToDisjoint);
                    }
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
                    if (isFeatureExists(mm, currentKey, currentValue, FeatureType.RequestURI)) {
                        refinedValue = refineValueTwitter(currentValue);
                        String finalString = ("#" + nextKey + "_" + refinedValue).toString().replace("''", "\'");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        owlClassToDisjointRequestUriList.add(owlClassToDisjoint);
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

                    if (isFeatureExists(mm, currentKey, currentValue, FeatureType.ResponseHeader)) {
                        refinedValue = refineValueTwitter(currentValue);

                        String finalString = ("#ResponseHeader_" + nextKey + "_" + refinedValue).toString().replace("\"", "");

                        if (!finalString.contains("content-type_json")) {
                            OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                            owlClassToDisjointResponseHeaderList.add(owlClassToDisjoint);
                        }
                    }
                }
            }
        }

        // RESPONSE BODY
        for (int i = 0; i < ResponseBodyOwlClassList.size(); i++) {
            for (int j = 0; j < ResponseBodyOwlClassList.size(); j++) {
                String currentKeyString = "";
                String currentKey = "";
                String nextKeyString = "";
                String nextKey = "";

                currentKeyString = ResponseBodyOwlClassList.get(i).toString();
                currentKey = getResponseBodyKeyValue(currentKeyString).split("~")[0];
                currentValue = getResponseBodyKeyValue(currentKeyString).split("~")[1];
                nextKeyString = ResponseBodyOwlClassList.get(j).toString();
                nextKey = getResponseBodyKeyValue(nextKeyString).split("~")[0];

                if (currentKey.equals(nextKey) && i != j) {
                    if (isFeatureExists(mm, currentKey, currentValue, FeatureType.ResponseBody)) {
                        refinedValue = refineValueTwitter(currentValue);

                        if (refinedValue.contains("#")) {
                            refinedValue = refinedValue.split("#")[1];
                        }

                        if (refinedValue.contains("_")) {
                            refinedValue = refinedValue.replace("_", "");
                        }

                        String finalString = ("#ResponseBody_" + nextKey + "_" + refinedValue).toString().replace("\"", "");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        owlClassToDisjointResponseBodyList.add(owlClassToDisjoint);
                    }
                }
            }
        }
    }

    public static void createOwlClassLists(IRI IOR, OWLDataFactory factory, String subTrainingFileName) throws IOException {
        String csvFile = "src/resources/" + subTrainingFileName + ".csv";
        BufferedReader br = null;
        String line = "";

        String cvsSplitBy = ",(?=(?:[^\\\']*\\\'[^\\\']*\\\')*[^\\\']*$)";

        br = new BufferedReader(new FileReader(csvFile));
        line = br.readLine();
        String[] headerText = line.split(cvsSplitBy);

        List<String> valueLinesList = br.lines().distinct().collect(Collectors.toList());

        createOwlClassList(IOR, factory, FeatureType.RequestMethod, valueLinesList, headerText);
        createOwlClassList(IOR, factory, FeatureType.RequestHeader, valueLinesList, headerText);
        createOwlClassList(IOR, factory, FeatureType.ResponseHeader, valueLinesList, headerText);
        createOwlClassList(IOR, factory, FeatureType.ResponseStatusCode, valueLinesList, headerText);
        createOwlClassList(IOR, factory, FeatureType.ResponseBody, valueLinesList, headerText);
        createOwlClassList(IOR, factory, FeatureType.RequestAuthToken, valueLinesList, headerText);
        createOwlClassList(IOR, factory, FeatureType.RequestURI, valueLinesList, headerText);
    }

    public static void preprocess(OWLDataFactory factory) {
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            List<String> n = new LinkedList<String>();

            for (List<HTTPTransaction> mm : m.getValue().values()) {
                n.add(mm.get(0).transaction);
                // create disjoint class lists
                createDisjointClassList(mm, factory);
            }

            // get the list of transaction ids happened lastly in each resource
            lastIdList.add(n.get(n.size() - 1));
        }
    }
}
