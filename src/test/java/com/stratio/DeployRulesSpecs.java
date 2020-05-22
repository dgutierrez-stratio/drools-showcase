package com.stratio;

import java.util.HashMap;

import com.stratio.tais.persistence.model.ConceptState;
import com.stratio.tais.persistence.model.ServiceDeployment;
import com.stratio.tais.persistence.model.ServiceDeploymentParameter;
import com.stratio.tais.persistence.model.service.ServiceConfiguration;
import com.stratio.tais.persistence.model.service.networking.ServiceNetworking;

import mesosphere.marathon.client.model.v2.App;

public class DeployRulesSpecs {

  public ServiceDeployment serviceDeployment(String serviceName, String modelName, int status) {
    return serviceDeployment(null, serviceName, modelName, "service-id", status);
  }

  public ServiceDeployment serviceDeployment(ServiceConfiguration sc, String serviceName, String modelName, int status) {
    return serviceDeployment(sc, serviceName, modelName, "service-id", status);
  }

  public ServiceDeployment serviceDeployment(ServiceConfiguration sc, String serviceName, String model, String instanceName, int status) {
    ServiceDeployment deploy = new ServiceDeployment();
    deploy.setServiceConfiguration(sc);
    deploy.setDeploymentState(status);
    deploy.setServiceName(serviceName);
    deploy.setModel(model);
    deploy.setServiceId(instanceName);
    deploy.setDeployment(new App());
    deploy.getDeployment().setLabels(new HashMap<>());
    deploy.getDeployment().setEnv(new HashMap<>());
    return deploy;
  }

  public ServiceDeploymentParameter serviceDeploymentParameter(ServiceDeployment config, String name, String value) {
    ServiceDeploymentParameter scp = new ServiceDeploymentParameter();
    scp.setValue(value);
    scp.setParent(config.getUuid());
    scp.setName(name);
    return scp;
  }

  public ServiceConfiguration serviceConfiguration(String serviceName, String model, ConceptState status) {
    ServiceConfiguration config = new ServiceConfiguration();
    config.setState(status);
    config.setServiceName(serviceName);
    config.setModel(model);
    config.setNetworking(new ServiceNetworking());

    return config;
  }
}
