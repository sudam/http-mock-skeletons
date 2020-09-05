## Introduction

This is the project repository for the thesis titled **[Generating Mock Skeletons for Lightweight Web Service Testing](https://bitbucket.org/tbhagya/thesis/)**. And includes scripts to run the experiments carried out in this study and to reproduce the research results.

The aim of this research is to understand the appropriateness of SML techniques for generating mock skeletons of HTTP services directly from traffic records. We consider four promising symbolic learning algorithms: the **C4.5** decision tree algorithm, the **RIPPER** and **PART** rule learners (**from** **attribute-based learning** and we use their **WEKA** implementations) and the **OCEL** class expression learning algorithm (**from description logic learning** and it is implemented in **DL-Learner**). All the experiments have been done employing network traffic datasets derived from the services offered by GitHub, Twitter, Google, and Slack. 

There are four stages in the experimental methodology: 

1. **Data Preprocessing**: cleans the raw datasets by removing the contents of data records that impede datasets from being processed and/or parsed correctly. The cleansed data will be stored separately from the raw datasets for the next step to load up and process.
2. **Data Transformation**: converts preprocessed data into data formats appropriate for the selected learning algorithms. The attribute-based learning algorithms expect input data described in the propositional attribute-value format, whereas algorithms based on description logic learning require data with rich domain knowledge expressed in description logics. We, therefore, pursue two different directions for the transformation of preprocessed data. The data extracted from recorded HTTP transactions are subsequently converted into the Attribute-Relation File Format (ARFF) to be used in WEKA, also transformed into the OWL knowledge bases to be used in DL-Learner (also some individuals on each knowledge base are selected to serve as examples).
3. **Model Construction**: chosen algorithms are used to train models to predict some of the properties of HTTP service responses by directly inferring knowledge of the protocol structure and service status from training data. In attribute-based learning, each response property is viewed as a separate target to learn. However, targets with either one distinct value or holding a fairly large set of distinct values are excluded as they are non-optimal for predictions. In description logic learning, each response property value is treated as a separate class learning problem. Some instances in the example set also have to be identified as positive examples and others as negative ones to learn class definitions. However, target classes with no negative examples or without any positive examples are ignored from learning as OCEL has restricted its use to learning problems where both positive and negative examples exist.
4. **Model Evaluation**: tests the predictive ability and comprehensibility of generated models through cross validation. In attribute-based learning, we apply 10-fold cross validation to evaluate the models. However, due to memory constraints, we could not perform 10-fold cross validation in description logic learning and perform tests with 2-fold.

For more details on the construction of these phases, see **Chapter 5** in the thesis.

## Accessing Experimental Datasets

**Chapter 4** of the thesis details four network traffic datasets (GHTraffic, Twitter, Google Tasks, and Slack) designed to be suitable for reproducible research in service-oriented computing. 

The **small edition of GHTraffic 2.0.0**, **Twitter 1.0.0**, **Google Tasks 1.0.0**, and **Slack 1.0.0** datasets are used to perform experiments with attribute-based learning algorithms. However, description logic learning algorithms have confined their use to learning problems whose knowledge bases range from small to large (to allow loading the entire knowledge bases into the OWL reasoners used in the algorithms for reasoning). The Google Tasks 1.0.0 dataset is a mid-sized and mid-complex dataset that meets the above-mentioned requirement. The other three datasets are however very large and complex, so we use sub-datasets of the originals to do experiments.

Datasets for performing experiments with attribute-based learning can be downloaded using: 

- [GHTraffic](https://zenodo.org/record/4007589/files/ghtraffic-S-2.0.0.zip)
- [Twitter](https://zenodo.org/record/4007570/files/twitter-1.0.0.zip)
- [Google Tasks](https://zenodo.org/record/4007570/files/googletasks-1.0.0.zip)
- [Slack](https://zenodo.org/record/4007570/files/slack-1.0.0.zip)

Datasets for performing experiments with description logic learning can be downloaded using:

- [GHTraffic](https://zenodo.org/record/4008239/files/sub-ghtraffic-S-2.0.0.zip)
- [Twitter](https://zenodo.org/record/4008239/files/sub-twitter-1.0.0.zip)
- [Google Tasks](https://zenodo.org/record/4007570/files/googletasks-1.0.0.zip)
- [Slack](https://zenodo.org/record/4008239/files/sub-slack-1.0.0.zip)

## Using Experimental Scripts

The experimental methodology described above is implemented in Java and is developed using the Maven software project management architecture. We provide access to these scripts, including a pre-configured VirtualBox image, to run the experiments and to reproduce the research results set out in **Chapter 6**.

Scripts can be accessed by cloning the repository, or by downloading the VirtualBox image from here.

### Environment SetUp

The following setup is required in order to use the experimental scripts by cloning the repository:

1. Install Java Runtime Environment (JRE) 8 or above
2. Install Apache Maven 3.5.0 or above
3. Create scripts/src/resources folder where all raw and training data is stored 
4. Download the appropriate dataset from the links above, based on the learning type you intend to experiment with
5. Extract the zipped file and move the dataset file (i.e., json or xml) to scripts/src/resources

### Running Experiments

#### Generating Training Data

The script **datapreparator.sh** is configured to execute the **Data Preprocessing** and **Data Transformation** phases in which the downloaded raw data is cleaned and converted into data formats suitable for different learning types. 

It requires two arguments: 

- **-l** with either **abl** (for attribute-based learning) or **dll** (for description logic learning) specifying the learning type intended for experimentation
- **-d** with either **GHTraffic**, **Twitter**, **GoogleTasks** or **Slack** specifying the type of dataset downloaded

Depending on the dataset and learning type specified, the training data (e.g., ARFF or OWL) will be generated and stored in the resources folder. Note that it can take quite some time to generate training data.

For example to perform experiments on attribute-based learning algorithms with GoogleTasks dataset, clone the repository into a folder, extract the dataset into scripts/src/resources, cd into scripts folder and run the following command:

```
./datapreparator.sh -l abl -d GoogleTasks
```

#### Training Classifiers

The script **modellearner.sh** is configured to execute the **Model Construction** step in which the model artefacts are generated from training data created by datapreparator.sh.

It requires three arguments: 

- **-a** with either **C4.5**, **RIPPER**, **PART** or **OCEL** specifying the learning algorithm intended for experimentation
- **-d** with either **GHTraffic**, **Twitter**, **GoogleTasks** or **Slack** specifying the type of dataset
- **-i** with the **target index** to train in attribute-based learning or **-c** with the name of **target class** to train in description logic learning

Based on the specified algorithm and the target, the model will be generated and output to the terminal.

For example run the following command to generate a classification model by C4.5 for the ResponseStatusCode target attribute (with index 1) of the GoogleTasks dataset:

```
./modellearner.sh -a C4.5 -d GoogleTasks -i 1
```

The script also contains options **-ilist** and **-clist** with the **dataset type** as the value for retrieving the full list of target attribute indexes or target class names that are optimal for predictions in each dataset. By referring to these lists, you can identify the target attribute index or the target class name that you want to learn.

For example run the following command to access the target attribute list for the GoogleTasks dataset:

```
./modellearner.sh -ilist GoogleTasks
```

#### Testing Classifiers

The script **modelevaluator.sh** is configured to execute the **Model Evaluation** step in which the generated model artefacts are tested using the cross validation technique.

It requires the **same input arguments** as modellearner.sh.

Depending on the specified algorithm and the target, the model will be evaluated and the results will be output to the terminal. Note that the evaluation results can slightly differ in each execution as cross validation randomly generates folds.

For example run the following command to cross validate the C4.5 model for the ResponseStatusCode target attribute (with index 1) of the GoogleTasks dataset:

```
./modelevaluator.sh -a C4.5 -d GoogleTasks -i 1
```

