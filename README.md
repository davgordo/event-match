# Event Match API

This application makes event suggestions based on users' availability, location, and interests, given a database of events.

## Installation

- Start a cluster: `minishift start`
- Login to the CLI: `oc login -u admin`
- Apply Fuse 7 IS: `oc apply -f jboss-fuse-70-java-openshift.yml -n openshift`
- for each `suggestion`, `event`, `calendar`, and `profile` service `mvn fabric8:deploy`


