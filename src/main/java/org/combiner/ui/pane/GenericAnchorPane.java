package org.combiner.ui.pane;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class GenericAnchorPane extends AnchorPane {
    public GenericAnchorPane(Node node) {
        super(setAnchors(node));
    }

    private static Node setAnchors(Node node) {
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        return node;
    }
}
