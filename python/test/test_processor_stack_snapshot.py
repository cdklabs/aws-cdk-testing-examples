from aws_cdk import aws_sns as sns
from aws_cdk import core as cdk
from aws_cdk.assertions import Template

from app.processor_stack import ProcessorStack


# The snapshot parameter is injected by Pytest -- it's a fixture provided by
# syrupy, the snapshot testing library we're using:
# https://docs.pytest.org/en/stable/fixture.html
def test_matches_snapshot(snapshot):
    # Set up the app and resources in the other stack.
    app = cdk.App()
    topics_stack = cdk.Stack(app, "TopicsStack")
    topics = [sns.Topic(topics_stack, "Topic1")]

    # Create the ProcessorStack.
    processor_stack = ProcessorStack(
        app, "ProcessorStack", topics=topics  # Cross-stack reference
    )

    # Prepare the stack for assertions.
    template = Template.from_stack(processor_stack)

    assert template.to_json() == snapshot
