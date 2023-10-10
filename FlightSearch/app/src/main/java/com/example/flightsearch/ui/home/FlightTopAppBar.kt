package com.example.flightsearch.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.flightsearch.R
import com.example.flightsearch.ui.theme.FlightSearchTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightAppTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displaySmall
            )
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}