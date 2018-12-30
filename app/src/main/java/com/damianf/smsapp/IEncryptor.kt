package com.damianf.smsapp

interface IEncryptor{
    fun encrypt(message: String): String
    fun decrypt(message: String): String

}