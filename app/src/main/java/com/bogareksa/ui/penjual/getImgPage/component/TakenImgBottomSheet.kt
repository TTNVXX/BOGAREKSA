package com.bogareksa.ui.penjual.getImgPage.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakeImgBottomSheet(
    getListImg : List<Bitmap>
    ,modifier: Modifier = Modifier,
    isDisplay : Boolean
){

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false)}
    val scaffoldState = rememberBottomSheetScaffoldState()


//    if(isDisplay){
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = showBottomSheet
            },
            sheetState = sheetState
        ) {
            // Sheet content
            if(getListImg.isEmpty()){
                Text(text = "don't have any image")
            }else{
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(140.dp),
                    contentPadding = PaddingValues(15.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(17.dp),
                    content = {

                        items(
                            count = getListImg.size,
                            itemContent = {idx ->
                                val convertImg: ImageBitmap = remember(getListImg[idx].hashCode()) { getListImg[idx].asImageBitmap() }

                                Image(bitmap = convertImg, contentDescription ="images cam" )
                            }
                        )
                    }
                )

//            }
        }
    }

}