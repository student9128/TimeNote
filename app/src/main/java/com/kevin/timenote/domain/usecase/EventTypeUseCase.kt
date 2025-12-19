package com.kevin.timenote.domain.usecase

import com.kevin.timenote.data.local.entity.EventType
import com.kevin.timenote.domain.repository.EventTypeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventTypeUseCase @Inject constructor(
    private val repository: EventTypeRepository
) {
    // 获取所有类型
    fun observeAllTypes(): Flow<List<EventType>> = repository.allTypes

    // 添加新类型
    suspend fun addType(name: String) {
        if (name.isNotBlank()) {
            repository.addType(name)
        }
    }
    // 初始化默认数据 (可以在 App 启动或 ViewModel init 中调用)
    suspend fun initDefaultTypes() {
        repository.initDefaultTypes()
    }
}