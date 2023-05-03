# gdx-gemp-lib
This repository contains a set of small modules, which are too small to be separated into their own projects.

## Modules

### gdx-gemp-ai
A skeleton of Finite State machine implementation.

### gdx-gemp-box2d-artemis
Integrating Artemis ODB with Box2D for Physics simulation.

### gdx-gemp-camera2d
Configurable camera controller for 2D games. The camera's behavior is controlled via defined constraints.

### gdx-gemp-common
Set of tiny classes, which are used across many of other modules.

### gdx-gemp-lib-artemis
A plethora of utility Artemis ODB systems to use in your games. They handle a lof of things: AI, animation, audio, camera,
events, font loading, entity hierarchies, input, mouse picking, selection, spawning of entities, texture access
and entity Matrix4 transformations.

### gdx-gemp-network-artemis
Networking library for communication of Artemis ODB entity systems between server and clients.

### gdx-gemp-template
Library for loading JSON templates that allow to include other JSON files in it. This could be used to split commonly
used JSON fragments, or to facilitate "inheritance" of objects defined in the JSON files.

### gdx-gemp-template-artemis
Integration module for gdx-gemp-template with Artemis ODB.

### gdx-gemp-template-ashley
Integration module for gdx-gemp-template with Ashley.

### gdx-gemp-ui
A set of UI components to use in your projects. Most notably, it contains:
* Curve Editor - widget that allows to edit a non-smooth curve along the X axis,
* Gradient Editor - widget that allows to edit a gradient with multiple color points, 
* Graph Editor - widget that allows to create/edit non-specific graphs with nodes and connections, nodes can have any
number of input and output connectors, nodes can be grouped, supports graph validation,
* Preview Widget - widget rendering a preview of elements represented as a set of rectangles, on a larger canvas, which
also allows to navigate on that canvas,
* Tabbed Pane - cleaned up and much more configurable version of TabbedPane from VisUI, 
* set of Undoable UI elements (elements firing UndoableChangeEvent instead of ChangeEvent) for use with gdx-gemp-undo
module.

### gdx-gemp-undo
An undo/redo lightweight implementation for libGDX.

## Test Modules
Modules not designed to be used in your application, but might be a good place to see how to use the modules in this
repository.

### gdx-gemp-camera2d-test
A module containing an application demonstrating how to use gdx-gemp-camera2d. It also uses gdx-gemp-template-ashley module
for entity loading.

### gdx-gemp-ui-test
A module containing an application used for testing the UI elements in gdx-gemp-ui module.

---
For documentation, check the author's website:
http://www.gempukkustudio.com/libraries/gdx-gemp-lib.html
