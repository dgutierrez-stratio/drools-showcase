package com.stratio.examples;

import static com.stratio.tais.persistence.model.ConceptState.LEARNED;
import static com.stratio.tais.persistence.model.ServiceDeployment.STATE_CREATING_DEPLOYMENT;
import static com.stratio.tais.persistence.model.ServiceDeployment.STATE_CREATING_LABELS;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import com.github.jeichler.junit.drools.DroolsJUnitRunner;
import com.github.jeichler.junit.drools.annotation.DroolsFiles;
import com.github.jeichler.junit.drools.annotation.StatefulDroolsSession;
import com.github.jeichler.junit.drools.session.DroolsSession;
import com.stratio.DeployRulesSpecs;
import com.stratio.tais.persistence.model.ServiceDeployment;
import com.stratio.tais.persistence.model.ServiceDeploymentParameter;
import com.stratio.tais.persistence.model.service.ServiceConfiguration;

@DroolsFiles(ruleFiles = {
    "examples/interpolation-example.drl" // this drl example
})
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(DroolsJUnitRunner.class)
public class InterpolationTest extends DeployRulesSpecs {

  @StatefulDroolsSession
  private DroolsSession session;

  private final String SERVICE_NAME = "test-service";
  private final String SERVICE_MODEL = "model";

  @Test
  public void givenStateCreatingDeployment_whenFiringExampleRules_thenUpdateInterpolatedValues() {
    // given
    ServiceConfiguration descriptor = serviceConfiguration(SERVICE_NAME, SERVICE_MODEL, LEARNED);
    ServiceDeployment deployment = serviceDeployment(descriptor, SERVICE_NAME, SERVICE_MODEL, STATE_CREATING_DEPLOYMENT);
    ServiceDeploymentParameter someParameter = serviceDeploymentParameter(deployment, "someParameter", "someValue");
    ServiceDeploymentParameter interpolable = serviceDeploymentParameter(deployment, "parameterWithInterpolation",
        "${someParameter}");
    // this is only required for tests, in runtime every parameter is automatically added to the deployment
    deployment.addDeploymentParameter(someParameter);
    deployment.addDeploymentParameter(interpolable);

    // when
    session.insert(deployment, interpolable, someParameter);
    try {session.fireAllRules();} catch (Exception e) {}

    // then
    assertEquals("someValue", interpolable.getValue());
  }

}
