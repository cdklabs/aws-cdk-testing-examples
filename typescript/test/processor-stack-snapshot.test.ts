import { Template } from "@aws-cdk/assertions";
import * as cdk from "@aws-cdk/core";
import * as sns from "@aws-cdk/aws-sns";
import { ProcessorStack } from "../lib/processor-stack";

describe("ProcessorStack", () => {
  test("matches the snapshot", () => {
    // Set up the app and resources in the other stack.
    const app = new cdk.App();
    const topicsStack = new cdk.Stack(app, "TopicsStack");
    const topics = [new sns.Topic(topicsStack, "Topic1", {})];

    // Create the ProcessorStack.
    const processorStack = new ProcessorStack(app, "ProcessorStack", {
      topics: topics, // Cross-stack reference
    });

    // Prepare the stack for assertions.
    const template = Template.fromStack(processorStack);

    // Assert the template matches the snapshot.
    expect(template.toJSON()).toMatchSnapshot();
  });
});
