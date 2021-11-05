const AWS = require("aws-sdk");

const sfn = new AWS.StepFunctions();

exports.handler = async (event, context) => {
  await sfn
    .startExecution({
      stateMachineArn: process.env.STATE_MACHINE_ARN,
      input: JSON.stringify(event),
    })
    .promise();

  return { statusCode: 200 };
};
