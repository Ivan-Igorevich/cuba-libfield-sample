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

@NamePattern("%s|name")
@Table(name = "LIBFIELDSAMPLE_FIXED_VALUE_HOLDER")
@Entity(name = "libfieldsample$FixedValueHolder")
public class FixedValueHolder extends StandardEntity {
    private static final long serialVersionUID = 4180874235752102456L;

    @Column(name = "NAME")
    protected String name;

    @JoinTable(name = "LIBFIELDSAMPLE_FIXED_VALUE_HOLDER_LIB_ENTITY_LINK",
        joinColumns = @JoinColumn(name = "FIXED_VALUE_HOLDER_ID"),
        inverseJoinColumns = @JoinColumn(name = "LIB_ENTITY_ID"))
    @ManyToMany
    protected List<LibEntity> entries;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEntries(List<LibEntity> entries) {
        this.entries = entries;
    }

    public List<LibEntity> getEntries() {
        return entries;
    }


}