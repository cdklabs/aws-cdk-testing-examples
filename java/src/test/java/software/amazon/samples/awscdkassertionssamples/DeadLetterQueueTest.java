package software.amazon.samples.awscdkassertionssamples;

import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Match;
import software.amazon.awscdk.assertions.Template;
import software.amazon.awscdk.core.Stack;

import java.util.Collections;
import java.util.Map;

public class DeadLetterQueueTest {
    @Test
    public void testCreatesAlarm() {
        final Stack stack = new Stack();
        new DeadLetterQueue(stack, "DeadLetterQueue");

        final Template template = Template.fromStack(stack);
        template.hasResourceProperties("AWS::CloudWatch::Alarm", Map.of(
                "Namespace", "AWS/SQS",
                "MetricName", "ApproximateNumberOfMessagesVisible",
                "Dimensions", Collections.singletonList(Map.of(
                        "Name", "QueueName",
                        "Value", Match.anyValue()
                ))
        ));
    }
}
