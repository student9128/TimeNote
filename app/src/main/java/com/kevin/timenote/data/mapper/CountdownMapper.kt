package com.kevin.timenote.data.mapper

import com.kevin.timenote.data.local.entity.CountdownEntity
import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.ui.countdown.CountdownEditUiState

fun CountdownEntity.toDomain(): CountdownModel =
    CountdownModel(
        id = id,
        title = title,
        location = location,
        type = type,
        startTime = startTime,
        endTime = endTime,
        date = date,
        lunarDate = lunarDate,
        isLunar = isLunar,
        eventTypeName = eventTypeName,
        eventTypeColor = eventTypeColor,
        repeatMode = repeatMode,
        remind = remind
    )

fun CountdownModel.toEntity(): CountdownEntity =
    CountdownEntity(
        id = id,
        title = title,
        location = location,
        type = type,
        startTime = startTime,
        endTime = endTime,
        date = date,
        lunarDate = lunarDate,
        isLunar = isLunar,
        eventTypeName = eventTypeName,
        eventTypeColor = eventTypeColor,
        repeatMode = repeatMode,
        remind = remind
    )

fun CountdownModel.toEditState(): CountdownEditUiState =
    CountdownEditUiState(
        id = id,
        title = title,
        location = location,
        type = type,
        startTime = startTime,
        endTime = endTime,
        date = date,
        lunarDate = lunarDate,
        isLunar = isLunar,
        eventTypeName = eventTypeName,
        eventTypeColor = eventTypeColor,
        repeatMode = repeatMode,
        remind = remind
    )

fun CountdownEditUiState.toDomain(): CountdownModel =
    CountdownModel(
        id = id,
        title = title,
        location = location,
        type = type,
        startTime = startTime,
        endTime = endTime,
        date = date,
        lunarDate = lunarDate,
        isLunar = isLunar,
        eventTypeName = eventTypeName,
        eventTypeColor = eventTypeColor,
        repeatMode = repeatMode,
        remind = remind
    )
