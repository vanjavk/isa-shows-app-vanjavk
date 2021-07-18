package me.vanjavk.isa_shows_app_vanjavk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import me.vanjavk.isa_shows_app_vanjavk.databinding.ActivityShowsBinding
import me.vanjavk.isa_shows_app_vanjavk.model.Genre
import me.vanjavk.isa_shows_app_vanjavk.model.Show

class ShowsActivity : AppCompatActivity() {

    companion object {

        fun buildIntent(activity: Activity): Intent {
            val intent = Intent(activity, ShowsActivity::class.java)
            return intent
        }
    }

    private val shows = listOf(
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

    private lateinit var binding: ActivityShowsBinding

    private var showsAdapter: ShowsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initShowsRecycler()

        initAddShowHideButton()
    }

    private fun initAddShowHideButton() {
        binding.showHideButton.setOnClickListener {
            binding.showsRecyclerView.apply {
                isVisible = !isVisible
            }
        }
    }

    private fun initShowsRecycler() {
        showsAdapter = ShowsAdapter(emptyList()) { item ->
            Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
        }

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.showsRecyclerView.adapter = showsAdapter

        showsAdapter?.setItems(shows)
    }

}