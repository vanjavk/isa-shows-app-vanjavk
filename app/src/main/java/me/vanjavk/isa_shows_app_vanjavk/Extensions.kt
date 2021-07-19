package me.vanjavk.isa_shows_app_vanjavk

import android.util.Patterns
import me.vanjavk.isa_shows_app_vanjavk.model.Genre
import me.vanjavk.isa_shows_app_vanjavk.model.Show

const val MIN_PASSWORD_LENGTH = 6
const val EXTRA_ID = "EXTRA_ID"

fun CharSequence?.isValidEmail() = !(!isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches())

fun CharSequence.getUsername() = this.substring(0, this.indexOf('@')).orEmpty()

val shows = listOf(
    Show(
        "tt0289879",
        "The Butterfly Effect",
        "Evan Treborn suffers blackouts during significant events of his life. As he grows up, he finds a way to remember these lost memories and a supernatural way to alter his life by reading his journal.",
        listOf(Genre.DRAMA, Genre.SCIFI, Genre.THRILLER),
        R.drawable.show_butterflyeffect
    ),
    Show(
        "tt0114746",
        "Twelve Monkeys",
        "In a future world devastated by disease, a convict is sent back in time to gather information about the man-made virus that wiped out most of the human population on the planet.",
        listOf(Genre.MYSTERY, Genre.SCIFI, Genre.THRILLER),
        R.drawable.show_twelvemonkeys
    ),
    Show(
        "tt2553424",
        "The Infinite Man",
        "A man's attempts to construct the ultimate romantic weekend backfire when his quest for perfection traps his lover in an infinite loop.",
        listOf(Genre.COMEDY, Genre.FANTASY, Genre.SCIFI),
        R.drawable.show_theinfiniteman
    ),
    Show(
        "tt1596363",
        "The Big Short",
        "In 2006-2007 a group of investors bet against the US mortgage market. In their research they discover how flawed and corrupt the market is.",
        listOf(Genre.BIOGRAPHY, Genre.COMEDY, Genre.DRAMA),
        R.drawable.show_thebigshort
    ),
    Show(
        "tt0209144",
        "Memento",
        "A man with short-term memory loss attempts to track down his wife's murderer.",
        listOf(Genre.MYSTERY, Genre.THRILLER),
        R.drawable.show_memento
    ),
    Show(
        "tt0119174",
        "The Game",
        "After a wealthy banker is given an opportunity to participate in a mysterious game, his life is turned upside down when he becomes unable to distinguish between the game and reality.",
        listOf(Genre.DRAMA, Genre.MYSTERY, Genre.THRILLER),
        R.drawable.show_thegame
    ),
    Show(
        "tt6751668",
        "Parasite",
        "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.",
        listOf(Genre.COMEDY, Genre.DRAMA, Genre.THRILLER),
        R.drawable.show_parasite
    ),
    Show(
        "tt0180093",
        "Requiem for a Dream ",
        "The drug-induced utopias of four Coney Island people are shattered when their addictions run deep.",
        listOf(Genre.DRAMA),
        R.drawable.show_requiemforadream
    ),
)
