package com.example.weatherpaint.ui.fragments.favoriteFragment

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherpaint.R
import com.example.weatherpaint.data.dataBase.DaoDB
import com.example.weatherpaint.data.dataBase.EntitiesWeatherDay
import com.example.weatherpaint.databinding.FavorFragmentBinding
import com.example.weatherpaint.ui.fragments.other.Const.SEARCH
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class FavorFragment : Fragment() , FavorFragmentInterface {
    private var _binding : FavorFragmentBinding? = null
    private val binding : FavorFragmentBinding get() = _binding!!

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val dayDao: DaoDB by inject { parametersOf(requireActivity()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FavorFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadDataRoom()
        chekButton()
    }

    private fun loadDataRoom(){
        scope.launch {
            val list = dayDao.getAllDays()
            withContext(Dispatchers.Main){
                setRecycler(list as ArrayList<EntitiesWeatherDay>)
            }
        }
    }

    private fun deleteDataRoom() {
        scope.launch {
            dayDao.deleteAll()
            withContext(Dispatchers.Main){
                loadDataRoom()
            }
        }
    }

    private fun chekButton(){
        binding.chipDeleteAll.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle(getString(R.string.alert_dialog_header));
            dialog.setCancelable(true)

            dialog.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            dialog.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                deleteDataRoom()
                dialog.dismiss()
            }
            dialog.create().show()
        }

        binding.chipSearch.setOnClickListener {
            if(binding.textSearch.text.toString() == SEARCH) {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerSearch, SearchFragment.newInstance(this@FavorFragment))
                    .commit()

                animations(-1350f)
            } else {
                loadDataRoom()
                binding.textSearch.text = getString(R.string.search)
                binding.chipSearch.strokeColor = Color.GRAY
            }
        }
    }

    override fun searchDataBase(city: String, date: String) {
        animations(0f)

        scope.launch {
            val list = dayDao.getDay(date,city)

            withContext(Dispatchers.Main){
                if(list.isEmpty()) {
                    Toast.makeText(requireContext(),"Not found",Toast.LENGTH_SHORT).show()
                } else {
                    setRecycler(list as ArrayList<EntitiesWeatherDay>)

                    binding.textSearch.text = getString(R.string.cancel)
                    binding.chipSearch.strokeColor = Color.rgb(250,170,170)
                }
            }
        }
    }

    private fun animations(value : Float){
        val animator = ObjectAnimator.ofFloat(binding.cardSearch,View.TRANSLATION_Y,value)
        animator.interpolator = AnticipateOvershootInterpolator(1f)
        animator.duration = 1000
        animator.start()
    }

    private fun setRecycler(listDay : ArrayList<EntitiesWeatherDay>) = with(binding) {
        listDay.reverse()
        recyclerAddedWeather.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recyclerAddedWeather.adapter = RecyclerAddedFragment(this@FavorFragment, listDay, recyclerAddedWeather)
    }

    override fun deleteDayDataBase(id : Int) {
        scope.launch {
            dayDao.deleteDay(id)
        }
    }

    companion object {
        fun newInstance() = FavorFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        scope.cancel()
    }
}