# jackson-json-error-instrumentation

Testing library that returns error permutations of JSONs to be sent to the API
under test.

This is only a first approach. So far it works with just a few annotations and
data types. There are also some interesting questions about arrays, collections
and maps, of whether it would be correct to nullify contents that are not
annotated or not.

Would also be a very good idea to either find or create an annotation that can
allow us to add specs to array, collection and map elements.

Need to also add some kind of description per mutation performed on the source
object. It will be very useful for naming the tests using this library. Maybe
using the generator's `getOutputContext().pathAsPointer()` method.

Also consider picking up from the last added test, instead of having to
regenerate the whole JSON for every error item.
