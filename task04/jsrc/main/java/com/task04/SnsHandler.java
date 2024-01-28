package com.task04;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.syndicate.deployment.annotations.events.SqsTriggerEventSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SqsTriggerEventSource(targetQueue = "async_queue", batchSize = 1)//
@LambdaHandler(lambdaName = "sns_handler",
	roleName = "sns_handler-role",
	isPublishVersion = true,
	aliasName = "${lambdas_alias_name}"
)
public class SnsHandler implements RequestHandler<SNSEvent, Void> {
	LambdaLogger logger;

	@Override
	public Void handleRequest(SNSEvent event, Context context) {
		logger = context.getLogger();
		List<SNSEvent.SNSRecord> records = event.getRecords();
		if (!records.isEmpty()) {
            for (SNSEvent.SNSRecord record : records) {
                processRecord(record, context);
            }
		}
        return null;
    }

	public void processRecord(SNSEvent.SNSRecord record, Context context) {
		try {
			String message = record.getSNS().getMessage();
			context.getLogger().log("Received SNS message: " + message);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
