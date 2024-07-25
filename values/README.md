DFXEngine Values contains mutable number types with a dynamic modifier system. It uses the [Math](../math) module to
represent numbers.

## [`Value`](src/main/java/me/datafox/dfxengine/values/ValueImpl.java)

`Value` is a mutable class that is identified with a handle and wraps two numerals, `base` and `value`. The base always
stays the same unless changed directly by the value's method, and the value is calculated when the getter method is used
based on modifiers attached to the value.

To avoid unnecessary calculations, the value is cached and invalidated when any of the attached modifiers
change. The base can be changed with the `set(Numeral)` and various `apply` methods, and values can be compared against
each other with the `compare(Comparison, ComparisonContext, Numeral)` method.

The value can also be immutable. This causes the `set(Numeral)` and `apply` methods to throw an
`UnsupportedOperationException`, effectively preventing the `base` from being changed, but modifiers may still be added
to immutable values like normal.

[`StaticValue`](src/main/java/me/datafox/dfxengine/values/StaticValue.java) is a special separate implementation of
value that is completely immutable, including modifiers, and is not identified by a handle. These are most useful as
parameters for modifiers.

## [`ValueMap`](src/main/java/me/datafox/dfxengine/values/DelegatedValueMap.java)

`ValueMap` is a map of values that extends [`HandleMap<Value>`](../handles#handlemapt). Along with all features of
handle maps, it also has various `set` and `apply` methods for changing all or some of its values, or even adding new
values. Modifiers can also be attached to it, which are automatically attached to all contained values, including values
added after the modifier has been attached. Values removed from the map also have the map-specific modifiers removed
from them.

Just like values, value maps can also be immutable. All immutability rules apply, and a map may only contain mutable or
immutable values, but never both.

## [`Operation`](../values-api/src/main/java/me/datafox/dfxengine/values/api/operation/Operation.java)

`Operation` is an interface that represents a math operation. It takes in a source parameter and an arbitrary number of
other parameters, the amount of which is determined by the `getParameterCount()` method. The method does not count the
source parameter. There are two implementations of operation:
* [`OperationChain`](src/main/java/me/datafox/dfxengine/values/operation/OperationChain.java) chains multiple operations
  together, using the previous operation's result as the source for the next one. The parameter count is the sum of
  child operation parameter counts, and parameters are given in order to the child operations
* [`MappingOperationChain`](src/main/java/me/datafox/dfxengine/values/operation/MappingOperationChain.java) also chains
  operations together, but is more flexible. It takes in more parameters than the sum of child operation parameters,
  specifically that plus the amount of child operations. Parameters are still given in order, but each operation also
  takes in its source parameter. It can take in special numerals as parameters, which represent either the source
  numeral given to the operation chain or the result of a prior operation in the chain. Special numerals can be
  acquired with the `sourceNumeral()` and `resultNumeral(int)` static methods, and referring to a future operation
  throws an `IllegalArgumentException`

In addition to these operations, there are three extending interfaces that have a set number of parameters. These are
functional interfaces and can be created with a method reference to the operation methods in the
[`Operations`](../math/src/main/java/me/datafox/dfxengine/math/utils/Operations.java) utility class from the Math
module. These extending interfaces are:
* [`SourceOperation`](../values-api/src/main/java/me/datafox/dfxengine/values/api/operation/SourceOperation.java)
  represents an operation that only takes in a source parameter
* [`SingleParameterOperation`](../values-api/src/main/java/me/datafox/dfxengine/values/api/operation/SingleParameterOperation.java)
  represents an operation that takes in a single parameter in addition to the source parameter
* [`DualParameterOperation`](../values-api/src/main/java/me/datafox/dfxengine/values/api/operation/DualParameterOperation.java)
  represents an operation that takes in two parameters in addition to the source parameter

## [`Modifier`](../values-api/src/main/java/me/datafox/dfxengine/values/api/Modifier.java)

`Modifier` is an interface that represents a math operation that can be calculated automatically. It has a priority 
which determines the order of operations between multiple modifiers. A modifier can be attached to values and value maps
to dynamically modify their values when retrieved. There are two default implementations of modifier:
* [`OperationModifier`](src/main/java/me/datafox/dfxengine/values/modifier/OperationModifier.java) wraps an operation
  and value parameters. All parameter numerals are retrieved with `Value.getValue()`, so all modifiers attached to those
  values are also taken into account
* [`MappingOperationModifier`](src/main/java/me/datafox/dfxengine/values/modifier/MappingOperationModifier.java) is the
  modifier equivalent of `MappingOperationChain`. It has value parameters and includes its own special values that wrap
  the special numerals, acquired with the `sourceValue()` and `resultValue(int)` static methods. Because all parameters
  are known ahead of calculation, the constructor throws `IllegalArgumentException` if future operations are referenced

## [`Modifiers`](src/main/java/me/datafox/dfxengine/values/utils/Modifiers.java)

`Modifiers` is a utility class that contains methods for creating operation modifiers based on the operation methods in
[`Operations`](../math/src/main/java/me/datafox/dfxengine/math/utils/Operations.java) from the Math module. It also has
some other helper methods for creating modifiers.

## [`Values`](src/main/java/me/datafox/dfxengine/values/utils/Values.java)

`Values` is a utility class that contains methods for creating values. It is very similar to 
[`Numerals`](../math/src/main/java/me/datafox/dfxengine/math/utils/Numerals.java) from the Math module.