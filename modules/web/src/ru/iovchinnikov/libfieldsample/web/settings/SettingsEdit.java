package ru.iovchinnikov.libfieldsample.web.settings;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import ru.iovchinnikov.libfieldsample.entity.LibEntity;
import ru.iovchinnikov.libfieldsample.entity.Settings;
import ru.iovchinnikov.libfieldsample.entity.ValueHolder;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class SettingsEdit extends AbstractEditor<Settings> {

    @Inject private ComponentsFactory componentsFactory;
    @Inject private Datasource<Settings> settingsDs;
    @Inject private CollectionDatasource<ValueHolder, UUID> holdersDs;

    public Component entityGen(Datasource datasource, String fieldId) {
        LookupField field = componentsFactory.createComponent(LookupField.class);
        field.setDatasource(settingsDs, "entity");
        holdersDs.refresh();
        Map<String, String> map = new LinkedHashMap<>();
        holdersDs.getItems().forEach(holder -> map.put(holder.getName(), holder.getName()));
        field.setOptionsMap(map);
        field.setNullOptionVisible(false);
        return field;
    }
}