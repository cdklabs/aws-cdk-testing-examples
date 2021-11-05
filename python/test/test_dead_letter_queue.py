from aws_cdk import core as cdk
from aws_cdk.assertions import Match, Template

from app.dead_letter_queue import DeadLetterQueue


def test_creates_alarm():
    stack = cdk.Stack()
    DeadLetterQueue(stack, "DeadLetterQueue")

    template = Template.from_stack(stack)
    template.has_resource_properties(
        "AWS::CloudWatch::Alarm",
        {
            "Namespace": "AWS/SQS",
            "MetricName": "ApproximateNumberOfMessagesVisible",
            "Dimensions": [
                {
                    "Name": "QueueName",
                    "Value": Match.any_value(),
                },
            ],
        },
    )
