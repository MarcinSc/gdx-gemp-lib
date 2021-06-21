# libgdx-gemp-lib
An utility library for libgdx.

## How to start?
In your project, define the version of the library you want to use:
```
gdxGempLibVersion=[version]
```
Then add dependency:
```
api "com.gempukku.libgdx.lib:libgdx-gemp-lib:$gdxGempLibVersion"
```
If you want to use the Ashley integration, also add:
```
api "com.gempukku.libgdx.lib:libgdx-gemp-lib-ashley:$gdxGempLibVersion"
```

### Gwt
In the `html/build.gradle` file define the source version of the library:
```
api "com.gempukku.libgdx.lib:libgdx-gemp-lib:$gdxGempLibVersion:sources"
```
Then inside the `html/src/main/java/.../GdxDefinitions.gwt.xml` file add under the other `<inherits />`:
```xml
<inherits name="com.gempukku.libgdx.lib"/>
```
After that delete `html/build` and `html/war` and you should be good to go.

## Camera 2D
The package provides classes to easily setup a camera system for 2D games.  
Documentation available: [Camera 2D documentation](https://github.com/MarcinSc/libgdx-gemp-lib/wiki/Camera-2D)

## Json Templates
It's cool to keep the data separate from your code, and Json files is the standard for LibGDX. However,
do you really want to have SO MUCH duplication in those files? Json Templates comes to the rescue. It 
even comes with Ashley integration (optional).  
Documentation available: [Json Templates documentation](https://github.com/MarcinSc/libgdx-gemp-lib/wiki/Json-Templates)

## Finite State Machine
Finite state machines are very useful, for example for player state to manage animations, key input, etc.
The `FiniteStateMachine` class helps out with this. The class is simple enough, that no documentation
is available at the moment.
