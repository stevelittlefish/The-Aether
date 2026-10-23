![Banner image](assets/banner.webp)
# The Aether Lite (Fabric, Minecraft 26.3)

This is an **unofficial fork** of [The Aether](https://github.com/The-Aether-Team/The-Aether) by The Aether Team. It is a cut-down "lite" version of the mod for Fabric on Minecraft 26.3. It keeps the dimension, the mobs, the dungeons and the progression, and drops the parts that change the base game's interface.

> [!IMPORTANT]
> This fork is not made or supported by The Aether Team. Please don't report problems with it on the official issue tracker or Discord. Report them on [this fork's issue tracker](https://github.com/stevelittlefish/The-Aether-Lite/issues) instead.

## :evergreen_tree: Where this fork comes from

1. **[The Aether](https://github.com/The-Aether-Team/The-Aether)** by The Aether Team, the original mod (NeoForge).
2. **[CasseShimada/The-Aether](https://github.com/CasseShimada/The-Aether)**, a native Fabric port for Minecraft 26.2 (the `26.2-develop-fabric` branch here).
3. **This fork**, which ports that work to Minecraft 26.3, fixes a number of behaviours that were lost in the port, and builds the lite version on top.

| Branch                 | What it is                                                                 |
|------------------------|----------------------------------------------------------------------------|
| `main`                 | The latest lite release, merged from `lite`                                |
| `26.2-develop-fabric`  | CasseShimada's Fabric port for 26.2, which this fork is based on           |
| `26.3-develop-fabric`  | The full mod ported to Minecraft 26.3                                      |
| `lite`                 | The lite version for 26.3, described below                                 |

## :feather: What "lite" means

The full Aether adds its own accessory inventory: extra slots for gloves, rings, pendants, capes and miscellaneous items, with their own screen, network packets and many mixins into vanilla. It also adds trivia, a custom title screen and supporter perks. Lite removes all of that, so **the vanilla inventory and menus are left alone**. The mod is smaller, conflicts less with other mods, and fits better in lightweight or vanilla-style modpacks.

Everything else stays: the portal, the dimension and its world generation, blocks, ores, tools, weapons, armour, food, mobs, Moas, the Bronze, Silver and Gold dungeons and their bosses.

### Accessories become capes and charms

**Capes** (Red, Blue, Yellow, White, Agility, Swet, Valkyrie and the Invisibility Cloak)
- Wear a cape in the chest slot, or combine it with any chestplate at a smithing table (no template needed) to wear both.
- Craft a caped chestplate on its own to take the cape back off.
- The Invisibility Cloak uses vanilla's invisibility effect.

**Charms** work while held in the **off hand**:
- Shield of Repulsion
- Golden Feather (uses vanilla's slow falling)
- Regeneration Stone
- Iron Bubble
- Zanite Ring (faster block breaking)
- Ice Ring, now crafted from four iron or gold ingots around an Icestone

### Removed

- The accessory slots and accessory screen, along with the related mixins (accessories on mobs, Elytra and Totems in accessory slots, piglin and powder snow checks).
- All 12 gloves, the iron and gold rings, and all pendants. The Zanite and Ice pendants are merged into their rings.
- Armour set bonuses now need only the four armour pieces, since there are no gloves.
- Dungeon loot that used to contain removed items now gives ingots, saddles, name tags, enchanted books, diamonds or an enchanted golden apple.
- Trivia on the title and loading screens, the Aether title screen and menu, and the "start in the Aether" option.
- Patreon supporter perks (Moa skins, halo, developer glow) and the Patreon and Discord chat message.
- Twilight Forest charm compatibility and the migration of accessories from the NeoForge version.

> [!WARNING]
> Lite is **not save-compatible** with the full mod. Worlds made with the full Aether lose any removed items, including everything in the accessory slots. Start a new world, or move those items out before switching.

## :package: Installing

Requirements:
- Minecraft 26.3
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.5 or newer
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25

Optional integrations: [JEI](https://modrinth.com/mod/jei), [Jade](https://modrinth.com/mod/jade), [WTHIT](https://modrinth.com/mod/wthit), Diggus Maximus and Carry On.

There are no published releases of the lite version yet. Build it from source:

```
git clone https://github.com/stevelittlefish/The-Aether-Lite.git
cd The-Aether-Lite
./gradlew build
```

The mod jar is written to `build/libs/`.

## :bug: Reporting issues

Report issues on [this fork's issue tracker](https://github.com/stevelittlefish/The-Aether-Lite/issues). If you can, first check whether the same problem happens in the official mod. If it does, it may belong upstream.

## :scroll: License

[![Asset license (Unlicensed)](https://img.shields.io/badge/assets%20license-All%20Rights%20Reserved-red.svg?style=flat-square)](https://en.wikipedia.org/wiki/All_rights_reserved)
[![Code license (LGPL v3.0)](https://img.shields.io/badge/code%20license-LGPL%20v3.0-green.svg?style=flat-square)](../LICENSE.txt)

This fork keeps the original licensing. The source code is under the [LGPL v3.0](../LICENSE.txt). **All assets of The Aether are unlicensed and all rights are reserved to them by The Aether Team and their respective authors.** See [LICENSE-ASSETS.txt](../LICENSE-ASSETS.txt). The Aether name and branding belong to The Aether Team.

## :heart: Credits and supporting the original

All credit for The Aether goes to [The Aether Team](https://github.com/The-Aether-Team) and their contributors. Credit for the Fabric port goes to [CasseShimada](https://github.com/CasseShimada). The full list of code contributors is in [CREDITS.txt](CREDITS.txt).

If you enjoy the mod, please support the people who made it:

[![Patreon](https://img.shields.io/endpoint.svg?url=https%3A%2F%2Fshieldsio-patreon.vercel.app%2Fapi%3Fusername%3DTheAetherTeam%26type%3Dpledges&style=flat-square&logoColor=white)](https://patreon.com/TheAetherTeam)
[![Discord](https://img.shields.io/discord/118816101936267265.svg?label=discord&logoColor=FFFFFF&logo=discord&color=7289DA&style=flat-square)](https://discord.gg/aethermod)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/YhmgMVyu?color=00AF5C&logo=modrinth)](https://modrinth.com/mod/aether)
[![wiki.gg](https://custom-icon-badges.demolab.com/badge/wiki.gg-aether-green?logo=wikigg&style=flat-square&color=FF1980)](https://aether.wiki.gg/)

<details>
<summary>:speech_balloon: Translators</summary>

The translations come from the official mod's [Crowdin](https://translate.aether.devaether.com/), thanks to these contributors:

| Language                    | Locale  | Translators                                                                                                |
|-----------------------------|---------|------------------------------------------------------------------------------------------------------------|
| Arabic                      | (ar_sa) | Leopotion                                                                                                  |
| Austrian German             | (de_at) | Builderdog                                                                                                 |
| Brazilian Portuguese        | (pt_br) | Wanadi, Metal_knight, ZippetTV, CavaleiroDev, Jay_See, cacovil08, arthurdepaulafreitas, leparkorsz         |
| Chinese Simplified          | (zh_cn) | ETStareak, Parallel011, Haku98, minecraftXYZ, LuminaHeinsborough                                           |
| Chinese Traditional         | (zh_tw) | y830705                                                                                                    |
| Czech                       | (cs_cz) | Aerod, pomi108, bruno.sulc602                                                                              |
| Dutch                       | (nl_nl) | Critical, Morgan Hofmann, WarpedCarrot                                                                     |
| European French             | (fr_fr) | lololoustau, Beethov46, Aethyus, HidaSharker, ninjdai                                                      |
| German                      | (de_de) | HazeWorksOnFiveMServer, Catchears, A1403, DarkBread, KaiLeon8789, alleseinfacher, magiczocker, TotallyABot |
| Greek                       | (el_gr) | Manthos3gr                                                                                                 |
| Hungarian                   | (hu_hu) | HedSut                                                                                                     |
| Indonesian                  | (id_id) | F640                                                                                                       |
| Italian                     | (it_it) | fabiross096                                                                                                |
| Japanese                    | (ja_jp) | baguchan, falogonia, yukinosuke.tokyo                                                                      |
| Kazakh                      | (kk_kz) | snowcore                                                                                                   |
| Korean                      | (ko_kr) | minejango2, alligggg44, DataCenter1216, hyuzes, Cinam, smoong951                                           |
| Lithuanian                  | (lt_lt) | WaryRGMCA                                                                                                  |
| Malay                       | (ms_my) | HoodyP                                                                                                     |
| Mexican Spanish             | (es_mx) | Aer-ink, Kidi, Looxond                                                                                     |
| Polish                      | (pl_pl) | DarkKnightComes, Cinex20, damianek1984, mati.makosie, Pufferfish                                           |
| Romanian                    | (ro_ro) | lucatatar321, gigelciobanu205                                                                              |
| Russian                     | (ru_ru) | LeyxorCheysen, andreyalex1236, LunarP1, zombi1944, Foxgreat, lonespeaker, davekart0n, paskanius, AEZAMF    |
| Slovak                      | (sk_sk) | GaucovyAero                                                                                                |
| Spanish                     | (es_es) | cabaneter                                                                                                  |
| Swedish                     | (sv_se) | magicus, 2lay                                                                                              |
| Thai                        | (th_th) | txwfyxa                                                                                                    |
| Toki Pona                   | (tok)   | leko soko Wijapipapa                                                                                       |
| Turkish                     | (tr_tr) | Dragunovi, egeyesss                                                                                        |
| Ukrainian                   | (uk_ua) | Nerkin, un_roman                                                                                           |
| Upside Down British English | (en_ud) | magiczocker                                                                                                |
| Vietnamese                  | (vi_vn) | godkyo98                                                                                                   |
| Welsh                       | (cy_gb) | Plague von Karma                                                                                           |

Strings for removed features (trivia, gloves, pendants and so on) have been dropped from the language files.

</details>
