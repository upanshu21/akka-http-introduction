Akka is a popular actor-based toolkit for building concurrent and distributed applications in the JVM. These applications mostly use Scala or Java.

It has several modules that help to build such applications, and Akka HTTP is one of them.


Because Akka HTTP uses Akka actors and streams underneath, we will need to supply their dependencies as well:

An ActorSystem is used to manage actors. It is used for creating and looking them up. Actors in the same system typically share the same config.

The ExecutionContext is in charge of executing Future s. It knows where and how it should execute them, for example in a thread pool.

And finally, an ActorMaterializer is in charge of running streams.

With that done, we can create our hello route!


o create our route, we will use Akka HTTP’s routing DSL. It is based on “layers” of what’s called a directive. For an overview, feel free to browse their official docs.

Add the route below the dependencies:

def route = path("hello") {  get {    complete("Hello, World!")  }}
We have a first layer, where we try to match the incoming request’s path as “/hello”. If it doesn’t match it will be rejected.

If it matches it will try to match inner “directives”. In our case we are matching GET requests. We complete the request/response cycle with a “Hello, World” message.


Start the server
With our route created, all we need to do is start the server:

Http().bindAndHandle(route, host, port)
We are binding our route to the given host and port using the Akka HTTP Http object.

To run our Server object, you can right-click it and hit Run ‘Server’.

Give it a couple of seconds to compile, then go to a browser. Navigate to http://localhost:9000/hello and you should see our “Hello, World!” message.




