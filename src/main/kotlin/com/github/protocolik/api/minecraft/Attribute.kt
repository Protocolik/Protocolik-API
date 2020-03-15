package com.github.protocolik.api.minecraft

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.util.*

class Attribute(
        val type: Type,
        val value: Double,
        val modifiers: List<Modifier>
) {
    enum class Type(
            val namespace: String
    ) {
        GENERIC_MAX_HEALTH("generic.maxHealth"),
        GENERIC_FOLLOW_RANGE("generic.followRange"),
        GENERIC_KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
        GENERIC_MOVEMENT_SPEED("generic.movementSpeed"),
        GENERIC_ATTACK_DAMAGE("generic.attackDamage"),
        GENERIC_ATTACK_SPEED("generic.attackSpeed"),
        GENERIC_ARMOR("generic.armor"),
        GENERIC_ARMOR_TOUGHNESS("generic.armorToughness"),
        GENERIC_LUCK("generic.luck"),
        GENERIC_FLYING_SPEED("generic.flyingSpeed"),
        HORSE_JUMP_STRENGTH("horse.jumpStrength"),
        ZOMBIE_SPAWN_REINFORCEMENTS("zombie.spawnReinforcements");

        companion object {
            val values = values()
            private val byNamespace = values.map { it.namespace to it }.toMap()

            @JvmStatic
            operator fun get(namespace: String): Type =
                    byNamespace[namespace] ?: error("Unknown Attribute with namespace: '$namespace'")
        }
    }

    class Modifier(
            val type: Type,
            val amount: Double,
            val operation: Operation
    ) {
        enum class Type(
                val uuid: UUID
        ) {
            CREATURE_FLEE_SPEED_BONUS(UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A")),
            ENDERMAN_ATTACK_SPEED_BOOST(UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0")),
            SPRINT_SPEED_BOOST(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D")),
            PIGZOMBIE_ATTACK_SPEED_BOOST(UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718")),
            WITCH_DRINKING_SPEED_PENALTY(UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E")),
            ZOMBIE_BABY_SPEED_BOOST(UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836")),
            ATTACK_DAMAGE_MODIFIER(UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF")),
            ATTACK_SPEED_MODIFIER(UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3")),
            SPEED_POTION_MODIFIER(UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070635")),
            HEALTH_BOOST_POTION_MODIFIER(UUID.fromString("5D6F0BA2-1186-46AC-B896-C61C5CEE99CC")),
            SLOW_POTION_MODIFIER(UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890")),
            STRENGTH_POTION_MODIFIER(UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A9")),
            WEAKNESS_POTION_MODIFIER(UUID.fromString("22653B89-116E-49DC-9B6B-9971489B5BE5")),
            HASTE_POTION_MODIFIER(UUID.fromString("AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3")),
            MINING_FATIGUE_POTION_MODIFIER(UUID.fromString("55FCED67-E92A-486E-9800-B47F202C4386")),
            LUCK_POTION_MODIFIER(UUID.fromString("03C3C89D-7037-4B42-869F-B146BCB64D2E")),
            UNLUCK_POTION_MODIFIER(UUID.fromString("CC5AF142-2BD2-4215-B636-2605AED11727")),
            BOOTS_MODIFIER(UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B")),
            LEGGINGS_MODIFIER(UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D")),
            CHESTPLATE_MODIFIER(UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E")),
            HELMET_MODIFIER(UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")),
            COVERED_ARMOR_BONUS(UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F"));

            companion object {
                val values = values()
                private val byUuid = values.map { it.uuid to it }.toMap()

                @JvmStatic
                operator fun get(uuid: UUID): Type =
                        byUuid[uuid] ?: error("Unknown Modifier Type with UUID: $uuid")
            }
        }

        enum class Operation(
                val id: Int
        ) {
            ADD(0),
            ADD_MULTIPLIED(1),
            MULTIPLY(2);

            companion object {
                val values = values()
                private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

                @JvmStatic
                operator fun get(id: Number): Operation =
                        byId[id.toInt()] ?: error("Unknown Operation with id: $id")
            }
        }
    }
}
