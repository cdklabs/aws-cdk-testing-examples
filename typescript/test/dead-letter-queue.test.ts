import { Match, Template } from "@aws-cdk/assertions";
import * as cdk from "@aws-cdk/core";
import { DeadLetterQueue } from "../lib/dead-letter-queue";

describe("DeadLetterQueue", () => {
  test("creates an alarm", () => {
    const stack = new cdk.Stack();
    new DeadLetterQueue(stack, "DeadLetterQueue");

    const template = Template.fromStack(stack);
    template.hasResourceProperties("AWS::CloudWatch::Alarm", {
      Namespace: "AWS/SQS",
      MetricName: "ApproximateNumberOfMessagesVisible",
      Dimensions: [
        {
          Name: "QueueName",
          Value: Match.anyValue(),
        },
      ],
    });
  });
});
