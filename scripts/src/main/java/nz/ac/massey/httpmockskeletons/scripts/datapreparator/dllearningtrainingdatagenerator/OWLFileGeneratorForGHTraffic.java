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
 * Generate OWL Ontology for GHTraffic
 *
 * @author Thilini Bhagya
 **/

public class OWLFileGeneratorForGHTraffic {
    static BufferedReader br = null;
    static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    static OWLOntology ontology;
    static OWLIndividual T = null;
    static Logger LOGGER = Logging.getLogger(OWLFileGeneratorForGHTraffic.class);
    static OWLClass Transaction;
    static OWLSubClassOfAxiom OWLSubClass;
    static JSONArray ghTrafficHeaderLabelsArray = new JSONArray();
    static List<String> lastIdList = new ArrayList<>();

    public enum FeatureType {
        RequestMethod,
        RequestHeader,
        RequestBody,
        RequestURI,
        RequestAuthToken,
        RequestHasPayload,
        RequestHasValidPayload,
        ResponseHeader,
        ResponseStatusCode,
        ResponseBody
    }

    public static List<OWLClass> RequestMethodOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> RequestHeaderOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> RequestBodyOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> RequestUriOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> ResponseHeaderOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> ResponseBodyOwlClassList = new ArrayList<OWLClass>();
    public static List<OWLClass> ResponseStatusCodeOwlClassList = new ArrayList<OWLClass>();

    public static List<OWLClass> OwlClassToDisjointRequestHeaderList = new ArrayList<OWLClass>();
    public static List<OWLClass> OwlClassToDisjointRequestUriList = new ArrayList<OWLClass>();
    public static List<OWLClass> OwlClassToDisjointResponseHeaderList = new ArrayList<OWLClass>();
    public static List<OWLClass> OwlClassToDisjointResponseBodyList = new ArrayList<OWLClass>();

    public static void generateOwlFile(String owlFileName, String SubDataFileName, String subTrainingFileName) throws Exception {
        try {
            // IRI stands for Internationalised Resource Identifier
            // Provides link to the ontology on the web, every class, every property, every individual has one
            IRI IOR = IRI.create("http://owl.api/httptransactions.owl");

            // Ask manager to create a new, empty ontology
            ontology = manager.createOntology(IOR);

            // Create ontology building blocks
            OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();

            createOwlClassLists(IOR, factory, subTrainingFileName);

            LOGGER.info("Generating OWL file");

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

            HTTPTransaction.read("src/resources/" + SubDataFileName + ".csv");

            preprocess(factory);

            for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
                List<String> n = new LinkedList<String>();
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

            // Save in RDF/XML format
            File fileOut = new File("src/resources/" + owlFileName + ".owl");

            manager.saveOntology(ontology, new FileOutputStream(fileOut));

            LOGGER.info("OWL File Generated");

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
                refinedValue = refineValueGHTraffic(FeatureType.RequestHeader, value);
                refinedValueForLabel = refineValue(value);

                String finalString = ("#RequestHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                ////// ADD DISJOINT CLASSES TO ONTOLOGY

                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, OwlClassToDisjointRequestHeaderList.stream().distinct().collect(Collectors.toList()), FeatureType.RequestHeader);

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                //////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, refinedValueForLabel, owlClassToAdd);
            }
        }

        for (OWLClass owlClass : RequestBodyOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_", 3)[1];
            String value = owlClass.toString().split("#")[1].split("_", 3)[2].split(">")[0];

            if (isFeatureExists(mm, key, value, FeatureType.RequestBody)) {
                String finalString = ("#RequestBody_" + key + "_" + value).toString().replace("\"", "");
                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                /// DISJOINT
                String firstKey = RequestBodyOwlClassList.get(0).toString().split("#")[1].split("_")[1];
                Set<OWLClass> c1 = new HashSet<OWLClass>();

                for (OWLClass owlClass1 : RequestBodyOwlClassList) {
                    if (firstKey.equals(key)) {
                        c1.add(owlClass1);
                    }
                }

                if (c1.size() > 0) {
                    OWLDisjointClassesAxiom disjointClassesAxiom = factory.getOWLDisjointClassesAxiom(c1);
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }
                ///// DISJOINT END

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

            if (value.contains("\'")) {
                value = value.replace("\'", "");
            }

            if (isFeatureExists(mm, key, value, FeatureType.ResponseHeader)) {
                refinedValue = refineValueGHTraffic(FeatureType.ResponseHeader, value);
                refinedValueForLabel = refineValue(value);
                String finalString = ("#ResponseHeader_" + key + "_" + refinedValue).toString().replace("\"", "");
                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                ////// ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, OwlClassToDisjointResponseHeaderList.stream().distinct().collect(Collectors.toList()), FeatureType.ResponseHeader);

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                //////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, refinedValueForLabel, owlClassToAdd);
                ghTrafficHeaderLabelsArray.add(refinedValue + ":" + value);
            }
        }

        for (OWLClass owlClass : ResponseBodyOwlClassList) {
            String key = "";
            String value = "";

            String ResponseBodyString = owlClass.toString().split("#", 2)[1].split("_", 2)[1];

            key = getResponseBodyKeyValue(ResponseBodyString).split("~")[0];
            value = getResponseBodyKeyValue(ResponseBodyString).split("~")[1];

            if (isFeatureExists(mm, key, value, FeatureType.ResponseBody)) {
                refinedValue = refineValueGHTraffic(FeatureType.ResponseBody, value);
                refinedValueForLabel = refineValue(value);

                String finalString = ("#ResponseBody_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                ////// ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, OwlClassToDisjointResponseBodyList.stream().distinct().collect(Collectors.toList()), FeatureType.ResponseBody);

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                //////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, refinedValueForLabel, owlClassToAdd);

                ghTrafficHeaderLabelsArray.add(refinedValue + ":" + value);
            }
        }

        for (OWLClass owlClass : ResponseStatusCodeOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_")[0];
            String value = owlClass.toString().split("#")[1].split("_")[1].split(">")[0];

            if (isFeatureExists(mm, key, value, FeatureType.ResponseStatusCode)) {
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClass, Transaction);

                ///// DISJOINT
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
                ///// DISJOINT END

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClass, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClass);
                ghTrafficHeaderLabelsArray.add(refinedValue + ":" + value);
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

                ////// ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, OwlClassToDisjointRequestUriList.stream().distinct().collect(Collectors.toList()), FeatureType.RequestURI);

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                //////

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

            if (isFeatureExists(mm, key, value, FeatureType.RequestMethod)) {
                refinedValue = refineValue(value);
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
                refinedValue = refineValueGHTraffic(FeatureType.RequestHeader, value);
                refinedValueForLabel = refineValue(value);

                String finalString = ("#RequestHeader_" + key + "_" + refinedValue).toString().replace("\"", "");

                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                ////// ADD DISJOINT CLASSES TO ONTOLOGY

                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, OwlClassToDisjointRequestHeaderList.stream().distinct().collect(Collectors.toList()), FeatureType.RequestHeader);

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                //////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, refinedValueForLabel, owlClassToAdd);
            }
        }

        for (OWLClass owlClass : RequestBodyOwlClassList) {
            String key = owlClass.toString().split("#")[1].split("_", 3)[1];
            String value = owlClass.toString().split("#")[1].split("_", 3)[2].split(">")[0];

            if (isFeatureExists(mm, key, value, FeatureType.RequestBody)) {
                String finalString = ("#RequestBody_" + key + "_" + value).toString().replace("\"", "");
                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                /// DISJOINT
                String firstKey = RequestBodyOwlClassList.get(0).toString().split("#")[1].split("_")[1];
                Set<OWLClass> c1 = new HashSet<OWLClass>();

                for (OWLClass owlClass1 : RequestBodyOwlClassList) {
                    if (firstKey.equals(key)) {
                        c1.add(owlClass1);
                    }
                }

                if (c1.size() > 0) {
                    OWLDisjointClassesAxiom disjointClassesAxiom = factory.getOWLDisjointClassesAxiom(c1);
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }
                ///// DISJOINT END

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

            if (isFeatureExists(mm, key, value, FeatureType.RequestURI)) {
                refinedValue = refineValue(value);

                String finalString = ("#" + key + "_" + refinedValue).toString().replace("''", "\'");
                OWLClass owlClassToAdd = factory.getOWLClass(IRI.create(IOR + finalString));
                OWLSubClass = factory.getOWLSubClassOfAxiom(owlClassToAdd, Transaction);

                ////// ADD DISJOINT CLASSES TO ONTOLOGY
                OWLDisjointClassesAxiom disjointClassesAxiom = disjointFeature(factory, key, OwlClassToDisjointRequestUriList.stream().distinct().collect(Collectors.toList()), FeatureType.RequestURI);

                if (disjointClassesAxiom != null) {
                    manager.addAxiom(ontology, disjointClassesAxiom);
                }

                //////

                AddAxiom addAxiomChangeOwlSubClass = new AddAxiom(ontology, OWLSubClass);
                manager.applyChange(addAxiomChangeOwlSubClass);

                OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClassToAdd, T);
                manager.addAxiom(ontology, classAssertion);

                addLabelToAnnotation(factory, value, owlClassToAdd);
            }
        }
    }

    public static boolean isFeatureExists(List<HTTPTransaction> mm, String featureKey, String featureValue, FeatureType featureType) {

        // REQUEST METHOD
        if (featureType == FeatureType.RequestMethod) {
            return mm.get(0).getMethod().equals(featureValue);
        }

        // REQUEST HEADER
        else if (featureType == FeatureType.RequestHeader) {
            String RequestHeaderValue = "";

            if (featureKey.equals("HasAuthorisationToken")) {
                RequestHeaderValue = String.valueOf(HasAuthorizationToken(mm));
            } else if (featureKey.equals("HasRequestPayload")) {
                RequestHeaderValue = String.valueOf(HasRequestPayloadGHTraffic(mm));
            } else if (featureKey.equals("HasValidRequestPayload")) {
                RequestHeaderValue = String.valueOf(HasValidRequestPayloadGHTraffic(mm));
            } else {
                RequestHeaderValue = String.valueOf(Utilities.RequestHeadersGHTraffic(mm, featureKey));
            }

            if (RequestHeaderValue.contains("\'")) {
                RequestHeaderValue = RequestHeaderValue.replaceAll("\'", "");
            }

            return RequestHeaderValue.toUpperCase().equals(featureValue.toUpperCase());

        }

        // REQUEST BODY
        else if (featureType == FeatureType.RequestBody) {
            String HeaderValueToAdd = String.valueOf(Utilities.RequestBodyGHTraffic(mm, featureKey));

            return HeaderValueToAdd.toUpperCase().equals(featureValue.toUpperCase());

        }

        // RESPONSE HEADER
        else if (featureType == FeatureType.ResponseHeader) {
            String responseHeaderValue = String.valueOf(Utilities.ResponseHeadersGHTraffic(mm, featureKey));
            if (responseHeaderValue.contains("\'")) {
                responseHeaderValue = responseHeaderValue.replaceAll("\'", "");
            }

            return responseHeaderValue.equals(featureValue);

        }

        // RESPONSE BODY
        else if (featureType == FeatureType.ResponseBody && featureKey.contains("assignees")) {
            String HeaderKeyInside = featureKey.split("\\.")[1];
            return String.valueOf(Utilities.ResponseBodyGHTraffic(mm, "assignees", HeaderKeyInside, null)).toUpperCase().equals(featureValue.toUpperCase());
        } else if (featureType == FeatureType.ResponseBody && !featureKey.contains(".")) {
            return String.valueOf(Utilities.ResponseBodyGHTraffic(mm, featureKey, null, null)).toUpperCase().equals(featureValue.toUpperCase());
        } else if (featureType == FeatureType.ResponseBody && StringUtils.countMatches(featureKey, ".") == 1) {
            String message = featureKey.split("\\.")[0];
            String bot_id = featureKey.split("\\.")[1];
            return String.valueOf(Utilities.ResponseBodyGHTraffic(mm, message, bot_id, null)).toUpperCase().equals(featureValue.toUpperCase());
        } else if (featureType == FeatureType.ResponseBody && StringUtils.countMatches(featureKey, ".") == 2) {
            String message = featureKey.split("\\.")[0];
            String edited = featureKey.split("\\.")[1];
            String user = featureKey.split("\\.")[2];

            return String.valueOf(Utilities.ResponseBodyGHTraffic(mm, message, edited, user)).toUpperCase().equals(featureValue.toUpperCase());
        }

        // RESPONSE STATUS CODE
        else if (featureType == FeatureType.ResponseStatusCode) {
            return mm.get(0).getCode().equals(featureValue);
        }

        // REQUEST URI
        else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturischema")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetURLScheme(mm.get(0).getURL());
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturihost")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetUriHost(mm.get(0).getURL());
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturipathtoken1")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken1");
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturipathtoken2")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken2");
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturipathtoken3")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken3");
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturipathtoken4")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken4");
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturipathtoken5")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken5");
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturipathtoken6")) {
            try {
                String URLCoreTokenMap = URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken6");
                if (URLCoreTokenMap == null) {
                    URLCoreTokenMap = "not-exist";
                }
                if (URLCoreTokenMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturiquerytoken1")) {
            try {
                String URLQueryTokenMap = URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken1");
                if (URLQueryTokenMap == null) {
                    URLQueryTokenMap = "not-exist";
                }
                if (URLQueryTokenMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturiquerytoken2")) {
            try {
                String URLQueryTokenMap = URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken2");
                if (URLQueryTokenMap == null) {
                    URLQueryTokenMap = "not-exist";
                }
                if (URLQueryTokenMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturiquerytoken3")) {
            try {
                String URLQueryTokenMap = URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken3");
                if (URLQueryTokenMap == null) {
                    URLQueryTokenMap = "not-exist";
                }
                if (URLQueryTokenMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturiquerytoken4")) {
            try {
                String URLQueryTokenMap = URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken4");
                if (URLQueryTokenMap == null) {
                    URLQueryTokenMap = "not-exist";
                }
                if (URLQueryTokenMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturifragmenttoken1")) {
            try {
                String FragmentMap = URITokeniser.GetFragmentMap(mm.get(0).getURL()).get("fragmentToken1");
                if (FragmentMap == null) {
                    FragmentMap = "not-exist";
                }
                if (FragmentMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (featureType == FeatureType.RequestURI && featureKey.toLowerCase().contains("requesturifragmenttoken2")) {
            try {
                String FragmentMap = URITokeniser.GetFragmentMap(mm.get(0).getURL()).get("fragmentToken2");
                if (FragmentMap == null) {
                    FragmentMap = "not-exist";
                }
                if (FragmentMap.equals(featureValue)) {
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static String refineValue(String value) {
        if (value.contains(";") || value.contains("=") || value.contains(" ") || value.contains("/") || value.contains("@")) {
            value = "\'" + value + "\'";
            if (value.contains("Internal Server Error") || value.contains("Invalid request") || value.contains("Not Found") || value.contains("Problems parsing JSON") || value.contains("Requires authentication")) {
            } else {
                value = value.replace(" ", "");
            }

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

    public static String refineValueGHTraffic(FeatureType featureType, String value) {
        if (value.contains("application/json; charset=utf-8") && featureType == FeatureType.RequestHeader) {
            value = "json";
        }

        if (value.contains("*/*") && featureType == FeatureType.RequestHeader) {
            value = "all";
        } else if (value.contains("*") && featureType == FeatureType.ResponseHeader) {
            value = "allow-origin-all";
        }

        if (value.contains("application/json; charset=utf-8") && featureType == FeatureType.ResponseHeader) {
            value = "json";
        }

        if (value.contains("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 1.0.3705)")) {
            value = "mozilla4-winNT5";
        }

        if (value.contains("Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3")) {
            value = "mozilla5-Mac";
        }

        if (value.contains("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9) Gecko/2008052906 Firefox/3.0")) {
            value = "mozilla5-winNT5";
        }

        if (value.contains("Opera/9.27 (Windows NT 5.1; U; en)")) {
            value = "opera-winNT5";
        }

        if (value.contains("Opera/9.50 (J2ME/MIDP; Opera Mini/4.1.11320/534; U; en)")) {
            value = "opera-mini";
        }

        if (value.contains("https://avatars.githubusercontent.com/u/1703908?v=3")) {
            value = "avatar-1703908";
        }

        if (value.contains("https://avatars.githubusercontent.com/u/544569?v=3")) {
            value = "avatar-544569";
        }

        if (value.contains("https://avatars.githubusercontent.com/u/7139661?v=3")) {
            value = "avatar-7139661";
        }

        if (value.contains("https://api.github.com/users/cpovirk/events{/privacy}")) {
            value = "events-cpovirk";
        }

        if (value.contains("https://api.github.com/users/lowasser/events{/privacy}")) {
            value = "events-lowasser";
        }

        if (value.contains("https://api.github.com/users/rgabbard-bbn/events{/privacy}")) {
            value = "events-rgabbard-bbn";
        }

        if (value.contains("https://api.github.com/users/cpovirk/followers")) {
            value = "followers-cpovirk";
        }

        if (value.contains("https://api.github.com/users/lowasser/followers")) {
            value = "followers-lowasser";
        }

        if (value.contains("https://api.github.com/users/rgabbard-bbn/followers")) {
            value = "followers-rgabbard-bbn";
        }

        if (value.contains("https://api.github.com/users/cpovirk/following{/other_user}'")) {
            value = "following-cpovirk";
        }

        if (value.contains("https://api.github.com/users/lowasser/following{/other_user}")) {
            value = "following-lowasser";
        }

        if (value.contains("https://api.github.com/users/rgabbard-bbn/following{/other_user}")) {
            value = "following-rgabbard-bbn";
        }

        if (value.contains("https://api.github.com/users/cpovirk/gists{/gist_id}")) {
            value = "gists-cpovirk";
        }

        if (value.contains("https://api.github.com/users/lowasser/gists{/gist_id}")) {
            value = "gists-lowasser";
        }

        if (value.contains("https://api.github.com/users/rgabbard-bbn/gists{/gist_id}")) {
            value = "gists-rgabbard-bbn";
        }

        if (value.contains("https://github.com/cpovirk")) {
            value = "html-cpovirk";
        }

        if (value.contains("https://github.com/lowasser")) {
            value = "html-lowasser";
        }

        if (value.contains("https://github.com/rgabbard-bbn")) {
            value = "html-rgabbard-bbn";
        }

        if (value.contains("https://api.github.com/users/cpovirk/orgs")) {
            value = "organizations-cpovirk";
        }

        if (value.contains("https://api.github.com/users/lowasser/orgs")) {
            value = "organizations-lowasser";
        }

        if (value.contains("https://api.github.com/users/rgabbard-bbn/orgs")) {
            value = "organizations-rgabbard-bbn";
        }
        if (value.contains("https://api.github.com/users/cpovirk/received_events")) {
            value = "received-events-cpovirk";
        }
        if (value.contains("https://api.github.com/users/lowasser/received_events")) {
            value = "received-events-cpovirk-lowasser";
        }
        if (value.contains("https://api.github.com/users/rgabbard-bbn/received_events")) {
            value = "received-events-cpovirk-rgabbard-bbn";
        }
        if (value.contains("https://api.github.com/users/cpovirk/repos")) {
            value = "repos-cpovirk";
        }
        if (value.contains("https://api.github.com/users/lowasser/repos")) {
            value = "repos-lowasser";
        }
        if (value.contains("https://api.github.com/users/rgabbard-bbn/repos")) {
            value = "repos-rgabbard-bbn";
        }
        if (value.contains("https://api.github.com/users/cpovirk/starred{/owner}{/repo}")) {
            value = "starred-cpovirk";
        }
        if (value.contains("https://api.github.com/users/lowasser/starred{/owner}{/repo}")) {
            value = "starred-lowasser";
        }
        if (value.contains("https://api.github.com/users/rgabbard-bbn/starred{/owner}{/repo}")) {
            value = "starred-rgabbard-bbn";
        }
        if (value.contains("https://api.github.com/users/cpovirk/subscriptions")) {
            value = "subscriptions-cpovirk";
        }
        if (value.contains("https://api.github.com/users/lowasser/subscriptions")) {
            value = "subscriptions-lowasser";
        }
        if (value.contains("https://api.github.com/users/rgabbard-bbn/subscriptions")) {
            value = "subscriptions-rgabbard-bbn";
        }
        if (value.contains("https://api.github.com/users/cpovirk")) {
            value = "cpovirk";
        }
        if (value.contains("https://api.github.com/users/lowasser")) {
            value = "lowasser";
        }
        if (value.contains("https://api.github.com/users/rgabbard-bbn")) {
            value = "rgabbard-bbn";
        }
        if (value.contains("https://avatars.githubusercontent.com/u/101568?v=2")) {
            value = "avatar-v2";
        }
        if (value.contains("https://avatars.githubusercontent.com/u/101568?v=3")) {
            value = "avatar-v3";
        }
        if (value.contains("https://api.github.com/users/cgdecker/events{/privacy}")) {
            value = "events-privacy";
        }
        if (value.contains("https://api.github.com/users/cgdecker/followers")) {
            value = "followers-cgdecker";
        }
        if (value.contains("https://api.github.com/users/cgdecker/following{/other_user}")) {
            value = "following-cgdecker";
        }
        if (value.contains("https://api.github.com/users/cgdecker/gists{/gist_id}")) {
            value = "gists-cgdecker";
        }
        if (value.contains("https://github.com/cgdecker")) {
            value = "html-cgdecker";
        }
        if (value.contains("https://api.github.com/users/cgdecker/orgs")) {
            value = "organizations-cgdecker";
        }
        if (value.contains("https://api.github.com/users/cgdecker/received_events")) {
            value = "events-cgdecker";
        }
        if (value.contains("https://api.github.com/users/cgdecker/repos")) {
            value = "repos-cgdecker";
        }
        if (value.contains("https://api.github.com/users/cgdecker/starred{/owner}{/repo}")) {
            value = "starred-cgdecker";
        }
        if (value.contains("https://api.github.com/users/cgdecker/subscriptions")) {
            value = "subscriptions-cgdecker";
        }
        if (value.contains("https://api.github.com/users/cgdecker")) {
            value = "creator-cgdecker";
        }
        if (value.contains("ETag, X-OAuth-Scopes, X-Accepted-OAuth-Scopes")) {
            value = "oauth";
        }
        if (value.contains("private, max-age=60")) {
            value = "max-age";
        }
        if (value.contains("Accept, Authorization, Cookie")) {
            value = "accept";
        }
        if (value.contains("public_repo, repo")) {
            value = "accepted-public-repo";
        } else if (value.contains("public_repo")) {
            value = "public-repo";
        }
        if (value.contains("github.v3; format=json")) {
            value = "v3-json";
        }
        if (value.contains("https://developer.github.com/v3/issues/#create-an-issue")) {
            value = "create-an-issue";
        }
        if (value.contains("https://developer.github.com/v3/issues/#edit-an-issue")) {
            value = "edit-an-issue";
        }
        if (value.contains("https://developer.github.com/v3/issues/#lock-an-issue")) {
            value = "lock-an-issue";
        }
        if (value.contains("https://developer.github.com/v3/issues/#unlock-an-issue")) {
            value = "unlock-an-issue";
        }
        if (value.contains("https://developer.github.com/v3")) {
            value = "v3";
        }
        if (value.contains("Internal Server Error")) {
            value = "internal-server-error";
        }
        if (value.contains("Invalid request")) {
            value = "invalid-request";
        }
        if (value.contains("Not Found")) {
            value = "not-found";
        }
        if (value.contains("Problems parsing JSON")) {
            value = "problems-passing-json";
        }
        if (value.contains("Requires authentication")) {
            value = "requires-authentication";
        }

        return value;
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

            if (featureType == FeatureType.ResponseBody) {
                String ResponseBodyString = owlClass.toString().split("#", 2)[1].split("_", 2)[1];
                key = getResponseBodyKeyValue(ResponseBodyString).split("~")[0];

                if (key.equals(originalKey)) {
                    c1.add(owlClass);
                }
            } else {
                if (StringUtils.countMatches(owlClass.toString(), "_") > 2) {
                    if (owlClass.toString().contains("public_repo")) {
                        key = owlClass.toString().split("#")[1].split("_", 4)[1];
                        if (key.equals(originalKey)) {
                            c1.add(owlClass);
                        }
                    } else {
                        key = owlClass.toString().split("#")[1].split("_", 4)[1] + "_" + owlClass.toString().split("#")[1].split("_", 4)[2];
                        if (key.equals(originalKey)) {
                            c1.add(owlClass);
                        }
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

    public static String getResponseBodyKeyValue(String ResponseBodyString) {
        String key = "";
        String value = "";

        if (ResponseBodyString.contains("closed_by.")) {
            if (StringUtils.countMatches(ResponseBodyString, "_") >= 4) {
                if (ResponseBodyString.contains("received_events_url")) {
                    String[] Arr = ResponseBodyString.split("_", 6);
                    key = Arr[0] + "_" + Arr[1] + "_" + Arr[2] + "_" + Arr[3];
                    if (StringUtils.countMatches(ResponseBodyString, "_") < 5) {
                        value = Arr[4];
                    } else {
                        value = Arr[4] + "_" + Arr[5];
                    }
                } else {
                    String[] Arr = ResponseBodyString.split("_", 4);
                    key = Arr[0] + "_" + Arr[1] + "_" + Arr[2];
                    value = Arr[3];
                }
            } else if (StringUtils.countMatches(ResponseBodyString, "_") >= 3) {
                String[] Arr = ResponseBodyString.split("_", 4);
                key = Arr[0] + "_" + Arr[1] + "_" + Arr[2];
                value = Arr[3];
            } else if (StringUtils.countMatches(ResponseBodyString, "_") == 2) {
                String[] Arr = ResponseBodyString.split("_", 3);
                key = Arr[0] + "_" + Arr[1];
                value = Arr[2];
            }
        } else if (ResponseBodyString.contains("milestone.")) {
            if (StringUtils.countMatches(ResponseBodyString, "_") >= 3) {
                if (ResponseBodyString.contains("received_events_url")) {
                    key = ResponseBodyString.split("_", 4)[0] + "_" + ResponseBodyString.split("_", 4)[1] + "_" + ResponseBodyString.split("_", 4)[2];
                    value = ResponseBodyString.split("_", 4)[3];
                } else {
                    key = ResponseBodyString.split("_", 4)[0] + "_" + ResponseBodyString.split("_", 4)[1];
                    value = ResponseBodyString.split("_", 4)[2] + "_" + ResponseBodyString.split("_", 4)[3];
                }
            } else if (StringUtils.countMatches(ResponseBodyString, "_") >= 2) {
                key = ResponseBodyString.split("_", 3)[0] + "_" + ResponseBodyString.split("_", 3)[1];
                value = ResponseBodyString.split("_", 3)[2];
            } else {
                key = ResponseBodyString.split("_")[0];
                value = ResponseBodyString.split("_")[1];
            }
        } else if (ResponseBodyString.contains("documentation_url")) {
            key = ResponseBodyString.split("_", 3)[0] + "_" + ResponseBodyString.split("_", 3)[1];
            value = ResponseBodyString.split("_", 3)[2];
        } else {
            if (StringUtils.countMatches(ResponseBodyString, "_") >= 2) {
                key = ResponseBodyString.split("_", 3)[0] + "_" + ResponseBodyString.split("_", 3)[1];
                value = ResponseBodyString.split("_", 3)[2];
            } else if (StringUtils.countMatches(ResponseBodyString, "_") == 1) {
                key = ResponseBodyString.split("_", 3)[0];
                value = ResponseBodyString.split("_", 3)[1];
            }
        }

        if (String.valueOf(value).equals(">")) {
            value = "some-text";
        } else {
            value = value.split(">")[0];
        }

        if (value.contains("\'")) {
            value = value.replace("\'", "");
        }

        return key + "~" + value;
    }

    public static void createOwlClassList(IRI IOR, OWLDataFactory factory, FeatureType featureType, String[] feature, List<String> valueLinesList) throws IOException {
        LOGGER.info("Creating OWL Class lists for " + featureType);

        // set index of first and last columns to loop through each feature type
        int colStart = getColumnNumber(featureType, "start");
        int colEnd = getColumnNumber(featureType, "end");

        for (int i = colStart; i <= colEnd; i++) {

            List<String> distinctFeatureValueList = getDistinctFeatureValuesFromCSV(i, feature, valueLinesList);

            for (String headerItem : distinctFeatureValueList) {

                if (distinctFeatureValueList.size() >= 9) { // don't add classes to the list those who have 8 or more distinct values
                } else if (headerItem.contains("ResponseBody_milestone.html_url")){
                } else {
                    if (headerItem.indexOf(" ") >= 0 || headerItem.contains(",") || headerItem.contains(";") || headerItem.contains("=") || headerItem.contains("/")) {
                        headerItem = "\'" + headerItem + "\'";
                    }
                    switch (featureType) {
                        case RequestMethod:
                            RequestMethodOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
                            break;
                        case RequestHeader:
                            RequestHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
                            break;
                        case RequestBody:
                            RequestBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
                            break;
                        case ResponseHeader:
                            ResponseHeaderOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
                            break;
                        case ResponseBody:
                            ResponseBodyOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
                            break;
                        case ResponseStatusCode:
                            ResponseStatusCodeOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
                            break;
                        case RequestURI:
                            RequestUriOwlClassList.add(factory.getOWLClass(IRI.create(IOR + headerItem)));
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
            case ResponseStatusCode:
                startPoint = 1;
                endPoint = 1;
                break;
            case RequestURI:
                startPoint = 2;
                endPoint = 15;
                break;
            case RequestHeader:
                startPoint = 16;
                endPoint = 23;
                break;
            case ResponseHeader:
                startPoint = 24;
                endPoint = 38;
                break;
            case RequestBody:
                startPoint = 39;
                endPoint = 39;
                break;
            case ResponseBody:
                startPoint = 40;
                endPoint = 150;
                break;
            default:
                return 0;
        }

        return startOrEnd == "start" ? startPoint : endPoint;
    }

    public static List<String> getDistinctFeatureValuesFromCSV(int i, String[] headerText, List<String> valueLineList) throws IOException {

        String cvsSplitBy = ",(?=(?:[^\\\']*\\\'[^\\\']*\\\')*[^\\\']*$)";
        List<String> rhConnectionList = new ArrayList<>();

        String header = "";

        for (String valueLine : valueLineList) {
            header = headerText[i];
            if (header.contains(":")) {
                header = header.replace(":", "_");
            }

            String[] valueText = valueLine.split(cvsSplitBy);
            String finalString = "";

            finalString = ("#" + header + "_" + valueText[i]).toString().replace("\"", "");
            if (valueText[i] == "" || valueText[i].equals("")) {
            } else {
                rhConnectionList.add(finalString);
            }
        }

        List<String> rhConnectionFinalList = rhConnectionList.stream().distinct().collect(Collectors.toList());

        return rhConnectionFinalList;
    }

    public static void createOwlClassLists(IRI IOR, OWLDataFactory factory, String subTrainingFileName) throws IOException {

        String csvFile = "src/resources/" + subTrainingFileName + ".csv";
        BufferedReader br = null;
        String line = "";

        String cvsSplitBy = ",(?=(?:[^\\\']*\\\'[^\\\']*\\\')*[^\\\']*$)";

        br = new BufferedReader(new FileReader(csvFile));
        line = br.readLine();
        String[] feature = line.split(cvsSplitBy);

        List<String> valueLinesList = br.lines().distinct().collect(Collectors.toList());

        createOwlClassList(IOR, factory, FeatureType.RequestMethod, feature, valueLinesList);
        createOwlClassList(IOR, factory, FeatureType.RequestHeader, feature, valueLinesList);
        createOwlClassList(IOR, factory, FeatureType.RequestBody, feature, valueLinesList);
        createOwlClassList(IOR, factory, FeatureType.RequestURI, feature, valueLinesList);
        createOwlClassList(IOR, factory, FeatureType.ResponseHeader, feature, valueLinesList);
        createOwlClassList(IOR, factory, FeatureType.ResponseBody, feature, valueLinesList);
        createOwlClassList(IOR, factory, FeatureType.ResponseStatusCode, feature, valueLinesList);
    }

    public static void preprocess(OWLDataFactory factory) {
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            List<String> n = new LinkedList<String>();
            for (List<HTTPTransaction> mm : m.getValue().values()) {
                n.add(mm.get(0).transaction);
                // create disjoint class list
                createDisjointClassList(mm, factory);
            }

            // get the list of transaction ids happened lastly in each resource
            lastIdList.add(n.get(n.size() - 1));
        }
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

                    if (isFeatureExists(mm, currentKey, currentValue, FeatureType.RequestHeader)) {
                        refinedValue = refineValueGHTraffic(FeatureType.RequestHeader, currentValue);
                        String finalString = ("#RequestHeader_" + nextKey + "_" + refinedValue).toString().replace("\"", "");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        OwlClassToDisjointRequestHeaderList.add(owlClassToDisjoint);
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
                        refinedValue = refineValue(currentValue);
                        String finalString = ("#" + nextKey + "_" + refinedValue).toString().replace("''", "\'");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        OwlClassToDisjointRequestUriList.add(owlClassToDisjoint);
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
                    if (currentValue.contains("\'")) {
                        currentValue = currentValue.replace("\'", "");
                    }

                    if (isFeatureExists(mm, currentKey, currentValue, FeatureType.ResponseHeader)) {
                        refinedValue = refineValueGHTraffic(FeatureType.ResponseHeader, currentValue);
                        String finalString = ("#ResponseHeader_" + nextKey + "_" + refinedValue).toString().replace("\"", "");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        OwlClassToDisjointResponseHeaderList.add(owlClassToDisjoint);
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

                currentKeyString = ResponseBodyOwlClassList.get(i).toString().split("#", 2)[1].split("_", 2)[1];
                currentKey = getResponseBodyKeyValue(currentKeyString).split("~")[0];
                currentValue = getResponseBodyKeyValue(currentKeyString).split("~")[1];

                nextKeyString = ResponseBodyOwlClassList.get(j).toString().split("#", 2)[1].split("_", 2)[1];
                nextKey = getResponseBodyKeyValue(nextKeyString).split("~")[0];

                if (currentKey.equals(nextKey) && i != j) {
                    if (isFeatureExists(mm, currentKey, currentValue, FeatureType.ResponseBody)) {
                        refinedValue = refineValueGHTraffic(FeatureType.ResponseBody, currentValue);

                        String finalString = ("#ResponseBody_" + nextKey + "_" + refinedValue).toString().replace("\"", "");
                        OWLClass owlClassToDisjoint = factory.getOWLClass(IRI.create(IOR + finalString));
                        OwlClassToDisjointResponseBodyList.add(owlClassToDisjoint);
                    }
                }
            }
        }
    }
}