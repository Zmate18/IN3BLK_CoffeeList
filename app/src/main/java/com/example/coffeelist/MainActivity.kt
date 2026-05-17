package com.example.coffeelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coffeelist.data.CoffeeItem
import com.example.coffeelist.ui.theme.CardBeige
import com.example.coffeelist.ui.theme.CoffeeListTheme
import com.example.coffeelist.ui.theme.DarkCoffeeText
import com.example.coffeelist.ui.theme.HeaderBrown
import com.example.coffeelist.viewmodel.CoffeeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeListTheme {
                CoffeeListScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeListScreen(viewModel: CoffeeViewModel = viewModel()) {
    val items by viewModel.allItems.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<CoffeeItem?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Coffee Shopping List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HeaderBrown,
                    titleContentColor = Color.White,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    itemToEdit = null
                    showDialog = true
                },
                containerColor = HeaderBrown,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Coffee")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (items.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No coffee items yet. Tap + to add one!")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(items) { item ->
                        CoffeeItemRow(
                            item = item,
                            onEdit = {
                                itemToEdit = item
                                showDialog = true
                            },
                            onDelete = { viewModel.deleteItem(item) }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        CoffeeDialog(
            item = itemToEdit,
            onDismiss = { showDialog = false },
            onConfirm = { name, type, price ->
                if (itemToEdit == null) {
                    viewModel.addItem(name, type, price)
                } else {
                    viewModel.updateItem(itemToEdit!!.copy(name = name, type = type, price = price))
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun CoffeeItemRow(
    item: CoffeeItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBeige,
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkCoffeeText
                )
                Text(
                    text = "${item.type} - ${item.price} ft",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkCoffeeText.copy(alpha = 0.8f)
                )
            }
            IconButton(onClick = onEdit) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = DarkCoffeeText
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = DarkCoffeeText
                )
            }
        }
    }
}

@Composable
fun CoffeeDialog(
    item: CoffeeItem?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Double) -> Unit
) {
    var name by remember { mutableStateOf(item?.name ?: "") }
    var type by remember { mutableStateOf(item?.type ?: "") }
    var price by remember { mutableStateOf(item?.price?.toString() ?: "") }

    val textFieldColors = TextFieldDefaults.colors(
        focusedTextColor = DarkCoffeeText,
        unfocusedTextColor = DarkCoffeeText,
        focusedLabelColor = DarkCoffeeText,
        unfocusedLabelColor = DarkCoffeeText.copy(alpha = 0.7f),
        cursorColor = DarkCoffeeText,
        focusedIndicatorColor = DarkCoffeeText,
        unfocusedIndicatorColor = DarkCoffeeText.copy(alpha = 0.5f),
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item == null) "Add Coffee" else "Edit Coffee", color = DarkCoffeeText) },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = textFieldColors
                )
                TextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Type (e.g. Latte)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = textFieldColors
                )
                TextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price (ft)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val priceDouble = price.toDoubleOrNull() ?: 0.0
                    onConfirm(name, type, priceDouble)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = HeaderBrown,
                    contentColor = Color.White
                )
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = DarkCoffeeText)
            }
        }
    )
}
