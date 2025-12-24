package com.kevin.timenote.data.mapper

import com.kevin.timenote.data.local.entity.CountdownEntity
import com.kevin.timenote.domain.model.CountdownModel

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
        repeatMode = repeatMode
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
        repeatMode = repeatMode
    )
