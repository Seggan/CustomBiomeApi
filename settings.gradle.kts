rootProject.name = "CustomBiomeApi"

val versions: String by settings

include("lib", "api", *versions.split(',').map { "v" + it.replace('.', '_') }.toTypedArray())