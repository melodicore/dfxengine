DFXEngine Handles is a module for creating and managing dynamic identifier handles. It consists of four main classes, as 
well as four handle-related collections, one configuration class and one extra interface from the API module.

## [`Handle`](src/main/java/me/datafox/dfxengine/handles/HandleImpl.java)

`Handle` is the main part of the Handles module. It is identified with a String id, and belongs in a `Space` that acts
as a namespace for handles. It may have any number of tags, which are handles stored in a special tag space. It may also
have any number of subhandles, which are handles that use a normal handle as an extra namespace. A handle may also 
belong in any number of `Groups`, which are also space-specific. It contains two methods for creating subhandles, 
`createSubHandle(String)` and `getOrCreateSubHandle(String)`. Their difference is that createSubHandle throws an 
exception if a subhandle with that id is already present in the handle, while getOrCreateSubHandle returns the existing 
handle if it is present. Handles extend `Comparable<Handle>` where the comparison of handles within the same space are
done by their index within that space, and handles in different spaces are ordered by the index of their respective 
space's identifying handle in the "space space".

## [`Space`](src/main/java/me/datafox/dfxengine/handles/SpaceImpl.java)

`Space` is a namespace for Handles. It may contain any number of `Handles`, as well as any number of `Groups`. A space 
is identified by a handle that is stored in a special "space space". A space has two methods for creating handles, 
`createHandle(String)` and `getOrCreateHandle(String)`, which work in the same way as the methods for creating 
subhandles, as explained above in the section for handles. Two similar methods exist for creating groups, 
`createGroup(String)` and `getOrCreateGroup(String)`.

## [`HandleManager`](src/main/java/me/datafox/dfxengine/handles/HandleManagerImpl.java)

`HandleManager` is a singleton class that manages all `Spaces` and `Handles`. It has two main methods, 
`createSpace(String)` and `getOrCreateSpace(String)` which work in the same way as the methods for creating subhandles, 
handles and groups, as explained above in the section for handles. It also contains references to the two special 
spaces, accessible with `getSpaceSpace()` and `getTagSpace()`.

## [`Group`](src/main/java/me/datafox/dfxengine/handles/GroupImpl.java)

`Groups` are extra containers of `Handles` that are managed by `Spaces`. Like spaces, groups are identified by a handle,
but these handles are always subhandles of the handle that identifies the group containing the group in question. 
Handles can be added to a group with `getHandles().add(Object)`, the details of which are explained below in the section
for `HandleSets`.

## `HandleSet`

`HandleSet` is an extension of `Set<Handle>` that may only contain `Handles` from one `Space`. In addition to normal set 
behavior, it also allows for strings to be given to all methods that take in arbitrary objects for elements 
(`remove(Object)`, `contains(Object)`, `removeAll(Collection<?>)`, `containsAll(Collection<?>)` and so on), where the
string is automatically resolved to a handle in the space. It also has an extra `add(String)` method, which 
automatically creates a handle in the space if it does not already exist. Keep in mind that handle sets for the special 
"space space" are not able to create handles automatically, since handles in that space are created exclusively when 
creating spaces or `Groups`.

Two implementations for `HandleSet` exist, 
[`HashHandleSet`](src/main/java/me/datafox/dfxengine/handles/HashHandleSet.java) and
[`TreeHandleSet`](src/main/java/me/datafox/dfxengine/handles/TreeHandleSet.java), which are backed by a `HashSet` and a
`TreeSet` respectively.

## `HandleMap<T>`

`HandleMap<T>` is an extension of `Map<Handle,T>` that may only contain `Handle` keys from one `Space`. Much like 
`HandleSet`, all of its methods that take in arbitrary objects for keys also take in strings, where the string is 
automatically resolved to a handle in the space.

Two implementations for `HandleMap<T>` exist,
[`HashHandleMap<T>`](src/main/java/me/datafox/dfxengine/handles/HashHandleMap.java) and
[`TreeHandleMap<T>`](src/main/java/me/datafox/dfxengine/handles/TreeHandleMap.java), which are backed by a `HashMap` and
a `TreeMap` respectively.

## [`HandleManagerConfiguration`](src/main/java/me/datafox/dfxengine/handles/HandleManagerConfiguration.java)

`HandleManagerConfiguration` is used to configure the `HandleManager` and by extension all other classes in the module.
Specifically, it determines whether sorted or unsorted collections should be used in various places of the module. The
options are:
* `orderedSpaces` determines if `Spaces` within the handle manager should be ordered. Default is `true`.
* `orderedHandlesInSpaces` determines if `Handles` within spaces should be ordered. Default is `true`.
* `orderedHandlesInGroups` determines if handles within `Groups` should be ordered. Default is `false`.
* `orderedGroups` determines if groups within spaces should be ordered. Default is `true`.
* `orderedSubHandles` determines if subhandles within handles should be ordered. Default is `true`.
* `orderedTags` determines if tag handles within handles should be ordered. Default is `false`.

## [`Handled`](../handles-api/src/main/java/me/datafox/dfxengine/handles/api/Handled.java)

`Handled` is an interface in the Handles API that indicates that its implementing class is identified by a `Handle`.
`Spaces` and `Groups` implement handled by default. `HandleMap` has a method `putHandled(T)` which puts an object into
the map based on its identifying handle. Handled also implements `Comparable<Handled>` that where the comparison is 
based on `Handle.compareTo(Handle)` using the identifying handles.