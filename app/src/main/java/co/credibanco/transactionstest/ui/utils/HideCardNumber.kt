package co.credibanco.transactionstest.ui.utils

fun String.hideCardNumber(): String {
    return replaceRange(0, 12, "******")
}
