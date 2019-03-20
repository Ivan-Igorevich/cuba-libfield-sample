package ru.iovchinnikov.libfieldsample.web.valueholder;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.DataManager;
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
    @Inject private DataManager dataManager;

    private LookupField field;

    public Component fieldGen(Datasource datasource, String fieldId) {
        field = componentsFactory.createComponent(LookupField.class);
        field.setDatasource(valueHolderDs, "libValue");
        field.setNullOptionVisible(false);

//        field.setNewOptionAllowed(true);
//        field.setNewOptionHandler(caption -> {
//            LibEntity e = metadata.create(LibEntity.class);
//            e.setName(caption);
//            dataManager.commit(e);
//            field.setValue(e.getName());
//        });

        return field;
    }

    @Override
    public void ready() {
        super.ready();
        valueHolderDs.refresh();
        libEntitiesDs.refresh(ParamsMap.of("name", valueHolderDs.getItem().getName()));
        Map<String, String> map = new LinkedHashMap<>();
        libEntitiesDs.getItems().forEach(libEntity -> map.put(libEntity.getName(), libEntity.getName()));
        field.setOptionsMap(map);
    }
}
