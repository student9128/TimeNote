package com.kevin.timenote.data.repository

import com.kevin.timenote.data.local.dao.EventTypeDao
import com.kevin.timenote.data.local.entity.EventType
import com.kevin.timenote.domain.repository.EventTypeRepository
import com.kevin.timenote.ui.theme.anniversaryColor
import com.kevin.timenote.ui.theme.birthdayColor
import com.kevin.timenote.ui.theme.eventDefaultColor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventTypeRepositoryImpl @Inject constructor(
    private val dao: EventTypeDao
) : EventTypeRepository {

    override val allTypes: Flow<List<EventType>> = dao.getAllTypes()

    override suspend fun addType(name: String,color: Long) {
        dao.insertType(EventType(name = name, color = color))
    }

    override suspend fun deleteType(eventType: EventType) {
        dao.deleteType(eventType)
    }

    override suspend fun initDefaultTypes() {
        if (dao.getCount() == 0) {
            val defaults = listOf("倒数日", "生日", "纪念日")
            val defaultColors = listOf(eventDefaultColor, birthdayColor, anniversaryColor)
            defaults.forEachIndexed {index,item->
                dao.insertType(EventType(name = item, color = defaultColors[index], isSystemDefault = true))
            }
        }
    }
}
