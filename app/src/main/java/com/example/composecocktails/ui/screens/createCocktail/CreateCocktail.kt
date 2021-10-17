package com.example.composecocktails.ui.screens.createCocktail

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.example.composecocktails.Screens
import com.example.composecocktails.data.models.Cocktail
import com.example.composecocktails.ui.screens.*
import com.example.composecocktails.ui.theme.gradientBackground
import com.example.composecocktails.ui.theme.gradientDetailsSearch
import com.example.composecocktails.ui.theme.gradientHeader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

@SuppressLint("UnrememberedMutableState")
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun CreateCocktail(
    viewModel: SharedViewModel,
    listState: LazyListState
) {

    val systemUiController = rememberSystemUiController()
    val statusBarColour = MaterialTheme.colors.surface
    var fabOpenCreateCocktail by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val createdCocktails = viewModel.createdCocktails.observeAsState()
    val showDetails = viewModel.showAdditionalInfoCreate
    val cocktailInfo = viewModel.cocktailAdditionalInfoCreate
    val halfScreenHeight = LocalContext.current.resources.displayMetrics
        .run { heightPixels / density }.toInt() / 2
    val cocktailComponentsListState = rememberScrollState()

    val drinkName = rememberSaveable { mutableStateOf("") }
    val drinkInstructions = rememberSaveable { mutableStateOf("") }
    val measure1 = rememberSaveable { mutableStateOf("") }
    val ingredient1 = rememberSaveable { mutableStateOf("") }
    val measure2 = rememberSaveable { mutableStateOf("") }
    val ingredient2 = rememberSaveable { mutableStateOf("") }
    val measure3 = rememberSaveable { mutableStateOf("") }
    val ingredient3 = rememberSaveable { mutableStateOf("") }
    val measure4 = rememberSaveable { mutableStateOf("") }
    val ingredient4 = rememberSaveable { mutableStateOf("") }
    val measure5 = rememberSaveable { mutableStateOf("") }
    val ingredient5 = rememberSaveable { mutableStateOf("") }
    val measure6 = rememberSaveable { mutableStateOf("") }
    val ingredient6 = rememberSaveable { mutableStateOf("") }
    val measure7 = rememberSaveable { mutableStateOf("") }
    val ingredient7 = rememberSaveable { mutableStateOf("") }
    val measure8 = rememberSaveable { mutableStateOf("") }
    val ingredient8 = rememberSaveable { mutableStateOf("") }
    val measure9 = rememberSaveable { mutableStateOf("") }
    val ingredient9 = rememberSaveable { mutableStateOf("") }
    val measure10 = rememberSaveable { mutableStateOf("") }
    val ingredient10 = rememberSaveable { mutableStateOf("") }
    val measure11 = rememberSaveable { mutableStateOf("") }
    val ingredient11 = rememberSaveable { mutableStateOf("") }
    val measure12 = rememberSaveable { mutableStateOf("") }
    val ingredient12 = rememberSaveable { mutableStateOf("") }
    val measure13 = rememberSaveable { mutableStateOf("") }
    val ingredient13 = rememberSaveable { mutableStateOf("") }
    val measure14 = rememberSaveable { mutableStateOf("") }
    val ingredient14 = rememberSaveable { mutableStateOf("") }
    val measure15 = rememberSaveable { mutableStateOf("") }
    val ingredient15 = rememberSaveable { mutableStateOf("") }

    val validCocktail = mutableStateOf(
        drinkName.value.isNotBlank() &&
                drinkInstructions.value.isNotBlank() &&
                measure1.value.isNotBlank() &&
                ingredient1.value.isNotBlank()
    )

    val clearCocktail = mutableStateOf(
        if (!fabOpenCreateCocktail) {
            drinkName.value = ""
            drinkInstructions.value = ""
            measure1.value = ""; ingredient1.value = ""
            measure2.value = ""; ingredient2.value = ""
            measure3.value = ""; ingredient3.value = ""
            measure4.value = ""; ingredient4.value = ""
            measure5.value = ""; ingredient5.value = ""
            measure6.value = ""; ingredient6.value = ""
            measure7.value = ""; ingredient7.value = ""
            measure8.value = ""; ingredient8.value = ""
            measure9.value = ""; ingredient9.value = ""
            measure10.value = ""; ingredient10.value = ""
            measure11.value = ""; ingredient11.value = ""
            measure12.value = ""; ingredient12.value = ""
            measure13.value = ""; ingredient13.value = ""
            measure14.value = ""; ingredient14.value = ""
            measure15.value = ""; ingredient15.value = ""
        } else {
            null
        }
    )

    fun scrollToBottom() = coroutineScope.launch {
        cocktailComponentsListState.animateScrollBy(
            listState
                .layoutInfo
                .viewportEndOffset
                .toFloat()
        )
    }

    SideEffect {
        systemUiController.setStatusBarColor(statusBarColour)
    }

    Scaffold(
        floatingActionButton = {
            Fab(
                fabOpenCreateCocktail,
                updateCreateStatus = {
                    fabOpenCreateCocktail = it
                },
                addCreatedCocktail = {
                    viewModel.addCreatedCocktail(
                        Cocktail.Drink(
                            idDrink = System.currentTimeMillis().toString(),
                            strDrink = drinkName.value,
                            strInstructions = drinkInstructions.value,
                            strMeasure1 = measure1.value,
                            strMeasure2 = measure2.value,
                            strMeasure3 = measure3.value,
                            strMeasure4 = measure4.value,
                            strMeasure5 = measure5.value,
                            strMeasure6 = measure6.value,
                            strMeasure7 = measure7.value,
                            strMeasure8 = measure8.value,
                            strMeasure9 = measure9.value,
                            strMeasure10 = measure10.value,
                            strMeasure11 = measure11.value,
                            strMeasure12 = measure12.value,
                            strMeasure13 = measure13.value,
                            strMeasure14 = measure14.value,
                            strMeasure15 = measure15.value,
                            strIngredient1 = ingredient1.value,
                            strIngredient2 = ingredient2.value,
                            strIngredient3 = ingredient3.value,
                            strIngredient4 = ingredient4.value,
                            strIngredient5 = ingredient5.value,
                            strIngredient6 = ingredient6.value,
                            strIngredient7 = ingredient7.value,
                            strIngredient8 = ingredient8.value,
                            strIngredient9 = ingredient9.value,
                            strIngredient10 = ingredient10.value,
                            strIngredient11 = ingredient11.value,
                            strIngredient12 = ingredient12.value,
                            strIngredient13 = ingredient13.value,
                            strIngredient14 = ingredient14.value,
                            strIngredient15 = ingredient15.value,
                            isUserCreated = true,

                            )
                    )
                },
                validCocktail.value
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradientBackground())
            ) {
                LazyColumn(
                    Modifier
                        .fillMaxSize(),
                    state = listState
                ) {

                    item {
                        Header(
                            "Create cocktails...",
                            gradientHeader()
                        )
                    }

                    if (!createdCocktails.value.isNullOrEmpty()) {
                        items(createdCocktails.value!!) { cocktail ->
                            CocktailListItem(
                                cocktail = cocktail,
                                showInfo = {
                                    viewModel.updateAdditionalInfo(it, Screens.Create.title)
                                },
                                updateFavourite = { viewModel.updateFavourite(it) }
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = fabOpenCreateCocktail,
                    enter = slideInVertically(
                        initialOffsetY = { it }
                    ) + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {

                    Column(
                        Modifier
//                            .offset(y = 40.dp)
                            .background(gradientDetailsSearch())
                            .padding(16.dp)
                            .wrapContentSize()
                            .verticalScroll(cocktailComponentsListState)
                    ) {

                        CocktailComponent(
                            mandatory = true,
                            labelText = "Cocktail name",
                            submitText = {
                                drinkName.value = it
                            }
                        )

                        CocktailComponent(
                            mandatory = true,
                            labelText = "Cocktail instructions",
                            submitText = {
                                drinkInstructions.value = it
                            }
                        )


                        Row {

                            Column(Modifier.weight(1f)) {
                                CocktailComponent(
                                    mandatory = true,
                                    labelText = "Measure 1",
                                    submitText = {
                                        measure1.value = it
                                    }
                                )
                            }

                            Column(Modifier.weight(2f)) {
                                CocktailComponent(
                                    mandatory = true,
                                    labelText = "Ingredient 1",
                                    submitText = {
                                        ingredient1.value = it
                                        scrollToBottom()
                                    }
                                )
                            }
                        }

                        AnimatedVisibility(
                            visible = measure1.value.isNotBlank() && ingredient1.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 2",
                                        submitText = {
                                            measure2.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 2",
                                        submitText = {
                                            ingredient2.value = it
                                            scrollToBottom()
                                        }
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = measure2.value.isNotBlank() && ingredient2.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 3",
                                        submitText = {
                                            measure3.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 3",
                                        submitText = {
                                            ingredient3.value = it
                                            scrollToBottom()
                                        }
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = measure3.value.isNotBlank() && ingredient3.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 4",
                                        submitText = {
                                            measure4.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 4",
                                        submitText = {
                                            ingredient4.value = it
                                            scrollToBottom()
                                        }
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = measure4.value.isNotBlank() && ingredient4.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 5",
                                        submitText = {
                                            measure5.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 5",
                                        submitText = {
                                            ingredient5.value = it
                                            scrollToBottom()
                                        }
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = measure5.value.isNotBlank() && ingredient5.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 6",
                                        submitText = {
                                            measure6.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 6",
                                        submitText = {
                                            ingredient6.value = it
                                            scrollToBottom()
                                        }
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = measure6.value.isNotBlank() && ingredient6.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 7",
                                        submitText = {
                                            measure7.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 7",
                                        submitText = {
                                            ingredient7.value = it
                                            scrollToBottom()
                                        }
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = measure7.value.isNotBlank() && ingredient7.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 8",
                                        submitText = {
                                            measure8.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 8",
                                        submitText = {
                                            ingredient8.value = it
                                            scrollToBottom()
                                        }
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = measure8.value.isNotBlank() && ingredient8.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 9",
                                        submitText = {
                                            measure9.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 9",
                                        submitText = {
                                            ingredient9.value = it
                                            scrollToBottom()
                                        }
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = measure9.value.isNotBlank() && ingredient9.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 10",
                                        submitText = {
                                            measure10.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 10",
                                        submitText = {
                                            ingredient10.value = it
                                            scrollToBottom()
                                        }
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = measure10.value.isNotBlank() && ingredient10.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 11",
                                        submitText = {
                                            measure11.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 11",
                                        submitText = {
                                            ingredient11.value = it
                                            scrollToBottom()
                                        }
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = measure11.value.isNotBlank() && ingredient11.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 12",
                                        submitText = {
                                            measure12.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 12",
                                        submitText = {
                                            ingredient12.value = it
                                            scrollToBottom()
                                        }
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = measure12.value.isNotBlank() && ingredient12.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 13",
                                        submitText = {
                                            measure13.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 13",
                                        submitText = {
                                            ingredient13.value = it
                                            scrollToBottom()
                                        }
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = measure13.value.isNotBlank() && ingredient13.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 14",
                                        submitText = {
                                            measure14.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 14",
                                        submitText = {
                                            ingredient14.value = it
                                            scrollToBottom()
                                        }
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = measure14.value.isNotBlank() && ingredient14.value.isNotBlank()
                        ) {
                            Row {

                                Column(Modifier.weight(1f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Measure 15",
                                        submitText = {
                                            measure15.value = it
                                        }
                                    )
                                }

                                Column(Modifier.weight(2f)) {
                                    CocktailComponent(
                                        mandatory = false,
                                        labelText = "Ingredient 15",
                                        submitText = {
                                            ingredient15.value = it
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .clip(MaterialTheme.shapes.large)
                        .heightIn(0.dp, halfScreenHeight.dp)
                ) {

                    item {
                        DetailsWindow(
                            cocktail = cocktailInfo,
                            visibility = showDetails.value,
                            gradientBg = gradientDetailsSearch()
                        ) {
                            viewModel.updateAdditionalInfo(cocktailInfo, Screens.Create.title)
                        }
                    }
                }
            }
        }
    )
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun Fab(
    clickStatus: Boolean,
    updateCreateStatus: (Boolean) -> Unit,
    addCreatedCocktail: () -> Unit,
    validCocktail: Boolean,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var isAdding by remember { mutableStateOf(false) }
    val successGreen = Color(0xFF66BB6A)
    val fabColour by animateColorAsState(
        targetValue = if (validCocktail) {
            successGreen
        } else {
            if (clickStatus) {
                MaterialTheme.colors.error
            } else {
                MaterialTheme.colors.secondary
            }
        },
        animationSpec = tween(durationMillis = 500)
    )

    Row {
        AnimatedVisibility(visible = validCocktail) {
            FloatingActionButton(
                onClick = {
                    addCreatedCocktail()
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    isAdding = !isAdding

                    Timer(false).schedule(1500) {
                        updateCreateStatus(!clickStatus)
                        isAdding = !isAdding
                    }

                },
                modifier = modifier
                    .padding(8.dp),
                backgroundColor = fabColour
            ) {
                AnimatedContent(
                    targetState = isAdding,
                    transitionSpec = {
                        slideInVertically({ height -> height }) + fadeIn() with
                                slideOutVertically({ height -> -height }) + fadeOut()
                    }) { targetStatus ->
                    Icon(
                        imageVector = if (targetStatus) {
                            Icons.Filled.Check
                        } else {
                            Icons.Filled.Add
                        },
                        contentDescription = null
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { updateCreateStatus(!clickStatus) },
            modifier = modifier
                .padding(8.dp),
            backgroundColor = fabColour
        ) {
            AnimatedContent(
                targetState = clickStatus,
                transitionSpec = {
                    slideInVertically({ height -> height }) + fadeIn() with
                            slideOutVertically({ height -> -height }) + fadeOut()
                }) { targetStatus ->
                Icon(
                    imageVector =
                    if (!targetStatus) {
                        Icons.Filled.Add
                    } else {
                        Icons.Filled.Close
                    },
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun CocktailComponent(
    modifier: Modifier = Modifier,
    mandatory: Boolean,
    labelText: String,
    submitText: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var inputText by rememberSaveable { mutableStateOf("") }

    TextField(
        value = inputText,
        onValueChange = { inputText = it; submitText(it) },
        modifier
            .padding(8.dp, 4.dp, 8.dp, 4.dp)
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.subtitle1,
        label = {
            Text(
                text = labelText,
                style = MaterialTheme.typography.subtitle2,
                fontStyle = FontStyle.Italic
            )
        },
        isError = inputText.isEmpty() && mandatory,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
            keyboardController?.hide()
        }),
    )
}