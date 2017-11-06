package seedu.address.ui;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;

import com.google.common.eventbus.Subscribe;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String tagColor = "#5AC0FB";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final ReadOnlyPerson person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    //@@author qihao27
    @FXML
    private ImageView favourite;
    @FXML
    private ImageView todo;
    @FXML
    private Label totalTodo;
    //@@author

    public PersonCard(ReadOnlyPerson person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        initTags(person);
        bindListeners(person);
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(ReadOnlyPerson person) {
        name.textProperty().bind(Bindings.convert(person.nameProperty()));
        //@@author aaronyhsoh-unsed
        //highlightName(person);
        //@@author
        phone.textProperty().bind(Bindings.convert(person.phoneProperty()));
        address.textProperty().bind(Bindings.convert(person.addressProperty()));
        email.textProperty().bind(Bindings.convert(person.emailProperty()));
        person.tagProperty().addListener((observable, oldValue, newValue) -> {
            tags.getChildren().clear();
            initTags(person);
        });
        //@@author qihao27
        addFavouriteStar(person);
        addTodoCount(person);
        //@@author
    }

    /**
     * Initialise the tags with colours
     */
    private void initTags(ReadOnlyPerson person) {
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.setStyle("-fx-background-color: " + tagColor);
            tags.getChildren().add(tagLabel);
        });
    }

    //@@author qihao27
    private void addFavouriteStar(ReadOnlyPerson person) {
        if (person.getFavourite()) {
            favourite.setId("favouriteStar");
        }
    }

    private void addTodoCount(ReadOnlyPerson person) {
        if (person.getTodoItems().size() > 0) {
            totalTodo.setText((person.getTodoItems().size()+""));
            todo.setId("todoBackground");
        } else {
            totalTodo.setText("");
        }
    }

    private void setTodoCount(int totalTodo) {
        if (totalTodo > 0) {
            this.totalTodo.setText(totalTodo + "");
        } else {
            this.totalTodo.setText("");
        }
    }

    //@@author aaronyhsoh-unsed
    /*private void highlightName(ReadOnlyPerson person) {
        if (person.getFavourite()) {
            name.setStyle("-fx-text-fill: red");
        }
    }*/
    //@@auther

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }

    //@@author qihao27
    @Subscribe
    public void handleAddressBookChangedEvent(AddressBookChangedEvent abce) {
        setTodoCount(abce.data.getTodoList().size());
    }
}
