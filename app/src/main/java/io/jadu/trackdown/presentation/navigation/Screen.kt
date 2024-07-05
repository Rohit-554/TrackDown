package io.jadu.trackdown.presentation.navigation

sealed class Screen(val route:String) {
    data object StockApp: Screen("stocksApp")
    data object CompanyDetail: Screen("InfoDetails")
    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg->
                append("/$arg")
            }
        }
    }
}