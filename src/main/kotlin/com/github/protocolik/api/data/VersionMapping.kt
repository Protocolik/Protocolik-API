package com.github.protocolik.api.data

import com.github.protocolik.api.dataFolder
import com.github.protocolik.api.log
import com.github.protocolik.api.protocol.ProtocolVersion
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.collections.HashMap

abstract class VersionMapping<T>(
        val clazz: Class<T>,
        private val mappingResource: String? = null
) {
    private val versionMapper = HashMap<ProtocolVersion, MappingElement<T>>()

    fun reload() {
        clear()
        if (mappingResource != null) {
            val jsonFile = File(dataFolder, mappingResource)
            if (!jsonFile.exists()) {
                jsonFile.createNewFile()
                javaClass.classLoader.getResourceAsStream(mappingResource)?.use { inputStream ->
                    jsonFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
            val json = JsonParser().parse(JsonReader(FileReader(jsonFile))).asJsonObject
            parse(json)
        }
    }

    fun clear() {
        val isEnum = clazz.isEnum
        versionMapper.clear()
        ProtocolVersion.values().forEach {
            @Suppress("UNCHECKED_CAST")
            versionMapper[it] = if (isEnum) {
                EnumElement::class.java.constructors.first().newInstance(it, clazz) as MappingElement<T>
            } else {
                ObjectElement(it)
            }
        }
    }

    open fun parse(jsonObject: JsonObject) {
        if (mappingResource == null) {
            throw UnsupportedOperationException("Mapping Resource is null")
        }
    }

    fun register(obj: T, vararg idMappers: Pair<ProtocolVersion, Int>) {
        idMappers.sortBy { -it.first.ordinal }
        val iterator = idMappers.iterator()

        val versions = ProtocolVersion.values()
        var mapper = iterator.next()
        for (i in versions.indices.reversed()) {
            val version: ProtocolVersion = versions[i]
            if (mapper.second != -1) {
                versionMapper[version]?.register(obj, mapper.second)
            }
            if (mapper.first == version) {
                if (iterator.hasNext()) {
                    val next = iterator.next()
                    if (mapper.first == next.first) {
//                        log.warning("While registering $obj object, the same versions were specified ${next.first}")
                    }
                    if (mapper.second == next.second) {
//                        log.warning("While registering $obj object, the same ids were specified ${next.second}")
                    }
                    mapper = next
                } else {
                    break
                }
            }
        }
    }

    operator fun get(protocolVersion: ProtocolVersion) = versionMapper[protocolVersion]!!

    interface MappingElement<T> {
        val version: ProtocolVersion
        val objectById: MutableMap<Int, T>
        val idByObject: MutableMap<T, Int>

        operator fun get(obj: T): Int = idByObject[obj] ?: -1

        operator fun get(id: Int): T? = objectById[id]

        fun register(obj: T, vararg ids: Int) {
            if (objectById.containsKey(ids.first()) || idByObject.containsKey(obj)) {
                log.severe("Object ${ids.first()} (0x${Integer.toHexString(ids.first())}) $obj already registered by version $version: ${objectById[ids.first()]}")
            }
            for (id in ids) {
                objectById[id] = obj
            }
            idByObject[obj] = ids.first()
        }
    }

    class ObjectElement<T>(
            override val version: ProtocolVersion
    ) : MappingElement<T> {
        override val objectById = Int2ObjectOpenHashMap<T>()
        override val idByObject = HashMap<T, Int>()
    }

    class EnumElement<E : Enum<E>>(
            override val version: ProtocolVersion,
            clazz: Class<E>
    ) : MappingElement<E> {
        override val objectById = Int2ObjectOpenHashMap<E>()
        override val idByObject = EnumMap<E, Int>(clazz)
    }
}