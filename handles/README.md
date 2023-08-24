DFXEngine Handles contains Handles, a data type meant for identification, containing a String
id and zero or more tags to identify each Handle. Tags themselves are also Handles. 

The module also contains Spaces, which work as namespaces for Handle ids, and the HandleManager,
a singleton class for manipulating Handles and Spaces. 

Finally, the module also contains two collection types, the TreeHandleSet and the
TreeHandleMap, which extend TreeSet and TreeMap implementations with various Handle-related
helper methods.

The HandleManager defines two hardcoded Spaces, one of which contains all Handles used to
identify Spaces themselves, and the other which contains all Handles used as tags.