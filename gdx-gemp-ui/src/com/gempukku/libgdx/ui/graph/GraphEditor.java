package com.gempukku.libgdx.ui.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.ui.DisposableTable;
import com.gempukku.libgdx.undo.*;
import com.gempukku.libgdx.ui.graph.data.*;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraph;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultNodeGroup;
import com.gempukku.libgdx.ui.graph.editor.*;
import com.gempukku.libgdx.ui.graph.validator.GraphValidationResult;
import com.gempukku.libgdx.ui.preview.NavigableCanvas;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.gempukku.libgdx.undo.event.UndoableChangeListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class GraphEditor extends DisposableTable implements NavigableCanvas {
    private static final float CANVAS_GAP = 50f;

    private static final Color INVALID_LABEL_COLOR = Color.RED;
    private static final Color WARNING_LABEL_COLOR = Color.GOLDENROD;
    private static final Color VALID_LABEL_COLOR = Color.WHITE;
    private static final float NODE_GROUP_PADDING = 4f;

    private final GraphEditorStyle style;
    private final Function<String, GraphNodeEditorProducer> graphNodeEditorProducers;
    private final PopupMenuProducer popupMenuProducer;
    private final ConvertToGraphChangedListener convertToGraphChangedListener = new ConvertToGraphChangedListener();

    private ShapeRenderer shapeRenderer;

    private final Map<NodeConnector, Shape> connectionNodeMap = new HashMap<>();
    private final Map<DrawnGraphConnection, Shape> connections = new HashMap<>();

    private final ObjectSet<String> selectedNodes = new ObjectSet<>();

    private final DefaultGraph<GraphNodeWindow, DrawnGraphConnection, RectangleNodeGroup> editedGraph;
    private ObjectMap<String, Vector2> windowPositionsAtDragStart;

    private float canvasX;
    private float canvasY;
    private float canvasWidth;
    private float canvasHeight;
    private boolean canvasMoving;
    private boolean moveGroup = true;
    private NodeConnector drawingFromConnector;

    public GraphEditor(Graph graph, Function<String, GraphNodeEditorProducer> graphNodeEditorProducers, final PopupMenuProducer popupMenuProducer) {
        this(graph, graphNodeEditorProducers, popupMenuProducer, "default");
    }

    public GraphEditor(Graph graph, Function<String, GraphNodeEditorProducer> graphNodeEditorProducers, final PopupMenuProducer popupMenuProducer,
                       String styleName) {
        this(graph, graphNodeEditorProducers, popupMenuProducer, VisUI.getSkin().get(styleName, GraphEditorStyle.class));
    }

    public GraphEditor(Graph graph, Function<String, GraphNodeEditorProducer> graphNodeEditorProducers, final PopupMenuProducer popupMenuProducer,
                       final GraphEditorStyle style) {
        this.style = style;
        this.graphNodeEditorProducers = graphNodeEditorProducers;
        this.popupMenuProducer = popupMenuProducer;

        editedGraph = new DefaultGraph<>(graph.getType());

        for (GraphNode node : graph.getNodes()) {
            addGraphNode(node.getId(), node.getType(), node.getData(), node.getX(), node.getY());
        }

        for (GraphConnection connection : graph.getConnections()) {
            addGraphConnection(connection.getNodeFrom(), connection.getFieldFrom(), connection.getNodeTo(), connection.getFieldTo());
        }

        for (NodeGroup group : graph.getGroups()) {
            addNodeGroup(group.getName(), group.getNodeIds());
        }

        setClip(true);
        setTouchable(Touchable.enabled);

        addListener(
                new ClickListener(Input.Buttons.RIGHT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        processRightClick(x, y);
                    }
                });
        addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        processLeftClick(x, y);
                    }
                });
        DragListener dragListener = new DragListener() {
            private float canvasXStart;
            private float canvasYStart;
            private RectangleNodeGroup dragGroup;
            private float movedByX = 0;
            private float movedByY = 0;

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                if (event.getTarget() == GraphEditor.this) {
                    canvasXStart = canvasX;
                    canvasYStart = canvasY;
                    movedByX = 0;
                    movedByY = 0;
                    dragGroup = findDragGroup(x, y);
                    windowsStartingToMove();
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                }
            }

            private RectangleNodeGroup findDragGroup(float x, float y) {
                for (RectangleNodeGroup group : editedGraph.getGroups()) {
                    Rectangle rectangle = group.getRectangle();
                    if (rectangle.contains(x, y) && y > rectangle.y + rectangle.height - style.groupNameFont.getLineHeight()) {
                        // Hit the label
                        return group;
                    }
                }
                return null;
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (event.getTarget() == GraphEditor.this) {
                    if (dragGroup != null) {
                        float moveByX = x - getDragStartX() - movedByX;
                        float moveByY = y - getDragStartY() - movedByY;
                        moveGroup = false;
                        for (String nodeId : dragGroup.getNodeIds()) {
                            editedGraph.getNodeById(nodeId).moveBy(moveByX, moveByY);
                        }
                        moveGroup = true;
                        movedByX += moveByX;
                        movedByY += moveByY;
                        windowsMoved();
                        updateNodeGroups();
                    } else {
                        navigateTo(canvasXStart + getDragStartX() - x, canvasYStart + getDragStartY() - y);
                    }
                }
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                if (event.getTarget() == GraphEditor.this) {
                    if (dragGroup != null) {
                        dragGroup = null;
                    }
                    windowsFinishedToMove();
                    windowsMoved();
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                }
            }
        };
        dragListener.setTapSquareSize(0f);
        addListener(dragListener);

        updateCanvas(true);
    }

    private void windowsStartingToMove() {
        // Snapshot position of all windows
        windowPositionsAtDragStart = new ObjectMap<>();
        for (GraphNodeWindow node : editedGraph.getNodes()) {
            Vector2 windowPosition = Pools.obtain(Vector2.class).set(canvasX + node.getX(), canvasY + node.getY());
            windowPositionsAtDragStart.put(node.getId(), windowPosition);
        }
    }

    private void windowsFinishedToMove() {
        if (windowPositionsAtDragStart != null) {
            CompositeUndoableAction undoableAction = new CompositeUndoableAction();
            for (GraphNodeWindow node : editedGraph.getNodes()) {
                Vector2 oldPosition = windowPositionsAtDragStart.get(node.getId());
                if (oldPosition != null) {
                    Vector2 newPosition = Pools.obtain(Vector2.class).set(canvasX + node.getX(), canvasY + node.getY());
                    if (!oldPosition.equals(newPosition)) {
                        MoveNodeAction moveNodeAction = new MoveNodeAction(node, oldPosition, newPosition);
                        undoableAction.addUndoableAction(moveNodeAction);
                    } else {
                        Pools.free(oldPosition);
                        Pools.free(newPosition);
                    }
                }
            }
            windowPositionsAtDragStart = null;
            if (undoableAction.hasActions()) {
                DecoratedUndoableAction decorated = new DecoratedUndoableAction(undoableAction);
                WindowsMovedAction windowsMovedAction = new WindowsMovedAction();
                decorated.setAfterRedo(windowsMovedAction);
                decorated.setAfterUndo(windowsMovedAction);
                graphChanged(false, false, decorated);
            }
        }
    }

    private void graphChanged(boolean structure, boolean data, UndoableAction undoableAction) {
        GraphChangedEvent event = Pools.obtain(GraphChangedEvent.class);
        event.setStructure(structure);
        event.setData(data);
        event.setUndoableAction(undoableAction);
        fire(event);
        Pools.free(event);
    }

    public Graph getGraph() {
        return editedGraph;
    }

    public void addGraphNode(String nodeId, String type, JsonValue data, float x, float y) {
        GraphNodeEditorProducer graphNodeEditorProducer = graphNodeEditorProducers.evaluate(type);
        GraphNodeEditor pipelineGraphBox = graphNodeEditorProducer.createNodeEditor(data);
        final GraphNodeWindow graphNodeWindow = new GraphNodeWindow(nodeId, pipelineGraphBox,
                graphNodeEditorProducer.getName(), style.windowTitleAlignment, style.windowStyle, style.windowCloseButtonStyle) {
            @Override
            protected void positionChanged(float fromX, float fromY, float toX, float toY) {
                graphWindowMoved(this, fromX, fromY, toX, toY);
            }

            @Override
            protected void close() {
                removeGraphNodeWindow(this);
                remove();
            }

            @Override
            public void toFront() {
                super.toFront();
                String nodeId = getId();
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    if (selectedNodes.contains(nodeId))
                        removeFromSelection(nodeId);
                    else
                        addToSelection(nodeId);
                } else {
                    setSelection(nodeId);
                }
            }
        };
        graphNodeWindow.addListener(convertToGraphChangedListener);
        graphNodeWindow.addListener(
                new InputListener() {
                    private boolean dragging;

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if (!dragging && graphNodeWindow.isDragging()) {
                            dragging = true;
                            windowsStartingToMove();
                        }
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        if (dragging && !graphNodeWindow.isDragging()) {
                            dragging = false;
                            windowsFinishedToMove();
                        }
                    }
                });
        graphNodeWindow.setKeepWithinStage(false);
        if (graphNodeEditorProducer.isCloseable()) {
            graphNodeWindow.addCloseButton();
        }

        AddGraphNodeAction addNodeAction = new AddGraphNodeAction(graphNodeWindow, x + canvasX, y + canvasY);
        addNodeAction.doAction();
        graphChanged(true, true, addNodeAction);
    }

    public void addNodeGroup(String name, ObjectSet<String> nodeIds) {
        final RectangleNodeGroup nodeGroup = new RectangleNodeGroup(new DefaultNodeGroup(name, nodeIds));
        AddNodeGroupAction addGroupAction = new AddNodeGroupAction(nodeGroup);
        addGroupAction.doAction();
        graphChanged(false, false, addGroupAction);
    }

    public void addGraphConnection(String fromNode, String fromField, String toNode, String toField) {
        final DrawnGraphConnection graphConnection = new DrawnGraphConnection(fromNode, fromField, toNode, toField);
        AddGraphConnectionAction addConnectionAction = new AddGraphConnectionAction(graphConnection);
        addConnectionAction.doAction();
        graphChanged(true, false, addConnectionAction);
    }

    public void centerCanvas() {
        navigateTo((getWidth() - canvasWidth) / 2f, (getHeight() - canvasHeight) / 2f);
    }

    @Override
    public void getCanvasPosition(Vector2 result) {
        result.set(canvasX, canvasY);
    }

    @Override
    public void getCanvasSize(Vector2 result) {
        result.set(canvasWidth, canvasHeight);
    }

    @Override
    public void getVisibleSize(Vector2 result) {
        result.set(getWidth(), getHeight());
    }

    @Override
    public void processElements(Callback callback) {
        for (GraphNodeWindow window : editedGraph.getNodes()) {
            callback.processElement(canvasX + window.getX(), canvasY + window.getY(), window.getWidth(), window.getHeight());
        }
    }

    @Override
    public void navigateTo(float x, float y) {
        x = MathUtils.round(x);
        y = MathUtils.round(y);

        canvasMoving = true;
        float difX = x - canvasX;
        float difY = y - canvasY;
        for (Actor element : editedGraph.getNodes()) {
            element.moveBy(-difX, -difY);
        }
        canvasX = x;
        canvasY = y;
        canvasMoving = false;

        windowsMoved();
    }

    private void updateCanvas(boolean adjustPosition) {
        if (!canvasMoving) {
            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = Float.MIN_VALUE;
            float maxY = Float.MIN_VALUE;

            if (!editedGraph.getNodes().iterator().hasNext()) {
                minX = 0;
                minY = 0;
                maxX = 0;
                maxY = 0;
            } else {
                for (Actor child : editedGraph.getNodes()) {
                    float childX = child.getX();
                    float childY = child.getY();
                    float childWidth = child.getWidth();
                    float childHeight = child.getHeight();
                    minX = Math.min(minX, childX);
                    minY = Math.min(minY, childY);
                    maxX = Math.max(maxX, childX + childWidth);
                    maxY = Math.max(maxY, childY + childHeight);
                }
            }

            minX -= CANVAS_GAP;
            minY -= CANVAS_GAP;
            maxX += CANVAS_GAP;
            maxY += CANVAS_GAP;

            canvasWidth = maxX - minX;
            canvasHeight = maxY - minY;

//            if (adjustPosition) {
//                canvasX = -minX;
//                canvasY = -minY;
//            }
        }
    }

    public void setValidationResult(GraphValidationResult validationResult) {
        for (GraphNodeWindow window : editedGraph.getNodes()) {
            window.clearConnectorErrors();
            if (validationResult.getErrorNodes().contains(window.getId())) {
                window.getTitleLabel().setColor(INVALID_LABEL_COLOR);
            } else if (validationResult.getWarningNodes().contains(window.getId())) {
                window.getTitleLabel().setColor(WARNING_LABEL_COLOR);
            } else {
                window.getTitleLabel().setColor(VALID_LABEL_COLOR);
            }
        }

        for (NodeConnector errorConnector : validationResult.getErrorConnectors()) {
            editedGraph.getNodeById(errorConnector.getNodeId()).setConnectorError(errorConnector.getFieldId());
        }

        for (DrawnGraphConnection connection : editedGraph.getConnections()) {
            connection.setError(validationResult.getErrorConnections().contains(connection));
        }
    }

    private void processRightClick(float x, float y) {
        if (!containedInWindow(x, y)) {
            RectangleNodeGroup nodeGroup = null;
            for (RectangleNodeGroup nodeGroupValue : editedGraph.getGroups()) {
                Rectangle rectangle = nodeGroupValue.getRectangle();
                if (rectangle.contains(x, y) && y > rectangle.y + rectangle.height - style.groupNameFont.getLineHeight()) {
                    // Hit the label
                    nodeGroup = nodeGroupValue;
                    break;
                }
            }
            if (nodeGroup != null) {
                final RectangleNodeGroup finalNodeGroup = nodeGroup;

                PopupMenu popupMenu = new PopupMenu();
                MenuItem rename = new MenuItem("Rename group");
                rename.addListener(
                        new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                Dialogs.showInputDialog(getStage(), "Enter group name", "Name",
                                        new InputValidator() {
                                            @Override
                                            public boolean validateInput(String input) {
                                                return !input.trim().isEmpty();
                                            }
                                        },
                                        new InputDialogListener() {
                                            @Override
                                            public void finished(String input) {
                                                final String oldName = finalNodeGroup.getName();
                                                final String newName = input.trim();
                                                RenameGroupAction renameGroupAction = new RenameGroupAction(finalNodeGroup, oldName, newName);
                                                renameGroupAction.doAction();
                                                graphChanged(false, false, renameGroupAction);
                                            }

                                            @Override
                                            public void canceled() {

                                            }
                                        });
                            }
                        });
                popupMenu.addItem(rename);

                MenuItem remove = new MenuItem("Remove group");
                remove.addListener(
                        new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                RemoveGroupAction removeGroupAction = new RemoveGroupAction(finalNodeGroup);
                                removeGroupAction.doAction();
                                graphChanged(false, false, removeGroupAction);
                            }
                        });
                popupMenu.addItem(remove);

                popupMenu.showMenu(getStage(), x + getX(), y + getY());
            } else {
                PopupMenu popupMenu = popupMenuProducer.createPopupMenu(x, y);
                popupMenu.showMenu(getStage(), x + getX(), y + getY());
            }
        }
    }

    private void processLeftClick(float x, float y) {
        if (containedInWindow(x, y))
            return;

        for (Map.Entry<NodeConnector, Shape> nodeEntry : connectionNodeMap.entrySet()) {
            if (nodeEntry.getValue().contains(x, y)) {
                processNodeClick(nodeEntry.getKey());
                return;
            }
        }

        for (Map.Entry<DrawnGraphConnection, Shape> connectionEntry : connections.entrySet()) {
            if (connectionEntry.getValue().contains(x, y)) {
                removeConnection(connectionEntry.getKey());
                return;
            }
        }

        drawingFromConnector = null;
    }

    private boolean containedInWindow(float x, float y) {
        for (VisWindow window : editedGraph.getNodes()) {
            float x1 = window.getX();
            float y1 = window.getY();
            float width = window.getWidth();
            float height = window.getHeight();
            // If window contains it - return
            if (x >= x1 && x < x1 + width
                    && y >= y1 && y < y1 + height)
                return true;
        }
        return false;
    }

    private void removeConnection(final DrawnGraphConnection connection) {
        RemoveConnectionAction removeConnectionAction = new RemoveConnectionAction(connection);
        removeConnectionAction.doAction();
        graphChanged(true, false, removeConnectionAction);
    }

    private void processNodeClick(NodeConnector clickedNodeConnector) {
        GraphNodeEditor clickedNode = getGraphNodeEditorById(clickedNodeConnector.getNodeId());
        if (drawingFromConnector != null) {
            if (!drawingFromConnector.equals(clickedNodeConnector)) {
                GraphNodeEditor drawingFromNode = getGraphNodeEditorById(drawingFromConnector.getNodeId());

                boolean drawingFromIsInput = drawingFromNode.getInputs().containsKey(drawingFromConnector.getFieldId());
                if (drawingFromIsInput == clickedNode.getConfiguration().getNodeInputs().containsKey(clickedNodeConnector.getFieldId())) {
                    drawingFromConnector = null;
                } else {
                    NodeConnector connectorFrom = drawingFromIsInput ? clickedNodeConnector : drawingFromConnector;
                    NodeConnector connectorTo = drawingFromIsInput ? drawingFromConnector : clickedNodeConnector;

                    GraphNodeOutput output = getGraphNodeEditorById(connectorFrom.getNodeId()).getConfiguration().getNodeOutputs().get(connectorFrom.getFieldId());
                    GraphNodeInput input = getGraphNodeEditorById(connectorTo.getNodeId()).getConfiguration().getNodeInputs().get(connectorTo.getFieldId());

                    if (!connectorsMatch(input, output)) {
                        // Either input-input, output-output, or different property type
                        drawingFromConnector = null;
                    } else {
                        // Remove conflicting connections if needed
                        if (!input.acceptsMultipleConnections()) {
                            for (DrawnGraphConnection oldConnection : findNodeConnections(connectorTo)) {
                                removeConnection(oldConnection);
                            }
                        }
                        if (!output.acceptsMultipleConnections()) {
                            for (DrawnGraphConnection oldConnection : findNodeConnections(connectorFrom)) {
                                removeConnection(oldConnection);
                            }
                        }
                        addGraphConnection(connectorFrom.getNodeId(), connectorFrom.getFieldId(),
                                connectorTo.getNodeId(), connectorTo.getFieldId());
                        drawingFromConnector = null;
                    }
                }
            } else {
                // Same node, that started at
                drawingFromConnector = null;
            }
        } else {
            boolean input = clickedNode.getConfiguration().getNodeInputs().containsKey(clickedNodeConnector.getFieldId());
            if ((input && !clickedNode.getConfiguration().getNodeInputs().get(clickedNodeConnector.getFieldId()).acceptsMultipleConnections())
                    || (!input && !clickedNode.getConfiguration().getNodeOutputs().get(clickedNodeConnector.getFieldId()).acceptsMultipleConnections())) {
                Array<DrawnGraphConnection> nodeConnections = findNodeConnections(clickedNodeConnector);
                if (nodeConnections.size > 0) {
                    DrawnGraphConnection oldConnection = nodeConnections.get(0);
                    removeConnection(oldConnection);
                    NodeConnector oldNode = getNodeInfo(oldConnection.getNodeFrom(), oldConnection.getFieldFrom());
                    if (oldNode.equals(clickedNodeConnector))
                        drawingFromConnector = getNodeInfo(oldConnection.getNodeTo(), oldConnection.getFieldTo());
                    else
                        drawingFromConnector = oldNode;
                } else {
                    drawingFromConnector = clickedNodeConnector;
                }
            } else {
                drawingFromConnector = clickedNodeConnector;
            }
        }
    }

    private boolean connectorsMatch(GraphNodeInput input, GraphNodeOutput output) {
        Array<String> producablePropertyTypes = output.getConnectableFieldTypes();
        for (String acceptedPropertyType : input.getConnectableFieldTypes()) {
            if (producablePropertyTypes.contains(acceptedPropertyType, false))
                return true;
        }

        return false;
    }

    private Array<DrawnGraphConnection> findNodeConnections(NodeConnector nodeConnector) {
        String nodeId = nodeConnector.getNodeId();
        String fieldId = nodeConnector.getFieldId();

        Array<DrawnGraphConnection> result = new Array<>();
        for (DrawnGraphConnection graphConnection : editedGraph.getConnections()) {
            if ((graphConnection.getNodeFrom().equals(nodeId) && graphConnection.getFieldFrom().equals(fieldId))
                    || (graphConnection.getNodeTo().equals(nodeId) && graphConnection.getFieldTo().equals(fieldId)))
                result.add(graphConnection);
        }
        return result;
    }

    private void graphWindowMoved(GraphNodeWindow window, float fromX, float fromY, float toX, float toY) {
        if (moveGroup && !canvasMoving) {
            // This flag prevents recursive moves
            moveGroup = false;
            for (String selectedNode : selectedNodes) {
                if (!selectedNode.equals(window.getId())) {
                    editedGraph.getNodeById(selectedNode).moveBy(toX - fromX, toY - fromY);
                }
            }
            moveGroup = true;
            windowsMoved();
        }
    }

    private void windowsMoved() {
        recreateClickableShapes();
        updateNodeGroups();
        updateCanvas(true);
        graphChanged(false, false, null);
    }

    private void removeFromSelection(String nodeId) {
        selectedNodes.remove(nodeId);
        updateSelectedVisuals();
    }

    private void addToSelection(String nodeId) {
        selectedNodes.add(nodeId);
        updateSelectedVisuals();
    }

    private void setSelection(String nodeId) {
        selectedNodes.clear();
        selectedNodes.add(nodeId);
        updateSelectedVisuals();
    }

    private void updateSelectedVisuals() {
        VisWindow.WindowStyle notSelectedStyle = style.windowStyle;
        VisWindow.WindowStyle selectedStyle = style.windowSelectedStyle != null ? style.windowSelectedStyle : style.windowStyle;

        for (GraphNodeWindow window : editedGraph.getNodes()) {
            VisWindow.WindowStyle newStyle = selectedNodes.contains(window.getId()) ? selectedStyle : notSelectedStyle;
            window.setStyle(newStyle);
        }
    }

    public ObjectSet<String> getSelectedNodes() {
        return selectedNodes;
    }

    private void removeGraphNodeWindow(final GraphNodeWindow window) {
        final String nodeId = window.getId();

        CompositeUndoableAction compositeUndoableAction = new CompositeUndoableAction();

        // Remove all connections to window
        for (DrawnGraphConnection graphConnection : editedGraph.getConnections()) {
            if (graphConnection.getNodeFrom().equals(nodeId)
                    || graphConnection.getNodeTo().equals(nodeId)) {
                compositeUndoableAction.addUndoableAction(new RemoveConnectionAction(graphConnection));
            }
        }

        // Remove window from group
        for (final RectangleNodeGroup nodeGroup : editedGraph.getGroups()) {
            if (nodeGroup.getNodeIds().contains(nodeId)) {
                compositeUndoableAction.addUndoableAction(new RemoveNodeFromGroupAction(window, nodeGroup));
                break;
            }
        }

        compositeUndoableAction.addUndoableAction(new RemoveGraphNodeAction(window, window.getX() + canvasX, window.getY() + canvasY));
        compositeUndoableAction.doAction();

        graphChanged(true, true, compositeUndoableAction);
    }

    @Override
    public void layout() {
        super.layout();
        recreateClickableShapes();
        updateNodeGroups();
        updateCanvas(false);
    }

    private void updateNodeGroups() {
        for (RectangleNodeGroup nodeGroup : editedGraph.getGroups()) {
            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = -Float.MAX_VALUE;
            float maxY = -Float.MAX_VALUE;

            for (String nodeId : nodeGroup.getNodeIds()) {
                GraphNodeWindow window = editedGraph.getNodeById(nodeId);
                if (window == null)
                    throw new IllegalStateException("Unable to find node with id: " + nodeId);
                float windowX = window.getX();
                float windowY = window.getY();
                float windowWidth = window.getWidth();
                float windowHeight = window.getHeight();
                minX = Math.min(minX, windowX);
                minY = Math.min(minY, windowY);
                maxX = Math.max(maxX, windowX + windowWidth);
                maxY = Math.max(maxY, windowY + windowHeight);
            }

            minX -= style.groupGap;
            minY -= style.groupGap;
            maxX += style.groupGap;
            maxY += style.groupGap + style.groupNameFont.getLineHeight();
            nodeGroup.getRectangle().set(minX, minY, maxX - minX, maxY - minY);
        }
    }

    private void recreateClickableShapes() {
        connectionNodeMap.clear();
        connections.clear();

        Vector2 from = new Vector2();
        for (GraphNodeWindow window : editedGraph.getNodes()) {
            String nodeId = window.getId();
            GraphNodeEditor graphNodeEditor = window.getGraphNodeEditor();
            float windowX = window.getX();
            float windowY = window.getY();
            for (GraphNodeEditorInput connector : graphNodeEditor.getInputs().values()) {
                switch (connector.getSide()) {
                    case Left:
                        from.set(windowX - style.connectorLength, windowY + connector.getOffset());
                        break;
                    case Top:
                        from.set(windowX + connector.getOffset(), windowY + window.getHeight() + style.connectorLength);
                        break;
                }
                Rectangle2D rectangle = new Rectangle2D.Float(
                        from.x - style.connectorRadius, from.y - style.connectorRadius,
                        style.connectorRadius * 2, style.connectorRadius * 2);

                connectionNodeMap.put(new NodeConnector(nodeId, connector.getFieldId()), rectangle);
            }
            for (GraphNodeEditorOutput connector : graphNodeEditor.getOutputs().values()) {
                switch (connector.getSide()) {
                    case Right:
                        from.set(windowX + window.getWidth() + style.connectorLength, windowY + connector.getOffset());
                        break;
                    case Bottom:
                        from.set(windowX + connector.getOffset(), windowY - style.connectorLength);
                        break;
                }
                Rectangle2D rectangle = new Rectangle2D.Float(
                        from.x - style.connectorRadius, from.y - style.connectorRadius,
                        style.connectorRadius * 2, style.connectorRadius * 2);

                connectionNodeMap.put(new NodeConnector(nodeId, connector.getFieldId()), rectangle);
            }
        }

        BasicStroke basicStroke = new BasicStroke(7);
        Vector2 to = new Vector2();
        for (DrawnGraphConnection graphConnection : editedGraph.getConnections()) {
            NodeConnector fromNode = getNodeInfo(graphConnection.getNodeFrom(), graphConnection.getFieldFrom());
            GraphNodeWindow fromWindow = editedGraph.getNodeById(fromNode.getNodeId());
            GraphNodeEditorOutput output = getGraphNodeEditorById(fromNode.getNodeId()).getOutputs().get(fromNode.getFieldId());
            calculateConnection(from, fromWindow, output);
            NodeConnector toNode = getNodeInfo(graphConnection.getNodeTo(), graphConnection.getFieldTo());
            GraphNodeWindow toWindow = editedGraph.getNodeById(toNode.getNodeId());
            GraphNodeEditorInput input = getGraphNodeEditorById(toNode.getNodeId()).getInputs().get(toNode.getFieldId());
            calculateConnection(to, toWindow, input);
            Shape shape;
            if (output.getSide() == GraphNodeOutputSide.Right) {
                shape = basicStroke.createStrokedShape(new CubicCurve2D.Float(from.x, from.y, ((from.x + to.x) / 2), from.y, ((from.x + to.x) / 2), to.y, to.x, to.y));
            } else {
                shape = basicStroke.createStrokedShape(new CubicCurve2D.Float(from.x, from.y, from.x, ((from.y + to.y) / 2), to.x, ((from.y + to.y) / 2), to.x, to.y));
            }
            connections.put(graphConnection, shape);
        }
    }

    private NodeConnector getNodeInfo(String nodeId, String fieldId) {
        GraphNodeEditor graphBox = editedGraph.getNodeById(nodeId).getGraphNodeEditor();
        if (graphBox.getInputs().get(fieldId) != null || graphBox.getOutputs().get(fieldId) != null)
            return new NodeConnector(nodeId, fieldId);
        throw new IllegalArgumentException("Unable to find node connector");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();
        style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
        batch.flush();
        if (clipBegin()) {
            drawGroups(batch);
            batch.flush();
            clipEnd();
        }
        batch.end();
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        drawConnections();
        batch.begin();
        super.draw(batch, parentAlpha);
    }

    private void drawGroups(Batch batch) {
        if (editedGraph.getGroups().iterator().hasNext()) {
            float x = getX();
            float y = getY();

            batch.setColor(Color.WHITE);
            for (RectangleNodeGroup nodeGroup : editedGraph.getGroups()) {
                Rectangle rectangle = nodeGroup.getRectangle();
                style.groupBackground.draw(batch, x + rectangle.x, y + rectangle.y, rectangle.width, rectangle.height);
            }

            batch.setColor(style.groupNameColor);
            for (RectangleNodeGroup nodeGroup : editedGraph.getGroups()) {
                Rectangle rectangle = nodeGroup.getRectangle();
                String name = nodeGroup.getName();
                style.groupNameFont.draw(batch, name, x + rectangle.x + NODE_GROUP_PADDING, y + rectangle.y + rectangle.height - NODE_GROUP_PADDING,
                        0, name.length(), rectangle.width - NODE_GROUP_PADDING * 2, Align.center, false, "...");
            }
        }
    }

    private void drawConnections() {
        float x = getX();
        float y = getY();

        Vector2 from = new Vector2();
        Vector2 to = new Vector2();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(style.connectionColor);

        for (GraphNodeWindow window : editedGraph.getNodes()) {
            GraphNodeEditor graphBox = window.getGraphNodeEditor();
            for (GraphNodeInput connector : graphBox.getConfiguration().getNodeInputs().values()) {
                if (!connector.isRequired()) {
                    String fieldId = connector.getFieldId();
                    calculateConnector(from, to, window, graphBox.getInputs().get(fieldId));
                    from.add(x, y);
                    to.add(x, y);

                    shapeRenderer.line(from, to);
                    shapeRenderer.circle(from.x, from.y, style.connectorRadius);
                }
            }

            for (GraphNodeEditorOutput connector : graphBox.getOutputs().values()) {
                calculateConnector(from, to, window, connector);
                from.add(x, y);
                to.add(x, y);

                shapeRenderer.line(from, to);
                shapeRenderer.circle(from.x, from.y, style.connectorRadius);
            }
        }

        for (DrawnGraphConnection graphConnection : editedGraph.getConnections()) {
            NodeConnector fromNode = getNodeInfo(graphConnection.getNodeFrom(), graphConnection.getFieldFrom());
            GraphNodeWindow fromWindow = editedGraph.getNodeById(fromNode.getNodeId());
            GraphNodeEditorOutput output = getGraphNodeEditorById(fromNode.getNodeId()).getOutputs().get(fromNode.getFieldId());
            calculateConnection(from, fromWindow, output);
            NodeConnector toNode = getNodeInfo(graphConnection.getNodeTo(), graphConnection.getFieldTo());
            GraphNodeWindow toWindow = editedGraph.getNodeById(toNode.getNodeId());
            GraphNodeEditorInput input = getGraphNodeEditorById(toNode.getNodeId()).getInputs().get(toNode.getFieldId());
            calculateConnection(to, toWindow, input);

            shapeRenderer.setColor(graphConnection.isError() ? getInvalidConnectorColor() : style.connectorColor);

            from.add(x, y);
            to.add(x, y);
            if (output.getSide() == GraphNodeOutputSide.Right) {
                shapeRenderer.curve(from.x, from.y, ((from.x + to.x) / 2), from.y, ((from.x + to.x) / 2), to.y, to.x, to.y, 100);
            } else {
                shapeRenderer.curve(from.x, from.y, from.x, ((from.y + to.y) / 2), to.x, ((from.y + to.y) / 2), to.x, to.y, 100);
            }

        }

        if (drawingFromConnector != null) {
            shapeRenderer.setColor(style.connectionColor);
            GraphNodeEditor drawingFromNode = getGraphNodeEditorById(drawingFromConnector.getNodeId());
            String nodeId = drawingFromConnector.getNodeId();
            VisWindow fromWindow = editedGraph.getNodeById(nodeId);
            if (drawingFromNode.getConfiguration().getNodeInputs().containsKey(drawingFromConnector.getFieldId())) {
                GraphNodeEditorInput input = drawingFromNode.getInputs().get(drawingFromConnector.getFieldId());
                calculateConnection(from, fromWindow, input);
                Vector2 mouseLocation = screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY())).add(x, y);
                if (input.getSide() == GraphNodeInputSide.Left) {
                    shapeRenderer.curve(x + from.x, y + from.y, ((x + from.x + mouseLocation.x) / 2), y + from.y,
                            ((x + from.x + mouseLocation.x) / 2), mouseLocation.y, mouseLocation.x, mouseLocation.y, 100);
                } else {
                    shapeRenderer.curve(x + from.x, y + from.y, x + from.x, ((y + from.y + mouseLocation.y) / 2),
                            mouseLocation.x, ((y + from.y + mouseLocation.y) / 2), mouseLocation.x, mouseLocation.y, 100);
                }
            } else {
                GraphNodeEditorOutput output = drawingFromNode.getOutputs().get(drawingFromConnector.getFieldId());
                calculateConnection(from, fromWindow, output);
                Vector2 mouseLocation = screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY())).add(x, y);
                if (output.getSide() == GraphNodeOutputSide.Right) {
                    shapeRenderer.curve(x + from.x, y + from.y, ((x + from.x + mouseLocation.x) / 2), y + from.y,
                            ((x + from.x + mouseLocation.x) / 2), mouseLocation.y, mouseLocation.x, mouseLocation.y, 100);
                } else {
                    shapeRenderer.curve(x + from.x, y + from.y, x + from.x, ((y + from.y + mouseLocation.y) / 2),
                            mouseLocation.x, ((y + from.y + mouseLocation.y) / 2), mouseLocation.x, mouseLocation.y, 100);
                }
            }
        }

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        for (GraphNodeWindow window : editedGraph.getNodes()) {
            GraphNodeEditor graphBox = window.getGraphNodeEditor();
            for (GraphNodeInput connector : graphBox.getConfiguration().getNodeInputs().values()) {
                if (connector.isRequired()) {
                    String fieldId = connector.getFieldId();
                    calculateConnector(from, to, window, graphBox.getInputs().get(fieldId));
                    from.add(x, y);
                    to.add(x, y);

                    shapeRenderer.setColor(window.isConnectorError(fieldId) ? getInvalidConnectorColor() : style.connectorColor);

                    shapeRenderer.line(from, to);
                    shapeRenderer.circle(from.x, from.y, style.connectorRadius);
                }
            }
        }
        shapeRenderer.end();
    }

    private Color getInvalidConnectorColor() {
        return (style.invalidConnectorColor != null) ? style.invalidConnectorColor : style.connectorColor;
    }

    private void calculateConnector(Vector2 from, Vector2 to, VisWindow window, GraphNodeEditorOutput connector) {
        float windowX = window.getX();
        float windowY = window.getY();
        switch (connector.getSide()) {
            case Right:
                from.set(windowX + window.getWidth() + style.connectorLength, windowY + connector.getOffset());
                to.set(windowX + window.getWidth(), windowY + connector.getOffset());
                break;
            case Bottom:
                from.set(windowX + connector.getOffset(), windowY - style.connectorLength);
                to.set(windowX + connector.getOffset(), windowY);
                break;
        }
    }

    private void calculateConnector(Vector2 from, Vector2 to, VisWindow window, GraphNodeEditorInput connector) {
        float windowX = window.getX();
        float windowY = window.getY();
        switch (connector.getSide()) {
            case Left:
                from.set(windowX - style.connectorLength, windowY + connector.getOffset());
                to.set(windowX, windowY + connector.getOffset());
                break;
            case Top:
                from.set(windowX + connector.getOffset(), windowY + window.getHeight() + style.connectorLength);
                to.set(windowX + connector.getOffset(), windowY + window.getHeight());
                break;
        }
    }

    private GraphNodeEditor getGraphNodeEditorById(String id) {
        return editedGraph.getNodeById(id).getGraphNodeEditor();
    }

    private void calculateConnection(Vector2 position, VisWindow window, GraphNodeEditorInput connector) {
        float windowX = window.getX();
        float windowY = window.getY();
        switch (connector.getSide()) {
            case Left:
                position.set(windowX - style.connectorLength, windowY + connector.getOffset());
                break;
            case Top:
                position.set(windowX + connector.getOffset(), windowY + window.getHeight() + style.connectorLength);
                break;
        }
    }

    private void calculateConnection(Vector2 position, VisWindow window, GraphNodeEditorOutput connector) {
        float windowX = window.getX();
        float windowY = window.getY();
        switch (connector.getSide()) {
            case Right:
                position.set(windowX + window.getWidth() + style.connectorLength, windowY + connector.getOffset());
                break;
            case Bottom:
                position.set(windowX + connector.getOffset(), windowY - style.connectorLength);
                break;
        }
    }

    @Override
    protected void initializeWidget() {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }

    @Override
    protected void disposeWidget() {
        shapeRenderer.dispose();
        shapeRenderer = null;
    }

    public static class GraphEditorStyle {
        public Drawable background;
        public Window.WindowStyle windowStyle;
        public Window.WindowStyle windowSelectedStyle = null; // optional - defaults to windowStyle
        public VisImageButton.VisImageButtonStyle windowCloseButtonStyle;
        public int windowTitleAlignment = 1;
        // Group style
        public Drawable groupBackground;
        public float groupGap = 10;
        public BitmapFont groupNameFont;
        public Color groupNameColor;
        // Connection style
        public float connectorLength = 10;
        public float connectorRadius = 5;
        public Color connectionColor = Color.WHITE;
        public Color connectorColor = Color.WHITE;
        public Color invalidConnectorColor = null; // optional - defaults to connectorColor
    }

    private class AddGraphNodeAction extends DefaultUndoableAction {
        private final GraphNodeWindow graphNodeWindow;
        private final float x;
        private final float y;

        public AddGraphNodeAction(GraphNodeWindow graphNodeWindow, float x, float y) {
            this.graphNodeWindow = graphNodeWindow;
            this.x = x;
            this.y = y;
        }

        @Override
        public void undoAction() {
            graphNodeWindow.remove();
            editedGraph.removeGraphNode(graphNodeWindow);
            selectedNodes.remove(graphNodeWindow.getId());
            graphChanged(true, true, null);
        }

        @Override
        public void redoAction() {
            addActor(graphNodeWindow);
            graphNodeWindow.setPosition(x - canvasX, y - canvasX);
            graphNodeWindow.setSize(Math.max(150, graphNodeWindow.getPrefWidth()), graphNodeWindow.getPrefHeight());
            editedGraph.addGraphNode(graphNodeWindow);
            graphChanged(true, true, null);
        }
    }

    private class AddNodeGroupAction extends DefaultUndoableAction {
        private RectangleNodeGroup nodeGroup;

        public AddNodeGroupAction(RectangleNodeGroup nodeGroup) {
            this.nodeGroup = nodeGroup;
        }

        @Override
        public void undoAction() {
            editedGraph.removeNodeGroup(nodeGroup);
        }

        @Override
        public void redoAction() {
            editedGraph.addNodeGroup(nodeGroup);
            updateNodeGroups();
        }
    }

    private class AddGraphConnectionAction extends DefaultUndoableAction {
        private DrawnGraphConnection graphConnection;

        public AddGraphConnectionAction(DrawnGraphConnection graphConnection) {
            this.graphConnection = graphConnection;
        }

        @Override
        public void undoAction() {
            editedGraph.removeGraphConnection(graphConnection);
            graphChanged(true, false, null);
        }

        @Override
        public void redoAction() {
            editedGraph.addGraphConnection(graphConnection);
            graphChanged(true, false, null);
        }
    }

    private class RenameGroupAction extends DefaultUndoableAction {
        private RectangleNodeGroup nodeGroup;
        private String oldName;
        private String newName;

        public RenameGroupAction(RectangleNodeGroup nodeGroup, String oldName, String newName) {
            this.nodeGroup = nodeGroup;
            this.oldName = oldName;
            this.newName = newName;
        }

        @Override
        public void undoAction() {
            nodeGroup.setName(oldName);
        }

        @Override
        public void redoAction() {
            nodeGroup.setName(newName);
        }
    }

    private class RemoveGroupAction extends DefaultUndoableAction {
        private RectangleNodeGroup nodeGroup;

        public RemoveGroupAction(RectangleNodeGroup nodeGroup) {
            this.nodeGroup = nodeGroup;
        }

        @Override
        public void undoAction() {
            editedGraph.addNodeGroup(nodeGroup);
            updateNodeGroups();
        }

        @Override
        public void redoAction() {
            editedGraph.removeNodeGroup(nodeGroup);
        }
    }

    private class RemoveConnectionAction extends DefaultUndoableAction {
        private DrawnGraphConnection connection;

        public RemoveConnectionAction(DrawnGraphConnection connection) {
            this.connection = connection;
        }

        @Override
        public void undoAction() {
            editedGraph.addGraphConnection(connection);
            graphChanged(true, false, null);
        }

        @Override
        public void redoAction() {
            editedGraph.removeGraphConnection(connection);
            graphChanged(true, false, null);
        }
    }

    private class RemoveGraphNodeAction extends DefaultUndoableAction {
        private final GraphNodeWindow graphNodeWindow;
        private final float x;
        private final float y;

        public RemoveGraphNodeAction(GraphNodeWindow graphNodeWindow, float x, float y) {
            this.graphNodeWindow = graphNodeWindow;
            this.x = x;
            this.y = y;
        }

        @Override
        public void undoAction() {
            addActor(graphNodeWindow);
            graphNodeWindow.setPosition(x - canvasX, y - canvasY);
            graphNodeWindow.setSize(Math.max(150, graphNodeWindow.getPrefWidth()), graphNodeWindow.getPrefHeight());
            editedGraph.addGraphNode(graphNodeWindow);
            graphChanged(true, true, null);
        }

        @Override
        public void redoAction() {
            graphNodeWindow.remove();
            editedGraph.removeGraphNode(graphNodeWindow);
            selectedNodes.remove(graphNodeWindow.getId());
            graphChanged(true, true, null);
        }
    }

    private class RemoveNodeFromGroupAction extends DefaultUndoableAction {
        private GraphNodeWindow graphNodeWindow;
        private RectangleNodeGroup nodeGroup;

        public RemoveNodeFromGroupAction(GraphNodeWindow graphNodeWindow, RectangleNodeGroup nodeGroup) {
            this.graphNodeWindow = graphNodeWindow;
            this.nodeGroup = nodeGroup;
        }

        @Override
        public void undoAction() {
            if (nodeGroup.getNodeIds().isEmpty()) {
                editedGraph.addNodeGroup(nodeGroup);
            }
            nodeGroup.getNodeIds().add(graphNodeWindow.getId());
        }

        @Override
        public void redoAction() {
            nodeGroup.getNodeIds().remove(graphNodeWindow.getId());
            if (nodeGroup.getNodeIds().isEmpty()) {
                editedGraph.removeNodeGroup(nodeGroup);
            }
        }
    }

    private class MoveNodeAction extends DefaultUndoableAction {
        private GraphNodeWindow node;
        private Vector2 oldPosition;
        private Vector2 newPosition;

        public MoveNodeAction(GraphNodeWindow node, Vector2 oldPosition, Vector2 newPosition) {
            this.node = node;
            this.oldPosition = oldPosition;
            this.newPosition = newPosition;
        }

        @Override
        public void undoAction() {
            moveGroup = false;
            node.setPosition(oldPosition.x - canvasX, oldPosition.y - canvasY);
            moveGroup = true;
        }

        @Override
        public void redoAction() {
            moveGroup = false;
            node.setPosition(newPosition.x - canvasX, newPosition.y - canvasY);
            moveGroup = true;
        }
    }

    private class WindowsMovedAction implements Runnable {
        @Override
        public void run() {
            windowsMoved();
        }
    }

    private class ConvertToGraphChangedListener extends UndoableChangeListener {
        @Override
        public void changed(UndoableChangeEvent event) {
            GraphChangedEvent graphChangedEvent = Pools.obtain(GraphChangedEvent.class);
            graphChangedEvent.setData(true);
            graphChangedEvent.setUndoableAction(event.getUndoableAction());
            fire(graphChangedEvent);
            Pools.free(graphChangedEvent);

            event.stop();
        }
    }
}
