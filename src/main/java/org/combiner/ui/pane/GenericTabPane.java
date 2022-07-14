package org.combiner.ui.pane;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class GenericTabPane extends TabPane {
    public GenericTabPane() {
        setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
    }

    protected Optional<Tab> getTabByName(String name) {
        return getTabs().stream().filter(tab -> tab.getText().equals(name)).findAny();
    }

    protected <T> void setTabs(Collection<T> c, Function<T, Tab> toTab) {
        getTabs().addAll(toTabs(c, toTab));
    }

    private static <T> List<Tab> toTabs(Collection<T> c, Function<T, Tab> toTab) {
        return c.stream().map(toTab).toList();
    }

    protected void addNewTabButton(Supplier<Tab> tabSupplier) {
        getTabs().add(newTabButton(tabSupplier));
    }

    private Tab newTabButton(Supplier<Tab> tabSupplier) {
        Tab addTab = new Tab("+");
        addTab.setClosable(false);
        getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == addTab) {
                getTabs().add(getTabs().size() - 1, tabSupplier.get());
                getSelectionModel().select(getTabs().size() - 2);
            }
        });
        return addTab;
    }
}
