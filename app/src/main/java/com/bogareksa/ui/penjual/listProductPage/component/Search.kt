package com.bogareksa.ui.penjual.listProductPage.component


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchItemSeller(modifier: Modifier = Modifier,deleteText : () -> Unit,query : String,onQueryChange : (String) -> Unit) {
    Row (
        modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 0.dp)
        ,verticalAlignment = Alignment.CenterVertically){

        androidx.compose.material3.SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {},
            active = false,
            onActiveChange = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            placeholder = {
                Text("cari barang")
            },
            shape = MaterialTheme.shapes.large,
            modifier = modifier
                .padding(end = 10.dp)
                .width(300.dp)
                .heightIn(min = 48.dp)
        ) {
        }
//        Spacer(modifier = modifier.weight(1f))
//        IconButton(
//
//            onClick = {
//
//        },
//        ) {
//            Icon(imageVector = Icons.Default.Clear, contentDescription ="delete" )
//        }
    }

}
