# jackson-json-error-instrumentation

Testing library that returns error permutations of JSONs to be sent to the API
under test.

This is only a first approach. So far it works with just a few annotations and
data types. There are also some interesting questions about arrays, collections
and maps, of whether it would be correct to nullify contents that are not
annotated.

Would also be a very good idea to either find or create an annotation that can
allow us to add specs to array, collection and map elements.

Also consider picking up from the last added test, instead of having to
regenerate the whole JSON for every error item.

**Will be extremely important to move the counter from the mutagens to a better
location, so that if the user needs to build custom mutagens the counters will
not be an extra hurdle to deal with.**
