package com.sol.quizzapp.domain.model.quiz

import com.sol.quizzapp.R

enum class TriviaCategory(val id: Int, val displayName: String, val icon: Int) {
    GENERAL_KNOWLEDGE(9, "General Knowledge", R.drawable.general_knowledge),
    ENTERTAINMENT_BOOKS(10, "Entertainment: Books", R.drawable.books),
    ENTERTAINMENT_FILM(11, "Entertainment: Film", R.drawable.film),
    ENTERTAINMENT_MUSIC(12, "Entertainment: Music", R.drawable.music),
    ENTERTAINMENT_MUSICALS(13, "Entertainment: Musicals & Theatres", R.drawable.musicals_theatres),
    ENTERTAINMENT_TELEVISION(14, "Entertainment: Television", R.drawable.television),
    ENTERTAINMENT_VIDEO_GAMES(15, "Entertainment: Video Games", R.drawable.video_games),
    ENTERTAINMENT_BOARD_GAMES(16, "Entertainment: Board Games", R.drawable.board_games),
    SCIENCE_NATURE(17, "Science & Nature", R.drawable.science_nature),
    SCIENCE_COMPUTERS(18, "Science: Computers", R.drawable.computers),
    SCIENCE_MATHEMATICS(19, "Science: Mathematics", R.drawable.mathematics),
    MYTHOLOGY(20, "Mythology", R.drawable.mythology),
    SPORTS(21, "Sports", R.drawable.sports),
    GEOGRAPHY(22, "Geography", R.drawable.geography),
    HISTORY(23, "History", R.drawable.history),
    POLITICS(24, "Politics", R.drawable.politics),
    ART(25, "Art", R.drawable.art),
    CELEBRITIES(26, "Celebrities", R.drawable.celebrities),
    ANIMALS(27, "Animals", R.drawable.animals),
    VEHICLES(28, "Vehicles", R.drawable.vehicles),
    ENTERTAINMENT_COMICS(29, "Entertainment: Comics", R.drawable.comics),
    SCIENCE_GADGETS(30, "Science: Gadgets", R.drawable.gadgets),
    ENTERTAINMENT_ANIME(31, "Entertainment: Japanese Anime & Manga", R.drawable.anime_manga),
    ENTERTAINMENT_CARTOONS(
        32,
        "Entertainment: Cartoon & Animations",
        R.drawable.cartoon_animations
    );

    companion object {
        fun fromId(id: Int): TriviaCategory? {
            return values().find { it.id == id }
        }
    }
}
