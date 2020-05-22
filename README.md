# Drools showcase

Behold! The wonders of Drools at last in legible format for the fun of the whole family. Over the next exciting examples, and to help you cope with this delightful quarantine, we will be learning how to overcomplicate and obfuscate what should be relatively easy tasks, so... buckle up!

## How to proceed

The showcase is divided into two packages *secrets* and *examples*, covering the most frequently used sorcery that we've seen in production environments. We recommend taking a look a the DRL files along with the associated Java test files, in order to understand the features and avoid suicidal thoughts. Also, this project is bundled with the core dependencies required to compile every scenario and to facilitate the development of rules.

## Secrets management

One of the star features provided by the mighty Drools is the secret generation API. You will find proof of these miracles under the following resources:

The **certificates** section covers how to generate regular certificates with customized suffixes as well as certificates that have to be signed by a third party CA. This will be the case for k8s boys. Enjoy fellas.

| Example DRL | Java test classes |
|--------------------------------------------------------|---------------------------|
| /src/main/resources/secrets/certificate-generation.drl | CertificateGenerationTest |

The **passwords** section introduces methods to retrieve cluster secrets that need to be propagated as well as some error management.

| Example DRL | Java test classes |
|-----------------------------------------------------|------------------------|
| /src/main/resources/secrets/password-generation.drl | PasswordGenerationTest |

The **keytabs** section includes also an example for generating some random number of identities. Spare no resources guys!

| Example DRL | Java test classes |
|---------------------------------------------------|----------------------|
| /src/main/resources/secrets/keytab-generation.drl | KeytabGenerationTest |

## Parameter injection

Deployments where not complicated enough, so we introduced a way to dynamically include deployment parameters in runtime! This will be particularly useful when parameters have to be included or modified for instance during pre-requisites execution. Any other sadistic or mentally disturbed practice is also supported by Drools.

| Example DRL | Java test classes |
|---------------------------------------------------------------|--------------------------|
| /src/main/resources/secrets/deployment-parameters-example.drl | DeploymentParametersTest |

## Manual interpolation

Now you can feel like God from the comfort of home-prison by manually invoking the interpolator anytime. You are welcome dear *developas*.

| Example DRL | Java test classes |
|-------------------------------------------------------|-------------------|
| /src/main/resources/secrets/interpolation-example.drl | InterpolationTest |

## Marathon labels injection

We know that you like to manipulate, twist and break the laws of nature, so we prepared a mind blowing example to let you create, eliminate or modify any Marathon label in the deployment.

| Example DRL | Java test classes |
|---------------------------------------------------------|--------------------|
| /src/main/resources/secrets/marathon-labels-example.drl | MarathonLabelsTest |

## Direct access to TAIS senses

We couldn't resist introducing you, the reader, into the occult arts of messing up with TAIS senses. This is not for initiates, and it requires profound knowledge of potions and spells from the Hogwarts school of witchcraft and wizardry, but it sure is worth the risk!

| Example DRL | Java test classes |
|-------------------------------------------------------------|-------------------|
| /src/main/resources/secrets/senses-interaction-examples.drl | N/A |

## Creating deploy-ready zip files

This project comes with an utility to generate sample zip files ready to be uploaded into ccy-deploy-api.

```bash
version=1.0.0 make package
```