package software.amazon.samples.awscdkassertionssamples;

import software.amazon.awscdk.core.App;


public class SampleApp {
    public static void main(final String[] args) {
        App app = new App();

        // Stacks are intentionally not created here -- this application isn't meant to be deployed.

        app.synth();
    }
}
