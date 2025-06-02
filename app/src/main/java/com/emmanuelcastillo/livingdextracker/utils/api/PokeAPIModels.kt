// Response for Pokedex Api Call
data class PokedexResponse(
    val id: Int,
    val name: String,
    val pokemon_entries: List<PokemonEntry>
)

// pokemon_entries element
data class PokemonEntry(
    // number assigned relative to pokedex
    val entry_number: Int,
    val pokemon_species: PokemonSpecies
)

// pokemon_species data class
data class PokemonSpecies(
    val name: String,
    val url: String
)

// Response from PokemonSpecies url call
data class PokemonResponse (
    // national pokedex number
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val abilities: List<PokemonAbility>,
    val location_area_encounters: String,
    val moves: List<PokemonMove>,
    val cries: PokemonCries,
    val stats: List<PokemonStat>,
    val types: List<PokemonType>,
    val sprites: PokemonSprites
)

data class PokemonAbility (
    val is_hidden: Boolean,
    val slot: Int,
    val ability: NamedAPIResource
)

data class Ability (
    val id: Int,
    val name: String,
    val effect_entries: VerboseEffect
)

data class PokemonMove (
    val move: NamedAPIResource,
    val version_group_details: List<PokemonMoveVersion>
)

data class PokemonMoveVersion (
    val move_learn_method: NamedAPIResource,
    val version_group: NamedAPIResource,
    val level_learned_at: Int
)

data class PokemonCries (
    val latest: String,
    val legacy: String
)

data class PokemonStat (
    val stat: NamedAPIResource,
    val effort: Int,
    val base_stat: Int
)

data class PokemonType (
    val slot: Int,
    val type: NamedAPIResource
)

data class PokemonSprites(
    val front_default: String
)

data class PokemonLocationArea (
    val location_area: NamedAPIResource,
    val version_details: List<VersionEncounterDetail>
)

data class VersionEncounterDetail (
    val version: NamedAPIResource,
    val max_chance: Int,
    val encounter_details: List<Encounter>
)

data class Encounter (
    val min_level: Int,
    val max_level: Int,
    val condition_values: List<NamedAPIResource>,
    val chance: Int,
    val method: NamedAPIResource
)

data class VerboseEffect (
    val effect: String,
    val short_effect: String
)

data class NamedAPIResource (
    val name: String,
    val url: String
)

data class APIResource (
    val url: String
)

// Species of Pokemon to grab all variants
data class PokemonSpeciesResponse (
    val id: Int,
    val name: String,
    val order: Int,
    val gender_rate: Int,
    val capture_rate: Int,
    val base_happiness: Int,
    val is_baby: Boolean,
    val is_legendary: Boolean,
    val is_mythical: Boolean,
    val hatch_counter: Int,
    val has_gender_differences: Boolean,
    val forms_switchable: Boolean,
    val growth_rate: NamedAPIResource,
    val pokedex_numbers: List<PokemonSpeciesDexEntry>,
    val egg_groups: List<NamedAPIResource>,
    val color: NamedAPIResource,
    val shape: NamedAPIResource,
    val evolves_from_species: NamedAPIResource,
    val evolution_chain: APIResource,
    val habitat: NamedAPIResource,
    val generation: NamedAPIResource,
    val names: List<Name>,
    val pal_park_encounters: List<PalParkEncounterArea>,
    val flavor_text_entries: List<FlavorText>,
    val form_descriptions: List<Description>,
    val genera: List<Genus>,
    val varieties: List<PokemonSpeciesVariety>
)

data class PokemonSpeciesDexEntry (
    val entry_number: Int,
    val pokedex: NamedAPIResource
)

data class Name(
    val name: String,
    val url: String
)

data class PalParkEncounterArea(
    val base_score: Int,
    val rate: Int,
    val area: NamedAPIResource
)

data class FlavorText(
    val flavor_text: String,
    val language: NamedAPIResource,
    val version: NamedAPIResource
)

data class Description(
    val description: String,
    val language: NamedAPIResource
)

data class Genus(
    val genus: String,
    val language: NamedAPIResource
)

data class PokemonSpeciesVariety(
    val is_default: Boolean,
    val pokemon: NamedAPIResource
)