package com.example.video_game_collections.Screens


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.video_game_collections.allViewModels.ordersCustomerSideViewModel
import com.example.video_game_collections.allViewModels.ordersSellerSideViewModel
import com.example.video_game_collections.dataModels.OrderStatus
import com.example.video_game_collections.dataModels.productOrderModel
import kotlinx.serialization.Contextual
import productOrderModelsToListOfMaps

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun displayProductsInCurrentOrderForSeller(
    ordersSellerSideViewModel:ordersSellerSideViewModel,
    ordersCustomerSideViewModel: ordersCustomerSideViewModel,
    totalOrderCost:String,
    orderID: String,
    buyerID: String,
) {

    var observedDisplayProductsInCurrentOrderListState = ordersCustomerSideViewModel.displayProductsInCurrentOrderListState.observeAsState(
        emptyList<Map<String,Any>>()
    )





    val context = LocalContext.current



    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally


        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth()
            ) {

                items(observedDisplayProductsInCurrentOrderListState.value){
                    Log.i("displayProductsInCurrentOrderForSeller","invoked displayProductsInCurrentOrderForSeller")

                    Card(

                        modifier = Modifier.padding(8.dp),
                        colors = if(it["status"] == OrderStatus.ACCEPTED ||it["status"] == "ACCEPTED"){
                            CardDefaults.cardColors(containerColor = Color.Green)
                        }else if(it["status"] == OrderStatus.REJECTED||it["status"] == "REJECTED"){
                            CardDefaults.cardColors(containerColor = Color.Red)
                        }else if(it["status"] == OrderStatus.PENDING||it["status"] == "PENDING"){ // if pending
                            CardDefaults.cardColors(containerColor = Color.LightGray)
                        }else{
                            CardDefaults.cardColors(containerColor = Color.Cyan)
                        }

                    ) {

                        Row() {
                            AsyncImage(
                                model = it["imageURL"],
                                contentDescription = null,
                                modifier = Modifier
                                    .height(150.dp)
                                    .width(200.dp),
                                contentScale = ContentScale.Crop

                            )
                            Column {
                                Text(text = "Name: "+ it["pname"] )
                                Text(text = "Cost: "+it["pcost"] )
                                Text(text = "Quantity: "+it["quantity"] )
                                Text(text = "TotalCost: "+it["totalProductCost"] )
                                Text(text = "status "+it["status"])

                                Row {
                                    IconButton(onClick = {

                                        ordersCustomerSideViewModel.changeStateToAcceptedOrRejected(
                                            orderID = orderID,
                                            pID =  it["pid"].toString(),
                                            status = OrderStatus.ACCEPTED,
                                            userID =buyerID,
                                            context = context,
                                            productList = observedDisplayProductsInCurrentOrderListState.value as MutableList<Map<String, Any>>




                                        )

                                    }) {
                                            Icon(
                                                imageVector = Icons.Default.Done,
                                                contentDescription = null
                                            )
                                    }

                                    IconButton(onClick = {
                                        ordersCustomerSideViewModel.changeStateToAcceptedOrRejected(
                                            orderID = orderID,
                                            pID =  it["pid"].toString(),
                                            status = OrderStatus.REJECTED,
                                            userID =buyerID,
                                            context = context,
                                            productList = observedDisplayProductsInCurrentOrderListState.value as MutableList<Map<String, Any>>

                                        )

                                    }) {

                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = null
                                        )
                                    }
                                }

                            }
                        }


                    }


                }

            }

        }


        Text(text = "Total Cost of the order "+totalOrderCost.toString())




    }

}