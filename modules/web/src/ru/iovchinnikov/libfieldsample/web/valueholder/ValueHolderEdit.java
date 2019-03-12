package ru.iovchinnikov.libfieldsample.web.valueholder;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import ru.iovchinnikov.libfieldsample.entity.LibEntity;
import ru.iovchinnikov.libfieldsample.entity.ValueHolder;
import com.haulmont.cuba.gui.data.Datasource;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ValueHolderEdit extends AbstractEditor<ValueHolder> {

    @Inject private ComponentsFactory componentsFactory;
    @Inject private CollectionDatasource<LibEntity, UUID> libEntitiesDs;
    @Inject private Datasource<ValueHolder> valueHolderDs;
    @Inject private Metadata metadata;

    public Component fieldGen(Datasource datasource, String fieldId) {
        LookupField field = componentsFactory.createComponent(LookupField.class);
        field.setDatasource(valueHolderDs, "libValue");
        libEntitiesDs.refresh();
        Map<String, String> map = new LinkedHashMap<>();
        libEntitiesDs.getItems().forEach(libEntity -> map.put(libEntity.getName(), libEntity.getName()));
        field.setOptionsMap(map);
        field.setNullOptionVisible(false);
        field.setNewOptionAllowed(true);
        field.setNewOptionHandler(caption -> {
            LibEntity e = metadata.create(LibEntity.class);
            e.setName(caption);
            AbstractEditor ae = openEditor(e, WindowManager.OpenType.DIALOG);
            ae.addCloseWithCommitListener(() -> {
                field.setValue(e.getName());
            });
        });
        return field;
    }
}
