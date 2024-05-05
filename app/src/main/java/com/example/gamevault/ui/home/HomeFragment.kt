import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.databinding.FragmentHomeBinding
import com.example.gamevault.model.Gamemodel
import com.example.gamevault.ui.home.HomeFragmentDirections

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = GameAdapter { game ->
            navigateToGameDetails(game)
        }
        binding.gamesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        viewModel.games.observe(viewLifecycleOwner) { games ->
            adapter.submitList(games)
            Log.d("HomeFragment", "Displaying ${games.size} games")
        }
    }

    private val navigateToGameDetails: (Gamemodel) -> Unit = { game ->
        game.id?.let { gameId ->
            val action = HomeFragmentDirections.actionNavHomeToGameDetailsFragment(gameId, game.titulo)
            findNavController().navigate(action)
        } ?: Log.e("HomeFragment", "Attempted to navigate with a null game ID")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class HomeViewModel(private val dbHelper: DBhelper) : ViewModel() {
    val games = dbHelper.getAllGamesLiveData()
}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    private val dbHelper: DBhelper by lazy { DBhelper(context) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dbHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
