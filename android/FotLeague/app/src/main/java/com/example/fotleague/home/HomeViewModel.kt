package com.example.fotleague.home

import androidx.lifecycle.ViewModel
import com.example.fotleague.data.FotLeagueApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val api: FotLeagueApi): ViewModel() {


}