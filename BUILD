#~/git/base_workspace/ithinkican/BUILD

java_binary(
        name = "build",
        srcs = glob(["src/**/*.java"]),
        main_class = "ithinkican.core.Main",
	deps = [":pi4j"]
)

java_import(
	name = "pi4j",
	jars = [

		"misc/pi4j-core.jar",
		"misc/pi4j-core-javadoc.jar",
		"misc/pi4j-core-sources.jar",
		"misc/pi4j-device.jar",
		"misc/pi4j-device-javadoc.jar",
		"misc/pi4j-device-sources.jar",
		"misc/pi4j-gpio-extension.jar",
		"misc/pi4j-gpio-extension-javadoc.jar",
		"misc/pi4j-gpio-extension-sources.jar",
		"misc/pi4j-service.jar",
		"misc/pi4j-service-javadoc.jar",
		"misc/pi4j-service-sources.jar"
	],
)



