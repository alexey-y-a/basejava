package ru.javawebinar.basejava.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.postgresql.util.PGobject;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.Section;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final Logger LOG = Logger.getLogger(SqlStorage.class.getName());
    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("SELECT * FROM resume WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume resume = new Resume(rs.getString("uuid").trim(), rs.getString("full_name"));
            String contactsJson = rs.getString("contacts");
            String sectionsJson = rs.getString("sections");

            LOG.info("Retrieved contacts JSON for uuid " + uuid + ": " + contactsJson);
            LOG.info("Retrieved sections JSON for uuid " + uuid + ": " + sectionsJson);

            if (contactsJson != null && !contactsJson.equals("{}")) {
                try {
                    Map<String, String> contactsMap = OBJECT_MAPPER.readValue(contactsJson, Map.class);
                    for (Map.Entry<String, String> entry : contactsMap.entrySet()) {
                        resume.setContact(ContactType.valueOf(entry.getKey()), entry.getValue());
                    }
                } catch (IOException e) {
                    throw new StorageException("Error deserializing contacts", uuid, e);
                }
            }

            if (sectionsJson != null && !sectionsJson.equals("{}")) {
                try {
                    Map<String, Map<String, Object>> sectionsMap = OBJECT_MAPPER.readValue(sectionsJson, Map.class);
                    for (Map.Entry<String, Map<String, Object>> entry : sectionsMap.entrySet()) {
                        SectionType type = SectionType.valueOf(entry.getKey());
                        Section section = Section.fromMap(type.name(), entry.getValue());
                        resume.setSection(type, section);
                    }
                } catch (IOException e) {
                    throw new StorageException("Error deserializing sections", uuid, e);
                }
            }
            return resume;
        });
    }

    @Override
    public void update(Resume r) {
        LOG.info("Executing update for resume: " + r.getUuid());
        sqlHelper.execute("UPDATE resume SET full_name = ?, contacts = ?, sections = ? WHERE uuid = ?", ps -> {
            ps.setString(1, r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            Map<SectionType, Section> sections = r.getSections();
            try {
                PGobject contactsJsonb = new PGobject();
                contactsJsonb.setType("jsonb");
                contactsJsonb.setValue(OBJECT_MAPPER.writeValueAsString(contacts));
                ps.setObject(2, contactsJsonb);

                PGobject sectionsJsonb = new PGobject();
                sectionsJsonb.setType("jsonb");
                sectionsJsonb.setValue(OBJECT_MAPPER.writeValueAsString(sections));
                ps.setObject(3, sectionsJsonb);

                ps.setString(4, r.getUuid());
            } catch (Exception e) {
                throw new StorageException("Error serializing contacts or sections", r.getUuid(), e);
            }
            LOG.info("Executing update query for uuid: " + r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        try {
            sqlHelper.execute("INSERT INTO resume (uuid, full_name, contacts, sections) VALUES (?, ?, ?, ?)", ps -> {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                Map<ContactType, String> contacts = r.getContacts();
                Map<SectionType, Section> sections = r.getSections();
                try {
                    PGobject contactsJsonb = new PGobject();
                    contactsJsonb.setType("jsonb");
                    contactsJsonb.setValue(OBJECT_MAPPER.writeValueAsString(contacts));
                    ps.setObject(3, contactsJsonb);

                    PGobject sectionsJsonb = new PGobject();
                    sectionsJsonb.setType("jsonb");
                    sectionsJsonb.setValue(OBJECT_MAPPER.writeValueAsString(sections));
                    ps.setObject(4, sectionsJsonb);
                } catch (Exception e) {
                    throw new StorageException("Error serializing contacts or sections", r.getUuid(), e);
                }
                ps.executeUpdate();
                return null;
            });
        } catch (StorageException e) {
            if (e.getCause() instanceof org.postgresql.util.PSQLException) {
                org.postgresql.util.PSQLException psqlException = (org.postgresql.util.PSQLException) e.getCause();
                if (psqlException.getSQLState().equals("23505")) {
                    throw new ExistStorageException(r.getUuid());
                }
            }
            throw e;
        }
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("SELECT * FROM resume ORDER BY full_name, uuid", ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> resumes = new ArrayList<>();
            while (rs.next()) {
                String uuid = rs.getString("uuid").trim();
                String fullName = rs.getString("full_name");
                Resume resume = new Resume(uuid, fullName);
                String contactsJson = rs.getString("contacts");
                String sectionsJson = rs.getString("sections");

                LOG.info("Retrieved contacts JSON for uuid " + uuid + ": " + contactsJson);
                LOG.info("Retrieved sections JSON for uuid " + uuid + ": " + sectionsJson);

                if (contactsJson != null && !contactsJson.equals("{}")) {
                    try {
                        Map<String, String> contactsMap = OBJECT_MAPPER.readValue(contactsJson, Map.class);
                        for (Map.Entry<String, String> entry : contactsMap.entrySet()) {
                            resume.setContact(ContactType.valueOf(entry.getKey()), entry.getValue());
                        }
                    } catch (IOException e) {
                        throw new StorageException("Error deserializing contacts", uuid, e);
                    }
                }

                if (sectionsJson != null && !sectionsJson.equals("{}")) {
                    try {
                        Map<String, Map<String, Object>> sectionsMap = OBJECT_MAPPER.readValue(sectionsJson, Map.class);
                        for (Map.Entry<String, Map<String, Object>> entry : sectionsMap.entrySet()) {
                            SectionType type = SectionType.valueOf(entry.getKey());
                            Section section = Section.fromMap(type.name(), entry.getValue());
                            resume.setSection(type, section);
                        }
                    } catch (IOException e) {
                        throw new StorageException("Error deserializing sections", uuid, e);
                    }
                }
                resumes.add(resume);
            }
            return resumes;
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT COUNT(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }
}
