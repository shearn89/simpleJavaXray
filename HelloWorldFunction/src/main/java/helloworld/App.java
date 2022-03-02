package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.strategy.sampling.NoSamplingStrategy;

import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Map<String,Object>, String> {

    public App() {
        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard();
        builder.withSamplingStrategy(new NoSamplingStrategy());
        AWSXRay.setGlobalRecorder(builder.build());
    }

    public String handleRequest(Map<String,Object> input, final Context context) {
        AWSXRay.beginSegment("simpleJavaXrayTest");
        String output = doSomething();
        AWSXRay.endSegment();
        return output;
    }

    private String doSomething() {
        Subsegment subsegment = AWSXRay.beginSubsegment("doSomething");
        try {
            thrower();
        } catch (Exception e) {
            subsegment.addException(e);
        }
        AWSXRay.endSubsegment();
        return "here we are";
    }

    private void thrower() throws Exception {
        throw new Exception("oops");
    }
}
