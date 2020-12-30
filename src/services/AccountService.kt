package com.kttasks.services

import com.kttasks.dtos.UserDto
import com.kttasks.repositories.UserRepository
import org.mindrot.jbcrypt.BCrypt

class AccountService(private val tokenService: TokenService) {
    private val userRepository = UserRepository()

    fun signUp(userInfo: UserDto): Int {
        val existingUser = userRepository.findUserByUsername(userInfo.username)
        var status: Int = -1

        if (existingUser == null) {
            val hashedPassword = BCrypt.hashpw(userInfo.password, BCrypt.gensalt(10))
            val newUser = UserDto(userInfo.username, hashedPassword)

            status = userRepository.addNewUser(newUser)
        }
        return status
    }

    fun signIn(userInfo: UserDto): String? {
        val existingUser = userRepository.findUserByUsername(userInfo.username) ?: return null
        val doPasswordsMatch = BCrypt.checkpw(userInfo.password, existingUser.hashedPassword)

        if (!doPasswordsMatch) {
            return null
        }

        return tokenService.generateToken(userInfo)
    }
}