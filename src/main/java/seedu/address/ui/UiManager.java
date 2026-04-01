package seedu.address.ui;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;

/**
 * The manager of the UI component.
 */
public class UiManager implements Ui {

    public static final String ALERT_DIALOG_PANE_FIELD_ID = "alertDialogPane";

    private static final Logger logger = LogsCenter.getLogger(UiManager.class);
    private static final String ICON_APPLICATION = "/images/address_book_32.png";
    private static final String ASSERTION_FAILURE_TITLE = "Assertion failure";
    private static final String ASSERTION_FAILURE_HEADER =
            "An internal assertion failed. The application will exit.";

    private Logic logic;
    private MainWindow mainWindow;
    private final AtomicBoolean isShutdownInProgress = new AtomicBoolean(false);

    /**
     * Creates a {@code UiManager} with the given {@code Logic}.
     */
    public UiManager(Logic logic) {
        this.logic = logic;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting UI...");

        //Set the application icon.
        primaryStage.getIcons().add(getImage(ICON_APPLICATION));

        try {
            mainWindow = new MainWindow(primaryStage, logic);
            installAssertionFailureHandler();
            mainWindow.show(); //This should be called before creating other UI parts
            mainWindow.fillInnerParts();

        } catch (Throwable e) {
            logger.severe(StringUtil.getDetails(e));
            showFatalErrorDialogAndShutdown("Fatal error during initializing", e);
        }
    }

    private Image getImage(String imagePath) {
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

    void showAlertDialogAndWait(Alert.AlertType type, String title, String headerText, String contentText) {
        showAlertDialogAndWait(getPrimaryStageOrNull(), type, title, headerText, contentText);
    }

    /**
     * Shows an alert dialog on {@code owner} with the given parameters.
     * This method only returns after the user has closed the alert dialog.
     */
    private static void showAlertDialogAndWait(Stage owner, AlertType type, String title, String headerText,
                                               String contentText) {
        final Alert alert = new Alert(type);
        alert.getDialogPane().getStylesheets().add("view/DarkTheme.css");
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getDialogPane().setId(ALERT_DIALOG_PANE_FIELD_ID);
        alert.showAndWait();
    }

    /**
     * Shows an error alert dialog with {@code title} and error message, {@code e},
     * and exits the application after the user has closed the alert dialog.
     */
    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        logger.severe(title + " " + e.getMessage() + StringUtil.getDetails(e));
        showAlertDialogAndWait(Alert.AlertType.ERROR, title, e.getMessage(), e.toString());
        Platform.exit();
        System.exit(1);
    }

    private void installAssertionFailureHandler() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            AssertionError assertionError = findAssertionError(throwable);
            if (assertionError == null) {
                return;
            }

            if (!isShutdownInProgress.compareAndSet(false, true)) {
                return;
            }

            logger.severe("Assertion failed on thread " + thread.getName() + ": "
                    + StringUtil.getDetails(assertionError));
            Platform.runLater(() -> showAssertionFailureDialogAndShutdown(assertionError));
        });
    }

    private static AssertionError findAssertionError(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof AssertionError) {
                return (AssertionError) current;
            }
            current = current.getCause();
        }
        return null;
    }

    private void showAssertionFailureDialogAndShutdown(AssertionError assertionError) {
        String content = assertionError.getMessage() == null ? assertionError.toString() : assertionError.getMessage();
        showAlertDialogAndWait(getPrimaryStageOrNull(), AlertType.ERROR,
                ASSERTION_FAILURE_TITLE, ASSERTION_FAILURE_HEADER, content);
        Platform.exit();
        System.exit(1);
    }

    private Stage getPrimaryStageOrNull() {
        return mainWindow == null ? null : mainWindow.getPrimaryStage();
    }

}
