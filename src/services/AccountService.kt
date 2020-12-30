package com.kttasks.services

import com.kttasks.daos.UserDao
import com.kttasks.dtos.UserDto
import com.kttasks.repositories.UserRepository
import org.mindrot.jbcrypt.BCrypt

class AccountService {
    private val userRepository = UserRepository()

    fun signUserUp(userInfo: UserDto): Int {
        val existingUser = userRepository.findUserByUsername(userInfo.username)
        var status: Int = -1

        if (existingUser == null) {
            val hashedPassword = BCrypt.hashpw(userInfo.password, BCrypt.gensalt(10))
            val newUser = UserDto(userInfo.username, hashedPassword)

            status = userRepository.addNewUser(newUser)
        }
        return status
    }
}