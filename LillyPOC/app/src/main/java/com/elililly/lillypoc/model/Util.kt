package com.elililly.lillypoc.model

import android.bluetooth.BluetoothAdapter
import android.content.Context
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

const val EMPTY: String = ""

fun isValidEmail(email: String) : Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isBluetoothEnabled(): Boolean {
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    return bluetoothAdapter.isEnabled
}

fun saveDataIntoFile(context: Context, data: String): Boolean {
    try {
        /**
         * For external storage
         * manifest -> <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
         * external folder dir -> val directoryPath = context.getExternalFilesDir(null)
         *
         * Below logic is to write data into internal storage
         */

        val directoryPath = context.filesDir
        val directory = File(directoryPath, "elililly")
        var folderExist = true
        if (!directory.exists()) {
            folderExist = directory.mkdir()
        }

        if (folderExist) {
            val lillyFile = File(directory, "lilly.txt")
            FileOutputStream(lillyFile).use {
                it.write(data.toByteArray())
            }
        }
        return true
    } catch (e: FileNotFoundException){
        e.printStackTrace()
    }catch (e: NumberFormatException){
        e.printStackTrace()
    }catch (e: IOException){
        e.printStackTrace()
    }catch (e: Exception){
        e.printStackTrace()
    }
    return false
}