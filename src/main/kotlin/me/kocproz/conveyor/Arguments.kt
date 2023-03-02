package me.kocproz.conveyor

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

object Arguments : ArgParser("conveyor") {
    var configFile by option(ArgType.String, shortName = "c", description = "Config file path").default("config.yaml")
}
