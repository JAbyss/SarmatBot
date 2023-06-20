package utils

import ServerDate
import ServerDate.fullDate
import getDaysOnWeek
import kotlinx.coroutines.*
import listBuildings
import listFacs
import listFloors
import listHeadings
import mongo.DataBase
import mongo.buildings.getAll
import mongo.facultys.getAll
import mongo.floors.getAll
import mongo.headings.getAll
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object TaskManager {

    @OptIn(DelicateCoroutinesApi::class)
    private val dispatcher = newSingleThreadContext("task-thread")

    data class Task(
        val code: String,
        val duration: Long,
        val before_action: (suspend () -> Unit)? = null,
        val after_action: suspend () -> Unit
    )

    private val tasks = ConcurrentHashMap<String, Job>()

    private fun stop(code: String) {
        tasks.remove(code)?.cancel()
    }

    private suspend fun startTask(task: Task) = CoroutineScope(dispatcher).launch {
        task.before_action?.let { it() }
        delay(task.duration)
        task.after_action()
        stop(task.code)
    }

    fun addTask(task: Task) {
        CoroutineScope(dispatcher).launch {
            tasks[task.code] = startTask(task)
            println("$fullDate Added task: ${task.code}, All tasks: ${tasks.size}")
        }
    }

    fun cancelTask(nameTask: String) {
        tasks.remove(nameTask)?.cancel()
        println("$fullDate  Deleted task: $nameTask, All tasks: ${tasks.size}")
    }
}

fun recursTask(delay: Long) {
    val id = UUID.randomUUID().toString()
    TaskManager.addTask(task = TaskManager.Task(
        id,
        duration = delay,
        before_action = {
            listFacs = DataBase.Faculty.getAll().map { it.name }
            listHeadings = DataBase.Heading.getAll()
            listBuildings = DataBase.Buildings.getAll().map { it.name }
            listFloors = DataBase.Floors.getAll().map { it.name }

        }
    ) {
        getDaysOnWeek()
        recursTask(ServerDate.timeToEndDay + 1.m)
    })
}