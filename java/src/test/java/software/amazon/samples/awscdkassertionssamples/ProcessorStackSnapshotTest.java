package software.amazon.samples.awscdkassertionssamples;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import software.amazon.awscdk.assertions.Template;
import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.sns.Topic;

import java.util.Collections;
import java.util.List;


// SnapshotExtension is responsible for setting up the snapshot tests, which includes populating the expect field on
// this class.
@ExtendWith(SnapshotExtension.class)
public class ProcessorStackSnapshotTest {
    private Expect expect;

    @Test
    public void testMatchesSnapshot() {
        // Set up the app and resources in the other stack.
        final App app = new App();
        final Stack topicsStack = new Stack(app, "TopicsStack");
        final List<Topic> topics = Collections.singletonList(Topic.Builder.create(topicsStack, "Topic1").build());

        // Create the ProcessorStack.
        final ProcessorStack processorStack = new ProcessorStack(
                app,
                "ProcessorStack",
                topics // Cross-stack reference
        );

        // Prepare the stack for assertions.
        final Template template = Template.fromStack(processorStack);

        // Assert the template matches the snapshot.
        expect.toMatchSnapshot(template.toJSON());
    }
}
