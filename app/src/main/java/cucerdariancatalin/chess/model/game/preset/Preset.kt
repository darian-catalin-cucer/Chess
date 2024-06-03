package com.github.zsoltk.chesso.model.game.preset

import cucerdariancatalin.chess.model.game.controller.GameController

interface Preset {

    fun apply(gameController: GameController)
}
