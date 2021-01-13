package os.abuyahya.hashgeneratorapp

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import os.abuyahya.hashgeneratorapp.databinding.FragmentHomeBinding

class HomeFrag : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        binding.generateBtn.setOnClickListener {
            onGenerateClicked()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val hashAlgorithms = resources.getStringArray(R.array.hash_algorithms)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, hashAlgorithms)
        binding.autoCompleteTxt.setAdapter(arrayAdapter)
    }

    private fun onGenerateClicked(){
        if (binding.planeTextEdt.text.isEmpty()){
            showSnackBar("Field is Empty!")
        }
        else{
            lifecycleScope.launch {
                applyAnimation()
                navigateToSuccess(getHashData())
            }
        }
    }

    private fun getHashData(): String {
        val algorithms = binding.autoCompleteTxt.text.toString()
        val plainText = binding.planeTextEdt.text.toString()
        return viewModel.getHash(plainText, algorithms)
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Okay"){}
        snackBar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
        snackBar.show()
    }

    private suspend fun applyAnimation() {
        binding.generateBtn.isClickable = false
        binding.titleTxt.animate().alpha(0f).duration = 400L
        binding.generateBtn.animate().alpha(0f).duration = 400L
        binding.textInputLayout.animate().alpha(0f).translationX(1200f).duration = 400L
        binding.planeTextEdt.animate().alpha(0f).translationX(-1200f).duration = 400L
        delay(400)

        binding.successBackground.animate().alpha(1f).rotationBy(720f).duration = 600L
        binding.successBackground.animate().scaleXBy(900f).scaleYBy(900f).duration = 800L
        delay(600)

        binding.successImg.animate().alpha(1f).duration = 1000L
        delay(1500)
    }

    private fun navigateToSuccess(hashData: String) {
        val action = HomeFragDirections.actionHomeFragToSuccessFrag(hashData)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear_menu){
            binding.planeTextEdt.text.clear()
            showSnackBar("Cleared!")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
