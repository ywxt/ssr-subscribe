package ywxt.ssr.subscribe.config

import com.fasterxml.jackson.annotation.JsonProperty

data class ConfigFile(
    @JsonProperty("defaultLocal")
    val defaultLocalConfig: LocalConfig,
    @JsonProperty("subscription")
    val subscriptionConfig: SubscriptionConfig
) {
    companion object {
        const val fileName = "setting.json"
        const val path = "~/.ssr-sub/"

        fun from(file: String = path + fileName): ConfigFile {

        }
    }

}