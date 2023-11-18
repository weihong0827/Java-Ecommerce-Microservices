CREATE TABLE undo_log (
                          id BIGSERIAL NOT NULL,
                          branch_id bigint NOT NULL,
                          xid varchar(100) NOT NULL,
                          context varchar(128) NOT NULL,
                          rollback_info BYTEA NOT NULL,
                          log_status int NOT NULL,
                          log_created timestamp without time zone NOT NULL,
                          log_modified timestamp without time zone NOT NULL,
                          PRIMARY KEY (id),
                          UNIQUE (xid, branch_id)
);
