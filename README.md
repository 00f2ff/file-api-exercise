# File System API

My submission for a File System API is built using Kotlin and the [ktor](https://ktor.io/)
server framework. ktor allows developers to build asychronous servers
with imperative code.

### API Documentation
Copy the contents of `openapi_spec.yaml` into a [Swagger editor](https://editor.swagger.io/)
to see how the API accepts a path and returns different types of objects.

### Running the Server
1. Enter `chmod a+rx run.sh` to make `run.sh` into an executable
2. `run.sh` takes an absolute filepath as a parameter (no quotes). Run
   something like `run.sh /Users/Duncan` to start the server.

`run.sh` sets the filepath parameter as an environment variable. The variable
then gets picked up by the `docker-compose` and inserted into the app's
environment, as well as mounted as a volume on the container. When the
API receives requests, the specified portion of the file system is read
and returns responses accordingly.

