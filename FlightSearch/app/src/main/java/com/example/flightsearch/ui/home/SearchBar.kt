package com.example.flightsearch.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flightsearch.ui.FlightUiState
import com.example.flightsearch.ui.theme.FlightSearchTheme


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    flightUiState: FlightUiState,
    autocompleteSuggestions: List<AnnotatedString>,
    onSearchTextChange: (String) -> Unit,
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchTextValue = remember {
        mutableStateOf(
            TextFieldValue(
                text = flightUiState.searchText,
                selection = TextRange(flightUiState.searchText.length)
            )
        )
    }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedSuggestion by remember { mutableStateOf<String?>(null) }

    // Update displayedSearchText when flightUiState.searchText changes
    LaunchedEffect(flightUiState.searchText) {
        searchTextValue.value = TextFieldValue(
            text = flightUiState.searchText,
            selection = TextRange(flightUiState.searchText.length)
        )
    }

    TextField(
        value = searchTextValue.value,
        onValueChange = { textFieldValue ->
            searchTextValue.value = textFieldValue.copy(
                selection = TextRange(textFieldValue.text.length)
            )
            onSearchTextChange(textFieldValue.text)
            isDropdownExpanded = textFieldValue.text.isNotEmpty()
        },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
        trailingIcon = {
           if(searchTextValue.value.text.isNotEmpty()){
               IconButton(onClick = {
                   onSearchTextChange("")
                   isDropdownExpanded = false
               }) {
                   Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear Search")
               }
           }
        },
        placeholder = { Text(text = "Search") },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )

   if(isDropdownExpanded && selectedSuggestion == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ){
            Column{
                autocompleteSuggestions.forEach{ suggestion ->
                    Text(
                        text = suggestion,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedSuggestion = suggestion.substring(0,3)
                                onSuggestionClick(selectedSuggestion!!)
                                isDropdownExpanded = false
                            }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchBar() {
    val uiState = FlightUiState(emptyList(), "Fake Text")

    FlightSearchTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                SearchBar(
                    flightUiState = uiState,
                    autocompleteSuggestions = emptyList(),
                    onSearchTextChange = {},
                    onSuggestionClick = {},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}