package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TELE_HANDLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TUTORIAL_GROUP_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.STUDENT_ID_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TELE_HANDLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TELE_HANDLE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TUTORIAL_GROUP_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TUTORIAL_GROUP_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STUDENT_ID_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELE_HANDLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUTORIAL_GROUP_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELE_HANDLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL_GROUP;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.TeleHandle;
import seedu.address.model.person.TutorialGroup;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 x/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TELE_HANDLE_DESC, TeleHandle.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TUTORIAL_GROUP_DESC, TutorialGroup.MESSAGE_CONSTRAINTS);

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser,
                "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_TELE_HANDLE_AMY + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY + STUDENT_ID_DESC_AMY + EMAIL_DESC_AMY
                + PHONE_DESC_BOB + TELE_HANDLE_DESC_AMY + TUTORIAL_GROUP_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withStudentId(VALID_STUDENT_ID_AMY).withEmail(VALID_EMAIL_AMY).withPhone(VALID_PHONE_BOB)
                .withTeleHandle(VALID_TELE_HANDLE_AMY).withTutorialGroup(VALID_TUTORIAL_GROUP_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // studentId
        userInput = targetIndex.getOneBased() + STUDENT_ID_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withStudentId(VALID_STUDENT_ID_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // teleHandle
        userInput = targetIndex.getOneBased() + TELE_HANDLE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withTeleHandle(VALID_TELE_HANDLE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tutorialGroup
        userInput = targetIndex.getOneBased() + TUTORIAL_GROUP_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withTutorialGroup(VALID_TUTORIAL_GROUP_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple valid fields repeated
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + TELE_HANDLE_DESC_AMY + EMAIL_DESC_AMY
                + TUTORIAL_GROUP_DESC_AMY + PHONE_DESC_AMY + TELE_HANDLE_DESC_AMY + EMAIL_DESC_AMY
                + TUTORIAL_GROUP_DESC_AMY + PHONE_DESC_BOB + TELE_HANDLE_DESC_BOB + EMAIL_DESC_BOB
                + TUTORIAL_GROUP_DESC_BOB;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELE_HANDLE,
                        PREFIX_TUTORIAL_GROUP));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_TELE_HANDLE_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_TELE_HANDLE_DESC + INVALID_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELE_HANDLE));

        // duplicate studentId
        userInput = targetIndex.getOneBased() + STUDENT_ID_DESC_AMY + STUDENT_ID_DESC_AMY;
        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STUDENT_ID));
    }
}
