CREATE TABLE resume (
    uuid VARCHAR(36) PRIMARY KEY,
    full_name VARCHAR(100)
);

CREATE TABLE contact (
    resume_uuid VARCHAR(36),
    type VARCHAR(50),
    value VARCHAR(255),
    FOREIGN KEY (resume_uuid) REFERENCES resume(uuid)
);

CREATE TABLE section (
    resume_uuid VARCHAR(36),
    type VARCHAR(50),
    content TEXT,
    FOREIGN KEY (resume_uuid) REFERENCES resume(uuid)
);