package ru.iovchinnikov.libfieldsample.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|name")
@Table(name = "LIBFIELDSAMPLE_VALUE_HOLDER")
@Entity(name = "libfieldsample$ValueHolder")
public class ValueHolder extends StandardEntity {
    private static final long serialVersionUID = -3619531406110986284L;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "LIB_VALUE")
    protected String libValue;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setLibValue(String libValue) {
        this.libValue = libValue;
    }

    public String getLibValue() {
        return libValue;
    }


}