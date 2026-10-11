package com.maxdgf.newspaper.presentation.all_news_screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.maxdgf.newspaper.R
import com.maxdgf.newspaper.domain.model.EverythingEndpointSettings
import com.maxdgf.newspaper.domain.model.TopHeadlinesEndpointSettings
import com.maxdgf.newspaper.domain.model.request_parameters.NewsContentCategory
import com.maxdgf.newspaper.domain.model.request_parameters.NewsSortMode
import com.maxdgf.newspaper.domain.model.request_parameters.NewsTextLanguage
import com.maxdgf.newspaper.presentation.all_news_screen.event.SaveEverythingEndpointSettingsEvent
import com.maxdgf.newspaper.presentation.all_news_screen.event.SaveTopHeadlinesEndpointSettingsEvent
import com.maxdgf.newspaper.presentation.common.model.ValueString

@Composable
fun SearchSettingsDialog(
    onDismissRequest: () -> Unit,
    currentEndpointIndex: Int,
    everythingEndpointSettings: EverythingEndpointSettings,
    topHeadlinesEndpointSettings: TopHeadlinesEndpointSettings,
    onSaveCurrentEndpointIndex: (Int) -> Unit,
    onSaveEverythingEndpointSettings: (SaveEverythingEndpointSettingsEvent) -> Unit,
    onSaveTopHeadlinesEndpointSettings: (SaveTopHeadlinesEndpointSettingsEvent) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 10.dp,
                        end = 5.dp,
                        start = 5.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.outline_search_24),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(text = "Search settings")

                    Spacer(modifier = Modifier.weight(1.0f))

                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_clear_24),
                            contentDescription = null
                        )
                    }
                }

                val endpoints = listOf("Everything", "Top-headlines")

                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    endpoints.forEachIndexed { index, string ->
                        SegmentedButton(
                            selected = index == currentEndpointIndex,
                            onClick = { onSaveCurrentEndpointIndex(index) },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = endpoints.size
                            )
                        ) { Text(text = string) }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    when (currentEndpointIndex) {
                        0 -> {
                            // --- 'search in' settings ---
                            item(key = 0) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(text = "Search in:")

                                        val searchIn = remember(everythingEndpointSettings.searchIn) {
                                            return@remember if (everythingEndpointSettings.searchIn.isNotBlank())
                                                everythingEndpointSettings.searchIn.split(",").toMutableList()
                                            else
                                                mutableListOf()
                                        }
                                        val options = remember {
                                            listOf(
                                                ValueString("in title", "title"),
                                                ValueString("in description", "description"),
                                                ValueString("in content", "content")
                                            )
                                        }

                                        options.forEach { entry ->
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Checkbox(
                                                    checked = searchIn.contains(entry.value),
                                                    onCheckedChange = { state ->
                                                        if (state)
                                                            searchIn.add(entry.value)
                                                        else
                                                            searchIn.remove(entry.value)

                                                        onSaveEverythingEndpointSettings(
                                                            SaveEverythingEndpointSettingsEvent.UpdateSearchIn(searchIn.joinToString(","))
                                                        )
                                                    }
                                                )

                                                Spacer(modifier = Modifier.width(10.dp))

                                                Text(text = entry.text)
                                            }
                                        }
                                    }
                                }
                            }

                            // --- 'sort by' settings ---
                            item(key = 1) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(text = "Sort by:")

                                        val options = remember {
                                            listOf(
                                                ValueString("by nothing", "nothing"),
                                                ValueString("by relevancy", "relevancy"),
                                                ValueString("by popularity", "popularity"),
                                                ValueString("by publish date", "publishedAt")
                                            )
                                        }

                                        options.forEach { entry ->
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                RadioButton(
                                                    selected = entry.value.equals(everythingEndpointSettings.sortBy.name, ignoreCase = true),
                                                    onClick = {
                                                        onSaveEverythingEndpointSettings(
                                                            SaveEverythingEndpointSettingsEvent.UpdateNewsSortMode(
                                                                when (entry.value) {
                                                                    "nothing" -> NewsSortMode.Nothing
                                                                    "relevancy" -> NewsSortMode.Relevancy
                                                                    "popularity" -> NewsSortMode.Popularity
                                                                    "publishedAt" -> NewsSortMode.PublishedAt
                                                                    else -> NewsSortMode.Nothing
                                                                }
                                                            )
                                                        )
                                                    }
                                                )

                                                Spacer(modifier = Modifier.width(10.dp))

                                                Text(text = entry.text)
                                            }
                                        }
                                    }
                                }
                            }

                            // --- 'language' settings ---
                            item(key = 2) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(text = "Language")

                                        val options = remember {
                                            listOf(
                                                ValueString("all languages", "all"),
                                                ValueString("arabian", "ar"),
                                                ValueString("dutch", "de"),
                                                ValueString("english", "en"),
                                                ValueString("spanish", "es"),
                                                ValueString("french", "fr"),
                                                ValueString("hebrew", "he"),
                                                ValueString("italian", "it"),
                                                ValueString("netherlands(dutch)", "nl"),
                                                ValueString("norwegian", "no"),
                                                ValueString("portuguese", "pt"),
                                                ValueString("russian", "ru"),
                                                ValueString("spanish", "sv"),
                                                ValueString("ud", "ud"),
                                                ValueString("chinese", "zh"),
                                            )
                                        }

                                        options.forEach { entry ->
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                RadioButton(
                                                    selected = entry.value.equals(
                                                        everythingEndpointSettings.language.name, ignoreCase = true),
                                                    onClick = {
                                                        onSaveEverythingEndpointSettings(
                                                            SaveEverythingEndpointSettingsEvent.UpdateNewsTextLanguage(
                                                                when (entry.value) {
                                                                    "all" -> NewsTextLanguage.All
                                                                    "ar" -> NewsTextLanguage.Ar
                                                                    "de" -> NewsTextLanguage.De
                                                                    "en" -> NewsTextLanguage.En
                                                                    "es" -> NewsTextLanguage.Es
                                                                    "fr" -> NewsTextLanguage.Fr
                                                                    "he" -> NewsTextLanguage.He
                                                                    "it" -> NewsTextLanguage.It
                                                                    "nl" -> NewsTextLanguage.Nl
                                                                    "no" -> NewsTextLanguage.No
                                                                    "pt" -> NewsTextLanguage.Pt
                                                                    "ru" -> NewsTextLanguage.Ru
                                                                    "sv" -> NewsTextLanguage.Sv
                                                                    "ud" -> NewsTextLanguage.Ud
                                                                    "zh" -> NewsTextLanguage.Zh
                                                                    else -> NewsTextLanguage.All
                                                                }
                                                            )
                                                        )
                                                    }
                                                )

                                                Spacer(modifier = Modifier.width(10.dp))

                                                Text(text = entry.text)
                                            }
                                        }
                                    }
                                }
                            }

                            // --- 'domains' settings ---
                            item(key = 4) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(text = "Include domains")

                                        var includeDomains by remember(everythingEndpointSettings) {
                                            mutableStateOf(everythingEndpointSettings.domains)
                                        }

                                        AnimatedVisibility(visible = includeDomains != everythingEndpointSettings.domains) {
                                            Text(
                                                text = "Don't forget to tap the checkmark to apply the changes!",
                                                softWrap = false,
                                                maxLines = 1,
                                                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Light
                                            )
                                        }

                                        OutlinedTextField(
                                            value = includeDomains,
                                            onValueChange = { newValue -> includeDomains = newValue },
                                            modifier = Modifier.fillMaxWidth(),
                                            placeholder = {
                                                Text(
                                                    text = "Write domains to include, for example (example1.com,example2.com…)",
                                                    modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                                                )
                                            },
                                            trailingIcon = {
                                                IconButton(
                                                    onClick = {
                                                        onSaveEverythingEndpointSettings(
                                                            SaveEverythingEndpointSettingsEvent.IncludeDomains(
                                                                includeDomains.filter { it != ' ' }
                                                            )
                                                        )
                                                    },
                                                    enabled = includeDomains != everythingEndpointSettings.domains
                                                ) {
                                                    Icon(
                                                        painter = painterResource(R.drawable.baseline_done_24),
                                                        contentDescription = null
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }

                            // --- 'exclude domains' settings ---
                            item(key = 5) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(text = "Exclude domains:")

                                        var excludeDomains by remember(everythingEndpointSettings) {
                                            mutableStateOf(everythingEndpointSettings.excludeDomains)
                                        }

                                        AnimatedVisibility(visible = excludeDomains != everythingEndpointSettings.excludeDomains) {
                                            Text(
                                                text = "Don't forget to tap the checkmark to apply the changes!",
                                                softWrap = false,
                                                maxLines = 1,
                                                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Light
                                            )
                                        }

                                        OutlinedTextField(
                                            value = excludeDomains,
                                            onValueChange = { newValue -> excludeDomains = newValue },
                                            modifier = Modifier.fillMaxWidth(),
                                            singleLine = true,
                                            placeholder = {
                                                Text(
                                                    text = "Write domains to exclude, for example (example1.com,example2.com...)",
                                                    modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                                                )
                                            },
                                            trailingIcon = {
                                                IconButton(
                                                    onClick = {
                                                        onSaveEverythingEndpointSettings(
                                                            SaveEverythingEndpointSettingsEvent.ExcludeDomains(
                                                                excludeDomains.filter { it != ' ' }
                                                            )
                                                        )
                                                    },
                                                    enabled = excludeDomains != everythingEndpointSettings.excludeDomains
                                                ) {
                                                    Icon(
                                                        painter = painterResource(R.drawable.baseline_done_24),
                                                        contentDescription = null
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        1 -> {
                            item(key = 6) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(text = "Country:")

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(
                                                checked = topHeadlinesEndpointSettings.isUsCountry,
                                                onCheckedChange = { state ->
                                                    onSaveTopHeadlinesEndpointSettings(
                                                        SaveTopHeadlinesEndpointSettingsEvent.UpdateIsUsCountryState(state)
                                                    )
                                                }
                                            )

                                            Spacer(modifier = Modifier.width(10.dp))

                                            Text(text = "us")
                                        }
                                    }
                                }
                            }

                            item(key = 7) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(text = "Category:")

                                        val options = remember {
                                            listOf(
                                                ValueString("all categories", "default"),
                                                ValueString("business", "business"),
                                                ValueString("entertainment", "entertainment"),
                                                ValueString("general", "general"),
                                                ValueString("health", "health"),
                                                ValueString("science", "science"),
                                                ValueString("sports", "sports"),
                                                ValueString("technologies", "technology")
                                            )
                                        }

                                        options.forEach { entry ->
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                RadioButton(
                                                    selected = entry.value == topHeadlinesEndpointSettings.category.name.lowercase(),
                                                    onClick = {
                                                        onSaveTopHeadlinesEndpointSettings(
                                                            SaveTopHeadlinesEndpointSettingsEvent.UpdateNewsCategory(
                                                                when (entry.value) {
                                                                    "default" -> NewsContentCategory.Default
                                                                    "business" -> NewsContentCategory.Business
                                                                    "entertainment" -> NewsContentCategory.Entertainment
                                                                    "general" -> NewsContentCategory.General
                                                                    "health" -> NewsContentCategory.Health
                                                                    "science" -> NewsContentCategory.Science
                                                                    "sports" -> NewsContentCategory.Sports
                                                                    "technology" -> NewsContentCategory.Technology
                                                                    else -> NewsContentCategory.Default
                                                                }
                                                            )
                                                        )
                                                    }
                                                )

                                                Spacer(modifier = Modifier.width(10.dp))

                                                Text(text = entry.text)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
