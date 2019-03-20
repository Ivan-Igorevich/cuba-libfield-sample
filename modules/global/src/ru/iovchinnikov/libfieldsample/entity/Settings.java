package ru.iovchinnikov.libfieldsample.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|entity")
@Table(name = "LIBFIELDSAMPLE_SETTINGS")
@Entity(name = "libfieldsample$Settings")
public class Settings extends StandardEntity {
    private static final long serialVersionUID = -4478155422709116363L;

    @Column(name = "ENTITY", unique = true)
    protected String entity;

    @JoinTable(name = "LIBFIELDSAMPLE_SETTINGS_LIB_ENTITY_LINK",
        joinColumns = @JoinColumn(name = "SETTINGS_ID"),
        inverseJoinColumns = @JoinColumn(name = "LIB_ENTITY_ID"))
    @ManyToMany
    protected List<LibEntity> possible;

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }

    public void setPossible(List<LibEntity> possible) {
        this.possible = possible;
    }

    public List<LibEntity> getPossible() {
        return possible;
    }


}