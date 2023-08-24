package com.example.amphibiansapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.amphibiansapp.R
import com.example.amphibiansapp.model.AmphibianImage
import com.example.amphibiansapp.ui.theme.AmphibiansAppTheme

@Composable
fun HomeScreen(
    amphibianUiState: AmphibianUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
){
    when(amphibianUiState){
        is AmphibianUiState.Loading -> LoadingScreen(
            modifier
                .fillMaxWidth()
                .size(200.dp))
        is AmphibianUiState.Success -> ImageListScreen(amphibianUiState.amphibians, modifier.fillMaxWidth())
        is AmphibianUiState.Error -> ErrorScreen(retryAction, modifier.fillMaxSize())
    }
}

@Composable
fun ImageListScreen(images: List<AmphibianImage>, modifier: Modifier = Modifier){
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ){
        items(
            items = images,
            key = { amphibian ->
                amphibian.name
            }
        ) { amphibian ->
            ImageCard(amphibianImage = amphibian, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun ImageCard(modifier: Modifier = Modifier, amphibianImage: AmphibianImage){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column() {
            Text(
                text = stringResource(id = R.string.amphibian_title, amphibianImage.name, amphibianImage.type),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            )
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(amphibianImage.imgSrc)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = amphibianImage.description,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(id = R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.loading_failed),
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text(stringResource(id = R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview(){
    AmphibiansAppTheme {
        ErrorScreen(retryAction = { })
    }
}

@Preview(showBackground = true)
@Composable
fun ImageListScreenPreview(){

}

@Preview(showBackground = true)
@Composable
fun cardPreview(){
    AmphibiansAppTheme {
        val img = AmphibianImage(
            description = "A Description",
            name = "Great Basin Spadefoot",
            type = "Toad",
            imgSrc ="https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/great-basin-spadefoot.png"
        )
        ImageCard(amphibianImage = img, modifier = Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
fun listImagePreview(){
    AmphibiansAppTheme() {
        val mockData = List(10) {
            AmphibianImage(
                "Lorem Ipsum - $it",
                "$it",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do" +
                        " eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad" +
                        " minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip" +
                        " ex ea commodo consequat.",
                imgSrc = ""
            )
        }

        ImageListScreen(images = mockData, Modifier.fillMaxSize())
    }
}


