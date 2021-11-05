import * as cdk from "@aws-cdk/core";
import * as cloudwatch from "@aws-cdk/aws-cloudwatch";
import * as sqs from "@aws-cdk/aws-sqs";

export class DeadLetterQueue extends sqs.Queue {
  public readonly messagesInQueueAlarm: cloudwatch.IAlarm;

  constructor(scope: cdk.Construct, id: string) {
    super(scope, id);

    this.messagesInQueueAlarm = new cloudwatch.Alarm(this, "Alarm", {
      alarmDescription: "There are messages in the Dead Letter Queue.",
      evaluationPeriods: 1,
      threshold: 1,
      metric: this.metricApproximateNumberOfMessagesVisible(),
    });
  }
}
