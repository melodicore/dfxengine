DFXEngine Math is a module containing numbers with automatic type promotion and various math operations.

## [`Numeral`](../math-api/src/main/java/me/datafox/dfxengine/math/api/Numeral.java)

`Numeral` is a type that can wrap six different number types. It also contains methods for conversion between these 
types. Numeral is immutable, and every operation on it creates a new instance, just like native Java number types. The 
six types are:
* [`IntNumeral`](src/main/java/me/datafox/dfxengine/math/numeral/IntNumeral.java), which is backed by an `int`
* [`LongNumeral`](src/main/java/me/datafox/dfxengine/math/numeral/LongNumeral.java), which is backed by a `long`
* [`BigIntNumeral`](src/main/java/me/datafox/dfxengine/math/numeral/BigIntNumeral.java), which is backed by a
`BigInteger`
* [`FloatNumeral`](src/main/java/me/datafox/dfxengine/math/numeral/FloatNumeral.java), which is backed by a `float`
* [`DoubleNumeral`](src/main/java/me/datafox/dfxengine/math/numeral/DoubleNumeral.java), which is backed by a `double`
* [`BigDecNumeral`](src/main/java/me/datafox/dfxengine/math/numeral/BigDecNumeral.java), which is backed by a 
`BigDecimal`

The numeral has various conversion methods. The methods do not allow conversions that would result in an integer 
overflow or an infinite floating point value, but do allow conversions from non-integers to integers. The methods are:
* `convert(NumeralType)` converts a numeral to the type if allowed, and throws `ExtendedArithmeticException` otherwise
* `convertIfAllowed(NumeralType)` converts a numeral to the type if allowed, and returns the same numeral otherwise
* `toInteger()` converts a numeral to the smallest allowed integer type 
* `toDecimal()` converts a numeral to the smallest allowed decimal type
* `toSmallestType()` converts a numeral to the smallest allowed type, but does not convert between integer and decimal 
types

Along these methods, the numeral also has methods for converting them to native Java number types. These methods throw
ExtendedArithmeticException if the numeral's value is out of bounds.

Numerals can be created with their constructors, but the `Numerals` utility contains various methods for creating 
numerals. Details of utility classes are explained below.

## Utility classes

### [`Conversion`](src/main/java/me/datafox/dfxengine/math/utils/Conversion.java)

`Conversion` contains methods for converting numerals into native Java number types. The conversion methods are used 
internally by the conversion methods within numerals.

### [`Numerals`](src/main/java/me/datafox/dfxengine/math/utils/Numerals.java)

`Numerals` contains methods for creating numerals from numbers, including string representations of numbers and 
arbitrary `Number` instances.

### [`Operations`](src/main/java/me/datafox/dfxengine/math/utils/Operations.java)

`Operations` contains a multitude of math operations for numerals. These operations automatically promote numeral types
to allow every operation to finish. The `MathContext` for `BigDecimal` operations can be chosen with 
`Operations.setContext(MathContext)` and the default value is `MathContext.DECIMAL128`.

List of operations: `add`, `subtract`, `multiply`, `divide`, `inverse`, `power`, `exp`, `sqrt`, `cbrt`, (nth) `root`, 
`log`, `log2`, `log10`, `logN`, `min`, `max` and `lerp`.

### [`Range`](src/main/java/me/datafox/dfxengine/math/utils/Range.java)

`Range` contains methods for checking if a number or numeral is within the allowed range of a numeral type.