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
CREATE TABLE "address" (
                           "id" SERIAL PRIMARY KEY,
                           "user_id" BIGINT,
                           "default_status" INTEGER CHECK (default_status IN (0, 1)),
                           "receive_name" VARCHAR(64),
                           "phone" VARCHAR(64),
                           "province" VARCHAR(64),
                           "city" VARCHAR(64),
                           "region" VARCHAR(64),
                           "detail_address" VARCHAR(200),
                           "create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON COLUMN "address"."user_id" IS 'User id';
COMMENT ON COLUMN "address"."default_status" IS 'Whether it is the default shipping address: 0->no; 1->yes';
COMMENT ON COLUMN "address"."receive_name" IS 'Receiver''s name';
COMMENT ON COLUMN "address"."phone" IS 'Receiver''s phone';
COMMENT ON COLUMN "address"."province" IS 'Province/municipality';
COMMENT ON COLUMN "address"."city" IS 'City';
COMMENT ON COLUMN "address"."region" IS 'District';
COMMENT ON COLUMN "address"."detail_address" IS 'Detailed address';
COMMENT ON COLUMN "users".name IS 'Nickname';
COMMENT ON COLUMN "users".pwd IS 'Password';
COMMENT ON COLUMN "users".head_img IS 'Profile Picture';
COMMENT ON COLUMN "users".slogan IS 'User Signature';
COMMENT ON COLUMN "users".sex IS '0 for female, 1 for male';
COMMENT ON COLUMN "users".points IS 'Points';
COMMENT ON COLUMN "users".mail IS 'Email';
COMMENT ON COLUMN "users".secret IS 'Salt, used for personal sensitive information processing';
