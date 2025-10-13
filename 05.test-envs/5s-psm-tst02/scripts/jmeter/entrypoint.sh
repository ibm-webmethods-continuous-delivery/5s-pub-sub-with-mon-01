#!/bin/sh

wget https://repo1.maven.org/maven2/kg/apc/jmeter-plugins-manager/1.11/jmeter-plugins-manager-1.11.jar \
-O ./lib/ext/jmeter-plugins-manager-1.11.jar 

wget https://github.com/johrstrom/jmeter-prometheus-plugin/releases/download/0.7.1/jmeter-prometheus-plugin-0.7.1.jar \
-O ./lib/ext/jmeter-prometheus-plugin-0.7.1.jar

/mnt/scripts/runTests.sh

echo "Work in progress. For now open a shell and play with jmeter"

tail -f /dev/null