package com.github.protocolik.api.protocol

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class ProtocolVersion(
        val id: Int,
        val displayName: String
) {
    // Before netty rewrite
    RELEASE_1_0(22, "1.0"),
    RELEASE_1_0_1(22, "1.0.1"),
    RELEASE_1_1(23, "1.1"),
    RELEASE_1_2_1(28, "1.2.1"),
    RELEASE_1_2_2(28, "1.2.2"),
    RELEASE_1_2_3(28, "1.2.3"),
    RELEASE_1_2_4(29, "1.2.4"),
    RELEASE_1_2_5(29, "1.2.5"),
    RELEASE_1_3_1(39, "1.3.1"),
    RELEASE_1_3_2(39, "1.3.2"),
    RELEASE_1_4_2(47, "1.4.2"),
    RELEASE_1_4_3(48, "1.4.3"),
    RELEASE_1_4_4(49, "1.4.4"),
    RELEASE_1_4_5(49, "1.4.5"),
    RELEASE_1_4_6(51, "1.4.6"),
    RELEASE_1_4_7(51, "1.4.7"),
    RELEASE_1_5(60, "1.5"),
    RELEASE_1_5_1(60, "1.5.1"),
    RELEASE_1_5_2(61, "1.5.2"),
    RELEASE_1_6_1(73, "1.6.1"),
    RELEASE_1_6_2(74, "1.6.2"),
    RELEASE_1_6_4(78, "1.6.4"),

    // After netty rewrite
    RELEASE_1_7(4, "1.7"),
    RELEASE_1_7_1(4, "1.7.1"),
    RELEASE_1_7_2(4, "1.7.2"),
    RELEASE_1_7_3(4, "1.7.3"),
    RELEASE_1_7_4(4, "1.7.4"),
    RELEASE_1_7_5(4, "1.7.5"),
    RELEASE_1_7_6(5, "1.7.6"),
    RELEASE_1_7_7(5, "1.7.7"),
    RELEASE_1_7_8(5, "1.7.8"),
    RELEASE_1_7_9(5, "1.7.9"),
    RELEASE_1_7_10(5, "1.7.10"),
    RELEASE_1_8(47, "1.8"),
    RELEASE_1_8_1(47, "1.8.1"),
    RELEASE_1_8_2(47, "1.8.2"),
    RELEASE_1_8_3(47, "1.8.3"),
    RELEASE_1_8_4(47, "1.8.4"),
    RELEASE_1_8_5(47, "1.8.5"),
    RELEASE_1_8_6(47, "1.8.6"),
    RELEASE_1_8_7(47, "1.8.7"),
    RELEASE_1_8_8(47, "1.8.8"),
    RELEASE_1_8_9(47, "1.8.9"),
    RELEASE_1_9(107, "1.9"),
    RELEASE_1_9_1(108, "1.9.1"),
    RELEASE_1_9_2(109, "1.9.2"),
    RELEASE_1_9_3(110, "1.9.3"),
    RELEASE_1_9_4(110, "1.9.4"),
    RELEASE_1_10(210, "1.10"),
    RELEASE_1_10_1(210, "1.10.1"),
    RELEASE_1_10_2(210, "1.10.2"),
    RELEASE_1_11(315, "1.11"),
    RELEASE_1_11_1(316, "1.11.1"),
    RELEASE_1_11_2(316, "1.11.2"),
    RELEASE_1_12(335, "1.12"),
    RELEASE_1_12_1(338, "1.12.1"),
    RELEASE_1_12_2(340, "1.12.2"),
    RELEASE_1_13(393, "1.13"),
    RELEASE_1_13_1(401, "1.13.1"),
    RELEASE_1_13_2(404, "1.13.2"),
    RELEASE_1_14(477, "1.14"),
    RELEASE_1_14_1(480, "1.14.1"),
    RELEASE_1_14_2(485, "1.14.2"),
    RELEASE_1_14_3(490, "1.14.3"),
    RELEASE_1_14_4(498, "1.14.4"),
    RELEASE_1_15(573, "1.15"),
    RELEASE_1_15_1(575, "1.15.1"),
    RELEASE_1_15_2(578, "1.15.2"),
    ;

    val versionInfo = VersionInfo(id, displayName)

    companion object {
        val CURRENT = RELEASE_1_12_2
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())
        private val byDisplayName = values.map { it.displayName to it }.toMap()

        @JvmStatic
        operator fun get(id: Number): ProtocolVersion =
                byId[id] ?: error("Unknown ProtocolVersion with id: $id")

        @JvmStatic
        operator fun get(name: String): ProtocolVersion =
                byDisplayName[name] ?: error("Unknown ProtocolVersion with name: $name")
    }
}