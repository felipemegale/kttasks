package com.kttasks.repositories

import com.kttasks.daos.UserDao
import com.kttasks.daos.Users
import com.kttasks.dtos.UserDto
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {
    constructor() {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    fun findUserByUsername(username: String): UserDao? {
        var user: UserDao? = null
        transaction {
            addLogger(StdOutSqlLogger)
            val userFromDb = UserDao.find { Users.username eq username }.firstOrNull()
            user = userFromDb
        }
        return user
    }

    fun addNewUser(newUser: UserDto): Int {
        var addedId: Int = -1

        transaction {
            addLogger(StdOutSqlLogger)
            val user = UserDao.new {
                username = newUser.username
                hashedPassword = newUser.password
            }
            addedId = user.id.value
        }

        return addedId
    }
}