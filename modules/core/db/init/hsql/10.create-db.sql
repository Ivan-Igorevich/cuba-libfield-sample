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
-- begin LIBFIELDSAMPLE_FIXED_VALUE_HOLDER
create table LIBFIELDSAMPLE_FIXED_VALUE_HOLDER (
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
    --
    primary key (ID)
)^
-- end LIBFIELDSAMPLE_FIXED_VALUE_HOLDER
-- begin LIBFIELDSAMPLE_LIBRARY_SETTINGS
create table LIBFIELDSAMPLE_LIBRARY_SETTINGS (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    primary key (ID)
)^
-- end LIBFIELDSAMPLE_LIBRARY_SETTINGS
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
-- begin LIBFIELDSAMPLE_FIXED_VALUE_HOLDER_LIB_ENTITY_LINK
create table LIBFIELDSAMPLE_FIXED_VALUE_HOLDER_LIB_ENTITY_LINK (
    FIXED_VALUE_HOLDER_ID varchar(36) not null,
    LIB_ENTITY_ID varchar(36) not null,
    primary key (FIXED_VALUE_HOLDER_ID, LIB_ENTITY_ID)
)^
-- end LIBFIELDSAMPLE_FIXED_VALUE_HOLDER_LIB_ENTITY_LINK
-- begin LIBFIELDSAMPLE_SETTINGS_LIB_ENTITY_LINK
create table LIBFIELDSAMPLE_SETTINGS_LIB_ENTITY_LINK (
    SETTINGS_ID varchar(36) not null,
    LIB_ENTITY_ID varchar(36) not null,
    primary key (SETTINGS_ID, LIB_ENTITY_ID)
)^
-- end LIBFIELDSAMPLE_SETTINGS_LIB_ENTITY_LINK
