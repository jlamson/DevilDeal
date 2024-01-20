package com.darkmoose117.devildeal.util

import com.darkmoose117.devildeal.DevilDealMod

const val IDCheckPath = "/IDCheckStringsDONT-EDIT-AT-ALL.json"

fun String.makeCardPath() = "${DevilDealMod.modID}Resources/images/cards/$this"

fun String.makeRelicPath() = "${DevilDealMod.modID}Resources/images/relics/$this"

fun String.makeRelicOutlinePath() = "${DevilDealMod.modID}Resources/images/relics/outline/$this"

fun String.makeOrbPath() = "${DevilDealMod.modID}Resources/images/orbs/$this"

fun String.makePowerPath() = "${DevilDealMod.modID}Resources/images/powers/$this"

fun String.makeEventPath() = "${DevilDealMod.modID}Resources/images/events/$this"

fun String.makeStringsFilePath() = "${DevilDealMod.modID}Resources/localization/eng/$this"