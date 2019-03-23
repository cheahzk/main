package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyDaysKeeper;
import seedu.address.model.ReadOnlyTaskBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of Tasketch data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private DaysKeeperStorage daysKeeperStorage;
    private TaskBookStorage taskBookStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(TaskBookStorage taskBookStorage, DaysKeeperStorage daysKeeperStorage,
                          UserPrefsStorage userPrefsStorage) {
        super();
        this.daysKeeperStorage = daysKeeperStorage;
        this.taskBookStorage = taskBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ TaskBook methods ==============================

    @Override
    public Path getTaskBookFilePath() {
        return taskBookStorage.getTaskBookFilePath();
    }

    @Override
    public Optional<ReadOnlyTaskBook> readTaskBook() throws DataConversionException, IOException {
        return readTaskBook(taskBookStorage.getTaskBookFilePath());
    }

    @Override
    public Optional<ReadOnlyTaskBook> readTaskBook(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return taskBookStorage.readTaskBook(filePath);
    }

    @Override
    public void saveTaskBook(ReadOnlyTaskBook taskBook) throws IOException {
        saveTaskBook(taskBook, taskBookStorage.getTaskBookFilePath());
    }

    @Override
    public void saveTaskBook(ReadOnlyTaskBook taskBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        taskBookStorage.saveTaskBook(taskBook, filePath);
    }

    // ================ DaysKeeper methods ==============================

    @Override
    public Path getDaysKeeperFilePath() {
        return daysKeeperStorage.getDaysKeeperFilePath();
    }

    @Override
    public Optional<ReadOnlyDaysKeeper> readDaysKeeper() throws DataConversionException, IOException {
        return readDaysKeeper(daysKeeperStorage.getDaysKeeperFilePath());
    }

    @Override
    public Optional<ReadOnlyDaysKeeper> readDaysKeeper(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return daysKeeperStorage.readDaysKeeper(filePath);
    }

    @Override
    public void saveDaysKeeper(ReadOnlyDaysKeeper daysKeeper) throws IOException {
        saveDaysKeeper(daysKeeper, daysKeeperStorage.getDaysKeeperFilePath());
    }

    @Override
    public void saveDaysKeeper(ReadOnlyDaysKeeper daysKeeper, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        daysKeeperStorage.saveDaysKeeper(daysKeeper, filePath);
    }

}
