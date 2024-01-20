package com.darkmoose117.devildeal.cards

import basemod.abstracts.CustomCard
import com.darkmoose117.devildeal.util.makeCardPath
import com.darkmoose117.devildeal.util.makeID
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction
import com.megacrit.cardcrawl.actions.utility.WaitAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.helpers.CardLibrary
import com.megacrit.cardcrawl.monsters.AbstractMonster

class DealWithTheDevil : CustomCard(
    ID,
    NAME,
    IMG,
    COST,
    DESCRIPTION,
    CardType.CURSE,
    CardColor.CURSE,
    CardRarity.CURSE,
    CardTarget.ALL_ENEMY
) {

    companion object {
        val ID: String = "DealWithTheDevil".makeID()
        private val cardStrings = CardCrawlGame.languagePack.getCardStrings(ID)!!
        val NAME: String = cardStrings.NAME
        val DESCRIPTION: String = cardStrings.DESCRIPTION
        val IMG: String = "Skill.png".makeCardPath()
        const val COST: Int = 0
        const val DEVILS_NUMBER = 666
    }

    init {
        baseDamage = DEVILS_NUMBER
        baseBlock = DEVILS_NUMBER
        exhaust = true
    }

    override fun use(p: AbstractPlayer?, m: AbstractMonster?) {
        val curse = CardLibrary.getCurse()
        // Permanently adds curse to master deck
        addToBot(AddCardToDeckAction(curse))
        // Adds to draw pile since adding to deck doesn't matter until following games
        addToBot(MakeTempCardInDrawPileAction(curse.makeCopy(), 1, true, true))

        addToBot(GainBlockAction(p, p, block))
        addToBot(WaitAction(0.1f))
        addToBot(DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AttackEffect.FIRE))
    }

    override fun upgrade() = Unit
}