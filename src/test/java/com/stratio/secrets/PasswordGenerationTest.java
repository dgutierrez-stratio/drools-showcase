package com.stratio.secrets;

import static com.stratio.tais.persistence.model.ConceptState.LEARNED;
import static com.stratio.tais.persistence.model.ServiceDeployment.STATE_CREATING_DEPLOYMENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import com.github.jeichler.junit.drools.DroolsJUnitRunner;
import com.github.jeichler.junit.drools.annotation.DroolsFiles;
import com.github.jeichler.junit.drools.annotation.StatefulDroolsSession;
import com.github.jeichler.junit.drools.session.DroolsSession;
import com.stratio.DeployRulesSpecs;
import com.stratio.tais.cns.Senses;
import com.stratio.tais.cns.exceptions.MesosSecretException;
import com.stratio.tais.cns.exceptions.SecretCreationException;
import com.stratio.tais.persistence.model.ServiceDeployment;
import com.stratio.tais.persistence.model.ServiceDeploymentParameter;
import com.stratio.tais.persistence.model.service.ServiceConfiguration;
import com.stratio.tais.senses.service.VaultSense;
import com.stratio.tais.service.util.UniverseUtils;

@DroolsFiles(ruleFiles = {
    "secrets/password-generation.drl", // this drl example
    "rules/secretManagement.drl"          // core drl from cct-deploy-api for secret generation
})
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(DroolsJUnitRunner.class)
@PrepareForTest({VaultSense.class, Senses.class})
public class PasswordGenerationTest extends DeployRulesSpecs {

  @StatefulDroolsSession
  private DroolsSession session;

  private VaultSense vaultSense;

  private final String SERVICE_NAME = "test-service";
  private final String SERVICE_MODEL = "model";

  @Before
  public void initialize() {
    PowerMockito.mockStatic(Senses.class);
    this.vaultSense = mock(VaultSense.class);
    PowerMockito.when(Senses.access(any())).thenReturn(vaultSense);

    doReturn(true).when(this.vaultSense).isGenerationPasswordsEnabled();
    doReturn("admin").when(vaultSense).getSingleSignOnUser();
    doReturn("1234").when(vaultSense).getSingleSignOnPassword();
  }

  @Test
  public void givenDeploymentConditions_whenNoErrorsAreProduced_thenSecretsCreatedFlagIsTrue()
      throws MesosSecretException {
    // given
    doReturn("someSecret").when(vaultSense).getMesosSecret(anyString());
    ServiceConfiguration descriptor = serviceConfiguration(SERVICE_NAME, SERVICE_MODEL, LEARNED);
    ServiceDeployment deployment = serviceDeployment(descriptor, SERVICE_NAME, SERVICE_MODEL, STATE_CREATING_DEPLOYMENT);
    ServiceDeploymentParameter instance = serviceDeploymentParameter(deployment, "serviceInstance", "/my-test-service");
    ServiceDeploymentParameter principal = serviceDeploymentParameter(deployment, UniverseUtils.PARAM_MESOS_PPAL, "open");

    // when
    session.insert(deployment, descriptor, instance, principal);
    try {session.fireAllRules();} catch (Exception e) {e.printStackTrace();}

    // then
    assertTrue(deployment.isSecretsCreated());
  }

  @Test
  public void givenDeploymentConditions_whenErrorsAreProduced_thenSecretsCreatedFlagIsTrue()
      throws MesosSecretException {
    // given
    doThrow(new MesosSecretException()).when(vaultSense).getMesosSecret(anyString());
    ServiceConfiguration descriptor = serviceConfiguration(SERVICE_NAME, SERVICE_MODEL, LEARNED);
    ServiceDeployment deployment = serviceDeployment(descriptor, SERVICE_NAME, SERVICE_MODEL, STATE_CREATING_DEPLOYMENT);
    ServiceDeploymentParameter instance = serviceDeploymentParameter(deployment, "serviceInstance", "/my-test-service");
    ServiceDeploymentParameter principal = serviceDeploymentParameter(deployment, UniverseUtils.PARAM_MESOS_PPAL, "open");

    // when
    session.insert(deployment, descriptor, instance, principal);
    try {session.fireAllRules();} catch (Exception e) {e.printStackTrace();}

    // then
    assertFalse(deployment.isSecretsCreated());
  }

  @Test
  public void givenDeploymentConditions_whenDeploymentIsFinished_thenVerifyGeneratedSecrets()
      throws MesosSecretException, SecretCreationException {
    // given
    doReturn("someSecret").when(vaultSense).getMesosSecret(anyString());
    ServiceConfiguration descriptor = serviceConfiguration(SERVICE_NAME, SERVICE_MODEL, LEARNED);
    ServiceDeployment deployment = serviceDeployment(descriptor, SERVICE_NAME, SERVICE_MODEL, STATE_CREATING_DEPLOYMENT);
    ServiceDeploymentParameter instance = serviceDeploymentParameter(deployment, "serviceInstance", "/my-test-service");
    ServiceDeploymentParameter principal = serviceDeploymentParameter(deployment, UniverseUtils.PARAM_MESOS_PPAL, "open");

    // when
    session.insert(deployment, descriptor, instance, principal);
    try {session.fireAllRules();} catch (Exception e) {}

    // then
    ArgumentCaptor<String> pathCaptor = ArgumentCaptor.forClass(String.class);
    verify(vaultSense, times(2)).createSecret(pathCaptor.capture(), anyMap());
    assertEquals("/userland/passwords/my-test-service/mesos", pathCaptor.getAllValues().get(0));
    assertEquals("/userland/passwords/my-test-service/sso", pathCaptor.getAllValues().get(1));
  }
}
