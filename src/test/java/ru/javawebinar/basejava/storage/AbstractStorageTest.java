package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.ResumeTestData;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.SectionType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractStorageTest {
    protected final Storage storage;

    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";

    protected static final Resume RESUME_1 = ResumeTestData.createResume(UUID_1, "Name1");
    protected static final Resume RESUME_2 = ResumeTestData.createResume(UUID_2, "Name2");
    protected static final Resume RESUME_3 = ResumeTestData.createResume(UUID_3, "Name3");
    protected static final Resume RESUME_4 = ResumeTestData.createResume(UUID_4, "Name4");

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    protected void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        Resume r1 = new Resume(RESUME_1.getUuid(), RESUME_1.getFullName());
        Resume r2 = new Resume(RESUME_2.getUuid(), RESUME_2.getFullName());
        Resume r3 = new Resume(RESUME_3.getUuid(), RESUME_3.getFullName());
        for (var entry : RESUME_1.getSections().entrySet()) {
            if (entry.getKey() != SectionType.EXPERIENCE && entry.getKey() != SectionType.EDUCATION) {
                r1.setSection(entry.getKey(), entry.getValue());
            }
        }
        for (var entry : RESUME_2.getSections().entrySet()) {
            if (entry.getKey() != SectionType.EXPERIENCE && entry.getKey() != SectionType.EDUCATION) {
                r2.setSection(entry.getKey(), entry.getValue());
            }
        }
        for (var entry : RESUME_3.getSections().entrySet()) {
            if (entry.getKey() != SectionType.EXPERIENCE && entry.getKey() != SectionType.EDUCATION) {
                r3.setSection(entry.getKey(), entry.getValue());
            }
        }
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        assertTrue(storage.getAllSorted().isEmpty());
    }

    @Test
    public void update() {
        Resume newResume = ResumeTestData.createResume(UUID_1, "Name1 Updated");
        Resume filteredResume = new Resume(newResume.getUuid(), newResume.getFullName());
        for (var entry : newResume.getSections().entrySet()) {
            if (entry.getKey() != SectionType.EXPERIENCE && entry.getKey() != SectionType.EDUCATION) {
                filteredResume.setSection(entry.getKey(), entry.getValue());
            }
        }
        storage.update(filteredResume);
        Resume retrieved = storage.get(UUID_1);
        assertEquals(filteredResume.getUuid(), retrieved.getUuid());
        assertEquals(filteredResume.getFullName(), retrieved.getFullName());
    }

    @Test
    public void get() {
        Resume resume1 = storage.get(UUID_1);
        assertEquals(RESUME_1.getUuid(), resume1.getUuid());
        assertEquals(RESUME_1.getFullName(), resume1.getFullName());
    }

    @Test
    public void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get("dummy"));
    }

    @Test
    public void save() {
        Resume r4 = new Resume(RESUME_4.getUuid(), RESUME_4.getFullName());
        for (var entry : RESUME_4.getSections().entrySet()) {
            if (entry.getKey() != SectionType.EXPERIENCE && entry.getKey() != SectionType.EDUCATION) {
                r4.setSection(entry.getKey(), entry.getValue());
            }
        }
        storage.save(r4);
        assertSize(4);
        Resume retrieved = storage.get(UUID_4);
        assertEquals(r4.getUuid(), retrieved.getUuid());
        assertEquals(r4.getFullName(), retrieved.getFullName());
    }

    @Test
    public void saveExist() {
        assertThrows(ExistStorageException.class, () -> storage.save(RESUME_1));
    }

    @Test
    public void updateNotExist() {
        Resume dummyResume = ResumeTestData.createResume("dummy", "Dummy Name");
        Resume filteredResume = new Resume(dummyResume.getUuid(), dummyResume.getFullName());
        for (var entry : dummyResume.getSections().entrySet()) {
            if (entry.getKey() != SectionType.EXPERIENCE && entry.getKey() != SectionType.EDUCATION) {
                filteredResume.setSection(entry.getKey(), entry.getValue());
            }
        }
        assertThrows(NotExistStorageException.class, () -> storage.update(filteredResume));
    }

    @Test
    public void delete() {
        storage.delete(UUID_1);
        assertSize(2);
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_1));
    }

    @Test
    public void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.delete("dummy"));
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> result = storage.getAllSorted();
        assertEquals(3, result.size());
        assertEquals(RESUME_1.getUuid(), result.get(0).getUuid());
        assertEquals(RESUME_1.getFullName(), result.get(0).getFullName());
        assertEquals(RESUME_2.getUuid(), result.get(1).getUuid());
        assertEquals(RESUME_2.getFullName(), result.get(1).getFullName());
        assertEquals(RESUME_3.getUuid(), result.get(2).getUuid());
        assertEquals(RESUME_3.getFullName(), result.get(2).getFullName());
    }

}