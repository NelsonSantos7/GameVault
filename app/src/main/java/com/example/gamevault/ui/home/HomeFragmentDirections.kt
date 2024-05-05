import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamevault.databinding.FragmentHomeBinding
import com.example.gamevault.model.Gamemodel

class HomeFragmentDirections : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        val adapter = GameAdapter { game ->
            game.id?.let { gameId ->
                val action = HomeFragmentDirections.actionNavHomeToGameDetailsFragment(gameId, game.titulo)
                findNavController().navigate(action)
            }
        }
        binding.gamesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

    private fun observeViewModel() {
        viewModel.games.observe(viewLifecycleOwner) { games ->
            (binding.gamesRecyclerView.adapter as? GameAdapter)?.submitList(games)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
