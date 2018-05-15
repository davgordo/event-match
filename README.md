# Event Match API

This application makes event suggestions based on users' availability, location, and interests, given a database of events.

## Installation

Start a cluster: 

`minishift start`

Login to the CLI: 

`oc login -u admin`

Apply Fuse 7 Image Stream: 

`oc apply -f ci/jboss-fuse-70-java-openshift.yml -n openshift`

Create DEV and TEST environments:

`oc new-project event-match-dev`

`oc new-project event-match-test`

`oc new-project event-match-prod`

Create CI Project:

`oc new-project ci`

Create the Jenkins Pipelines:

`oc apply -f ci/pipelines.yml`

## Ad-hoc Deployment for Experimental Development

for any `suggestion`, `event`, `calendar`, and `profile` services 

`cd ${service_name}` 

`mvn fabric8:deploy`
