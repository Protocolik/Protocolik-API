package com.github.protocolik.api.minecraft.world

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class Painting(
        val id: Int,
        val namespace: String
) {
    KEBAB(0, "Kebab"),
    AZTEC(1, "Aztec"),
    ALBAN(2, "Alban"),
    AZTEC2(3, "Aztec2"),
    BOMB(4, "Bomb"),
    PLANT(5, "Plant"),
    WASTELAND(6, "Wasteland"),
    POOL(7, "Pool"),
    COURBET(8, "Courbet"),
    SEA(9, "Sea"),
    SUNSET(10, "Sunset"),
    CREEBET(11, "Creebet"),
    WANDERER(12, "Wanderer"),
    GRAHAM(13, "Graham"),
    MATCH(14, "Match"),
    BUST(15, "Bust"),
    STAGE(16, "Stage"),
    VOID(17, "Void"),
    SKULL_AND_ROSES(18, "SkullAndRoses"),
    WITHER(19, "Wither"),
    FIGHTERS(20, "Fighters"),
    POINTER(21, "Pointer"),
    PIG_SCENE(22, "Pigscene"),
    BURNING_SKULL(23, "BurningSkull"),
    SKELETON(24, "Skeleton"),
    DONKEY_KONG(25, "DonkeyKong");

    companion object {
        private val byId = values().map { it.id to it }.toMap(Int2ObjectOpenHashMap())
        private val byNamespace = values().map { it.namespace to it }.toMap()

        @JvmStatic
        operator fun get(id: Number): Painting =
                byId[id.toInt()] ?: error("Unknown Painting with id: $id")

        @JvmStatic
        operator fun get(namespace: String): Painting =
                byNamespace[namespace] ?: error("Unknown Painting with namespace: $namespace")
    }
}