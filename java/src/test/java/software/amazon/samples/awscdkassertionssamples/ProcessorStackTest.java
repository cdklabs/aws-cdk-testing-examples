package software.amazon.samples.awscdkassertionssamples;

import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Capture;
import software.amazon.awscdk.assertions.Match;
import software.amazon.awscdk.assertions.Template;
import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.sns.Topic;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcessorStackTest {
    @Test
    public void testSynthesizesProperly() {
        final App app = new App();

        // Since the ProcessorStack consumes resources from a separate stack (cross-stack references), we create a stack
        // for our SNS topics to live in here. These topics can then be passed to the ProcessorStack later, creating a
        // cross-stack reference.
        final Stack topicsStack = new Stack(app, "TopicsStack");

        // Create the topic the stack we're testing will reference.
        final List<Topic> topics = Collections.singletonList(Topic.Builder.create(topicsStack, "Topic1").build());

        // Create the ProcessorStack.
        final ProcessorStack processorStack = new ProcessorStack(
                app,
                "ProcessorStack",
                topics // Cross-stack reference
        );

        // Prepare the stack for assertions.
        final Template template = Template.fromStack(processorStack);

        // Assert it creates the function with the correct properties...
        template.hasResourceProperties("AWS::Lambda::Function", Map.of(
                "Handler", "handler",
                "Runtime", "nodejs14.x"
        ));

        // Creates the subscription...
        template.resourceCountIs("AWS::SNS::Subscription", 1);

        // Gives permission to SNS to invoke the function for the topic...
        template.hasResourceProperties("AWS::Lambda::Permission", Map.of(
                "Action", "lambda:InvokeFunction",
                "Principal", "sns.amazonaws.com"
        ));

        // Creates the function's execution role...
        template.hasResourceProperties("AWS::IAM::Role", Map.of(
                "AssumeRolePolicyDocument", Collections.singletonMap(
                        "Statement", Collections.singletonList(Map.of(
                                "Action", "sts:AssumeRole",
                                "Effect", "Allow",
                                "Principal", Collections.singletonMap("Service", "lambda.amazonaws.com")
                        ))
                )
        ));

        // And finally the execution role's policy
        template.hasResourceProperties("AWS::IAM::Policy", Map.of(
                // It should be allowed to execute the state machine
                "PolicyDocument", Collections.singletonMap(
                        "Statement", Collections.singletonList(
                                // We need to use Match.objectLike() explicitly since we're now inside an array.
                                Match.objectLike(Map.of(
                                        "Action", "states:StartExecution",
                                        "Effect", "Allow"
                                ))
                        )
                )
        ));

        // Fully assert on the state machine's IAM role with matchers.
        template.hasResourceProperties("AWS::IAM::Role", Match.objectEquals(
                Collections.singletonMap("AssumeRolePolicyDocument", Map.of(
                        "Version", "2012-10-17",
                        "Statement", Collections.singletonList(Map.of(
                                "Action", "sts:AssumeRole",
                                "Effect", "Allow",
                                "Principal", Collections.singletonMap(
                                        "Service", Collections.singletonMap(
                                                "Fn::Join", Arrays.asList(
                                                        "",
                                                        Arrays.asList("states.", Match.anyValue(), ".amazonaws.com")
                                                )
                                        )
                                )
                        ))
                ))
        ));

        // Assert on the state machine's definition with the Match.serializedJson() matcher.
        template.hasResourceProperties("AWS::StepFunctions::StateMachine", Collections.singletonMap(
                "DefinitionString", Match.serializedJson(
                        // Match.objectEquals() is used implicitly, but we use it explicitly here for extra clarity.
                        Match.objectEquals(Map.of(
                                "StartAt", "StartState",
                                "States", Collections.singletonMap(
                                        "StartState", Map.of(
                                                "Type", "Pass",
                                                "End", true,
                                                // Make sure this state doesn't provide a next state -- we can't provide
                                                // both Next and set End to true.
                                                "Next", Match.absent()
                                        )
                                )
                        ))
                )
        ));

        // Capture some data from the state machine's definition.
        final Capture startAtCapture = new Capture();
        final Capture statesCapture = new Capture();
        template.hasResourceProperties("AWS::StepFunctions::StateMachine", Collections.singletonMap(
                "DefinitionString", Match.serializedJson(
                        Match.objectLike(Map.of(
                                "StartAt", startAtCapture,
                                "States", statesCapture
                        ))
                )
        ));

        // Assert that the start state starts with "Start".
        assertThat(startAtCapture.asString()).matches("^Start.+");

        // Assert that the start state actually exists in the states object of the state machine definition.
        assertThat(statesCapture.asObject()).containsKey(startAtCapture.asString());
    }
}
