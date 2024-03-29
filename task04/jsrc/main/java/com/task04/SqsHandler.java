package com.task04;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.syndicate.deployment.annotations.events.SqsTriggerEventSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;

@SqsTriggerEventSource(targetQueue = "async_queue", batchSize = 10)
@LambdaHandler(lambdaName = "sqs_handler",
	roleName = "sqs_handler-role"
)
public class SqsHandler implements RequestHandler<SQSEvent, Void> {

	@Override
	public Void handleRequest(SQSEvent event, Context context)
	{
		for(SQSEvent.SQSMessage msg : event.getRecords()){
			processMessage(msg, context);
		}
		return null;
	}

	private void processMessage(SQSEvent.SQSMessage msg, Context context) {
		try {
			context.getLogger().log("Processed message " + msg.getBody());

		} catch (Exception e) {
			context.getLogger().log("An error occurred");
			throw e;
		}

	}
}
