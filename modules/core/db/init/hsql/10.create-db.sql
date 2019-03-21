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

-- begin LIBFIELDSAMPLE_SETTINGS
create table LIBFIELDSAMPLE_SETTINGS (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ENTITY varchar(255),
    --
    primary key (ID)
)^
-- end LIBFIELDSAMPLE_SETTINGS

-- begin LIBFIELDSAMPLE_SETTINGS_LIB_ENTITY_LINK
create table LIBFIELDSAMPLE_SETTINGS_LIB_ENTITY_LINK (
    SETTINGS_ID varchar(36) not null,
    LIB_ENTITY_ID varchar(36) not null,
    primary key (SETTINGS_ID, LIB_ENTITY_ID)
)^
-- end LIBFIELDSAMPLE_SETTINGS_LIB_ENTITY_LINK
-- begin LIBFIELDSAMPLE_UID_VAL_SETTINGS
create table LIBFIELDSAMPLE_UID_VAL_SETTINGS (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ENTITY_ID varchar(36),
    --
    primary key (ID)
)^
-- end LIBFIELDSAMPLE_UID_VAL_SETTINGS
-- begin LIBFIELDSAMPLE_ID_VAL_SETTINGS_LIB_ENTITY_LINK
create table LIBFIELDSAMPLE_ID_VAL_SETTINGS_LIB_ENTITY_LINK (
    ID_VAL_SETTINGS_ID varchar(36) not null,
    LIB_ENTITY_ID varchar(36) not null,
    primary key (ID_VAL_SETTINGS_ID, LIB_ENTITY_ID)
)^
-- end LIBFIELDSAMPLE_ID_VAL_SETTINGS_LIB_ENTITY_LINK
