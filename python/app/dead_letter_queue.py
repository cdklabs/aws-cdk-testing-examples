from aws_cdk import aws_cloudwatch as cloudwatch
from aws_cdk import aws_sqs as sqs
from aws_cdk import core as cdk


class DeadLetterQueue(sqs.Queue):
    def __init__(self, scope: cdk.Construct, id: str):
        super().__init__(scope, id)

        self.messages_in_queue_alarm = cloudwatch.Alarm(
            self,
            "Alarm",
            alarm_description="There are messages in the Dead Letter Queue.",
            evaluation_periods=1,
            threshold=1,
            metric=self.metric_approximate_number_of_messages_visible(),
        )
