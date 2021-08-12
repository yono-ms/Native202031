package com.example.native202031

data class DestScreen(
    val route: Route,
    val args: String = ""
) {
    enum class Route(val rawValue: String, val argKey: String = "") {
        HOME("home"),
        SIGN_IN("sign_in"),
        CHECK_USER("check_user"),
        COMMIT("commit/{repo}", "repo"),
        BACK("pop back stack")
    }

    fun toRouteWithArgs(): String {
        return this.route.rawValue.replace("{${this.route.argKey}}", args)
    }

}
