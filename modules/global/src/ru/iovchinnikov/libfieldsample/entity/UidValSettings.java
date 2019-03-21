package ru.iovchinnikov.libfieldsample.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|entity")
@Table(name = "LIBFIELDSAMPLE_UID_VAL_SETTINGS")
@Entity(name = "libfieldsample$UidValSettings")
public class UidValSettings extends StandardEntity {
    private static final long serialVersionUID = 8085328611300322081L;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTITY_ID", unique = true)
    protected ValueHolder entity;

    @JoinTable(name = "LIBFIELDSAMPLE_ID_VAL_SETTINGS_LIB_ENTITY_LINK",
        joinColumns = @JoinColumn(name = "ID_VAL_SETTINGS_ID"),
        inverseJoinColumns = @JoinColumn(name = "LIB_ENTITY_ID"))
    @ManyToMany
    protected List<LibEntity> possible;

    public void setEntity(ValueHolder entity) {
        this.entity = entity;
    }

    public ValueHolder getEntity() {
        return entity;
    }

    public void setPossible(List<LibEntity> possible) {
        this.possible = possible;
    }

    public List<LibEntity> getPossible() {
        return possible;
    }


}