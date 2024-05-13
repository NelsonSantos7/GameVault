package com.example.gamevault.ui.home

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.gamevault.R

object HomeFragmentDirections {
    private const val ARG_GAME_ID = "game_id"
    private const val ARG_GAME_TITLE = "game_title"

    fun actionNavHomeToGameDetailsFragment(gameId: Int, gameTitle: String): NavDirections {
        require(gameId > 0) { "Game ID must be positive. Provided ID: $gameId" }
        require(gameTitle.isNotEmpty()) { "Game title cannot be empty." }

        return object : NavDirections {
            override val actionId: Int
                get() = R.id.action_nav_home_to_gameDetailsFragment

            override val arguments: Bundle
                get() = Bundle().apply {
                    putInt(ARG_GAME_ID, gameId)
                    putString(ARG_GAME_TITLE, gameTitle)
                }
        }
    }
}
