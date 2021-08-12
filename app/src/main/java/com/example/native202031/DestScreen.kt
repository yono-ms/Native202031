package com.example.native202031

data class DestScreen(
    val route: Route,
    val args: String = ""
) {
    enum class Route(val rawValue: String) {
        HOME("home"),
        SIGN_IN("sign_in"),
        CHECK_USER("check_user"),
        COMMIT("commit/{repo}"),
        BACK("pop back stack")
    }

    val argKey = "repo"

    fun toRouteWithArgs(): String {
        return this.route.rawValue.replace("{$argKey}", args)
    }

}
