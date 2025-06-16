CREATE TABLE resume (
    uuid VARCHAR(36) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL
);

CREATE TABLE contact (
    resume_uuid VARCHAR(36) REFERENCES resume(uuid) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL,
    value VARCHAR(255) NOT NULL,
    PRIMARY KEY (resume_uuid, type)
);