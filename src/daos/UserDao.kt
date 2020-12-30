package com.kttasks.daos

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.`java-time`.CurrentDateTime
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

object Users : IntIdTable() {
    val username: Column<String> = varchar("username", 50)
    val hashedPassword: Column<String> = varchar("hashedPassword", 200)
    val createdAt: Column<LocalDateTime> = datetime("createdAt").defaultExpression(CurrentDateTime())
    val updatedAt: Column<LocalDateTime?> = datetime("updatedAt").nullable()
}

class UserDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<UserDao>(Users)
    var username by Users.username
    var hashedPassword by Users.hashedPassword
    var createdAt by Users.createdAt
    var updatedAt by Users.updatedAt
}