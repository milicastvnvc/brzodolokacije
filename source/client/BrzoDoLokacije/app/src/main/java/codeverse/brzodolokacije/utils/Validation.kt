package codeverse.brzodolokacije.utils

import android.text.TextUtils
import android.util.Patterns

class Validation {

    companion object{
        fun validateUsername(username : String) : Pair<Boolean,String>{

            if(TextUtils.isEmpty(username))
                return Pair(false,"Unesite korisničko ime")

            return Pair(true,"")
        }

        fun validatePassword(password : String, passwordConfirm: String? = null) : Pair<Boolean,String>{

            if(TextUtils.isEmpty(password))
                return Pair(false,"Unesite šifru")
            if (passwordConfirm != null){
                if (password.length <= 5)
                    return Pair(false,"Šifra mora imat više od 5 karaktera")
                if (!password.equals(passwordConfirm)) {
                    return Pair(false,"Šifre moraju da se poklapaju")
                }
            }

            return Pair(true,"")
        }

        fun validateEmail(email : String) : Pair<Boolean,String>{

            if(TextUtils.isEmpty(email))
                return Pair(false,"Unesite email")
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                return Pair(false,"Unesite ispravan email")
            }
            return Pair(true,"")
        }

        fun validateName(firstName: String, lastName: String) : Pair<Boolean,String>{

            if(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName))
                return Pair(false,"Unesite ime i prezime")

            if(!isLetters(firstName) || !isLetters(lastName)){
                return Pair(false,"Ime i prezime moraju biti samo slova")
            }

            return Pair(true,"")
        }

        fun isLetters(string: String): Boolean {
            return string.all { it.isLetter() }
        }

        fun isLettersAndDigits(string: String): Boolean {
            return string.all { it.isLetter() || it.isDigit() }
        }
    }
}
