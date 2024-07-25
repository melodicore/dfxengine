DFXEngine Text contains tools for generating text from various objects.

## [`TextFactory`](src/main/java/me/datafox/dfxengine/text/TextFactoryImpl.java)

`TextFactory` is a singleton class that builds strings from `Text` objects and contains references to various tools to
use in generation. Use the `build(Text)` and `build(Text...)` methods for generation.

## [`TextConfiguration`](src/main/java/me/datafox/dfxengine/text/utils/TextConfigurationImpl.java)

`TextConfiguration` is a configuration object that is used by all configurable classes of the module. `ConfigurationKey`
is used to determine the type and default value of a configuration entry, and values to configuration entries may be
static objects or a `Supplier<T>`. Configuration keys specific to a single class are contained in the class, and generic
keys are in the `ConfigurationKeys` utility class. These generic keys are:
* `ConfigurationKey<String> DELIMITER` determines the delimiter used in normal concatenation. The default value is ` `
* `ConfigurationKey<String> LIST_DELIMITER` determines the delimiter used in list concatenation, which is when the last
  delimiter should be different from the other ones. The default value is <code>,&nbsp;</code>
* `ConfigurationKey<String> LIST_LAST_DELIMITER` determines the last delimiter used in list concatenation. The default
  value is <code>&nbsp;and&nbsp;</code>
* `ConfigurationKey<Handle> NUMBER_FORMATTER` determines the `NumberFormatter` to be used when formatting numbers
* `ConfigurationKey<Handle> NUMBER_SUFFIX_FORMATTER` determines the `NumberSuffixFormatter` to be used by number
  formatters to separate a number to a mantissa and exponent

## [`Text`](../text-api/src/main/java/me/datafox/dfxengine/text/api/Text.java)

`Text` is an interface that contains a text generation method and possibly references to other objects to aid in
generation. There are multiple default implementations of text:

* [`StaticText`](src/main/java/me/datafox/dfxengine/text/text/StaticText.java) is the simplest text implementation. It
  takes in a string and returns it on generation
* [`ConfigurationText<T>`](src/main/java/me/datafox/dfxengine/text/text/ConfigurationText.java) takes in a
  `ConfigurationKey<T>` and a `Function<T,String>`, and returns the configuration value ran through the function on
  generation
* [`NameText<T>`](src/main/java/me/datafox/dfxengine/text/text/NameText.java) takes in a `Supplier<T>` and returns the
  name for the supplied object on generation. Name is resolved with `TextFactory.getName(T)`. It has one
  `ConfigurationKey<Boolean>` called `USE_PLURAL` which uses the plural form of the name if `true`. The default value is
  `false`
* [`NumberText`](src/main/java/me/datafox/dfxengine/text/text/NumberText.java) takes in a `Supplier<Number>` and returns
  the formatted version of the supplied number on generation. The number is formatted using the
  `ConfigurationKeys.NUMBER_FORMATTER` and `ConfigurationKeys.NUMBER_SUFFIX_FORMATTER` configuration keys
* [`NumeralText`](src/main/java/me/datafox/dfxengine/text/text/NumeralText.java) is the same as `NumberText`, but works
  on numerals from the [Math](../math) module instead of regular numbers
* [`ValueText`](src/main/java/me/datafox/dfxengine/text/text/ValueText.java) is the same as `NumberText`, but works on
  values from the [Values](../values) module instead of regular numbers. It also has a `ConfigurationKey<Boolean>`
  called `USE_MODIFIED`, which uses the `Value.getValue()` method if `true` and the `Value.getBase()` method if `false`.
  The default value is `true`
* [`ChainedText`](src/main/java/me/datafox/dfxengine/text/text/ChainedText.java) takes in any amount of other text
  objects and chains them together using delimiters. It has one `ConfigurationKey<Boolean>` called `USE_LIST_MODIFIER`
  which uses `ConfigurationKeys.LIST_DELIMITER` and `ConfigurationKeys.LIST_LAST_DELIMITER` if `true` and
  `ConfigurationKeys.DELIMITER` if `false`. The default value is `false`

## [`NumberFormatter`](../text-api/src/main/java/me/datafox/dfxengine/text/api/NumberFormatter.java)

`NumberFormatter` is an interface that formats a `BigDecimal` number to a string representation. It uses a
`NumberSuffixFormatter` to separate the number into mantissa and exponent parts when necessary. There are three default
implementations of number formatter:

* [`SimpleNumberFormatter`](src/main/java/me/datafox/dfxengine/text/formatter/SimpleNumberFormatter.java) is the
  simplest number formatter. It has three configuration keys:
    * `ConfigurationKey<Integer> PRECISION` determines the precision of the formatted number. This is directly
      equivalent to `MathContext.getPrecision()`. The default value is 6
    * `ConfigurationKey<Integer> MIN_EXPONENT` determines the minimum power of ten that the number must be before a
      `NumberSuffixFormatter` is used to generate an exponential suffix for the number. This value must be smaller than
      or equal to `PRECISION`. The default value is 3
    * `ConfigurationKey<Boolean> STRIP_ZEROS` determines if trailing zeros after a decimal point should be stripped. The
      default value is `true`
* [`EvenLengthNumberFormatter`](src/main/java/me/datafox/dfxengine/text/formatter/EvenLengthNumberFormatter.java) works
  mostly in the same way as the `SimpleNumberFormatter`, but tries to format all numbers to take the same amount of
  characters. It has three configuration keys:
    * `ConfigurationKey<Integer> LENGTH` determines how many characters the resulting string should be in length. Values
      smaller than 7 are not recommended. The default value is 8
    * `ConfigurationKey<Integer> MIN_EXPONENT` determines the minimum power of ten that the number must be before a
      `NumberSuffixFormatter` is used to generate an exponential suffix for the number. This value must be smaller than
      or equal to `LENGTH`. The default value is 3
    * `ConfigurationKey<Boolean> PAD_ZEROS` determines if numbers resulting in fewer characters than `LENGHT` should be
      padded with a decimal separator (if one is not present already) and trailing zeros. The default value is `true`
* [`SplittingNumberFormatter`](src/main/java/me/datafox/dfxengine/text/formatter/SplittingNumberFormatter.java) splits
  a number into multiple parts. This is most useful when formatting durations of time. It has four configuration keys:
    * `ConfigurationKey<Split[]> SPLITS` determines how numbers are split. Every split object contains a number
      multiplier and a suffix in singular and plural form. The splits must be in ascending order based on the
      multiplier, and the first split's multiplier is recommended to be 1. The default value is `TIME_SHORT`. Two
      preset split arrays are defined:
        * `TIME_SHORT` splits a number of seconds into different units of time ranging from seconds to years. Months are
          assumed to be 30 days in length, and years 365 days in length. The units are abbreviated as their name's first
          character, except for months which are abbreviated with `mo` to avoid confusion with minutes
        * `TIME_LONG` is the same as `TIME_SHORT` but uses full names for the suffixes
    * `ConfigurationKey<Handle> FORMATTER` determines which `NumberFormatter` is to be used for each individual split.
      The default value is the handle for `SimpleNumberFormatter`
    * `ConfigurationKey<Boolean> ROUND_SMALLEST` determines if the least significant split should be rounded down to
      an integer value. The default value is `true`
    * `ConfigurationKey<Boolean> USE_LIST_MODIFIER` determines if `ConfigurationKeys.LIST_DELIMITER` and
      `ConfigurationKeys.LIST_LAST_DELIMITER` should be used instead of `ConfigurationKeys.DELIMITER`. The default value
      is `true`

## [`NumberSuffixFormatter`](../text-api/src/main/java/me/datafox/dfxengine/text/api/NumberSuffixFormatter.java)

`NumberSuffixFormatter` is an interface that separates a `BigDecimal` number into a mantissa and an exponent. It is used
by number formatters. There are three default implementations of number suffix formatter:
* [`ExponentSuffixFormatter`](src/main/java/me/datafox/dfxengine/text/suffix/ExponentSuffixFormatter.java) splits a 
  number into a mantissa and an exponent based on two configuration keys:
    * `ConfigurationKey<Integer> INTERVAL` determines the interval of exponents. For values larger than 1, the 
      exponent is rounded down to the nearest multiple of the interval, and the mantissa is scaled accordingly. A value
      of 3 is equivalent to engineering representation. Values smaller than ` are not allowed. The default value is 1
    * `ConfigurationKey<Boolean> EXPONENT_PLUS` determines if the exponent should be prefixed by a plus when it is 
      positive. The default value is `false`
* [`CharDigitSuffixFormatter`](src/main/java/me/datafox/dfxengine/text/suffix/CharDigitSuffixFormatter.java) is 
  otherwise the same as the `ExponentSuffixFormatter`, but instead of regular digits, it represents the exponent in an
  arbitrary base with any characters as digits. It has three configuration keys:
    * `ConfigurationKey<char[]> CHARACTERS` determines the characters to be used as digits. It defaults to `ALPHABET`, 
      which contains the 26 English alphabet in lowercase
    * `ConfigurationKey<Integer> INTERVAL` determines the interval of exponents. It works otherwise in the same way as
      `ExponentSuffixFormatter`'s `INTERVAL`, but for values larger than 1 the exponent doesn't represent powers of 
      10, but powers of 10<sup>`INTERVAL`</sup>. For example, formatting the number 52500 with default characters 
      and interval 1 would result in `5.25d`, and with interval 3 the result would be `52.5a`. The default value is 1
    * `ConfigurationKey<Boolean> EXPONENT_PLUS` determines if the exponent should be prefixed by a plus when it is 
      positive. The default value is `false`
* [`NamedSuffixFormatter`](src/main/java/me/datafox/dfxengine/text/suffix/NamedSuffixFormatter.java) is otherwise the 
  same as the `CharDigitSuffixFormatter`, but it uses string suffixes instead of exponents. It has three configuration 
  keys:
    * `ConfigurationKey<String[]> SUFFIXES` determines the suffixes to be used. The default value is `SHORT`. Three 
      preset split arrays are defined:
        * `SI` contains abbreviated SI unit prefixes up to `Q` (Quetta, 10<sup>30</sup>)
        * `SHORT` contains abbreviated names of powers of 1000 in the short scale, up to `NOg` (Novenoctogintillion, 
          10<sup>270</sup>)
        * `LONG` contains abbreviated names of powers of 1000 in the long scale, up to `QaQD` 
          (Quattuorquadragintilliard, 10<sup>267</sup>)
    * `ConfigurationKey<Integer> INTERVAL` determines the interval of exponents, in the same way as 
      `CharDigitSuffixFormatter` does. The default suffixes should all use a value of 3. The default value is 3

## [`NameConverter<T>`](../text-api/src/main/java/me/datafox/dfxengine/text/api/NameConverter.java)

`NameConverter<T>` is an interface that converts an object to a singular and optionally also a plural name. The name
converter is used by `NameText<T>`. There are two default implementations of name converter:
* [`HandleNameConverter`](src/main/java/me/datafox/dfxengine/text/converter/HandleNameConverter.java) converts a handle
  to a name by using its id. It is not plural capable
* [`HandledNameConverter`](src/main/java/me/datafox/dfxengine/text/converter/HandledNameConverter.java) converts a
  handled object to a name by using its handle's id. It is not plural capable

## [`PluralConverter`](../text-api/src/main/java/me/datafox/dfxengine/text/api/PluralConverter.java)

`PluralConverter` is an interface that converts a singular string into plural form. The 
[default implementation](src/main/java/me/datafox/dfxengine/text/converter/DefaultPluralConverter.java) has three rules:
* If the string ends in `s`, `x` or `ch`, `es` is added to the end of the string
* If the string ends in a consonant followed by `y`, the `y` is stripped and `ies` is added to the end of the string
* In all other cases, `s` is added to the end of the string