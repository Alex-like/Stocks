Build app and add docker image with app to local docker registry:

```mvn -am -pl stocks package```

Run integration test with docker:

```mvn -am -pl client test```