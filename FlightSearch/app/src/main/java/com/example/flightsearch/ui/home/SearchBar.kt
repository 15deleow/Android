package com.example.flightsearch.ui.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearch.ui.FlightUiState
import com.example.flightsearch.ui.theme.FlightSearchTheme

private val TAG = "SEARCHBAR"

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
        mutableStateOf(TextFieldValue("", TextRange(0)))
    }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var suggestionSelected by remember { mutableStateOf(false) }
    val isTextFieldFocused = remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    // Update displayedSearchText when flightUiState.searchText changes
    LaunchedEffect(flightUiState.searchText) {
        searchTextValue.value = TextFieldValue(
            text = flightUiState.searchText,
            selection = TextRange(flightUiState.searchText.length)
        )
    }

    Column(
    ) {
        //Search Bar
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
                        suggestionSelected = false
                    }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear Search")
                    }
                }
            },
            placeholder = { Text(text = "Search") },
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .focusable()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    Log.d(TAG, "${it.isFocused}")
                }
        )

        // Dropdown suggestions
        AnimatedVisibility(
            visible = (isDropdownExpanded && !suggestionSelected),
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(250)),
            exit = slideOutVertically(targetOffsetY = { it - it}, animationSpec = tween(250))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                Column {
                    autocompleteSuggestions.forEach { suggestion ->
                        Text(
                            text = suggestion,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .focusProperties { canFocus = false }
                                .clickable(
                                    true,
                                    onClick = {
                                    onSuggestionClick(suggestion.substring(0, 3))
                                    isDropdownExpanded = false
                                    suggestionSelected = true
                                    keyboardController?.hide()
                                })
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchBar() {
    val uiState = FlightUiState(emptyList(), "")

    val suggestions: List<AnnotatedString> = listOf(
        buildAnnotatedString { append("OPO - Francisco Sa Carneiro Airport") },
    )

    FlightSearchTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                SearchBar(
                    flightUiState = uiState,
                    autocompleteSuggestions = suggestions,
                    onSearchTextChange = {},
                    onSuggestionClick = {},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}