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
CREATE TABLE "users" (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(128) DEFAULT NULL,
                         pwd VARCHAR(124) DEFAULT NULL,
                         head_img VARCHAR(524) DEFAULT NULL,
                         slogan VARCHAR(524) DEFAULT NULL,
                         sex SMALLINT DEFAULT 1, -- 0 for female, 1 for male
                         points INT DEFAULT 0,
                         create_time TIMESTAMP DEFAULT NULL,
                         mail VARCHAR(64) UNIQUE DEFAULT NULL,
                         secret VARCHAR(12) DEFAULT NULL
);
COMMENT ON COLUMN "users".name IS 'Nickname';
COMMENT ON COLUMN "users".pwd IS 'Password';
COMMENT ON COLUMN "users".head_img IS 'Profile Picture';
COMMENT ON COLUMN "users".slogan IS 'User Signature';
COMMENT ON COLUMN "users".sex IS '0 for female, 1 for male';
COMMENT ON COLUMN "users".points IS 'Points';
COMMENT ON COLUMN "users".mail IS 'Email';
COMMENT ON COLUMN "users".secret IS 'Salt, used for personal sensitive information processing';
