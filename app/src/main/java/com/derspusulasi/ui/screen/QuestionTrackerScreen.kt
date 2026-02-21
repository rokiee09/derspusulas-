package com.derspusulasi.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.foundation.clickable
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import com.derspusulasi.R
import com.derspusulasi.ui.viewmodel.QuestionTrackerViewModel
import com.derspusulasi.ui.viewmodel.SortOrder
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionTrackerScreen(
    viewModel: QuestionTrackerViewModel,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    var showDatePicker by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    
    // Local state for form fields
    var localSubject by remember { mutableStateOf("") }
    var localTotal by remember { mutableStateOf("") }
    var localCorrect by remember { mutableStateOf("") }
    var localWrong by remember { mutableStateOf("") }
    var localBlank by remember { mutableStateOf("") }
    
    // Sync with editing record
    LaunchedEffect(uiState.editingRecord) {
        if (uiState.editingRecord != null) {
            localSubject = uiState.selectedSubject
            localTotal = uiState.total
            localCorrect = uiState.correct
            localWrong = uiState.wrong
            localBlank = uiState.blank
            showEditDialog = true
        } else {
            localSubject = ""
            localTotal = ""
            localCorrect = ""
            localWrong = ""
            localBlank = ""
            showEditDialog = false
        }
    }
    
    // Reset local state after save
    LaunchedEffect(uiState.records.size) {
        if (!showEditDialog && localSubject.isNotBlank()) {
            kotlinx.coroutines.delay(100)
            localSubject = ""
            localTotal = ""
            localCorrect = ""
            localWrong = ""
            localBlank = ""
        }
    }
    
    val currentDate = remember(uiState.selectedDate) {
        try {
            dateFormat.parse(uiState.selectedDate) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }
    
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDate.time
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header with back button and title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onBackClick() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.soru_takibi),
                contentDescription = "Soru Takibi",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Soru Takibi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // Statistics Card (Collapsible)
        if (uiState.records.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.BarChart,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "İstatistikler",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        IconButton(
                            onClick = { viewModel.toggleStatistics() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                if (uiState.showStatistics) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (uiState.showStatistics) "Gizle" else "Göster"
                            )
                        }
                    }
                    
                    if (uiState.showStatistics) {
                        val stats = viewModel.getStatistics()
                        Spacer(modifier = Modifier.height(12.dp))
                        StatisticsContent(stats = stats)
                    }
                }
            }
        }

        // Search and Filter Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Search Bar
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Ara (Ders, Tarih)") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Temizle")
                            }
                        }
                    },
                    singleLine = true
                )
                
                // Filter Chips Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Subject Filter
                    if (uiState.availableSubjects.isNotEmpty()) {
                        var showSubjectMenu by remember { mutableStateOf(false) }
                        Box {
                            FilterChip(
                                selected = uiState.filteredSubject != null,
                                onClick = { showSubjectMenu = true },
                                label = { 
                                    Text(uiState.filteredSubject ?: "Tüm Dersler", maxLines = 1)
                                },
                                leadingIcon = if (uiState.filteredSubject != null) {
                                    { Icon(Icons.Default.FilterList, null, modifier = Modifier.size(18.dp)) }
                                } else null
                            )
                            DropdownMenu(
                                expanded = showSubjectMenu,
                                onDismissRequest = { showSubjectMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Tüm Dersler") },
                                    onClick = {
                                        viewModel.filterBySubject(null)
                                        showSubjectMenu = false
                                    }
                                )
                                uiState.availableSubjects.forEach { subject ->
                                    DropdownMenuItem(
                                        text = { Text(subject) },
                                        onClick = {
                                            viewModel.filterBySubject(subject)
                                            showSubjectMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    // Date Filter
                    if (uiState.availableDates.isNotEmpty()) {
                        FilterChip(
                            selected = uiState.filteredDate != null,
                            onClick = {
                                if (uiState.filteredDate != null) {
                                    viewModel.filterByDate(null)
                                }
                            },
                            label = { 
                                Text(uiState.filteredDate ?: "Tüm Tarihler", maxLines = 1)
                            },
                            leadingIcon = if (uiState.filteredDate != null) {
                                { Icon(Icons.Default.FilterList, null, modifier = Modifier.size(18.dp)) }
                            } else null
                        )
                    }
                    
                    // Sort Menu
                    Box {
                        FilterChip(
                            selected = false,
                            onClick = { showSortMenu = true },
                            label = { 
                                Text(
                                    when (uiState.sortOrder) {
                                        SortOrder.DATE_DESC -> "Yeni → Eski"
                                        SortOrder.DATE_ASC -> "Eski → Yeni"
                                        SortOrder.SUBJECT_ASC -> "Ders A→Z"
                                        SortOrder.SUBJECT_DESC -> "Ders Z→A"
                                        SortOrder.NET_DESC -> "Net ↓"
                                        SortOrder.NET_ASC -> "Net ↑"
                                    },
                                    maxLines = 1
                                )
                            },
                            leadingIcon = { Icon(Icons.Default.Sort, null, modifier = Modifier.size(18.dp)) }
                        )
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Yeni → Eski") },
                                onClick = { viewModel.setSortOrder(SortOrder.DATE_DESC); showSortMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Eski → Yeni") },
                                onClick = { viewModel.setSortOrder(SortOrder.DATE_ASC); showSortMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Ders A→Z") },
                                onClick = { viewModel.setSortOrder(SortOrder.SUBJECT_ASC); showSortMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Ders Z→A") },
                                onClick = { viewModel.setSortOrder(SortOrder.SUBJECT_DESC); showSortMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Net Yüksek → Düşük") },
                                onClick = { viewModel.setSortOrder(SortOrder.NET_DESC); showSortMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Net Düşük → Yüksek") },
                                onClick = { viewModel.setSortOrder(SortOrder.NET_ASC); showSortMenu = false }
                            )
                        }
                    }
                }
                
                // Date Filter Chips (if dates available)
                if (uiState.availableDates.isNotEmpty() && uiState.filteredDate == null) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.availableDates.take(10)) { date ->
                            FilterChip(
                                selected = false,
                                onClick = { viewModel.filterByDate(date) },
                                label = { Text(date) }
                            )
                        }
                    }
                }
            }
        }

        // Input Form (Collapsible when editing)
        if (!showEditDialog) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Yeni Kayıt",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    OutlinedTextField(
                        value = localSubject,
                        onValueChange = { localSubject = it; viewModel.setSubject(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Ders") },
                        placeholder = { Text("Örn: Matematik, Türkçe") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = uiState.selectedDate,
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Tarih") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Default.CalendarToday, contentDescription = "Tarih Seç")
                            }
                        }
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = localTotal,
                            onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() }) { localTotal = it; viewModel.setTotal(it) } },
                            modifier = Modifier.weight(1f),
                            label = { Text("Toplam") },
                            placeholder = { Text("50") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                        OutlinedTextField(
                            value = localCorrect,
                            onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() }) { localCorrect = it; viewModel.setCorrect(it) } },
                            modifier = Modifier.weight(1f),
                            label = { Text("Doğru") },
                            placeholder = { Text("40") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = localWrong,
                            onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() }) { localWrong = it; viewModel.setWrong(it) } },
                            modifier = Modifier.weight(1f),
                            label = { Text("Yanlış") },
                            placeholder = { Text("4") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                        OutlinedTextField(
                            value = localBlank,
                            onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() }) { localBlank = it; viewModel.setBlank(it) } },
                            modifier = Modifier.weight(1f),
                            label = { Text("Boş (Opsiyonel)") },
                            placeholder = { Text("6") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
                        )
                    }

                    val net = viewModel.calculateNet()
                    if (net != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Text(
                                text = "Net: ${String.format("%.2f", net)}",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.setSubject(localSubject)
                            viewModel.setTotal(localTotal)
                            viewModel.setCorrect(localCorrect)
                            viewModel.setWrong(localWrong)
                            viewModel.setBlank(localBlank)
                            viewModel.saveRecord()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = localSubject.isNotBlank() && localTotal.isNotBlank() && localCorrect.isNotBlank() && localWrong.isNotBlank()
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Kaydet")
                    }
                }
            }
        }

        // Records List
        Text(
            text = if (uiState.filteredDate != null || uiState.filteredSubject != null || uiState.searchQuery.isNotBlank()) {
                "Filtrelenmiş Kayıtlar (${uiState.records.size})"
            } else {
                "Tüm Kayıtlar (${uiState.records.size})"
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        if (uiState.records.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Book,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Text(
                        text = when {
                            uiState.searchQuery.isNotBlank() -> "Arama sonucu bulunamadı"
                            uiState.filteredDate != null || uiState.filteredSubject != null -> "Filtreye uygun kayıt yok"
                            else -> "Henüz kayıt yok"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.records) { record ->
                    QuestionRecordItem(
                        record = record,
                        onEdit = { viewModel.setEditingRecord(record) },
                        onDelete = { viewModel.deleteRecord(record.id) }
                    )
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        Dialog(onDismissRequest = { showDatePicker = false }) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Tarih Seç",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    DatePicker(state = datePickerState, modifier = Modifier.fillMaxWidth())
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("İptal")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    val selectedDate = Date(it)
                                    val formattedDate = dateFormat.format(selectedDate)
                                    viewModel.setDate(formattedDate)
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("Tamam")
                        }
                    }
                }
            }
        }
    }

    // Edit Dialog
    if (showEditDialog && uiState.editingRecord != null) {
        Dialog(onDismissRequest = { viewModel.setEditingRecord(null) }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Kayıt Düzenle",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    OutlinedTextField(
                        value = localSubject,
                        onValueChange = { localSubject = it; viewModel.setSubject(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Ders") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = uiState.selectedDate,
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Tarih") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Default.CalendarToday, contentDescription = "Tarih Seç")
                            }
                        }
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = localTotal,
                            onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() }) { localTotal = it; viewModel.setTotal(it) } },
                            modifier = Modifier.weight(1f),
                            label = { Text("Toplam") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                        OutlinedTextField(
                            value = localCorrect,
                            onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() }) { localCorrect = it; viewModel.setCorrect(it) } },
                            modifier = Modifier.weight(1f),
                            label = { Text("Doğru") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = localWrong,
                            onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() }) { localWrong = it; viewModel.setWrong(it) } },
                            modifier = Modifier.weight(1f),
                            label = { Text("Yanlış") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                        OutlinedTextField(
                            value = localBlank,
                            onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() }) { localBlank = it; viewModel.setBlank(it) } },
                            modifier = Modifier.weight(1f),
                            label = { Text("Boş") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
                        )
                    }

                    val net = viewModel.calculateNet()
                    if (net != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Text(
                                text = "Net: ${String.format("%.2f", net)}",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.setEditingRecord(null) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("İptal")
                        }
                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.setSubject(localSubject)
                                viewModel.setTotal(localTotal)
                                viewModel.setCorrect(localCorrect)
                                viewModel.setWrong(localWrong)
                                viewModel.setBlank(localBlank)
                                viewModel.updateRecord()
                            },
                            modifier = Modifier.weight(1f),
                            enabled = localSubject.isNotBlank() && localTotal.isNotBlank() && localCorrect.isNotBlank() && localWrong.isNotBlank()
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Kaydet")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionRecordItem(
    record: com.derspusulasi.data.model.QuestionRecord,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = record.subject,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = record.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoChip("Toplam: ${record.total}", MaterialTheme.colorScheme.primary)
                    InfoChip("✓ ${record.correct}", Color(0xFF4CAF50))
                    InfoChip("✗ ${record.wrong}", MaterialTheme.colorScheme.error)
                    if (record.blank > 0) {
                        InfoChip("○ ${record.blank}", MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Net: ${String.format("%.2f", record.net)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Box {
                    IconButton(onClick = { showMenu = true }, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menü", modifier = Modifier.size(20.dp))
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Düzenle") },
                            leadingIcon = { Icon(Icons.Default.Edit, null) },
                            onClick = {
                                showMenu = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Sil") },
                            leadingIcon = { Icon(Icons.Default.Delete, null) },
                            onClick = {
                                showMenu = false
                                onDelete()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoChip(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StatisticsContent(stats: com.derspusulasi.ui.viewmodel.QuestionStatistics) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Overall Statistics
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatItem("Toplam Kayıt", stats.totalRecords.toString())
            StatItem("Toplam Net", String.format("%.2f", stats.totalNet))
            StatItem("Ortalama Net", String.format("%.2f", stats.averageNet))
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatItem("Toplam Soru", stats.totalQuestions.toString())
            StatItem("Doğru", stats.totalCorrect.toString(), Color(0xFF4CAF50))
            StatItem("Yanlış", stats.totalWrong.toString(), MaterialTheme.colorScheme.error)
        }
        
        // Subject Statistics
        if (stats.subjectStatistics.isNotEmpty()) {
            Divider()
            Text(
                text = "Ders Bazlı İstatistikler",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            stats.subjectStatistics.entries.sortedByDescending { it.value.averageNet }.forEach { (subject, subStats) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = subject,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${subStats.count} kayıt • Ort. Net: ${String.format("%.2f", subStats.averageNet)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, valueColor: Color = MaterialTheme.colorScheme.primary) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black
        )
    }
}
