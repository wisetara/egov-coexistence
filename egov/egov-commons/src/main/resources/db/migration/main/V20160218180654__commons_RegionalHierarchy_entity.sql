
create table EG_REGIONALHIERARCHY (
id bigint NOT NULL,
    code bigint not null,
    parent bigint,
    name character varying(512) NOT NULL,
    type bigint NOT NULL,
    ishistory boolean default false,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    version bigint
);
CREATE SEQUENCE seq_eg_regionlaHierarchy;
ALTER TABLE ONLY EG_REGIONALHIERARCHY ADD CONSTRAINT pk_eg_REGIONALHIERARCHY  PRIMARY KEY (id);
