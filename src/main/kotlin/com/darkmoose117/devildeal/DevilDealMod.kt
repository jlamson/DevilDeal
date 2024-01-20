package com.darkmoose117.devildeal

import basemod.BaseMod
import basemod.interfaces.EditCardsSubscriber
import basemod.interfaces.EditKeywordsSubscriber
import basemod.interfaces.EditStringsSubscriber
import com.badlogic.gdx.Gdx
import com.darkmoose117.devildeal.cards.DealWithTheDevil
import com.darkmoose117.devildeal.util.IDCheckDontTouchPls
import com.darkmoose117.devildeal.util.IDCheckPath
import com.darkmoose117.devildeal.util.makeStringsFilePath
import com.evacipated.cardcrawl.mod.stslib.Keyword
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.google.gson.Gson
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.unlock.UnlockTracker
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@SpireInitializer
class DevilDealMod private constructor(): EditCardsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber {

    companion object {
        val logger: Logger = LogManager.getLogger()

        lateinit var modID: String
            private set

        private const val MOD_NAME = "Deal with the Devil"
        private const val AUTHOR = "darkmoose117"
        private const val DESCRIPTION = "A curse that gives you great power... but at what cost?"

        private lateinit var instance: DevilDealMod

        /**
         * [initialize] function required by [SpireInitializer]
         */
        @Suppress("unused")
        @JvmStatic
        fun initialize() {
            logger.info("========================= [com.darkmoose117.devildeal:DevilDealMod Initializing] =========================")
            instance = DevilDealMod()
            logger.info("========================= /com.darkmoose117.devildeal:DevilDealMod Initialized/ =========================")
        }

        // region Safety Checks - DO NOT EDIT

        @Suppress("SameParameterValue")
        private fun setModId(id: String) {
            logger.info("You are attempting to set your mod ID as: $id")
            val exceptionStrings: IDCheckDontTouchPls = getExceptionStrings()
            modID = when (id) {
                exceptionStrings.DEFAULTID -> throw RuntimeException(exceptionStrings.EXCEPTION)
                exceptionStrings.DEVID -> exceptionStrings.DEFAULTID
                else -> id
            }
            logger.info("Success! ID is $modID")
        }

        private fun pathCheck() {
            val exceptionStrings = getExceptionStrings()
            val packageName: String = DevilDealMod::class.java.getPackage().name
            val resourcePathExists = Gdx.files.internal(modID + "Resources")
            if (modID != exceptionStrings.DEVID) {
                if (packageName != modID) {
                    throw RuntimeException(exceptionStrings.PACKAGE_EXCEPTION + modID)
                }
                if (!resourcePathExists.exists()) {
                    throw RuntimeException(exceptionStrings.RESOURCE_FOLDER_EXCEPTION + modID + "Resources")
                }
            }
        }

        private fun getExceptionStrings(): IDCheckDontTouchPls {
            val gson = Gson()
            val inputStream: InputStream = DevilDealMod::class.java.getResourceAsStream(IDCheckPath)
                ?: throw RuntimeException("Missing $IDCheckPath file")
            return gson.fromJson(
                InputStreamReader(inputStream, StandardCharsets.UTF_8),
                IDCheckDontTouchPls::class.java
            )
        }

        // endregion Safety Checks - DO NOT EDIT
    }

    init {
        logger.info("Subscribe to BaseMod hooks")

        BaseMod.subscribe(this)
        setModId("com.darkmoose117.devildeal")

        logger.info("Done subscribing")
    }

    override fun receiveEditCards() {
        logger.info("Adding Cards")
        pathCheck()

        // TODO try again later
//        AutoAdd("DevilDeal") // ${project.artifactId}
//            .packageFilter("com.darkmoose117.devildeal.cards")
//            .setDefaultSeen(true)
//            .cards()

        BaseMod.addCard(DealWithTheDevil())
        UnlockTracker.unlockCard(DealWithTheDevil.ID)

        logger.info("Done adding cards")
    }

    override fun receiveEditStrings() {
        logger.info("Begin to edit strings")
        pathCheck()

        val cardStringsFilePath = "DevilDeal-Card-Strings.json".makeStringsFilePath()
        BaseMod.loadCustomStringsFile(CardStrings::class.java, cardStringsFilePath)

        logger.info("Done editing Strings")
    }

    override fun receiveEditKeywords() {
        logger.info("Begin to load keywords")
        val gson = Gson()
        val json = Gdx.files.internal("DevilDeal-Keyword-Strings.json".makeStringsFilePath())
                .readString(StandardCharsets.UTF_8.toString())
        val keywords = gson.fromJson(
            json,
            Array<Keyword>::class.java
        )

        if (keywords != null) {
            for (keyword in keywords) {
                BaseMod.addKeyword(
                    modID,
                    keyword.PROPER_NAME,
                    keyword.NAMES,
                    keyword.DESCRIPTION
                )
            }
        }

        logger.info("Done loading ${keywords?.size ?: 0} keyword(s)")
    }
}