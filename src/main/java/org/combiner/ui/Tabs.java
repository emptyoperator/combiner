package org.combiner.ui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class Tabs {
    public static <T> List<Tab> toTabs(Collection<T> c, Function<T, Tab> toTab) {
        return c.stream().map(toTab).toList();
    }

    public static Optional<Tab> getTabById(TabPane tabPane, String id) {
        return tabPane.getTabs().stream().filter(tab -> tab.getId().equals(id)).findAny();
    }

    public static Optional<Tab> getTabByName(TabPane tabPane, String name) {
        return tabPane.getTabs().stream().filter(tab -> tab.getText().equals(name)).findAny();
    }

    public static TabPane tabPane(Collection<Tab> tabs, Supplier<Tab> tabSupplier) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        tabPane.getTabs().addAll(tabs);
        tabPane.getTabs().add(newTabButton(tabPane, tabSupplier));
        return tabPane;
    }

    private static Tab newTabButton(TabPane tabPane, Supplier<Tab> tabSupplier) {
        Tab addTab = new Tab("+");
        addTab.setClosable(false);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == addTab) {
                tabPane.getTabs().add(tabPane.getTabs().size() - 1, tabSupplier.get());
                tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2);
            }
        });
        return addTab;
    }
}
