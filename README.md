## Introduction



## Accessing Experimental Datasets

The different editions of the GHTraffic dataset can be downloaded using: S, M, and L.

## Using Experimental Script

We also provide access to the scripts used to generate GHTraffic. Using these scripts, you can modify the configuration properties in the config.properties file in order to create a customised version of the GHTraffic dataset for your own use.

Scripts can be accessed by cloning the repository, or by downloading the pre-configured VirtualBox image from here.

### Environment SetUp

The following set up is required to use the GHTraffic script by cloning the repository:

Install openjdk-8-jdk

Install MongoDB 3.4.9

Create data/db folder where Mongo stores data

Download the GHTorrent dump from here

Move the gzipped tar file to /data/db directory and extract (requires nearly 50 GB of memory)

Move all the content in /data/db/dump/github/ to /data/db/github

Start the Mongo server by running the following command:

sudo service mongod start 
Restore data from the binary database dump to a MongoDB instance by running the following command:
mongorestore $path-to-data-folder/data/db/github -d github 

### Generating GHTraffic Dataset

The script build.sh with options S, M or L can be used to create either edition.

To generate the small edition of GHTraffic, clone the repository into a folder, cd into script folder and run the following command:

./build.sh -S 
To generate all three editions of GHTraffic, run the following command:
./build.sh -S -M -L 
Each dataset will be located in a sub folder called scripts/DataSet

The script analyse.sh analyses each dataset in terms of HTTP request methods, status codes and GHTraffic record type.

Latex tables per matric will be generated in paper/Metrics folder

**Note that due to the use of random data generation this scripts will produce slightly different datasets at each execution.**