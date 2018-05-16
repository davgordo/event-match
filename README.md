# Event Match API

This application makes event suggestions based on users' availability, location, and interests, given a database of events.

![Application Overview Diagram](agile-integration-demo.png?raw=true "Application Overview Diagram") 

## Installation

### Deploy Cluster

Start an OpenShift cluster with the appropriate resources: 

`minishift start --cpus=2 --memory=6GB`

Login to the CLI: 

`oc login -u admin`

### Add Fuse 7 Template

Apply Fuse 7 Image Stream: 

`oc apply -f ci/jboss-fuse-70-java-openshift.yml -n openshift`

### Create App Environments

Create DEV and TEST environments:

`oc new-project event-match-dev`

`oc new-project event-match-test`

`oc new-project event-match-prod`

### Deploy AMQ

`oc process -f ci/amq.yml -o yaml | oc -n event-match-dev apply -f -`

`oc process -f ci/amq.yml -o yaml | oc -n event-match-test apply -f -`

`oc process -f ci/amq.yml -o yaml | oc -n event-match-prod apply -f -`

Note: You can provide parameters to the template by adding `-l AMQ_USER=dev AMQ_PASSWORD=secret` to the `oc process` command.

### Set up CI/CD

Create CI Project:

`oc new-project ci`

Create the Jenkins Pipelines:

`oc apply -f ci/pipelines.yml`

### Deploy Maven Mirror (Optional)

A local Maven mirror is highly recommended for faster builds.

Create a Nexus instance:

`oc apply -f ci/nexus.yml`

Open the resulting Nexus route URL in your browser then add `/nexus` to the HTTP path, e.g. `http://nexus-ci.192.168.42.192.nip.io/nexus`

Login with user: `admin` password: `admin123`

Browse to `Repositories`

Add the following proxy repositores:

| Repo ID             | Repo Name                           | Remote Storage Location                              |
| ------------------- | ----------------------------------- | ---------------------------------------------------- |
| jboss-ga-repository | The Red Hat GA repository           | https://maven.repository.redhat.com/ga/              |
| jboss-ea-repository | The Red Hat Early Access repository | https://maven.repository.redhat.com/earlyaccess/all/ |

Go to the configuration tab for the `Public Repositories` group, and include the two newly created proxy repos.

Note: If you do not deploy Nexus to the `ci` project, remove the "mirror" configuration from `${service}/configuration/settings.xml`.

## Ad-hoc Deployment for Experimental Development

for any `suggestion`, `event`, `calendar`, and `profile` services 

`cd ${service}` 

`mvn fabric8:deploy`
