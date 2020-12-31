package com.kttasks.daos

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.`java-time`.CurrentDateTime
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import java.time.LocalDateTime

object Tasks : IntIdTable() {
    val title: Column<String> = varchar("title", 100)
    val description: Column<ExposedBlob> = blob("description")
    val dueDate: Column<LocalDateTime> = datetime("duedate")
    val completed: Column<Boolean> = bool("completed").default(false)
    val user: Column<EntityID<Int>> = reference("user", Users, onDelete = ReferenceOption.CASCADE)
    val createdAt: Column<LocalDateTime> = datetime("createdAt").defaultExpression(CurrentDateTime())
    val updatedAt: Column<LocalDateTime> = datetime("updatedAt")
}

class TaskDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<TaskDao>(Tasks)
    var title by Tasks.title
    var description by Tasks.description
    var dueDate by Tasks.dueDate
    var completed by Tasks.completed
    var user by UserDao referencedOn Tasks.user
    var createdAt by Tasks.createdAt
    var updatedAt by Tasks.updatedAt
}