#!/bin/bash
JAVA_OPTIONS="-Dappdynamics.agent.applicationName=APPDEX_LBG
-Dappdynamics.controller.hostName=${controllerHost-localhost}
-Dappdynamics.controller.port=${controllerPort-8090}
-Dappdynamics.agent.tierName=APPDEX2
-Dappdynamics.agent.nodeName=NodeUTIL22
-Dappdynamics.agent.uniqueHostId=NOPE222
-Dappdynamics.agent.maxMetrics=1000
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

java $JAVA_OPTIONS -jar ./build/sandbox/agent/machineagent.jar
