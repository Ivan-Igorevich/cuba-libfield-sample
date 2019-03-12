-- begin LIBFIELDSAMPLE_LIB_ENTITY
create table LIBFIELDSAMPLE_LIB_ENTITY (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    DESCRIPTION varchar(255),
    --
    primary key (ID)
)^
-- end LIBFIELDSAMPLE_LIB_ENTITY
-- begin LIBFIELDSAMPLE_VALUE_HOLDER
create table LIBFIELDSAMPLE_VALUE_HOLDER (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    LIB_VALUE varchar(255),
    --
    primary key (ID)
)^
-- end LIBFIELDSAMPLE_VALUE_HOLDER
