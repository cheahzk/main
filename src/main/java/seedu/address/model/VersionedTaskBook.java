package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code TaskBook} that keeps track of its own history.
 */
public class VersionedTaskBook extends TaskBook {

    private final List<ReadOnlyTaskBook> taskBookStateList;
    private int currentStatePointer;

    public VersionedTaskBook(ReadOnlyTaskBook initialState) {
        super(initialState);

        taskBookStateList = new ArrayList<>();
        taskBookStateList.add(new TaskBook(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code TaskBook} state at the end of the state list.
     * Undone states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        taskBookStateList.add(new TaskBook(this));
        currentStatePointer++;
        indicateModified();
    }

    private void removeStatesAfterCurrentPointer() {
        taskBookStateList.subList(currentStatePointer + 1, taskBookStateList.size()).clear();
    }

    /**
     * Restores the task book to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(taskBookStateList.get(currentStatePointer));
    }

    /**
     * Restores the task book to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(taskBookStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has task book states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has task book states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < taskBookStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedTaskBook)) {
            return false;
        }

        VersionedTaskBook otherVersionedTaskBook = (VersionedTaskBook) other;

        // state check
        return super.equals(otherVersionedTaskBook)
                && taskBookStateList.equals(otherVersionedTaskBook.taskBookStateList)
                && currentStatePointer == otherVersionedTaskBook.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("Current state pointer at start of taskBookState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("Current state pointer at end of taskBookState list, unable to redo.");
        }
    }
}
