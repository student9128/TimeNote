package com.kevin.timenote.domain.repository

import com.kevin.timenote.data.local.entity.EventType
import kotlinx.coroutines.flow.Flow

interface EventTypeRepository {
    val allTypes: Flow<List<EventType>>
    suspend fun addType(name: String,color: Long=0xff2196F3)
    suspend fun deleteType(eventType: EventType)
    suspend fun initDefaultTypes() // 初始化默认数据
}