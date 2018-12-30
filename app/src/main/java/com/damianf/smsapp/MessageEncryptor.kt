package com.damianf.smsapp

class MessageEncryptor : IEncryptor {
    val switch = 5
    override fun encrypt(message: String):String {
        val list = mutableListOf<Char>()
        message.forEach {
            list.add((it.toInt() + switch).toChar())

        }

        return String(list.toCharArray())
    }

    override fun decrypt(message: String): String {
        val list = mutableListOf<Char>()
        message.forEach {
            list.add((it.toInt() - switch).toChar())
        }
        return String(list.toCharArray())
    }
}