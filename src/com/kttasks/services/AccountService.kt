package com.kttasks.services

import com.kttasks.dtos.UserDto
import com.kttasks.repositories.UserRepository
import org.mindrot.jbcrypt.BCrypt

class AccountService(private val tokenService: TokenService? = null) {
    private val userRepository = UserRepository()

    fun signUp(userInfo: UserDto): Int {
        val existingUser = userRepository.findUserByUsername(userInfo.username)

        if (existingUser != null) {
            return -1
        }

        val hashedPassword = BCrypt.hashpw(userInfo.password, BCrypt.gensalt(10))
        val newUser = UserDto(userInfo.username, hashedPassword)

        return userRepository.addNewUser(newUser)
    }

    fun signIn(userInfo: UserDto): String? {
        val existingUser = userRepository.findUserByUsername(userInfo.username) ?: return null
        val doPasswordsMatch = BCrypt.checkpw(userInfo.password, existingUser.hashedPassword)

        if (!doPasswordsMatch) {
            return null
        }

        return tokenService?.generateToken(userInfo)
    }

    fun updatePassword(userInfo: UserDto): Boolean {
        return userRepository.updatePassword(userInfo)
    }
}