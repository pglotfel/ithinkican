#~/git/base_workspace/ithinkican/BUILD
java_binary(
        name = "ithinkican",
        srcs = glob(["**/*.java"]),
        main_class = "com.core.SPI",
	deps = [":pi4j"]
)

java_import(
	name = "pi4j",
	jars = [

		"misc/pi4j-1.0-RC/lib/pi4j-core.jar",
		"misc/pi4j-1.0-RC/lib/pi4j-core-javadoc.jar",
		"misc/pi4j-1.0-RC/lib/pi4j-core-sources.jar",
		"misc/pi4j-1.0-RC/lib/pi4j-device.jar",
		"misc/pi4j-1.0-RC/lib/pi4j-device-javadoc.jar",
		"misc/pi4j-1.0-RC/lib/pi4j-device-sources.jar",
		"misc/pi4j-1.0-RC/lib/pi4j-gpio-extension.jar",
		"misc/pi4j-1.0-RC/lib/pi4j-gpio-extension-javadoc.jar",
		"misc/pi4j-1.0-RC/lib/pi4j-gpio-extension-sources.jar",
		"misc/pi4j-1.0-RC/lib/pi4j-service.jar",
		"misc/pi4j-1.0-RC/lib/pi4j-service-javadoc.jar",
		"misc/pi4j-1.0-RC/lib/pi4j-service-sources.jar"
	],
)



