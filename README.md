# Event Match API

This application makes event suggestions based on users' availability, location, and interests, given a database of events.

## Installation

### Deploy Cluster

Start an OpenShift cluster: 

`minishift start`

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

### Set up CI/CD

Create CI Project:

`oc new-project ci`

Create the Jenkins Pipelines:

`oc apply -f ci/pipelines.yml`

### Deploy Maven Mirror (Optional)

A local Maven mirror is highly recommended for faster builds.

More info about deploying Nexus is here: https://alainpham.github.io/posts/faster-fuse-integration-service-s2i-openshift-deployments/

Note: If you do not deploy Nexus to the `ci` project, remove the "mirror" configuration from `${service}/configuration/settings.xml`.

## Ad-hoc Deployment for Experimental Development

for any `suggestion`, `event`, `calendar`, and `profile` services 

`cd ${service}` 

`mvn fabric8:deploy`
