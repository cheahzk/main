package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STARTDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STARTTIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENDDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENDTIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TASKS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;



import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

import seedu.address.model.tag.Tag;
import seedu.address.model.task.Description;
import seedu.address.model.task.EndDate;
import seedu.address.model.task.EndTime;
import seedu.address.model.task.Name;
import seedu.address.model.task.StartDate;
import seedu.address.model.task.StartTime;
import seedu.address.model.task.Task;



 /**
  * Edits the details of an existing person in the address book.
  */

 public class EditCommand extends Command {


    public static final String COMMAND_WORD = "edit";
    public static final String COMMAND_ALIAS = "e";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_DESCRIPTION + "DECRIPTION] "
            + "[" + PREFIX_STARTDATE + "STRATDATE] "
            + "[" + PREFIX_STARTTIME + "STARTTIME] "
            + "[" + PREFIX_ENDDATE + "ENDDATE] "
            + "[" + PREFIX_ENDTIME + "ENDTIME]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "Study "
            + PREFIX_DESCRIPTION + "Study for the whole day";


    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the address book.";

    private final Index index;
    private final EditTaskDescriptor editTaskDescriptor;

     /**
      * @param index of the Task in the filtered Task list to edit
      * @param editPersonDescriptor details to edit the person with
      */

    public EditCommand(Index index, EditTaskDescriptor editTaskDescriptor) {
        requireNonNull(index);
        requireNonNull(editTaskDescriptor);

        this.index = index;
        this.editTaskDescriptor = new EditTaskDescriptor(editTaskDescriptor);
    }

 	@Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }


        Task TaskToEdit = lastShownList.get(index.getZeroBased());
        Task editedTask = createEditedTask(TaskToEdit, editTaskDescriptor);

        if (!TaskToEdit.isSameTask(editedTask) && model.hasTask(editedTask)) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

        model.setTask(TaskToEdit, editedTask);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        model.commitTaskBook();
        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, editedTask));
    }



     /**
     * Creates and returns a {@code Task} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Task createEditedTask(Task personToEdit, EditTaskDescriptor editTaskDescriptor) {

        assert personToEdit != null;


        Name updatedName = editTaskDescriptor.getName().orElse(personToEdit.getName());
        Description updatedDescription = editTaskDescriptor.getDescription().orElse(personToEdit.getDescription());
        StartDate updatedStartDate = editTaskDescriptor.getStartDate().orElse(personToEdit.getStartDate());
        StartTime updatedStartTime = editTaskDescriptor.getStartTime().orElse(personToEdit.getStartTime());
        EndDate updatedEndDate = editTaskDescriptor.getEndDate().orElse(personToEdit.getEndDate());
        EndTime updatedEndTime = editTaskDescriptor.getEndTime().orElse(personToEdit.getEndTime());
        Set<Tag> updatedTags = editTaskDescriptor.getTags().orElse(personToEdit.getTags());

        System.out.println(editTaskDescriptor.getName());
        return new Task(updatedName, updatedStartDate, updatedStartTime, updatedEndDate, updatedEndTime, updatedDescription, updatedTags);
    }



     @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }



         // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }



         // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editTaskDescriptor.equals(e.editTaskDescriptor);
    }



     /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditTaskDescriptor {


        private Description description;
        private EndDate endDate;
        private EndTime endTime;
        private Name name;
        private StartDate startDate;
        private StartTime startTime;
        private Set<Tag> tags = new HashSet<>();


        public EditTaskDescriptor() {}


        public EditTaskDescriptor(Description description, EndDate endDate, EndTime endTime,
				Name name, StartDate startDate, StartTime startTime, Set<Tag> tags) {
			super();
			this.description = description;
			this.endDate = endDate;
			this.endTime = endTime;
			this.name = name;
			this.startDate = startDate;
			this.startTime = startTime;
			this.tags = tags;
		}




 		/**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            setName(toCopy.name);
            setDescription(toCopy.description);
            setEndDate(toCopy.endDate);
            setEndTime(toCopy.endTime);
            setStartDate(toCopy.startDate);
            setStartTime(toCopy.startTime);
            setTags(toCopy.tags);
        }


         /**
         * Returns true if at least one field is edited.
         */

        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, description, endDate, endTime, startDate, startTime);
        }

         /**
		 * @return the description
		 */
        public Optional<Description> getDescription() {
			return Optional.ofNullable(description);
		}



 		/**
		 * @param description the description to set
		 */
		public void setDescription(Description description) {
			this.description = description;
		}




 		/**
		 * @return the endDate
		 */
		public Optional<EndDate> getEndDate() {
            return Optional.ofNullable(endDate);
        }

 		/**
		 * @param endDate the endDate to set
		 */
		public void setEndDate(EndDate endDate) {
			this.endDate = endDate;
		}


 		/**
		 * @return the endTime
		 */
		public Optional<EndTime> getEndTime() {
			return Optional.ofNullable(endTime);
		}


 		/**
		 * @param endTime the endTime to set
		 */
		public void setEndTime(EndTime endTime) {
			this.endTime = endTime;
		}


 		/**
		 * @return the name
		 */
		public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }


 		/**
		 * @param name2 the name to set
		 */
		public void setName(Name name2) {
			this.name = name2;
		}


 		/**
		 * @return the startDate
		 */
		public Optional<StartDate> getStartDate() {
			return Optional.ofNullable(startDate);
		}


 		/**
		 * @param startDate the startDate to set
		 */
		public void setStartDate(StartDate startDate) {
			this.startDate = startDate;
		}


 		/**
		 * @return the startTime
		 */
		public Optional<StartTime> getStartTime() {
			return Optional.ofNullable(startTime);
		}


 		/**
		 * @param startTime the startTime to set
		 */
		public void setStartTime(StartTime startTime) {
			this.startTime = startTime;
		}

 		/**
		 * @return the tags
		 */
		public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }



         /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

 		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "EditTaskDescriptor [description=" + description + ", endDate=" + endDate
					+ ", endTime=" + endTime + ", name=" + name + ", startDate=" + startDate + ", startTime="
					+ startTime + "]";
		}



 		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((description == null) ? 0 : description.hashCode());
			result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
			result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
			result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
			return result;

 		}

 		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */

     }

}
