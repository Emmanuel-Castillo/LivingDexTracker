package com.emmanuelcastillo.livingdextracker.utils.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Entity to store user caught pokemon
@Entity(
    tableName = "UserPokemon", foreignKeys = [
        ForeignKey(
            entity = PokemonGameEntry::class,
            parentColumns = ["gameEntryId"],
            childColumns = ["gameEntryId"]
        ),
    ForeignKey(entity = PokemonGame::class,
        parentColumns = ["gameId"],
        childColumns = ["gameId"])
    ]
)
data class UserPokemon(
    @PrimaryKey(autoGenerate = true)
    val userPokemonId: Int = 0,
    val gameEntryId: Int,
    val gameId: Int
)

// Table to store pokemon entries depending on form and game
@Entity(
    tableName = "PokemonGameEntry",
    foreignKeys = [
        ForeignKey(
            entity = Pokedex::class,
            parentColumns = ["pokedexId"],
            childColumns = ["pokedexId"]
        ),
        ForeignKey(
            entity = PokemonVariant::class,
            parentColumns = ["variantId"],
            childColumns = ["variantId"]
        )
    ]
)
data class PokemonGameEntry(
    @PrimaryKey(autoGenerate = true)
    val gameEntryId: Int = 0,
    val pokedexId: Int,
    val variantId: Int,     // References PokemonVariants.variantId
    val pokedexOrderNumber: Int
)

// Table to lookup pokemon game details
@Entity(
    tableName = "PokemonGame",
    foreignKeys = [
        ForeignKey(PokemonRegion::class,
            parentColumns = ["region"],
            childColumns = ["region"])
    , ForeignKey(Pokedex::class,
        parentColumns = ["pokedexId"],
        childColumns = ["pokedexId"])]
)
data class PokemonGame(
    @PrimaryKey(autoGenerate = true)
    val gameId: Int = 0,
    val name: String,
    val generation: Int,
    val region: String,
    val pokedexApiId: Int,
    val pokedexId: Int
)

@Entity(tableName = "Pokedex")
data class Pokedex(
    @PrimaryKey(autoGenerate = true)
    val pokedexId: Int = 0,
    val pokedexName: String
    )

// Table that stores Pokemon regions
@Entity(
    tableName = "PokemonRegion"
)
data class PokemonRegion(
    @PrimaryKey
    val region: String
)

// Store locations for every game
@Entity(
    tableName = "GameLocation",
    foreignKeys = [ForeignKey(
        entity = PokemonRegion::class,
        parentColumns = ["region"],
        childColumns = ["region"]
    )]
)
data class GameLocation(
    @PrimaryKey(autoGenerate = true)
    val locationId: Int = 0,
    val locationName: String,
    val region: String,
)

// Storing anchors for each location
@Entity(
    tableName = "LocationAnchor",
    foreignKeys = [ForeignKey(
        entity = GameLocation::class,
        parentColumns = ["locationId"],
        childColumns = ["locationId"],
    )]
)
data class LocationAnchor(
    @PrimaryKey(autoGenerate = true)
    val anchorId: Int = 0,
    val locationId: Int,
    val anchorName: String
)

// Table to store all pokemon encounters
@Entity(
    tableName = "PokemonEncounter",
    foreignKeys = [
        ForeignKey(
            entity = PokemonGameEntry::class,
            parentColumns = ["gameEntryId"],
            childColumns = ["gameEntryId"]
        ),
        ForeignKey(
            entity = GameLocation::class,
            parentColumns = ["locationId"],
            childColumns = ["locationId"]
        ),
        ForeignKey(
            entity = LocationAnchor::class,
            parentColumns = ["anchorId"],
            childColumns = ["anchorId"]
        ),
    ForeignKey(
        entity = PokemonGame::class,
        parentColumns = ["gameId"],
        childColumns = ["gameExclusive"]
    )
    ]
)
//FIX: GAME EXCLUSIVITY
data class PokemonEncounter(
    @PrimaryKey(autoGenerate = true)
    val encounterId: Int = 0,
    val method: String,
    val timeOfDay: String,
    val itemNeeded: String?,
    val requisite: String?,
    val chance: String,
    val gameEntryId: Int,
    val locationId: Int,
    val anchorId: Int,
    val gameExclusive: Int?
)

// Table to store all variants for all pokemon
@Entity(
    tableName = "PokemonVariants",
    foreignKeys = [
        ForeignKey(
            entity = Pokemon::class,
            parentColumns = ["nationalDexId"],
            childColumns = ["nationalDexId"]
        ),
        ForeignKey(
            entity = RegionalVariant::class,
            parentColumns = ["rvId"],
            childColumns = ["rvId"]
        )
    ]
)
data class PokemonVariant(
    @PrimaryKey(autoGenerate = true)
    val variantId: Int = 0,
    val variantName: String,
    val nationalDexId: Int,
    val rvId: Int,
    val isDefault: Boolean,
    val type1: String,
    val type2: String?,
    val ability1: String,
    val ability2: String?,
    val hiddenAbility: String?,
    val heightDecimetres: Int,
    val weightHectograms: Int,
    val hpBaseStat: Int,
    val atkBaseStat: Int,
    val defBaseStat: Int,
    val spAtkBaseStat: Int,
    val spDefBaseStat: Int,
    val speedBaseStat: Int,
    val cry: String,
    val sprite: String
)

// Keeps track of all Pokemon regional variants (Alolan, Galarian, Hisuian, Paldean, etc)
@Entity(tableName = "RegionalVariants",
    foreignKeys = [ForeignKey(PokemonRegion::class,
        parentColumns = ["region"],
        childColumns = ["nativeRegion"])])
data class RegionalVariant(
    @PrimaryKey(autoGenerate = true)
    val rvId: Int = 0,
    val name: String?,
    val nativeRegion: String?
)

// Holds which games inlcude which regional variants, and sets priority on which variants to
// display in the living dex
@Entity(
    tableName = "RegionalVariantAvailability",
    foreignKeys = [ForeignKey(
        RegionalVariant::class,
        parentColumns = ["rvId"],
        childColumns = ["rvId"]
    ),
        ForeignKey(PokemonGame::class, parentColumns = ["gameId"], childColumns = ["gameId"])]
)
data class RegionalVariantAvailability(
    @PrimaryKey(autoGenerate = true)
    val rvaId: Int = 0,
    val gameId: Int,
    val rvId: Int,
    val priority: Int
)

// Entities to store Pokemon dex id, and name
@Entity(tableName = "Pokemon")
data class Pokemon(
    @PrimaryKey
    val nationalDexId: Int,
    val name: String
)
