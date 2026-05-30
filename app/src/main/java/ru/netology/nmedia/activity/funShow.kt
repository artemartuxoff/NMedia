package ru.netology.nmedia.activity

fun showAmount(amount: Int): String {
    return when {
        (amount >= 1100000) -> ((amount / 1000000.0 * 10).toInt() / 10.0).toString() + "M"
        (amount >= 1000000) -> (amount / 1000000).toString() + "M"
        (amount >= 10000) -> (amount / 1000).toString() + "K"
        (amount >= 1100) -> ((amount / 1000.0 * 10).toInt() / 10.0).toString() + "K"
        (amount >= 1000) -> (amount / 1000).toString() + "K"
        else -> amount.toString()
    }
}